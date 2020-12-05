#!/usr/bin/python
# coding:utf-8
import math
import datetime
startDate = datetime.datetime.strptime("2018-01-01","%Y-%m-%d")
endDate = datetime.datetime.now()
HourList = [8,9,10,11,12,13,14,15]
longitude_jinan = 117
longitude_beijing = 120
latitude = 36.67

'''*********************************计算两个日期之间差的天数*************************'''
def calculateNumberOfDays(startDate,endDate):
    d1 = startDate
    d2 = endDate
    NumberOfDays = (d2 - d1).days + 1
    return NumberOfDays


'''*********************************计算太阳高度角***************************************'''
def calculateSolarElevation(numberOfDays):
    b = 2*math.pi*(numberOfDays - 1)/365
    sigma = 0.006918-0.399912*math.cos(b)+0.070257*math.sin(b)-0.006758*math.cos(2*b)+0.000907*math.sin(2*b)-0.002697*math.cos(3*b)+0.00148*math.sin(3*b)
    timeDifference = float((longitude_jinan-longitude_beijing))/15
    SolarElevationList = []
    for item in HourList:
        st = item+timeDifference
        t = 15*(st-12)
        SolarElevationList.append(math.degrees(math.asin(math.sin(math.radians(latitude))*math.sin(math.radians(sigma))+math.cos(math.radians(latitude))*math.cos(math.radians(sigma))*math.cos(math.radians(t)))))
    return SolarElevationList


'''**********************************用字典实现switch，case************************************'''
def eight(startDate,endDate):
    solarElevation = calculateSolarElevation(calculateNumberOfDays(startDate,endDate))[0]

    return solarElevation


def nine(startDate,endDate):
    solarElevation = calculateSolarElevation(calculateNumberOfDays(startDate,endDate))[1]
    return solarElevation


def ten(startDate,endDate):
    solarElevation = calculateSolarElevation(calculateNumberOfDays(startDate,endDate))[2]
    return solarElevation


def eleven(startDate,endDate):
    solarElevation = calculateSolarElevation(calculateNumberOfDays(startDate,endDate))[3]
    return solarElevation


def twelve(startDate,endDate):
    solarElevation = calculateSolarElevation(calculateNumberOfDays(startDate,endDate))[4]
    return solarElevation


def thirteen(startDate,endDate):
    solarElevation = calculateSolarElevation(calculateNumberOfDays(startDate,endDate))[5]
    return solarElevation


def fourteen(startDate,endDate):
    solarElevation = calculateSolarElevation(calculateNumberOfDays(startDate,endDate))[6]
    return solarElevation


def fifteen(startDate,endDate):
    solarElevation = calculateSolarElevation(calculateNumberOfDays(startDate,endDate))[7]
    print solarElevation
    return solarElevation


timeDict = {'08':eight,'09':nine,'10':ten,'11':eleven,'12':twelve,'13':thirteen,'14':fourteen,'15':fifteen}


def getSolarElevation(time,startDate,endDate):
    solarElevation = timeDict.get(time)(startDate, endDate)
    return solarElevation



