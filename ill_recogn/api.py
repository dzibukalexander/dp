from fastapi import FastAPI, File, UploadFile
from keras._tf_keras.keras.models import load_model
from keras._tf_keras.keras.preprocessing import image
import numpy as np
import uvicorn
import io

app = FastAPI()

# Загрузка модели
brain_tumor_model = load_model("./brain_tumor/brain_tumor_model.h5")

@app.post("/predict/")
async def predict(file: UploadFile = File(...)):
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

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)