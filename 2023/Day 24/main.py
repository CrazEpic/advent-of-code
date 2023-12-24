import sympy
import re

# you must be crazy to think I would implement solving equations in java

stones = []
for line in open("b.txt"):
    line = line.strip()
    line = re.sub("[@,]", " ", line)
    line = re.sub("[ ]+", " ", line).strip()
    stone = [int(x) for x in line.split()]
    stones.append(stone)

xr, yr, zr, vxr, vyr, vzr = sympy.symbols("xr, yr, zr, vxr, vyr, vzr")
equations = []

# positions have to line up
# x_h + t * v_xh = x_r + t * v_xr
# solve for t = (x_r - x_h) / (v_xh - v_xr)
# do this for x, y, and z
# (x_r - x_h) / (v_xh - v_xr) = (y_r - y_h) / (v_yh - v_yr)
# also for z
# solve for unknowns: xr, yr, zr, vxr, vyr, vzr
for x, y, z, vx, vy, vz in stones:
    equations.append((xr - x) * (vy - vyr) - (yr - y) * (vx - vxr))
    equations.append((yr - y) * (vz - vzr) - (zr - z) * (vy - vyr))
    equations.append((zr - z) * (vx - vxr) - (xr - x) * (vz - vzr))
solution = sympy.solve(equations)
vars = solution[0]
print(solution)
print(vars[xr] + vars[yr] + vars[zr])