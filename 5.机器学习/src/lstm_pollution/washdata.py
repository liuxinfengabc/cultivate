import pandas as pd
from pandas import read_csv
import datetime
from datetime import datetime

# load data  清洗数据
'''def parse(x):
	return datetime.strptime(x, '%Y %m %d %H')'''

'''parse = lambda dates: pd.datetime.strptime(dates, '%Y %m %d %H')
dataset = read_csv('raw.csv',  parse_dates = [['year', 'month', 'day', 'hour']], index_col=0, date_parser=parse)
# parse_dates列表清单。例如If [[1，3]]:组合列1和3并解析为单个日期列。'''


def parser(x):
    return datetime.strptime(x, '%Y %m %d %H')

dataset = read_csv('raw2.csv', parse_dates=[['year', 'month', 'day', 'hour']], index_col=0,
                   date_parser=parser)
dataset.drop('No', axis=1, inplace=True)  # axis=1,删除列；inplace=True,直接在原DataFrame上执行删除

# manually specify column names
dataset.columns = ['pollution', 'dew', 'temp', 'press', 'wnd_dir', 'wnd_spd', 'snow', 'rain']
dataset.index.name = 'date'
# mark all NA values with 0
dataset['pollution'].fillna(0, inplace=True)
# drop the first 24 hours
dataset = dataset[24:]
# summarize first 5 rows
print(dataset.head(5))
# save to file
dataset.to_csv('pollution.csv')