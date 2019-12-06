#!/bin/bash
iterations="2 3 4 5"
for i in $iterations; do
#	echo "credit_10000"
#	sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server1/log_creditReq_10000_$i>temp &
#	ssh debian@SharemindServer2 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server2/log_creditReq_10000_$i>temp &
#	ssh debian@SharemindServer3 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server3/log_creditReq_10000_$i>temp &

#	sudo sharemind-runscript reduced_Algorithm_Final_credit_10000.sb > /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/client_creditReq_10000_$i

#	sudo pkill -f nethogs
#	ssh debian@SharemindServer2 sudo pkill -f nethogs
#	ssh debian@SharemindServer3 sudo pkill -f nethogs

#	echo "credit_1000"
#	sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server1/log_creditReq_1000_$i>temp &
#	ssh debian@SharemindServer2 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server2/log_creditReq_1000_$i>temp &
#	ssh debian@SharemindServer3 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server3/log_creditReq_1000_$i>temp &

#	sudo sharemind-runscript reduced_Algorithm_Final_credit_1000.sb > /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/client_creditReq_1000_$i

#	sudo pkill -f nethogs
#	ssh debian@SharemindServer2 sudo pkill -f nethogs
#	ssh debian@SharemindServer3 sudo pkill -f nethogs
	
	
#	echo "credit_100"
#	sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server1/log_creditReq_100_$i>temp &
#	ssh debian@SharemindServer2 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server2/log_creditReq_100_$i>temp &
#	ssh debian@SharemindServer3 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server3/log_creditReq_100_$i>temp &

#	sudo sharemind-runscript reduced_Algorithm_Final_credit_100.sb > /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/client_creditReq_100_$i

#	sudo pkill -f nethogs
#	ssh debian@SharemindServer2 sudo pkill -f nethogs
#	ssh debian@SharemindServer3 sudo pkill -f nethogs
	echo "credit_10"
	sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server1/log_creditReq_10_$i>temp &
	ssh debian@SharemindServer2 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server2/log_creditReq_10_$i>temp &
	ssh debian@SharemindServer3 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server3/log_creditReq_10_$i>temp &

	sudo sharemind-runscript reduced_Algorithm_Final_credit_10.sb > /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/client_creditReq_10_$i

	sudo pkill -f nethogs
	ssh debian@SharemindServer2 sudo pkill -f nethogs
	ssh debian@SharemindServer3 sudo pkill -f nethogs


#	echo "credit_1"
#	sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server1/log_credit_$i>temp &
#	ssh debian@SharemindServer2 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server2/log_creditReq_$i>temp &
#	ssh debian@SharemindServer3 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server3/log_creditReq_$i>temp &

#	sudo sharemind-runscript reduced_Algorithm_Final_credit.sb > /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/client_creditReq_$i

#	sudo pkill -f nethogs
#	ssh debian@SharemindServer2 sudo pkill -f nethogs
#	ssh debian@SharemindServer3 sudo pkill -f nethogs

	#################################################################
#	echo "bpi_1000"
#	sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server1/log_bpi_1000_$i>temp &
#	ssh debian@SharemindServer2 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server2/log_bpi_1000_$i>temp &
#	ssh debian@SharemindServer3 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server3/log_bpi_1000_$i>temp &

#	sudo sharemind-runscript reduced_Algorithm_Final_bpi_1000.sb > /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/client_bpi_1000_$i

#	sudo pkill -f nethogs
#	ssh debian@SharemindServer2 sudo pkill -f nethogs
#	ssh debian@SharemindServer3 sudo pkill -f nethogs

#	echo "bpi_100"
#	sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server1/log_bpi_100_$i>temp &
#	ssh debian@SharemindServer2 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server2/log_bpi_100_$i>temp &
#	ssh debian@SharemindServer3 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server3/log_bpi_100_$i>temp &

#	sudo sharemind-runscript reduced_Algorithm_Final_bpi_100.sb > /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/client_bpi_100_$i

#	sudo pkill -f nethogs
#	ssh debian@SharemindServer2 sudo pkill -f nethogs
#	ssh debian@SharemindServer3 sudo pkill -f nethogs

	echo "bpi_10"
	sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server1/log_bpi_10_$i>temp &
	ssh debian@SharemindServer2 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server2/log_bpi_10_$i>temp &
	ssh debian@SharemindServer3 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server3/log_bpi_10_$i>temp &

	sudo sharemind-runscript reduced_Algorithm_Final_bpi_10.sb > /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/client_bpi_10_$i

	sudo pkill -f nethogs
	ssh debian@SharemindServer2 sudo pkill -f nethogs
	ssh debian@SharemindServer3 sudo pkill -f nethogs

