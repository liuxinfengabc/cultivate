# 手写数字识别
import numpy as np
import keras
from keras.models import Sequential
from keras.layers.core import Dense, Dropout, Activation
from keras.optimizers import SGD  # SGD随机梯度下降
import matplotlib.pyplot as plt
from   keras_mnist.data_show import LossHistory
from keras.callbacks import EarlyStopping
from keras.callbacks import ReduceLROnPlateau
generate_path='../../../model/mnist/flatten/'

# --------第一步选择模型model的模型分Model  函数式模型 和Sequetial 序贯模型--------------->
# 函数式模型 单输入单输出 不能跨层连接 快   序贯模型 多数入多输出 可以跨层（残差网络）效率低一些
model = Sequential()

#--------- 第二步构建模型---------------------->
# mnist给定的数据集包括60000张28×28的训练集和10000张28×28的测试集及其对应的目标数字。
# 我们后端选择tensorFlow，则数据格式是channel_last  则（6000，28，28）,图片为灰度图，所以为1通道
# Keras中输入多为(nb_samples, input_dim)的形式：即(样本数量，输入维度)。而我们也常将一张图像整合成一个向量。

model.add(Dense(1024, input_shape=(784,)))  # 隐层节点500个，model的输入是28*28
model.add(Activation('tanh'))
model.add(Dropout(0.1))

model.add(Dense(10))  # 输出结点是10分类，所以dense是10维
model.add(Activation('softmax'))  # 最后一层用softmax做为多分类激励函数
model.summary()


#---------- 第三步，编译,模型在使用前必须编译，否则在调用fit或evaluate时会抛出异常。----------->


# 所以在save模型时候，要save编译前的。load模型之后要编译这个模型，所以save的时候不能保存loss函数，optimizer函数等。这些要在使用的时候自己写。
sgd = SGD(lr=0.01, momentum=0.9, decay=1e-9, nesterov=True)  # 优化函数，参数有学习率，学习衰退率  ,指数1e-9这样写
model.compile(optimizer=sgd, loss='categorical_crossentropy', metrics = ['accuracy'])  # 使用交叉熵作为loss函数

# 不要在model.compile（）里写,class_mode='categorical'。因为会出“TypeError: run() got an unexpected keyword argument 'class_mode'”这个错误


#----------- 第四步 ， 训练---------------------->
'''''
   .fit的一些参数
   batch_size：对总的样本数进行分组，每组包含的样本数量
   epochs ：训练次数
   shuffle：是否把数据随机打乱之后再进行训练
   validation_split：拿出百分之多少用来做交叉验证
   verbose：屏显模式 0：不输出  1：输出进度  2：输出每次的训练结果
'''

# (x_train, y_train), (x_test, y_test) = mnist.load_data()
# 使用Keras自带的mnist工具读取数据（第一次需要联网）
# 由于网址会被隔断，访问不到。我们将数据下载到.py通缉处
# 由于mist的输入数据维度是(num, 28, 28)，这里需要把后面的维度直接拼起来变成784维


path = '../../../data/mnist.npz'

f = np.load(path)
x_train, y_train = f['x_train'], f['y_train']
x_test, y_test = f['x_test'], f['y_test']
f.close()

for i in range(9):
    plt.subplot(3,3,i+1)
    plt.imshow(x_train[i], cmap='gray', interpolation='none')
    plt.title("Class {}".format(y_train[i]))
plt.show()


x_train = x_train.reshape(x_train.shape[0], 28 * 28).astype('float32')
x_test = x_test.reshape(x_test.shape[0], 28 * 28).astype('float32')

num_classes = 10

# convert class vectors to binary class matrices,label为0~9共10个类别，keras要求格式为binary class matrices
y_train = keras.utils.to_categorical(y_train, num_classes)
# to_categorical  i  for use with categorical_crossentropy.也就是说它是对于一个类型的容器（整型）的转化为二元类型矩阵。比如用来计算多类别交叉熵来使用的。
y_test = keras.utils.to_categorical(y_test, num_classes)


# verbose：日志显示，0为不在标准输出流输出日志信息，1为输出进度条记录，2为每个epoch输出一行记录
# shuffle：布尔值或字符串，一般为布尔值，表示是否在训练过程中随机打乱输入样本的顺序。若为字符串“batch”，则是用来处理HDF5数据的特殊情况，它将在batch内部将数据打乱。
# validation_split：0~1之间的浮点数，用来指定训练集的一定比例数据作为验证集。验证集将不参与训练，并在每个epoch结束后测试的模型的指标，如损失函数、精确度等。
# 注意，validation_split的划分在shuffle之前，因此如果你的数据本身是有序的，需要先手工打乱再指定validation_split，否则可能会出现验证集样本不均匀。
# initial_epoch: 从该参数指定的epoch开始训练，在继续之前的训练时有用。
# fit函数返回一个History的对象，其History.history属性记录了损失函数和其他指标的数值随epoch变化的情况，如果有验证集的话，也包含了验证集的这些指标变化情况
#tensorboard --logdir=logs/

stopping = EarlyStopping(monitor='val_loss',
                         min_delta=0,
                         patience=30,
                         verbose=1,
                         mode='auto',
                         restore_best_weights=True)
reduce_lr = ReduceLROnPlateau(monitor='val_loss', factor=0.2,
                              patience=10, min_lr=0.00001, verbose=1)
tbCallbacks = keras.callbacks.TensorBoard(log_dir=generate_path+'logs',
                                    histogram_freq=1,
                                    write_graph=True,
                                    write_images=True,
                                    write_grads=True)
history = LossHistory(train_path=generate_path, model_name= 'nmist_flatten', train_id=1)

model.fit(x_train, y_train, batch_size=500, epochs=50, shuffle=True, verbose=1, validation_split=0.3,
                        callbacks=[history, stopping, tbCallbacks])



model.save(generate_path+ 'mnist_model.h5')
print("test set")

# evaluate是在每个batch后得到一个值，而predict是在k个batch后得到的一个均值。
scores = model.evaluate(x_test, y_test, batch_size=500, verbose=0)
print("The test loss is %f  ,accuracy is %f" % (scores[0],scores[1]))


# ----------------------第五步输出---------------------------------->
result = model.predict(x_test, batch_size=200, verbose=0)
result_max = np.argmax(result, axis=1)
test_max = np.argmax(y_test, axis=1)

result_bool = np.equal(result_max, test_max)
true_num = np.sum(result_bool)
print("")
print("The accuracy of the model is %f" % (true_num / len(result_bool)))

history.loss_plot("epoch")