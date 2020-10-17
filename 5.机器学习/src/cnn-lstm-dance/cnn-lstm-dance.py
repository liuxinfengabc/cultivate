from random import random
from random import randint
from numpy import array
from numpy import zeros
from keras.models import Sequential
from keras.layers import Conv2D
from keras.layers import MaxPooling2D
from keras.layers import LSTM
from keras.layers import Dense
from keras.layers import Flatten
from keras.layers import TimeDistributed
import matplotlib.pyplot as plt

#https://www.jiqizhixin.com/articles/2019-02-11-8

# generate the next frame in the sequence
def next_frame(last_step, last_frame, column):
    # define the scope of the next step
    lower = max(0, last_step-1)
    upper = min(last_frame.shape[0]-1, last_step+1)
    # choose the row index for the next step
    step = randint(lower, upper)
    # copy the prior frame
    frame = last_frame.copy()
    # add the new step
    frame[step, column] = 1
    return frame, step
# generate a sequence of frames of a dot moving across an image
'''build frames()函数使用一个参数来定义图像的大小，并返回一系列图像，
以及该行是向右移动（1）还是向左移动（0）。
这个函数调用另一个函数next frame()来创建当先在图像上移动时的每一个后续帧。'''
def build_frames(size):
    frames = list()
    # create the first frame
    frame = zeros((size,size))

    step = randint(0, size-1)
    '''我们现在可以选择是否在图像上画左或右线。我们将使用random()函数来决定。
    如果是向右(right)，我们将从左边或第0栏开始，
    如果向左(left)，我们将从右边开始，或者从第9栏开始。'''
    # decide if we are heading left or right
    right = 1 if random() < 0.5 else 0
    col = 0 if right else size-1

    frame[step, col] = 255
    frames.append(frame)

    # create all remaining frames .产生剩余frame
    for i in range(1, size):
        col = i if right else size-1-i
        frame, step = next_frame(step, frame, col)
        frames.append(frame)

    '''
    plt.subplot(1, 5, 1)
    plt.imshow(frames[10], cmap='gray', interpolation='none')
    if right==1:
        plt.title("right")
    else: plt.title("left")

    plt.subplot(1, 5, 2)
    plt.imshow(frames[20], cmap='gray', interpolation='none')
    plt.subplot(1, 5, 3)
    plt.imshow(frames[30], cmap='gray', interpolation='none')
    plt.subplot(1, 5, 4)
    plt.imshow(frames[40], cmap='gray', interpolation='none')
    plt.subplot(1, 5, 5)
    plt.imshow(frames[49], cmap='gray', interpolation='none')
    plt.show()
    '''
    return frames, right


# generate multiple sequences of frames and reshape for network input
def generate_examples(size, n_patterns):
    X, y = list(), list()
    for _ in range(n_patterns):
        frames, right = build_frames(size)
        X.append(frames)
        y.append(right)
    # resize as [samples, timesteps, width, height, channels]
    X = array(X).reshape(n_patterns, size, size, size, 1)
    y = array(y).reshape(n_patterns, 1)
    return X, y

# configure problem
size = 50
# define the model
model = Sequential()
model.add(TimeDistributed(Conv2D(2, (2,2), activation= 'relu'), input_shape=(None,size,size,1)))
model.add(TimeDistributed(MaxPooling2D(pool_size=(2, 2))))
model.add(TimeDistributed(Flatten()))
model.add(LSTM(50))
model.add(Dense(1, activation='sigmoid'))
model.compile(loss='binary_crossentropy', optimizer='adam', metrics=['acc'])
print(model.summary())

# fit model
X, y = generate_examples(size, 5000)
model.fit(X, y, batch_size=32, epochs=1)

# evaluate model
X, y = generate_examples(size, 100)
loss, acc = model.evaluate(X, y, verbose=0)
print('loss: %f, acc: %f' % (loss, acc*100))

# prediction on new data
X, y = generate_examples(size, 1)
yhat = model.predict_classes(X, verbose=0)
expected = "Right" if y[0]==1 else "Left"
predicted = "Right" if yhat[0]==1 else "Left"
print('Expected: %s, Predicted: %s' % (expected, predicted))