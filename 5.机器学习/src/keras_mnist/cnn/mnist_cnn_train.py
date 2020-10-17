'''
训练方式：
1.多添加卷积层数
2.修改Epoch，训练次数
3.修改全连接的神经元个数
4.修改优化函数和损失函数
'''
from __future__ import print_function
import numpy as np
np.random.seed(1337)
from keras.models import Sequential
from keras.layers import Dense, Dropout, Activation, Flatten
from keras.layers import Convolution2D, MaxPooling2D
from keras.utils import np_utils
from   data_show import LossHistory
import keras

generate_path='../../../model/mnist/cnn/'

batch_size = 128
nb_classes = 10
nb_epoch = 10

# 输入图像的维度，此处是mnist图像，因此是28*28
img_rows, img_cols = 28, 28
# 卷积层中使用的卷积核的个数
nb_filters = 32
# 池化层操作的范围
pool_size = (2,2)
# 卷积核的大小
kernel_size = (3,3)

# keras中的mnist数据集已经被划分成了60,000个训练集，10,000个测试集的形式，按以下格式调用即可
#(X_train, y_train), (X_test, y_test) = mnist.load_data()

path = '../../../data/mnist.npz'
f = np.load(path)
X_train, y_train = f['x_train'], f['y_train']
X_test, y_test = f['x_test'], f['y_test']
f.close()

# 后端使用tensorflow时，会将100张RGB三通道的16*32彩色图表示为(samples,rows，cols,channels)，
# 第一个维度是样本维，表示样本的数目， 第二和第三个维度是高和宽， 最后一个维度是通道维，表示颜色通道数
X_train = X_train.reshape(X_train.shape[0], img_rows, img_cols, 1)
X_test = X_test.reshape(X_test.shape[0], img_rows, img_cols, 1)
input_shape = (img_rows, img_cols, 1)

# 将X_train, X_test的数据格式转为float32
X_train = X_train.astype('float32')
X_test = X_test.astype('float32')
# 归一化
X_train /= 255
X_test /= 255
# 打印出相关信息
print('X_train shape:', X_train.shape)
print(X_train.shape[0], 'train samples')
print(X_test.shape[0], 'test samples')


# 将类别向量(从0到nb_classes的整数向量)映射为二值类别矩阵，
# 相当于将向量用one-hot重新编码
Y_train = np_utils.to_categorical(y_train, nb_classes)
Y_test = np_utils.to_categorical(y_test, nb_classes)

# 建立序贯模型
model = Sequential()

# 卷积层，对二维输入进行滑动窗卷积
# 当使用该层为第一层时，应提供input_shape参数，在tf模式中，通道维位于第三个位置
# border_mode：边界模式，为"valid","same"或"full"，即图像外的边缘点是补0
# 还是补成相同像素，或者是补1
model.add(Convolution2D(nb_filters, kernel_size,
                        border_mode='valid',
                        input_shape=input_shape, activation='relu'))
model.add(MaxPooling2D(pool_size=pool_size))

# 卷积层，激活函数是ReLu
model.add(Convolution2D(nb_filters, kernel_size, activation='relu'))

# 池化层，选用Maxpooling，给定pool_size，dropout比例为0.25
model.add(MaxPooling2D(pool_size=pool_size))
model.add(Dropout(0.25))

# Flatten层，把多维输入进行一维化，常用在卷积层到全连接层的过渡
model.add(Flatten())

# 包含256个神经元的全连接层，激活函数为ReLu，dropout比例为0.5
model.add(Dense(256))
model.add(Activation('relu'))
model.add(Dropout(0.5))

# 包含10个神经元的输出层，激活函数为Softmax
model.add(Dense(nb_classes))
model.add(Activation('softmax'))

# 输出模型的参数信息
model.summary()

from keras.utils.vis_utils import model_to_dot
from IPython.display import SVG
#model即为要可视化的网络模型
SVG(model_to_dot(model).create(prog='dot', format='svg'))



# 配置模型的学习过程
model.compile(loss='categorical_crossentropy',
              optimizer='adadelta',
              metrics=['accuracy'])



history = LossHistory(train_path=generate_path, model_name= 'nmist_flatten', train_id=1)

tbCallbacks = keras.callbacks.TensorBoard(log_dir=generate_path+'logs',
                                    histogram_freq=1,
                                    write_graph=True,
                                    write_images=True,
                                    write_grads=True)

# 训练模型
model.fit(X_train, Y_train, batch_size=batch_size, nb_epoch=nb_epoch,
          verbose=1, validation_data=(X_test, Y_test),
          callbacks=[history,tbCallbacks])

history.loss_plot("batch")
history.loss_plot("epoch")

# 按batch计算在某些输入数据上模型的误差
score = model.evaluate(X_test, Y_test, verbose=0)
# 输出训练好的模型在测试集上的表现
print('Test score:', score[0])
print('Test accuracy:', score[1])


model.save('./cnn_model.h5')