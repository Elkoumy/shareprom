# ShareProm
This repository contains the source code of ShareProm, a Secure Multi-party computation approach for Inter-organizational Process Mining. The system is still under development. A research publication is pending for the system by [Gamal Elkoumy](https://scholar.google.com/citations?user=Y1ze0vQAAAAJ&hl=en&oi=ao), [Stephan A. Fahrenkrog-Peterson](https://scholar.google.com/citations?user=Le-1B90AAAAJ&hl=en&oi=sra), [Marlon Dumas](https://scholar.google.com/citations?user=9lIttRkAAAAJ&hl=en&oi=ao) , [Peeter Laud](https://scholar.google.com/citations?user=3hc5DR8AAAAJ&hl=en&oi=ao), [Alisa Pankova](https://scholar.google.com/citations?user=KG2eH5sAAAAJ&hl=en&oi=ao) and [Matthias Weldich](https://scholar.google.com/citations?user=P_9a7I0AAAAJ&hl=en).

This repository contains scripts for the implementation of Shareprom and for performing the experimental evaluation of the system. The system is implemented on top of Sharemind [1], a secure multi-party computation. 

## System Requirement
The implementation of the system is in SecreC [2], a C++-like language that runs on top of Sharemind. The following are the required environmental setup for running the system:
* The installation of sharemind servers on 3 servers and the key authentication between the servers so they can communicate securely.
* Installation of sharemind client, that could be either on one of the 3 servers or a separate machine. Also, we need to share the key of the client on the 3 servers so they can trust the client applications.
* All the importing process of the CSV files should be performed using sharemind csv-importer.
* The installation of linux package apt-transport-https
Furthermore, to run the application, the following sharemind packages should be installed:
* sharemind-server 
* libsharemind-mod-algorithms 
* libsharemind-mod-shared3pdev or libsharemind-mod-shared3p
* libsharemind-mod-tabledb-hdf5 
* scc 
* secrec-stdlib

To perform the profiling of the applications, you need to configure the servers and enable profiling property "Profiler=on"

## Installation instructions


## Configuration

## Usage




##References
[1] Archer, David W., et al. "From Keys to Databases—Real-World Applications of Secure Multi-Party Computation." The Computer Journal 61.12 (2018): 1749-1771.
[2] Bogdanov, Dan, Peeter Laud, and Jaak Randmets. "Domain-polymorphic language for privacy-preserving applications." Proceedings of the First ACM workshop on Language support for privacy-enhancing technologies. ACM, 2013.
