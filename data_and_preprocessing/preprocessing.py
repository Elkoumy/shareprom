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


#input_file = sys.argv[1]
#output_file = sys.argv[2]

input_file=r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Github\SecureMPCBPM\data_and_preprocessing\Loan_Process.csv"
output_dir=r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Github\SecureMPCBPM\data_and_preprocessing"

data = pd.read_csv(input_file)
new_case_ids = pd.Index(data['case'].unique())
data['case'] = data['case'].apply(lambda x: new_case_ids.get_loc(x))
#df['event'] = pd.util.hash_pandas_object(df['event'],index=False)

""" generating relative time"""
data['completeTime'] = data['completeTime'].apply(lambda x: int(time.mktime(datetime.strptime(x,"%Y-%m-%d %H:%M:%S").timetuple())))
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


for i in range(0,data.shape[0]):
    
    if data.event[i] in ('0000100','0001000'):
        party_B=party_B.append([list(data.ix[i])])
    else:
        party_A=party_A.append([list(data.ix[i])])

party_A.columns = list(data.columns)
party_B.columns = list(data.columns)


""" performing padding """
counts = party_A.groupby("case").count().event
max_count= counts.max()


for item in counts.iteritems():
    if item[1]<max_count:
        for i in range(0,max_count-item[1]):
            dic={'case' : item[0] , 'completeTime':0, 'event' : 0, }
            for i in range(0, len(unique_events)):
                dic['b'+str(i)]=0
            party_A= party_A.append(dic , ignore_index=True)

#data.event= data.event.astype('uint64')
party_A= party_A.sort_values(by=['case', 'completeTime'])


counts = party_B.groupby("case").count().event
max_count= counts.max()


for item in counts.iteritems():
    if item[1]<max_count:
        for i in range(0,max_count-item[1]):
            dic={'case' : item[0] , 'completeTime':0, 'event' : 0, }
            for i in range(0, len(unique_events)):
                dic['b'+str(i)]=0
            party_B= party_B.append(dic , ignore_index=True)

#data.event= data.event.astype('uint64')
party_B= party_B.sort_values(by=['case', 'completeTime'])



data.to_csv(output_dir+"\\"+"Loan_Process_MPC.csv", index=0)
party_A.to_csv(output_dir+"\\"+"party_A_Loan_Process_MPC.csv",index=0)
party_B.to_csv(output_dir+"\\"+"party_B_Loan_Process_MPC.csv", index=0)     