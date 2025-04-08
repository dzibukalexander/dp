import os
import time
from glob import glob
from tqdm import tqdm

import cv2
import numpy as np
import pandas as pd
import matplotlib.patches as mpatches
import matplotlib.pyplot as plt
import albumentations as A
import segmentation_models_pytorch as smp
from sklearn.impute import SimpleImputer
from sklearn.model_selection import train_test_split
from scipy.ndimage import binary_dilation


import torch
from torch.optim import Adam
from torch.optim.lr_scheduler import ReduceLROnPlateau
from torch.utils.data import Dataset, DataLoader
from torchvision import transforms as T



device = torch.device("cuda" if torch.cuda.is_available() else "cpu")

# 1. Уменьшаем размер батча
batch_size = 8  # было 16

# 2. Используем более легкую модель
model = smp.Unet(
    encoder_name="efficientnet-b0",  # было efficientnet-b7 (более легкая версия)
    encoder_weights="imagenet",
    in_channels=3,
    classes=1,
    activation='sigmoid',
)

# 3. Уменьшаем количество данных для обучения (возьмем подвыборку)
files_dir = './kaggle_3m/'
file_paths = glob(f'{files_dir}/*/*[0-9].tif')[:500]  # ограничиваем количество файлов

csv_path = './kaggle_3m/data.csv'
df = pd.read_csv(csv_path)


def get_file_row(path):
    path_no_ext, ext = os.path.splitext(path)
    filename = os.path.basename(path)
    patient_id = '_'.join(filename.split('_')[:3])
    return [patient_id, path, f'{path_no_ext}_mask{ext}']


filenames_df = pd.DataFrame((get_file_row(filename) for filename in file_paths),
                            columns=['Patient', 'image_filename', 'mask_filename'])

df = pd.merge(df, filenames_df, on="Patient")

# 4. Уменьшаем размер тестовой и валидационной выборки
train_df, test_df = train_test_split(df, test_size=0.2)  # было 0.3
test_df, valid_df = train_test_split(test_df, test_size=0.5)

# 5. Упрощаем аугментации
transform = A.Compose([
    A.RandomBrightnessContrast(p=0.2),  # было 0.3 и больше преобразований
])


def iou_pytorch(predictions: torch.Tensor, labels: torch.Tensor, e: float = 1e-7):
    """Calculates Intersection over Union for a tensor of predictions"""
    predictions = torch.where(predictions > 0.5, 1, 0)
    labels = labels.byte()

    intersection = (predictions & labels).float().sum((1, 2))
    union = (predictions | labels).float().sum((1, 2))

    iou = (intersection + e) / (union + e)
    return iou


def dice_pytorch(predictions: torch.Tensor, labels: torch.Tensor, e: float = 1e-7):
    """Calculates Dice coefficient for a tensor of predictions"""
    predictions = torch.where(predictions > 0.5, 1, 0)
    labels = labels.byte()

    intersection = (predictions & labels).float().sum((1, 2))
    return ((2 * intersection) + e) / (predictions.float().sum((1, 2)) + labels.float().sum((1, 2)) + e)


class MriDataset(Dataset):
    def __init__(self, df, transform=None, mean=0.5, std=0.25):
        super(MriDataset, self).__init__()
        self.df = df
        self.transform = transform
        self.mean = mean
        self.std = std

    def __len__(self):
        return len(self.df)

    def __getitem__(self, idx, raw=False):
        row = self.df.iloc[idx]
        img = cv2.imread(row['image_filename'], cv2.IMREAD_UNCHANGED)
        mask = cv2.imread(row['mask_filename'], cv2.IMREAD_GRAYSCALE)

        if raw:
            return img, mask

        if self.transform:
            augmented = self.transform(image=img, mask=mask)
            image, mask = augmented['image'], augmented['mask']

        img = T.functional.to_tensor(img)
        mask = mask // 255
        mask = torch.Tensor(mask)
        return img, mask


train_dataset = MriDataset(train_df, transform)
valid_dataset = MriDataset(valid_df)
test_dataset = MriDataset(test_df)

# train_loader = DataLoader(train_dataset, batch_size=batch_size, shuffle=True, num_workers=2)  # добавили workers
# valid_loader = DataLoader(valid_dataset, batch_size=batch_size, shuffle=True, num_workers=2)
# test_loader = DataLoader(test_dataset, batch_size=1, num_workers=2)


# 6. Упрощаем функцию потерь
def BCE_dice(output, target, alpha=0.1):  # увеличили alpha для ускорения сходимости
    bce = torch.nn.functional.binary_cross_entropy(output, target)
    return bce  # убрали dice для упрощения


# 7. Увеличиваем learning rate для более быстрого обучения
optimizer = Adam(model.parameters(), lr=0.01)  # было 0.001
epochs = 1
lr_scheduler = ReduceLROnPlateau(optimizer=optimizer, patience=2, factor=0.2)


# Остальной код оставляем без изменений
# [остальные функции и training_loop остаются такими же]

