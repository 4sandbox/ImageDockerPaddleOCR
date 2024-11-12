import json
import sys
import os
from paddleocr import PaddleOCR
from PIL import Image

# Initialize PaddleOCR with Vietnamese language
ocr = PaddleOCR(use_angle_cls=True, lang='vi')

def perform_ocr_on_image_file(image_path):
    try:
        # Mở và xử lý file ảnh
        img = Image.open(image_path)
        result = ocr.ocr(img, cls=True)

        # Chuyển đổi kết quả OCR sang JSON
        output = []
        for idx, res in enumerate(result):
            for line in res:
                output.append({
                    "text": line[1][0],        # Detected text
                    "confidence": line[1][1],  # Confidence score
                    "box": line[0]             # Bounding box coordinates
                })

        # Xóa file ảnh sau khi xử lý
        os.remove(image_path)

        # Trả về kết quả JSON
        return json.dumps(output, ensure_ascii=False)

    except Exception as e:
        return json.dumps({"error": str(e)}, ensure_ascii=False)

if __name__ == "__main__":
    # Đọc đường dẫn file ảnh từ đối số dòng lệnh
    image_path = sys.argv[1]
    # Thực hiện OCR và xuất kết quả
    result = perform_ocr_on_image_file(image_path)
    print(result)
