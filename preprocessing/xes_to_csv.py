# -*- coding: utf-8 -*-
"""
Created on Tue Nov 19 16:31:52 2019

@author: Elkoumy
"""

from pm4py.objects.log.importer.xes import factory as xes_import_factory
from pm4py.objects.log.exporter.csv import factory as csv_exporter

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

log = xes_import_factory.apply(r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\CreditRequirement.xes")

csv_exporter.export(log, r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\CreditRequirement.csv")