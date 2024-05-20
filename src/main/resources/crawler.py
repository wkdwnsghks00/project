import multiprocessing
from bs4 import BeautifulSoup
import requests
import requests.packages
import datetime
import MySQLdb
import schedule
import time

# 데이터베이스 연결 정보  아마존
DB_USER = "root"
DB_PASSWORD = "Wnsghks189493_"
DB_HOST = "mydb1.c5ye0co6cobn.ap-northeast-2.rds.amazonaws.com"
DB_NAME = "crawl_data"

# # 데이터베이스 연결 정보 로컬호스트
# DB_USER = "crawl_user"
# DB_PASSWORD = "Test001"
# DB_HOST = "localhost"
# DB_NAME = "crawl_data"

# MySQL 연결
conn = MySQLdb.connect(
    user=DB_USER,
    passwd=DB_PASSWORD,
    host=DB_HOST,
    db=DB_NAME
)
# 커서 생성
cursor = conn.cursor()

# HTTP 요청 헤더
headers = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36",
    "Accept-Language": "ko-KR,ko;q=0.8,en-US;q=0.5,en;q=0.3"
}

# 카테고리 정보
category_name = ["notebook", "desktop", "monitor", "keyboard", "mouse", "smartphone", "tablet", "smartwatch", "game"]
product_code = {
    "notebook": 497135,
    "desktop": 497136,
    "monitor": 510541,
    "keyboard": 497146,
    "mouse": 497143,
    "smartphone": 497244,
    "tablet": 497245,
    "smartwatch": 497252,
    "game": 395267
} # 카테고리별 상품코드
url_list = [f"https://www.coupang.com/np/categories/{product_code[category]}?listSize=120&page=" for category in category_name]

# 카테고리 ID 매핑 딕셔너리
category_mapping = {
    "497035": 1,  # notebook
    "497036": 2,  # desktop
    "510441": 3,  # monitor
    "497046": 4,  # keyboard
    "497043": 5,  # mouse
    "497144": 6,  # smartphone
    "497145": 7,  # tablet
    "497152": 8,  # smartwatch
    "395167": 9   # game
}

# 크롤링된 URL 저장
crawled_urls = set()

# URL을 처리하고 데이터를 가져오는 함수
def fetch_product_links(category_url, page_num):
    link_list = [] # 상품 링크를 저장할 리스트
    try:
        url = f"{category_url}{page_num}"
        response = requests.get(url, headers=headers)
        response.raise_for_status()
        html = response.text
        soup = BeautifulSoup(html, "html.parser")
        items = soup.select(".baby-product.renew-badge")

        for item in items:
            badge = item.select_one(".badge.rocket")
            # 로켓배송이면
            if not badge:
                continue # 로켓배송 아이콘이 없으면 다음 상품으로 넘어감
            link = f"https://www.coupang.com{item.a['href']}" # 상품 링크 추출
            if link not in crawled_urls:
                link_list.append(link) # 중복되지 않는 새로운 상품 링크라면 목록에 추가
                crawled_urls.add(link) # 크롤링된 링크 목록에 추가
    except Exception as e:
        print(f"Error processing URL {url}: {e}")
    return link_list # 수집된 제품 링크 목록 반환

