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



//************** element wise DFG **************

pd_shared3p uint64 [[3]]  DFG_elementwise( pd_shared3p uint64[[1]]tid ,pd_shared3p uint64[[1]]tid2, pd_shared3p uint64[[2]]event ,pd_shared3p uint64[[2]]event2 , pd_shared3p uint64 [[1]] subtraction){

uint data_size = size(subtraction);
pd_shared3p uint64 [[2]] result_time(shape(event)[1], shape(event)[1])=0;
pd_shared3p uint64 [[2]] result_freq(shape(event)[1], shape(event)[1])=0;

pd_shared3p bool [[1]] b = (tid == tid2);
pd_shared3p uint64 [[2]] bb=  myTranspose(reshape(copyBlock((uint)b,shape(event)[1]),shape(event)[1], shape(event)[0]  ));
pd_shared3p uint64 [[2]] subtractionCopies=  myTranspose(reshape(copyBlock((uint)subtraction,shape(event)[1]* shape(event)[1]),shape(event)[1] *shape(event)[1], shape(event)[0]  ));
pd_shared3p uint64[[2]] M = outerProduct(event * bb, event2);

pd_shared3p uint64 [[2]]temp_time = M* subtractionCopies;
pd_shared3p uint64 [[1]] time_sum =  colSums(temp_time);
pd_shared3p uint64 [[1]] freq_sum = colSums(M);

result_time = reshape( time_sum, shape(event)[1], shape(event)[1]) ;
result_freq= reshape( freq_sum, shape(event)[1], shape(event)[1]);

pd_shared3p uint64 [[3]] result(shape(event)[1], shape(event)[1],2);
result[:,:,0]=result_freq;
result[:,:,1]=result_time;
return result;

}




pd_shared3p uint64 [[3]]  DFG_calculation( pd_shared3p uint64[[3]] data_chunk, uint total_count,uint column_count){
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

}


