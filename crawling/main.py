import subprocess

scripts = [
    "crawl_allinone.py",
    "crawl_cream.py",
    "crawl_essence.py",
    "crawl_lotion.py",
    "crawl_skin.py",
    "crawl_set.py",
    "crawl_review_allinone.py",
    "crawl_review_cream.py",
    "crawl_review_essence.py",
    "crawl_review_lotion.py",
    "crawl_review_set.py",
    "crawl_review_skin.py"
]

for script in scripts:
    print(f"\n🚀 Running {script}...")
    result = subprocess.run(["python", script], capture_output=True, text=True)
    
    if result.returncode == 0:
        print(f"✅ {script} 실행 성공")
    else:
        print(f"❌ {script} 실행 실패\n{result.stderr}")