#	echo "bpi_1"
#	sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server1/log_bpi_$i>temp &
#	ssh debian@SharemindServer2 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server2/log_bpi_$i>temp &
#	ssh debian@SharemindServer3 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server3/log_bpi_$i>temp &
#
#	sudo sharemind-runscript reduced_Algorithm_Final_bpi.sb > /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/client_bpi_1_$i

#	sudo pkill -f nethogs
#	ssh debian@SharemindServer2 sudo pkill -f nethogs
#	ssh debian@SharemindServer3 sudo pkill -f nethogs
	
	#######################################################################################
#	echo "traffic_70000"
#	sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server1/log_traffic_70000_$i>temp &
#	ssh debian@SharemindServer2 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server2/log_traffic_70000_$i>temp &
#	ssh debian@SharemindServer3 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server3/log_traffic_70000_$i>temp &

#	sudo sharemind-runscript reduced_Algorithm_Final_traffic_70000.sb > /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/client_traffic_70000_$i

#	sudo pkill -f nethogs
#	ssh debian@SharemindServer2 sudo pkill -f nethogs
#	ssh debian@SharemindServer3 sudo pkill -f nethogs

	echo "traffic_10000"
	sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server1/log_traffic_10000_$i>temp &
	ssh debian@SharemindServer2 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server2/log_traffic_10000_$i>temp &
	ssh debian@SharemindServer3 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server3/log_traffic_10000_$i>temp &

	sudo sharemind-runscript reduced_Algorithm_Final_traffic_10000.sb > /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/client_traffic_10000_$i

	sudo pkill -f nethogs
	ssh debian@SharemindServer2 sudo pkill -f nethogs
	ssh debian@SharemindServer3 sudo pkill -f nethogs


#	echo "traffic_1000"
#	sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server1/log_traffic_1000_$i>temp &
#	ssh debian@SharemindServer2 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server2/log_traffic_1000_$i>temp &
#	ssh debian@SharemindServer3 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server3/log_traffic_1000_$i>temp &

#	sudo sharemind-runscript reduced_Algorithm_Final_traffic_1000.sb > /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/client_traffic_1000_$i

#	sudo pkill -f nethogs
#	ssh debian@SharemindServer2 sudo pkill -f nethogs
#	ssh debian@SharemindServer3 sudo pkill -f nethogs

#	echo "traffic_100"
#	sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server1/log_traffic_100_$i>temp &
#	ssh debian@SharemindServer2 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server2/log_traffic_100_$i>temp &
#	ssh debian@SharemindServer3 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server3/log_traffic_100_$i>temp &

#	sudo sharemind-runscript reduced_Algorithm_Final_traffic_100.sb > /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/client_traffic_100_$i

#	sudo pkill -f nethogs
#	ssh debian@SharemindServer2 sudo pkill -f nethogs
#	ssh debian@SharemindServer3 sudo pkill -f nethogs

	echo "traffic_10"
	sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server1/log_traffic_10_$i>temp &
	ssh debian@SharemindServer2 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server2/log_traffic_10_$i>temp &
	ssh debian@SharemindServer3 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server3/log_traffic_10_$i>temp &

	sudo sharemind-runscript reduced_Algorithm_Final_traffic_10.sb > /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/client_traffic_10_$i

	sudo pkill -f nethogs
	ssh debian@SharemindServer2 sudo pkill -f nethogs
	ssh debian@SharemindServer3 sudo pkill -f nethogs


#	echo "traffic_1"
#	sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server1/log_traffic_$i>temp &
#	ssh debian@SharemindServer2 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server2/log_traffic_$i>temp &
#	ssh debian@SharemindServer3 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server3/log_traffic_$i>temp &

#	sudo sharemind-runscript reduced_Algorithm_Final_traffic.sb > /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/client_traffic_$i

#	sudo pkill -f nethogs
#	ssh debian@SharemindServer2 sudo pkill -f nethogs
#	ssh debian@SharemindServer3 sudo pkill -f nethogs


done


: '
sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server1/log_traffic_70000_2>temp &
ssh debian@SharemindServer2 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server2/log_traffic_70000_2>temp &
ssh debian@SharemindServer3 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server3/log_traffic_70000_2>temp &

sudo sharemind-runscript reduced_Algorithm_Final_traffic_70000.sb > /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/client_traffic_70000_2

