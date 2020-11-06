

#coding:utf-8
import os.path
import keras
import matplotlib.pyplot as plt
#记录损失值和相关参数
class LossHistory(keras.callbacks.Callback):
    def __init__(self, train_path,model_name,train_id):

        plt.ion()  # iteration on
        plt.clf()

        self.train_path = train_path
        self.model_name = model_name
        self.train_id = train_id
    def on_train_begin(self, logs={}):
        self.losses = {'batch':[], 'epoch':[]}
        self.accuracy = {'batch':[], 'epoch':[]}
        self.val_loss = {'batch':[], 'epoch':[]}
        self.val_acc = {'batch':[], 'epoch':[]}

    def on_batch_end(self, batch, logs={}):
        self.losses['batch'].append(logs.get('loss'))
        self.accuracy['batch'].append(logs.get('acc'))
        self.val_loss['batch'].append(logs.get('val_loss'))
        self.val_acc['batch'].append(logs.get('val_acc'))
        dirname = self.train_path+'train_procese/'+str(self.train_id)+'/'
        filename=dirname +self.model_name+'_'+"_loss_batch.txt"
        oldFile=False
        if not os.path.exists(dirname):
            os.makedirs(dirname)
        if os.path.exists(filename):
            oldFile=True
        with open(filename,'a', encoding='utf-8') as f:
            if oldFile==False:
                f.writelines("loss,acc"+'\n')
            f.writelines(str(logs.get('loss'))+","+ str(logs.get('acc'))+'\n')

    def on_epoch_end(self, batch, logs={}):
        self.losses['epoch'].append(logs.get('loss'))
        self.accuracy['epoch'].append(logs.get('acc'))
        self.val_loss['epoch'].append(logs.get('val_loss'))
        self.val_acc['epoch'].append(logs.get('val_acc'))
        dirname = self.train_path + 'train_procese/' +str( self.train_id) + '/'
        filename = dirname + self.model_name + '_' + "_loss_epoch.txt"
        oldFile = False
        if not os.path.exists(dirname):
            os.makedirs(dirname)
        if os.path.exists(filename):
            oldFile = True
        with open(filename, 'a', encoding='utf-8') as f:
            if oldFile == False:
                f.writelines("loss,acc,val_loss,val_acc"+'\n');
            f.writelines(str(logs.get('loss'))+","+ str(logs.get('acc'))+','+str(logs.get('val_loss'))+","+str(logs.get('val_acc'))+'\n')
        self.loss_plot("epoch")
    def loss_plot(self, loss_type):
        iters = range(len(self.losses[loss_type]))
        plt.cla()
        # acc
        plt.plot(iters, self.accuracy[loss_type], 'r', label='train acc')
        # loss
        plt.plot(iters, self.losses[loss_type], 'g', label='train loss')

        if loss_type == 'epoch':
            # val_acc
            plt.plot(iters, self.val_acc[loss_type], 'b', label='val acc')
            # val_loss
            plt.plot(iters, self.val_loss[loss_type], 'k', label='val loss')
        plt.grid(True)
        plt.xlabel(loss_type)
        plt.ylabel('acc-loss')
        plt.legend(loc="upper right")
        plt.pause(0.1)
        #plt.savefig(self.train_path+'/train_procese/'+str(self.train_id)+'/'+self.model_name+'_'+"loss_batch.png")
        plt.show()

