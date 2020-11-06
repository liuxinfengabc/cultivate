

#coding:utf-8
import os.path
import keras
import numpy as np
import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation



fig, ax = plt.subplots()
plt.ion()
#ax在第2个子图中绘制，其中行优先，
ln, = ax.plot([], [], 'r-', animated=False)  #第三个参数表示画曲线的颜色和线型，具体参见：https://blog.csdn.net/tengqingyong/article/details/78829596


xdata, ydata = [], []      #初始化两个数组
losses = {'batch': [], 'epoch': []}
accuracy = {'batch': [], 'epoch': []}
val_loss = {'batch': [], 'epoch': []}
val_acc = {'batch': [], 'epoch': []}

def init():
    ax.set_xlim(0, 100000000)  #设置x轴的范围epoch
    ax.set_ylim(0, 2)#设置y轴的范围
    return ln,          #返回曲线
def update(n):
    xdata.append(n)         #将每次传过来的n追加到xdata中
    #ydata.append(np.sin(n))
    ln.set_data(xdata, accuracy['epoch'])    #重新设置曲线的值
    return ln,

def gen_function():
    lastPos = 0
    while (1):
        print("gen_function")
        accuracy['epoch'].append(0.5)
        yield 1


ani = FuncAnimation(fig, update,
                    frames=gen_function,   # 这里的frames在调用update函数是会将frames作为实参传递给“n”
                    init_func=init, blit=True)
plt.show()

