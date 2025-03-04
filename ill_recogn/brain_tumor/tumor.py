import keras
import tensorflow as tf
from keras.src.legacy.preprocessing.image import ImageDataGenerator
from keras.src.losses import BinaryCrossentropy
from keras.src.optimizers import Adam
from keras.src.layers import MaxPooling2D

gen = ImageDataGenerator(rescale=1./255,validation_split = 0.2,zoom_range=(0.99,0.99))

train = gen.flow_from_directory("./Brain Tumor Data Set/Brain Tumor Data Set/",
                               target_size = (150,150),
                               batch_size = 256,
                               class_mode = "binary",
                               color_mode = "rgb",
                               shuffle = True,
                               seed = 123,
                               subset = "training")

val = gen.flow_from_directory("./Brain Tumor Data Set/Brain Tumor Data Set/",
                               target_size = (150,150),
                               batch_size = 8,
                               class_mode = "binary",
                               color_mode = "rgb",
                               shuffle = True,
                               seed = 123,
                               subset = "validation")
classes = val.class_indices

import seaborn as sns

t = 0
h = 0
for i in range(15):
    a, b = next(train)
    for j in b:
        if j == 1:
            h += 1
        else:
            t += 1

sns.barplot(x=['tumor','healty'],y=[t,h])

import matplotlib.pyplot as plt
batch = next(train)

plt.imshow(batch[0][0])

from keras.src.layers import Conv2D, LeakyReLU, BatchNormalization, Dropout, Dense, InputLayer, Flatten


model = keras.Sequential()
model.add(InputLayer(input_shape=(150,150,3)))
model.add(Conv2D(filters=32,kernel_size=3, activation="relu", padding="same"))
model.add(MaxPooling2D())
model.add(Conv2D(filters=64,kernel_size=3, activation="relu", padding="same"))
model.add(MaxPooling2D())


model.add(Flatten())


model.add(Dense(128, activation='relu'))
model.add(BatchNormalization())
model.add(Dropout(rate=0.3))
model.add(Dense(64, activation="relu"))
model.add(BatchNormalization())
model.add(Dropout(rate=0.3))
model.add(Dense(1, activation="sigmoid"))


model.compile(optimizer=Adam(0.001),loss = BinaryCrossentropy(),metrics=['accuracy'])

model.summary()

tf.keras.utils.plot_model(
    model, to_file='model.png', show_shapes=True,
    show_layer_names=True,
)

from keras import callbacks, Sequential

earlystopping = callbacks.EarlyStopping(monitor="val_loss", mode="min",
                                        patience=5, restore_best_weights = True)
# Обучение модели
model.fit(train, validation_data=val, epochs=1, callbacks=[earlystopping])
model.save("brain_tumor_model.h5")


# history = model.fit(train, validation_data=val, epochs=1, callbacks=[earlystopping])
#
# # Визуализация точности и потерь
# plt.plot(history.history['accuracy'], label='accuracy')
# plt.plot(history.history['val_accuracy'], label='val_accuracy')
# plt.xlabel('Epoch')
# plt.ylabel('Accuracy')
# plt.ylim([0, 1])
# plt.legend(loc='lower right')
# plt.show()
#
# plt.plot(history.history['loss'], label='loss')
# plt.plot(history.history['val_loss'], label='val_loss')
# plt.xlabel('Epoch')
# plt.ylabel('Loss')
# plt.ylim([0, 1])
# plt.legend(loc='lower right')
# plt.show()

