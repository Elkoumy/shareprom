# -*- coding: utf-8 -*-
"""
Created on Thu Nov  7 10:35:24 2019

@author: Elkoumy
"""

import sys
import pandas as pd
import time
from datetime import datetime

def to_list(s):
    return list(s)

def parse_time(x):
    res=0
    try:
      res=int(time.mktime(datetime.strptime(x,"%Y-%m-%d %H:%M:%S.%f%z").timetuple()))
    except:
      res=int(time.mktime(datetime.strptime(x,"%Y-%m-%d %H:%M:%S%z").timetuple()))
    
    return res
#input_file = sys.argv[1]
#output_file = sys.argv[2]

input_file=r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Github\SecureMPCBPM\data_and_preprocessing\BPI_Challenge_2017_3_columns.csv"
output_dir=r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Github\SecureMPCBPM\data_and_preprocessing"

data = pd.read_csv(input_file)
new_case_ids = pd.Index(data['case'].unique())
data['case'] = data['case'].apply(lambda x: new_case_ids.get_loc(x))
#df['event'] = pd.util.hash_pandas_object(df['event'],index=False)

""" generating relative time"""
#data['completeTime'] = data['completeTime'].apply(lambda x: int(time.mktime(datetime.strptime(x,"%Y-%m-%d %H:%M:%S.%f%z").timetuple())))
data['completeTime'] = data['completeTime'].apply(parse_time)
data.completeTime=data.completeTime-min(data.completeTime) 


""" Generating binary representation of the events """
#moving event to the last column
data=data[['case','completeTime','event']]

unique_events = list(data.event.unique())
#
ini_binary = "0"*(len(unique_events)-1)+"1"
event_idx= {}
for  event in unique_events:
    event_idx[event]= ini_binary
    ini_binary= ini_binary[1:]+"0"

bits_column_names=["b"+str(i) for i in range(0,len(unique_events))]    
data.event=data.event.apply(lambda x: event_idx[x])
temp= data.event.apply(to_list)
temp= pd.DataFrame.from_items(zip(temp.index, temp.values)).T    
data[bits_column_names]=temp




""" splitting the file over partyA and partyB """
party_A=pd.DataFrame()

party_B=pd.DataFrame()




party_A=data[~data.event.isin(['00000000000000001000000000','00000000000000001000000000','00000000000000010000000000'])]
party_B=data[data.event.isin(['00000000000000001000000000','00000000000000001000000000','00000000000000010000000000'])]

""" performing padding """

''' party A '''
counts = party_A.groupby("case").count().event
max_count= counts.max()


need_increase=counts[counts<max_count]
difference=max_count-need_increase


padded_value=[]
for i in difference.index:
    temp= difference[i] *[[i,0,0]]
    padded_value=padded_value+temp

padded_value=pd.DataFrame.from_records(padded_value)    

for i in range(0, len(unique_events)):
    padded_value['b'+str(i)]=0

padded_value.columns=party_A.columns

party_A= party_A.append(padded_value , ignore_index=True)
party_A= party_A.sort_values(by=['case', 'completeTime'])

''' party B'''

counts = party_B.groupby("case").count().event
max_count= counts.max()


need_increase=counts[counts<max_count]
difference=max_count-need_increase


padded_value=[]
for i in difference.index:
    temp= difference[i] *[[i,0,0]]
    padded_value=padded_value+temp

padded_value=pd.DataFrame.from_records(padded_value)    

for i in range(0, len(unique_events)):
    padded_value['b'+str(i)]=0

padded_value.columns=party_B.columns

party_B= party_B.append(padded_value , ignore_index=True)
party_B= party_B.sort_values(by=['case', 'completeTime'])


data.to_csv(output_dir+"\\"+"BPI_Challenge_2017_MPC.csv", index=0)
party_A.to_csv(output_dir+"\\"+"party_A_BPI_Challenge_2017_MPC.csv",index=0)
party_B.to_csv(output_dir+"\\"+"party_B_BPI_Challenge_2017_MPC.csv", index=0)     