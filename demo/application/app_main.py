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
out_dir = r"DFG_log/DFG.log"
freq, time= convert_DFG_to_matrix(out_dir)

''' apply differential privacy here  '''

# print(freq.index.values)

dfg= convert_DFG_to_counter(freq)

draw_DFG(dfg)


