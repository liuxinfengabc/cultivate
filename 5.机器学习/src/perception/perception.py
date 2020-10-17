from numpy import *  
import operator
import os
import numpy as np
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D


# create a dataset which contains 3 samples with 2 classes  
def createDataSet():  
    # create a matrix: each row as a sample  
    group = array([[3,3], [4,3], [1,1]])  
    labels = [1, 1, -1] # four samples and two classes

    x1 = np.array([3,4,1])
    x2 = np.array([3,3,1])
    fig = plt.figure(1, figsize=(12, 12))
    a,b = np.meshgrid(x1, x2)
    ax = Axes3D(fig)
    ax.scatter(x1, x2, labels)
    plt.show()
    return group, labels

#classify using perceptron
def perceptronClassify(trainGroup,trainLabels):
    global w, b
    isFind = False  #the flag of find the best w and b
    numSamples = trainGroup.shape[0]
    mLenth = trainGroup.shape[1]
    w = [1]*mLenth
    b = 3
    while(not isFind):
        for i in range(numSamples):
            err=calLoss(trainGroup[i],trainLabels[i])
            if  err<= 0:
                print ("训练--->有误差："+str(err)+"    wegith[w1,w2]:"+str(w)+"---[b]:"+str(b))
                update(trainGroup[i],trainLabels[i])
                break    #end for loop
            elif i == numSamples-1:
                print ("训练--->成功：wegith[w1,w2]:"+str(w)+"---[b]:"+str(b))
                isFind = True   #end while loop

#classify using perceptron
def perceptronTest(trainGroup,trainLabels):
    global w, b
    numSamples = trainGroup.shape[0]
    mLenth = trainGroup.shape[1]
    for i in range(numSamples):
        if calLoss(trainGroup[i],trainLabels[i]) <= 0:
            print ("测试--->错误"+str(trainGroup[i])+str(trainLabels[i]))
        else:

            print("测试--->正确"+str(trainGroup[i])+str(trainLabels[i]))

def calLoss(input_x,trainLabel):
    global w, b
    res = 0
    for i in range(len(input_x)):
        res += input_x[i] * w[i] #
    res += b
    res *= trainLabel
    return res
def update(input_x,trainLabel):
    global w, b
    for i in range(len(input_x)):
        w[i] += trainLabel * input_x[i] #yi*xi
    b += trainLabel  #yi

g,l = createDataSet()
perceptronClassify(g,l)
perceptronTest(g,l)
