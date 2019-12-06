module prefixsums;

import stdlib;
import shared3p;
import rearrange;

//this module consists only of local operations!

template <domain D, type T>
D T [[1]] parScalarProdByConst(D T [[1]] in, T [[1]] coefficients){
    uint n = shape(in)[0];
    uint k = shape(coefficients)[0];
    D T [[1]] out (n - k + 1);
    __syscall("shared3p::parScalarProdByConst_$T\_vec", __domainid(D), in, out, __cref coefficients);
    return out;
}

template <domain D, type T>
D T [[2]] parScalarProdByConstPar(D T [[2]] in, T [[1]] coefficients){
    uint m = shape(in)[0];
    uint n = shape(in)[1];
    uint k = shape(coefficients)[0];
    D T [[1]] out_flat (m * n - k + 1);
    __syscall("shared3p::parScalarProdByConst_$T\_vec", __domainid(D), in, out_flat, __cref coefficients);
    uint [[1]] source (m * (n - k + 1));
    uint [[1]] target (m * (n - k + 1));
    D T [[2]] out (m, n - k + 1);
    for (uint i = 0; i < m; i++){
        for (uint j = 0; j < n - k + 1; j++){
            source[i * (n - k + 1) + j] = i * n + j;
            target[i * (n - k + 1) + j] = i * (n - k + 1) + j;
        }
    }
    out = partialRearrange(out_flat, out, source, target);
    return out;
}

template <domain D, type T>
D T [[1]] prefixSum(D T [[1]] a){
    __syscall("shared3p::prefixSum_$T\_vec", __domainid(D), a);
    return a;
}

template <type T>
T [[1]] prefixSum(T [[1]] a){
    T c = 0;
    for (uint i = 0; i < size(a); i++){
        c = c + a[i];
        a[i] = c;
    }
    return a;
}

template <domain D, type T, type S, dim N>
D T [[N]] prefixSum(D T [[N]] A, S [[1]] start, S [[1]] step, S [[1]] count){
    __syscall("shared3p::manyPrefixSum_$T\_vec", __domainid(D), A, __cref (uint32)start, __cref (uint32)step, __cref (uint32)count);
    return A;
}

template <domain D, type T>
D T [[2]] prefixSumPar(D T [[2]] A){
    uint m = shape(A)[0];
    uint n = shape(A)[1];

    uint32 [[1]] start = (uint32)iota(m) * (uint32)n;
    uint32 [[1]] step(m); step = 1;
    uint32 [[1]] count(m); count = (uint32)n;

    __syscall("shared3p::manyPrefixSum_$T\_vec", __domainid(D), A, __cref start, __cref step, __cref count);
    return A;
}

//start -- starting points for prefix sums
//count -- how many entries to add for every point
//step  -- the step of one count (1 if add sequentially)
template <domain D, type T>
D T [[1]] prefixSumPartial(D T [[1]] x, uint [[1]] indices){
    uint m = size(indices);
    uint32 [[1]] start = (uint32)indices;
    uint32 [[1]] step(m); step = 1;
    uint [[1]] count(m);
    for (uint i = 0; i < m - 1; i++){
        count[i] = indices[i] - indices[i-1];
    }
    count[m-1] = size(x) - indices[m-1];

    __syscall("shared3p::manyPrefixSum_$T\_vec", __domainid(D), x, __cref start, __cref step, __cref (uint32)count);
    return x;
}

template <domain D, type T, dim N>
D T [[N]] prefixSumPartial(D T [[N]] x, uint [[1]] indices, uint n){
    uint m = size(indices);
    uint32 [[1]] start = (uint32)indices;
    uint32 [[1]] step(m); step = 1;
    uint32 [[1]] count(m); count = (uint32)n;
    __syscall("shared3p::manyPrefixSum_$T\_vec", __domainid(D), x, __cref start, __cref step, __cref count);
    return x;
}

