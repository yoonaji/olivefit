import asyncio
from playwright.async_api import async_playwright
import psycopg2

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

# 제품 상세 페이지 크롤링 함수 (비동기)
async def crawl_detail_with_playwright(page, link):
    try:
        await page.goto(link, timeout=60000)
        await page.wait_for_timeout(3000)  # 3초 대기
        
        # 리뷰 탭 클릭
        await page.click("ul#tabList.prd_detail_tab > li#reviewInfo > a.goods_reputation")
        await page.wait_for_timeout(6000)  # 6초 대기 (데이터 로드 시간)

        skin_type = skin_concern = irritation_level = None

        # poll_all_clrfix 안에 있는 항목들 파싱
        poll_container = page.locator("div.poll_all.clrfix")
        if await poll_container.count() > 0:
            poll_sections = poll_container.locator("dl.poll_type2.type3")
            section_count = await poll_sections.count()

            for i in range(section_count):
                section = poll_sections.nth(i)
                title_tag = section.locator("dt span")
                title_text = await title_tag.inner_text()

                items = section.locator("ul.list > li")
                item_count = await items.count()
                candidates = []

                for j in range(item_count):
                    item = items.nth(j)
                    txt_tag = item.locator("span.txt")
                    per_tag = item.locator("em.per")

                    txt = await txt_tag.inner_text()
                    per = await per_tag.get_attribute('data-value')

                    if txt and per:
                        candidates.append((txt.strip(), per.strip()))

                # 항목별 분기 처리
                if "피부타입" in title_text:
                    skin_type = get_max_text(candidates)
                elif "피부고민" in title_text:
                    skin_concern = get_max_text(candidates)
                elif "자극도" in title_text:
                    irritation_level = get_max_text(candidates)

        # 평점
        score_tag = page.locator("p#repReview b")
        product_score = None
        if await score_tag.count() > 0:
            product_score = await score_tag.inner_text()
            product_score = float(product_score.strip())

        # 이미지
        img_tag = page.locator("img#mainImg")
        product_image = None
        if await img_tag.count() > 0:
            product_image = await img_tag.get_attribute("src")

        # 가격
        price_tag = page.locator(".price-2 strong")
        product_price = None
        if await price_tag.count() > 0:
            price_text = await price_tag.inner_text()
            product_price = int(price_text.replace(",", "").replace("원", "").strip())

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

# 메인 실행 함수
async def main():
    async with async_playwright() as p:
        browser = await p.chromium.launch(headless=False)  # headless=True 하면 창 안뜸
        page = await browser.new_page()

        # 크롤링할 제품 링크 가져오기
        cur.execute("SELECT id, product_link FROM skin_products")
        products = cur.fetchall()

        for pid, link in products:
            print(f"[{pid}] 크롤링 시작: {link}")

            data = await crawl_detail_with_playwright(page, link)
            print(f"[{pid}] 크롤링 결과: {data}")

            if data:
                cur.execute("""
                    UPDATE skin_products SET
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
                conn.commit()
                print(f"[{pid}] 업데이트 완료")
            else:
                print(f"[{pid}] 크롤링 실패")

        await browser.close()
        cur.close()
        conn.close()

# asyncio로 비동기 메인 실행
if __name__ == "__main__":
    asyncio.run(main())
