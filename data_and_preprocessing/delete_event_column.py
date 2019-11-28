# -*- coding: utf-8 -*-
"""
Created on Thu Nov 21 11:38:25 2019

@author: Elkoumy
"""

import pandas as pd

#file = r'C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\BPI_Challenge_2017_MPC.csv'
#data = pd.read_csv(file)
#
#del data['Unnamed: 0']
#
#data.to_csv(file,index=0)
#
#file = r'C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\party_A_BPI_Challenge_2017_MPC.csv'
#data = pd.read_csv(file)
#
#del data['Unnamed: 0']
#
#data.to_csv(file,index=0)
#
#file = r'C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\party_B_BPI_Challenge_2017_MPC.csv'
#data = pd.read_csv(file)
#
#del data['Unnamed: 0']
#
#data.to_csv(file,index=0)
#
#
#
#file = r'C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\Hospital_log_MPC.csv'
#data = pd.read_csv(file)
#
#del data['Unnamed: 0']
#
#data.to_csv(file, index=0)
#
#file = r'C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\party_A_Hospital_log_MPC.csv'
#data = pd.read_csv(file)
#
#del data['Unnamed: 0']
#
#data.to_csv(file, index=0)
#
#file = r'C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\party_B_Hospital_log_MPC.csv'
#data = pd.read_csv(file)
#
#del data['Unnamed: 0']
#
#data.to_csv(file, index=0)
#
#
#
#file = r'C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\Road_Traffic_Fine_Management_Process_MPC.csv'
#data = pd.read_csv(file)
#
#del data['Unnamed: 0']
#
#data.to_csv(file, index=0)
#
#file = r'C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\party_A_Road_Traffic_Fine_Management_Process_MPC.csv'
#data = pd.read_csv(file)
#
#del data['Unnamed: 0']
#
#data.to_csv(file, index=0)
#
#file = r'C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\party_B_Road_Traffic_Fine_Management_Process_MPC.csv'
#data = pd.read_csv(file)
#
#del data['Unnamed: 0']
#
#data.to_csv(file, index=0)


file = r'C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\CreditRequirement_MPC.csv'
data = pd.read_csv(file)

del data['event']

data.to_csv(file, index=0)

file = r'C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\party_A_CreditRequirement_MPC.csv'
data = pd.read_csv(file)

del data['event']

data.to_csv(file, index=0)

file = r'C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\party_B_CreditRequirement_MPC.csv'
data = pd.read_csv(file)

del data['event']

data.to_csv(file, index=0)