#!/usr/bin/python
# coding:utf-8

# 对原始数据进行分为训练数据和测试数据
from sklearn.model_selection import train_test_split
from sklearn.externals import joblib
from sklearn.ensemble import RandomForestClassifier
import numpy as np
import pandas as pd

featureName = 'dispersion ratio', 'generate level', 'solar elevation'
className = 'yes', 'no'

# 1、读入数据，并将原始数据中的数据转换为数字形式
data = np.loadtxt("data1.txt", delimiter="\t", dtype=float,usecols=(1,2,3,4))
data_index = np.loadtxt("data1.txt", dtype=str, delimiter=",", usecols=0)
dataframe = pd.DataFrame(data=data, columns=('dispersion ratio', 'generate level', 'solar elevation', 'label'),index=data_index)
x, y = np.split(dataframe, (3,), axis=1)

# 2、拆分训练数据与测试数据，为了进行交叉验证
# x_train, x_test, y_train, y_test = train_test_split(x, y, test_size=0.3,random_state=2)
x_train, x_test, y_train, y_test = train_test_split(x, y, test_size=0.3)
a, b, c = np.split(x_test, 3, axis=1)

clf = RandomForestClassifier(n_estimators=100, n_jobs=2, oob_score=True, verbose=1)
# 参数分别代表n_estimators：决策树的个数，n_jobs：并行的个数，oob_score：带外数据验证 verbose： 是否显示任务进程
print(clf)
clf.fit(x_train,y_train)
joblib.dump(clf, "train_model2.m")  # 保存模型
print("训练完成")

# 用训练集验证
answer = clf.predict(x_train)
#y_train = y_train.reshape(-1)
y_train1 = y_train['label']
print(np.mean(answer == y_train1))

# 用测试集验证
answer1 = clf.predict(x_test)
y_test1 = y_test['label']
print(np.mean(answer1 == y_test1))


