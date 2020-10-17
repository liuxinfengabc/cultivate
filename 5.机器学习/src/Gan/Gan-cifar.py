# coding: utf-8

# In[6]:
#https://blog.csdn.net/github_39611196/article/details/84198545

'''
生成器（generator），代表造假分子
首先，创建一个“生成器（generator）”模型，它将一个矢量（从潜在空间 - 在训练期间随机采样）转换为候选图像。
GAN通常出现的许多问题之一是generator卡在生成的图像上，看起来像噪声。一种可能的解决方案是在鉴别器（discriminator）
和生成器（generator）上使用dropout。
'''
import keras
from keras import layers
import numpy as np
from  load_cifar10 import  load_data_cifar10

latent_dim = 32
height = 32
width = 32
channels = 3

#shape: 形状元组（整型），不包括batch size。for instance, shape=(32,) 表示了预期的输入将是一批32维的向量。
generator_input = keras.Input(shape=(latent_dim,))

# 首先，将输入转换为16x16 128通道的feature map
x = layers.Dense(128 * 16 * 16)(generator_input)
x = layers.LeakyReLU()(x)
x = layers.Reshape((16, 16, 128))(x)

# 然后，添加卷积层
x = layers.Conv2D(256, 5, padding='same')(x)
x = layers.LeakyReLU()(x)

# 上采样至 32 x 32
x = layers.Conv2DTranspose(256, 4, strides=2, padding='same')(x)
x = layers.LeakyReLU()(x)

# 添加更多的卷积层
x = layers.Conv2D(256, 5, padding='same')(x)
x = layers.LeakyReLU()(x)
x = layers.Conv2D(256, 5, padding='same')(x)
x = layers.LeakyReLU()(x)

# 生成一个 32x32 1-channel 的feature map
x = layers.Conv2D(channels, 7, activation='tanh', padding='same')(x)
generator = keras.models.Model(generator_input, x)
generator.summary()

# In[8]:


'''
discriminator(鉴别器)
创建鉴别器模型，它将候选图像（真实的或合成的）作为输入，并将其分为两类：“生成的图像”或“来自训练集的真实图像”。
'''
discriminator_input = layers.Input(shape=(height, width, channels))
x = layers.Conv2D(128, 3)(discriminator_input)
x = layers.LeakyReLU()(x)
x = layers.Conv2D(128, 4, strides=2)(x)
x = layers.LeakyReLU()(x)
x = layers.Conv2D(128, 4, strides=2)(x)
x = layers.LeakyReLU()(x)
x = layers.Conv2D(128, 4, strides=2)(x)
x = layers.LeakyReLU()(x)
x = layers.Flatten()(x)

# 重要的技巧（添加一个dropout层）
x = layers.Dropout(0, 4)(x)

# 分类层
x = layers.Dense(1, activation='sigmoid')(x)

discriminator = keras.models.Model(discriminator_input, x)
discriminator.summary()

# In[11]:


# 为了训练稳定，在优化器中使用学习率衰减和梯度限幅（按值）。
discriminator_optimizer = keras.optimizers.RMSprop(lr=8e-4, clipvalue=1.0, decay=1e-8)
discriminator.compile(optimizer=discriminator_optimizer, loss='binary_crossentropy')

# In[16]:


'''
The adversarial network:对抗网络
最后，设置GAN，它链接生成器（generator）和鉴别器（discrimitor）。 这是一种模型，经过训练，
将使生成器（generator）朝着提高其愚弄鉴别器（discrimitor）能力的方向移动。 该模型将潜在的空间点转换为分类决策，
“假的”或“真实的”，并且意味着使用始终是“这些是真实图像”的标签来训练。 所以训练`gan`将以一种方式更新
“发生器”的权重，使得“鉴别器”在查看假图像时更可能预测“真实”。 非常重要的是，将鉴别器设置为在训练
期间被冻结（不可训练）：训练“gan”时其权重不会更新。 如果在此过程中可以更新鉴别器权重，那么将训练鉴别
器始终预测“真实”。
'''
# 将鉴别器（discrimitor）权重设置为不可训练（仅适用于`gan`模型）
discriminator.trainable = False

