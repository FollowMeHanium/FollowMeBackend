import requests
from bs4 import BeautifulSoup
from selenium import webdriver
import time

path = "C:/Users/seokz/Downloads/chromedriver.exe"
driver = webdriver.Chrome(path)

# 테스트용 사이트
url = 'https://www.diningcode.com/profile.php?rid=flSYGM6JJdYM'

#webdriver가 페이지에 접속하도록 명령
driver.get(url)

html = driver.page_source
soup = BeautifulSoup(html) 

part = soup.find("div", attrs={"class":"tit-point"})
title = part.find("p",attrs={"class":"tit"})

address = soup.find("li",attrs={"class":"locat"})
tel = soup.find("li",attrs={"class":"tel"})

#특정 tag 내의 데이터 불러오기
part = soup.find("li", attrs={"class":"tag"})
keyword = part.findAll("span", attrs={"class":"button"})

# 요일별 정보가 정형화 안돼있음. 팀원들과 의논
part = soup.find("div", attrs={"class":"busi-hours"})
openday = part.findAll("p",attrs={"class":"l-txt"})
opentime = part.findAll("p",attrs={"class":"r-txt"})

# 메뉴 (더보기 누른 후 크롤링)
driver.execute_script("setMore('menu-info');")

part = soup.find("div", attrs={"class":"menu-info"})
menuname = part.findAll("p",attrs={"class":"l-txt"})
menuprice = part.findAll("p",attrs={"class":"r-txt"})

#가게 이미지 - 마지막 사진 중복현상(중복 제거해야함: limit 8)
#섬네일만 추출함 - 사진 더 필요하면 수정해야함
part = soup.find("ul", attrs={"class":"store-pic"})
photo = part.findAll("li", attrs={"class":"btn-gallery-open"})

#데이터 추출부
print(title.get_text())
print(address.get_text())
print(tel.get_text())

part = soup.find("li", attrs={"class":"tag"})
for a in part.findAll("span", attrs={"class":"button"}):
    print(a.get_text())

part = soup.find("div", attrs={"class":"busi-hours"})
for a in part.findAll("p",attrs={"class":"l-txt"}):
    print(a.get_text())

for a in part.findAll("p",attrs={"class":"r-txt"}):
    print(a.get_text())

part = soup.find("div", attrs={"class":"menu-info"})
for a in part.findAll("p",attrs={"class":"l-txt"}):
    print(a.get_text())

for a in part.findAll("p",attrs={"class":"r-txt"}):
    print(a.get_text())

part = soup.find("ul", attrs={"class":"store-pic"})
for a in part.findAll("img"):
    print(a['src'])