# 각 상품의 세부 정보를 처리하는 함수
def crawl_product_data(url):
    try:
        response = requests.get(url, headers=headers)
        html = response.text
        soup = BeautifulSoup(html, "html.parser")

        # 제품 정보 수집

        # 브랜드 정보 추출
        brand = soup.select_one(".prod-brand-name")
        brand = "-" if not brand else brand.text.strip()
        # 제목 추출
        title = soup.select_one(".prod-buy-header")
        title = "-" if not title else title.select_one(".prod-buy-header__title").text.strip() 
        # 할인율 추출
        discount_rate = soup.select_one(".discount-rate")
        discount_rate = "0%" if not discount_rate else discount_rate.text.strip() 
        # 원래 가격 추출
        origin_price = soup.select_one(".origin-price")
        origin_price = 0 if not origin_price else int(origin_price.text.strip().replace(',', '')[:-1]) 
        # 판매 가격 추출
        sale_price = soup.select_one(".prod-sale-price")
        sale_price = 0 if not sale_price else int(sale_price.select_one(".total-price").text.strip().replace(',', '')[:-1]) 
        # 쿠폰 가격 추출
        coupon_price = soup.select_one(".prod-coupon-price")
        coupon_price = 0 if not coupon_price else int(coupon_price.select_one(".total-price").text.strip().replace(',', '')[:-1]) 
        # 제품 이미지 URL 추출
        img_url = "http:" + soup.select_one("#repImageContainer > img")['src'] 
        # 현재 날짜와 시간 정보 추출
        now = datetime.datetime.now() 
        crawl_time = now.strftime("%Y-%m-%d %H:%M:%S") # 현재 시간을 포맷에 맞추어 문자열로 변환

        # 옵션 정보
        option = soup.select(".prod-option__item")
        if option:
            option_list = [f"{item.select_one('.title').string.strip()}:{item.select_one('.value').string.strip()}" for item in option]
            option = ", ".join(option_list)
        else:
            option = "-"

        # 상세 설명
        description = soup.select(".prod-description .prod-attr-item")
        if description:
            description_list = [desc.string.strip() for desc in description]
            description = ", ".join(description_list)
        else:
            description = "-"

        # 데이터 삽입을 위한 딕셔너리 생성
        data = {
            'brand': brand,
            'title': title,
            'option': option,
            'description': description,
            'img_url': img_url,
            'url': url,
            'origin_price': origin_price,
            'sale_price': sale_price,
            'coupon_price': coupon_price,
            'discount_rate': discount_rate,
            'crawl_time': crawl_time
        }

        # 카테고리 코드와 카테고리 ID 매핑 딕셔너리
        category_mapping = {
            "497035": 1,
            "497036": 2,
            "510441": 3,
            "497046": 4,
            "497043": 5,
            "497144": 6,
            "497145": 7,
            "497152": 8,
            "395167": 9
        }

        # URL에서 카테고리 코드 추출하여 카테고리 ID 매핑
        category_code = url[-6:]
        if category_code in category_mapping:
            data['category_id'] = category_mapping[category_code]
        else:
            data['category_id'] = None  # 카테고리 코드가 없을 경우 None으로 설정

        # url로 데이터베이스에서 상품 확인 ( 중복 체크 )
        check_product_query = "SELECT id FROM product WHERE url = %(url)s"
        cursor.execute(check_product_query, {'url': url})
        existing_product = cursor.fetchone()

        if existing_product:
            product_id = existing_product[0]  # 기존 상품이 존재하면 상품 ID 가져오기
            print("이미존재함")
        else:
            # 상품이 없는 경우에만 새로운 상품을 데이터베이스에 추가
            print("존재하지않음")
            add_product_query = """
                INSERT INTO product (title, brand, prod_option, description, img_url, url, category_id)
                VALUES (%(title)s, %(brand)s, %(option)s, %(description)s, %(img_url)s, %(url)s, %(category_id)s)
            """
            cursor.execute(add_product_query, data)
            product_id = cursor.lastrowid

        # 가격 테이블에 데이터 삽입
        add_price_query = """
            INSERT INTO price (origin_price, sale_price, coupon_price, discount_rate, crawl_time, product_id)
            VALUES (%(origin_price)s, %(sale_price)s, %(coupon_price)s, %(discount_rate)s, %(crawl_time)s, %(product_id)s)
        """
        data['product_id'] = product_id
        cursor.execute(add_price_query, data)

        print("저장")

        conn.commit() # 데이터베이스에 변경 사항을 커밋

    except Exception as e:
        print(f"Error processing product {url}: {e}")

def main():
    pool = multiprocessing.Pool(processes=4)
    # 각 카테고리 페이지에서 제품 링크 수집하여 크롤링
    for category_url in url_list:
        for page_num in range(1, 6): # 각 카테고리 페이지의 1부터 5까지 크롤링
            product_links = fetch_product_links(category_url, page_num) # 제품 링크 수집
            pool.map(crawl_product_data, product_links) # 제품 데이터 크롤링 함수를 제품 링크에 매핑하여 병렬로 실행

    pool.close() # 프로세스 풀을 닫음
    pool.join() # 모든 작업이 완료될 때까지 대기

    cursor.close() # 데이터베이스 커서 닫음
    conn.close() # 데이터베이스 연결 닫음
    

if __name__ == "__main__":
    main() # 메인 함수 호출하여 크롤링 수행