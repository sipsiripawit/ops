# app/schemas.py

from pydantic import BaseModel

class OCRResponse(BaseModel):
    extracted_text: str

class ErrorResponse(BaseModel):
    type: str
    title: str
    status: int
    detail: str
    instance: str
