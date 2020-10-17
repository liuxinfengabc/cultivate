
# coding: utf-8

# In[1]:


import keras
keras.__version__


# # Advanced usage of recurrent neural networks
# 
# This notebook contains the code samples found in Chapter 6, Section 3 of [Deep Learning with Python](https://www.manning.com/books/deep-learning-with-python?a_aid=keras&a_bid=76564dff). Note that the original text features far more content, in particular further explanations and figures: in this notebook, you will only find source code and related comments.
# 
# ---
# 
# In this section, we will review three advanced techniques for improving the performance and generalization power of recurrent neural 
# networks. By the end of the section, you will know most of what there is to know about using recurrent networks with Keras. We will 
# demonstrate all three concepts on a weather forecasting problem, where we have access to a timeseries of data points coming from sensors 
# installed on the roof of a building, such as temperature, air pressure, and humidity, which we use to predict what the temperature will be 
# 24 hours after the last data point collected. This is a fairly challenging problem that exemplifies many common difficulties encountered 
# when working with timeseries.
# 
# We will cover the following techniques:
# 
# * *Recurrent dropout*, a specific, built-in way to use dropout to fight overfitting in recurrent layers.
# * *Stacking recurrent layers*, to increase the representational power of the network (at the cost of higher computational loads).
# * *Bidirectional recurrent layers*, which presents the same information to a recurrent network in different ways, increasing accuracy and 
# mitigating forgetting issues.

# ## A temperature forecasting problem
# 
# Until now, the only sequence data we have covered has been text data, for instance the IMDB dataset and the Reuters dataset. But sequence 
# data is found in many more problems than just language processing. In all of our examples in this section, we will be playing with a weather 
# timeseries dataset recorded at the Weather Station at the Max-Planck-Institute for Biogeochemistry in Jena, Germany: http://www.bgc-jena.mpg.de/wetter/.
# 
# In this dataset, fourteen different quantities (such air temperature, atmospheric pressure, humidity, wind direction, etc.) are recorded 
# every ten minutes, over several years. The original data goes back to 2003, but we limit ourselves to data from 2009-2016. This dataset is 
# perfect for learning to work with numerical timeseries. We will use it to build a model that takes as input some data from the recent past (a 
# few days worth of data points) and predicts the air temperature 24 hours in the future.

# Let's take a look at the data:

# In[6]:


import os

data_dir = '/home/ubuntu/data/'
fname = os.path.join(data_dir, 'jena_climate_2009_2016.csv')

f = open(fname)
data = f.read()
f.close()

lines = data.split('\n')
header = lines[0].split(',')
lines = lines[1:]

print(header)
print(len(lines))


# Let's convert all of these 420,551 lines of data into a Numpy array:

# In[7]:


import numpy as np

float_data = np.zeros((len(lines), len(header) - 1))
for i, line in enumerate(lines):
    values = [float(x) for x in line.split(',')[1:]]
    float_data[i, :] = values


# For instance, here is the plot of temperature (in degrees Celsius) over time:

# In[5]:


from matplotlib import pyplot as plt

temp = float_data[:, 1]  # temperature (in degrees Celsius)
plt.plot(range(len(temp)), temp)
plt.show()


# 
# On this plot, you can clearly see the yearly periodicity of temperature.
# 
# Here is a more narrow plot of the first ten days of temperature data (since the data is recorded every ten minutes, we get 144 data points 
# per day):

# In[6]:


plt.plot(range(1440), temp[:1440])
plt.show()


# 
# On this plot, you can see daily periodicity, especially evident for the last 4 days. We can also note that this ten-days period must be 
# coming from a fairly cold winter month.
# 
# If we were trying to predict average temperature for the next month given a few month of past data, the problem would be easy, due to the 
# reliable year-scale periodicity of the data. But looking at the data over a scale of days, the temperature looks a lot more chaotic. So is 
# this timeseries predictable at a daily scale? Let's find out.

