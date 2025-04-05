# app/config.py

import os

# ตัวอย่าง configuration: path ไปยัง Tesseract executable (สำหรับ Windows)
# หากใช้งานใน Linux โดยทั่วไปจะถูกติดตั้งไว้ที่ /usr/bin/tesseract
TESSERACT_CMD = os.getenv("TESSERACT_CMD", "tesseract")
