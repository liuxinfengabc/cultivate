from keras.models import Sequential
from keras.layers import Conv2D
from keras.layers import MaxPooling2D
from keras.layers import Dense
from keras.layers import Flatten
from keras.layers import Dropout
from keras.layers import BatchNormalization

filters = 32#滤镜个数
filter_size = 3#滤镜大小
pooling_size = 3
conv_layers = 2
dense_units = 256
dense_layers = 2
use_drop = True

my1stCNN = Sequential()
if (conv_layers == 1):
    my1stCNN.add(Conv2D(filters, (filter_size,filter_size), activation = 'relu', input_shape = (30,46,3)))
else:
    my1stCNN.add(Conv2D(filters, (filter_size,filter_size), activation = 'relu', input_shape = (30,46,3)))
for i in range(conv_layers -1):
  my1stCNN.add(Conv2D(64, (filter_size,filter_size), activation = 'relu'))
    
my1stCNN.add(MaxPooling2D(pool_size = (pooling_size,pooling_size)))
my1stCNN.add(Dropout(0.35))
my1stCNN.add(Flatten())
    
for i in range(dense_layers):
    my1stCNN.add(Dense(units = dense_units, activation = 'relu'))
if (use_drop):
    my1stCNN.add(Dropout(0.5))
    
my1stCNN.add(Dense(units= 10,activation='softmax'))
    
print(my1stCNN.summary())
    
    # optimizer, regularization
my1stCNN.compile(optimizer = 'adam', loss = 'categorical_crossentropy', metrics = ['accuracy'])
    
    # training
from keras.preprocessing.image import ImageDataGenerator
    
train_datagen = ImageDataGenerator(rescale = 1./255)
    
test_datagen = ImageDataGenerator(rescale = 1./255)
    
train_set = train_datagen.flow_from_directory('/Users/like/Downloads/data_set/train_set',
                                              target_size = (30,46),
                                              batch_size = 5,
                                              class_mode = 'categorical')
test_set = test_datagen.flow_from_directory('/Users/like/Downloads/data_set/test_set',
                                             target_size = (30,46),
                                             batch_size = 5,
                                             class_mode = 'categorical')
    
my1stCNN.fit_generator(train_set,
                       nb_epoch = 20,
                       steps_per_epoch = 400,
                       validation_data = test_set,
                       nb_val_samples = 300,
                       use_multiprocessing = True,
                       workers = 4)

##save architecture
#json_string = my1stCNN.to_json()
#open('my_model_architecture3.json','w').write(json_string)
##save weights
#my1stCNN.save_weights('my_model_weights3.h5')
