import json
import argparse
from paddleocr import PaddleOCR

def ocr_to_json(img_path, lang='vi', det_model_dir=None, rec_model_dir=None, cls_model_dir=None):
    # Khởi tạo PaddleOCR với các model directory được chỉ định
    ocr = PaddleOCR(
        use_angle_cls=True,
        lang=lang,
        det_model_dir=det_model_dir,
        rec_model_dir=rec_model_dir,
        cls_model_dir=cls_model_dir
    )
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
    parser.add_argument("--det_model_dir", default="models/en_PP-OCRv3_det_infer", help="Path to the detection model directory")
    parser.add_argument("--rec_model_dir", default="models/latin_PP-OCRv3_rec_infer", help="Path to the recognition model directory")
    parser.add_argument("--cls_model_dir", default="models/ch_ppocr_mobile_v2.0_cls_infer", help="Path to the classification model directory")

    args = parser.parse_args()
    result = ocr_to_json(
        args.img_path,
        lang=args.lang,
        det_model_dir=args.det_model_dir,
        rec_model_dir=args.rec_model_dir,
        cls_model_dir=args.cls_model_dir
    )
    print(result)
