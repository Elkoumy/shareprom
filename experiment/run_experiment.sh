#!/bin/bash
echo -n CCC19>tbl_name
sudo sharemind-runscript main.sb --str=TBL --str= --str=string --file=tbl_name --str=CHUNKS --str="" --str=uint64 --size=8 --uint64=8 --str=EVENTA --str pd_shared3p --str=uint64 --size=8 --uint64=27 --str=EVENTB --str pd_shared3p --str=uint64 --size=8 --uint64=12sudo sharemind-runscript main.sb --str=TBL --str= --str=string --file=tbl_name --str=CHUNKS --str="" --str=uint64 --size=8 --uint64=8 --str=EVENTA --str pd_shared3p --str=uint64 --size=8 --uint64=27 --str=EVENTB --str pd_shared3p --str=uint64 --size=8 --uint64=12


echo -n reduced_traffic>tbl_name
sudo sharemind-runscript main.sb --str=TBL --str= --str=string --file=tbl_name --str=CHUNKS --str="" --str=uint64 --size=8 --uint64=8 --str=EVENTA --str pd_shared3p --str=uint64 --size=8 --uint64=27 --str=EVENTB --str pd_shared3p --str=uint64 --size=8 --uint64=12sudo sharemind-runscript main.sb --str=TBL --str= --str=string --file=tbl_name --str=CHUNKS --str="" --str=uint64 --size=8 --uint64=8 --str=EVENTA --str pd_shared3p --str=uint64 --size=8 --uint64=15 --str=EVENTB --str pd_shared3p --str=uint64 --size=8 --uint64=5