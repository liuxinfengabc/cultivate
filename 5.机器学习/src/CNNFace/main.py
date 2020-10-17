from tkinter import *
from camera import CatchPICFromVideo
from findme import *
import tkinter.messagebox #这个是消息框，对话框的关键
import os
import pandas as pd
import csv

IMAGE_SIZE = 64
rootdir = './facedata/'
facedir= './facedata/face/'
labelFileName='./facedata/'+"label.csv"
feature = 0


def get_screen_size(window):
    return window.winfo_screenwidth(), window.winfo_screenheight()


def get_window_size(window):
    return window.winfo_reqwidth(), window.winfo_reqheight()


def center_window(root, width, height):
    screenwidth = root.winfo_screenwidth()
    screenheight = root.winfo_screenheight()
    size = '%dx%d+%d+%d' % (width, height, (screenwidth - width) / 2, (screenheight - height) / 2)
    #print(size)
    root.geometry(size)

def createmodel():
    dataset = Dataset(facedir,labelFileName)
    print(os.listdir(facedir))
    nb_classes = len(os.listdir(facedir))+1  #多一个未知分类
    dataset.load(IMAGE_SIZE, IMAGE_SIZE,  3,nb_classes)#自动获取分类信息
    model = Model()
    model.build_model(dataset,nb_classes)
    model.train(dataset)
    model.save_model(file_path='./me.face.model.h5')
    tkinter.messagebox.showinfo('提示','模型建立完成')

def beginCaputre(labelName,photoNumber):

    dirFacePath=facedir+labelName

    if(re.match('([a-z]{3,16})',labelName)):
        if not os.path.exists(facedir):
            os.mkdir(facedir)
        if not os.path.exists(dirFacePath):
            os.mkdir(dirFacePath)
        currentClasses = len(os.listdir(facedir))

        with open(labelFileName, 'a', newline='') as f:
            writer = csv.writer(f)
            writer.writerow([dirFacePath, labelName, currentClasses])
        CatchPICFromVideo("get face",0, photoNumber,dirFacePath)
    else:
        tkinter.messagebox.showerror('错误', '输入错误')

def mainFrame():
    root.title('信息采集')
    startButton.destroy()
    quitButton.destroy()
    hideButton.destroy()
    showButton.destroy()
    center_window(root, 300, 240)
    root.maxsize(600, 400)
    root.minsize(300, 240)
    text = Entry(root,width=12,textvariable=newnametext)
    text.place(x=80, y=100)
    info = Label(root,text='目录名')
    info.place(x=30, y=100)
    textphone = Entry(root, width=12, textvariable=numphone)
    textphone.place(x=80, y=140)
    num = Label(root, text='照片数量')
    num.place(x=20, y=140)

    getinfoButton = Button(root, text="开始采集", command=lambda : beginCaputre(newnametext.get(),numphone.get()))
    getinfoButton.place(x=190, y=120)

    returnButton = Button(root, text="返回", command=lambda : returnaction(text,info,getinfoButton,returnButton,textphone,num))
    returnButton.place(x=250, y=120)


def result():
    startshow()




root = Tk()
newnametext = StringVar()
nametext = StringVar()
numphone = IntVar()
root.title('智能识别系统')


center_window(root, 300, 240)
root.maxsize(600, 400)
root.minsize(300, 240)


startButton = Button(root, text="信息采集", command=mainFrame)

startButton.place(x=10, y=10)

quitButton = Button(root, text="模型生成",command=createmodel)
quitButton.place(x=80, y=10)


showButton = Button(root, text="智能识别",command=result)
showButton.place(x=10, y=50)

hideButton = Button(root, text="退出",command=root.quit)
hideButton.place(x=80, y=50)
root.mainloop()

