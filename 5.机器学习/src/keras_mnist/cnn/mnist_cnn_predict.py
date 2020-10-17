import numpy as np
import keras
from keras.models import load_model
from keras.optimizers import SGD
from keras.utils.vis_utils import plot_model
path="./mnist.npz"
f=np.load(path)
X_train, y_train = f['x_train'], f['y_train']
X_test, y_test = f['x_test'], f['y_test']

X_train = X_train.reshape(X_train.shape[0], 28, 28, 1)
X_test = X_test.reshape(X_test.shape[0], 28, 28, 1)
# 将X_train, X_test的数据格式转为float32
X_train = X_train.astype('float32')
X_test = X_test.astype('float32')
# 归一化
X_train /= 255
X_test /= 255

y_test=keras.utils.to_categorical(y_test,10)
f.close()


model=load_model('./cnn_model.h5')
print("*********")

accuracy=0
for index in range(len(X_test)):

    x=np.array([X_test[index]])#!!!!!!!important!!!!!!!
    ans=model.predict(x)
    print("predict result vertor")
    print(ans)
    print("real result vector")
    print(y_test[index])

    ansmax=np.argmax(ans)
    yuanmax=np.argmax(y_test[index])

    print("predict result number")
    print(ansmax)

    print("real result vector number")
    print(yuanmax)
    if ansmax==yuanmax:
        accuracy+=1


print(accuracy/100)