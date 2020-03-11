import shared3p;
import shared3p_table_database;
import stdlib;
import table_database;
import shared3p_matrix;

import profiling;
import aux_sorting; //sorting and reshaping
import DFG;





void main() {



    string ds = "DS1"; // Data source name
    string tbl = "activities_10"; // Table name
    string tbl_party_A=tbl+"_party_A";
    string tbl_party_B=tbl+"_party_B";

   uint ini_no_of_chunks= 20;
   pd_shared3p uint event_per_case_A = 23;
   pd_shared3p uint event_per_case_B = 22;


   uint32 section = newSectionType("full_run_"+tbl+"_"+tostring(ini_no_of_chunks));
   uint32 id_full = startSection(section,1::uint64);


   uint32 section_prep = newSectionType("preprocessing_"+tbl+"_"+tostring(ini_no_of_chunks));
   uint32 id_prep = startSection(section_prep,1::uint64);
    // Open database before running operations on it
    tdbOpenConnection(ds);

    uint unique_events= (tdbGetColumnCount(ds, tbl_party_A)+1)/2-2; //division by 2 as there is a flag column after each column
    print("************ Reading the events **************");

// reading party A columns
pd_shared3p uint32 [[1]] case_A = tdbReadColumn(ds, tbl_party_A, "case");
pd_shared3p uint32 [[1]] completeTime_A = tdbReadColumn(ds, tbl_party_A, "completeTime");


// reading party B columns
pd_shared3p uint32 [[1]] case_B = tdbReadColumn(ds, tbl_party_B, "case");
pd_shared3p uint32 [[1]] completeTime_B = tdbReadColumn(ds, tbl_party_B, "completeTime");

/*
Based on our assumption the followig values are shared between the 2 parties:
    * the number of unique events, which will be used for the number of bits.
    * the maximum number of events per a trace, which will be used for the chunk calculations
*/



pd_shared3p uint event_per_case = event_per_case_A+event_per_case_B;
uint size_A=size(case_A);
uint size_B=size(case_B);

uint column_count= 2+unique_events; // 2 (trace, completeTime) columns + 7 bits (event)
uint64 no_of_cases=declassify(size_A/event_per_case_A);




uint no_of_chunks = 0;
if (no_of_cases % ini_no_of_chunks==0){

    no_of_chunks= ini_no_of_chunks;
}else{
    ini_no_of_chunks=ini_no_of_chunks-1;
    no_of_chunks= ini_no_of_chunks-1;
   }

pd_shared3p uint64 result =no_of_cases/no_of_chunks*no_of_chunks;
uint64 bound = declassify(result);
uint64 bound_A = declassify(bound*event_per_case_A);
uint64 bound_B =declassify( bound*event_per_case_B);




//data[:size(case_A),0]= mySetSlice((uint64)case_A,data[:size(case_A),0],0::uint,size(case_A));

uint chunk_size_A= bound_A/no_of_chunks;
uint chunk_size_B= bound_B/no_of_chunks;

uint total_count=(chunk_size_A + chunk_size_B)* ini_no_of_chunks;
pd_shared3p uint64[[3]] data_chunk (ini_no_of_chunks,chunk_size_A+chunk_size_B,column_count);



//*****************************************************
// inserting party_A columns into the dataset
//*****************************************************
data_chunk[:no_of_chunks,:chunk_size_A,0] =_Reshape((uint64) case_A[0:bound_A],no_of_chunks,chunk_size_A);
data_chunk[:no_of_chunks,:chunk_size_A,1] =_Reshape((uint64) completeTime_A[0:bound_A],no_of_chunks,chunk_size_A);
for (uint i =0; i<unique_events; i++)
{
pd_shared3p uint8 [[1]] temp =  tdbReadColumn(ds, tbl_party_A, "b"+tostring(i));
data_chunk[:no_of_chunks,:chunk_size_A,i+2] =_Reshape((uint64)temp[0:bound_A],no_of_chunks,chunk_size_A);
}



//********************** in case of size is not divisable of chunks
if (no_of_chunks != ini_no_of_chunks)
{data_chunk[no_of_chunks,:size_A-bound_A,0] =(uint64) case_A[bound_A:];
data_chunk[no_of_chunks,:size_A-bound_A,1] =(uint64) completeTime_A[bound_A:];

for (uint i =0; i<unique_events; i++)
{
pd_shared3p uint8 [[1]] temp =  tdbReadColumn(ds, tbl_party_A, "b"+tostring(i));
data_chunk[no_of_chunks,:size_A-bound_A,i+2] =(uint64)temp[bound_A:];
}
}



//*****************************************************
// inserting party_B columns into the dataset
//*****************************************************

data_chunk[:no_of_chunks,chunk_size_A:,0] =_Reshape((uint64) case_B[0:bound_B],no_of_chunks,chunk_size_B);
data_chunk[:no_of_chunks,chunk_size_A:,1] =_Reshape((uint64) completeTime_B[0:bound_B],no_of_chunks,chunk_size_B);
for (uint i =0; i<unique_events; i++)
{
pd_shared3p uint8 [[1]] temp =  tdbReadColumn(ds, tbl_party_B, "b"+tostring(i));
data_chunk[:no_of_chunks,chunk_size_A:,i+2] =_Reshape((uint64)temp[0:bound_B],no_of_chunks,chunk_size_B);
}



//********************** in case of size is not divisable of chunks
if (no_of_chunks != ini_no_of_chunks){

data_chunk[no_of_chunks,size_A-bound_A:size_A-bound_A+size_B-bound_B,0] =(uint64) case_B[bound_B:];
data_chunk[no_of_chunks,size_A-bound_A:size_A-bound_A+size_B-bound_B,1] =(uint64) completeTime_B[bound_B:];

for (uint i =0; i<unique_events; i++)
{
pd_shared3p uint8 [[1]] temp =  tdbReadColumn(ds, tbl_party_B, "b"+tostring(i));
data_chunk[no_of_chunks,size_A-bound_A:size_A-bound_A+size_B-bound_B,i+2] =(uint64)temp[bound_B:];
}
}

//**************************************************************************


endSection(id_prep);


string log_string="_"+tbl+"_"+tostring(ini_no_of_chunks);
pd_shared3p uint64 [[3]] DFG_matrix =DFG_calculation(data_chunk,total_count,column_count,log_string);

endSection(id_full);

print_DFG(declassify(DFG_matrix));


// closing connection
print("************************* closing connection *****************************");
tdbCloseConnection(ds);


}
