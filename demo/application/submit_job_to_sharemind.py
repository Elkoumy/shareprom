

def submit(no_of_chunks, dataset_name, event_a, event_b):
    print("submitting the job to sharemind")
    command="sharemind-runscript main.sb --str=TBL --str= --str=string --str="+dataset_name+" --str=CHUNKS --str="" --str=uint64 --size=8 --uint64="+str(no_of_chunks)+" --str=EVENTA --str pd_shared3p --str=uint64 --size=8 --uint64="+str(event_a)+" --str=EVENTB --str pd_shared3p --str=uint64 --size=8 --uint64="+str(event_b)
    print(command)