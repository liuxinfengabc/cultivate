
from pgmpy.readwrite import BIFReader
reader = BIFReader('asia.bif')

asia_model = reader.get_model()

asia_model.nodes()
asia_model.edges()
asia_model.get_cpds()

# 用变量消去法来精确推理
from pgmpy.inference import VariableElimination
asia_infer = VariableElimination(asia_model)

# Computing the probability of bronc given smoke.
q = asia_infer.query(variables=['bronc'], evidence={'smoke': 0})
print(q['bronc'])
q = asia_infer.query(variables=['bronc'], evidence={'smoke': 1})
print(q['bronc'])