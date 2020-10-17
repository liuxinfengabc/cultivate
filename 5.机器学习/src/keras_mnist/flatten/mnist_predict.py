import numpy as np
import cv2
import keras
from keras.models import load_model
from keras.optimizers import SGD
from keras.utils.vis_utils import plot_model
path="./mnist.npz"
f=np.load(path)
x_train, y_train = f['x_train'], f['y_train']
x_test, y_test = f['x_test'], f['y_test']
cv2.namedWindow("img")
cv2.imshow("img",x_test[0])
cv2.waitKey(0)
x_train=x_train.reshape(x_train.shape[0],28*28).astype('float32')
x_test=x_test.reshape(x_test.shape[0],28*28).astype('float32')
#x_test[0]=x_test[0].reshape(1,28*28)
y_test=keras.utils.to_categorical(y_test,10)
f.close()
model=load_model('./now_model.h5')
sgd=SGD(lr=0.01,momentum=0.9,decay=1e-9,nesterov=True)#优化函数，参数有学习率，学习衰退率  ,指数1e-9这样写
model.compile(optimizer=sgd,loss='categorical_crossentropy') # 使用交叉熵作为loss函数
#plot_model(model, to_file='model1.png', show_shapes=True)
print("*********")
x=np.array([x_test[0]])#!!!!!!!important!!!!!!!
ans=model.predict(x)
print(ans)
print(y_test[0])
ansmax=np.argmax(ans)
yuanmax=np.argmax(y_test[0])
print(ansmax)
print(yuanmax)
