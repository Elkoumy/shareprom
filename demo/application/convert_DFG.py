
import numpy as np
import pandas as pd
from math import sqrt
from collections import Counter

def convert_DFG_to_matrix(out_dir):
    f = open(out_dir, "r")
    f=f.read()
    f= f.replace("-DFG_matrix = [","")
    f=f.replace("]","")
    f=f.split(',')

    freq=[]
    time=[]
    for ix, i in enumerate(f):
        if ix%2==0:
            freq.append(i)
        else:
            time.append(i)

    event_count =int(sqrt(len(freq)))
    freq=pd.DataFrame( np.array(freq).reshape(event_count, event_count))
    time=pd.DataFrame(  np.array(time).reshape(event_count, event_count))

    freq.to_csv(r"DFG_log/freq.out",index=0,header=0)
    time.to_csv(r"DFG_log/time.out", index=0, header=0)
    return freq,time


def convert_DFG_to_counter(df):
    dfg ={}
    for col in df.columns:
        for ix,val in enumerate(df[col]):
            if int(val) !=0:
                dfg[(str(col),str(df.index.values[ix]))]=float(val)
                # print("("+str(col) +","+str(df.index.values[ix])+") ="+str(val))

    return Counter(dfg)