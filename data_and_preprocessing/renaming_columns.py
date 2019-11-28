# -*- coding: utf-8 -*-
"""
Created on Wed Nov 20 09:44:59 2019

@author: Elkoumy
"""

import pandas as pd

#data1= pd.read_csv(r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\Road_Traffic_Fine_Management_Process.csv")
#
#output=pd.DataFrame()
#output['case']=data1['case:concept:name']
#output['event']=data1['concept:name']
#output['completeTime']=data1['time:timestamp']
#
#output.to_csv(r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\Road_Traffic_Fine_Management_Process_3_columns.csv", index=0)
#
#
#data1= pd.read_csv(r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\Hospital_log.csv")
#
#output=pd.DataFrame()
#output['case']=data1['case:concept:name']
#output['event']=data1['concept:name']
#output['completeTime']=data1['time:timestamp']
#
#output.to_csv(r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\Hospital_log_3_columns.csv",index=0)
#
#
#data1= pd.read_csv(r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\BPI_Challenge_2017.csv")
#
#output=pd.DataFrame()
#output['case']=data1['case:concept:name']
#output['event']=data1['concept:name']
#output['completeTime']=data1['time:timestamp']
#
#output.to_csv(r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\BPI_Challenge_2017_3_columns.csv",index=0)
#

data1= pd.read_csv(r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\CreditRequirement.csv")

output=pd.DataFrame()
output['case']=data1['case:concept:name']
output['event']=data1['concept:name']
output['completeTime']=data1['time:timestamp']

output.to_csv(r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\CreditRequirement_3_columns.csv",index=0)