class EarlyStopping():
    """
    Stops training when loss stops decreasing in a PyTorch module.
    """
    def __init__(self, patience:int = 6, min_delta: float = 0, weights_path: str = 'weights.pt'):
        """
        :param patience: number of epochs of non-decreasing loss before stopping
        :param min_delta: minimum difference between best and new loss that is considered
            an improvement
        :paran weights_path: Path to the file that should store the model's weights
        """
        self.patience = patience
        self.min_delta = min_delta
        self.counter = 0
        self.best_loss = float('inf')
        self.weights_path = weights_path

    def __call__(self, val_loss: float, model: torch.nn.Module):
        if self.best_loss - val_loss > self.min_delta:
            self.best_loss = val_loss
            torch.save(model.state_dict(), self.weights_path)
            self.counter = 0
        else:
            self.counter += 1
            if self.counter >= self.patience:
                return True
        return False

    def load_weights(self, model: torch.nn.Module):
        """
        Loads weights of the best model.
        :param model: model to which the weigths should be loaded
        """
        return model.load_state_dict(torch.load(self.weights_path))

def training_loop(epochs, model, train_loader, valid_loader, optimizer, loss_fn, lr_scheduler):
    history = {'train_loss': [], 'val_loss': [], 'val_IoU': [], 'val_dice': []}
    early_stopping = EarlyStopping(patience=7)

    for epoch in range(1, epochs + 1):
        start_time = time.time()

        running_loss = 0
        model.train()
        for i, data in enumerate(tqdm(train_loader)):
            img, mask = data
            img, mask = img.to(device), mask.to(device)
            predictions = model(img)
            predictions = predictions.squeeze(1)
            loss = loss_fn(predictions, mask)
            running_loss += loss.item() * img.size(0)
            loss.backward()
            optimizer.step()
            optimizer.zero_grad()

        model.eval()
        with torch.no_grad():
            running_IoU = 0
            running_dice = 0
            running_valid_loss = 0
            for i, data in enumerate(valid_loader):
                img, mask = data
                img, mask = img.to(device), mask.to(device)
                predictions = model(img)
                predictions = predictions.squeeze(1)
                running_dice += dice_pytorch(predictions, mask).sum().item()
                running_IoU += iou_pytorch(predictions, mask).sum().item()
                loss = loss_fn(predictions, mask)
                running_valid_loss += loss.item() * img.size(0)
        train_loss = running_loss / len(train_loader.dataset)
        val_loss = running_valid_loss / len(valid_loader.dataset)
        val_dice = running_dice / len(valid_loader.dataset)
        val_IoU = running_IoU / len(valid_loader.dataset)

        history['train_loss'].append(train_loss)
        history['val_loss'].append(val_loss)
        history['val_IoU'].append(val_IoU)
        history['val_dice'].append(val_dice)
        print(
            f'Epoch: {epoch}/{epochs} | Training loss: {train_loss} | Validation loss: {val_loss} | Validation Mean IoU: {val_IoU} '
            f'| Validation Dice coefficient: {val_dice}')

        lr_scheduler.step(val_loss)
        if early_stopping(val_loss, model):
            early_stopping.load_weights(model)
            break
    model.eval()
    return history

# loss_fn = BCE_dice
# optimizer = Adam(model.parameters(), lr=0.001)
# epochs = 1
# lr_scheduler = ReduceLROnPlateau(optimizer=optimizer, patience=2,factor=0.2)
#
# history = training_loop(epochs, model, train_loader, valid_loader, optimizer, loss_fn, lr_scheduler)
#

if __name__ == '__main__':
    # Для Windows лучше уменьшить или обнулить num_workers
    train_loader = DataLoader(train_dataset, batch_size=batch_size, shuffle=True, num_workers=0)
    valid_loader = DataLoader(valid_dataset, batch_size=batch_size, shuffle=True, num_workers=0)
    test_loader = DataLoader(test_dataset, batch_size=1, num_workers=0)

    # Инициализация модели и перенос на устройство
    model = model.to(device)

    # Инициализация оптимизатора и других компонентов
    loss_fn = BCE_dice
    optimizer = Adam(model.parameters(), lr=0.01)  # Используем увеличенный lr
    lr_scheduler = ReduceLROnPlateau(optimizer=optimizer, patience=2, factor=0.2)

    # Запуск обучения
    history = training_loop(epochs=1,
                            model=model,
                            train_loader=train_loader,
                            valid_loader=valid_loader,
                            optimizer=optimizer,
                            loss_fn=loss_fn,
                            lr_scheduler=lr_scheduler)

    # Сохраняем модель сегментации
    import torch.jit

    # Переводим модель в режим eval
    model.eval()
    torch.save({
        'model_state_dict': model.state_dict(),
        'encoder_name': 'efficientnet-b0',
        'classes': 1,
        'input_size': (3, 256, 256)
    }, "brain_tumor_segmentation_model.pth")
    print("Model weights saved as brain_tumor_segmentation_model.pth")