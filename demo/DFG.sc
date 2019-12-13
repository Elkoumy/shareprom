import shared3p;
import shared3p_table_database;
import stdlib;
import table_database;
import shared3p_matrix;

import profiling;
// **** importing scripts from Alisa ****
import rearrange;
//import sorts;
//import bitextr;
import aux_sorting;
// *********************
domain pd_shared3p shared3p;



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
data=_Reshape(data_chunk, shape(data)[0], shape(data)[1]);
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
pd_shared3p uint64 [[2]] bb=  _Transpose(_Reshape(copyBlock((uint)b,shape(event)[1]),shape(event)[1], shape(event)[0]  ));
endSection(id_traspose);

print("************ my transpose  subtractionCopies**************");
id_traspose = startSection(section_transpose,2::uint64);
pd_shared3p uint64 [[2]] subtractionCopies=  _Transpose(_Reshape(copyBlock((uint)subtraction,shape(event)[1]* shape(event)[1]),shape(event)[1] *shape(event)[1], shape(event)[0]  ));
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
pd_shared3p uint64 [[1]] time_sum =  _ColSums(temp_time);
endSection(id_colSums);
print("************ second colSums **************");
id_colSums = startSection(section_colSums,2::uint64);
pd_shared3p uint64 [[1]] freq_sum = _ColSums(M);
endSection(id_colSums);


uint32 section_combine= newSectionType("combine");
uint32 id_combine= startSection(section_combine,1::uint64);

print("************ Final Reshaping**************");
result_time = _Reshape( time_sum, shape(event)[1], shape(event)[1]) ;
result_freq= _Reshape( freq_sum, shape(event)[1], shape(event)[1]);

pd_shared3p uint64 [[3]] result(shape(event)[1], shape(event)[1],2);

print("************ Combining Results **************");
result[:,:,0]=result_freq;
result[:,:,1]=result_time;

endSection(id_combine);

return result;
//return 0;
}

