import os
import io
import tensorflow as tf
from tensorflow import keras
import numpy as np
from PIL import Image

from flask import Flask, request, jsonify

app = Flask(__name__)

# Mematikan pesan log TensorFlow yang tidak perlu
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'

# Memuat model klasifikasi jenis sayuran
model = keras.models.load_model("model1.h5")

# Dictionary label untuk jenis sayuran
labels = {
    0: 'fresh_cabbage',
    1: 'fresh_carrot',
    2: 'fresh_cucumber',
    3: 'fresh_okra',
    4: 'fresh_pepper',
    5: 'fresh_potato',
    6: 'fresh_tomato',
    7: 'rotten_cabbage',
    8: 'rotten_carrot',
    9: 'rotten_cucumber',
    10: 'rotten_okra',
    11: 'rotten_pepper',
    12: 'rotten_potato',
    13: 'rotten_tomato'
}

# Fungsi untuk mengubah gambar menjadi tensor yang cocok untuk model
def transform_image(pillow_image):
    # Konversi gambar grayscale ke RGB
    pillow_image_rgb = pillow_image.convert('RGB')
    data = np.asarray(pillow_image_rgb)
    data = data / 255.0 
    data = data[np.newaxis, ...]
    data = tf.image.resize(data, [640, 640])
    return data

# Fungsi untuk mengubah gambar menjadi tensor yang cocok untuk model kesegaran
def transform_image_for_freshness(pillow_image):
    # Konversi gambar grayscale ke RGB
    pillow_image_rgb = pillow_image.convert('RGB')
    data = np.asarray(pillow_image_rgb)
    data = data / 255.0 
    data = tf.image.resize(data, [128, 128])  # Mengubah ukuran gambar menjadi 128x128
    return data

# Fungsi untuk melakukan prediksi jenis sayuran
def predict_classification(x):
    predictions = model(x)
    predictions = tf.nn.softmax(predictions)
    label_id = np.argmax(predictions)
    label = labels[label_id]
    return label

# Fungsi untuk mengekstrak informasi kesegaran dari label
def get_freshness(label):
    try:
        # Menggunakan pemisahan string untuk mendapatkan kata pertama dari label
        first_word = label.split("_")[0]

        # Memeriksa apakah kata pertama adalah 'fresh' atau 'rotten'
        if first_word == 'fresh':
            return 'Fresh'
        elif first_word == 'rotten':
            return 'Rotten'
        else:
            return 'Unknown'  # Jika label tidak sesuai dengan format yang diharapkan
    except Exception as e:
        return 'Unknown'  # Menangani kasus ketika label tidak dapat dipecahkan

# Endpoint untuk melakukan pemindaian gambar
# Endpoint untuk melakukan pemindaian gambar
@app.route("/scan", methods=["POST"])
def index():
    file = request.files.get('file')
    if file is None or file.filename == "":
        return jsonify({"error": "No file uploaded"})

    try:
        image_bytes = file.read()
        pillow_img = Image.open(io.BytesIO(image_bytes)).convert('L')
        
        # Transformasi gambar untuk model klasifikasi jenis sayuran
        tensor_classification = transform_image(pillow_img)
        
        # Transformasi gambar untuk model klasifikasi kesegaran
        tensor_freshness = transform_image_for_freshness(pillow_img)
        
        # Melakukan prediksi
        prediction = predict_classification(tensor_classification)
        freshness_info = get_freshness(prediction)
        
        # Menyiapkan data respons
        data = {
            "prediction": prediction,
            "freshness_info": freshness_info,
        }
        return jsonify({"data": data})
    except Exception as e:
        return jsonify({"error": str(e)})

if __name__ == "__main__":
    app.run(debug=True)
