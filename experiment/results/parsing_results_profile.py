# -*- coding: utf-8 -*-
"""
Created on Tue Nov 26 08:43:48 2019

@author: Elkoumy
"""

import os
import pandas as pd

reslult_folder = r"../profiles"

""" parsing profile files """

profile_dir = r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Shareprom\experiment\profiles\main.2776.csv"

result = []
col_names = ['server', 'dataset', 'chunks', 'preprocessing', 'per_chunk_sort', 'reshaping', 'shifting_and_subtraction',
             'boolean_comparison', 'transpose_1', 'transpose_2', 'outerProduct', 'matrixMultiplication', 'colSums_1',
             'colSums_2', 'combine', 'full_run']

cnt =0
result=[]
with open(profile_dir) as f:
    content = f.readlines()
    content = [x.strip() for x in content]
    for line in content:
        if cnt!=0:
            row=[]
            values= line.split(';')
            field1=values[0].split("_")

            row.append(field1[-3])
            row.append(field1[-2])
            row.append(field1[-1])
            row.append("_".join(field1[:-3]))
            row.append(values[3])
            result.append(row)

        cnt=cnt+1

df = pd.DataFrame.from_records(result)
df.columns=["dataset","var_value","chunks","step","duration"]
print(df)

