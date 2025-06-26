# 베이스 이미지
FROM python:3.10-slim

# 작업 디렉토리 설정
WORKDIR /app

# 필요한 파일 복사
COPY . /app

# 필수 system dependencies 설치 (chromium용)
RUN apt-get update && apt-get install -y \
    libnss3 \
    libatk-bridge2.0-0 \
    libxcomposite1 \
    libxdamage1 \
    libxrandr2 \
    libgbm1 \
    libasound2 \
    libpangocairo-1.0-0 \
    libcups2 \
    libxss1 \
    libxtst6 \
    libgtk-3-0 \
    libx11-xcb1 \
    --no-install-recommends && rm -rf /var/lib/apt/lists/*

# 패키지 설치
RUN pip install --upgrade pip
RUN pip install -r requirements.txt

# Playwright 브라우저 설치 (chromium만 설치 추천 — 시간 절약 가능)
RUN playwright install chromium

# 엔트리포인트
CMD ["python", "crawl_all_reviews.py"]
