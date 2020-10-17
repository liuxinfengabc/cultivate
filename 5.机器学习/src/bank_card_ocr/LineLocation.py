#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Thu Jun 13 19:35:33 2019

@author: zhangyiming
"""
import cv2
import numpy as np  
def LineLocation(PathOfPicture):
    img=cv2.imread(PathOfPicture)  #'/Users/like/Desktop/test/test_images/5.jpeg'
    GrayImage=cv2.cvtColor(img,cv2.COLOR_BGR2GRAY)   #将BGR图转为灰度图
    ret,thresh1=cv2.threshold(GrayImage,130,255,cv2.THRESH_BINARY)  #将图片进行二值化（130,255）之间的点均变为255（背景）
    
    (h,w)=thresh1.shape #返回高和宽
    
    a = [0 for z in range(0, w)] 
    
    for j in range(0,w): #遍历一列 
        for i in range(0,h):  #遍历行
            if  thresh1[i,j]==0:  #如果改点为黑点
                a[j]+=1  		#该列的计数器加一计数
                thresh1[i,j]=255  #记录完后将其变为白色            
    #            
    for j  in range(0,w):  #遍历每一列
        for i in range((h-a[j]),h):  #从该列应该变黑的最顶部的点开始向最底部涂黑
            thresh1[i,j]=0   #涂黑
    thresh2=thresh1/255
    T1=np.sum(thresh2,axis=0)
    for ii in range(0,w):
        if T1[ii]<=h*0.95:
            startx=ii
            break
    
    for jj in range(w-1,-1,-1):
        if T1[jj]<=h*0.95:
            endx=jj
            break
    N=np.linspace(endx,w,w-endx)
    img=np.delete(img,N,axis=1)
    N=np.linspace(1,startx,startx)
    img=np.delete(img,N,axis=1)
    GrayImage=cv2.cvtColor(img,cv2.COLOR_BGR2GRAY)   #将BGR图转为灰度图
    ret,thresh1=cv2.threshold(GrayImage,130,255,cv2.THRESH_BINARY)  #将图片进行二值化（130,255）之间的点均变为255（背景）
    
    (h,w)=thresh1.shape #返回高和宽
    
    a = [0 for z in range(0, w)] 
    
    for j in range(0,w): #遍历一列 
        for i in range(0,h):  #遍历行
            if  thresh1[i,j]==0:  #如果改点为黑点
                a[j]+=1  		#该列的计数器加一计数
                thresh1[i,j]=255  #记录完后将其变为白色            
    #            
    for j  in range(0,w):  #遍历每一列
        for i in range((h-a[j]),h):  #从该列应该变黑的最顶部的点开始向最底部涂黑
            thresh1[i,j]=0   #涂黑
    
    GrayImage=cv2.cvtColor(img,cv2.COLOR_BGR2GRAY)   #将BGR图转为灰度图
    ret,thresh2=cv2.threshold(GrayImage,130,255,cv2.THRESH_BINARY)  #将图片进行二值化（130,255）之间的点均变为255（背景）
    (h,w)=thresh2.shape #返回高和宽
    b = [0 for z in range(0, h)] 
     
    for j in range(0,h):  
        for i in range(0,w):  
            if  thresh2[j,i]==0: 
                b[j]+=1 
                thresh2[j,i]=255         
    for j  in range(0,h):  
        for i in range(0,b[j]):   
            thresh2[j,i]=0     
    #plt.imshow(thresh2,cmap=plt.gray())
    #plt.show()
    
    #plt.imshow(img)
    #plt.show()
    thresh3=thresh2/255
    (h1,w1)=thresh2.shape
    T2=np.sum(thresh3,axis=1)
    for ii1 in range(0,h):
        if T2[ii1]<w*0.95:
            starty=ii1
            break
    for jj1 in range(h-1,-1,-1):
        if T2[jj1]<w*0.95:
            endy=jj1
            break
    
    
    N=np.linspace(endy,h1,h1-endy)
    img=np.delete(img,N,axis=0)
    N=np.linspace(1,starty,starty)
    img=np.delete(img,N,axis=0)
    
    
    ret,thresh100=cv2.threshold(GrayImage,130,255,cv2.THRESH_BINARY)
    (H,W)=thresh100.shape
    hstart=int(0.53*H)
    hend=int(0.6*H)
    wstart=int(0.2*H)
    wend=int(0.83*W)
    cardimg=img[hstart:hend,wstart:wend]
    #plt.imshow(img)
    #plt.imshow(cardimg)
    cv2.imwrite('a.jpg', cardimg)
