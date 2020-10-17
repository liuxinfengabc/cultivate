
# coding: utf-8

# In[1]:


import random
import os

import keras
import numpy as np
from keras.callbacks import LambdaCallback
from keras.models import Input, Model, load_model
from keras.layers import LSTM, Dropout, Dense
from keras.optimizers import Adam

from data_utils import *


# In[7]:


class PoetryModel(object):
    def __init__(self, config):
        self.model = None
        self.do_train = True
        self.loaded_model = True
        self.config = config

        # 文件预处理
        # words 100个单词

        self.word2numF, self.num2word, self.words, self.files_content = preprocess_file(self.config)
        feature_len = len(self.words)
        # 诗的list
        self.poems = self.files_content.split(']')
        # 诗的总数量
        self.poems_num = len(self.poems)
        
        # 如果模型文件存在则直接加载模型，否则开始训练
        if os.path.exists(self.config.weight_file) and self.loaded_model:
            self.model = load_model(self.config.weight_file)
        else:
            self.train()

    def build_model(self):
        '''建立模型'''
        print('building model')

        # 输入的dimension   lxf
        # time_step =self.config.max_len=6 根据前6个单词预测低7个；input_dim 5552个单词  feature?
        # shape(6,5552)
        feature_len=len(self.words)
        input_tensor = Input(shape=(self.config.max_len, len(self.words)))
        lstm = LSTM(512, return_sequences=True)(input_tensor)
        dropout = Dropout(0.6)(lstm)
        lstm = LSTM(256)(dropout)
        dropout = Dropout(0.6)(lstm)
        dense = Dense(len(self.words), activation='softmax')(dropout)
        self.model = Model(inputs=input_tensor, outputs=dense)
        optimizer = Adam(lr=self.config.learning_rate)
        self.model.compile(loss='categorical_crossentropy', optimizer=optimizer, metrics=['accuracy'])

    def sample(self, preds, temperature=1.0):
        '''
        当temperature=1.0时，模型输出正常
        当temperature=0.5时，模型输出比较open
        当temperature=1.5时，模型输出比较保守
        在训练的过程中可以看到temperature不同，结果也不同
        就是一个概率分布变换的问题，保守的时候概率大的值变得更大，选择的可能性也更大
        '''
        preds = np.asarray(preds).astype('float64')
        exp_preds = np.power(preds,1./temperature)
        preds = exp_preds / np.sum(exp_preds)
        pro = np.random.choice(range(len(preds)),1,p=preds)
        return int(pro.squeeze())
    
    def generate_sample_result(self, epoch, logs):
        '''训练过程中，每4个epoch打印出当前的学习情况'''
        if epoch % 4 != 0:
            return
        
        with open('out/out.txt', 'a',encoding='utf-8') as f:
            f.write('==================Epoch {}=====================\n'.format(epoch))
                
        print("\n==================Epoch {}=====================".format(epoch))
        for diversity in [0.7, 1.0, 1.3]:
            print("------------Diversity {}--------------".format(diversity))
            generate = self.predict_random(temperature=diversity)
            print(generate)
            
            # 训练时的预测结果写入txt
            with open('out/out.txt', 'a',encoding='utf-8') as f:
                f.write(generate+'\n')
    
    def predict_random(self,temperature = 1):
        '''随机从库中选取一句开头的诗句，生成五言绝句'''
        if not self.model:
            print('model not loaded')
            return
        
        index = random.randint(0, self.poems_num)
        sentence = self.poems[index][: self.config.max_len]
        generate = self.predict_sen(sentence,temperature=temperature)
        return generate
    
    def predict_first(self, char,temperature =1):
        '''根据给出的首个文字，生成五言绝句'''
        if not self.model:
            print('model not loaded')
            return
        
        index = random.randint(0, self.poems_num)
        #选取随机一首诗的最后max_len字符+给出的首个文字作为初始输入
        sentence = self.poems[index][1-self.config.max_len:] + char
        generate = str(char)
