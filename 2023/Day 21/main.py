import sympy as sy 
from sympy.polys.polyfuncs import interpolate

X = [65, 65 + 131, 65 + 131 + 131]
Y = [3849, 34331, 95175]
points = []
for i in range(len(X)):
    points.append((X[i], Y[i]))
x = sy.Symbol("x")
func = sy.interpolate(points, x)
# 15181*x**2/17161 + 30901*x/17161 - 95601/17161
def f(x):
    return 15181*x**2/17161 + 30901*x/17161 - 95601/17161
print(func)
print(f(26501365))
# 621289922886148.9 -> 621289922886149