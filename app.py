import json
import sys
from paddleocr import PaddleOCR
from PIL import Image

# Khởi tạo PaddleOCR với hỗ trợ ngôn ngữ tiếng Anh
ocr = PaddleOCR(use_angle_cls=True, lang='en')

def perform_ocr_on_image_file(image_path):
    try:
        # Mở ảnh từ đường dẫn
        img = Image.open(image_path).convert('RGB')
        
        # Thực hiện OCR trên ảnh
        result = ocr.ocr(img, cls=True)

        # Chuyển đổi kết quả OCR sang định dạng JSON
        output = []
        for res in result:
            for line in res:
                output.append({
                    "text": line[1][0],        # Văn bản đã nhận dạng
                    "confidence": line[1][1],  # Độ tin cậy của văn bản
                    "box": line[0]             # Tọa độ hộp bao quanh văn bản
                })

        # Xuất kết quả dưới dạng JSON
        return json.dumps(output, ensure_ascii=False)

    except Exception as e:
        return json.dumps({"error": str(e)}, ensure_ascii=False)

if __name__ == "__main__":
    # Nhận đường dẫn ảnh từ dòng lệnh
    if len(sys.argv) < 2:
        print(json.dumps({"error": "No image path provided."}))
        sys.exit(1)
    
    image_path = sys.argv[1]
    # Thực hiện OCR và in kết quả ra STDOUT dưới dạng JSON
    result = perform_ocr_on_image_file(image_path)
    print(result)
