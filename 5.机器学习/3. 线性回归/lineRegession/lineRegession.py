import pandas as pd
import matplotlib.pyplot as plt
from sklearn.cross_validation import train_test_split
from sklearn.linear_model import LinearRegression
import numpy as np
import os
# 1.读取数据
print(os.getcwd())
data = pd.read_csv('Advertising.csv')
data.head()
X = data[['TV', 'radio', 'newspaper']]
y = data['sales']
#画图
plt.figure(figsize=(9,12))
plt.subplot(311)
plt.plot(data['TV'], y, 'ro')
plt.title('TV')
plt.grid()
plt.subplot(312)
plt.plot(data['radio'], y, 'g^')
plt.title('radio')
plt.grid()
plt.subplot(313)
plt.plot(data['newspaper'], y, 'b*')
plt.title('newspaper')
plt.grid()
plt.tight_layout()
plt.show()
#创建一个Python特性名称列表
feature_cols = ['TV', 'radio', 'newspaper']
#使用该列表选择原始帧的一个子集
X = data[feature_cols]
print (X.head())
#检查x的类型和形状。 print (type(X))
print (X.shape)
#选择从一系列的数据帧
y = data['sales']
print (y.head())
#构建训练集与测试集
X_train,X_test, y_train, y_test = train_test_split(X, y, random_state=1)
print ("-----------shape：-------------")
print (X_train.shape)
print (y_train.shape)
print (X_test.shape)
print (y_test.shape)
#sklearn的线性回归
linreg = LinearRegression()
model=linreg.fit(X_train, y_train)
print ("-----------model：-------------")
print (model)
print ("-----------linreg.intercept_：-------------")
print (linreg.intercept_)
print ("-----------linreg.coef_：-------------")
print (linreg.coef_)
# pair the feature names with the coefficients
zip(feature_cols, linreg.coef_)
# 预测
y_pred = linreg.predict(X_test)
print ("-----------y_pred-------------")
print (y_pred)
print (type(y_pred))
#回归问题的评价测度
print ("-----------回归问题的评价测度-------------")
print (type(y_pred),type(y_test))
print (len(y_pred),len(y_test))
print (y_pred.shape,y_test.shape)
sum_mean=0
for i in range(len(y_pred)):
    sum_mean+=(y_pred[i]-y_test.values[i])**2
print ("RMSE by hand:", np.sqrt(sum_mean/len(y_pred)))
#作图
plt.figure()
plt.plot(range(len(y_pred)),y_pred,'b',label="predict")
plt.plot(range(len(y_pred)),y_test,'r',label="test")
plt.legend(loc="upper right") #显示图中的标签
plt.show()
