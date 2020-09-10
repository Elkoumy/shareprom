# ShareProm
This repository contains the source code of ShareProm, a Secure Multi-party computation system for Inter-organizational Process Mining. The system is still under development. A research publication is pending for the system by [Gamal Elkoumy](https://scholar.google.com/citations?user=Y1ze0vQAAAAJ&hl=en&oi=ao), [Stephan A. Fahrenkrog-Peterson](https://scholar.google.com/citations?user=Le-1B90AAAAJ&hl=en&oi=sra), [Marlon Dumas](https://scholar.google.com/citations?user=9lIttRkAAAAJ&hl=en&oi=ao) , [Peeter Laud](https://scholar.google.com/citations?user=3hc5DR8AAAAJ&hl=en&oi=ao), [Alisa Pankova](https://scholar.google.com/citations?user=KG2eH5sAAAAJ&hl=en&oi=ao) and [Matthias Weldich](https://scholar.google.com/citations?user=P_9a7I0AAAAJ&hl=en).

This repository contains scripts for the implementation of Shareprom and for performing the experimental evaluation of the system. The system is implemented on top of Sharemind [1], a secure multi-party computation engine. The system architecture and evaluation experiments are presented in [[2](https://link.springer.com/chapter/10.1007/978-3-030-49418-6_11)]. A demonstration video of the tool could be found on [YouTube](https://youtu.be/uz2mrYz-y-w).


## System Requirement
The implementation of the system is in SecreC [3], a C++ like language that runs on top of Sharemind. The following are the required environmental setup for running the system:
* The installation of sharemind servers on 3 servers and the key authentication between the servers so they can communicate securely.
* Installation of sharemind client, that could be either on one of the 3 servers or a separate machine. Also, we need to share the key of the client on the 3 servers so they can trust the client applications.
* All the importing process of the CSV files should be performed using sharemind csv-importer. The CSV importer is only available for Academic and industrial licenses and we use our Academic license.
* The installation of linux package apt-transport-https
* The application implementation is using python
* Below is the required python packages.

You can download a pre-installed sharemind virtual machine for free from sharemind's [website](https://sharemind.cyber.ee/).

Or, you can install sharemind on your own environment using [sharemind server installation guide](https://docs.sharemind.cyber.ee/2019.03/installation/application-server) and [client installation guide](https://docs.sharemind.cyber.ee/2019.03/installation/client-applications). After the instraction on the guides run the following command on the three servers to download the package ```libsharemind-mod-shared3pdev-ctrl```:

```
sudo apt-get install libsharemind-mod-shared3pdev-ctrl
```
And on the client's machine to enabling running the script using clients:
```
sudo apt-get install sharemind-runscript
```

### Python Packages installation

Installation of pm4py library for the support of XES files.
```
pip install pm4py
```
Installation of svglib
```
sudo apt-get install graphviz
```
The installation directory should be included in the $PATH variable
```
pip install svglib
```
You can install the differential privacy library
```
pip install diffprivlib
```

## Data Format
The proposed approach assume the following:
* The data of partyA and partyB are loaded to the servers separately, each event log is in a separate file.
* To use the Outer product approach, the data should label the events in binary representation (0100), with each bit in a separate column.
* To use the parallel chunks, the data should include padding. The padding makes all the traces have the same length as the maximum length of traces of each party. 

Also, you can find preprocessing and data examples in python in the following directory:
```
shareprom/data_and_preprocessing/
```
In the current release of Shareprom, you don't need to perform the transformation. You can import the XES files using the client applications installed on both party A and party B.

## Running The System
* Start the 3 sharemind servers.
* Start the sharemind service on the 3 servers using the command:
```
sudo sharemind-server
```
* Once the sharemind servers are running, you can start running the application on the 3 servers. We assume one server for party A, one server for party B, and one server for the Analysis firm.

* On the 3 servers, move to the directory
```
cd shareprom/demo
```
* To run the application on party A
```
python ./GUI_application_partyA.py
```
* To run the application on party B
```
python ./GUI_application_partyB.py
```
* To run the application on the data analysis firm
```
python ./GUI_application_analysis_firm.py
```

You can use both the GUI of party A and party B to import the XES files. You can find a sample data on the same directory. The file ```manufacurer.xes``` for party A and the file ```supplier_B.xes``` for party B.

To get the result, on the data analysis firm, you can use the application to view the process model after applying the differential privacy.


## References
[1] Archer, David W., et al. "From Keys to Databases—Real-World Applications of Secure Multi-Party Computation." The Computer Journal 61.12 (2018): 1749-1771.

[2] Elkoumy, Gamal, et al. "Secure Multi-party Computation for Inter-organizational Process Mining." Enterprise, Business-Process and Information Systems Modeling. Springer, Cham, 2020. 166-181.

[3] Bogdanov, Dan, Peeter Laud, and Jaak Randmets. "Domain-polymorphic language for privacy-preserving applications." Proceedings of the First ACM workshop on Language support for privacy-enhancing technologies. ACM, 2013.

