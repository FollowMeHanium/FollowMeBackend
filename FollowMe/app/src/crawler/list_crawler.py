import requests
from bs4 import BeautifulSoup
from selenium import webdriver
import time

path = "C:/Users/seokz/Downloads/chromedriver.exe"
driver = webdriver.Chrome(path)

param = '종로'
url = 'https://www.diningcode.com/list.php?query='+param

#webdriver가 페이지에 접속하도록 명령
driver.get(url)

#[더보기] 문제 해결법. 12회 돌려야 100개 리스트 출력 가능
for n in range(1, 12):
    driver.execute_script("getMoreList();")
    time.sleep(0.5)

html = driver.page_source
soup = BeautifulSoup(html) 

#4월의 이벤트 크롤링되는거 첫 글자에 대해 isdigit() 함수 등으로 분류 가능함! 대신 10번째마다 short_address가 하나씩 밀리는데 이는 조건문으로 해결은 가능함..
restaurants = soup.findAll("span",attrs={"class":"btxt"})

# for i in range(9):
#     del restaurants[10*(i+1)-1]

food_kinds = soup.findAll("span", attrs={"class":"stxt"})

# 밀림 현상의 주범 삭제 -> 긴 주소에서 추출하겠음
# short_address = soup.findAll("i", attrs={"class":"loca"})

long_address = soup.findAll("span", attrs={"class":"ctxt"})

# #이상하게 class=ctxt 는 카테고리와 주소가 함께 들어있다. 2칸 간격의 반복문으로 해결
# for line1, line2, line3, line4 in zip(restaurants[1:], food_kinds[1:], long_address[2::2], long_address[3::2]):
#     print(line1.get_text(), end= '/ ')
#     print(line2.get_text(), end= '/ ')
#     print(line3.get_text(), end= '/ ')
#     #주소 겹치는거 해결
#     part_address =  line4.get_text().partition('서울')
#     only_address = part_address[1] + part_address[2]
#     print(only_address)

# 본 목적: 상위 100개 URL 크롤링
url = soup.findAll("a", attrs={"class":"blink"})

for a in soup.find_all('a', attrs={"class":"blink"}, href=True):
    print('https://www.diningcode.com/'+a['href'])

# 결과에서 광고 URL 중복 10개 제거해주면 상위 100개 추출 가능

