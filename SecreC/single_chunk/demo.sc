import shared3p;
import shared3p_table_database;
import stdlib;
import table_database;
import shared3p_matrix;

import profiling;
// **** importing scripts from Alisa ****
import rearrange;
import sorts;
import bitextr;
import aux_sorting;
import DFG;
import demo_party_A_data;
import demo_party_B_data;
// *********************


void main() {
//load_party_A();
//load_party_B();
uint32 section = newSectionType("full_run");
uint32 id_full = startSection(section,1::uint64);


uint32 section_prep = newSectionType("preprocessing");
uint32 id_prep = startSection(section_prep,1::uint64);

// input argument of the program
    string ds = "DS1"; // Data source name
    string tbl = "demo"; // Table name

    uint ini_no_of_chunks= 8;
    uint event_per_case_A = 4;
    uint event_per_case_B = 2;

    string tbl_party_A=tbl+"_party_A";
    string tbl_party_B=tbl+"_party_B";
    // Open database before running operations on it
    tdbOpenConnection(ds);


    // getting the number of unique events for the number of bits
    uint unique_events= (tdbGetColumnCount(ds, tbl_party_A)+1)/2-2;


    print("************ Reading the events **************");

    // reading party A columns
    pd_shared3p uint64 [[1]] case_A = tdbReadColumn(ds, tbl_party_A, (uint)0);
    pd_shared3p uint64 [[1]] completeTime_A = tdbReadColumn(ds, tbl_party_A, 1::uint);


    // reading party B columns
    pd_shared3p uint64 [[1]] case_B = tdbReadColumn(ds, tbl_party_B, 0::uint);
    pd_shared3p uint64 [[1]] completeTime_B = tdbReadColumn(ds, tbl_party_B, 1::uint);

///*
//    Based on our assumption the followig values are shared between the 2 parties:
//        * the number of unique events, which will be used for the number of bits.
//        * the maximum number of events per a trace, which will be used for the chunk calculations
//*/



uint event_per_case = event_per_case_A+event_per_case_B;
uint size_A=size(case_A);
uint size_B=size(case_B);

uint column_count= 2+unique_events; // 2 (trace, completeTime) columns + 7 bits (event)
uint64 no_of_cases= size_A/event_per_case_A;




uint no_of_chunks = 0;
    if (no_of_cases % ini_no_of_chunks==0){

        no_of_chunks= ini_no_of_chunks;
    }else{
        no_of_chunks= ini_no_of_chunks-1;
       }


uint64 bound = no_of_cases/no_of_chunks*no_of_chunks;
uint64 bound_A = bound*event_per_case_A;
uint64 bound_B = bound*event_per_case_B;



//data[:size(case_A),0]= mySetSlice((uint64)case_A,data[:size(case_A),0],0::uint,size(case_A));

uint chunk_size_A= bound_A/no_of_chunks;
uint chunk_size_B= bound_B/no_of_chunks;

uint total_count=(chunk_size_A + chunk_size_B)* ini_no_of_chunks;
pd_shared3p uint64[[3]] data_chunk (ini_no_of_chunks,chunk_size_A+chunk_size_B,column_count);



//*****************************************************
// inserting party_A columns into the dataset
//*****************************************************
data_chunk[:no_of_chunks,:chunk_size_A,0] =myReshape( case_A[0:bound_A],no_of_chunks,chunk_size_A);
data_chunk[:no_of_chunks,:chunk_size_A,1] =myReshape( completeTime_A[0:bound_A],no_of_chunks,chunk_size_A);
for (uint i =2; i<unique_events+2; i++)
{
pd_shared3p uint64 [[1]] temp =  tdbReadColumn(ds, tbl_party_A, (uint)i);
data_chunk[:no_of_chunks,:chunk_size_A,i] =myReshape(temp[0:bound_A],no_of_chunks,chunk_size_A);
}


//********************** in case of size is not divisable of chunks
if (no_of_chunks != ini_no_of_chunks)
    {data_chunk[no_of_chunks,:size_A-bound_A,0] =case_A[bound_A:];
    data_chunk[no_of_chunks,:size_A-bound_A,1] = completeTime_A[bound_A:];

    for (uint i =2; i<unique_events+2; i++)
    {
    pd_shared3p uint64 [[1]] temp =  tdbReadColumn(ds, tbl_party_A, i);
    data_chunk[no_of_chunks,:size_A-bound_A,i] =temp[bound_A:];
    }
}



////*****************************************************
//// inserting party_B columns into the dataset
////*****************************************************

data_chunk[:no_of_chunks,chunk_size_A:,0] =myReshape( case_B[0:bound_B],no_of_chunks,chunk_size_B);
data_chunk[:no_of_chunks,chunk_size_A:,1] =myReshape( completeTime_B[0:bound_B],no_of_chunks,chunk_size_B);
for (uint i =2; i<unique_events+2; i++)
{
pd_shared3p uint64 [[1]] temp =  tdbReadColumn(ds, tbl_party_B, (uint)i);
data_chunk[:no_of_chunks,chunk_size_A:,i] =myReshape(temp[0:bound_B],no_of_chunks,chunk_size_B);
}


//********************** in case of size is not divisable of chunks
if (no_of_chunks != ini_no_of_chunks){

    data_chunk[no_of_chunks,size_A-bound_A:size_A-bound_A+size_B-bound_B,0] =case_B[bound_B:];
    data_chunk[no_of_chunks,size_A-bound_A:size_A-bound_A+size_B-bound_B,1] =completeTime_B[bound_B:];

    for (uint i =2; i<unique_events+2; i++)
    {
    pd_shared3p uint64 [[1]] temp =  tdbReadColumn(ds, tbl_party_B, i);
    data_chunk[no_of_chunks,size_A-bound_A:size_A-bound_A+size_B-bound_B,i] =temp[bound_B:];
    }
}

////**************************************************************************


endSection(id_prep);

pd_shared3p uint64 [[3]] DFG_matrix =DFG_calculation(data_chunk,total_count,column_count);

endSection(id_full);

print_DFG(declassify(DFG_matrix));


// closing connection
    print("************************* closing connection *****************************");
    tdbCloseConnection(ds);


}
