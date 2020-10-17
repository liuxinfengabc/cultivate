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



#write csv
def csv_write(path,data):
    with open(path,'w',encoding='utf-8',newline='') as f:
        writer = csv.writer(f,dialect='excel')
        for row in data:
            writer.writerow(row)
    return True
# load the dataset
dataframe = read_csv('international-airline-passengers.csv', usecols=[1], engine='python', skipfooter=3)
dataset = dataframe.values
# 将整型变为float
dataset = dataset.astype('float32')

plt.plot(dataset)
plt.show()


# X is the number of passengers at a given time (t) and Y is the number of passengers at the next time (t + 1).
'''
数据转化
将一列变成两列，第一列是 t 月的乘客数，第二列是 t+1 月的乘客数。
 look_back 就是预测下一步所需要的 time steps：

timesteps 就是 LSTM 认为每个输入数据与前多少个陆续输入的数据有联系。
例如具有这样用段序列数据 “…ABCDBCEDF…”，
当 timesteps 为 3 时，在模型预测中如果输入数据为“D”，
那么之前接收的数据如果为“B”和“C”则此时的预测输出为 B 的概率更大，之前接收的数据如果为“C”和“E”，
则此时的预测输出为 F 的概率更大'''
# convert an array of values into a dataset matrix
def create_dataset(dataset, look_back=1):
    dataX, dataY = [], []
    dataAll=[]
    for i in range(len(dataset)-look_back-1):
        a = dataset[i:(i+look_back), 0]
        dataX.append(a)
        dataY.append(dataset[i + look_back, 0])
        dataAll.append(dataset[i:(i+look_back+1), 0])

    #输出数据到excel文件
    csv_write("./data/data"+str(look_back)+".csv",dataAll)


    return numpy.array(dataX), numpy.array(dataY)

# fix random seed for reproducibility
numpy.random.seed(7)

# normalize the dataset
scaler = MinMaxScaler(feature_range=(0, 1))
dataset = scaler.fit_transform(dataset)


# split into train and test sets
train_size = int(len(dataset) * 0.67)
test_size = len(dataset) - train_size
train, test = dataset[0:train_size,:], dataset[train_size:len(dataset),:]



def trainData(time_steps,errArray):

    # use this function to prepare the train and test datasets for modeling
    trainX, trainY = create_dataset(train, time_steps)
    testX, testY = create_dataset(test, time_steps)
    #投入到 LSTM 的 X 需要有这样的结构： [samples, time step, features]，所以做一下变换
    # reshape input to be [samples, time step, features]
    samples=trainX.shape[0]
    trainX = numpy.reshape(trainX, (samples, time_steps, 1))

    samples = testX.shape[0]
    testX = numpy.reshape(testX, (samples, time_steps, 1))

    '''建立 LSTM 模型：输入层有 1 个input，隐藏层有 4 个神经元，
     输出层就是预测一个值，激活函数用 sigmoid，迭代 100 次，batch size 为 1
     '''
    # create and fit the LSTM network
    model = Sequential()
    model.add(LSTM(4, input_shape=(time_steps, 1)))#input_length=time_steps   input_dim维度=1,
    model.add(Dense(1))
    model.compile(loss='mean_squared_error', optimizer='adam')
    model.fit(trainX, trainY, epochs=100, batch_size=1, verbose=2)

    # make predictions
    trainPredict = model.predict(trainX)
    testPredict = model.predict(testX)
    # invert predictions
    trainPredict = scaler.inverse_transform(trainPredict)
    trainY = scaler.inverse_transform([trainY])
    testPredict = scaler.inverse_transform(testPredict)
    testY = scaler.inverse_transform([testY])
    trainScore = math.sqrt(mean_squared_error(trainY[0], trainPredict[:,0]))
    print('Train Score: %.2f RMSE' % (trainScore))
    testScore = math.sqrt(mean_squared_error(testY[0], testPredict[:,0]))
    print('Test Score: %.2f RMSE' % (testScore))

    errArray.append([time_steps,trainScore,testScore])

errArray=[]

time_steps=1
import pandas as pd
for i in range(2,47):
    time_steps=i
    trainData(i,errArray)

mydata = pd.DataFrame(data=errArray,columns=['c1', 'c2', 'c3'])#指定行列名

csv_write("error_mulsteps.csv", errArray)


plt.title('不同time_steps的误差曲线',loc = 'right')

plt.plot(mydata['c1'], mydata['c2'], 'r.--', mydata['c1'], mydata['c3'], 'go--')
plt.xlabel('time_steps', size=14)
plt.ylabel('rmse', size=14)  # 均方根误差
plt.legend(['train_rmse', 'test_rmse'])  # 给三个曲线都上图例
plt.show()

# shift train predictions for plotting
trainPredictPlot = numpy.empty_like(dataset)
trainPredictPlot[:, :] = numpy.nan
trainPredictPlot[time_steps:len(trainPredict)+time_steps, :] = trainPredict

# shift test predictions for plotting
testPredictPlot = numpy.empty_like(dataset)
testPredictPlot[:, :] = numpy.nan
testPredictPlot[len(trainPredict)+(time_steps*2)+1:len(dataset)-1, :] = testPredict

# plot baseline and predictions
plt.plot(scaler.inverse_transform(dataset))
plt.plot(trainPredictPlot)
plt.plot(testPredictPlot)
plt.show()
