import numpy as np
from sklearn.linear_model import LogisticRegression
import matplotlib.pyplot as plt
def iris_type(s):
    it = {b'Iris-setosa': 0,b'Iris-versicolor':1, b'Iris-virginica': 2}
    return it[s]

path = 'iris.data' # 数据文件路径
# 路径，浮点型数据，逗号分隔，第 4 列使用函数 iris_type 单独处理
data = np.loadtxt(path, dtype=float, delimiter=',', converters={4:iris_type})
# 将数据的 0 到 3 列组成 x ，第 4 列得到 y
x, y = np.split(data, (4,), axis=1)
# 为了可视化，仅使用前两列特征
x = x[:, :2]
logreg = LogisticRegression() # Logistic 回归模型
logreg.fit(x, y.ravel()) # 根据数据 [x,y] ，计算回归参数
# 画图
N, M = 500, 500 # 横纵各采样多少个值
x1_min, x1_max = x[:, 0].min(), x[:, 0].max() # 第 0 列的范围
x2_min, x2_max = x[:, 1].min(), x[:, 1].max() # 第 1 列的范围
t1 = np.linspace(x1_min, x1_max, N)
t2 = np.linspace(x2_min, x2_max, M)
x1, x2 = np.meshgrid(t1, t2) # 生成网格采样点
x_test = np.stack((x1.flat, x2.flat), axis=1) # 测试点

y_hat = logreg.predict(x_test) # 预测值
y_hat = y_hat.reshape(x1.shape) # 使之与输入形状相同
plt.pcolormesh(x1, x2, y_hat, cmap=plt.cm.prism) # 预测值的显示
plt.scatter(x[:, 0], x[:, 1], c=np.squeeze(y), edgecolors='k', cmap=plt.cm.prism)
# 样本的显示
plt.xlabel('Sepal length')
plt.ylabel('Sepal width')
plt.xlim(x1_min, x1_max)
plt.ylim(x2_min, x2_max)
plt.grid()
plt.show()


# 训练集上的预测结果
y_hat = logreg.predict(x)
y = y.reshape(-1)
print(y_hat.shape)
print(y.shape)
result = y_hat == y
print(y_hat)
print(y)
print(result)
c = np.count_nonzero(result)
print(c)
print('Accuracy: %.2f%%' % (100 * float(c) / float(len(result))))