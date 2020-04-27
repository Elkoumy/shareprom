from preprocessing_old import preprocessing_partyA
from preprocessing import read_xes, endcoding_events, padding_log, building_sharemind_model, preprocessing
from upload_to_sharemind import upload
from submit_job_to_sharemind import submit
from parse_results import parse_results
from convert_DFG import convert_DFG_to_matrix,convert_DFG_to_counter
from draw_DFG import draw_DFG



input_dir=r"/home/sharemind/shareprom/demo/application/data"
output_dir=r"/home/sharemind/shareprom/demo/application/data"
log_dir= r"/DFG_log/DFG.out"
# xes_file= r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\Sepsis Cases - Event Log (1).xes"
xes_file=r"manufacurer.xes"
dataset_name= "demo"
input_dir=r"data/"
output_dir=r"data/"

no_of_chunks=1
event_a=9
event_b=3
total_activities= event_a+event_b
partyB_event_names={'"order intermediate B"': '001000000000', '"produce intermediate B"': '010000000000', '"Transport intermediate B"': '100000000000'}
# data, activities_count, event_per_case=read_xes(xes_file)
#event_per_case is going to be used when uploading the file to sharemind

#encoding start =0 for party A
# event_names=preprocessing(data,total_activities, 0, dataset_name, "party_A", output_dir)
#
# upload(output_dir,dataset_name,"party_A")
# log_dir=r"DFG_log/DFG.out"
# submit(no_of_chunks, dataset_name, event_a, event_b ,log_dir)
#
# parse_results(log_dir)

#
# out_dir = r"DFG_log/DFG.log"
# freq, time= convert_DFG_to_matrix(out_dir)

''' apply differential privacy here  '''

# print(freq.index.values)
#
# for key in partyB_event_names.keys():
#     event_names[key]=partyB_event_names[key]
#
# dfg= convert_DFG_to_counter(freq,list(event_names.keys()))
#
# draw_DFG(dfg)
#
#


from pm4py.algo.discovery.dfg import factory as dfg_factory
from pm4py.visualization.dfg import factory as dfg_vis_factory
from pm4py.objects.log.importer.xes import factory as xes_importer
from pm4py.objects.conversion.log.versions.to_dataframe import get_dataframe_from_event_stream

log_man = xes_importer.import_log("manufacurer.xes")
log_man =get_dataframe_from_event_stream(log_man)
log_sup = xes_importer.import_log("supplier_B.xes")
log_sup =get_dataframe_from_event_stream(log_sup)

import pandas as pd
log=pd.concat([log_man,log_sup])
log.to_csv('combined.csv')
from pm4py.objects.log.exporter.xes import factory as xes_exporter
xes_exporter.export_log(log, "combined.xes")
from pm4py.objects.log.importer.xes import factory as xes_import_factory
log = xes_import_factory.apply("combined_log.xes")
dfg = dfg_factory.apply(log)
parameters = {"format": "svg"}
gviz = dfg_vis_factory.apply(dfg, log=log, variant="performance", parameters=parameters)
dfg_vis_factory.save(gviz, "dfg_out.svg")


