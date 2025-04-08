import base64
import time

import cv2
from keras._tf_keras.keras.models import load_model
from keras._tf_keras.keras.preprocessing import image
from fastapi import FastAPI, File, UploadFile
from pydantic import BaseModel
import torch
import torchvision.transforms as transforms
from PIL import Image
import numpy as np
import uvicorn
import io
import segmentation_models_pytorch as smp

class ModelInfo(BaseModel):
    name: str
    description: str
    input_type: str
    output_type: str
    endpoint: str

app = FastAPI()

# Конфигурации моделей
MODEL_CONFIGS = {
    "classification": {
        "name": "Brain Tumor Classification",
        "description": "Classifies brain MRI images as tumor or healthy",
        "inputType": "image",
        "outputType": "text",
        "endpoint": "/brainTumorModel/predict/",
        "model_path": "./brain_tumor/brain_tumor_model.h5"
    },
    "segmentation": {
        "name": "Brain Tumor Segmentation",
        "description": "Segments tumor regions in brain MRI images",
        "inputType": "image",
        "outputType": "image",
        "endpoint": "/brainTumorSegmentation/predict/",
        "model_path": "./brain_tumor_segment/brain_tumor_segmentation_model.pth"
    }
}

# Загрузка моделей
classification_model = load_model(MODEL_CONFIGS["classification"]["model_path"])

# Трансформации для сегментации
seg_transform = transforms.Compose([
    transforms.Resize((256, 256)),  # Используйте ваш размер
    transforms.ToTensor(),
    transforms.Normalize(mean=[0.5, 0.5, 0.5], std=[0.25, 0.25, 0.25])
])


# @app.on_event("startup")
# async def register_model():
#     registry_url = "http://localhost:8080/api/ml/register"
#     try:
#         response = requests.post(registry_url, json=MODEL_CONFIG)
#         response.raise_for_status()
#         print("Model registered successfully")
#     except Exception as e:
#         print(f"Failed to register model: {str(e)}")


# Загрузка модели
brain_tumor_model = load_model("./brain_tumor/brain_tumor_model.h5")
@app.post(MODEL_CONFIGS["classification"]["endpoint"])
async def classify_tumor(file: UploadFile = File(...)):
    # Реализация предсказания
    # Чтение изображения
    contents = await file.read()
    img = image.load_img(io.BytesIO(contents), target_size=(150, 150))
    img_array = image.img_to_array(img)
    img_array = np.expand_dims(img_array, axis=0)
    img_array /= 255.0

    # Предсказание
    prediction = brain_tumor_model.predict(img_array)
    result = "Tumor" if prediction[0][0] > 0.5 else "Healthy"

    time.sleep(15)

    return {"result": result}


def load_segmentation_model():
    # Загружаем с weights_only=False для совместимости
    checkpoint = torch.load(
        MODEL_CONFIGS["segmentation"]["model_path"],
        map_location=torch.device('cpu'),  # Загружаем на CPU
        weights_only=False  # Отключаем ограничение для загрузки
    )

    model = smp.Unet(
        encoder_name=checkpoint['encoder_name'],
        encoder_weights=None,
        in_channels=3,
        classes=checkpoint['classes'],
        activation='sigmoid'
    )

    # Убедимся, что ключи состояния совместимы
    state_dict = checkpoint['model_state_dict']

    # Удалим префикс 'module.' если модель была сохранена в DataParallel
    new_state_dict = {}
    for k, v in state_dict.items():
        name = k.replace('module.', '')  # Удаляем 'module.' если есть
        new_state_dict[name] = v

    model.load_state_dict(new_state_dict)
    model.eval()
    return model

# Загрузка модели при старте приложения
try:
    segmentation_model = load_segmentation_model()
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    segmentation_model = segmentation_model.to(device)
    print("Segmentation model loaded successfully")
except Exception as e:
    print(f"Failed to load segmentation model: {str(e)}")
    segmentation_model = None


@app.post(MODEL_CONFIGS["segmentation"]["endpoint"])
async def segment_tumor(file: UploadFile = File(...)):
    if segmentation_model is None:
        return {"error": "Segmentation model not loaded"}

    try:
        # Чтение изображения
        contents = await file.read()
        original_img = Image.open(io.BytesIO(contents)).convert('RGB')

        # Сохраняем оригинальный размер для последующего масштабирования
        original_size = original_img.size

        # Преобразование для модели
        transform = transforms.Compose([
            transforms.Resize((256, 256)),
            transforms.ToTensor(),
            transforms.Normalize(mean=[0.5, 0.5, 0.5], std=[0.25, 0.25, 0.25])
        ])

        img_tensor = transform(original_img).unsqueeze(0).to(device)

        # Предсказание
        with torch.no_grad():
            output = segmentation_model(img_tensor)
            mask = (torch.sigmoid(output) > 0.5).float()

        # Преобразуем маску в numpy array и изменяем размер до оригинального
        mask_np = mask.squeeze().cpu().numpy()
        mask_img = Image.fromarray((mask_np * 255).astype('uint8'))
        mask_img = mask_img.resize(original_size, Image.BILINEAR)
        mask_np = np.array(mask_img) > 128  # Бинаризуем с порогом 128

        # Создаем наложение маски на оригинальное изображение
        original_np = np.array(original_img)

        # Создаем красное наложение (можно изменить цвет)
        overlay = np.zeros_like(original_np)
        overlay[mask_np] = [255, 0, 0]  # Красный цвет для маски

        # Смешиваем оригинал и наложение с прозрачностью
        alpha = 0.3  # Прозрачность наложения (0-1)
        result_img = cv2.addWeighted(original_np, 1, overlay, alpha, 0)

        # Конвертируем обратно в PIL Image
        result_img = Image.fromarray(result_img)

        # Возвращаем результат как PNG
        buf = io.BytesIO()
        result_img.save(buf, format='PNG')
        buf.seek(0)

        # Конвертируем результат в base64
        buf = io.BytesIO()
        result_img.save(buf, format='PNG')
        image_bytes = buf.getvalue()

        base64_image = base64.b64encode(image_bytes).decode('utf-8')
        time.sleep(8)
        return {
            "result": "Segmentation completed",  # Текстовый результат
            "photoData": base64_image,  # Изображение в base64
            "contentType": "image/png"  # Тип контента
        }

    except Exception as e:
        return {"error": f"Error processing image: {str(e)}"}


if __name__ == "__main__":
    uvicorn.run(app, host="localhost", port=3333)