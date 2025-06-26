from flask import Flask
import threading
import crawl_allinone
import crawl_cream
import crawl_essence
import crawl_lotion
import crawl_set
import crawl_skin
import os

app = Flask(__name__)

def run_crawlers():
    # 각 크롤링 작업을 병렬 Thread로 실행
    threads = []
    for func in [
        crawl_allinone.main,
        crawl_cream.main,
        crawl_essence.main,
        crawl_lotion.main,
        crawl_set.main,
        crawl_skin.main
    ]:
        t = threading.Thread(target=func)
        t.start()
        threads.append(t)

    # 모든 Thread가 끝날 때까지 기다리기 (원하면 join 빼도 됨 → 그냥 백그라운드로 다 실행됨)
    for t in threads:
        t.join()

@app.route("/run-crawler", methods=["POST"])
def run_crawler_endpoint():
    # run_crawlers 자체를 Thread로 실행 (메인 요청은 바로 200 OK 응답)
    threading.Thread(target=run_crawlers).start()
    return "크롤링 시작됨", 200

if __name__ == "__main__":
    # Flask 서버 실행 (Cloud Run 서비스는 PORT=8080에서 Listen 해야 정상 동작함)
    app.run(host="0.0.0.0", port=int(os.environ.get("PORT", 8080)))
