
#  https://github.com/youyuge34/Poems_generator_Keras

请使用pycharm请单独打开该文件夹运行

Poems_generator_Keras
========
 
- 唐诗，古诗，五言绝句自动生成，基于Keras，LSTM-RNN。       
- 附带训练好的模型文件，可直接上手使用。         
- 功能：藏头诗，随机写诗，给定第一句诗/字进行作诗        
 
 

测试结果：
---------


在GPU Tesla K80上，2s/epoch，一共有3w+个epoch。训练时的测试结果令人满意：


==================Epoch 4304=====================     
县幽公事稀，上仙晓更高。风行随时朝，还云避倚里。     
病客与僧闲，来王不鹤星。火气北所晚，边飞无已去。     
玉律阳和变，下石凤明君。对动晨桂步，飞群安行金。      
==================Epoch 4308=====================          
绮阁云霞满，地国五自去。云人芳国思，云堂兵曲中。    
帝城深处寺，此梦与云色。朝枝使天何，水天开光时。    
石门千仞断，乡金在画使。天林东去结，北里石叶锦。     
==================Epoch 4312=====================        
王室今如毁，汉云树来寒。路平应人江，山不开云古。     
扰扰走人寰，文尘李气奉。时秋田客岁，高斗不南中。     
何以保孤危，都书丹道边。惜言为日芳，波垂日桃花。     
==================Epoch 4316=====================           
爱酒如偷蜜，心若大如去。入忆似烟春，如成台忆圣。     
我爱窦高士，如箭变素外。明成丹泥为，今风酒影重。     
画楼吹笛妓，何从出还玉。遍阴火川下，乘合未云虚。         



环境配置
-------
- python3
- tensorflow
- Keras
- h5py
- Jupyter
- numpy……      

 
食用指南
--------
```
from config import Config
#加载模型（若无训练好的模型，会开始训练）
model = PoetryModel(Config)
print('model loaded')

#藏头诗
sen = model.predict_hide('争云日夏')
print(sen)
```
 
输出结果：    
```
争空谁上尽，云云中林翠。日落危西烟，夏更无长塞。
```
 
**其他方法调用请看notebook，里面都有。**       



如何使用训练好的模型：
-------
1. 我训练好了一个h5模型，点击[页面](https://www.floydhub.com/youyuge34/projects/poems_generator/4/output/poetry_model.h5)中右侧的小箭头可下载，迅雷满速
2. 将`poetry_model.h5`放入根目录，记得改一下`self.loaded_model = True`
3. 注意，此模型只跑了4000+epoch，还有提升空间       
            
        
            
更新说明：
------------

**Version 1.1:**

- 代码大幅度重构，更加简洁                           
- 添加多个模型方法，可按需要生成诗句        
- notebook中添加样例        
 
    
**Version 1.0:**    
     
在[ioiogoo](https://github.com/ioiogoo/poetry_generator_Keras)的架构之上进行优化：

- 改用`Jupyter notebook`    
- 数据只使用五言绝句，否则输出不稳定    
- 修复`bugs`  
- 精简代码   
- 训练时的测试数据会写入`out/out.txt`   
- 训练时的测试输入为随机一首诗的开头，确保输出好看   
- ………………   
