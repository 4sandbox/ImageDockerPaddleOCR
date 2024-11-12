import json
import sys
import base64
from io import BytesIO
from paddleocr import PaddleOCR
from PIL import Image

# Initialize PaddleOCR with Vietnamese language
ocr = PaddleOCR(use_angle_cls=True, lang='vi')

def perform_ocr_on_base64_image(base64_str):
    try:
        # Decode base64 to bytes
        image_data = base64.b64decode(base64_str)
        # Open image from bytes
        img = Image.open(BytesIO(image_data))
        
        # Perform OCR on the image
        result = ocr.ocr(img, cls=True)

        # Convert OCR results to JSON format
        output = []
        for idx, res in enumerate(result):
            for line in res:
                output.append({
                    "text": line[1][0],        # Detected text
                    "confidence": line[1][1],  # Confidence score
                    "box": line[0]             # Bounding box coordinates
                })

        # Output JSON result
        return json.dumps(output, ensure_ascii=False)

    except Exception as e:
        return json.dumps({"error": str(e)}, ensure_ascii=False)

if __name__ == "__main__":
    # Đọc dữ liệu Base64 từ STDIN
    base64_image = sys.stdin.read().strip()
    # Thực hiện OCR và xuất kết quả
    result = perform_ocr_on_base64_image(base64_image)
    print(result)
