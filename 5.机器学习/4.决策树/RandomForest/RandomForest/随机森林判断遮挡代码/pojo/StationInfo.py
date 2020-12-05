#coding:utf-8

import sys
print sys.version
class StationInfo() :

    def __init__(self, stationId, stationName, townName, countyName):
        self.stationId = stationId
        self.stationName = stationName
        self.townName = townName
        self.countyName = countyName

    def getStationName(self):
        return self.stationName

    def setPredictResult(self, predictResult):
        self.predictResult = predictResult

    def getPredictResult(self):
        return self.predictResult

    def getStationId(self):
        return self.stationId

    def getTownName(self):
        return self.townName

    def getCountyName(self):
        return self.countyName


