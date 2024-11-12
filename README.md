pip install paddlepaddle
pip install paddleocr
sudo apt-get update
sudo apt-get install -y libgl1-mesa-glx
pip install pyinstaller
sudo apt update
sudo apt install -y default-jre
pip install vncorenlp

pyinstaller --onefile app.py
Cài đặt pyenv:

curl https://pyenv.run | bash

Thiết lập đường dẫn cho pyenv: Thêm các dòng sau vào ~/.bashrc hoặc ~/.bash_profile và tải lại:

export PATH="$HOME/.pyenv/bin:$PATH"
eval "$(pyenv init --path)"
eval "$(pyenv virtualenv-init -)"


source ~/.bashrc

Cài đặt Python 3.10 qua pyenv:

pyenv install 3.10.0

Tạo môi trường ảo với Python 3.10:

pyenv shell 3.10.0 python -m venv ~/idcard-ocr source ~/idcard-ocr/bin/activate

Cài đặt PyInstaller và đóng gói:

pip install pyinstaller
pyinstaller --onefile app.py