gan_input = keras.Input(shape=(latent_dim,))
gan_output = discriminator(generator(gan_input))
gan = keras.models.Model(gan_input, gan_output)

gan_optimizer = keras.optimizers.RMSprop(lr=4e-4, clipvalue=1.0, decay=1e-8)
gan.compile(optimizer=gan_optimizer, loss='binary_crossentropy')

# In[19]:


'''
  开始训练了。
  每个epoch：
   *在潜在空间中绘制随机点（随机噪声）。
   *使用此随机噪声生成带有“generator”的图像。
   *将生成的图像与实际图像混合。
   *使用这些混合图像训练“鉴别器”，使用相应的目标，“真实”（对于真实图像）或“假”（对于生成的图像）。
   *在潜在空间中绘制新的随机点。
   *使用这些随机向量训练“gan”，目标都是“这些是真实的图像”。 这将更新发生器的权重（仅因为鉴别器在“gan”内被冻结）
   以使它们朝向获得鉴别器以预测所生成图像的“这些是真实图像”，即这训练发生器欺骗鉴别器。
'''
import os
from keras.preprocessing import image

# 导入CIFAR10数据集
(x_train, y_train), (_, _) = load_data_cifar10()  #keras.datasets.cifar10.load_data()

# 从CIFAR10数据集中选择frog类（class 6）
x_train = x_train[y_train.flatten() == 6]

# 标准化数据
x_train = x_train.reshape(
    (x_train.shape[0],) + (height, width, channels)).astype('float32') / 255.

iterations = 10000
batch_size = 20
save_dir = '.\\gan_image'

start = 0
# 开始训练迭代
for step in range(iterations):
    # 在潜在空间中抽样随机点
    random_latent_vectors = np.random.normal(size=(batch_size, latent_dim))

    # 将随机抽样点解码为假图像
    generated_images = generator.predict(random_latent_vectors)

    # 将假图像与真实图像进行比较
    stop = start + batch_size
    real_images = x_train[start: stop]
    combined_images = np.concatenate([generated_images, real_images])

    # 组装区别真假图像的标签
    labels = np.concatenate([np.ones((batch_size, 1)),
                             np.zeros((batch_size, 1))])
    # 重要的技巧，在标签上添加随机噪声
    labels += 0.05 * np.random.random(labels.shape)

    # 训练鉴别器（discrimitor）
    d_loss = discriminator.train_on_batch(combined_images, labels)

    # 在潜在空间中采样随机点
    random_latent_vectors = np.random.normal(size=(batch_size, latent_dim))

    # 汇集标有“所有真实图像”的标签
    misleading_targets = np.zeros((batch_size, 1))

    # 训练生成器（generator）（通过gan模型，鉴别器（discrimitor）权值被冻结）
    a_loss = gan.train_on_batch(random_latent_vectors, misleading_targets)

    start += batch_size
    if start > len(x_train) - batch_size:
        start = 0
    if step % 100 == 0:
        # 保存网络权值
        gan.save_weights('gan.h5')

        # 输出metrics
        print('discriminator loss at step %s: %s' % (step, d_loss))
        print('adversarial loss at step %s: %s' % (step, a_loss))

        # 保存生成的图像
        img = image.array_to_img(generated_images[0] * 255., scale=False)
        img.save(os.path.join(save_dir, 'generated_frog' + str(step) + '.png'))

        # 保存真实图像，以便进行比较
        img = image.array_to_img(real_images[0] * 255., scale=False)
        img.save(os.path.join(save_dir, 'real_frog' + str(step) + '.png'))

# In[ ]:


# 绘图
import matplotlib.pyplot as plt

# 在潜在空间中抽样随机点
random_latent_vectors = np.random.normal(size=(10, latent_dim))

# 将随机抽样点解码为假图像
generated_images = generator.predict(random_latent_vectors)

for i in range(generated_images.shape[0]):
    img = image.array_to_img(generated_images[i] * 255., scale=False)
    plt.figure()
    plt.imshow(img)

plt.show()
