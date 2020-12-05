from sklearn import datasets, model_selection, naive_bayes
import matplotlib.pyplot as plt
import numpy as np


def load_data(datasets_name='iris'):
    if datasets_name == 'iris':
        data = datasets.load_iris()  # 加载 scikit-learn 自带的 iris 鸢尾花数据集-分类
    elif datasets_name == 'wine': # 0.18.2 没有
        data = datasets.load_wine()  # 加载 scikit-learn 自带的 wine 红酒起源数据集-分类
    elif datasets_name == 'cancer':
        data = datasets.load_breast_cancer()  # 加载 scikit-learn 自带的 乳腺癌数据集-分类
    elif datasets_name == 'digits':
        data = datasets.load_digits()  # 加载 scikit-learn 自带的 digits 糖尿病数据集-回归
    elif datasets_name == 'boston':
        data = datasets.load_boston()  # 加载 scikit-learn 自带的 boston 波士顿房价数据集-回归
    else:
        pass

    return model_selection.train_test_split(data.data, data.target,test_size=0.25, random_state=0,stratify=data.target)
    # 分层采样拆分成训练集和测试集，测试集大小为原始数据集大小的 1/4


def test_GaussianNB(*data, show=False):
    X_train, X_test, y_train, y_test = data
    cls = naive_bayes.GaussianNB()
    cls.fit(X_train, y_train)
    # print('GaussianNB Training Score: %.2f' % cls.score(X_train, y_train))
    print('GaussianNB Testing Score: %.2f' % cls.score(X_test, y_test))

def test_MultinomialNB(*data, show=False):
    X_train, X_test, y_train, y_test = data
    cls = naive_bayes.MultinomialNB()
    cls.fit(X_train, y_train)
    # print('MultinomialNB Training Score: %.2f' % cls.score(X_train, y_train))
    print('MultinomialNB Testing Score: %.2f' % cls.score(X_test, y_test))
def test_MultinomialNB_alpha(*data, show=False):
    X_train, X_test, y_train, y_test = data
    alphas = np.logspace(-2, 5, num=200)
    train_scores = []
    test_scores = []
    for alpha in alphas:
        cls = naive_bayes.MultinomialNB(alpha=alpha)
        cls.fit(X_train, y_train)
        train_scores.append(cls.score(X_train, y_train))
        test_scores.append(cls.score(X_test, y_test))

    if show:
        ## 绘图:MultinomialNB 的预测性能随 alpha 参数的影响
        fig = plt.figure()
        ax = fig.add_subplot(1, 1, 1)
        ax.plot(alphas, train_scores, label='Training Score')
        ax.plot(alphas, test_scores, label='Testing Score')
        ax.set_xlabel(r'$\alpha$')
        ax.set_ylabel('score')
        ax.set_ylim(0, 1.0)
        ax.set_title('MultinomialNB')
        ax.set_xscale('log')
        plt.show()

    # print('MultinomialNB_alpha best train_scores %.2f' % (max(train_scores)))
    print('MultinomialNB_alpha best test_scores %.2f' % (max(test_scores)))

def test_BernoulliNB(*data, show=False):
    X_train, X_test, y_train, y_test = data
    cls = naive_bayes.BernoulliNB()
    cls.fit(X_train, y_train)
    # print('BernoulliNB Training Score: %.2f' % cls.score(X_train, y_train))
    print('BernoulliNB Testing Score: %.2f' % cls.score(X_test, y_test))

def test_BernoulliNB_alpha(*data, show=False):
    X_train, X_test, y_train, y_test = data
    alphas = np.logspace(-2, 5, num=200)
    train_scores = []
    test_scores = []
    for alpha in alphas:
        cls = naive_bayes.BernoulliNB(alpha=alpha)
        cls.fit(X_train, y_train)
        train_scores.append(cls.score(X_train, y_train))
        test_scores.append(cls.score(X_test, y_test))

    if show:
        ## 绘图-展示BernoulliNB 的预测性能随 alpha 参数的影响
        fig = plt.figure()
        ax = fig.add_subplot(1, 1, 1)
        ax.plot(alphas, train_scores, label='Training Score')
        ax.plot(alphas, test_scores, label='Testing Score')
        ax.set_xlabel(r'$\alpha$')
        ax.set_ylabel('score')
        ax.set_ylim(0, 1.0)
        ax.set_title('BernoulliNB')
        ax.set_xscale('log')
        ax.legend(loc='best')
        plt.show()

    # print('BernoulliNB_alpha best train_scores %.2f' % (max(train_scores)))
    print('BernoulliNB_alpha best test_scores %.2f' % (max(test_scores)))

def test_BernoulliNB_binarize(*data, show=False):
    X_train, X_test, y_train, y_test = data
    min_x = min(np.min(X_train.ravel()), np.min(X_test.ravel())) - 0.1
    max_x = max(np.max(X_train.ravel()), np.max(X_test.ravel())) + 0.1
    binarizes = np.linspace(min_x, max_x, endpoint=True, num=100)
    train_scores = []
    test_scores = []
    for binarize in binarizes:
        cls = naive_bayes.BernoulliNB(binarize=binarize)
        cls.fit(X_train, y_train)
        train_scores.append(cls.score(X_train, y_train))
        test_scores.append(cls.score(X_test, y_test))

    if show:
        ## 绘图-展示BernoulliNB 的预测性能随 binarize 参数的影响
        fig = plt.figure()
        ax = fig.add_subplot(1, 1, 1)
        ax.plot(binarizes, train_scores, label='Training Score')
        ax.plot(binarizes, test_scores, label='Testing Score')
        ax.set_xlabel('binarize')
        ax.set_ylabel('score')
        ax.set_ylim(0, 1.0)
        ax.set_xlim(min_x - 1, max_x + 1)
        ax.set_title('BernoulliNB')
        ax.legend(loc='best')
        plt.show()

    # print('BernoulliNB_binarize best train_scores %.2f' % (max(train_scores)))
    print('BernoulliNB_binarize best test_scores %.2f' % (max(test_scores)))

def testFuc(fuc,show = False,no_all = True):
    for i in ['iris', 'wine', 'cancer', 'digits', ]:
            print('\n====== %s ======\n' % i)
            X_train, X_test, y_train, y_test = load_data(datasets_name=i)  # 产生用于分类问题的数据集
            if no_all:
                fuc(X_train, X_test, y_train, y_test,show = show)
            else:
                test_GaussianNB(X_train, X_test, y_train, y_test,show = show)  # 调用 test_GaussianNB
                test_MultinomialNB(X_train,X_test,y_train,y_test,show = show) # 调用 test_MultinomialNB
                test_MultinomialNB_alpha(X_train, X_test, y_train, y_test,show = show)  # 调用 test_MultinomialNB_alpha
                test_BernoulliNB(X_train,X_test,y_train,y_test,show = show) # 调用 test_BernoulliNB
                test_BernoulliNB_alpha(X_train, X_test, y_train, y_test,show = show)  # 调用 test_BernoulliNB_alpha
                test_BernoulliNB_binarize(X_train, X_test, y_train, y_test,show = show)  # 调用 test_BernoulliNB_binarize

#testFuc(test_GaussianNB)
testFuc(test_MultinomialNB,no_all = False)