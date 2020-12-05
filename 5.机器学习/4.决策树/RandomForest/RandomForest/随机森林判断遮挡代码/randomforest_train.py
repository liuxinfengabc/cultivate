#!/usr/bin/python
# coding:utf-8

# 对原始数据进行分为训练数据和测试数据
from sklearn.model_selection import train_test_split
from sklearn.externals import joblib
from sklearn.ensemble import RandomForestClassifier
import numpy as np
import pandas as pd
from sklearn.model_selection import GridSearchCV
from sklearn.metrics import accuracy_score, make_scorer
from sklearn.model_selection import KFold
from IPython.display import display

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
#a, b, c = np.split(x_test, 3, axis=1)


clf = RandomForestClassifier(criterion="entropy",max_depth=3,n_estimators=170,max_features=2,
                            min_samples_split=8,min_samples_leaf=4,oob_score=True, random_state=10)
# 参数分别代表n_estimators：决策树的个数，n_jobs：并行的个数，oob_score：带外数据验证 verbose： 是否显示任务进程
print(clf)
clf.fit(x_train,y_train)
#joblib.dump(clf, "train_modeltest.m")  # 保存模型
print("训练完成")


clf1 = RandomForestClassifier(criterion="gini",max_depth=3,n_estimators=170,max_features=2,
                                                           min_samples_split=8,min_samples_leaf=4,oob_score=True, random_state=10)
# 参数分别代表n_estimators：决策树的个数，n_jobs：并行的个数，oob_score：带外数据验证 verbose： 是否显示任务进程
print(clf1)
clf1.fit(x_train,y_train)
#joblib.dump(clf, "train_modeltest.m")  # 保存模型
print("训练完成")


# 用训练集验证
answer = clf.predict(x_train)
#y_train = y_train.reshape(-1)
y_train1 = y_train['label']
print("entropy",np.mean(answer == y_train1))

answer2 = clf1.predict(x_train)
#y_train = y_train.reshape(-1)
y_train12 = y_train['label']
print("gini", np.mean(answer2 == y_train12))


# 用测试集验证
answer1 = clf.predict(x_test)
y_test1 = y_test['label']
print("entropy", np.mean(answer1 == y_test1))

answer11 = clf1.predict(x_test)
y_test11 = y_test['label']
print("gini",np.mean(answer11 == y_test11))



print(clf.oob_score_)
print(clf1.oob_score_)
'''param_test1 = {'n_estimators':range(10,201,10)}
gsearch1 = GridSearchCV(estimator = RandomForestClassifier(min_samples_split=8,
                                  min_samples_leaf=3,max_depth=4,max_features='sqrt' ,random_state=10),
                       param_grid = param_test1, scoring='roc_auc',cv=5)
gsearch1.fit(x_train,y_train)'''
'''
param_test2 = {'max_depth':range(3,14,1), 'min_samples_split':range(3,20,1)}
gsearch2 = GridSearchCV(estimator = RandomForestClassifier(n_estimators= 60,
                                  min_samples_leaf=3,max_features='sqrt' ,oob_score=True, random_state=10),
   param_grid = param_test2, scoring='roc_auc',iid=False, cv=5)
gsearch2.fit(x_train,y_train)
'''

'''param_test3 = {'min_samples_split':range(8,10,1), 'min_samples_leaf':range(2,10,1)}
gsearch3 = GridSearchCV(estimator = RandomForestClassifier(n_estimators=140, max_depth=4,
                                  max_features='sqrt' ,oob_score=True, random_state=10),
   param_grid = param_test3, scoring='roc_auc',iid=False, cv=5)'''
'''param_test4 = {'min_samples_leaf':range(2,6,1)}
scoring_fnc = make_scorer(accuracy_score)
kfold = KFold(n_splits=10)
gsearch4 = GridSearchCV(estimator = RandomForestClassifier(criterion="entropy",max_depth=3,n_estimators=170,max_features=2,
                                                           min_samples_split=8,min_samples_leaf=4,oob_score=True, random_state=10),
   param_grid = param_test4, scoring=scoring_fnc,iid=False, cv=kfold)
gsearch4.fit(x_train, y_train)
print gsearch4.cv_results_
print gsearch4.best_params_
print gsearch4.best_score_



display(pd.DataFrame(gsearch4.cv_results_).T)
print "best score: {}".format(gsearch4.best_score_)
param_test5 = {'min_samples_split':range(2,16,2)}
gsearch5 = GridSearchCV(estimator = RandomForestClassifier(criterion="gini",max_depth=3,n_estimators=170,max_features=2,
                                                           min_samples_split=8,min_samples_leaf=4,oob_score=True, random_state=10),
   param_grid = param_test5, scoring=scoring_fnc,iid=False, cv=kfold)
gsearch5.fit(x_train, y_train)
print gsearch5.cv_results_
print gsearch5.best_params_
print gsearch5.best_score_


'''