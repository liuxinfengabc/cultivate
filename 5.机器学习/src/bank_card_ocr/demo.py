from keras.models import model_from_json
from LineLocation import LineLocation
from create123froma import create123froma
from generateSingleNumber import generateSingleNumber
from predictnumber import predictnumber
PathOfpicture='/home/deep/demo/src/bank_card_ocr/5.jpeg'
LineLocation(PathOfpicture)
create123froma('/home/deep/demo/src/bank_card_ocr/a.jpg')
generateSingleNumber('/home/deep/demo/src/bank_card_ocr/123.jpg')
json_file = open('my_model_architecture3.json', 'r')
loaded_model_json = json_file.read()
json_file.close()
model = model_from_json(loaded_model_json)
# load weights into new model
model.load_weights("my_model_weights3.h5")
head='/home/deep/demo/src/bank_card_ocr/'
tail='.jpg'
result = [0]*19
for i in range(0,19):
    file_path=head+str(i)+tail
    result[i]=predictnumber(file_path,model)
    print(result[i])