# ## Preparing the data
# 
# 
# The exact formulation of our problem will be the following: given data going as far back as `lookback` timesteps (a timestep is 10 minutes) 
# and sampled every `steps` timesteps, can we predict the temperature in `delay` timesteps?
# 
# We will use the following parameter values:
# 
# * `lookback = 720`, i.e. our observations will go back 5 days.
# * `steps = 6`, i.e. our observations will be sampled at one data point per hour.
# * `delay = 144`, i.e. our targets will be 24 hours in the future.
# 
# To get started, we need to do two things:
# 
# * Preprocess the data to a format a neural network can ingest. This is easy: the data is already numerical, so we don't need to do any 
# vectorization. However each timeseries in the data is on a different scale (e.g. temperature is typically between -20 and +30, but 
# pressure, measured in mbar, is around 1000). So we will normalize each timeseries independently so that they all take small values on a 
# similar scale.
# * Write a Python generator that takes our current array of float data and yields batches of data from the recent past, alongside with a 
# target temperature in the future. Since the samples in our dataset are highly redundant (e.g. sample `N` and sample `N + 1` will have most 
# of their timesteps in common), it would be very wasteful to explicitly allocate every sample. Instead, we will generate the samples on the 
# fly using the original data.
# 
# We preprocess the data by subtracting the mean of each timeseries and dividing by the standard deviation. We plan on using the first 
# 200,000 timesteps as training data, so we compute the mean and standard deviation only on this fraction of the data:

# In[8]:


mean = float_data[:200000].mean(axis=0)
float_data -= mean
std = float_data[:200000].std(axis=0)
float_data /= std


# 
# Now here is the data generator that we will use. It yields a tuple `(samples, targets)` where `samples` is one batch of input data and 
# `targets` is the corresponding array of target temperatures. It takes the following arguments:
# 
# * `data`: The original array of floating point data, which we just normalized in the code snippet above.
# * `lookback`: How many timesteps back should our input data go.
# * `delay`: How many timesteps in the future should our target be.
# * `min_index` and `max_index`: Indices in the `data` array that delimit which timesteps to draw from. This is useful for keeping a segment 
# of the data for validation and another one for testing.
# * `shuffle`: Whether to shuffle our samples or draw them in chronological order.
# * `batch_size`: The number of samples per batch.
# * `step`: The period, in timesteps, at which we sample data. We will set it 6 in order to draw one data point every hour.

# In[9]:


def generator(data, lookback, delay, min_index, max_index,
              shuffle=False, batch_size=128, step=6):
    if max_index is None:
        max_index = len(data) - delay - 1
    i = min_index + lookback
    while 1:
        if shuffle:
            rows = np.random.randint(
                min_index + lookback, max_index, size=batch_size)
        else:
            if i + batch_size >= max_index:
                i = min_index + lookback
            rows = np.arange(i, min(i + batch_size, max_index))
            i += len(rows)

        samples = np.zeros((len(rows),
                           lookback // step,
                           data.shape[-1]))
        targets = np.zeros((len(rows),))
        for j, row in enumerate(rows):
            indices = range(rows[j] - lookback, rows[j], step)
            samples[j] = data[indices]
            targets[j] = data[rows[j] + delay][1]
        yield samples, targets


# 
# Now let's use our abstract generator function to instantiate three generators, one for training, one for validation and one for testing. 
# Each will look at different temporal segments of the original data: the training generator looks at the first 200,000 timesteps, the 
# validation generator looks at the following 100,000, and the test generator looks at the remainder.

# In[10]:


lookback = 1440
step = 6
delay = 144
batch_size = 128

train_gen = generator(float_data,
                      lookback=lookback,
                      delay=delay,
                      min_index=0,
                      max_index=200000,
                      shuffle=True,
                      step=step, 
                      batch_size=batch_size)
val_gen = generator(float_data,
                    lookback=lookback,
                    delay=delay,
                    min_index=200001,
                    max_index=300000,
                    step=step,
                    batch_size=batch_size)
