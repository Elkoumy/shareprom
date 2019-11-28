# -*- coding: utf-8 -*-
"""
Created on Tue Nov 26 08:43:48 2019

@author: Elkoumy
"""

import os
import pandas as pd


reslult_folder=r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Github\SecureMPCBPM\SecreC_implementation\experiment\results"

""" parsing profile files """

directory=r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\profiles_filtered3"

result=[]
col_names= ['server','dataset','chunks','preprocessing','per_chunk_sort','reshaping','shifting_and_subtraction','boolean_comparison','transpose_1','transpose_2','outerProduct','matrixMultiplication','colSums_1','colSums_2','combine','full_run']

list_of_folders=[x[0] for x in os.walk(directory)]

list_of_folders=list_of_folders[1:]

for folder in list_of_folders:
    current_server = folder.split("\\")[-1]
    list_of_files= [x for x in os.walk(folder)][0][2]
    for file in list_of_files:
        file_to_open=os.path.join(folder,file)

        with open(file_to_open) as f:
            content = f.readlines()
        content = [x.strip() for x in content] 
        current_result=[]
        
        for item in content:
            temp=item.split(';')
        
            current_result.append(temp[3])
            
        current_result=current_result[1:]
        if len(file.split('_'))==5:
            current_result=[current_server,file.split('_')[3], file.split('_')[4].split('.')[0]]+current_result
        elif len(file.split('_'))==4:
            current_result=[current_server,file.split('_')[3].split('.')[0], '1']+current_result
        if len(current_result)==16:
            result.append(current_result)
            
result= pd.DataFrame(result, columns=col_names)        

aggregated_result_time= result

del aggregated_result_time['server']

aggregated_result_time[['preprocessing','per_chunk_sort','reshaping','shifting_and_subtraction','boolean_comparison','transpose_1','transpose_2','outerProduct','matrixMultiplication','colSums_1','colSums_2','combine','full_run']] = aggregated_result_time[['preprocessing','per_chunk_sort','reshaping','shifting_and_subtraction','boolean_comparison','transpose_1','transpose_2','outerProduct','matrixMultiplication','colSums_1','colSums_2','combine','full_run']].apply(pd.to_numeric)

aggregated_result_time=aggregated_result_time.groupby(['dataset','chunks']).max()
aggregated_result_time=aggregated_result_time/1000/1000/60
aggregated_result_time=aggregated_result_time.reset_index()
aggregated_result_time.chunks=aggregated_result_time.chunks.astype('float')

aggregated_result_time['chunks_per_minute']=aggregated_result_time.chunks/aggregated_result_time.per_chunk_sort

#to get the total number of traces per dataset
temp= aggregated_result_time.groupby(['dataset']).chunks.max()
temp.reset_index()

aggregated_result_time=pd.merge(aggregated_result_time,temp,on='dataset')
aggregated_result_time['no_of_traces']=aggregated_result_time.chunks_y
del aggregated_result_time['chunks_y']
#traces per second
aggregated_result_time['events_per_second']=aggregated_result_time.no_of_traces/aggregated_result_time.full_run/60


#events per second
counts=pd.DataFrame([['credit',140000],['traffic',140000],['bpi',238000] ])
counts.columns=['dataset','no_of_events']
aggregated_result_time=pd.merge(aggregated_result_time,counts,on='dataset')

aggregated_result_time.to_csv(os.path.join(reslult_folder,'time_result3.csv'),index=0)

""" Parsing communication overhead files """    

directory=r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\communication3"

result=[]
col_names= ['server','dataset','iteration','chunks','sent','received']

list_of_folders=[x[0] for x in os.walk(directory)]

list_of_folders=list_of_folders[1:]

for folder in list_of_folders:
    current_server = folder.split("\\")[-1]
    list_of_files= [x for x in os.walk(folder)][0][2]
    for file in list_of_files:
        file_to_open=os.path.join(folder,file)

        with open(file_to_open) as f:
            content = f.readlines()
        content = [x.strip().split('\t') for x in content] 
        
        
        for item in content:
            if 'sharemind-server' in item[0]:
                if len(file.split('_'))==4:
                    temp=[current_server,file.split('_')[1],file.split('_')[3],file.split('_')[2],item[1],item[2]]
                else:
                    temp=[current_server,file.split('_')[1],file.split('_')[2],'1',item[1],item[2]]
                result.append(temp)
        
result= pd.DataFrame(result, columns=col_names) 
result.sent=result.sent.astype('float')
result.received=result.received.astype('float')

no_of_iteration=1
aggregated_result_comm=result.groupby(['server','dataset','chunks']).sum().reset_index()
aggregated_result_comm.received=aggregated_result_comm.received/1048576/ no_of_iteration
aggregated_result_comm.sent=aggregated_result_comm.sent/1048576/no_of_iteration

aggregated_result_comm.to_csv(os.path.join(reslult_folder,'communiation_result3.csv'),index=0)