#         print('first line = ',sentence)
        # 直接预测后面23个字符
        generate += self._preds(sentence,length=23,temperature=temperature)
        return generate
    
    def predict_sen(self, text,temperature =1):
        '''根据给出的前max_len个字，生成诗句'''
        '''此例中，即根据给出的第一句诗句（含逗号），来生成古诗'''
        if not self.model:
            return
        max_len = self.config.max_len
        if len(text)<max_len:
            print('length should not be less than ',max_len)
            return

        sentence = text[-max_len:]
        print('the first line:',sentence)
        generate = str(sentence)
        generate += self._preds(sentence,length = 24-max_len,temperature=temperature)
        return generate
    
    def predict_hide(self, text,temperature = 1):
        '''根据给4个字，生成藏头诗五言绝句'''
        if not self.model:
            print('model not loaded')
            return
        if len(text)!=4:
            print('藏头诗的输入必须是4个字！')
            return
        
        index = random.randint(0, self.poems_num)
        #选取随机一首诗的最后max_len字符+给出的首个文字作为初始输入
        sentence = self.poems[index][1-self.config.max_len:] + text[0]
        generate = str(text[0])
        print('first line = ',sentence)
        
        for i in range(5):
            next_char = self._pred(sentence,temperature)           
            sentence = sentence[1:] + next_char
            generate+= next_char
        
        for i in range(3):
            generate += text[i+1]
            sentence = sentence[1:] + text[i+1]
            for i in range(5):
                next_char = self._pred(sentence,temperature)           
                sentence = sentence[1:] + next_char
                generate+= next_char

        return generate
    
    
    def _preds(self,sentence,length = 23,temperature =1):
        '''
        sentence:预测输入值
        lenth:预测出的字符串长度
        供类内部调用，输入max_len长度字符串，返回length长度的预测值字符串
        '''
        sentence = sentence[:self.config.max_len]
        generate = ''
        for i in range(length):
            pred = self._pred(sentence,temperature)
            generate += pred
            sentence = sentence[1:]+pred
        return generate
        
        
    def _pred(self,sentence,temperature =1):
        '''内部使用方法，根据一串输入，返回单个预测字符'''
        if len(sentence) < self.config.max_len:
            print('in def _pred,length error ')
            return
        
        sentence = sentence[-self.config.max_len:]
        x_pred = np.zeros((1, self.config.max_len, len(self.words)))
        for t, char in enumerate(sentence):
            feat=self.word2numF(char)
            x_pred[0, t, feat] = 1.
        preds = self.model.predict(x_pred, verbose=0)[0]
        next_index = self.sample(preds,temperature=temperature)
        next_char = self.num2word[next_index]
        
        return next_char

    def data_generator(self):
        '''生成器生成数据'''
        i = 0
        while 1:
            x = self.files_content[i: i + self.config.max_len]
            y = self.files_content[i + self.config.max_len]

            if ']' in x or ']' in y:
                i += 1
                continue

            y_vec = np.zeros(
                shape=(1, len(self.words)),
                dtype=np.bool
            )
            y_vec[0, self.word2numF(y)] = 1.0

            x_vec = np.zeros(
                shape=(1, self.config.max_len, len(self.words)),
                dtype=np.bool
            )

            for t, char in enumerate(x):
                x_vec[0, t, self.word2numF(char)] = 1.0

            yield x_vec, y_vec
            i += 1

    def train(self):
        '''训练模型'''
        print('training')
        number_of_epoch = len(self.files_content)-(self.config.max_len + 1)*self.poems_num
        number_of_epoch /= self.config.batch_size 
        number_of_epoch = int(number_of_epoch / 1.5)
        print('epoches = ',number_of_epoch)
        print('poems_num = ',self.poems_num)
        print('len(self.files_content) = ',len(self.files_content))

        if not self.model:
            self.build_model()

        self.model.fit_generator(
            generator=self.data_generator(),
            verbose=True,
            steps_per_epoch=self.config.batch_size,
            epochs=number_of_epoch,
            callbacks=[
                keras.callbacks.ModelCheckpoint(self.config.weight_file, save_weights_only=False),
                LambdaCallback(on_epoch_end=self.generate_sample_result)
            ]
        )
