import cv2
import numpy as np  
def generateSingleNumber(pathOf123,pathofa):
    img=cv2.imread(pathOf123)  #'/Users/zhangyiming/Desktop/workshop/123.jpg'
    GrayImage=cv2.cvtColor(img,cv2.COLOR_BGR2GRAY)   #将BGR图转为灰度图
    ret,thresh1=cv2.threshold(GrayImage,130,255,cv2.THRESH_BINARY)  #将图片进行二值化（130,255）之间的点均变为255（背景）
    # print(thresh1[0,0])#250  输出[0,0]这个点的像素值  				#返回值ret为阈值
    # print(ret)#130
    (h,w)=thresh1.shape #返回高和宽
    # print(h,w)#s输出高和宽
    a = [0 for z in range(0, w)] 
    #print(a) #a = [0,0,0,0,0,0,0,0,0,0,...,0,0]初始化一个长度为w的数组，用于记录每一列的黑点个数  
    #记录每一列的波峰
    for j in range(0,w): #遍历一列 
        for i in range(0,h):  #遍历行
            if  thresh1[i,j]==0:  #如果改点为黑点
                a[j]+=1  		#该列的计数器加一计数
                thresh1[i,j]=255  #记录完后将其变为白色            
    #            
    for j  in range(0,w):  #遍历每一列
        for i in range((h-a[j]),h):  #从该列应该变黑的最顶部的点开始向最底部涂黑
            thresh1[i,j]=0   #涂黑
    #plt.imshow(thresh1,cmap=plt.gray())
    #plt.show()
    thresh3=thresh1/255
    T=np.sum(thresh3,axis=0)
    start=[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
    end=[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
    k=0
    m=0
    for l in range(0,w-1):
        if T[l]==0 and T[l+1]>=1 and T[l+2]>=1 and T[l+3]>=1 and T[l+4]>=1 and T[l+5]>=1:
            start[k]=l
            k=k+1
    for z in range(6,w-1):
        if T[z]==0 and T[z-1]>=1 and T[z-2]>=1 and T[z-3]>=1 and T[z-4]>=1 and T[z-5]>=1:
            end[m]=z
            m=m+1
            
    for q in range (0,19):
        
        src = cv2.imread(pathofa)
        cardimg=src[0:h,start[q]:end[q]]
        #plt.imshow(img)
        #plt.imshow(cardimg)
        cv2.imwrite(str(q)+'.jpg', cardimg)