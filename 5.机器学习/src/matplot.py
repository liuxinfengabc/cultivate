import matplotlib.pyplot as plt
import matplotlib.collections as mcol
from matplotlib.legend_handler import HandlerLineCollection, HandlerTuple
from matplotlib.lines import Line2D
import numpy as np
data = np.loadtxt('/home/nathan/workspace/liu刘新峰老师索要文档/数据分析资料/14/VGG16__loss_epoch.txt',delimiter=',')
plt.figure(1)
plt.figure(figsize=(14, 10))
#plt.plot(data[:,0],'r--',data[:,1],'g^',data[:,2],'bs',data[:,3],'bo')
plt.plot(data[:,1],'b--',label='train acc')
plt.plot(data[:,3],'k',marker='<',markersize=1,label='val acc')
plt.xlabel('epoch')
plt.ylabel('acc')
#plt.title('Vgg16 train accuracy and validation accuracy')
plt.legend()
plt.savefig("/home/nathan/workspace/数据分析资料/14/filename1.png")
plt.figure(2)
plt.figure(figsize=(14, 10))
plt.plot(data[:,0],'r--',label='train loss')
plt.plot(data[:,2],marker='>',markersize=5,label='val loss')
plt.xlabel('epoch')
plt.ylabel('loss')
#plt.title('Vgg16 train loss and validation loss')
plt.legend()
plt.savefig("/home/nathan/workspace/数据分析资料/14/filename2.png")
plt.show()