test_gen = generator(float_data,
                     lookback=lookback,
                     delay=delay,
                     min_index=300001,
                     max_index=None,
                     step=step,
                     batch_size=batch_size)

# This is how many steps to draw from `val_gen`
# in order to see the whole validation set:
val_steps = (300000 - 200001 - lookback) // batch_size

# This is how many steps to draw from `test_gen`
# in order to see the whole test set:
test_steps = (len(float_data) - 300001 - lookback) // batch_size


# ## A common sense, non-machine learning baseline
# 
# 
# Before we start leveraging black-box deep learning models to solve our temperature prediction problem, let's try out a simple common-sense 
# approach. It will serve as a sanity check, and it will establish a baseline that we will have to beat in order to demonstrate the 
# usefulness of more advanced machine learning models. Such common-sense baselines can be very useful when approaching a new problem for 
# which there is no known solution (yet). A classic example is that of unbalanced classification tasks, where some classes can be much more 
# common than others. If your dataset contains 90% of instances of class A and 10% of instances of class B, then a common sense approach to 
# the classification task would be to always predict "A" when presented with a new sample. Such a classifier would be 90% accurate overall, 
# and any learning-based approach should therefore beat this 90% score in order to demonstrate usefulness. Sometimes such elementary 
# baseline can prove surprisingly hard to beat.
# 
# In our case, the temperature timeseries can safely be assumed to be continuous (the temperatures tomorrow are likely to be close to the 
# temperatures today) as well as periodical with a daily period. Thus a common sense approach would be to always predict that the temperature 
# 24 hours from now will be equal to the temperature right now. Let's evaluate this approach, using the Mean Absolute Error metric (MAE). 
# Mean Absolute Error is simply equal to:

# In[ ]:


np.mean(np.abs(preds - targets))


# Here's our evaluation loop:

# In[10]:


def evaluate_naive_method():
    batch_maes = []
    for step in range(val_steps):
        samples, targets = next(val_gen)
        preds = samples[:, -1, 1]
        mae = np.mean(np.abs(preds - targets))
        batch_maes.append(mae)
    print(np.mean(batch_maes))
    
evaluate_naive_method()


# It yields a MAE of 0.29. Since our temperature data has been normalized to be centered on 0 and have a standard deviation of one, this 
# number is not immediately interpretable. It translates to an average absolute error of `0.29 * temperature_std` degrees Celsius, i.e. 
# 2.57˚C. That's a fairly large average absolute error -- now the game is to leverage our knowledge of deep learning to do better. 

# ## A basic machine learning approach
# 
# In the same way that it is useful to establish a common sense baseline before trying machine learning approaches, it is useful to try 
# simple and cheap machine learning models (such as small densely-connected networks) before looking into complicated and computationally 
# expensive models such as RNNs. This is the best way to make sure that any further complexity we throw at the problem later on is legitimate 
# and delivers real benefits.
# 
# Here is a simply fully-connected model in which we start by flattening the data, then run it through two `Dense` layers. Note the lack of 
# activation function on the last `Dense` layer, which is typical for a regression problem. We use MAE as the loss. Since we are evaluating 
# on the exact same data and with the exact same metric as with our common sense approach, the results will be directly comparable.

# In[17]:


from keras.models import Sequential
from keras import layers
from keras.optimizers import RMSprop

