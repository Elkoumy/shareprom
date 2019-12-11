# -*- coding: utf-8 -*-
"""
Created on Wed Dec 11 21:16:40 2019

@author: Elkoumy
"""
import os


data_dir=os.path.join(  os.path.dirname(os.path.dirname(__file__)) ,'data')

#reading party A Data
f=open(data_dir+"//party_A_Loan_Process_MPC.csv")

insertion_A=""
while (f.readline()):
    line=f.readline().replace('\n','').split(',')
    if len(line)>1:
        line.pop(2)
        str = 'row={'+",".join(line) +'};\n'
        str+='tdbInsertRow(ds, tbl, row);\n'
        insertion_A+=str
        
print(insertion_A)   


#reading party B Data
f=open(data_dir+"//party_B_Loan_Process_MPC.csv")

insertion_B=""
while (f.readline()):
    line=f.readline().replace('\n','').split(',')
    if len(line)>1:
        line.pop(2)
        str = 'row={'+",".join(line) +'};\n'
        str+='tdbInsertRow(ds, tbl, row);\n'
        insertion_B+=str
        
print(insertion_B)    