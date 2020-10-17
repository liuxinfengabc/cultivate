import math
from math import sin,cos
#方程：-(cos(5*x) sin(5*y)+ cos(5*y) sin(5*z) + cos(5*z) sin(5*x)+ 1.3)>=0   %%表示一个区间
#范围（0<x<1；0<y<1；0<z<1）
#步长0.001
#坐标点满足方程用1表示；不满足方程用0表示
#输出0，1格式的文件

gridN=100;#每个维度方向的网格数量
m = [[[0]*gridN]*gridN]*gridN

text="";
for i in range(gridN):
    for j in range (gridN):
        for k in range(gridN):
            x=i/gridN
            y=j/gridN
            z=k/gridN
            m[i][j][k]=-(cos(5*x)*sin(5*y)+
                         cos(5*y)*sin(5*z)+cos(5*z)*sin(5*x)+1.3)
            if m[i][j][k]>=0:
                m[i][j][k]=1;
            else:
                m[i][j][k]=0;
            #print(str(m[i][j][k])+ ", " )
            text=text+str(m[i][j][k])+","
file = open("test.txt",'a')
file.write(text)
file.write('\n')
file.close()
print("保存文件成功")