model = Sequential()
model.add(layers.Flatten(input_shape=(lookback // step, float_data.shape[-1])))
model.add(layers.Dense(32, activation='relu'))
model.add(layers.Dense(1))

model.compile(optimizer=RMSprop(), loss='mae')
history = model.fit_generator(train_gen,
                              steps_per_epoch=500,
                              epochs=20,
                              validation_data=val_gen,
                              validation_steps=val_steps)


# Let's display the loss curves for validation and training:

# In[23]:


import matplotlib.pyplot as plt

loss = history.history['loss']
val_loss = history.history['val_loss']

epochs = range(len(loss))

plt.figure()

plt.plot(epochs, loss, 'bo', label='Training loss')
plt.plot(epochs, val_loss, 'b', label='Validation loss')
plt.title('Training and validation loss')
plt.legend()

plt.show()


# 
# Some of our validation losses get close to the no-learning baseline, but not very reliably. This goes to show the merit of having had this baseline in the first place: it turns out not to be so easy to outperform. Our 
# common sense contains already a lot of valuable information that a machine learning model does not have access to.
# 
# You may ask, if there exists a simple, well-performing model to go from the data to the targets (our common sense baseline), why doesn't 
# the model we are training find it and improve on it? Simply put: because this simple solution is not what our training setup is looking 
# for. The space of models in which we are searching for a solution, i.e. our hypothesis space, is the space of all possible 2-layer networks 
# with the configuration that we defined. These networks are already fairly complicated. When looking for a solution with a space of 
# complicated models, the simple well-performing baseline might be unlearnable, even if it's technically part of the hypothesis space. That 
# is a pretty significant limitation of machine learning in general: unless the learning algorithm is hard-coded to look for a specific kind 
# of simple model, parameter learning can sometimes fail to find a simple solution to a simple problem.

# ## A first recurrent baseline
# 
# 
# Our first fully-connected approach didn't do so well, but that doesn't mean machine learning is not applicable to our problem. The approach 
# above consisted in first flattening the timeseries, which removed the notion of time from the input data. Let us instead look at our data 
# as what it is: a sequence, where causality and order matter. We will try a recurrent sequence processing model -- it should be the perfect 
# fit for such sequence data, precisely because it does exploit the temporal ordering of data points, unlike our first approach.
# 
# Instead of the `LSTM` layer introduced in the previous section, we will use the `GRU` layer, developed by Cho et al. in 2014. `GRU` layers 
# (which stands for "gated recurrent unit") work by leveraging the same principle as LSTM, but they are somewhat streamlined and thus cheaper 
# to run, albeit they may not have quite as much representational power as LSTM. This trade-off between computational expensiveness and 
# representational power is seen everywhere in machine learning.

# In[25]:


from keras.models import Sequential
from keras import layers
from keras.optimizers import RMSprop

model = Sequential()
model.add(layers.GRU(32, input_shape=(None, float_data.shape[-1])))
model.add(layers.Dense(1))

model.compile(optimizer=RMSprop(), loss='mae')
history = model.fit_generator(train_gen,
                              steps_per_epoch=500,
                              epochs=20,
                              validation_data=val_gen,
                              validation_steps=val_steps)


# Let look at our results:

# In[26]:


loss = history.history['loss']
val_loss = history.history['val_loss']

epochs = range(len(loss))

plt.figure()

plt.plot(epochs, loss, 'bo', label='Training loss')
plt.plot(epochs, val_loss, 'b', label='Validation loss')
plt.title('Training and validation loss')
plt.legend()

plt.show()


# 
# Much better! We are able to significantly beat the common sense baseline, such demonstrating the value of machine learning here, as well as 
# the superiority of recurrent networks compared to sequence-flattening dense networks on this type of task.
# 
# Our new validation MAE of ~0.265 (before we start significantly overfitting) translates to a mean absolute error of 2.35˚C after 
# de-normalization. That's a solid gain on our initial error of 2.57˚C, but we probably still have a bit of margin for improvement.

# ## Using recurrent dropout to fight overfitting
# 
# 
# It is evident from our training and validation curves that our model is overfitting: the training and validation losses start diverging 
# considerably after a few epochs. You are already familiar with a classic technique for fighting this phenomenon: dropout, consisting in 
# randomly zeroing-out input units of a layer in order to break happenstance correlations in the training data that the layer is exposed to. 
# How to correctly apply dropout in recurrent networks, however, is not a trivial question. It has long been known that applying dropout 
# before a recurrent layer hinders learning rather than helping with regularization. In 2015, Yarin Gal, as part of his Ph.D. thesis on 
# Bayesian deep learning, determined the proper way to use dropout with a recurrent network: the same dropout mask (the same pattern of 
# dropped units) should be applied at every timestep, instead of a dropout mask that would vary randomly from timestep to timestep. What's 
# more: in order to regularize the representations formed by the recurrent gates of layers such as GRU and LSTM, a temporally constant 
# dropout mask should be applied to the inner recurrent activations of the layer (a "recurrent" dropout mask). Using the same dropout mask at 
# every timestep allows the network to properly propagate its learning error through time; a temporally random dropout mask would instead 
# disrupt this error signal and be harmful to the learning process.
# 
# Yarin Gal did his research using Keras and helped build this mechanism directly into Keras recurrent layers. Every recurrent layer in Keras 
# has two dropout-related arguments: `dropout`, a float specifying the dropout rate for input units of the layer, and `recurrent_dropout`, 
# specifying the dropout rate of the recurrent units. Let's add dropout and recurrent dropout to our GRU layer and see how it impacts 
# overfitting. Because networks being regularized with dropout always take longer to fully converge, we train our network for twice as many 
# epochs.

# In[28]:


from keras.models import Sequential
from keras import layers
from keras.optimizers import RMSprop

model = Sequential()
model.add(layers.GRU(32,
                     dropout=0.2,
                     recurrent_dropout=0.2,
                     input_shape=(None, float_data.shape[-1])))
model.add(layers.Dense(1))

model.compile(optimizer=RMSprop(), loss='mae')
history = model.fit_generator(train_gen,
                              steps_per_epoch=500,
                              epochs=40,
                              validation_data=val_gen,
                              validation_steps=val_steps)


# In[33]:


loss = history.history['loss']
val_loss = history.history['val_loss']

epochs = range(len(loss))

plt.figure()

plt.plot(epochs, loss, 'bo', label='Training loss')
plt.plot(epochs, val_loss, 'b', label='Validation loss')
plt.title('Training and validation loss')
plt.legend()

plt.show()


# Great success; we are no longer overfitting during the first 30 epochs. However, while we have more stable evaluation scores, our best 
# scores are not much lower than they were previously.

# ## Stacking recurrent layers
# 
# Since we are no longer overfitting yet we seem to have hit a performance bottleneck, we should start considering increasing the capacity of 
# our network. If you remember our description of the "universal machine learning workflow": it is a generally a good idea to increase the 
# capacity of your network until overfitting becomes your primary obstacle (assuming that you are already taking basic steps to mitigate 
# overfitting, such as using dropout). As long as you are not overfitting too badly, then you are likely under-capacity.
# 
# Increasing network capacity is typically done by increasing the number of units in the layers, or adding more layers. Recurrent layer 
# stacking is a classic way to build more powerful recurrent networks: for instance, what currently powers the Google translate algorithm is 
# a stack of seven large LSTM layers -- that's huge.
# 
# To stack recurrent layers on top of each other in Keras, all intermediate layers should return their full sequence of outputs (a 3D tensor) 
# rather than their output at the last timestep. This is done by specifying `return_sequences=True`: 

# In[36]:


from keras.models import Sequential
from keras import layers
from keras.optimizers import RMSprop

model = Sequential()
model.add(layers.GRU(32,
                     dropout=0.1,
                     recurrent_dropout=0.5,
                     return_sequences=True,
                     input_shape=(None, float_data.shape[-1])))
model.add(layers.GRU(64, activation='relu',
                     dropout=0.1, 
                     recurrent_dropout=0.5))
model.add(layers.Dense(1))

model.compile(optimizer=RMSprop(), loss='mae')
history = model.fit_generator(train_gen,
                              steps_per_epoch=500,
                              epochs=40,
                              validation_data=val_gen,
                              validation_steps=val_steps)


# Let's take a look at our results:

# In[37]:


loss = history.history['loss']
val_loss = history.history['val_loss']

epochs = range(len(loss))

plt.figure()

plt.plot(epochs, loss, 'bo', label='Training loss')
plt.plot(epochs, val_loss, 'b', label='Validation loss')
plt.title('Training and validation loss')
plt.legend()

plt.show()


# We can see that the added layers does improve ours results by a bit, albeit not very significantly. We can draw two conclusions:
# 
# * Since we are still not overfitting too badly, we could safely increase the size of our layers, in quest for a bit of validation loss 
# improvement. This does have a non-negligible computational cost, though. 
# * Since adding a layer did not help us by a significant factor, we may be seeing diminishing returns to increasing network capacity at this 
# point.

# ## Using bidirectional RNNs
# 
# 
# The last technique that we will introduce in this section is called "bidirectional RNNs". A bidirectional RNN is common RNN variant which 
# can offer higher performance than a regular RNN on certain tasks. It is frequently used in natural language processing -- you could call it 
# the Swiss army knife of deep learning for NLP.
# 
# RNNs are notably order-dependent, or time-dependent: they process the timesteps of their input sequences in order, and shuffling or 
# reversing the timesteps can completely change the representations that the RNN will extract from the sequence. This is precisely the reason 
# why they perform well on problems where order is meaningful, such as our temperature forecasting problem. A bidirectional RNN exploits 
# the order-sensitivity of RNNs: it simply consists of two regular RNNs, such as the GRU or LSTM layers that you are already familiar with, 
# each processing input sequence in one direction (chronologically and antichronologically), then merging their representations. By 
# processing a sequence both way, a bidirectional RNN is able to catch patterns that may have been overlooked by a one-direction RNN.
# 
# Remarkably, the fact that the RNN layers in this section have so far processed sequences in chronological order (older timesteps first) may 
# have been an arbitrary decision. At least, it's a decision we made no attempt at questioning so far. Could it be that our RNNs could have 
# performed well enough if it were processing input sequences in antichronological order, for instance (newer timesteps first)? Let's try 
# this in practice and see what we get. All we need to do is write a variant of our data generator, where the input sequences get reverted 
# along the time dimension (replace the last line with `yield samples[:, ::-1, :], targets`). Training the same one-GRU-layer network as we 
# used in the first experiment in this section, we get the following results:

# In[40]:


def reverse_order_generator(data, lookback, delay, min_index, max_index,
                            shuffle=False, batch_size=128, step=6):
    if max_index is None:
        max_index = len(data) - delay - 1
    i = min_index + lookback
    while 1:
        if shuffle:
            rows = np.random.randint(
                min_index + lookback, max_index, size=batch_size)
        else:
            if i + batch_size >= max_index:
                i = min_index + lookback
            rows = np.arange(i, min(i + batch_size, max_index))
            i += len(rows)

        samples = np.zeros((len(rows),
                           lookback // step,
                           data.shape[-1]))
        targets = np.zeros((len(rows),))
        for j, row in enumerate(rows):
            indices = range(rows[j] - lookback, rows[j], step)
            samples[j] = data[indices]
            targets[j] = data[rows[j] + delay][1]
        yield samples[:, ::-1, :], targets
        
train_gen_reverse = reverse_order_generator(
    float_data,
    lookback=lookback,
    delay=delay,
    min_index=0,
    max_index=200000,
    shuffle=True,
    step=step, 
    batch_size=batch_size)
val_gen_reverse = reverse_order_generator(
    float_data,
    lookback=lookback,
    delay=delay,
    min_index=200001,
    max_index=300000,
    step=step,
    batch_size=batch_size)


# In[44]:


model = Sequential()
model.add(layers.GRU(32, input_shape=(None, float_data.shape[-1])))
model.add(layers.Dense(1))

model.compile(optimizer=RMSprop(), loss='mae')
history = model.fit_generator(train_gen_reverse,
                              steps_per_epoch=500,
                              epochs=20,
                              validation_data=val_gen_reverse,
                              validation_steps=val_steps)


# In[45]:


loss = history.history['loss']
val_loss = history.history['val_loss']

epochs = range(len(loss))

plt.figure()

plt.plot(epochs, loss, 'bo', label='Training loss')
plt.plot(epochs, val_loss, 'b', label='Validation loss')
plt.title('Training and validation loss')
plt.legend()

plt.show()


# 
# So the reversed-order GRU strongly underperforms even the common-sense baseline, indicating that the in our case chronological processing is very 
# important to the success of our approach. This makes perfect sense: the underlying GRU layer will typically be better at remembering the 
# recent past than the distant past, and naturally the more recent weather data points are more predictive than older data points in our 
# problem (that's precisely what makes the common-sense baseline a fairly strong baseline). Thus the chronological version of the layer is 
# bound to outperform the reversed-order version. Importantly, this is generally not true for many other problems, including natural 
# language: intuitively, the importance of a word in understanding a sentence is not usually dependent on its position in the sentence. Let's 
# try the same trick on the LSTM IMDB example from the previous section:

# In[3]:


from keras.datasets import imdb
from keras.preprocessing import sequence
from keras import layers
from keras.models import Sequential

# Number of words to consider as features
max_features = 10000
# Cut texts after this number of words (among top max_features most common words)
maxlen = 500

# Load data
(x_train, y_train), (x_test, y_test) = imdb.load_data(num_words=max_features)

# Reverse sequences
x_train = [x[::-1] for x in x_train]
x_test = [x[::-1] for x in x_test]

# Pad sequences
x_train = sequence.pad_sequences(x_train, maxlen=maxlen)
x_test = sequence.pad_sequences(x_test, maxlen=maxlen)

model = Sequential()
model.add(layers.Embedding(max_features, 128))
model.add(layers.LSTM(32))
model.add(layers.Dense(1, activation='sigmoid'))

model.compile(optimizer='rmsprop',
              loss='binary_crossentropy',
              metrics=['acc'])
history = model.fit(x_train, y_train,
                    epochs=10,
                    batch_size=128,
                    validation_split=0.2)


# 
# We get near-identical performance as the chronological-order LSTM we tried in the previous section.
# 
# Thus, remarkably, on such a text dataset, reversed-order processing works just as well as chronological processing, confirming our 
# hypothesis that, albeit word order *does* matter in understanding language, *which* order you use isn't crucial. Importantly, a RNN trained 
# on reversed sequences will learn different representations than one trained on the original sequences, in much the same way that you would 
# have quite different mental models if time flowed backwards in the real world -- if you lived a life where you died on your first day and 
# you were born on your last day. In machine learning, representations that are *different* yet *useful* are always worth exploiting, and the 
# more they differ the better: they offer a new angle from which to look at your data, capturing aspects of the data that were missed by other 
# approaches, and thus they can allow to boost performance on a task. This is the intuition behind "ensembling", a concept that we will 
# introduce in the next chapter.
# 
# A bidirectional RNN exploits this idea to improve upon the performance of chronological-order RNNs: it looks at its inputs sequence both 
# ways, obtaining potentially richer representations and capturing patterns that may have been missed by the chronological-order version alone.

# ![bidirectional rnn](https://s3.amazonaws.com/book.keras.io/img/ch6/bidirectional_rnn.png)

# To instantiate a bidirectional RNN in Keras, one would use the `Bidirectional` layer, which takes as first argument a recurrent layer 
# instance. `Bidirectional` will create a second, separate instance of this recurrent layer, and will use one instance for processing the 
# input sequences in chronological order and the other instance for processing the input sequences in reversed order. Let's try it on the 
# IMDB sentiment analysis task:

# In[4]:


from keras import backend as K
K.clear_session()


# In[5]:


model = Sequential()
model.add(layers.Embedding(max_features, 32))
model.add(layers.Bidirectional(layers.LSTM(32)))
model.add(layers.Dense(1, activation='sigmoid'))

model.compile(optimizer='rmsprop', loss='binary_crossentropy', metrics=['acc'])
history = model.fit(x_train, y_train, epochs=10, batch_size=128, validation_split=0.2)


# It performs slightly better than the regular LSTM we tried in the previous section, going above 88% validation accuracy. It also seems to 
# overfit faster, which is unsurprising since a bidirectional layer has twice more parameters than a chronological LSTM. With some 
# regularization, the bidirectional approach would likely be a strong performer on this task.
# 
# Now let's try the same approach on the weather prediction task:

# In[15]:


from keras.models import Sequential
from keras import layers
from keras.optimizers import RMSprop

model = Sequential()
model.add(layers.Bidirectional(
    layers.GRU(32), input_shape=(None, float_data.shape[-1])))
model.add(layers.Dense(1))

model.compile(optimizer=RMSprop(), loss='mae')
history = model.fit_generator(train_gen,
                              steps_per_epoch=500,
                              epochs=40,
                              validation_data=val_gen,
                              validation_steps=val_steps)


# 
# It performs about as well as the regular GRU layer. It's easy to understand why: all of the predictive capacity must be coming from the 
# chronological half of the network, since the anti-chronological half is known to be severely underperforming on this task (again, because 
# the recent past matters much more than the distant past in this case).

# 
# ## Going even further
# 
# At this stage, there are still many other things you could try in order to improve performance on our weather forecasting problem:
# 
# * Adjust the number of units in each recurrent layer in the stacked setup. Our current choices are largely arbitrary and thus likely 
# suboptimal.
# * Adjust the learning rate used by our `RMSprop` optimizer.
# * Try using `LSTM` layers instead of `GRU` layers.
# * Try using a bigger densely-connected regressor on top of the recurrent layers, i.e. a bigger `Dense` layer or even a stack of `Dense` 
# layers.
# * Don't forget to eventually run the best performing models (in terms of validation MAE) on the test set! Least you start developing 
# architectures that are overfitting to the validation set.   
# 
# As usual: deep learning is more an art than a science, and while we can provide guidelines as to what is likely to work or not work on a 
# given problem, ultimately every problem is unique and you will have to try and evaluate different strategies empirically. There is 
# currently no theory that will tell you in advance precisely what you should do to optimally solve a problem. You must try and iterate.
# 
# 
# ## Wrapping up
# 
# Here's what you should take away from this section:
# 
# * As you first learned in Chapter 4, when approaching a new problem, 
# it is good to first establish common sense baselines for your metric of choice. If you don't have a 
# baseline to beat, you can't tell if you are making any real progress.
# * Try simple models before expensive ones, to justify the additional expense. Sometimes a simple model will turn out to be your best option.
# * On data where temporal ordering matters, recurrent networks are a great fit and easily outperform models that first flatten the temporal 
# data.
# * To use dropout with recurrent networks, one should use a time-constant dropout mask and recurrent dropout mask. This is built into Keras 
# recurrent layers, so all you have to do is use the `dropout` and `recurrent_dropout` arguments of recurrent layers.
# * Stacked RNNs provide more representational power than a single RNN layer. They are also much more expensive, and thus not always worth it. 
# While they offer clear gains on complex problems (e.g. machine translation), they might not always be relevant to smaller, simpler problems.
# * Bidirectional RNNs, which look at a sequence both ways, are very useful on natural language processing problems. However, they will not 
# be strong performers on sequence data where the recent past is much more informative than the beginning of the sequence.
# 
# Note there are two important concepts that we will not cover in detail here: recurrent "attention", and sequence masking. Both tend to be 
# especially relevant for natural language processing, and are not particularly applicable to our temperature forecasting problem. We will 
# leave them for future study outside of this book.
