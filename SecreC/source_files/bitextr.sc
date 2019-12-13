module bitextr;

import stdlib;
import shared3p;
import prefixsums;
import rearrange;

//this module consists only of local operations!
template <domain D, dim N>
uint size_in_bits (D xor_uint8 [[N]] x)  { return 8; }
template <domain D, dim N>
uint size_in_bits (D xor_uint16 [[N]] x)  { return 16; }
template <domain D, dim N>
uint size_in_bits (D xor_uint32 [[N]] x)  { return 32; }
template <domain D, dim N>
uint size_in_bits (D xor_uint64 [[N]] x)  { return 64; }

template <domain D, dim N>
uint size_in_bits (D uint8 [[N]] x)  { return 8; }
template <domain D, dim N>
uint size_in_bits (D uint16 [[N]] x)  { return 16; }
template <domain D, dim N>
uint size_in_bits (D uint32 [[N]] x)  { return 32; }
template <domain D, dim N>
uint size_in_bits (D uint64 [[N]] x)  { return 64; }


template <domain D, dim N>
bool is_xor (D xor_uint8 [[N]] x)  { return true; }
template <domain D, dim N>
bool is_xor (D xor_uint16 [[N]] x)  { return true; }
template <domain D, dim N>
bool is_xor (D xor_uint32 [[N]] x)  { return true; }
template <domain D, dim N>
bool is_xor (D xor_uint64 [[N]] x)  { return true; }

template <domain D, dim N>
bool is_xor (D uint8 [[N]] x)  { return false; }
template <domain D, dim N>
bool is_xor (D uint16 [[N]] x)  { return false; }
template <domain D, dim N>
bool is_xor (D uint32 [[N]] x)  { return false; }
template <domain D, dim N>
bool is_xor (D uint64 [[N]] x)  { return false; }


template <domain D, type T, dim N>
D bool [[1]] bit_extract_all_types(D T [[N]] x) {

    //bit decomposition
    if (is_xor(x)){
        return bit_extract(x);
    } else {
        return bit_extract_uint(x);
    }
}

template <domain D, dim N>
D bool [[1]] bit_extract_all_types(D uint8  [[N]] x) { D xor_uint8  [[N]] kk = reshare(x); return bit_extract(kk); }
template <domain D, dim N>
D bool [[1]] bit_extract_all_types(D uint16 [[N]] x) { D xor_uint16 [[N]] kk = reshare(x); return bit_extract(kk); }
template <domain D, dim N>
D bool [[1]] bit_extract_all_types(D uint32 [[N]] x) { D xor_uint32 [[N]] kk = reshare(x); return bit_extract(kk); }
template <domain D, dim N>
D bool [[1]] bit_extract_all_types(D uint64 [[N]] x) { D xor_uint64 [[N]] kk = reshare(x); return bit_extract(kk); }

template <domain D, dim N>
D bool [[1]] bit_extract_all_types(D xor_uint8  [[N]] x) { return bit_extract(x); }
template <domain D, dim N>
D bool [[1]] bit_extract_all_types(D xor_uint16 [[N]] x) { return bit_extract(x); }
template <domain D, dim N>
D bool [[1]] bit_extract_all_types(D xor_uint32 [[N]] x) { return bit_extract(x); }
template <domain D, dim N>
D bool [[1]] bit_extract_all_types(D xor_uint64 [[N]] x) { return bit_extract(x); }


//convert a vector of bools to a xor_unit
template <domain D, type T >
D T bool_to_xor(D bool [[1]] X){
    uint n = shape(X)[0];
    D T [[1]] Z = (T)X & 1;
    int [[1]] shifts = glist(0 :: uint, n);
    D T [[1]] W = shiftBitsLeftVec(Z, shifts);
    return prefixSum(W)[n-1];
}

//convert a vector of bools to a xor_unit
template <domain D, type T >
D T [[1]] bool_to_xor(D bool [[2]] X){
    uint m = shape(X)[0];
    uint n = shape(X)[1];
    D T [[2]] Z = (T)X & 1;
    int [[1]] shifts = copiedGlist(m, n);
    D T [[2]] W = shiftBitsLeftVec(Z, shifts);
    return myGetProj(prefixSumPar(W), 1 :: uint, 0 :: uint, m, n-1, n);
}

//this is only 32-bit in the standard library, but a __syscall does exist!
template <domain D, type T, dim N>
D T[[N]] shiftBitsLeftVec (D T[[N]] bits, int [[1]] shifts) {
    __syscall ("shared3p::shift_left_$T\_vec", __domainid (D), bits, __cref shifts, bits);
    return bits;
}

