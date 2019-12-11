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
// *********************


//domain pd_shared3p shared3p;

void print_DFG(uint64 [[3]] DFG_matrix){
print("***********************************");
print(" the DFG matrix will be as follows ");
for(uint i=0; i<shape(DFG_matrix)[0]; i++){
    for(uint j =0; j < shape(DFG_matrix)[0] ; j++){

        print("(", DFG_matrix[i,j,0],",",DFG_matrix[i,j,1], ")");
    }

}
}





pd_shared3p uint64 [[3]]  DFG_calculation( pd_shared3p uint64[[3]] data_chunk, uint total_count,uint column_count){

//void DFG_calculation( pd_shared3p uint64[[3]] data_chunk, uint total_count,uint column_count){
print("************ DFG_calculation **************");


uint32 section_chunk = newSectionType("per_chunk_sort");
uint32 id_chunk = startSection(section_chunk,1::uint64);

print("************ before sorting **************");
data_chunk= quickSortPar(data_chunk,0::uint);
print("************ 2nd step sorting **************");
data_chunk= quickSortPar(data_chunk,1::uint);
print("************ after sorting **************");

endSection(id_chunk);


uint32 section_reshaping = newSectionType("reshaping");
uint32 id_reshaping = startSection(section_reshaping,2::uint64);
pd_shared3p uint64[[2]] data(total_count,column_count);
data=myReshape(data_chunk, shape(data)[0], shape(data)[1]);
uint data_size_1= size(data[:,0]);
endSection(id_reshaping);
print("************* after reshaping ***********");

uint32 section_shifting = newSectionType("shifting_and_subtraction");
uint32 id_shifting = startSection(section_shifting,1::uint64);
// copy the data to another shifted version
pd_shared3p uint [[2]] shifted=data[1:,:];



pd_shared3p uint64[[1]]tid =data[:data_size_1-1,0];
pd_shared3p uint64[[1]]tid2=shifted[:,0];
pd_shared3p uint64[[2]]event =data[:data_size_1-1,2:];
pd_shared3p uint64[[2]]event2 =shifted[:,2:];
pd_shared3p uint64 [[1]] subtraction=data[:data_size_1-1,1]-shifted[:,1];

endSection(id_shifting);

print("************ Building final matrix**************");
uint data_size = size(subtraction);
pd_shared3p uint64 [[2]] result_time(shape(event)[1], shape(event)[1])=0;
pd_shared3p uint64 [[2]] result_freq(shape(event)[1], shape(event)[1])=0;

uint32 section_boolean = newSectionType("boolean_comparison");
uint32 id_boolean = startSection(section_boolean,1::uint64);
pd_shared3p bool [[1]] b = (tid == tid2);
endSection(id_boolean);

print("************ my transpose bb **************");

uint32 section_transpose = newSectionType("transpose");
uint32 id_traspose = startSection(section_transpose,1::uint64);
pd_shared3p uint64 [[2]] bb=  myTranspose(myReshape(copyBlock((uint)b,shape(event)[1]),shape(event)[1], shape(event)[0]  ));
endSection(id_traspose);

print("************ my transpose  subtractionCopies**************");
id_traspose = startSection(section_transpose,2::uint64);
pd_shared3p uint64 [[2]] subtractionCopies=  myTranspose(myReshape(copyBlock((uint)subtraction,shape(event)[1]* shape(event)[1]),shape(event)[1] *shape(event)[1], shape(event)[0]  ));
endSection(id_traspose);

print("************ outerProduct**************");
uint32 section_outerProduct = newSectionType("outerProduct");
uint32 id_outer = startSection(section_outerProduct,1::uint64);
pd_shared3p uint64[[2]] M = outerProduct(event * bb, event2);
endSection(id_outer);

print("************ matrix multiplication **************");
uint32 section_mat = newSectionType("matrixMultiplication");
uint32 id_mat = startSection(section_mat,1::uint64);
pd_shared3p uint64 [[2]]temp_time = M* subtractionCopies;
endSection(id_mat);

print("************ first colSums **************");
uint32 section_colSums = newSectionType("colSums");
uint32 id_colSums = startSection(section_colSums,1::uint64);
pd_shared3p uint64 [[1]] time_sum =  myColSums(temp_time);
endSection(id_colSums);
print("************ second colSums **************");
id_colSums = startSection(section_colSums,2::uint64);
pd_shared3p uint64 [[1]] freq_sum = myColSums(M);
endSection(id_colSums);


uint32 section_combine= newSectionType("combine");
uint32 id_combine= startSection(section_combine,1::uint64);

print("************ Final Reshaping**************");
result_time = myReshape( time_sum, shape(event)[1], shape(event)[1]) ;
result_freq= myReshape( freq_sum, shape(event)[1], shape(event)[1]);

pd_shared3p uint64 [[3]] result(shape(event)[1], shape(event)[1],2);

print("************ Combining Results **************");
result[:,:,0]=result_freq;
result[:,:,1]=result_time;

endSection(id_combine);

return result;
//return 0;
}

void main() {

uint32 section = newSectionType("full_run");
uint32 id_full = startSection(section,1::uint64);


uint32 section_prep = newSectionType("preprocessing");
uint32 id_prep = startSection(section_prep,1::uint64);

// input argument of the program
    string ds = "DS1"; // Data source name
    string tbl = "eventlog"; // Table name

    uint ini_no_of_chunks= 8;
    uint event_per_case_A = 4;
    uint event_per_case_B = 2;

    string tbl_party_A=tbl+"_party_A";
    string tbl_party_B=tbl+"_party_B";
    // Open database before running operations on it
    tdbOpenConnection(ds);

    // getting the number of unique events for the number of bits
    uint unique_events= (tdbGetColumnCount(ds, tbl_party_A)+1)/2-3;


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
data_chunk[:no_of_chunks,:chunk_size_A,0] =myReshape((uint64) case_A[0:bound_A],no_of_chunks,chunk_size_A);
data_chunk[:no_of_chunks,:chunk_size_A,1] =myReshape((uint64) completeTime_A[0:bound_A],no_of_chunks,chunk_size_A);
for (uint i =0; i<unique_events; i++)
{
pd_shared3p uint8 [[1]] temp =  tdbReadColumn(ds, tbl_party_A, "b"+tostring(i));
data_chunk[:no_of_chunks,:chunk_size_A,i+2] =myReshape((uint64)temp[0:bound_A],no_of_chunks,chunk_size_A);
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

data_chunk[:no_of_chunks,chunk_size_A:,0] =myReshape((uint64) case_B[0:bound_B],no_of_chunks,chunk_size_B);
data_chunk[:no_of_chunks,chunk_size_A:,1] =myReshape((uint64) completeTime_B[0:bound_B],no_of_chunks,chunk_size_B);
for (uint i =0; i<unique_events; i++)
{
pd_shared3p uint8 [[1]] temp =  tdbReadColumn(ds, tbl_party_B, "b"+tostring(i));
data_chunk[:no_of_chunks,chunk_size_A:,i+2] =myReshape((uint64)temp[0:bound_B],no_of_chunks,chunk_size_B);
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

pd_shared3p uint64 [[3]] DFG_matrix =DFG_calculation(data_chunk,total_count,column_count);

endSection(id_full);

print_DFG(declassify(DFG_matrix));


// closing connection
    print("************************* closing connection *****************************");
    tdbCloseConnection(ds);


}
