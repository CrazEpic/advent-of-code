import matplotlib.pyplot as plt
import pandas as pd

df = pd.read_csv("data.csv")
cycles = df.iloc[:, 0]
weights = df.iloc[:, 1]
plt.scatter(cycles, weights)
#plt.show()
print(df.describe())
print(df.iloc[:, 1].value_counts().sort_values())