sudo pkill -f nethogs
ssh debian@SharemindServer2 sudo pkill -f nethogs
ssh debian@SharemindServer3 sudo pkill -f nethogs


sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server1/log_traffic_1000_2>temp &
ssh debian@SharemindServer2 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server2/log_traffic_1000_2>temp &
ssh debian@SharemindServer3 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server3/log_traffic_1000_2>temp &

sudo sharemind-runscript reduced_Algorithm_Final_traffic_1000.sb > /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/client_traffic_1000_2

sudo pkill -f nethogs
ssh debian@SharemindServer2 sudo pkill -f nethogs
ssh debian@SharemindServer3 sudo pkill -f nethogs


sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server1/log_traffic_100_2>temp &
ssh debian@SharemindServer2 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server2/log_traffic_100_2>temp &
ssh debian@SharemindServer3 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server3/log_traffic_100_2>temp &

sudo sharemind-runscript reduced_Algorithm_Final_traffic_100.sb > /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/client_traffic_100_2

sudo pkill -f nethogs
ssh debian@SharemindServer2 sudo pkill -f nethogs
ssh debian@SharemindServer3 sudo pkill -f nethogs


sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server1/log_traffic_2>temp &
ssh debian@SharemindServer2 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server2/log_traffic_2>temp &
ssh debian@SharemindServer3 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server3/log_traffic_2>temp &

sudo sharemind-runscript reduced_Algorithm_Final_traffic.sb > /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/client_traffic_2

sudo pkill -f nethogs
ssh debian@SharemindServer2 sudo pkill -f nethogs
ssh debian@SharemindServer3 sudo pkill -f nethogs

#################################################################
sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server1/log_bpi_1000_2>temp &
ssh debian@SharemindServer2 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server2/log_bpi_1000_2>temp &
ssh debian@SharemindServer3 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server3/log_bpi_1000_2>temp &

sudo sharemind-runscript reduced_Algorithm_Final_bpi_1000.sb > /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/client_bpi_1000_2

sudo pkill -f nethogs
ssh debian@SharemindServer2 sudo pkill -f nethogs
ssh debian@SharemindServer3 sudo pkill -f nethogs


sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server1/log_bpi_100_2>temp &
ssh debian@SharemindServer2 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server2/log_bpi_100_2>temp &
ssh debian@SharemindServer3 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server3/log_bpi_100_2>temp &

sudo sharemind-runscript reduced_Algorithm_Final_bpi_100.sb > /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/client_bpi_100

sudo pkill -f nethogs
ssh debian@SharemindServer2 sudo pkill -f nethogs
ssh debian@SharemindServer3 sudo pkill -f nethogs


sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server1/log_bpi_2>temp &
ssh debian@SharemindServer2 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server2/log_bpi_2>temp &
ssh debian@SharemindServer3 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server3/log_bpi_2>temp &

sudo sharemind-runscript reduced_Algorithm_Final_bpi.sb > /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/client_bpi_2

sudo pkill -f nethogs
ssh debian@SharemindServer2 sudo pkill -f nethogs
ssh debian@SharemindServer3 sudo pkill -f nethogs



sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server1/log_traffic_70000_3>temp &
ssh debian@SharemindServer2 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server2/log_traffic_70000_3>temp &
ssh debian@SharemindServer3 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server3/log_traffic_70000_3>temp &

sudo sharemind-runscript reduced_Algorithm_Final_traffic_70000.sb > /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/client_traffic_70000_3

sudo pkill -f nethogs
ssh debian@SharemindServer2 sudo pkill -f nethogs
ssh debian@SharemindServer3 sudo pkill -f nethogs


sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server1/log_traffic_1000_3>temp &
ssh debian@SharemindServer2 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server2/log_traffic_1000_3>temp &
ssh debian@SharemindServer3 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server3/log_traffic_1000_3>temp &

sudo sharemind-runscript reduced_Algorithm_Final_traffic_1000.sb > /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/client_traffic_1000_3

sudo pkill -f nethogs
ssh debian@SharemindServer2 sudo pkill -f nethogs
ssh debian@SharemindServer3 sudo pkill -f nethogs


sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server1/log_traffic_100_3>temp &
ssh debian@SharemindServer2 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server2/log_traffic_100_3>temp &
ssh debian@SharemindServer3 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server3/log_traffic_100_3>temp &

sudo sharemind-runscript reduced_Algorithm_Final_traffic_100.sb > /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/client_traffic_100_3

sudo pkill -f nethogs
ssh debian@SharemindServer2 sudo pkill -f nethogs
ssh debian@SharemindServer3 sudo pkill -f nethogs


sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server1/log_traffic_3>temp &
ssh debian@SharemindServer2 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server2/log_traffic_3>temp &
ssh debian@SharemindServer3 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server3/log_traffic_3>temp &

sudo sharemind-runscript reduced_Algorithm_Final_traffic.sb > /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/client_traffic_3

sudo pkill -f nethogs
ssh debian@SharemindServer2 sudo pkill -f nethogs
ssh debian@SharemindServer3 sudo pkill -f nethogs

#################################################################
sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server1/log_bpi_1000_3>temp &
ssh debian@SharemindServer2 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server2/log_bpi_1000_3>temp &
ssh debian@SharemindServer3 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server3/log_bpi_1000_3>temp &

sudo sharemind-runscript reduced_Algorithm_Final_bpi_1000.sb > /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/client_bpi_1000_3

sudo pkill -f nethogs
ssh debian@SharemindServer2 sudo pkill -f nethogs
ssh debian@SharemindServer3 sudo pkill -f nethogs


sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server1/log_bpi_100_3>temp &
ssh debian@SharemindServer2 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server2/log_bpi_100_3>temp &
ssh debian@SharemindServer3 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server3/log_bpi_100_3>temp &

sudo sharemind-runscript reduced_Algorithm_Final_bpi_100.sb > /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/client_bpi_100

sudo pkill -f nethogs
ssh debian@SharemindServer2 sudo pkill -f nethogs
ssh debian@SharemindServer3 sudo pkill -f nethogs


sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server1/log_bpi_3>temp &
ssh debian@SharemindServer2 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server2/log_bpi_3>temp &
ssh debian@SharemindServer3 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server3/log_bpi_3>temp &

sudo sharemind-runscript reduced_Algorithm_Final_bpi.sb > /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/client_bpi_3

sudo pkill -f nethogs
ssh debian@SharemindServer2 sudo pkill -f nethogs
ssh debian@SharemindServer3 sudo pkill -f nethogs



sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server1/log_traffic_70000_4>temp &
ssh debian@SharemindServer2 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server2/log_traffic_70000_4>temp &
ssh debian@SharemindServer3 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server3/log_traffic_70000_4>temp &

sudo sharemind-runscript reduced_Algorithm_Final_traffic_70000.sb > /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/client_traffic_70000_4

sudo pkill -f nethogs
ssh debian@SharemindServer2 sudo pkill -f nethogs
ssh debian@SharemindServer3 sudo pkill -f nethogs


sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server1/log_traffic_1000_4>temp &
ssh debian@SharemindServer2 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server2/log_traffic_1000_4>temp &
ssh debian@SharemindServer3 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server3/log_traffic_1000_4>temp &

sudo sharemind-runscript reduced_Algorithm_Final_traffic_1000.sb > /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/client_traffic_1000_4

sudo pkill -f nethogs
ssh debian@SharemindServer2 sudo pkill -f nethogs
ssh debian@SharemindServer3 sudo pkill -f nethogs


sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server1/log_traffic_100_4>temp &
ssh debian@SharemindServer2 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server2/log_traffic_100_4>temp &
ssh debian@SharemindServer3 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server3/log_traffic_100_4>temp &

sudo sharemind-runscript reduced_Algorithm_Final_traffic_100.sb > /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/client_traffic_100_4

sudo pkill -f nethogs
ssh debian@SharemindServer2 sudo pkill -f nethogs
ssh debian@SharemindServer3 sudo pkill -f nethogs


sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server1/log_traffic_4>temp &
ssh debian@SharemindServer2 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server2/log_traffic_4>temp &
ssh debian@SharemindServer3 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server3/log_traffic_4>temp &

sudo sharemind-runscript reduced_Algorithm_Final_traffic.sb > /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/client_traffic_4

sudo pkill -f nethogs
ssh debian@SharemindServer2 sudo pkill -f nethogs
ssh debian@SharemindServer3 sudo pkill -f nethogs

#################################################################
sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server1/log_bpi_1000_4>temp &
ssh debian@SharemindServer2 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server2/log_bpi_1000_4>temp &
ssh debian@SharemindServer3 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server3/log_bpi_1000_4>temp &

sudo sharemind-runscript reduced_Algorithm_Final_bpi_1000.sb > /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/client_bpi_1000_4

sudo pkill -f nethogs
ssh debian@SharemindServer2 sudo pkill -f nethogs
ssh debian@SharemindServer3 sudo pkill -f nethogs


sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server1/log_bpi_100_4>temp &
ssh debian@SharemindServer2 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server2/log_bpi_100_4>temp &
ssh debian@SharemindServer3 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server3/log_bpi_100_4>temp &

sudo sharemind-runscript reduced_Algorithm_Final_bpi_100.sb > /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/client_bpi_100

sudo pkill -f nethogs
ssh debian@SharemindServer2 sudo pkill -f nethogs
ssh debian@SharemindServer3 sudo pkill -f nethogs


sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server1/log_bpi_4>temp &
ssh debian@SharemindServer2 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server2/log_bpi_4>temp &
ssh debian@SharemindServer3 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server3/log_bpi_4>temp &

sudo sharemind-runscript reduced_Algorithm_Final_bpi.sb > /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/client_bpi_4

sudo pkill -f nethogs
ssh debian@SharemindServer2 sudo pkill -f nethogs
ssh debian@SharemindServer3 sudo pkill -f nethogs


sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server1/log_traffic_70000_5>temp &
ssh debian@SharemindServer2 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server2/log_traffic_70000_5>temp &
ssh debian@SharemindServer3 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server3/log_traffic_70000_5>temp &

sudo sharemind-runscript reduced_Algorithm_Final_traffic_70000.sb > /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/client_traffic_70000_5

sudo pkill -f nethogs
ssh debian@SharemindServer2 sudo pkill -f nethogs
ssh debian@SharemindServer3 sudo pkill -f nethogs


sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server1/log_traffic_1000_5>temp &
ssh debian@SharemindServer2 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server2/log_traffic_1000_5>temp &
ssh debian@SharemindServer3 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server3/log_traffic_1000_5>temp &

sudo sharemind-runscript reduced_Algorithm_Final_traffic_1000.sb > /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/client_traffic_1000_5

sudo pkill -f nethogs
ssh debian@SharemindServer2 sudo pkill -f nethogs
ssh debian@SharemindServer3 sudo pkill -f nethogs


sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server1/log_traffic_100_5>temp &
ssh debian@SharemindServer2 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server2/log_traffic_100_5>temp &
ssh debian@SharemindServer3 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server3/log_traffic_100_5>temp &

sudo sharemind-runscript reduced_Algorithm_Final_traffic_100.sb > /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/client_traffic_100_5

sudo pkill -f nethogs
ssh debian@SharemindServer2 sudo pkill -f nethogs
ssh debian@SharemindServer3 sudo pkill -f nethogs


sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server1/log_traffic_5>temp &
ssh debian@SharemindServer2 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server2/log_traffic_5>temp &
ssh debian@SharemindServer3 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server3/log_traffic_5>temp &

sudo sharemind-runscript reduced_Algorithm_Final_traffic.sb > /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/client_traffic_5

sudo pkill -f nethogs
ssh debian@SharemindServer2 sudo pkill -f nethogs
ssh debian@SharemindServer3 sudo pkill -f nethogs

#################################################################
sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server1/log_bpi_1000_5>temp &
ssh debian@SharemindServer2 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server2/log_bpi_1000_5>temp &
ssh debian@SharemindServer3 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server3/log_bpi_1000_5>temp &

sudo sharemind-runscript reduced_Algorithm_Final_bpi_1000.sb > /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/client_bpi_1000_5

sudo pkill -f nethogs
ssh debian@SharemindServer2 sudo pkill -f nethogs
ssh debian@SharemindServer3 sudo pkill -f nethogs


sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server1/log_bpi_100_5>temp &
ssh debian@SharemindServer2 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server2/log_bpi_100_5>temp &
ssh debian@SharemindServer3 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server3/log_bpi_100_5>temp &

sudo sharemind-runscript reduced_Algorithm_Final_bpi_100.sb > /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/client_bpi_100

sudo pkill -f nethogs
ssh debian@SharemindServer2 sudo pkill -f nethogs
ssh debian@SharemindServer3 sudo pkill -f nethogs


sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server1/log_bpi_5>temp &
ssh debian@SharemindServer2 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server2/log_bpi_5>temp &
ssh debian@SharemindServer3 sudo nohup nethogs -t |tee /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/logs/server3/log_bpi_5>temp &

sudo sharemind-runscript reduced_Algorithm_Final_bpi.sb > /home/debian/sharemind/Github/SecureMPCBPM/SecreC_implementation/experiment/client_bpi_5

sudo pkill -f nethogs
ssh debian@SharemindServer3 sudo pkill -f nethogs
ssh debian@SharemindServer2 sudo pkill -f nethogs
'