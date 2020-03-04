# -*- coding: utf-8 -*-
"""
Created on Tue Nov 19 16:31:52 2019

@author: Elkoumy
"""

from pm4py.objects.log.importer.xes import factory as xes_import_factory
from pm4py.objects.log.exporter.csv import factory as csv_exporter
import pandas as pd


#log = xes_import_factory.apply(r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\Road_Traffic_Fine_Management_Process.xes")
#
#csv_exporter.export(log, r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\Road_Traffic_Fine_Management_Process.csv")
#
#log = xes_import_factory.apply(r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\Hospital_log.xes")
#
#csv_exporter.export(log, r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\Hospital_log.csv")
#
#log = xes_import_factory.apply(r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\BPI Challenge 2017.xes")
#
#csv_exporter.export(log, r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\BPI_Challenge_2017.csv")
#

#log = xes_import_factory.apply(r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\CreditRequirement.xes")
#
#csv_exporter.export(log, r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\CreditRequirement.csv")
#

#print("2")
#log = xes_import_factory.apply(r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\CoSeLoG_WABO_2.xes")
#
#csv_exporter.export(log, r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\CoSeLoG_WABO_2.csv")
#print("3")
#log = xes_import_factory.apply(r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\CoSeLoG_WABO_3.xes")
#
#csv_exporter.export(log, r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\CoSeLoG_WABO_3.csv")
#print("4")
#log = xes_import_factory.apply(r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\CoSeLoG_WABO_4.xes")
#
#csv_exporter.export(log, r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\CoSeLoG_WABO_4.csv")
#print("5")
#log = xes_import_factory.apply(r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\CoSeLoG_WABO_5.xes")
#
#csv_exporter.export(log, r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\CoSeLoG_WABO_5.csv")


#print("1")
#log = xes_import_factory.apply(r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\CCC19.xes")
#
#csv_exporter.export(log, r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\CCC19.csv")
#print("2")
#log = xes_import_factory.apply(r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\BPI_2013.xes")
#
#csv_exporter.export(log, r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\BPI_2013.csv")

#
#
#print("1")
#log = xes_import_factory.apply(r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\BPIC15_1.xes")
#
#csv_exporter.export(log, r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\BPIC15_1.csv")
#
#print("2")
#log = xes_import_factory.apply(r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\BPIC15_2.xes")
#
#csv_exporter.export(log, r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\BPIC15_2.csv")
#
#
#print("3")
#log = xes_import_factory.apply(r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\BPIC15_3.xes")
#
#csv_exporter.export(log, r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\BPIC15_3.csv")
#
#
#print("4")
#log = xes_import_factory.apply(r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\BPIC15_4.xes")
#
#csv_exporter.export(log, r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\BPIC15_4.csv")


print("5")
log = xes_import_factory.apply(r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\Sepsis Cases - Event Log (1).xes")

csv_exporter.export(log, r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\Sepsis Cases - Event Log.csv")

