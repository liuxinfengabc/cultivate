    
https://www.cnblogs.com/lyrichu/p/11257408.html
    
最近在github 上发现了一个好玩的项目，一个基于LSTM + keras 实现的诗歌生成器，地址是:https://github.com/youyuge34/Poems_generator_Keras. 我去看了一下代码，实现的原理其实很common,就是普通的基于LSTM 的序列模型。模型使用了43030首诗进行训练,最后生成的诗歌还是有模有样的(当然仅限于有模有样，自然是不能深究的了)
 
模型的核心思路:
复制代码
1 input_tensor = Input(shape=(self.config.max_len, len(self.words)))
2 lstm = LSTM(512, return_sequences=True)(input_tensor)
3 dropout = Dropout(0.6)(lstm)
4 lstm = LSTM(256)(dropout)
5 dropout = Dropout(0.6)(lstm)
6 dense = Dense(len(self.words), activation='softmax')(dropout)
7 self.model = Model(inputs=input_tensor, outputs=dense)
8 optimizer = Adam(lr=self.config.learning_rate)
9 self.model.compile(loss='categorical_crossentropy', optimizer=optimizer, metrics=['accuracy'])
复制代码
keras 真的是极简的深度学习语言，上述短短的9行代码就已经包括了全部的模型结构，而且不需要注释基本都能看懂代码在做什么。
第一行: 构造输入向量
第二行，构造了一个LSTM layer, hidden units size = 512
第三行,构造了一个dropout layer,dropout rate = 0.6
第四行,构造了一个LSTM layer,hidden units size = 256
第五行,构造了一个dropout layer,dropout rate = 0.6
第六行，构造了一个全连接层+softmax 作为 output layer
第七行,利用 inputs 和 outputs 构造Model
第八行,使用 Adam 优化器
第九行,compile model,指定了模型的损失函数类型为交叉熵损失,优化器以及评价指标
 

2. 数据，https://github.com/youyuge34/Poems_generator_Keras/blob/master/dataset/poetry.txt，部分数据格式如下:
 
 首春:寒随穷律变，春逐鸟声开。初风飘带柳，晚雪间花梅。碧林青旧竹，绿沼翠新苔。芝田初雁去，绮树巧莺来。
 初晴落景:晚霞聊自怡，初晴弥可喜。日晃百花色，风动千林翠。池鱼跃不同，园鸟声还异。寄言博通者，知予物外志。
 初夏:一朝春夏改，隔夜鸟花迁。阴阳深浅叶，晓夕重轻烟。哢莺犹响殿，横丝正网天。珮高兰影接，绶细草纹连。碧鳞惊棹侧，玄燕舞檐前。何必汾阳处，始复有山泉。
度秋:夏律昨留灰，秋箭今移晷。峨嵋岫初出，洞庭波渐起。桂白发幽岩，菊黄开灞涘。运流方可叹，含毫属微理。
冒号前的是诗的名字，冒号后的是诗的内容
 
3. 训练
我使用的单卡 RTX2080ti(11G显存) 进行训练，按照作者的默认配置，一共 训练了 34000+ epoch,每个 epoch 耗时 1s 左右，总共训练了接近10个小时。最终得到的keras 可用的模型文件以及训练日志我放到百度云了，地址是:https://pan.baidu.com/s/1XV9InTe9vMmwKNs5lBS-tQ
 
4.训练完成之后，原始的代码一共提供了4个进行predict 的API:
predict_first:给定一个汉字，输出一首五言绝句
predict_random:随机从全部的训练诗作当中抽出一首诗的首句，然后生成一首诗
predict_gen:给定五个汉字作为首句，生成一首五言绝句
predict_hide:给定四个汉字，输出以这个四个汉字开头的藏头诗
为了方便预测，我封装了一个简单的 命令行工具，提供了四种预测，代码可以参考我对原始repo 的 fork 版本(https://github.com/Lyrichu/Poems_generator_Keras/tree/huchengchun),使用方式如下: