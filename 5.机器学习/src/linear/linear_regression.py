import numpy as np
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D

# 本代码是一个最简单的线形回归问题，优化函数为经典的gradient descent
#https://blog.csdn.net/tsyccnh/article/details/75714791

rate = 0.1 # learning rate

def da(y,y_p,x):
    return (y-y_p)*(-x)

def db(y,y_p):
    return (y-y_p)*(-1)
def calc_loss(a,b,x,y):
    tmp = y - (a * x + b)
    tmp = tmp ** 2  # 对矩阵内的每一个元素平方
    SSE = sum(tmp) / (2 * len(x))
    return SSE

#损失值计算
def draw_hill(x,y):

    a = np.linspace(-20,20,100)
    print(a)
    b = np.linspace(-20,20,100)

    x = np.array(x)
    y = np.array(y)

    allSSE = np.zeros(shape=(len(a),len(b)))
    for i in range(0,len(a)):
        for j in range(0,len(b)):
            a0 = a[i]
            b0 = b[j]
            SSE = calc_loss(a=a0,b=b0,x=x,y=y)
            allSSE[i][j] = SSE

    a,b = np.meshgrid(a, b)

    return [a,b,allSSE]



# -------------------------------start  模拟数据-------------------------------------

x = [30	,35,37,	59,	70,	76,	88,	100]
y = [1100,	1423,	1377,	1800,	2304,	2588,	3495,	4839]
x_org=list(x)
y_org=list(y)






# 数据归一化
x_max = max(x)
x_min = min(x)
y_max = max(y)
y_min = min(y)
for i in range(0,len(x)):
    x[i] = (x[i] - x_min)/(x_max - x_min)
    y[i] = (y[i] - y_min)/(y_max - y_min)





[ha,hb,hallSSE] = draw_hill(x,y)

hallSSE = hallSSE.T# 重要，将所有的losses做一个转置。原因是矩阵是以左上角至右下角顺序排列元素，而绘图是以左下角为原点。


# 初始化a,b值
a = -20.0
b = -20.0
fig = plt.figure(1, figsize=(12, 12))

row=2
col=3
#-------------Fig.1 原始图形
plt.title("原始图形")
plt.subplot(row,col,1)
plt.plot(x_org, y_org)
plt.plot(x_org, y_org, 'o')

#-------------Fig.2 归一化后图形
plt.subplot(row,col, 2)
plt.plot(x, y)
plt.plot(x, y, 'o')

#-------------Fig.3 求解
plt.subplot(row, col, 3)
plt.plot(x, y)
plt.plot(x, y, 'o')


# ------------Fig.4 绘制图的曲面
ax = fig.add_subplot(row, col, 4, projection='3d')
ax.set_top_view()
ax.plot_surface(ha, hb, hallSSE, rstride=2, cstride=2, cmap='rainbow')
plt.xlabel("a")
plt.ylabel("b")



# ----------Fig.5 绘制图2的等高线图
plt.subplot(row,col,5)
#ta = np.linspace(-20, 20, 100)
#tb = np.linspace(-20, 20, 100)
plt.contourf(ha,hb,hallSSE,15,alpha=0.5,cmap=plt.cm.hot)
C = plt.contour(ha,hb,hallSSE,15,colors='black')
plt.clabel(C,inline=True)
plt.xlabel('a')
plt.ylabel('b')



plt.ion() # iteration on

all_loss = []
all_step = []
last_a = a
last_b = b
for step in range(1,200):
    loss = 0
    all_da = 0
    all_db = 0
    if i>150:
        rate=0.12
    for i in range(0,len(x)):
        y_p = a*x[i] + b
        loss = loss + (y[i] - y_p)*(y[i] - y_p)/2
        all_da = all_da + da(y[i],y_p,x[i])
        all_db = all_db + db(y[i],y_p)
    #loss_ = calc_loss(a = a,b=b,x=np.array(x),y=np.array(y))
    loss = loss/len(x)

    # -------------Fig.2 归一化后图形
    if step%30==0:
        plt.subplot(row, col, 2)
        x_ = np.linspace(0, 1, 2)
        y_draw = a * x_ + b
        plt.xlim(0, 1)
        plt.ylim(0, 1)
        plt.plot(x_, y_draw, "--")


    # 绘制图3中的回归直线
    plt.subplot(row, col, 3)
    x_ = np.linspace(0, 1, 2)
    y_draw = a * x_ + b
    #plt.xlim(min(x_), max(y_draw))
    #plt.ylim((min(y_draw), max(y_draw)))
    plt.xlim(0,1)
    plt.ylim(0,1)
    plt.plot(x_, y_draw,"--")



    # 绘制图4中的loss点
    ax.scatter(a, b, loss, color='black')



    # 绘制图5中的loss点
    plt.subplot(row,col,5)
    plt.scatter(a,b,s=5,color='blue')
    plt.plot([last_a,a],[last_b,b],color='aqua')



    # 绘制图6的loss更新曲线
    all_loss.append(loss)
    all_step.append(step)
    plt.subplot(row,col,6)
    plt.plot(all_step,all_loss,color='orange')
    plt.xlabel("step")
    plt.ylabel("loss")


    # print('a = %.3f,b = %.3f' % (a,b))
    last_a = a
    last_b = b
    a = a - rate*all_da
    b = b - rate*all_db

    if step%1 == 0:
        print("step: ", step, " loss: ", loss)
        plt.show()
        plt.pause(0.01)

# 绘制图3中的回归直线
plt.subplot(row, col, 2)
x_ = np.linspace(0, 1, 2)
y_draw = a * x_ + b
plt.xlim(0,1)
plt.ylim(0,1)
plt.plot(x_, y_draw,"green")
plt.show()
plt.pause(99999999999)