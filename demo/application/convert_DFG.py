
import numpy as np
import pandas as pd
from math import sqrt

def convert_DFG(out_dir):
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
