from sklearn import datasets
from sklearn.linear_model import LinearRegression

# 直接加载数据集
loaded_data = datasets.load_boston()
data_X = loaded_data.data
data_y = loaded_data.target
model = LinearRegression() # 定义模型

model.fit(data_X, data_y) # 学习参数
print (model.coef_)# 输出权重
print (model.intercept_)# 输出偏置
# 输出model定义时的参数, 没有给定则返回默认参数 print model.get_params()
# 评估学习到的模型 # 通过coefficient of determination(决定系数),来判断回归方程拟合的程度.
print (model.score(data_X, data_y))
