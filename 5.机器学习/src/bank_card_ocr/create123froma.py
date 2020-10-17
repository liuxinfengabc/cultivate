#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Thu Jun 13 19:35:33 2019

@author: zhangyiming
"""
import cv2 as cv
def threshold_demo(image):
    gray = cv.cvtColor(image, cv.COLOR_RGB2GRAY)  #把输入图像灰度化
    #直接阈值化是对输入的单通道矩阵逐像素进行阈值分割。
    binary = cv.threshold(gray, 0, 255, cv.THRESH_BINARY | cv.THRESH_TRIANGLE)
    return binary
def create123froma(PathOfa):
    src = cv.imread(PathOfa)#'/Users/like/python_item/item_python_code/a.jpg'
    
    src=threshold_demo(src)
    
    #plt.imshow(src[1])
    cv.imwrite('123.jpg', src[1])