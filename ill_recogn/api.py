# from fastapi import FastAPI, File, UploadFile
# from keras._tf_keras.keras.models import load_model
# from keras._tf_keras.keras.preprocessing import image
# import numpy as np
# import uvicorn
# import io
#
# app = FastAPI()
#
# Загрузка модели
# brain_tumor_model = load_model("./brain_tumor/brain_tumor_model.h5")
#
# @app.post("/brainTumorModel/predict/")
# async def predict(file: UploadFile = File(...)):
#     # Чтение изображения
#     contents = await file.read()
#     img = image.load_img(io.BytesIO(contents), target_size=(150, 150))
#     img_array = image.img_to_array(img)
#     img_array = np.expand_dims(img_array, axis=0)
#     img_array /= 255.0
#
#     # Предсказание
#     prediction = brain_tumor_model.predict(img_array)
#     result = "Tumor" if prediction[0][0] > 0.5 else "Healthy"
#
#     return {"result": result}
#
# if __name__ == "__main__":
#     uvicorn.run(app, host="127.0.0.1", port=2000)


from fastapi import FastAPI, File, UploadFile
from pydantic import BaseModel
from keras._tf_keras.keras.models import load_model
from keras._tf_keras.keras.preprocessing import image
import numpy as np
import uvicorn
import requests
import io

class ModelInfo(BaseModel):
    name: str
    description: str
    input_type: str
    output_type: str
    endpoint: str

app = FastAPI()

# Конфигурация модели

MODEL_CONFIG = {
    "name": "Brain Tumor Detection",
    "description": "Detects brain tumors from MRI images",
    "inputType": "image",
    "outputType": "text",
    "endpoint": "brainTumorModel/predict/",
    "kaggleURL": "Asd"
    # "status": "ACTIVE",
    # "is_default": "true"
}
@app.on_event("startup")
async def register_model():
    # Регистрация модели в сервисе Discovery
    registry_url = "http://localhost:8080/api/ml/register"
    try:
        response = requests.post(registry_url, json=MODEL_CONFIG)
        response.raise_for_status()
        print("Model registered successfully")
    except Exception as e:
        print(f"Failed to register model: {str(e)}")


# Загрузка модели
brain_tumor_model = load_model("./brain_tumor/brain_tumor_model.h5")
@app.post(MODEL_CONFIG["endpoint"])
async def predict(file: UploadFile = File(...)):
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

    return {"result": result}
    # return {"result": "Tumor", "confidence": 0.95}

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=2000)