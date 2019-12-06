# -*- coding: utf-8 -*-
"""
Created on Wed Dec  4 16:03:45 2019

@author: Elkoumy
"""

# -*- coding: utf-8 -*-
"""
Created on Wed Dec  4 15:47:11 2019

@author: Elkoumy
"""

# -*- coding: utf-8 -*-
"""
Created on Thu Nov  7 10:35:24 2019

@author: Elkoumy
"""

import sys
import os
import pandas as pd
import time
from datetime import datetime

def to_list(s):
    return list(s)

def generate_rows(s):
    return s*[[s,0,0]]

#input_file = sys.argv[1]
#output_file = sys.argv[2]


input_dir=r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets"
output_dir=r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets"

file_name= "BPI_2013_3_columns"
input_file= os.path.join(input_dir,file_name+".csv")

data = pd.read_csv(input_file)
new_case_ids = pd.Index(data['case'].unique())
data['case'] = data['case'].apply(lambda x: new_case_ids.get_loc(x))
#df['event'] = pd.util.hash_pandas_object(df['event'],index=False)

""" generating relative time"""
data['completeTime'] = data['completeTime'].apply(lambda x: int(time.mktime(datetime.strptime(x,"%Y-%m-%d %H:%M:%S%z").timetuple())))
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


bits_size= len(bits_column_names)
events_in_b=[]

for i in range (bits_size//2+2):
    s=""
    for j in range(bits_size):
        if j==i:
            s+="1"
        else:
            s+="0"
    events_in_b.append(s)   
     
print(events_in_b)

party_A=data[~data.event.isin(events_in_b)]
party_B=data[data.event.isin(events_in_b)]


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




data.to_csv(os.path.join(output_dir,file_name+"_MPC.csv"), index=0)
party_A.to_csv(os.path.join(output_dir,"party_A_"+file_name+"_MPC.csv"),index=0)
party_B.to_csv(os.path.join(output_dir,"party_B_"+file_name+"_MPC.csv"), index=0)     


''' generating xml model '''''
#models_directory=
models_dir=os.path.abspath(os.path.join(os.path.dirname( __file__ ), '..', 'models'))

s=""
name="bpi13"
s+="\" dataSource=\"DS1\" \n handler=\"import-script.sb\"> \n <column key=\"true\" type=\"primitive\">\n    <source name=\"case\" type=\"uint32\"/>\n    <target name=\"case\" type=\"uint32\"/>\n  </column>\n  <column key=\"false\" type=\"primitive\">\n<source name=\"completeTime\" type=\"uint32\"/>\n    <target name=\"completeTime\" type=\"uint32\"/>\n  </column>"
  
for i in range(0,bits_size):
    s+=" <column key=\"false\" type=\"primitive\">\n    <source name=\"b"+str(i)+"\" type=\"uint8\"/>\n    <target name=\"b"+str(i)+"\" type=\"uint8\"/>\n  </column>"


s=s+"\n</table>" 


model = "<table name=\""+name+s

print(model)


text_file = open(os.path.join(models_dir,name+"_model.xml"), "w")
text_file.write(model)
text_file.close()


model = "<table name=\""+name+"_party_A"+s

print(model)


text_file = open(os.path.join(models_dir,name+"_model_party_A.xml"), "w")
text_file.write(model)
text_file.close()


model = "<table name=\""+name+"_party_B"+s

print(model)


text_file = open(os.path.join(models_dir,name+"_model_party_B.xml"), "w")
text_file.write(model)
text_file.close()