#!/usr/bin/python
#-*- coding: utf-8 -*-

from sklearn.externals import joblib
import pandas as pd
import mod_db
import datetime
import randomforest_method
from pojo import StationInfo
# 强制转码
import sys

def judgeTheShade(Date):
    if sys.getdefaultencoding() != 'utf-8':
        reload(sys)
        sys.setdefaultencoding('utf-8')

    startDate = datetime.datetime.strptime("2018-01-01", "%Y-%m-%d")
    endDate = Date
    dateStr = datetime.datetime.strftime(endDate, "%Y-%m-%d")
    startTimeStr = dateStr + " 08:00:00"
    endtTimeStr = dateStr + " 15:00:00"
    year = datetime.datetime.strftime(endDate,"%Y")
    month = datetime.datetime.strftime(endDate,"%m")
    tableName = 'ls.energydata'+ year + month

    '''*******************************1、连接数据库，并从数据库读入数据***********************************'''

    sql ="select t6.datatime,t6.biaogandianliang,t6.hourpower, t6.lisanlv,t6.ChangZhanID,t6.pmax,t6.p,t6.generatelevel,t7.MingZi " \
         "stationName, t8.MingZi townName,t9.MingZi countyName from (select y.datatime, y.biaogandianliang, y.hourpower," \
         "y.lisanlv, x.ChangZhanID, x.pmax, y.p, y.p / x.pmax generatelevel from(select m.DataTime, m.biaogandianliang, " \
         "m.hourpower, m.lisanlv, m.ChangZhanID, MAX(m.hourpower / m.biaogandianliang) pmax from( SELECT a.DataTime, " \
         "a.`value` as biaogandianliang, a.hourpower hourpower, b.`value` lisanlv, a.ChangZhanID from (SELECT t1.DataTime," \
         " t1.BuJianCanShuID, t1.hourpower, t1.`value`, t1.ChangZhanID FROM " + tableName + " t1 WHERE DataTime >= " \
         "'"+startTimeStr+"' and DataTime <= '" + endtTimeStr +"' and BuJianCanShuID = 225) " \
         "a LEFT JOIN (SELECT t2.DataTime, t2.BuJianCanShuID, t2.`value`, t2.ChangZhanID FROM " + tableName+" t2 WHERE" \
         " DataTime >= '"+startTimeStr+"' and DataTime <= '" + endtTimeStr +"' and BuJianCanShuID = 254) b on a.DataTime" \
         " = b.DataTime and a.ChangZhanID = b.ChangZhanID ) m group by m.ChangZhanID) x  RIGHT JOIN (  SELECT n.DataTime, " \
         "n.biaogandianliang, n.hourpower, n.ChangZhanID,n.lisanlv, n.hourpower / n.biaogandianliang p from ( SELECT a.DataTime," \
         " a. `value` as biaogandianliang, a.hourpower hourpower, b.`value` lisanlv, a.ChangZhanID from " \
         " (SELECT t1.DataTime, t1.BuJianCanShuID, t1.hourpower, t1.`value`, t1.ChangZhanID" \
         " FROM " + tableName+" t1 WHERE DataTime >= '"+startTimeStr+"' and DataTime <= '" + endtTimeStr +"' and " \
         "BuJianCanShuID = 225) a  LEFT JOIN (SELECT t2.DataTime, t2.BuJianCanShuID, t2. `value`, t2.ChangZhanID FROM " \
         + tableName + " t2 WHERE DataTime >= '"+startTimeStr+"' and DataTime <= '" + endtTimeStr +"' and BuJianCanShuID " \
         "= 254) b on a.DataTime = b.DataTime and a.ChangZhanID = b.ChangZhanID ) n) y on x.changzhanid = y.changzhanid " \
         "order by y.ChangZhanID, y.DataTime)t6 INNER JOIN changzhan t7 on t6.ChangZhanID = t7.ID INNER JOIN subnet_ems t8 " \
         "on t7.SUBNETID = t8.ID INNER JOIN subnet_ems t9 on t8.parentId = t9.ID"
    print sql
    db = mod_db.database()
    result = db.fetch_all(sql)
    db.close()


    '''**************************2、读入数据，并将原始数据中的数据转换为dataframe形式***********************'''

    stationIDList = []
    generateLevelList = []
    dispersionRateList = []
    solarElevationList = []
    dateTimeList = []
    stationNameList = []
    countyNameList = []
    townNameList = []
    for i in range(0,len(result)-1):
        stationNameList.append(result[i]["stationName"])
        countyNameList.append(result[i]["countyName"])
        townNameList.append(result[i]["townName"])
        stationIDList.append(result[i]["ChangZhanID"])
        generateLevelList.append(result[i]["generatelevel"])
        dispersionRateList.append(result[i]["lisanlv"])
        time = datetime.datetime.strftime(result[i]["datatime"], "%H")
        dateTimeList.append(time)
        solarElevationList.append(randomforest_method.getSolarElevation(time,startDate,endDate))
    dataframe = pd.concat([pd.DataFrame({'dispersionRate': dispersionRateList}), pd.DataFrame({'generateLevel':
                          generateLevelList}), pd.DataFrame({'solarElevation':solarElevationList})], axis=1)#将列表拼接成dataframe

    inputData_dataframe = dataframe.dropna(axis=0, how='any')  # 清除包含空值的行


    '''*******************************3、读入训练好的随机森林模型***********************************'''
    clf = joblib.load("train_model2.m")  # 读入模型
    result = clf.predict(inputData_dataframe)  # 进行预测
    inputData_dataframe['predict'] = result
    # 添加其他相关变量
    outputData_dataframe = pd.concat([pd.DataFrame({'stationID':stationIDList}),pd.DataFrame({'countyName':countyNameList}),
                                      pd.DataFrame({'townName':townNameList}),pd.DataFrame({'dateTimeList':dateTimeList}),
                                      pd.DataFrame({'stationName':stationNameList}),inputData_dataframe], axis=1)
    print outputData_dataframe

    '''********************************4、计算电站遮挡个数*********************************************'''
    result_dict = {}
    stationInfo_dict = {}

    for i in range(0, len(outputData_dataframe)-1):
        if outputData_dataframe['predict'][i] > 0:  # 若有遮挡
            if result_dict.has_key(outputData_dataframe['stationID'][i]) == True:  # 判断之前是否有该电站的记录
                result_dict[outputData_dataframe['stationID'][i]] = result_dict[outputData_dataframe['stationID'][i]] + 1  # 若有则+1
            else:
                result_dict[outputData_dataframe['stationID'][i]] = 1  # 若无则创建
                stationId = outputData_dataframe['stationID'][i]
                stationName = outputData_dataframe['stationName'][i]
                townName = outputData_dataframe['townName'][i]
                countyName = outputData_dataframe['townName'][i]
                # 创建一个实体
                stationInfo = StationInfo.StationInfo(stationId, stationName, townName, countyName)
                stationInfo_dict[stationId] = stationInfo

    '''*******************************5、生成遮挡严重电站的字典******************************************'''
    shadeStationID_dict = {}
    for key,value in result_dict.items():
        if value > 6:
            stationInfo = stationInfo_dict[key]
            stationInfo.setPredictResult(value)
            shadeStationID_dict[key] = stationInfo

    '''*******************************6、将遮挡严重的电站写入数据库*************************************'''
    for key,value in shadeStationID_dict.items():
        stationInfo = shadeStationID_dict[key]
        strTemp= ",'决策编号101:"+ stationInfo.getCountyName() + stationInfo.getTownName()+stationInfo.getStationName()+"一天内"+str(stationInfo.getPredictResult())+ "个时刻遮挡，遮挡严重请及时处理'"
        sql = "INSERT INTO pms.pm_diagnosis (device_id, diagnosis_result, diagnosis_time,DEVICE_NAME,PROVINCE_NAME,CITY_NAME)" \
              " VALUES ("  +str(stationInfo.getStationId())+strTemp+",now(),'"+ stationInfo.getStationName() +"','"+ stationInfo.getCountyName()+"','"+stationInfo.getTownName()+"')"
        print(sql)
        db.update(sql)
        db.close()
print datetime.datetime.now()
nowhour = datetime.datetime.strftime(datetime.datetime.now(),'%H')
print nowhour
if nowhour == '22':
    print 1
else:
    print 0
'''shadeStationIdList = []
shadeStationNameList = []
shadeCountyNameList = []
shadeTownNameList = []
shadeResultList = []

for key,value in shadeStationID_dict.items():
    stationInfo = shadeStationID_dict[key]

    shadeStationIdList.append(stationInfo.getStationId())
    shadeStationNameList.append(stationInfo.getStationName())
    shadeCountyNameList.append(stationInfo.getCountyName())
    shadeTownNameList.append(stationInfo.getTownName())
    shadeResultList.append(stationInfo.getPredictResult())


aaa = pd.concat([pd.DataFrame({'stationId':shadeStationIdList}),pd.DataFrame({'stationName':shadeStationNameList}),pd.DataFrame({'countyName':shadeCountyNameList}),pd.DataFrame({'townName':shadeTownNameList}),pd.DataFrame({'Result':shadeResultList})],axis=1)
print aaa

'''
