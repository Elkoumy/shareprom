#!/bin/bash
 chunks="100" 
 for i in $chunks; do 
echo "BPI13_$i"
     sudo nohup nethogs -t |tee  /home/debian/shareprom/shareprom/experiment/logs/server1/BPI13_$i>temp &
    ssh debian@SharemindServer2 sudo nohup nethogs -t |tee  /home/debian/shareprom/shareprom/experiment/logs/server2/BPI13_$i>temp &
    ssh debian@SharemindServer3 sudo nohup nethogs -t |tee  /home/debian/shareprom/shareprom/experiment/logs/server3/BPI13_$i>temp &
echo -n BPI13>tbl_name
sudo sharemind-runscript main.sb --str=TBL --str= --str=string --file=tbl_name --str=CHUNKS --str="" --str=uint64 --size=8 --uint64=$i --str=EVENTA --str pd_shared3p --str=uint64 --size=8 --uint64=28 --str=EVENTB --str pd_shared3p --str=uint64 --size=8 --uint64=11
sudo pkill -f nethogs
    ssh debian@SharemindServer2 sudo pkill -f nethogs
    ssh debian@SharemindServer3 sudo pkill -f nethogs
echo "SEPSIS_$i"
     sudo nohup nethogs -t |tee  /home/debian/shareprom/shareprom/experiment/logs/server1/SEPSIS_$i>temp &
    ssh debian@SharemindServer2 sudo nohup nethogs -t |tee  /home/debian/shareprom/shareprom/experiment/logs/server2/SEPSIS_$i>temp &
    ssh debian@SharemindServer3 sudo nohup nethogs -t |tee  /home/debian/shareprom/shareprom/experiment/logs/server3/SEPSIS_$i>temp &
echo -n SEPSIS>tbl_name
sudo sharemind-runscript main.sb --str=TBL --str= --str=string --file=tbl_name --str=CHUNKS --str="" --str=uint64 --size=8 --uint64=$i --str=EVENTA --str pd_shared3p --str=uint64 --size=8 --uint64=144 --str=EVENTB --str pd_shared3p --str=uint64 --size=8 --uint64=61
sudo pkill -f nethogs
    ssh debian@SharemindServer2 sudo pkill -f nethogs
    ssh debian@SharemindServer3 sudo pkill -f nethogs
echo "WABO1_$i"
     sudo nohup nethogs -t |tee  /home/debian/shareprom/shareprom/experiment/logs/server1/WABO1_$i>temp &
    ssh debian@SharemindServer2 sudo nohup nethogs -t |tee  /home/debian/shareprom/shareprom/experiment/logs/server2/WABO1_$i>temp &
    ssh debian@SharemindServer3 sudo nohup nethogs -t |tee  /home/debian/shareprom/shareprom/experiment/logs/server3/WABO1_$i>temp &
echo -n WABO1>tbl_name
sudo sharemind-runscript main.sb --str=TBL --str= --str=string --file=tbl_name --str=CHUNKS --str="" --str=uint64 --size=8 --uint64=$i --str=EVENTA --str pd_shared3p --str=uint64 --size=8 --uint64=61 --str=EVENTB --str pd_shared3p --str=uint64 --size=8 --uint64=67
sudo pkill -f nethogs
    ssh debian@SharemindServer2 sudo pkill -f nethogs
    ssh debian@SharemindServer3 sudo pkill -f nethogs
echo "WABO2_$i"
     sudo nohup nethogs -t |tee  /home/debian/shareprom/shareprom/experiment/logs/server1/WABO2_$i>temp &
    ssh debian@SharemindServer2 sudo nohup nethogs -t |tee  /home/debian/shareprom/shareprom/experiment/logs/server2/WABO2_$i>temp &
    ssh debian@SharemindServer3 sudo nohup nethogs -t |tee  /home/debian/shareprom/shareprom/experiment/logs/server3/WABO2_$i>temp &
echo -n WABO2>tbl_name
sudo sharemind-runscript main.sb --str=TBL --str= --str=string --file=tbl_name --str=CHUNKS --str="" --str=uint64 --size=8 --uint64=$i --str=EVENTA --str pd_shared3p --str=uint64 --size=8 --uint64=109 --str=EVENTB --str pd_shared3p --str=uint64 --size=8 --uint64=21
sudo pkill -f nethogs
    ssh debian@SharemindServer2 sudo pkill -f nethogs
    ssh debian@SharemindServer3 sudo pkill -f nethogs
