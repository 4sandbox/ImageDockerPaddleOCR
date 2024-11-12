FROM python:3.8-slim

RUN apt-get update && \
    apt-get install -y libgl1-mesa-glx && \
    rm -rf /var/lib/apt/lists/*

RUN pip install --no-cache-dir paddlepaddle paddleocr
RUN pip install py_vncorenlp
WORKDIR /app

COPY . .
CMD ["python", "app.py"]
