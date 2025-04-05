# app/main.py

import os
import shutil
from fastapi import FastAPI, UploadFile, File, HTTPException, Request
from fastapi.responses import JSONResponse
from app.ocr import perform_ocr
from app.schemas import OCRResponse, ErrorResponse
from app.exceptions import OCRServiceException
from uuid import uuid4

app = FastAPI(title="Machine Learning OCR Service")

UPLOAD_DIR = "uploads"

# สร้าง directory สำหรับเก็บไฟล์อัปโหลด หากยังไม่มี
if not os.path.exists(UPLOAD_DIR):
    os.makedirs(UPLOAD_DIR)

@app.post("/ocr", response_model=OCRResponse)
async def ocr_endpoint(file: UploadFile = File(...)):
    """
    รับไฟล์รูปภาพและส่งกลับข้อความที่ได้จาก OCR
    """
    try:
        # สร้างไฟล์ชั่วคราวเพื่อบันทึกไฟล์ที่อัปโหลด
        file_extension = os.path.splitext(file.filename)[1]
        file_name = f"{uuid4().hex}{file_extension}"
        file_path = os.path.join(UPLOAD_DIR, file_name)

        with open(file_path, "wb") as buffer:
            shutil.copyfileobj(file.file, buffer)

        # ทำ OCR บนไฟล์ที่อัปโหลด
        extracted_text = perform_ocr(file_path)

        # ลบไฟล์ชั่วคราวหลังใช้งานแล้ว
        os.remove(file_path)

        return OCRResponse(extracted_text=extracted_text)
    except Exception as e:
        raise OCRServiceException(str(e))

@app.exception_handler(OCRServiceException)
async def ocr_exception_handler(request: Request, exc: OCRServiceException):
    error_response = ErrorResponse(
        type="about:blank",
        title="OCR Service Error",
        status=500,
        detail=exc.message,
        instance=request.url.path
    )
    return JSONResponse(status_code=500, content=error_response.dict())
