# Sử dụng image Python cơ bản
FROM python:3.8-slim

# Cài đặt các gói thư viện hệ thống cần thiết cho PaddleOCR và PaddlePaddle
RUN apt-get update && \
    apt-get install -y libgl1-mesa-glx libglib2.0-0 libgomp1 && \
    rm -rf /var/lib/apt/lists/*

# Cài đặt PaddlePaddle và PaddleOCR
RUN pip install --no-cache-dir paddlepaddle paddleocr

# Thiết lập thư mục làm việc trong container
WORKDIR /app

# Sao chép mã nguồn từ thư mục hiện tại vào thư mục /app trong container
COPY . .

# Thiết lập entrypoint cho ứng dụng
CMD ["python", "app.py"]
