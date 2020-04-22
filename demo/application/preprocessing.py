# -*- coding: utf-8 -*-
"""

"""

import sys
import os
import pandas as pd
import time
from datetime import datetime
from pm4py.objects.log.importer.xes import factory as xes_import_factory
from pm4py.objects.conversion.log.versions.to_dataframe import get_dataframe_from_event_stream
from pm4py.objects.log.exporter.csv import factory as csv_exporter


def to_list(s):
    return list(s)

def generate_rows(s):
    return s*[[s,0,0]]

#input_file = sys.argv[1]
#output_file = sys.argv[2]


# input_dir=r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets"
# output_dir=r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets"
#
# file_name= "CCC19_3_columns"


def read_xes(xes_file):
    #read the xes file
    log = xes_import_factory.apply(xes_file)
    data=get_dataframe_from_event_stream(log)


    # input_file = os.path.join(input_dir, file_name + ".csv")
    # data = pd.read_csv(input_file)
    # new_case_ids = pd.Index(data['trace'].unique())
    # data['trace'] = data['trace'].apply(lambda x: new_case_ids.get_loc(x))
    # df['event'] = pd.util.hash_pandas_object(df['event'],index=False)

    # """ generating relative time"""
    # try:
    #     data['time:timestamp'] = data['time:timestamp'].apply(
    #         lambda x: int(time.mktime(datetime.strptime(x, "%Y-%m-%d %H:%M:%S").timetuple())))
    # except:
    #     data['time:timestamp'] = data['time:timestamp'].apply(
    #         lambda x: int(time.mktime(datetime.strptime(x, "%Y-%m-%d %H:%M:%S%z").timetuple())))
    #

    data['time:timestamp'] = data['time:timestamp'] - min(data['time:timestamp'])
    # moving event to the last column
    data = data[['case:concept:name','time:timestamp', 'lifecycle:transition']]

    #renaming columns
    data.columns=['case','completeTime','event']

    # mapping cases to ids of cases
    new_case_ids = pd.Index(data['case'].unique())
    data['case'] = data['case'].apply(lambda x: new_case_ids.get_loc(x))

    data.completeTime=data.completeTime.astype('int64' ) # converting the time difference from time delta to int64
    activities_count = len(list(data.event.unique())) # the number of unique activities in the file

    event_per_case= data.groupby("case").count().event
    event_per_case= event_per_case.max()

    return data, activities_count, event_per_case


def endcoding_events(data,activities_count, encoding_start):
    # encoding start to differentiatet events from party A and party B
    if encoding_start==0:
        ini_binary = "0"*(activities_count-1)+"1"
    else:
        # for party B to start from where party A end (000000 100000)
        ini_binary = "0" * (activities_count -encoding_start- 1) + "1"+"0"*(encoding_start)

    event_idx= {}
    unique_events = list(data.event.unique())
    for  event in unique_events:
        event_idx[event]= ini_binary
        ini_binary= ini_binary[1:]+"0"

    bits_column_names=["b"+str(i) for i in range(0,len(unique_events))]
    data.event=data.event.apply(lambda x: event_idx[x])
    temp= data.event.apply(to_list)
    temp= pd.DataFrame.from_dict(dict(zip(temp.index, temp.values))).T
    data[bits_column_names]=temp
    return data


def padding_log (data,activities_count):
    counts = data.groupby("case").count().event
    max_count= counts.max()


    need_increase=counts[counts<max_count]
    difference=max_count-need_increase

    padded_value=[]
    if len(difference)!=0:
        for i in difference.index:
            temp= difference[i] *[[i,0,0]]
            padded_value=padded_value+temp

        padded_value=pd.DataFrame.from_records(padded_value)

        for i in range(0, activities_count):
            padded_value['b'+str(i)]=0

        padded_value.columns=data.columns

        data= data.append(padded_value , ignore_index=True)

    data= data.sort_values(by=['case', 'completeTime'])
    return data

def building_sharemind_model(bits_size, dataset_name, party):
    ''' generating xml model '''''
    #models_directory=


    s=""

    s+="\" dataSource=\"DS1\" \n handler=\"import-script.sb\"> \n <column key=\"true\" type=\"primitive\">\n    <source name=\"case\" type=\"uint32\"/>\n    <target name=\"case\" type=\"uint32\"/>\n  </column>\n  <column key=\"false\" type=\"primitive\">\n<source name=\"completeTime\" type=\"uint32\"/>\n    <target name=\"completeTime\" type=\"uint32\"/>\n  </column>"

    for i in range(0,bits_size):
        s+=" <column key=\"false\" type=\"primitive\">\n    <source name=\"b"+str(i)+"\" type=\"uint8\"/>\n    <target name=\"b"+str(i)+"\" type=\"uint8\"/>\n  </column>"


    s=s+"\n</table>"

    model = "<table name=\""+dataset_name+"_"+party+s
    # print(model)
    # text_file = open(os.path.join(output_dir,dataset_name+ "_model_party_A.xml"), "w")
    # text_file.write(model)
    # text_file.close()

    return model

# preprocessing

def preprocessing(data,activities_count, encoding_start, dataset_name, party, output_dir):
    encoded_data = endcoding_events(data, activities_count, encoding_start)
    padded_data = padding_log(encoded_data, activities_count)

    padded_data = padded_data.drop(['event'], axis=1)
    padded_data.to_csv(os.path.join(output_dir, party+"_" + dataset_name + "_MPC.csv"), index=0)

    model = building_sharemind_model(activities_count, dataset_name, "party_A")
    text_file = open(os.path.join(output_dir,dataset_name+ "_model_"+party+".xml"), "w")
    text_file.write(model)
    text_file.close()
