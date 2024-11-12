import json
import argparse
from paddleocr import PaddleOCR
from PIL import Image

# Initialize PaddleOCR with Vietnamese language
ocr = PaddleOCR(use_angle_cls=True, lang='vi')

def perform_ocr_on_image_file(image_path):
    try:
        # Perform OCR on the specified image file
        result = ocr.ocr(image_path, cls=True)

        # Convert OCR results to JSON format
        output = []
        for idx, res in enumerate(result):
            for line in res:
                # Each line is a text detected with position and confidence
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
    # Set up argument parsing for image path
    parser = argparse.ArgumentParser(description="Perform OCR on an image file.")
    parser.add_argument("image_path", help="Path to the image file")

    # Parse the arguments
    args = parser.parse_args()
    image_path = args.image_path

    # Perform OCR and print the result
    ocr_result_json = perform_ocr_on_image_file(image_path)
    print(ocr_result_json)