template <domain D, type T, dim N>
D T [[N]] prefixSumPartial(D T [[N]] x, uint [[1]] indices, uint steps, uint n){
    uint m = size(indices);
    uint32 [[1]] start           = (uint32)indices;
    uint32 [[1]] step(m);  step  = (uint32)steps;
    uint32 [[1]] count(m); count = (uint32)n;
    __syscall("shared3p::manyPrefixSum_$T\_vec", __domainid(D), x, __cref start, __cref step, __cref count);
    return x;
}

template <domain D, type T, dim N>
D T [[N]] prefixSumPartial(D T [[N]] x, uint [[1]] ms, uint steps, uint [[1]] ns){
    uint m = size(ms);
    uint32 [[1]] start           = (uint32)ms;
    uint32 [[1]] step(m);  step  = (uint32)steps;
    uint32 [[1]] count           = (uint32)ns;
    __syscall("shared3p::manyPrefixSum_$T\_vec", __domainid(D), x, __cref start, __cref step, __cref count);
    return x;
}

template <domain D, type T>
D T [[1]] invPrefixSum(D T [[1]] a){
    __syscall("shared3p::invPrefixSum_$T\_vec", __domainid(D), a);
    return a;
}

template <domain D, type T>
D T [[1]] invPrefixSum(D T [[1]] A, uint n){
    uint m = shape(A)[0] / n;

    uint32 [[1]] start = (uint32)iota(m) * (uint32)n;
    uint32 [[1]] step(m); step = 1;
    uint32 [[1]] count(m); count = (uint32)n;

    __syscall("shared3p::invManyPrefixSum_$T\_vec", __domainid(D), A, __cref start, __cref step, __cref count);
    return A;
}

template <domain D, type T>
D T [[2]] invPrefixSumPar(D T [[2]] A){
    uint m = shape(A)[0];
    uint n = shape(A)[1];

    uint32 [[1]] start = (uint32)iota(m) * (uint32)n;
    uint32 [[1]] step(m); step = 1;
    uint32 [[1]] count(m); count = (uint32)n;

    __syscall("shared3p::invManyPrefixSum_$T\_vec", __domainid(D), A, __cref start, __cref step, __cref count);
    return A;
}


template <domain D, type T, dim N >
D T [[1]] myRowSums(D T [[N]] A, uint m, uint n){
    uint [[1]] prefixSumIndices = iota(m) * n;
    uint [[1]] source = glist(1 :: uint, m + 1) * n - (1 :: uint);
    uint [[1]] target = iota(m);
    A = prefixSumPartial(A, prefixSumIndices, n);
    D T [[1]] b (m);
    b = partialRearrange(A, b, source, target);
    return b;
}

template <domain D, type T, dim N >
D T [[1]] myColSums(D T [[N]] A, uint m, uint n){
    uint [[1]] prefixSumIndices = iota(n);
    uint [[1]] source = glist((m-1)*n, m*n);
    uint [[1]] target = prefixSumIndices;
    A = prefixSumPartial(A, prefixSumIndices, n, m);
    D T [[1]] b (n);
    b = partialRearrange(A, b, source, target);
    return b;
}

template <domain D, type T >
D T [[1]] myRowSums(D T [[2]] A){
    return myRowSums(A, shape(A)[0], shape(A)[1]);
}

template <domain D, type T >
D T [[1]] myColSums(D T [[2]] A){
    return myColSums(A, shape(A)[0], shape(A)[1]);
}

template <domain D, type T>
D T [[2]] rowSumsPar (D T [[3]] matrices){

    uint m = shape(matrices)[0];
    uint n = shape(matrices)[1];
    uint l = shape(matrices)[2];
    D T [[2]] data = myReshape(matrices, m * n, l);
    D T [[1]] sums_flat = myRowSums(data);
    D T [[2]] sums  = myReshape(sums_flat, m, n);
    return sums;
}

template <domain D, type T>
D T [[2]] colSumsPar (D T [[3]] matrices){

    uint m = shape(matrices)[0];
    uint n = shape(matrices)[1];
    uint l = shape(matrices)[2];

    D T [[2]] data = myReshape(myTransposePar(matrices), n, m * l);
    D T [[1]] sums_flat = myColSums(data);
    D T [[2]] sums   = myReshape(sums_flat, m, l);
    return sums;
}