void main() {

uint32 section = newSectionType("full_run");
uint32 id_full = startSection(section,1::uint64);


uint32 section_prep = newSectionType("preprocessing");
uint32 id_prep = startSection(section_prep,1::uint64);

    string ds = "DS1"; // Data source name
    string tbl = "traffic"; // Table name
    string tbl_party_A="traffic_party_A";
    string tbl_party_B="traffic_party_B";

    // Open database before running operations on it
    tdbOpenConnection(ds);

    print("************ Reading the events **************");

    // reading party A columns
    pd_shared3p uint32 [[1]] case_A = tdbReadColumn(ds, tbl_party_A, "case");
    pd_shared3p uint32 [[1]] completeTime_A = tdbReadColumn(ds, tbl_party_A, "completeTime");

    // reading bits
    pd_shared3p uint8 [[1]] b0_A = tdbReadColumn(ds, tbl_party_A, "b0");
    pd_shared3p uint8 [[1]] b1_A = tdbReadColumn(ds, tbl_party_A, "b1");
    pd_shared3p uint8 [[1]] b2_A = tdbReadColumn(ds, tbl_party_A, "b2");
    pd_shared3p uint8 [[1]] b3_A = tdbReadColumn(ds, tbl_party_A, "b3");
    pd_shared3p uint8 [[1]] b4_A = tdbReadColumn(ds, tbl_party_A, "b4");
    pd_shared3p uint8 [[1]] b5_A = tdbReadColumn(ds, tbl_party_A, "b5");
    pd_shared3p uint8 [[1]] b6_A = tdbReadColumn(ds, tbl_party_A, "b6");
    pd_shared3p uint8 [[1]] b7_A = tdbReadColumn(ds, tbl_party_A, "b7");
    pd_shared3p uint8 [[1]] b8_A = tdbReadColumn(ds, tbl_party_A, "b8");
    pd_shared3p uint8 [[1]] b9_A = tdbReadColumn(ds, tbl_party_A, "b9");
    pd_shared3p uint8 [[1]] b10_A = tdbReadColumn(ds, tbl_party_A, "b10");


print("************ Reading party B **************");

    // reading party B columns
    pd_shared3p uint32 [[1]] case_B = tdbReadColumn(ds, tbl_party_B, "case");
    pd_shared3p uint32 [[1]] completeTime_B = tdbReadColumn(ds, tbl_party_B, "completeTime");


    // reading bits
    pd_shared3p uint8 [[1]] b0_B = tdbReadColumn(ds, tbl_party_B, "b0");
    pd_shared3p uint8 [[1]] b1_B = tdbReadColumn(ds, tbl_party_B, "b1");
    pd_shared3p uint8 [[1]] b2_B = tdbReadColumn(ds, tbl_party_B, "b2");
    pd_shared3p uint8 [[1]] b3_B = tdbReadColumn(ds, tbl_party_B, "b3");
    pd_shared3p uint8 [[1]] b4_B = tdbReadColumn(ds, tbl_party_B, "b4");
    pd_shared3p uint8 [[1]] b5_B = tdbReadColumn(ds, tbl_party_B, "b5");
    pd_shared3p uint8 [[1]] b6_B = tdbReadColumn(ds, tbl_party_B, "b6");
    pd_shared3p uint8 [[1]] b7_B = tdbReadColumn(ds, tbl_party_B, "b7");
    pd_shared3p uint8 [[1]] b8_B = tdbReadColumn(ds, tbl_party_B, "b8");
    pd_shared3p uint8 [[1]] b9_B = tdbReadColumn(ds, tbl_party_B, "b9");
    pd_shared3p uint8 [[1]] b10_B = tdbReadColumn(ds, tbl_party_B, "b10");



    /*
        Based on our assumption the followig values are shared between the 2 parties:
            * the number of unique events, which will be used for the number of bits.
            * the maximum number of events per a trace, which will be used for the chunk calculations
    */
    uint unique_events= 11;
    uint event_per_case_A = 16;
    uint event_per_case_B = 5;
    uint event_per_case = event_per_case_A+event_per_case_B;
    uint size_A=size(case_A);
    uint size_B=size(case_B);
    uint total_count=size(case_A)+size(case_B);
    uint column_count= 2+unique_events; // 2 (trace, completeTime) columns + 7 bits (event)

    uint no_of_chunks = 100; // 1,2,4,5,10,20,100
//   appending the two logs in a 2D array
pd_shared3p uint64[[2]] data(total_count,column_count);
pd_shared3p uint64[[3]] data_chunk (no_of_chunks,total_count/no_of_chunks,column_count);


//data[:size(case_A),0]= mySetSlice((uint64)case_A,data[:size(case_A),0],0::uint,size(case_A));

uint chunk_size_A= size_A/no_of_chunks;
uint chunk_size_B= size_B/no_of_chunks;

print("************ Reshaping party A **************");
data_chunk[:,:chunk_size_A,0] =myReshape((uint64) case_A,no_of_chunks,size_A/no_of_chunks);
//data[:size_A,1] = event_A;
data_chunk[:,:chunk_size_A,1] =myReshape((uint64) completeTime_A,no_of_chunks,size_A/no_of_chunks);
data_chunk[:,:chunk_size_A,2] =myReshape((uint64) b0_A,no_of_chunks,size_A/no_of_chunks);
data_chunk[:,:chunk_size_A,3] =myReshape((uint64) b1_A,no_of_chunks,size_A/no_of_chunks);
data_chunk[:,:chunk_size_A,4] =myReshape((uint64) b2_A,no_of_chunks,size_A/no_of_chunks);
data_chunk[:,:chunk_size_A,5] =myReshape((uint64) b3_A,no_of_chunks,size_A/no_of_chunks);
data_chunk[:,:chunk_size_A,6] =myReshape((uint64) b4_A,no_of_chunks,size_A/no_of_chunks);
data_chunk[:,:chunk_size_A,7] =myReshape((uint64) b5_A,no_of_chunks,size_A/no_of_chunks);
data_chunk[:,:chunk_size_A,8] =myReshape((uint64) b6_A,no_of_chunks,size_A/no_of_chunks);


print("************ Reshaping party B **************");
data_chunk[:,chunk_size_A:,0] =myReshape((uint64) case_B,no_of_chunks,size_B/no_of_chunks);
//data[size_A:,1] = event_B;
data_chunk[:,chunk_size_A:,1] =myReshape((uint64) completeTime_B,no_of_chunks,size_B/no_of_chunks);
data_chunk[:,chunk_size_A:,2] =myReshape((uint64) b0_B,no_of_chunks,size_B/no_of_chunks);
data_chunk[:,chunk_size_A:,3] =myReshape((uint64) b1_B,no_of_chunks,size_B/no_of_chunks);
data_chunk[:,chunk_size_A:,4] =myReshape((uint64) b2_B,no_of_chunks,size_B/no_of_chunks);
data_chunk[:,chunk_size_A:,5] =myReshape((uint64) b3_B,no_of_chunks,size_B/no_of_chunks);
data_chunk[:,chunk_size_A:,6] =myReshape((uint64) b4_B,no_of_chunks,size_B/no_of_chunks);
data_chunk[:,chunk_size_A:,7] =myReshape((uint64) b5_B,no_of_chunks,size_B/no_of_chunks);
data_chunk[:,chunk_size_A:,8] =myReshape((uint64) b6_B,no_of_chunks,size_B/no_of_chunks);


////************* subsetting the data temporary
////data = data[0:500,:];


uint data_size= size(data[:,0]);

endSection(id_prep);


pd_shared3p uint64 [[3]] DFG_matrix =DFG_calculation(data_chunk,total_count,column_count);


endSection(id_full);

print_DFG(declassify(DFG_matrix));


// closing connection
    print("************************* closing connection *****************************");
    tdbCloseConnection(ds);


}