//this is a parallelizable stack-based version that computes all the branches simultaneiously
//it returns all the branches alltogether
template <domain D >
D xor_uint8 [[2]] CharVecRec(D xor_uint8 [[2]] X){
    //print("CV level " + tostring(shape(X)[1]));
    //we have now n parallel algorithm instances running, that covers also the parallel branches
    //all the sizes of X[i] are the same
    //hence we can take if-then-else out and parallelize the inner contents only
    uint n = shape(X)[0];
    uint m = shape(X)[1];
    if(m == 0){
        D xor_uint8 [[2]] W (n,0);
        return W;
    }else if (m == 1){
        return MyCat(~X, X, 1);
    }else{
        //the input works like a stack
        //double each stack element and repeat the recursion
        bool cut_last = false;
        D xor_uint8 [[2]] left_X  = mySlice(X, 0 :: uint, shape(X)[0], 0 :: uint, m/2);
        D xor_uint8 [[2]] right_X = mySlice(X, 0 :: uint, shape(X)[0], m/2, shape(X)[1]);

        //if m is odd, then pad all the numbers by a meaningless most significant 0
        if (((m/2) * 2) != m){    
            D xor_uint8 [[2]] zero (n,1);
            left_X = MyCat(left_X, zero,1);
            cut_last = true;
        }
        D xor_uint8 [[2]] YZ = CharVecRec(MyCat(left_X,right_X,0));
        //now the first half is Y, and the second half is Z
        D xor_uint8 [[2]] Y = mySlice(YZ, 0 :: uint,    n, 0 :: uint, shape(YZ)[1]);
        D xor_uint8 [[2]] Z = mySlice(YZ, n, shape(YZ)[0], 0 :: uint, shape(YZ)[1]);
        if(cut_last){
            Y = mySlice(Y, 0 :: uint, shape(Y)[0], 0 :: uint, shape(Y)[1] / 2);
        }
        return outerProduct(Z,Y);
    }
}

//parallel characteristic vector (computed directly on a stack)
template <domain D, type T >
D xor_uint8 [[2]] CharVecPar(D T [[1]] X, uint logm,  uint m){
    uint n = size(X) / (8 :: uint);
    uint r = size(X) % (8 :: uint);
    if (r != 0){
        n = n + 1;
    }

    D T [[1]] Y (8 * n);
    Y = partialRearrange(X,Y);
    uint k = size_in_bits(Y);

    D bool [[2]] W = myTranspose(MyReshape(bit_extract(Y),8 :: uint, n * k));
    D xor_uint8 [[1]] Z = bool_to_xor(W);
    D xor_uint8 [[2]] XX = mySlice(MyReshape(Z, n, k), 0 :: uint, n, 0 :: uint, logm);
    D xor_uint8 [[2]] YY = CharVecRec(XX);
    return mySlice(YY, 0 :: uint, n, 0 :: uint, m);
}


//template <domain D , type T>
//D T [[2]] outerProduct(D T [[1]] X, D T [[1]] Y){
//    D T [[2]] Z (size(X), size(Y));
//    uint one = 1;
//    __syscall("shared3p::mat_mult_$T\_vec", __domainid(D), X, Y, Z, __cref size(X), __cref one, __cref size(Y));
//    return Z;
//}

//template <domain D , type T>
//D T [[2]] outerProduct(D T [[2]] X, D T [[2]] Y){
//    //assume shape(X)[0] = shape(Y)[0]
//    D T [[2]] Z (shape(X)[0], shape(X)[1] * shape(Y)[1]);
//    uint [[1]] mm (shape(X)[0]);
//    uint [[1]] nn (shape(X)[0]);
//    uint [[1]] pp (shape(X)[0]);
//    mm = shape(X)[1];
//    nn = 1;
//    pp = shape(Y)[1];
//    __syscall("shared3p::mat_mult_$T\_vec", __domainid(D), X, Y, Z, __cref mm, __cref nn, __cref pp);
//    return Z;
//}

////the marks denote which parts of X and Y are independent and need to be parallelized
//template <domain D , type T>
//D T [[2]] outerProduct(D T [[2]] X, D T [[2]] Y, uint [[1]] ms, uint[[1]] ns){
//    //assume size(ns) = size(ms)
//    D T [[2]] Z (shape(X)[0], sum(ms * ns));
//    uint [[1]] mm;
//    uint [[1]] nn (size(ms) * shape(X)[0]);
//    uint [[1]] pp;
//    mm = copyBlock(ms, shape(X)[0]);
//    nn = 1;
//    pp = copyBlock(ns, shape(X)[0]);
//    __syscall("shared3p::mat_mult_$T\_vec", __domainid(D), X, Y, Z, __cref mm, __cref nn, __cref pp);
//    return Z;
//}


//template <domain D , type T>
//D T [[2]] outerProductAndXor(D T [[2]] X, D T [[2]] Y){
//    D T [[2]] Z (shape(X)[1], shape(Y)[1]);
//    uint [[1]] dims (3);
//    dims[0] = shape(X)[1];
//    dims[1] = 1;
//    dims[2] = shape(Y)[1];
//    __syscall("shared3p::mat_multx_$T\_vec", __domainid(D), X, Y, Z, __cref dims);
//    return Z;
//}

