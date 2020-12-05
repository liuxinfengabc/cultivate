#-*- coding: UTF-8 -*-
import threading
import time,datetime
import Logging
import randomforest_test


def fun_timer():
    try:
        while (datetime.datetime.strftime(datetime.datetime.now(), '%H') == '22'):  # 程序运行时间为每晚22点
            predictTime = datetime.datetime.now()
            Logging.logger.info("*****************************************************************************************************************************************************************************")
            Logging.logger.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++分割线+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++")
            Logging.logger.info("*****************************************************************************************************************************************************************************")
            Logging.logger.info("正在检测遮挡，当前时间为"+str(predictTime))
            result = randomforest_test.judgeTheShade(predictTime)
            if result == 0:
                Logging.logger.info("数据异常无法检测")
            time.sleep(79200)  # 睡22个小时
    except Exception as ex:
        Logging.logger.error(ex)

    global timer
    timer = threading.Timer(1800, fun_timer)
    timer.start()
    Logging.logger.info("不在预测月内或超出预测时间，但程序仍在运行!" +  datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S'))

timer = threading.Timer(1, fun_timer)
timer.start()
