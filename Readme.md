# 🛍️ OliveFit - 사용자 설문 기반 맞춤 제품 추천 서비스
개발기간 : 2025.04 ~ 2025.06

풀스택 서비스 프로그래밍 - 개인 프로젝트


## 📌 프로젝트 개요
OliveFit은 사용자 피부 타입 설문 결과에 기반해 올리브영 웹사이트에서 크롤링한 제품/리뷰 데이터를 분석하여 맞춤형 화장품 추천을 제공하는 서비스입니다.

### 주요 기능

- 사용자 설문 기반 피부타입 진단

- 맞춤형 화장품 추천

- 제품/리뷰 데이터 크롤링 자동화

- 회원가입 / 로그인 (JWT 기반)

- 마이페이지 기능 (피부 재진단, 게시글 관리)

- 게시판 기능 (글 작성 / 수정 / 삭제 / 조회)

- 모바일 클라이언트 앱 제공 (Flutter)

### 시스템 아키텍쳐
![image](https://github.com/user-attachments/assets/8e1338f7-e080-4aa4-b294-79bf15407058)


### 🖥️ 프로젝트 구성
🔹 클라이언트 :  Flutter 앱

REST API 기반 JSON 통신

🔹 백엔드 서버 : Spring Boot 기반 REST API 서버. Dockerfile로 빌드 후 깃허브 action으로 gcp cloud run에 배포.

🔹 데이터베이스 : Cloud SQL (PostgreSQL)

🔹 크롤링 서버 : flask 기반 서버. Dockerfile로 빌드 후 gcp cloud run에 배포. 

### spring 서버 디렉토리 구조

![image](https://github.com/user-attachments/assets/c0be5d7b-400b-4f51-8f20-931d615b12fa)
![image](https://github.com/user-attachments/assets/b0564770-a68a-4dcd-aaa8-9357ee916d14)
![image](https://github.com/user-attachments/assets/8a8d0a6c-50d0-4781-a0f1-32f14706f31a)

### 🛠️ 기술 스택

- Backend: Spring Boot, Java 17
  
- Frontend: Flutter
  
- Database: Cloud SQL (PostgreSQL)
  
- Web Crawling: Python3, Flask, Playwright
  
- Deployment: Docker, GitHub Actions, GCP Cloud Run
  
- Authentication: JWT

### 인증 방식

- Backend: JWT 기반 인증 (HTTP Response Body에 Token 반환, Cookie 사용 X)
  
    - 로그인 성공 시 서버에서 JWT(Access Token)를 JSON Body로 반환
      
- Flutter 클라이언트:
  
    - 로그인 후 받은 Access Token을 메모리에 저장 (화면 전환 시 직접 token 넘겨서 사용)
      
    - API 호출 시 `Authorization: Bearer <Access Token>` 헤더에 포함해 서버 요청

### 서비스 상세 흐름
1️⃣ 사용자 설문 기반 피부 타입 진단

회원가입 후 로그인 -> 설문결과 없으면 설문 진행

총 7문항으로 구성

피부 타입(건성/지성/복합성/민감성 등) 

피부 고민(트러블/각질/건조 등)

설문 결과 cloudsql 저장

2️⃣ 제품 데이터 크롤링

1. 올리브영 카테고리별 제품명, 브랜드명, 상세페이지 url 크롤링

2. 크롤링한 URL 기반으로 제품 상세 정보 및 리뷰 크롤링

제품명,브랜드명,가격, 평점, 리뷰 데이터 , 제품 상세 url , 이미지 url이 포함됨.


3️⃣ 추천 알고리즘

사용자 설문 결과 기반 → 제품을 매칭해 추천
카테고리별로 제품을 보여줌


4️⃣ 회원 관리

회원가입 (/api/auth/signup)

로그인 (/api/auth/signin)

JWT 기반 인증


5️⃣ 마이페이지

본인 피부 타입 확인

피부 재진단 기능 제공

본인이 작성한 게시글 리스트 제공 (게시판 기능과 연동)

게시글 수정/삭제 가능
