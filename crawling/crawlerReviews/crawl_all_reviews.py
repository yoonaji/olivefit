from flask import Flask
import threading
import crawl_review_allinone
import crawl_review_cream
import crawl_review_essence
import crawl_review_lotion
import crawl_review_set
import crawl_review_skin
import os

app = Flask(__name__)
def run_crawlers():
    threads = []
    for func in [
        crawl_review_allinone.main,
        crawl_review_cream.main,
        crawl_review_essence.main,
        crawl_review_lotion.main,
        crawl_review_set.main,
        crawl_review_skin.main
    ]:
        t = threading.Thread(target=func)
        t.start()
        threads.append(t)

    for t in threads:
        t.join()
        
@app.route("/run-crawler", methods=["POST"])
def run_crawler_endpoint():
    # 크롤링을 비동기 Thread로 실행
    threading.Thread(target=run_crawlers).start()
    return "크롤링 시작됨", 200

if __name__ == "__main__":
    # Flask 서버 실행 (Cloud Run 서비스는 PORT=8080에서 Listen 해야 정상 동작함)
    app.run(host="0.0.0.0", port=int(os.environ.get("PORT", 8080)))
