# OCR Service (Machine Learning OCR)

บริการ OCR ที่ใช้ Machine Learning โดยใช้ FastAPI, EasyOCR และ OpenCV-Python สำหรับประมวลผลและอ่านข้อมูลจากเอกสาร เช่น Invoice หรือเลขไมล์

## Prerequisites

- Python 3.10+ (แนะนำให้ใช้ 3.11)
- ติดตั้ง [Tesseract OCR](https://github.com/tesseract-ocr/tesseract) (ถ้าต้องการใช้งาน pytesseract เพิ่มเติม)
- หรือติดตั้ง libgl, libglib2.0 สำหรับ OpenCV และ EasyOCR (ถ้ารันผ่าน Dockerfile จะจัดการให้)
- (ถ้ารันแบบ local) ติดตั้ง pipenv หรือ virtualenv เพื่อแยกสภาพแวดล้อม

## Build and Run (Local)

1. สร้าง virtual environment และติดตั้ง dependencies:
   ```bash
   pip install -r requirements.txt
   
2. รันเซิร์ฟเวอร์ FastAPI:
   ```bash
   uvicorn app.main:app --host 0.0.0.0 --port 8000 --reload

3. เข้าใช้งาน API ผ่าน Swagger UI ที่:
   ```bash
   http://localhost:8000/docs

## Build and Run (Docker)

1. สร้าง Docker image:
   ```bash
   docker build -t ocr-service .

2. รัน container:
   ```bash
   docker run -p 8000:8000 ocr-service

3. เข้าใช้งาน API ผ่าน Swagger UI ที่:
   ```bash
   http://localhost:8000/docs

## Usage

- Endpoint `/ocr` (POST) สำหรับอัปโหลดไฟล์รูปภาพ (multipart/form-data) และรับข้อความที่ OCR อ่านได้
- ตัวอย่างการเรียกด้วย `curl`:
  ```bash
  curl -X POST -F "file=@path_to_image.jpg" http://localhost:8000/ocr

## License

This project is licensed under the MIT License.