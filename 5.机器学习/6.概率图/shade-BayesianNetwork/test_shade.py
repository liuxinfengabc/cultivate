
from pgmpy.readwrite import BIFReader
reader = BIFReader('shade.bif')

asia_model = reader.get_model()

asia_model.nodes()
asia_model.edges()
asia_model.get_cpds()

# 用变量消去法来精确推理
from pgmpy.inference import VariableElimination
asia_infer = VariableElimination(asia_model)

# Computing the probability of bronc given smoke.
q = asia_infer.query(variables=['Shade'], evidence={'SolarElevation':1})
print(q['Shade'])
q = asia_infer.query(variables=['Shade'], evidence={'GenerateLevel': 1})
print(q['Shade'])
q = asia_infer.query(variables=['Shade'], evidence={'DipersionRatio':2})
print(q['Shade'])
q = asia_infer.query(variables=['Shade'], evidence={'GenerateLevel': 1,'DipersionRatio':2})
print(q['Shade'])