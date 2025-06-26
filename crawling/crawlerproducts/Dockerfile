# 베이스 이미지
FROM python:3.10-slim

# 작업 디렉토리 설정
WORKDIR /app

# 필요한 파일 복사
COPY . /app

# 패키지 설치
RUN pip install --upgrade pip
RUN pip install -r requirements.txt

# Playwright 설치 (리뷰 크롤링 시 필요 시 사용)
RUN playwright install

# 엔트리포인트
CMD ["python", "crawl_all_products.py"]

