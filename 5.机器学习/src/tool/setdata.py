

import numpy as np
import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation

import threading
import time

#生成子图，相当于fig = plt.figure(),ax = fig.add_subplot(),
fig, ax = plt.subplots()
#ax在第2个子图中绘制，其中行优先，
xdata, ydata = [], []      #初始化两个数组
# #第三个参数表示画曲线的颜色和线型，具体参见：https://blog.csdn.net/tengqingyong/article/details/78829596
ln, = ax.plot([], [], 'r-', animated=False)
def init():
    ax.set_xlim(0, 100)  #
    ax.set_ylim(-1, 2)#设置y轴的范围
    return ln,               #返回曲线

def update(n):
    xdata.append(n)         #将每次传过来的n追加到xdata中
    ydata.append(np.sin(n))
    #ax.set_xlim(0, n)
    ln.set_data(xdata, ydata)    #重新设置曲线的值
    print (n)
    return ln,
def gen_function():
    n = 0
    while (1):
        n=n+1
        print("gen_function:"+str(n))
        yield n
def draw_line():
    while True:
        ani = FuncAnimation(fig, update, frames=gen_function,     #这里的frames在调用update函数是会将frames作为实参传递给“n”
                        init_func=init, blit=True)
        plt.show()

def draw_ok():
    while True:
        test=1
        print("is this ok")
        time.sleep(1)
def draw_ok2():
    while True:
        test=1
        print("is this ok===2")
        time.sleep(1)

t2=threading.Thread(target=draw_ok)
t1=threading.Thread(target=draw_ok2)
t1.start()
t2.start()

ani = FuncAnimation(fig, update, frames=gen_function,  # 这里的frames在调用update函数是会将frames作为实参传递给“n”
                    init_func=init, blit=True)
plt.show()

