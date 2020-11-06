import numpy as np  
from keras.preprocessing import image
from keras.applications.vgg16 import preprocess_input
def predictnumber(file_path,model):
    #file_path = '/Users/like/python_item/item_python_code/18.jpg'
    img = image.load_img(file_path, target_size=(30, 46))
    x = image.img_to_array(img)
    
    x = np.expand_dims(x, axis=0)
    x = preprocess_input(x)
    
    
    predict_test = model.predict_classes(x)
    #inverted = encoder.inverse_transform([predict_test])
    return predict_test
    #print(inverted[0]