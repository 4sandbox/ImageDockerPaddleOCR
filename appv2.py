import json
import argparse
from paddleocr import PaddleOCR

def ocr_to_json(img_path, lang='vi', font_path=None):
    ocr = PaddleOCR(use_angle_cls=True, lang=lang)
    result = ocr.ocr(img_path, cls=True)

    json_result = []
    for idx in range(len(result)):
        res = result[idx]
        for line in res:
            json_result.append({
                'box': line[0],
                'text': line[1][0],
                'score': line[1][1]
            })

    return json.dumps(json_result, ensure_ascii=False, indent=4)

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Perform OCR on an image file.")
    parser.add_argument("img_path", help="Path to the image file")
    parser.add_argument("--lang", default='vi', help="Language for OCR")
    parser.add_argument("--font_path", help="Path to the font file")

    args = parser.parse_args()
    result = ocr_to_json(args.img_path, lang=args.lang, font_path=args.font_path)
    print(result)
