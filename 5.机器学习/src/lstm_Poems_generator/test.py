# coding: utf-8

# In[1]:


from poem_model import *
from config import Config
model = PoetryModel(Config)

print('model loaded')


# In[9]:
for i in range(3):
    #藏头诗
    sen = model.predict_hide('深度工作')
    print(sen)
# In[10]:
for i in range(3):
    #给出第一句话进行预测
    sen = model.predict_sen('齐教练真好，')
    print(sen)

# In[11]:
for i in range(3):
    #给出第一个字进行预测
    sen = model.predict_first('山')
    print(sen)


# In[12]:
for temp in [0.5,1,1.5]:
    #随机抽取第一句话进行预测
    sen = model.predict_random(temperature=temp)
    print(sen)