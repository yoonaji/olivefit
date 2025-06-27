from playwright.sync_api import sync_playwright
import time
from bs4 import BeautifulSoup
import psycopg2  # PostgreSQL 연결용
import os

# HTML 크롤링
def crawl_skin_list_html(playwright_page, page):
    url = f"https://www.oliveyoung.co.kr/store/display/getMCategoryList.do?dispCatNo=100000100010014&pageIdx=1&rowsPerPage=24"
    playwright_page.goto(url)
    time.sleep(3)
    return playwright_page.content()

# HTML 파싱
def parse_skin_list(html):
    soup = BeautifulSoup(html, 'lxml')
    items = soup.select(".prd_info")
    data = []
    for item in items:
        brand = item.select_one(".tx_brand").get_text(strip=True)
        name = item.select_one(".tx_name").get_text(strip=True)
        link = item.select_one("a")["href"]
        data.append({
            "product_brand": brand,
            "product_name": name,
            "product_link": link
        })
    return data

# PostgreSQL에 저장
def write_data_to_db(data):
    conn = psycopg2.connect(
    user=os.environ["DB_USER"],
    password=os.environ["DB_PASSWORD"],
    dbname=os.environ["DB_NAME"],
    host=f'/cloudsql/{os.environ["INSTANCE_CONNECTION_NAME"]}'
    )
    cur = conn.cursor()

    for row in data:
        cur.execute("""
            INSERT INTO essence_products (product_brand, product_name, product_link)
            VALUES (%s, %s, %s)
        """, (row['product_brand'], row['product_name'], row['product_link']))

    conn.commit()
    cur.close()
    conn.close()

# 실행 메인 로직
if __name__ == "__main__":
    num_pages = 10
    with sync_playwright() as p:
        browser = p.chromium.launch(headless=False)
        playwright_page = browser.new_page()
        total_data = []
        for i in range(num_pages):
            html = crawl_skin_list_html(playwright_page, i+1)
            data = parse_skin_list(html)
            total_data.extend(data)
    write_data_to_db(total_data)
    print("collected:", len(total_data))