# coding: utf-8

from poem_model import *
from config import Config
#加载模型（若无训练好的模型，会开始训练）
model = PoetryModel(Config)
print('model loaded')
model.train()

