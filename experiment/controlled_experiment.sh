#!/bin/bash

dataset=(
#  cases_10000
#  cases_100000
#  cases_1000000
  activities_10
  activities_50
  activities_100
#  max_100
#  max_1000
)
eventA=(
#  5
#  5
#  5
  73
  77
  91
#  74
#  622
)

eventB=(
#  4
#  4
#  4
  73
  69
  69
#  64
#  510
)


for chunk in {100,1000,10000}
do
 echo "***********************************************************"
 echo "chunks equal to $chunk"
for index in ${!dataset[*]}; do 
  echo "***********************************************************"
  echo "Current Dataset is ${dataset[$index]}"
  sudo nohup nethogs -t |tee  /home/debian/shareprom/shareprom/experiment/logs/server1/${dataset[$index]}_$chunk>/home/debian/shareprom/shareprom/experiment/logs/server1/${dataset[$index]}_$chunk &
    ssh debian@SharemindServer2 sudo nohup nethogs -t |tee  /home/debian/shareprom/shareprom/experiment/logs/server2/${dataset[$index]}_$chunk>/home/debian/shareprom/shareprom/experiment/logs/server2/${dataset[$index]}_$chunk &
    ssh debian@SharemindServer3 sudo nohup nethogs -t |tee  /home/debian/shareprom/shareprom/experiment/logs/server3/${dataset[$index]}_$chunk>/home/debian/shareprom/shareprom/experiment/logs/server3/${dataset[$index]}_$chunk &

sudo sharemind-runscript main.sb --str=TBL --str= --str=string --str="${dataset[$index]}" --str=CHUNKS --str="" --str=uint64 --size=8 --uint64=$chunk --str=EVENTA --str pd_shared3p --str=uint64 --size=8 --uint64=${eventA[$index]} --str=EVENTB --str pd_shared3p --str=uint64 --size=8 --uint64=${eventB[$index]}

    sudo pkill -f nethogs
    ssh debian@SharemindServer2 sudo pkill -f nethogs
    ssh debian@SharemindServer3 sudo pkill -f nethogs


done

done