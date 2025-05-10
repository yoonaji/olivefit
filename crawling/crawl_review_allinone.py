from playwright.sync_api import sync_playwright
from bs4 import BeautifulSoup
import psycopg2
import time

# PostgreSQL 연결
conn = psycopg2.connect(
    host="localhost",
    port=5432,
    dbname="cosmetics",
    user="postgres",
    password="0000"
)
cur = conn.cursor()

# 가장 비율이 높은 항목 텍스트 추출 함수
def get_max_text(items):
    if not items:
        return None
    try:
        max_item = max(items, key=lambda x: float(x[1]) if x[1] else 0)
        return max_item[0]
    except (ValueError, TypeError):
        print(f"Error processing items: {items}")
        return None

# 제품 상세 페이지 크롤링 함수
def crawl_detail_with_playwright(playwright_page, link):
    try:
        # 변수 미리 초기화
        skin_type = skin_concern = irritation_level = None
        
        playwright_page.goto(link, timeout=60000)
        time.sleep(3)
        
        # 리뷰 탭 클릭
        playwright_page.click("ul#tabList.prd_detail_tab > li#reviewInfo > a.goods_reputation")
        time.sleep(10)  # 대기 시간 증가
        
        soup = BeautifulSoup(playwright_page.content(), 'lxml')
        
        # 선택자 수정 및 디버깅 로그 추가
        poll_container = soup.select_one("div.poll_all.clrfix")
        print(f"[DEBUG] poll_container를 찾았나요? {poll_container is not None}")
        if poll_container:
            print(f"[DEBUG] poll_container HTML:\n{poll_container.prettify()}")
        else:
            print(f"[DEBUG] 전체 HTML 구조:\n{soup.prettify()[:1000]}")
        
        if poll_container:
            poll_sections = poll_container.select("dl.poll_type2")
            print(f"[DEBUG] poll_sections 길이: {len(poll_sections)}")
            
            for section in poll_sections:
                title = section.select_one("dt span")
                if not title:
                    continue
                title_text = title.text.strip()
                print(f"[DEBUG] Processing section: {title_text}")

                items = section.select("li")
                candidates = []
                for li in items:
                    txt_tag = li.select_one('span.txt')
                    per_tag = li.select_one('em.per')
                    if txt_tag and per_tag:
                        # 띄어쓰기 처리 개선
                        txt = ' '.join(txt_tag.get_text(strip=True).split())
                        per = per_tag.get('data-value')
                        print(f"[DEBUG] Found item: {txt} ({per})")
                        candidates.append((txt, per))

                print(f"[DEBUG] {title_text} 후보들:", candidates)

                if "피부타입" in title_text:
                    skin_type = get_max_text(candidates)
                    print(f"[DEBUG] 최종 선택된 피부타입: {skin_type}")
                elif "피부고민" in title_text:
                    skin_concern = get_max_text(candidates)
                    print(f"[DEBUG] 최종 선택된 피부고민: {skin_concern}")
                elif "자극도" in title_text:
                    irritation_level = get_max_text(candidates)
                    print(f"[DEBUG] 최종 선택된 자극도: {irritation_level}")

        # 평점
        score_tag = soup.select_one("p#repReview b")
        product_score = float(score_tag.text.strip()) if score_tag else None

        # 이미지
        img_tag = soup.select_one("img#mainImg")
        product_image = img_tag['src'] if img_tag else None

        # 가격
        price_tag = soup.select_one(".price-2 strong")
        product_price = int(price_tag.text.replace(",", "").replace("원", "")) if price_tag else None

        return {
            "product_score": product_score,
            "product_image": product_image,
            "product_price": product_price,
            "skin_type": skin_type,
            "skin_concern": skin_concern,
            "irritation_level": irritation_level
        }

    except Exception as e:
        print(f"[오류 발생] {link}\n{e}")
        return None

# 크롤링 및 DB 업데이트 실행
with sync_playwright() as p:
    browser = p.chromium.launch(headless=False)
    page = browser.new_page()

    cur.execute("SELECT id, product_link FROM allinone_products")
    products = cur.fetchall()

    for pid, link in products:

        print(f"[{pid}] 크롤링 시작: {link}")

        data = crawl_detail_with_playwright(page, link)
        print(f"[{pid}] 크롤링 결과: {data}")



        if data:
            cur.execute("""
                UPDATE allinone_products SET
                    product_score = %s,
                    product_image = %s,
                    product_price = %s,
                    skin_type = %s,
                    skin_concern = %s,
                    irritation_level = %s
                WHERE id = %s
            """, (
                data['product_score'],
                data['product_image'],
                data['product_price'],
                data['skin_type'],
                data['skin_concern'],
                data['irritation_level'],
                pid
            ))
            conn.commit()  # 한 건씩 반영
            print(f"[{pid}] 업데이트 완료")
        else:
            print(f"[{pid}] 크롤링 실패")

    cur.close()
    conn.close()
    browser.close()