echo "WABO3_$i"
     sudo nohup nethogs -t |tee  /home/debian/shareprom/shareprom/experiment/logs/server1/WABO3_$i>temp &
    ssh debian@SharemindServer2 sudo nohup nethogs -t |tee  /home/debian/shareprom/shareprom/experiment/logs/server2/WABO3_$i>temp &
    ssh debian@SharemindServer3 sudo nohup nethogs -t |tee  /home/debian/shareprom/shareprom/experiment/logs/server3/WABO3_$i>temp &
echo -n WABO3>tbl_name
sudo sharemind-runscript main.sb --str=TBL --str= --str=string --file=tbl_name --str=CHUNKS --str="" --str=uint64 --size=8 --uint64=$i --str=EVENTA --str pd_shared3p --str=uint64 --size=8 --uint64=124 --str=EVENTB --str pd_shared3p --str=uint64 --size=8 --uint64=43
sudo pkill -f nethogs
    ssh debian@SharemindServer2 sudo pkill -f nethogs
    ssh debian@SharemindServer3 sudo pkill -f nethogs
echo "WABO4_$i"
     sudo nohup nethogs -t |tee  /home/debian/shareprom/shareprom/experiment/logs/server1/WABO4_$i>temp &
    ssh debian@SharemindServer2 sudo nohup nethogs -t |tee  /home/debian/shareprom/shareprom/experiment/logs/server2/WABO4_$i>temp &
    ssh debian@SharemindServer3 sudo nohup nethogs -t |tee  /home/debian/shareprom/shareprom/experiment/logs/server3/WABO4_$i>temp &
echo -n WABO4>tbl_name
sudo sharemind-runscript main.sb --str=TBL --str= --str=string --file=tbl_name --str=CHUNKS --str="" --str=uint64 --size=8 --uint64=$i --str=EVENTA --str pd_shared3p --str=uint64 --size=8 --uint64=72 --str=EVENTB --str pd_shared3p --str=uint64 --size=8 --uint64=37
sudo pkill -f nethogs
    ssh debian@SharemindServer2 sudo pkill -f nethogs
    ssh debian@SharemindServer3 sudo pkill -f nethogs
echo "WABO5_$i"
     sudo nohup nethogs -t |tee  /home/debian/shareprom/shareprom/experiment/logs/server1/WABO5_$i>temp &
    ssh debian@SharemindServer2 sudo nohup nethogs -t |tee  /home/debian/shareprom/shareprom/experiment/logs/server2/WABO5_$i>temp &
    ssh debian@SharemindServer3 sudo nohup nethogs -t |tee  /home/debian/shareprom/shareprom/experiment/logs/server3/WABO5_$i>temp &
echo -n WABO5>tbl_name
sudo sharemind-runscript main.sb --str=TBL --str= --str=string --file=tbl_name --str=CHUNKS --str="" --str=uint64 --size=8 --uint64=$i --str=EVENTA --str pd_shared3p --str=uint64 --size=8 --uint64=88 --str=EVENTB --str pd_shared3p --str=uint64 --size=8 --uint64=47
sudo pkill -f nethogs
    ssh debian@SharemindServer2 sudo pkill -f nethogs
    ssh debian@SharemindServer3 sudo pkill -f nethogs
echo "BPI15_1_$i"
     sudo nohup nethogs -t |tee  /home/debian/shareprom/shareprom/experiment/logs/server1/BPI15_1_$i>temp &
    ssh debian@SharemindServer2 sudo nohup nethogs -t |tee  /home/debian/shareprom/shareprom/experiment/logs/server2/BPI15_1_$i>temp &
    ssh debian@SharemindServer3 sudo nohup nethogs -t |tee  /home/debian/shareprom/shareprom/experiment/logs/server3/BPI15_1_$i>temp &
echo -n BPI15_1>tbl_name
sudo sharemind-runscript main.sb --str=TBL --str= --str=string --file=tbl_name --str=CHUNKS --str="" --str=uint64 --size=8 --uint64=$i --str=EVENTA --str pd_shared3p --str=uint64 --size=8 --uint64=79 --str=EVENTB --str pd_shared3p --str=uint64 --size=8 --uint64=63
sudo pkill -f nethogs
    ssh debian@SharemindServer2 sudo pkill -f nethogs
    ssh debian@SharemindServer3 sudo pkill -f nethogs
