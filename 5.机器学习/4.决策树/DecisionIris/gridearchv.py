from sklearn.model_selection import GridSearchCV, KFold, train_test_split
from sklearn.metrics import make_scorer, accuracy_score
from sklearn.tree import DecisionTreeClassifier
from sklearn.datasets import load_breast_cancer

data = load_breast_cancer()
X_train, X_test, y_train, y_test = train_test_split(data['data'], data['target'], train_size=0.8, random_state=0)
regressor = DecisionTreeClassifier(random_state=0)
parameters = {'max_depth': range(1, 6)}
scoring_fnc = make_scorer(accuracy_score)
kfold = KFold(n_splits=10)
grid = GridSearchCV(regressor, parameters, scoring_fnc, cv=kfold)
grid = grid.fit(X_train, y_train)
reg = grid.best_estimator_
print('best score: %f'%grid.best_score_)
print('best parameters:')
for key in parameters.keys():
    print('%s: %d'%(key, reg.get_params()[key]))
print('test score: %f'%reg.score(X_test, y_test))
import pandas as pd
pd.DataFrame(grid.cv_results_).T