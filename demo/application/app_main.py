from preprocessing import preprocessing
from upload_to_sharemind import upload
from submit_job_to_sharemind import submit
from parse_results import parse_results
input_dir=r"/home/sharemind/shareprom/demo/application/data"
output_dir=r"/home/sharemind/shareprom/demo/application/data"
log_dir= r"/home/sharemind/shareprom/demo/application/out3.log"
dataset_name= "max_10"
no_of_chunks=1
event_a=10
event_b=9

#preprocessing(input_dir, output_dir, dataset_name)
upload(output_dir,dataset_name)
submit(no_of_chunks, dataset_name, event_a, event_b ,log_dir)
parse_results(log_dir)

