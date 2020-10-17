'''
问题：航班乘客预测
 数据：1949 到 1960 一共 12 年，每年 12 个月的数据，一共 144 个数据，单位是 1000
 下载地址
 目标：预测国际航班未来 1 个月的乘客数

 https://cloud.tencent.com/developer/article/1083338
'''

import numpy
import matplotlib.pyplot as plt
from pandas import read_csv
import math
from keras.models import Sequential
from keras.layers import Dense
from keras.layers import LSTM
from sklearn.preprocessing import MinMaxScaler
from sklearn.metrics import mean_squared_error
import  csv
import pandas as pd

def draw_error_line(file_name,label_x):
    # load the dataset
    dataframe = read_csv(file_name,  engine='python', skipfooter=3)
    dataset = dataframe.values

    mydata = pd.DataFrame(data=dataset,columns=['c1', 'c2', 'c3'])#指定行列名

    #plt.title('不同time_steps的误差曲线',loc = 'right')

    plt.plot(mydata['c1'],mydata['c2'],'r.--',mydata['c1'],mydata['c3'],'go--')
    plt.xlabel(label_x,size=14)
    plt.ylabel('rmse',size=14)#均方根误差
    plt.legend(['train_rmse','test_rmse'])  # 给三个曲线都上图例
    plt.show()

draw_error_line("error.csv",'feature_number')

draw_error_line("error_mulsteps.csv",'time_step')
