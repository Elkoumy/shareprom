# -*- coding: utf-8 -*-
"""
Created on Fri Nov 22 10:51:05 2019

@author: Elkoumy
"""
import pandas as pd

traces_per_file=1000

file = r'C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\BPI_Challenge_2017_MPC.csv'
target_file=r'C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\reduced_BPI_Challenge_2017_MPC.csv'
data = pd.read_csv(file, nrows=traces_per_file*(173+65))
#
#data.to_csv(target_file,index=0)
#
#for line in reversed(list(open(file))):
#    s=line.rstrip()
#    s=s.split(',')
#    print('bpi')
#    print(s[0])
#    break
#
#
#file = r'C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\party_A_BPI_Challenge_2017_MPC.csv'
#target_file = r'C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\reduced_party_A_BPI_Challenge_2017_MPC.csv'
#data = pd.read_csv(file, nrows=traces_per_file*173)
#
#data.to_csv(target_file,index=0)
#
#
#file = r'C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\party_B_BPI_Challenge_2017_MPC.csv'
#target_file = r'C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\reduced_party_B_BPI_Challenge_2017_MPC.csv'
#data = pd.read_csv(file, nrows=traces_per_file*65)
#
#data.to_csv(target_file,index=0)



#file = r'C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\Hospital_log_MPC.csv'
#target_file = r'C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\reduced_Hospital_log_MPC.csv'
#data = pd.read_csv(file, nrows=traces_per_file*(1176+638))
##
#data.to_csv(target_file, index=0)
##
#for line in reversed(list(open(file))):
#    s=line.rstrip()
#    s=s.split(',')
#    print('hospital')
#    print(s[0])
#    break
#
#file = r'C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\party_A_Hospital_log_MPC.csv'
#target_file = r'C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\reduced_party_A_Hospital_log_MPC.csv'
#data = pd.read_csv(file, nrows=traces_per_file*1176)
#
#data.to_csv(target_file, index=0)
#
#
#file = r'C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\party_B_Hospital_log_MPC.csv'
#target_file = r'C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\reduced_party_B_Hospital_log_MPC.csv'
#data = pd.read_csv(file, nrows=traces_per_file*638)
#
#data.to_csv(target_file, index=0)
#

#
#file = r'C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\Road_Traffic_Fine_Management_Process_MPC.csv'
#target_file = r'C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\reduced_Road_Traffic_Fine_Management_Process_MPC.csv'
#data = pd.read_csv(file, nrows=traces_per_file*(15+5))
#
#data.to_csv(target_file, index=0)
#
#for line in reversed(list(open(file))):
#    s=line.rstrip()
#    s=s.split(',')
#    print('traffic')
#    print(s[0])
#    break
#
#
#
#file = r'C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\party_A_Road_Traffic_Fine_Management_Process_MPC.csv'
#target_file = r'C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\reduced_party_A_Road_Traffic_Fine_Management_Process_MPC.csv'
#data = pd.read_csv(file, nrows= traces_per_file*15)
#
#data.to_csv(target_file, index=0)
#
#file = r'C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\party_B_Road_Traffic_Fine_Management_Process_MPC.csv'
#target_file = r'C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\reduced_party_B_Road_Traffic_Fine_Management_Process_MPC.csv'
#data = pd.read_csv(file, nrows=traces_per_file*5)
#
#data.to_csv(target_file, index=0)
#

#
#file = r'C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\CreditRequirement_MPC.csv'
#target_file = r'C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\reduced_CreditRequirement_MPC.csv'
#data = pd.read_csv(file, nrows=traces_per_file*(7+7))
#
#data.to_csv(target_file, index=0)
#
#for line in reversed(list(open(file))):
#    s=line.rstrip()
#    s=s.split(',')
#    print('traffic')
#    print(s[0])
#    break
#
#
#
#file = r'C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\party_A_CreditRequirement_MPC.csv'
#target_file = r'C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\reduced_party_A_CreditRequirement_MPC.csv'
#data = pd.read_csv(file, nrows= traces_per_file*7)
#
#data.to_csv(target_file, index=0)
#
#file = r'C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\party_B_CreditRequirement_MPC.csv'
#target_file = r'C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\reduced_party_B_CreditRequirement_MPC.csv'
#data = pd.read_csv(file, nrows=traces_per_file*7)
#
#data.to_csv(target_file, index=0)