echo "BPI15_2_$i"
     sudo nohup nethogs -t |tee  /home/debian/shareprom/shareprom/experiment/logs/server1/BPI15_2_$i>temp &
    ssh debian@SharemindServer2 sudo nohup nethogs -t |tee  /home/debian/shareprom/shareprom/experiment/logs/server2/BPI15_2_$i>temp &
    ssh debian@SharemindServer3 sudo nohup nethogs -t |tee  /home/debian/shareprom/shareprom/experiment/logs/server3/BPI15_2_$i>temp &
echo -n BPI15_2>tbl_name
sudo sharemind-runscript main.sb --str=TBL --str= --str=string --file=tbl_name --str=CHUNKS --str="" --str=uint64 --size=8 --uint64=$i --str=EVENTA --str pd_shared3p --str=uint64 --size=8 --uint64=89 --str=EVENTB --str pd_shared3p --str=uint64 --size=8 --uint64=52
sudo pkill -f nethogs
    ssh debian@SharemindServer2 sudo pkill -f nethogs
    ssh debian@SharemindServer3 sudo pkill -f nethogs
echo "BPI15_3_$i"
     sudo nohup nethogs -t |tee  /home/debian/shareprom/shareprom/experiment/logs/server1/BPI15_3_$i>temp &
    ssh debian@SharemindServer2 sudo nohup nethogs -t |tee  /home/debian/shareprom/shareprom/experiment/logs/server2/BPI15_3_$i>temp &
    ssh debian@SharemindServer3 sudo nohup nethogs -t |tee  /home/debian/shareprom/shareprom/experiment/logs/server3/BPI15_3_$i>temp &
echo -n BPI15_3>tbl_name
sudo sharemind-runscript main.sb --str=TBL --str= --str=string --file=tbl_name --str=CHUNKS --str="" --str=uint64 --size=8 --uint64=$i --str=EVENTA --str pd_shared3p --str=uint64 --size=8 --uint64=108 --str=EVENTB --str pd_shared3p --str=uint64 --size=8 --uint64=68
sudo pkill -f nethogs
    ssh debian@SharemindServer2 sudo pkill -f nethogs
    ssh debian@SharemindServer3 sudo pkill -f nethogs
echo "BPI15_4_$i"
     sudo nohup nethogs -t |tee  /home/debian/shareprom/shareprom/experiment/logs/server1/BPI15_4_$i>temp &
    ssh debian@SharemindServer2 sudo nohup nethogs -t |tee  /home/debian/shareprom/shareprom/experiment/logs/server2/BPI15_4_$i>temp &
    ssh debian@SharemindServer3 sudo nohup nethogs -t |tee  /home/debian/shareprom/shareprom/experiment/logs/server3/BPI15_4_$i>temp &
echo -n BPI15_4>tbl_name
sudo sharemind-runscript main.sb --str=TBL --str= --str=string --file=tbl_name --str=CHUNKS --str="" --str=uint64 --size=8 --uint64=$i --str=EVENTA --str pd_shared3p --str=uint64 --size=8 --uint64=60 --str=EVENTB --str pd_shared3p --str=uint64 --size=8 --uint64=61
sudo pkill -f nethogs
    ssh debian@SharemindServer2 sudo pkill -f nethogs
    ssh debian@SharemindServer3 sudo pkill -f nethogs
echo "BPI15_5_$i"
     sudo nohup nethogs -t |tee  /home/debian/shareprom/shareprom/experiment/logs/server1/BPI15_5_$i>temp &
    ssh debian@SharemindServer2 sudo nohup nethogs -t |tee  /home/debian/shareprom/shareprom/experiment/logs/server2/BPI15_5_$i>temp &
    ssh debian@SharemindServer3 sudo nohup nethogs -t |tee  /home/debian/shareprom/shareprom/experiment/logs/server3/BPI15_5_$i>temp &
echo -n BPI15_5>tbl_name
sudo sharemind-runscript main.sb --str=TBL --str= --str=string --file=tbl_name --str=CHUNKS --str="" --str=uint64 --size=8 --uint64=$i --str=EVENTA --str pd_shared3p --str=uint64 --size=8 --uint64=74 --str=EVENTB --str pd_shared3p --str=uint64 --size=8 --uint64=82
sudo pkill -f nethogs
    ssh debian@SharemindServer2 sudo pkill -f nethogs
    ssh debian@SharemindServer3 sudo pkill -f nethogs
done
