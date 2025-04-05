# app/ocr.py

import cv2
import easyocr
from app.config import EASYOCR_LANGUAGES
from app.exceptions import OCRServiceException

# สร้าง reader instance (สามารถกำหนด gpu=False ถ้าไม่มี GPU)
reader = easyocr.Reader([EASYOCR_LANGUAGES], gpu=False)

def perform_ocr(image_path: str) -> str:
    """
    ทำ OCR บนรูปภาพที่ระบุด้วย path
    โดยใช้ OpenCV สำหรับ pre-processing และ EasyOCR สำหรับดึงข้อความ
    """
    try:
        # อ่านรูปด้วย OpenCV
        image = cv2.imread(image_path)
        if image is None:
            raise OCRServiceException("Unable to read image file.")

        # ปรับขนาดรูปหรือ pre-processing ได้ตามความต้องการ
        # ตัวอย่าง: แปลงเป็น grayscale
        gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)

        # อาจทำ thresholding เพื่อเพิ่มความคมชัดของตัวอักษร
        _, thresh = cv2.threshold(gray, 150, 255, cv2.THRESH_BINARY)

        # บันทึกไฟล์ชั่วคราว (ถ้าต้องการ) หรือส่ง array ให้ EasyOCR ได้เลย
        # EasyOCR รองรับการอ่านจาก image array ได้โดยตรง
        result = reader.readtext(thresh, detail=0, paragraph=True)

        # รวมผลลัพธ์เป็นข้อความเดียว
        extracted_text = "\n".join(result)
        return extracted_text.strip()
    except Exception as e:
        raise OCRServiceException(f"OCR processing failed: {str(e)}")
