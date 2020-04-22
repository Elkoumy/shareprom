from preprocessing_old import preprocessing_partyA
from preprocessing import read_xes, endcoding_events, padding_log, building_sharemind_model, preprocessing
from upload_to_sharemind import upload
from submit_job_to_sharemind import submit
from parse_results import parse_results
from convert_DFG import convert_DFG_to_matrix,convert_DFG_to_counter

input_dir=r"/home/sharemind/shareprom/demo/application/data"
output_dir=r"/home/sharemind/shareprom/demo/application/data"
log_dir= r"/DFG_log/DFG.out"
# xes_file= r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\Sepsis Cases - Event Log (1).xes"
xes_file=r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\BPI_2013.xes"
# dataset_name= "activities_10"
input_dir=r"data/"
output_dir=r"data/"
dataset_name= "max_10"
no_of_chunks=1
event_a=23
event_b=22

# data, activities_count, event_per_case=read_xes(xes_file)
# #event_per_case is going to be used when uploading the file to sharemind

#encoding start =0 for party A
# preprocessing(data,activities_count, 0, dataset_name, "party_A", output_dir)

# upload(output_dir,dataset_name)
# submit(no_of_chunks, dataset_name, event_a, event_b ,log_dir)
# parse_results(log_dir)


out_dir = r"DFG_log/DFG.out"
freq, time= convert_DFG_to_matrix(out_dir)

# apply differential privacy here
dfg= convert_DFG_to_matrix(freq)

