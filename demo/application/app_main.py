from preprocessing import preprocessing
from upload_to_sharemind import upload
from submit_job_to_sharemind import submit
from parse_results import parse_results
input_dir=r"/home/sharemind/shareprom/demo/application/data"
output_dir=r"/home/sharemind/shareprom/demo/application/data"

dataset_name= "max_10"
no_of_chunks=1

#preprocessing(input_dir, output_dir, dataset_name)
upload(output_dir,dataset_name)
submit(no_of_chunks)
parse_results("")

