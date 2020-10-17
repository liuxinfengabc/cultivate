import cv2
import sys
import gc
from face_train import *

labelFileName='./facedata/'+"label.csv"


def startshow():
    # 加载模型
    model = Model()
    model.load_model(file_path='./me.face.model.h5')

    # 框住人脸的矩形边框颜色
    color = (0, 255, 0)

    # 捕获指定摄像头的实时视频流
    cap = cv2.VideoCapture(0)

    # 人脸识别分类器本地存储路径
    cascade_path = "./haarcascade_frontalface_alt2.xml"

    # 标签数据
    dicLabel = {}
    with open(labelFileName, encoding="utf-8") as f:
        reader = csv.reader(f)
        for row in reader:
            dicLabel[row[2]] = row[1]

    # 循环检测识别人脸
    while True:
        _, frame = cap.read()  # 读取一帧视频

        # 图像灰化，降低计算复杂度
        frame_gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

        # 使用人脸识别分类器，读入分类器
        cascade = cv2.CascadeClassifier(cascade_path)

        # 利用分类器识别出哪个区域为人脸
        faceRects = cascade.detectMultiScale(frame_gray, scaleFactor=1.2, minNeighbors=3, minSize=(32, 32))
        if len(faceRects) > 0:
            for faceRect in faceRects:
                x, y, w, h = faceRect

                # 截取脸部图像提交给模型识别这是谁
                image = frame[y - 10: y + h + 10, x - 10: x + w + 10]
                faceID = model.face_predict(image)

                faceName=dicLabel[str(faceID)]


                if faceName == None:
                    faceName="二狗子";

                cv2.rectangle(frame, (x - 10, y - 10), (x + w + 10, y + h + 10), color, thickness=2)

                # 文字提示是谁

                font = cv2.FONT_HERSHEY_SIMPLEX
                cv2.putText(frame, 'name:%s' % (faceName), (x + 30, y + 30), font, 1, (255, 0, 255), 4)


        cv2.imshow("find me", frame)

        # 等待10毫秒看是否有按键输入
        k = cv2.waitKey(10)
        # 如果输入q则退出循环
        if k & 0xFF == ord('q'):
            break

            # 释放摄像头并销毁所有窗口
    cap.release()
    cv2.destroyAllWindows()


if __name__ == '__main__':
    startshow()