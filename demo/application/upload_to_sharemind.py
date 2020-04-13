
def upload(dir, dataset_name):
    print("Uploading to shareind")
    csv_name=dir+"/party_A_"+dataset_name+"_MPC.csv"
    model_name=dir+"/"+dataset_name+"_model_party_A.xml"
    command =r"printf 'y\n | sharemind-csv-importer --mode overwrite --csv " +csv_name+ " --model "+model_name+" --separator c"
    print(command)

    csv_name = dir + "/party_B_" + dataset_name + "_MPC.csv"
    model_name = dir + "/" + dataset_name + "_model_party_B.xml"
    command = r"printf 'y\n | sharemind-csv-importer --mode overwrite --csv " + csv_name + " --model " + model_name + " --separator c"
    print(command)

