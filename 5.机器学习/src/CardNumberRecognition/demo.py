from keras.models import model_from_json
from LineLocation import LineLocation
from create123froma import create123froma
from generateSingleNumber import generateSingleNumber
from predictnumber import predictnumber
PathOfpicture='/Users/like/Desktop/test/test_images/5.jpeg'
LineLocation(PathOfpicture)
create123froma('/Users/like/Downloads/asd/a.jpg')
generateSingleNumber('/Users/like/Downloads/asd/123.jpg','/Users/like/Downloads/asd/a.jpg')
json_file = open('my_model_architecture3.json', 'r')
loaded_model_json = json_file.read()
json_file.close()
model = model_from_json(loaded_model_json)
# load weights into new model
model.load_weights("my_model_weights3.h5")
head='/Users/like/Downloads/asd/'
tail='.jpg'
result = [0]*19
for i in range(0,19):
    file_path=head+str(i)+tail
    result[i]=predictnumber(file_path,model)