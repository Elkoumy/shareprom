# -*- coding: utf-8 -*-
"""
Created on Mon Dec 16 18:51:53 2019

@author: Elkoumy
"""
import pandas as pd
import os

dir= r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets_to_Upload"
files= ["BPI13","SEPSIS","WABO1","WABO2","WABO3","WABO4","WABO5","BPI15_1","BPI15_2","BPI15_3","BPI15_4","BPI15_5"]

experiment_dir=os.path.abspath(os.path.join(os.path.dirname( __file__ ), '..', 'experiment'))


command = "#!/bin/bash\n \
chunks=\"100\" \n \
for i in $iterations; do \n"

for f in files:
    party_a= "party_A_"+f+"_MPC.csv"
    party_b= "party_B_"+f+"_MPC.csv"
    
    data= pd.read_csv(dir+"\\"+party_a)
    count_a=data.groupby(['case']).case.count().iloc[0]
    
    data= pd.read_csv(dir+"\\"+party_b)
    count_b=data.groupby(['case']).case.count().iloc[0]
    c="echo \""+f+"_$i\"\n \
    sudo nohup nethogs -t |tee  /home/debian/shareprom/shareprom/experiment/logs/server1/"+f+"_$i>temp &\n\
    ssh debian@SharemindServer2 sudo nohup nethogs -t |tee  /home/debian/shareprom/shareprom/experiment/logs/server2/"+f+"_$i>temp &\n\
    ssh debian@SharemindServer3 sudo nohup nethogs -t |tee  /home/debian/shareprom/shareprom/experiment/logs/server3/"+f+"_$i>temp &\n"
    c+="echo -n "+f+">tbl_name\nsudo sharemind-runscript main.sb --str=TBL --str= --str=string --file=tbl_name --str=CHUNKS --str=\"\" --str=uint64 --size=8 --uint64=$i --str=EVENTA --str pd_shared3p --str=uint64 --size=8 --uint64="+str(count_a)+" --str=EVENTB --str pd_shared3p --str=uint64 --size=8 --uint64="+str(count_b)+"\n"
    
    c+="sudo pkill -f nethogs\n\
    ssh debian@SharemindServer2 sudo pkill -f nethogs\n\
    ssh debian@SharemindServer3 sudo pkill -f nethogs\n"
    
    command+=c
    
    
text_file = open(os.path.join(experiment_dir,"run_experiment.sh"), "w")
text_file.write(command)
text_file.close()   
    