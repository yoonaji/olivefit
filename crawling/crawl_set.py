from playwright.sync_api import sync_playwright
import time
from bs4 import BeautifulSoup
import psycopg2  # PostgreSQL 연결용

# HTML 크롤링
def crawl_skin_list_html(playwright_page, page):
    url = f"https://www.oliveyoung.co.kr/store/display/getMCategoryList.do?dispCatNo=1000001000700070013&prdSort=01&pageIdx=1&rowsPerPage=24"
    playwright_page.goto(url) #띄운 크롬브라우저를 통해 url로 이동
    time.sleep(3)
    return playwright_page.content() #크롬브라우저에서 페이지 내용을 가져옴

# HTML 파싱
def parse_skin_list(html):
    soup = BeautifulSoup(html, 'lxml') #html을 파싱하여 soup 객체 생성
    items = soup.select(".prd_info") #prd_info 클래스를 가진 모든 요소를 선택
    data = []
    for item in items:
        brand = item.select_one(".tx_brand").get_text(strip=True) #tx_brand 클래스를 가진 요소의 텍스트를 가져옴
        name = item.select_one(".tx_name").get_text(strip=True) #tx_name 클래스를 가진 요소의 텍스트를 가져옴
        link = item.select_one("a")["href"] #a 태그의 href 속성값을 가져옴
        data.append({
            "product_brand": brand,
            "product_name": name,
            "product_link": link
        })
    return data

# PostgreSQL에 저장
def write_data_to_db(data):
    conn = psycopg2.connect(
        host="35.193.145.208",       # Docker 쓰면 "localhost" 또는 "127.0.0.1"
        port=5432,
        dbname="olivefit_db",     # 위에서 생성한 DB 이름
        user="postgres",        # 기본 사용자
        password="0000"         # 설정한 비밀번호
    )
    cur = conn.cursor() #DB 연결 객체에서 커서 객체 생성. 커서객체는 데이터베이스에 대한 쿼리를 실행하고 결과를 반환하는 역할

    for row in data:
        cur.execute("""
            INSERT INTO set_products (product_brand, product_name, product_link)
            VALUES (%s, %s, %s)
        """, (row['product_brand'], row['product_name'], row['product_link']))

    conn.commit() #커밋 실행. 커밋은 데이터베이스에 대한 변경사항을 영구적으로 저장하는 역할
    cur.close() #커서 객체 닫기
    conn.close() #DB 연결 객체 닫기

# 실행 메인 로직
if __name__ == "__main__":
    num_pages = 10
    with sync_playwright() as p:
        browser = p.chromium.launch(headless=False) #크롬 브라우저 실행. headless=False는 브라우저 창을 보여주는 옵션
        playwright_page = browser.new_page() #새로운 페이지 생성
        total_data = []
        for i in range(num_pages):
            html = crawl_skin_list_html(playwright_page, i+1) #크롬브라우저에서 페이지 내용을 가져옴
            data = parse_skin_list(html) #파싱된 데이터를 가져옴
            total_data.extend(data) #total_data 리스트에 파싱된 데이터를 추가
    write_data_to_db(total_data) #데이터베이스에 저장
    print("collected:", len(total_data)) #수집된 데이터 개수 출력