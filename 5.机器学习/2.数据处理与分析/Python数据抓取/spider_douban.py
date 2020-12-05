
import urllib.request
import re
import time

fp = open('e:\\douban_film_info.txt', 'w+')
fp.write('电影名字\t电影导演\t电影主演\t电影类型\t制片国家/地区\t年份\t电影票房\t上映时间\t电影评分\n')
count_url = 0
while count_url < 2:
    url = 'https://www.douban.com/doulist/1295618/?start=' + str(count_url)
    req = urllib.request.Request(url, headers={
        "User-Agent": "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36"})
    html_text = urllib.request.urlopen(req).read().decode('utf-8')
    #print(html_text)
    #电影列表
    '''<div class="title">
      <a href="https://movie.douban.com/subject/26363254/" target="_blank">
        战狼2
      </a>
    </div>
    '''
    filmList = re.compile('.*?<div class="title">.*?<a href=".*?" target="_blank">(.*?)</a>.*?</div>', re.S).findall(html_text)

    '''
     <div class="abstract">
         导演: 吴京
           <br />
         主演: 吴京 / 弗兰克·格里罗 / 吴刚
           <br />
         类型: 动作
           <br />
         制片国家/地区: 中国大陆
           <br />
         年份: 2017
     </div>
     '''
    filmAbstractList = re.compile(r'<div class="abstract">(.*?)</div>', re.S).findall(html_text)
    '''
     <div class="comment-item content" data-cid="273031232">
      <blockquote class="comment">
        <span>评语：</span>总票房：568323万元 | 上映日期：2017年7月27日（20:00） | 发行类别：国产
      </blockquote>
    </div>
    '''
    filmRemarkList = re.compile(r'<span>评语：</span>(.*?)</blockquote>', re.S).findall(html_text)

    '''
     <div class="rating">
          <span class="allstar35"></span>
          <span class="rating_nums">7.2</span>
          <span>(475457人评价)</span>
      </div>
    '''
    scores = re.findall(r'<span class="rating_nums">(.*?)</span>', html_text)
    count = 0
    filmIndex = 0

    try:
        print(len(filmAbstractList))

        for filmIndex in range(0,len(filmAbstractList)):

            fp.write(filmList[filmIndex].strip()+'\t')#写入影片名字

            abstract=filmAbstractList[filmIndex]
            print(abstract)
            # 获取简介的每个Item
            for item in (abstract.split('<br />')):
                fp.write(item.strip().replace(' / ', ',').replace('/', ',').split(': ')[1] + '\t')
                if count % 5 == 0:
                    pass
                count += 1

            #获取备注（评语）
            '''            
          <span>评语：</span>总票房：568323万元 | 上映日期：2017年7月27日（20:00） | 发行类别：国产
          </blockquote>
            '''
            for remark in filmRemarkList[filmIndex].split(' | '):
                inf = remark.split('：')[1]
                if inf.find('进口') == -1:
                    if inf.find('国产') == -1:
                        fp.write(inf + '\t')
            fp.write(scores[filmIndex] + '\t')
            fp.write('\n')
    except Exception:
        pass
    count_url += 25
    time.sleep(2)
