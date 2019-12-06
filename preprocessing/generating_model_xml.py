# -*- coding: utf-8 -*-
"""
Created on Thu Nov 21 12:10:03 2019

@author: Elkoumy
"""
s=""
name="reduced_bpi"
s+="<table name=\""+name+"\" dataSource=\"DS1\" \n handler=\"import-script.sb\"> \n <column key=\"true\" type=\"primitive\">\n    <source name=\"case\" type=\"uint32\"/>\n    <target name=\"case\" type=\"uint32\"/>\n  </column>\n  <column key=\"false\" type=\"primitive\">\n<source name=\"completeTime\" type=\"uint32\"/>\n    <target name=\"completeTime\" type=\"uint32\"/>\n  </column>"
  
for i in range(0,7):
    s+=" <column key=\"false\" type=\"primitive\">\n    <source name=\"b"+str(i)+"\" type=\"uint8\"/>\n    <target name=\"b"+str(i)+"\" type=\"uint8\"/>\n  </column>"


s=s+"\n</table>"

print(s)   


#import pandas as pd
#
#data=pd.read_csv(r'C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\party_B_Hospital_log_MPC.csv',nrows=2000) 
#
