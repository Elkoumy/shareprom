module aux;

import stdlib;
import shared3p;
import shared3p_random;

// TODO split the results back into modules

//this is missing from stdlib for some reason
template <domain D, type T, dim N >
D T [[N]] reshare(D T [[N]] x){
    return x;
}

//---- module sums.sc -- various sums

// turns [x1,x2,...,xn] to [x1, x1+x2, ... , x1+...+xn]
// defined for additive sharing only
template <domain D : shared3p, type T>
D T [[1]] prefixSum(D T [[1]] src) {
    if( size(src) <= 1 )
        return src;
    uint halfSize = size(src) / 2;
    uint [[1]] gatherEven = 2 * iota(halfSize);
    uint [[1]] gatherOdd = gatherEven + 1;
    D T [[1]] srcEven(halfSize), srcOdd(halfSize);
    __syscall("shared3p::gather_$T\_vec", __domainid(D), src, srcEven, __cref gatherEven);
    __syscall("shared3p::gather_$T\_vec", __domainid(D), src, srcOdd, __cref gatherOdd);
    D T [[1]] prefOdd = prefixSum(srcEven + srcOdd);
    D T [[1]] srcFstElem(1); srcFstElem[0] = src[0];
    D T [[1]] prefEven = myCat(srcFstElem, mySlice(prefOdd, (uint)0, halfSize - 1) + mySlice(srcEven, (uint)1, halfSize));
    D T [[1]] res(size(src));
    __syscall("shared3p::scatter_$T\_vec", __domainid(D), prefEven, res, __cref gatherEven);
    __syscall("shared3p::scatter_$T\_vec", __domainid(D), prefOdd, res, __cref gatherOdd);
    if(size(src) > 2 * halfSize)
        res[2 * halfSize] = prefOdd[halfSize - 1] + src[2 * halfSize];
    return res;
}

// turns each row of the input matrix [x1,x2,...,xn] to [x1, x1+x2, ... , x1+...+xn]
// defined for additive sharing only
template <domain D : shared3p, type T>
D T [[2]] prefixSumPar(D T [[2]] src) {
    uint m = shape(src)[0];
    uint n = shape(src)[1];
    if(n <= 1)
        return src;
    uint halfSize = n / 2;

    uint [[1]] gatherEven (m * halfSize);
    for (uint i = 0; i < m; i++){
        gatherEven[halfSize*i : halfSize*(i+1)] = n * i + 2 * iota(halfSize);
    }

    uint [[1]] gatherOdd = gatherEven + 1;
    D T [[2]] srcEven(m, halfSize), srcOdd(m, halfSize);
    __syscall("shared3p::gather_$T\_vec", __domainid(D), src, srcEven, __cref gatherEven);
    __syscall("shared3p::gather_$T\_vec", __domainid(D), src, srcOdd, __cref gatherOdd);
    D T [[2]] prefOdd = prefixSumPar(srcEven + srcOdd);

    D T [[2]] srcFstElem(m,1); srcFstElem = mySetSlice(mySlice(src,0,m,0,1), srcFstElem, 0,m,0,1);

    D T [[2]] prefEven = myCat(srcFstElem, mySlice(prefOdd, 0 :: uint, m, 0 :: uint, halfSize - 1) + mySlice(srcEven, 0 :: uint, m, 1 :: uint, halfSize), 1);
    D T [[2]] res(m,n);

    __syscall("shared3p::scatter_$T\_vec", __domainid(D), prefEven, res, __cref gatherEven);
    __syscall("shared3p::scatter_$T\_vec", __domainid(D), prefOdd,  res, __cref gatherOdd);
    if(n > 2 * halfSize)
        res = mySetSlice(mySlice(prefOdd,0,m,halfSize - 1,halfSize) + mySlice(src,0,m,2 * halfSize,2 * halfSize + 1), res, 0, m, 2 * halfSize, 2 * halfSize + 1);
    return res;
}

// turns [x1,x2,...,xn] to [x1, x1^x2, ... , x1^...^xn]
// defined for xor sharing only
template <domain D : shared3p, type T>
D T [[1]] prefixXOR(D T [[1]] src) {
    if( size(src) <= 1 )
        return src;
    uint halfSize = size(src) / 2;
    uint [[1]] gatherEven = 2 * iota(halfSize);
    uint [[1]] gatherOdd = gatherEven + 1;
    D T [[1]] srcEven(halfSize), srcOdd(halfSize);
    __syscall("shared3p::gather_$T\_vec", __domainid(D), src, srcEven, __cref gatherEven);
    __syscall("shared3p::gather_$T\_vec", __domainid(D), src, srcOdd, __cref gatherOdd);
    D T [[1]] prefOdd = prefixXOR(srcEven ^ srcOdd);
    D T [[1]] srcFstElem(1); srcFstElem[0] = src[0];
    D T [[1]] prefEven = myCat(srcFstElem, mySlice(prefOdd, (uint)0, halfSize - 1) ^ mySlice(srcEven, (uint)1, halfSize));
    D T [[1]] res(size(src));
    __syscall("shared3p::scatter_$T\_vec", __domainid(D), prefEven, res, __cref gatherEven);
    __syscall("shared3p::scatter_$T\_vec", __domainid(D), prefOdd, res, __cref gatherOdd);
    if(size(src) > 2 * halfSize)
        res[2 * halfSize] = prefOdd[halfSize - 1] ^ src[2 * halfSize];
    return res;
}
// turns each row of the input matrix [x1,x2,...,xn] to [x1, x1^x2, ... , x1^...^xn]
// defined for xor sharing only
template <domain D : shared3p, type T>
D T [[2]] prefixXORPar(D T [[2]] src) {
    uint m = shape(src)[0];
    uint n = shape(src)[1];
    if(n <= 1)
        return src;
    uint halfSize = n / 2;

    uint [[1]] gatherEven (m * halfSize);
    for (uint i = 0; i < m; i++){
        gatherEven[halfSize*i : halfSize*(i+1)] = n * i + 2 * iota(halfSize);
    }

    uint [[1]] gatherOdd = gatherEven + 1;
    D T [[2]] srcEven(m, halfSize), srcOdd(m, halfSize);
    __syscall("shared3p::gather_$T\_vec", __domainid(D), src, srcEven, __cref gatherEven);
    __syscall("shared3p::gather_$T\_vec", __domainid(D), src, srcOdd, __cref gatherOdd);
    D T [[2]] prefOdd = prefixXORPar(srcEven ^ srcOdd);

    D T [[2]] srcFstElem(m,1); srcFstElem = mySetSlice(mySlice(src,0,m,0,1), srcFstElem, 0,m,0,1);

    D T [[2]] prefEven = myCat(srcFstElem, mySlice(prefOdd, 0 :: uint, m, 0 :: uint, halfSize - 1) ^ mySlice(srcEven, 0 :: uint, m, 1 :: uint, halfSize), 1);
    D T [[2]] res(m,n);

    __syscall("shared3p::scatter_$T\_vec", __domainid(D), prefEven, res, __cref gatherEven);
    __syscall("shared3p::scatter_$T\_vec", __domainid(D), prefOdd,  res, __cref gatherOdd);
    if(n > 2 * halfSize)
        res = mySetSlice(mySlice(prefOdd,0,m,halfSize - 1,halfSize) ^ mySlice(src,0,m,2 * halfSize,2 * halfSize + 1), res, 0, m, 2 * halfSize, 2 * halfSize + 1);
    return res;
}

//finds the sum
template <domain D, type T>
D T [[1]] mySum(D T [[1]] A, uint [[1]] ks){
    D T [[1]] b (size(ks));
    __syscall("shared3p::sum_$T\_sub_vec", __domainid(D), A, b, __cref ks);
    return b;
}

//finds the sum of each row of matrix A
template <domain D : shared3p, type T>
D T[[1]] myRowSums (D T[[2]] A) {
    uint m = shape(A)[0];
    uint n = shape(A)[1];
    D T [[1]] b (m);
    uint [[1]] ks (m);
    ks = n;
    return mySum (myFlatten(A), ks);
}

//finds the sum of each column of matrix A
template <domain D : shared3p, type T>
D T[[1]] myColSums (D T[[2]] _A) {
    D T [[2]] A = myTranspose(_A);
    uint m = shape(A)[0];
    uint n = shape(A)[1];
    D T [[1]] b (m);
    uint [[1]] ks (m);
    ks = n;
    return mySum (myFlatten(A), ks);
}

//---- module stdlib_extended.sc -- basic rearrangement functions to be used everywhere

//equivalent of stdlib 'cat'
template <domain D: shared3p, type T>
D T[[1]] myCat(D T[[1]] X, D T[[1]] Y) {
    if (size(X) > 0 && size(Y) > 0){
        D T [[1]] Z (size(X) + size(Y));
        uint32 [[1]] indices_X (size(X));
        uint32 [[1]] indices_Y (size(Y));
        for (uint i = 0; i < size(X); i++){
            indices_X[i] = (uint32)i;
        }
        for (uint i = 0; i < size(Y); i++){
            indices_Y[i] = (uint32)i;
        }
        Z = partialRearrange(X,Z,indices_X,indices_X);
        Z = partialRearrange(Y,Z,indices_Y,indices_Y + (uint32)size(X));
        return Z;
    }else if (size(X) > 0){
        return X;
    }else{
        return Y;
    }
}

template <domain D: shared3p, type T>
D T[[2]] myCat(D T[[2]] X, D T[[2]] Y, int d) {
    if (size(X) > 0 && size(Y) > 0){
        D T [[2]] Z;
        uint offset;
        if (d == 0){
            offset = shape(X)[1];
            D T [[2]] Z_aux (shape(X)[0] + shape(Y)[0], shape(X)[1]);
            Z = Z_aux;
        }else{
            offset = shape(X)[1] + shape(Y)[1];
            D T [[2]] Z_aux (shape(X)[0], shape(X)[1] + shape(Y)[1]);
            Z = Z_aux;
        }
        uint32 [[1]] source_X (size(X));
        uint32 [[1]] source_Y (size(Y));
        uint32 [[1]] target_X (size(X));
        uint32 [[1]] target_Y (size(Y));
        for (uint i = 0; i < shape(X)[0]; i++){
           for (uint j = 0; j < shape(X)[1]; j++){
               source_X[i * shape(X)[1] + j] = (uint32)(i * shape(X)[1] + j);
               target_X[i * shape(X)[1] + j] = (uint32)(i * offset + j);
           }
        }
        for (uint i = 0; i < shape(Y)[0]; i++){
           for (uint j = 0; j < shape(Y)[1]; j++){
               source_Y[i * shape(Y)[1] + j] = (uint32)(i * shape(Y)[1] + j);
               target_Y[i * shape(Y)[1] + j] = (uint32)(i * offset + j);
           }
        }
        Z = partialRearrange(X,Z,source_X,target_X);
        uint32 [[1]] offset_Y (size(Y));
        if (d == 0){
            offset_Y = (uint32)size(X);
        }else{
            offset_Y = (uint32)shape(X)[1];
        }
        Z = partialRearrange(Y,Z,source_Y,target_Y + offset_Y);
        return Z;
    }else if (size(X) > 0){
        return X;
    }else{
        return Y;
    }
}

template <domain D: shared3p, type T>
D T[[3]] myCat(D T[[3]] X, D T[[3]] Y, int d) {
    if (size(X) > 0 && size(Y) > 0){
        D T [[3]] Z;
        uint offset;
        uint offset_i;
        uint offset_j;
        if (d == 0){
            offset_i = shape(X)[1];
            offset_j = shape(X)[2];
            D T [[3]] Z_aux (shape(X)[0] + shape(Y)[0], shape(X)[1], shape(X)[2]);
            Z = Z_aux;
        }else if (d == 1){
            offset_i = shape(X)[1] + shape(Y)[1];
            offset_j = shape(X)[2];
            D T [[3]] Z_aux (shape(X)[0], shape(X)[1] + shape(Y)[1], shape(X)[2]);
            Z = Z_aux;
        }else{
            offset_i = shape(X)[1];
            offset_j = shape(X)[2] + shape(Y)[2];
            D T [[3]] Z_aux (shape(X)[0], shape(X)[1], shape(X)[2] + shape(Y)[2]);
            Z = Z_aux;
        }
        uint32 [[1]] source_X (size(X));
        uint32 [[1]] source_Y (size(Y));
        uint32 [[1]] target_X (size(X));
        uint32 [[1]] target_Y (size(Y));
        for (uint i = 0; i < shape(X)[0]; i++){
           for (uint j = 0; j < shape(X)[1]; j++){
             for (uint k = 0; k < shape(X)[2]; k++){
               source_X[i * shape(X)[1] * shape(X)[2] + j * shape(X)[2] + k] = (uint32)(i * shape(X)[1] * shape(X)[2] + j * shape(X)[2] + k);
               target_X[i * shape(X)[1] * shape(X)[2] + j * shape(X)[2] + k] = (uint32)(i * offset_i * offset_j + j * offset_j + k);
             }
           }
        }
        for (uint i = 0; i < shape(Y)[0]; i++){
           for (uint j = 0; j < shape(Y)[1]; j++){
             for (uint k = 0; k < shape(Y)[2]; k++){
               source_Y[i * shape(Y)[1] * shape(Y)[2] + j * shape(Y)[2] + k] = (uint32)(i * shape(Y)[1] * shape(Y)[2] + j * shape(Y)[2] + k);
               target_Y[i * shape(Y)[1] * shape(Y)[2] + j * shape(Y)[2] + k] = (uint32)(i * offset_i * offset_j + j * offset_j + k);
             }
           }
        }
        Z = partialRearrange(X, Z, source_X, target_X);
        uint32 [[1]] offset_Y (size(Y));
        if (d == 0){
            offset_Y = (uint32)(shape(X)[0]*shape(X)[1]*shape(X)[2]);
        }else if (d == 1){
            offset_Y = (uint32)(shape(X)[1]*shape(X)[2]);
        }else{
            offset_Y = (uint32)shape(X)[2];
        }
        Z = partialRearrange(Y,Z,source_Y,target_Y + offset_Y);
        return Z;
    }else if (size(X) > 0){
        return X;
    }else{
        return Y;
    }
}

uint [[1]] _slice_indices(uint m, uint n, uint lb1, uint ub1, uint lb2, uint ub2, uint lb3, uint ub3) {
    assert(lb2 <= m);
    assert(lb3 <= n);
    assert(ub2 <= m);
    assert(ub3 <= n);
    uint [[3]] indices = reshape(iota(ub1 * m * n), ub1, m, n);
    return flatten(indices[lb1 : ub1, lb2 : ub2, lb3 : ub3]);
}

uint [[1]] _slice_indices(uint m, uint lb1, uint ub1, uint lb2, uint ub2) {
    assert(lb2 <= m);
    assert(ub2 <= m);
    uint [[2]] indices = reshape(iota(ub1 * m), ub1, m);
    return flatten(indices[lb1 : ub1, lb2 : ub2]);
}

//equivalent of slicings ... = X[lb1:ub1, lb2:ub2, lb3:ub3]
template<domain D, type T, type T1, type T2, type T3, type T4, type T5, type T6 >
D T[[3]] mySlice(D T[[3]] X, T1 _lb1, T2 _ub1, T3 _lb2, T4 _ub2, T5 _lb3, T6 _ub3) {

    uint lb1 = (uint)(_lb1); uint ub1 = (uint)(_ub1);
    uint lb2 = (uint)(_lb2); uint ub2 = (uint)(_ub2);
    uint lb3 = (uint)(_lb3); uint ub3 = (uint)(_ub3);

    D T[[3]] Y (ub1 -  lb1, ub2 - lb2, ub3 - lb3);
    uint [[1]] source = _slice_indices(shape(X)[1], shape(X)[2], lb1, ub1, lb2, ub2, lb3, ub3);
    uint [[1]] target = iota(size(Y));
    Y = partialRearrange(X,Y,source,target);
    return Y;
}

template<domain D, type T, type T1, type T2, type T3, type T4 >
D T[[2]] mySlice(D T[[2]] X, T1 _lb1, T2 _ub1, T3 _lb2, T4 _ub2) {

    uint lb1 = (uint)(_lb1); uint ub1 = (uint)(_ub1);
    uint lb2 = (uint)(_lb2); uint ub2 = (uint)(_ub2);

    D T[[2]] Y (ub1 -  lb1, ub2 - lb2);
    uint [[1]] source = _slice_indices(shape(X)[1], lb1, ub1, lb2, ub2);
    uint [[1]] target = iota(size(Y));
    Y = partialRearrange(X,Y,source,target);
    return Y;
}

template<domain D, type T, type T1, type T2 >
D T[[1]] mySlice(D T[[1]] X, T1 _lb, T2 _ub) {

    uint lb = (uint)(_lb); uint ub = (uint)(_ub);

    D T[[1]] Y (ub - lb);
    uint [[1]] source = iota(ub - lb) + lb;
    uint [[1]] target = iota(size(Y));
    Y = partialRearrange(X,Y,source,target);
    return Y;
}

//equivalent of slicing assignments X[lb1:ub1, lb2:ub2, lb3:ub3] = .....
template<domain D, type T, type T1, type T2, type T3, type T4, type T5, type T6 >
D T[[3]] mySetSlice(D T[[3]] X, D T[[3]] Y, T1 _lb1, T2 _ub1, T3 _lb2, T4 _ub2, T5 _lb3, T6 _ub3) {

    uint lb1 = (uint)(_lb1); uint ub1 = (uint)(_ub1);
    uint lb2 = (uint)(_lb2); uint ub2 = (uint)(_ub2);
    uint lb3 = (uint)(_lb3); uint ub3 = (uint)(_ub3);

    uint [[1]] source = iota(size(X));
    uint [[1]] target = _slice_indices(shape(Y)[1], shape(Y)[2], lb1, ub1, lb2, ub2, lb3, ub3);
    Y = partialRearrange(X,Y,source,target);
    return Y;
}

template<domain D, type T, type T1, type T2, type T3, type T4 >
D T[[2]] mySetSlice(D T[[2]] X, D T[[2]] Y, T1 _lb1, T2 _ub1, T3 _lb2, T4 _ub2) {

    uint lb1 = (uint)(_lb1); uint ub1 = (uint)(_ub1);
    uint lb2 = (uint)(_lb2); uint ub2 = (uint)(_ub2);

    uint [[1]] source = iota(size(X));
    uint [[1]] target = _slice_indices(shape(Y)[1], lb1, ub1, lb2, ub2);
    Y = partialRearrange(X,Y,source,target);
    return Y;
}

template<domain D, type T, type T1, type T2 >
D T[[1]] mySetSlice(D T[[1]] X, D T[[1]] Y, T1 _lb, T2 _ub) {

    uint lb = (uint)(_lb); uint ub = (uint)(_ub);

    uint [[1]] source = iota(size(X));
    uint [[1]] target = iota(ub - lb) + lb;
    Y = partialRearrange(X,Y,source,target);
    return Y;
}

//equivalent of .... = X[i,:,:]; .... = X[:,i,:]; .... = X[:,:,i]
// projects to the coordinate i of dimension k
template<domain D, type T, type T1, type T2 >
D T[[2]] myGetProj(D T[[3]] X, T1 _i, T2 _k) {

    uint i = (uint)_i;
    uint k = (uint)_k;
    assert(k < 3);

    uint lb1 = 0; uint ub1 = shape(X)[0];
    uint lb2 = 0; uint ub2 = shape(X)[1];
    uint lb3 = 0; uint ub3 = shape(X)[2];
    uint m = 0;
    uint n = 0;

    if (k == 0) {
        m = (ub2 - lb2);
        n = (ub3 - lb3);
        lb1 = i; ub1 = i + 1;
    } else if (k == 1) {
        m = (ub1 - lb1);
        n = (ub3 - lb3);
        lb2 = i; ub2 = i + 1;
    } else {
        m = (ub1 - lb1);
        n = (ub2 - lb2);
        lb3 = i; ub3 = i + 1;
    }
    D T [[3]] Z = mySlice(X, lb1, ub1, lb2, ub2, lb3, ub3);
    D T [[2]] Y = myReshape(Z,m,n);
    return Y;
}

//equivalent of .... = X[i,:] and .... = X[:,i]
// projects to the coordinate i of dimension k
template<domain D, type T, type T1, type T2 >
D T[[1]] myGetProj(D T[[2]] X, T1 _i, T2 _k) {

    uint i = (uint)_i;
    uint k = (uint)_k;
    assert(k < 2);

    uint lb1 = 0; uint ub1 = shape(X)[0];
    uint lb2 = 0; uint ub2 = shape(X)[1];
    uint m = 0;

    if (k == 0) {
        m = (ub2 - lb2);
        lb1 = i; ub1 = i + 1;
    } else {
        m = (ub1 - lb1);
        lb2 = i; ub2 = i + 1;
    }

    D T [[2]] Z = mySlice(X, lb1, ub1, lb2, ub2);
    D T [[1]] Y = myReshape(Z,m);
    return Y;
}


//equivalent of assignments X[i,:,:] = ....; X[;,i,:] = ....; X[:,i,:] = ....;
//updates the coordinate i of dimension k
template<domain D, dim N, type T, type T1, type T2 >
D T[[3]] mySetProj(D T[[N]] X, D T [[3]] Y, T1 _i, T2 _k) {

    uint i = (uint)_i;
    uint k = (uint)_k;
    assert(k < 3);

    uint lb1 = 0; uint ub1 = shape(Y)[0];
    uint lb2 = 0; uint ub2 = shape(Y)[1];
    uint lb3 = 0; uint ub3 = shape(Y)[2];
    uint m = 0;
    uint n = 0;
    uint o = 0;

    if (k == 0) {
        m = 1;
        n = (ub2 - lb2);
        o = (ub3 - lb3);
        lb1 = i; ub1 = i + 1;
    } else if (k == 1) {
        m = (ub1 - lb1);
        n = 1;
        o = (ub3 - lb3);
        lb2 = i; ub2 = i + 1;
    } else {
        m = (ub1 - lb1);
        n = (ub2 - lb2);
        o = 1;
        lb3 = i; ub3 = i + 1;
    }
    D T [[3]] Z = myReshape(X,m,n,o);
    Y = mySetSlice(Z, Y, lb1, ub1, lb2, ub2, lb3, ub3);
    return Y;
}

//equivalent of assignments X[i,:] = ....; X[:,i] = ....
template<domain D, dim N, type T, type T1, type T2 >
D T[[2]] mySetProj(D T[[N]] X, D T [[2]] Y, T1 _i, T2 _k) {

    uint i = (uint)_i;
    uint k = (uint)_k;
    assert(k < 2);

    uint lb1 = 0; uint ub1 = shape(X)[0];
    uint lb2 = 0; uint ub2 = shape(X)[1];
    uint m = 0;
    uint n = 0;

    if (k == 0) {
        m = (ub2 - lb2);
        n = 1;
        lb1 = i; ub1 = i + 1;
    } else {
        m = 1;
        n = (ub1 - lb1);
        lb2 = i; ub2 = i + 1;
    }

    D T [[2]] Z = myReshape(X,m,n);
    Y = mySetSlice(Z, Y, lb1, ub1, lb2, ub2);
    return Y;
}


//equivalent of stdlib 'reshape'
template <domain D, type T, type T1, dim N>
D T[[1]] myReshape (D T[[N]] X, T1 _m){
    uint m = (uint)(_m);
    assert(size(X) == m);
    return partialReshape(X, m);
}

template <domain D, type T, type T1, type T2, dim N>
D T[[2]] myReshape (D T[[N]] X, T1 _m, T2 _n){
    uint m = (uint)(_m); uint n = (uint)(_n);
    assert(size(X) == m * n);
    return partialReshape(X, m, n);
}

template <domain D, type T, type T1, type T2, type T3, dim N>
D T[[3]] myReshape (D T[[N]] X, T1 _m, T2 _n, T3 _o){
    uint m = (uint)(_m); uint n = (uint)(_n); uint o = (uint)(_o);
    assert(size(X) == m * n * o);
    return partialReshape(X, m, n, o);
}

template <domain D, type T, type T1, type T2, type T3, type T4, dim N>
D T[[4]] myReshape (D T[[N]] X, T1 _m, T2 _n, T3 _o, T4 _p){
    uint m = (uint)(_m); uint n = (uint)(_n); uint o = (uint)(_o); uint p = (uint)(_p);
    assert(size(X) == m * n * o * p);
    return partialReshape(X, m, n, o, p);
}

//a weaker variant of stdlib 'reshape', which allows non-exact fitting
template <domain D, type T, type T1, dim N>
D T[[1]] partialReshape (D T[[N]] X, T1 _m){
    uint m = (uint)(_m);
    D T[[1]] Y (m);
    Y = partialRearrange(X,Y);
    return Y;
}

template <domain D, type T, type T1, type T2, dim N>
D T[[2]] partialReshape (D T[[N]] X, T1 _m, T2 _n){
    uint m = (uint)(_m); uint n = (uint)(_n);
    D T[[2]] Y (m,n);
    Y = partialRearrange(X,Y);
    return Y;
}

template <domain D, type T, type T1, type T2, type T3, dim N>
D T[[3]] partialReshape (D T[[N]] X, T1 _m, T2 _n, T3 _o){
    uint m = (uint)(_m); uint n = (uint)(_n); uint o = (uint)(_o);
    D T[[3]] Y (m,n,o);
    Y = partialRearrange(X,Y);
    return Y;
}

template <domain D, type T, type T1, type T2, type T3, type T4, dim N>
D T[[4]] partialReshape (D T[[N]] X, T1 _m, T2 _n, T3 _o, T4 _p){
    uint m = (uint)(_m); uint n = (uint)(_n); uint o = (uint)(_o); uint p = (uint)(_p);
    D T[[4]] Y (m,n,o,p);
    Y = partialRearrange(X,Y);
    return Y;
}

//equivalent of stdlib 'flatten'
template <domain D : shared3p, type T, dim N >
D T[[1]] myFlatten (D T[[N]] X){
    D T[[1]] Y (size(X));
    Y = partialRearrange(X,Y);
    return Y;
}

template <domain D : shared3p, type T, dim N >
D T[[2]] myFlattenPar (D T[[N]] X){
    uint m = shape(X)[0];
    D T[[2]] Y (m, size(X) / m);
    Y = partialRearrange(X,Y);
    return Y;
}

//reverse an array
template <type T>
T[[1]] myReverse (T[[1]] X){
    uint m = size(X);
    T [[1]] Y (m);
    for (uint i = 0; i < m; i++){
        Y[m - i - 1] = X[i];
    }
    return Y;
}

template <domain D, type T>
D T[[1]] myReverse (D T[[1]] X){
    uint [[1]] indices = iota(size(X));
    D T [[1]] Y = partialRearrange(X,myReverse(indices));
    return Y;
}

//reverse several arrays in parallel
template <type T>
T[[2]] myReversePar (T[[2]] X){
    uint m = shape(X)[0];
    uint n = shape(X)[1];
    uint [[2]] Y (m, n);
    for (uint i = 0; i < m; i++){
        Y[i,:] = myReverse(X[i,:]);
    }
    return Y;
}

template <domain D, type T>
D T[[2]] myReversePar (D T[[2]] X){
    uint m = shape(X)[0];
    uint n = shape(X)[1];
    uint [[1]] source = iota(m * n); 
    uint [[1]] target = flatten(myReversePar(reshape(source,m,n)));
    D T [[2]] Y (m, n);
    Y = partialRearrange(X,Y,source,target);
    return Y;
}

//the core rearrangement function
template <domain D, type T, type S, dim M, dim N>
D T [[N]] partialRearrange(D T [[M]] a, D T [[N]] b, S [[1]] source, S [[1]] target){
    assert(size(source) == size(target));
    D T [[1]] temp (size(source));
    __syscall("shared3p::gather_$T\_vec",  __domainid(D), a, temp, __cref (uint)source);
    __syscall("shared3p::scatter_$T\_vec", __domainid(D), temp, b, __cref (uint)target);
    return b;
}

template <domain D, type T, dim M, dim N>
D T [[N]] partialRearrange(D T [[M]] a, D T [[N]] b){
    assert(size(b) >= size(a));
    uint [[1]] source = iota(size(a));
    uint [[1]] target = iota(size(a));
    b = partialRearrange(a, b, source, target);
    return b;
}


//replicate
template <type T, type T0, dim N>
T [[N]] myReplicate(T [[N]] a, T0 _n){
    uint n = (uint)(_n);
    uint [[1]] ms = {shape(a)[0]};
    uint [[1]] ns = {n};
    return myReplicate(a, ms, ns);
}

template <domain D, type T, type T0, dim N>
D T [[N]] myReplicate(D T [[N]] a, T0 _n){
    uint n = (uint)(_n);
    uint [[1]] ms = {shape(a)[0]};
    uint [[1]] ns = {n};
    return myReplicate(a, ms, ns);
}

//ms - differences between starting points (steps)
//ns - times to replicate
template <domain D, type T>
D T [[1]] myReplicate(D T [[1]] a, uint [[1]] ms, uint [[1]] ns){

    assert(size(ms) == size(ns));

    uint [[1]] indices = iota(size(a));
    uint [[1]] source = myReplicate(indices, ms, ns);
    uint [[1]] target = iota(size(source));
    D T [[1]] b (size(source));
    b = partialRearrange(a, b, source, target);
    return b;
}

template <domain D, type T>
D T [[1]] myReplicate(D T [[1]] a, uint [[1]] ns){

    assert(size(a) == size(ns));
    uint [[1]] ms (size(ns));
    ms = 1;

    uint [[1]] source = myReplicate(iota(size(a)), ms, ns);
    uint [[1]] target = iota(size(source));
    D T [[1]] b (size(source));
    b = partialRearrange(a, b, source, target);
    return b;
}

template <domain D, type T>
D T [[2]] myReplicate(D T [[2]] a, uint [[1]] ms, uint [[1]] ns){

    assert(size(ms) == size(ns));

    uint [[1]] source = widen_indices(myReplicate(iota(shape(a)[0]), ms, ns), shape(a)[1]);
    uint [[1]] target = iota(size(source));
    D T [[2]] b (sum(ms * ns), shape(a)[1]);
    b = partialRearrange(a, b, source, target);
    return b;
}

template <type T>
T [[1]] myReplicate(T [[1]] a, uint [[1]] ms, uint [[1]] ns){

    assert(size(ms) == size(ns));

    T [[1]] b (sum(ms * ns));
    uint t = 0;
    uint s = 0;

    for (uint k = 0; k < size(ms); k++){
        uint m = ms[k];
        uint n = ns[k];
        for (uint i = 0; i < m; i++){
           for (uint j = 0; j < n; j++){
               b[t] = a[s + i];
               t = t + 1;
           }
       }
       s = s + m;
    }
    return b;
}

//equivalent of matrix.sc 'transpose'
template <domain D, type T>
D T[[2]] myTranspose(D T[[2]] X) {
    uint[[1]] s = shape(X);

    uint [[1]] source (size(X));
    uint [[1]] target = iota(size(X));

    D T[[2]] Y(s[1], s[0]);

    for(uint i = 0; i < s[1]; ++i) {
      for(uint j = 0; j < s[0]; ++j) {
          source[j + i * s[0]] = j * s[1] + i;
      }
    }
    Y = partialRearrange(X,Y,source,target);
    return Y;
}

//this swaps the 2nd and the 3rd coordinates (transposes in parallel)
template <domain D, type T>
D T[[3]] myTransposePar(D T[[3]] X) {
    uint [[1]] s = shape(X);

    uint [[1]] source (size(X));
    uint [[1]] target = iota(size(X));

    D T[[3]] Y (s[0], s[2], s[1]);

    for(uint i = 0; i < s[0]; ++i) {
        for(uint j = 0; j < s[1]; ++j) {
            for(uint k = 0; k < s[2]; ++k) {
                source[k * s[1] + j + i * s[1] * s[2]] = k + j * s[2] + i * s[1] * s[2];
            }
        }
    }
    Y = partialRearrange(X,Y,source,target);
    return Y;
}

//---- module sort_extended.sc -- improved sorting functions

template <domain D, type T, type S>
D S [[1]] _countIndices (D T [[1]] key) {

    D S[[1]] sortCol = (S) key;
    D S[[1]] isortCol = 1 - sortCol;

    uint n = shape(key)[0];
    {
        D S[[1]] pTrue = prefixSum(isortCol) - isortCol;
        isortCol *= pTrue;
    }

    {
        D S[[1]] reverseSortCol = -myReverse(sortCol);
        D S[[1]] pFalse = myReverse(prefixSum(reverseSortCol) - reverseSortCol) + (S)(n - 1);
        sortCol *= pFalse;
    }
    return (sortCol + isortCol);
}

template <domain D, type T, type S>
D S[[2]] _countIndicesPar(D T [[2]] key) {

    D S[[2]] sortCol = (S) key;
    D S[[2]] isortCol = 1 - sortCol;

    uint m = shape(key)[0];
    uint n = shape(key)[1];
    {
        D S[[2]] pTrue = prefixSumPar(isortCol) - isortCol;
        isortCol *= pTrue;
    }

    {
        D S[[2]] reverseSortCol = -myReversePar(sortCol);
        D S[[2]] pFalse = myReversePar(prefixSumPar(reverseSortCol) - reverseSortCol) + (S)(n - 1);
        sortCol *= pFalse;
    }

    return sortCol + isortCol;
}

// key-value count sort
//ASSUMPTION: the entries in the 'key' are all either 0 or 1
// TODO: ignoring the assumpton causes "syscall error" and therefore leaks the fact that the element is not 0 or 1
template <domain D, type T, type U>
D T[[1]] countSort(D T[[1]] value, D U [[1]] key) {

    assert(4294967296 >= size(key));

    uint n = shape(value)[0];

    D uint32[[1]] indices = _countIndices(key);

    D uint8 [[1]] shuffle_key (32);
    shuffle_key = randomize(shuffle_key);

    indices = shuffle(indices, shuffle_key);
    value   = shuffle(value,   shuffle_key);

    uint[[1]] publishedIndices = (uint)declassify(indices);
    value                      = unapplyPublicPermutation(value, publishedIndices);

    return value;
}

// key-value count sort permutation
//ASSUMPTION: the entries in the 'key' are all either 0 or 1
template <domain D, type T>
D uint32 [[1]] countSortPermutation(D T [[1]] key) {

    assert(4294967296 >= size(key));

    uint n = shape(key)[0];

    //we start from the identity permutation
    uint32 [[1]] ident = (uint32)iota(n);
    D uint32 [[1]] pi = ident;

    D uint32 [[1]] indices = _countIndices(key);

    D uint8 [[1]] shuffle_key (32);
    shuffle_key = randomize(shuffle_key);

    indices = shuffle(indices, shuffle_key);
    pi      = shuffle(pi,   shuffle_key);

    uint[[1]] publishedIndices = (uint)declassify(indices);
    pi                         = unapplyPublicPermutation(pi, publishedIndices);

    return pi;
}

// key-value count sort for a matrix
//ASSUMPTION: the entries in the 'key' are all either 0 or 1
template <domain D, type T, type U>
D T[[2]] countSort(D T[[2]] value, D U [[1]] key) {

    assert(4294967296 >= size(key));

    uint n = shape(value)[0];
    uint l = shape(value)[1];

    D uint32[[1]] indices = _countIndices(key);

    D uint8 [[1]] shuffle_key (32);
    shuffle_key = randomize(shuffle_key);

    indices = shuffle(indices, shuffle_key);
    value   = shufflePar(value,   shuffle_key);

    uint[[1]] publishedIndices = (uint)declassify(indices);
    value                      = unapplyPublicPermutationPar(value, publishedIndices);

    return value;
}

// count sort
//ASSUMPTION: the entries in the 'key' are all either 0 or 1
template <domain D, type T>
D T [[1]] countSort(D T [[1]] value) {
    return countSort(value, value);
}

// count sort for a matrix
//ASSUMPTION: the entries in the 'key' are all either 0 or 1
template <domain D, type T>
D T[[2]] countSort(D T[[2]] matrix, uint column) {
    uint m = shape(matrix)[0];
    return countSort(matrix, myGetProj(matrix, column, 1));
}

// key-value parallel count sort
//ASSUMPTION: the entries in the 'key' are all either 0 or 1
template <domain D, type T, type U>
D T[[2]] countSortPar(D T[[2]] value, D U [[2]] key) {

    assert(4294967296 >= shape(key)[1]);

    uint m = shape(value)[0];
    uint n = shape(value)[1];

    D U [[2]] indices = _countIndicesPar(key);

    D uint8 [[1]] shuffle_key (32);
    shuffle_key = randomize(shuffle_key);


    indices = shufflePar(indices, shuffle_key);
    value   = shufflePar(value,   shuffle_key);

    uint [[2]] publishedIndices = (uint)declassify(indices);
    value                       = unapplyPublicPermutationPar(value, publishedIndices);

    return value;
}

// key-value parallel count sort permutation
//ASSUMPTION: the entries in the 'key' are all either 0 or 1
template <domain D, type T>
D uint32[[2]] countSortPermutationPar(D T [[2]] key) {

    assert(4294967296 >= shape(key)[1]);

    uint m = shape(key)[0];
    uint n = shape(key)[1];

    //we start from the identity permutation
    uint32 [[1]] ident = (uint32)iota(n);
    uint32 [[2]] pub_pi (m, n);
    for (uint i = 0; i < m; i++){
        pub_pi[i,:] = ident;
    }
    D uint32 [[2]] pi = pub_pi;

    D uint32 [[2]] indices = _countIndicesPar(key);

    D uint8 [[1]] shuffle_key (32);
    shuffle_key = randomize(shuffle_key);

    indices = shufflePar(indices, shuffle_key);
    pi      = shufflePar(pi,   shuffle_key);

    uint[[2]] publishedIndices = (uint)declassify(indices);
    pi                         = unapplyPublicPermutationPar(pi, publishedIndices);

    return pi;
}

// key-value parallel count sort for matrices
//ASSUMPTION: the entries in the 'key' are all either 0 or 1
template <domain D, type T, type U>
D T[[3]] countSortPar(D T[[3]] value, D U [[2]] key) {

    assert(4294967296 >= shape(key)[1]);

    uint m = shape(value)[0];
    uint n = shape(value)[1];
    uint l = shape(value)[2];

    D uint32[[2]] indices = _countIndicesPar(key);

    D uint8 [[1]] shuffle_key (32);
    shuffle_key = randomize(shuffle_key);

    indices = shufflePar(indices, shuffle_key);
    value   = shuffleRowsPar(value,   shuffle_key);

    uint[[2]] publishedIndices = (uint)declassify(indices);
    value                      = unapplyPublicPermutationRowsPar(value, publishedIndices);

    return value;
}

// parallel count sort
//ASSUMPTION: the entries in the 'key' are all either 0 or 1
template <domain D, type T>
D T [[2]] countSortPar(D T [[2]] value) {
    return countSortPar(value, value);
}

// parallel count sort for matrices
//ASSUMPTION: the entries in the 'key' are all either 0 or 1
template <domain D, type T>
D T[[3]] countSortPar(D T[[3]] matrix, uint column) {
    uint m = shape(matrix)[0];
    uint n = shape(matrix)[1];
    return countSortPar(matrix, myGetProj(matrix, column, 2));
}

// apply radix sort to the bits b_i for i in is (starting from the first)
template <domain D, type T>
D uint32 [[1]] boundedRadixSortPermutation(D T [[1]] key, uint [[1]] is) {

    //bit decomposition
    uint n = shape(key)[0];
    D bool [[1]] allBits = bit_extract_all_types(key);
    D bool [[2]] keyBits = myReshape(allBits, n, size(allBits) / n);

    //we start from the identity permutation
    uint32 [[1]] ident = (uint32)iota(n);
    D uint32 [[1]] pi = ident;

    //now sort sequentially k times by each bit
    for (uint k = 0; k < size(is); k++){
        uint i = is[k];
        D bool [[1]] bits = applyPrivatePermutation(myGetProj(keyBits, i, 1), pi);
        D uint32 [[1]] indices = _countIndices(bits);

        D uint8 [[1]] shuffle_key (32);
        shuffle_key = randomize(shuffle_key);

        indices = shuffle(indices, shuffle_key);
        pi      = shuffle(pi,   shuffle_key);

        uint[[1]] publishedIndices = (uint)declassify(indices);
        pi                         = unapplyPublicPermutation(pi, publishedIndices);

    }
    return pi;
}

// apply radix sort to the bits b_i for i in is (starting from the first) in parallel to each key matrix row
template <domain D, type T>
D uint32 [[2]] boundedRadixSortPermutationPar(D T[[2]] key, uint [[1]] is) {

    uint m = shape(key)[0];
    uint n = shape(key)[1];

    //we start from the identity permutation
    uint32 [[1]] ident = (uint32)iota(n);
    uint32 [[2]] pub_pi (m, n);
    for (uint i = 0; i < m; i++){
        pub_pi[i,:] = ident;
    }
    D uint32 [[2]] pi = pub_pi;

    if (m == 0 || n == 0) return pi;

    D bool [[1]] allBits = bit_extract_all_types(key);
    D bool [[3]] keyBits = myReshape(allBits, m, n, size(allBits) / m / n);

    //now sort sequentially k times by each bit
    for (uint k = 0; k < size(is); k++){
        uint i = is[k];
        D bool [[2]] bits = applyPrivatePermutationPar(myGetProj(keyBits, i, 2), pi);
        D uint32 [[2]] indices = _countIndicesPar(bits);

        D uint8 [[1]] shuffle_key (32);
        shuffle_key = randomize(shuffle_key);

        indices = shufflePar(indices, shuffle_key);
        pi      = shufflePar(pi,   shuffle_key);

        uint[[2]] publishedIndices = (uint)declassify(indices);
        pi                         = unapplyPublicPermutationPar(pi, publishedIndices);

    }
    return pi;
}

//if only one index k is provided, take up to k LSB bits
template <domain D, type T, type U, dim N>
D T [[N]] boundedRadixSort(D T [[N]] value, D U [[1]] key, uint k) {
    uint [[1]] is = iota(k);
    D uint32 [[1]] pi = boundedRadixSortPermutation(key, is);
    return applyPrivatePermutationRows(value, pi);
}

template <domain D, type T, type U, dim N>
D T[[N]] boundedRadixSortPar(D T[[N]] value, D U [[2]] key, uint k) {
    uint [[1]] is = iota(k);
    D uint32 [[2]] pi = boundedRadixSortPermutationPar(key, is);
    return applyPrivatePermutationPar(value, pi);
}

//if only the values are given, then they are also the keys
template <domain D, type T>
D T [[1]] boundedRadixSort(D T [[1]] value, uint k) {
    return boundedRadixSort(value, value, k);
}

template <domain D, type T>
D T [[2]] boundedRadixSortPar(D T [[2]] value, uint k) {
    return boundedRadixSortPar(value, value, k);
}

//if no keys are given for a matrix, then the column index of the key is given instead
template <domain D, type T>
D T[[2]] boundedRadixSort(D T[[2]] matrix, uint column, uint k) {
    uint m = shape(matrix)[0];
    return boundedRadixSort(matrix, myGetProj(matrix, column, 1), k);
}

template <domain D, type T>
D T[[3]] boundedRadixSortPar(D T[[3]] matrix, uint column, uint k) {
    uint m = shape(matrix)[0];
    uint n = shape(matrix)[1];
    return boundedRadixSortPar(matrix, myGetProj(matrix, column, 2), k);
}

//----------sorting network sorts

bool isPowerOfTwo (uint x) {
    return (x != 0 && (x & (x - 1)) == 0);
}

uint[[1]] generateSortingNetwork(uint arraysize) {
    uint snsize = 0;
    __syscall("SortingNetwork_serializedSize", arraysize, __return snsize);
    uint[[1]] sn (snsize);
    __syscall("SortingNetwork_serialize", arraysize, __ref sn);
    return sn;
}

uint[[1]] generateTopKSortingNetwork (uint n, uint k) {
    assert(isPowerOfTwo (n));
    assert(k <= n);
    assert(n > 0);

    uint snsize = 0;
    __syscall ("TopKSortingNetwork_serializedSize", n, k, __return snsize);
    uint[[1]] sn(snsize);
    __syscall ("TopKSortingNetwork_serialize", n, k, __ref sn);
    return sn;
}

template <domain D, type T>
D T[[2]] sortingNetworkSortPar (D T[[2]] arrays) {
    uint n = shape(arrays)[0];
    if (shape(arrays)[1] <= 1)
        return arrays;

    // Generate sorting network
    uint[[1]] sortnet = generateSortingNetwork (shape(arrays)[1]);

    // We will use this offset to decode the sorting network
    uint offset = 0;

    // Extract the number of stages
    uint numOfStages = sortnet[offset++];

    for (uint stage = 0; stage < numOfStages; stage++) {

        uint sizeOfStage = sortnet[offset++];

        D T[[2]] firstVector (n,sizeOfStage);
        D T[[2]] secondVector (n,sizeOfStage);
        D bool[[2]] exchangeFlagsVector (n,sizeOfStage);

        firstVector = applyPublicPermutationCols(arrays,sortnet[offset : offset + sizeOfStage]);
        offset = offset + sizeOfStage;

        secondVector = applyPublicPermutationCols(arrays,sortnet[offset : offset + sizeOfStage]);
        offset = offset + sizeOfStage;

        // Perform compares
        exchangeFlagsVector = firstVector <= secondVector;
        D bool[[2]] expandedExchangeFlagsVector (n,2 * sizeOfStage);
        uint counter = 0;

        uint [[1]] indices = iota(shape(exchangeFlagsVector)[1])[counter : counter + sizeOfStage];
        expandedExchangeFlagsVector = _applyPublicPermutationOddEvenCols(exchangeFlagsVector, expandedExchangeFlagsVector, indices, 0 :: uint);
        expandedExchangeFlagsVector = _applyPublicPermutationOddEvenCols(exchangeFlagsVector, expandedExchangeFlagsVector, indices, 1 :: uint);
        counter = counter + sizeOfStage;

        // Perform exchanges
        D T[[2]] firstFactor (n,2 * sizeOfStage);
        D T[[2]] secondFactor (n,2 * sizeOfStage);

        counter = 0;

        indices = iota(shape(firstVector)[1])[counter : counter + sizeOfStage];

        firstFactor = _applyPublicPermutationOddEvenCols(firstVector, firstFactor, indices, 0 :: uint);
        firstFactor = _applyPublicPermutationOddEvenCols(secondVector, firstFactor, indices, 1 :: uint);

        secondFactor = _applyPublicPermutationOddEvenCols(secondVector, secondFactor, indices, 0 :: uint);
        secondFactor = _applyPublicPermutationOddEvenCols(firstVector, secondFactor, indices, 1 :: uint);

        counter = counter + sizeOfStage;

        D T[[2]] choiceResults (n,2 * sizeOfStage);

        choiceResults = choose(expandedExchangeFlagsVector,firstFactor,secondFactor);

        // Finalize oblivious choices
        indices = sortnet[offset : offset + sizeOfStage];
        arrays = _unapplyPublicPermutationOddEvenCols(choiceResults, arrays, indices, 0 :: uint);
        offset = offset + sizeOfStage;
        indices = sortnet[offset : offset + sizeOfStage];
        arrays = _unapplyPublicPermutationOddEvenCols(choiceResults, arrays, indices, 1 :: uint);
        offset = offset + sizeOfStage;

    }
    return arrays;
}

template <domain D, type T>
D T[[2]] _sortingNetworkSortPar (D T[[2]] vector, D T[[2]] indices) {
    uint m = shape(vector)[0];

    uint[[1]] sortnet = generateSortingNetwork(shape(vector)[1]);

    uint offset = 0;
    uint numOfStages = sortnet[offset++];

    for (uint stage = 0; stage < numOfStages; stage++) {

        uint sizeOfStage = sortnet[offset++];

        uint [[1]] source = sortnet[offset : offset + sizeOfStage];
        D T[[2]] first1 = applyPublicPermutationCols(vector, source);
        D T[[2]] first2 = applyPublicPermutationCols(indices, source);
        D T[[2]] first = myCat(first1, first2, 1);
        offset = offset + sizeOfStage;

        source = sortnet[offset : offset + sizeOfStage];
        D T[[2]] second1 = applyPublicPermutationCols(vector, source);
        D T[[2]] second2 = applyPublicPermutationCols(indices, source);
        D T[[2]] second = myCat(second1, second2, 1);
        offset = offset + sizeOfStage;

        D bool[[2]] exchangeFlagsVector = mySlice(first,0,shape(first)[0],0,sizeOfStage) <= mySlice(second,0,shape(second)[0],0,sizeOfStage);
        exchangeFlagsVector = myCat(exchangeFlagsVector, exchangeFlagsVector,1);

        D T[[2]] results  = choose(exchangeFlagsVector, first, second);

        second = results ^ first ^ second;
        first = results;

        uint [[1]] target = sortnet[offset : offset + sizeOfStage];
        first1 = mySlice(first, 0, m, 0, sizeOfStage);
        first2 = mySlice(first, 0, m, sizeOfStage, 2 * sizeOfStage);
        vector = unapplyPublicPermutationCols(first1, vector, target);
        indices = unapplyPublicPermutationCols(first2, indices, target);
        offset = offset + sizeOfStage;

        target = sortnet[offset : offset + sizeOfStage];
        second1 = mySlice(second, 0, m, 0, sizeOfStage);
        second2 = mySlice(second, 0, m, sizeOfStage, 2 * sizeOfStage);
        vector = unapplyPublicPermutationCols(second1, vector, target);
        indices = unapplyPublicPermutationCols(second2, indices, target);
        offset = offset + sizeOfStage;

    }

    return indices;
}

//for simplicity, this sorts all the matrices according to the same columns
template <domain D>
D uint16[[3]] sortingNetworkSortPar (D uint16[[3]] matrix, uint column) {
    uint m = shape(matrix)[0];
    if (shape(matrix)[1] <= 1)
        return matrix;

    D uint16[[3]] shuffledMatrix (m, shape(matrix)[1],shape(matrix)[2]);
    shuffledMatrix = shuffleRowsPar(matrix);
    D xor_uint16[[2]] columnToSort = reshare(myGetProj(shuffledMatrix,column,2));

    
    uint[[2]] publicIndices (m, shape(columnToSort)[1]);
    for (uint i = 0; i < m; i++){
        publicIndices[i,:] = iota(shape(columnToSort)[1]);
    }
    D xor_uint16[[2]] indexVector = (uint16) publicIndices;
    indexVector = _sortingNetworkSortPar(columnToSort, indexVector);
    publicIndices = (uint) declassify(indexVector);

    D uint16[[3]] out = applyPublicPermutationRowsPar(shuffledMatrix, publicIndices);
    return out;
}

template <domain D>
D uint32[[3]] sortingNetworkSortPar (D uint32[[3]] matrix, uint column) {
    uint m = shape(matrix)[0];
    if (shape(matrix)[1] <= 1)
        return matrix;

    D uint32[[3]] shuffledMatrix (m, shape(matrix)[1],shape(matrix)[2]);
    shuffledMatrix = shuffleRowsPar(matrix);
    D xor_uint32[[2]] columnToSort = reshare(myGetProj(shuffledMatrix,column,2));

    
    uint[[2]] publicIndices (m, shape(columnToSort)[1]);
    for (uint i = 0; i < m; i++){
        publicIndices[i,:] = iota(shape(columnToSort)[1]);
    }
    D xor_uint32[[2]] indexVector = (uint32) publicIndices;
    indexVector = _sortingNetworkSortPar(columnToSort, indexVector);
    publicIndices = (uint) declassify(indexVector);

    D uint32[[3]] out = applyPublicPermutationRowsPar(shuffledMatrix, publicIndices);

    return out;
}

template <domain D>
D uint64[[3]] sortingNetworkSortPar (D uint64[[3]] matrix, uint column) {
    uint m = shape(matrix)[0];
    if (shape(matrix)[1] <= 1)
        return matrix;

    D uint64[[3]] shuffledMatrix (m, shape(matrix)[1],shape(matrix)[2]);
    shuffledMatrix = shuffleRowsPar(matrix);
    D xor_uint64[[2]] columnToSort = reshare(myGetProj(shuffledMatrix,column,2));

    
    uint[[2]] publicIndices (m, shape(columnToSort)[1]);
    for (uint i = 0; i < m; i++){
        publicIndices[i,:] = iota(shape(columnToSort)[1]);
    }
    D xor_uint64[[2]] indexVector = (uint64) publicIndices;
    indexVector = _sortingNetworkSortPar(columnToSort, indexVector);
    publicIndices = (uint) declassify(indexVector);

    D uint64[[3]] out = applyPublicPermutationRowsPar(shuffledMatrix, publicIndices);

    return out;
}

template <domain D>
D uint8[[3]] sortingNetworkSortPar (D uint8[[3]] matrix, uint column) {
    uint m = shape(matrix)[0];
    if (shape(matrix)[1] <= 1)
        return matrix;

    D uint8[[3]] shuffledMatrix (m, shape(matrix)[1],shape(matrix)[2]);
    shuffledMatrix = shuffleRowsPar(matrix);
    D xor_uint8[[2]] columnToSort = reshare(myGetProj(shuffledMatrix,column,2));

    
    uint[[2]] publicIndices (m, shape(columnToSort)[1]);
    for (uint i = 0; i < m; i++){
        publicIndices[i,:] = iota(shape(columnToSort)[1]);
    }
    D xor_uint8[[2]] indexVector = (uint8) publicIndices;
    indexVector = _sortingNetworkSortPar(columnToSort, indexVector);
    publicIndices = (uint) declassify(indexVector);

    D uint8[[3]] out = applyPublicPermutationRowsPar(shuffledMatrix, publicIndices);

    return out;
}

uint closestPowerOfTwo (uint x) {
   uint temp = 1;
   for (uint k = 0; k < x/2; k ++){
       if (temp >= x){
           break;
       }
       temp = temp * 2;
   }
   return temp;
}

template <domain D, type T, type U>
D T [[2]] _selectKPar (D T [[2]] vector, D T [[2]] indices, uint k, U minusOne) {
    //if (k >= shape(vector)[1]/2) return _sortingNetworkSortPar(vector, indices);
    uint m = shape(vector)[0];
    if (shape(vector)[1] <= 1)
        return vector;

    uint n = closestPowerOfTwo(shape(vector)[1]);
    //the padded values should be the largest, so that they are never taken
    D T [[2]] padding (m, n - shape(vector)[1]);
    padding = (0 :: U);
    vector = myCat(vector,padding,1);
    indices = myCat(indices,padding,1);

    uint[[1]] sortnet = generateTopKSortingNetwork (n, k);
    uint offset = 0;
    uint numOfStages = sortnet[offset++];

    for (uint stage = 0; stage < numOfStages; stage++) {

        uint sizeOfStage = sortnet[offset++];
        uint [[1]] source1 (sizeOfStage);
        uint [[1]] source2 (sizeOfStage);

        for (uint i = 0; i < sizeOfStage; ++i) {
            source1[i] = sortnet[offset+0];
            source2[i] = sortnet[offset+1];
            offset += 2;
        }

        D T[[2]] first1 = applyPublicPermutationCols(vector, source1);
        D T[[2]] first2 = applyPublicPermutationCols(indices, source1);
        D T[[2]] second1 = applyPublicPermutationCols(vector, source2);
        D T[[2]] second2 = applyPublicPermutationCols(indices, source2);
        D T[[2]] first = myCat(first1, first2, 1);
        D T[[2]] second = myCat(second1, second2, 1);


        D bool[[2]] exchangeFlagsVector = mySlice(first,0,shape(first)[0],0,sizeOfStage) <= mySlice(second,0,shape(first)[0],0,sizeOfStage);
        exchangeFlagsVector = myCat (exchangeFlagsVector, exchangeFlagsVector,1);

        D T[[2]] results = choose(exchangeFlagsVector, first, second);

        second = results ^ first ^ second;
        first = results;

        offset -= 2 * sizeOfStage;

        uint [[1]] target1 (sizeOfStage);
        uint [[1]] target2 (sizeOfStage);
        for (uint i = 0; i < sizeOfStage; ++i) {
            target1[i] = sortnet[offset+0];
            target2[i] = sortnet[offset+1];
            offset += 2;
        }
        first1  = mySlice(first, 0, m, 0, sizeOfStage);
        first2  = mySlice(first, 0, m, sizeOfStage, 2 * sizeOfStage);
        second1 = mySlice(second, 0, m, 0, sizeOfStage);
        second2 = mySlice(second, 0, m, sizeOfStage, 2 * sizeOfStage);

        vector = unapplyPublicPermutationCols(first1, vector, target1);
        indices = unapplyPublicPermutationCols(first2, indices, target1);
        vector = unapplyPublicPermutationCols(second1, vector, target2);
        indices = unapplyPublicPermutationCols(second2, indices, target2);

    }

    return indices;
}

//for simplicity, this sorts all the matrices according to the same columns
template <domain D>
D uint32[[3]] selectKPar (D uint32[[3]] matrix, uint k, uint column) {
    uint m = shape(matrix)[0];
    if (shape(matrix)[1] <= 1)
        return matrix;

    D uint32[[3]] shuffledMatrix (m, shape(matrix)[1],shape(matrix)[2]);
    shuffledMatrix = shuffleRowsPar(matrix);
    D xor_uint32[[2]] columnToSort = reshare(myGetProj(shuffledMatrix,column,2));

    uint[[2]] publicIndices (m, shape(columnToSort)[1]);
    for (uint i = 0; i < m; i++){
        publicIndices[i,:] = iota(shape(columnToSort)[1]);
    }
    D xor_uint32[[2]] indexVector = (uint32) publicIndices;
    indexVector = _selectKPar(columnToSort, indexVector, k, (0 :: uint32) - (1 :: uint32));
    publicIndices = (uint) declassify(indexVector);
    D uint32[[3]] out = applyPublicPermutationRowsPar(shuffledMatrix, publicIndices);

    return out;
}

template <domain D>
D uint16[[3]] selectKPar (D uint16[[3]] matrix, uint k, uint column) {
    uint m = shape(matrix)[0];
    if (shape(matrix)[1] <= 1)
        return matrix;

    D uint16[[3]] shuffledMatrix (m, shape(matrix)[1],shape(matrix)[2]);
    shuffledMatrix = shuffleRowsPar(matrix);
    D xor_uint16[[2]] columnToSort = reshare(myGetProj(shuffledMatrix,column,2));

    uint[[2]] publicIndices (m, shape(columnToSort)[1]);
    for (uint i = 0; i < m; i++){
        publicIndices[i,:] = iota(shape(columnToSort)[1]);
    }
    D xor_uint16[[2]] indexVector = (uint16) publicIndices;
    indexVector = _selectKPar(columnToSort, indexVector, k, (0 :: uint16) - (1 :: uint16));
    publicIndices = (uint) declassify(indexVector);
    D uint16[[3]] out = applyPublicPermutationRowsPar(shuffledMatrix, publicIndices);

    return out;
}

template <domain D>
D uint8[[3]] selectKPar (D uint8[[3]] matrix, uint k, uint column) {
    uint m = shape(matrix)[0];
    if (shape(matrix)[1] <= 1)
        return matrix;

    D uint8[[3]] shuffledMatrix (m, shape(matrix)[1],shape(matrix)[2]);
    shuffledMatrix = shuffleRowsPar(matrix);
    D xor_uint8[[2]] columnToSort = reshare(myGetProj(shuffledMatrix,column,2));

    uint[[2]] publicIndices (m, shape(columnToSort)[1]);
    for (uint i = 0; i < m; i++){
        publicIndices[i,:] = iota(shape(columnToSort)[1]);
    }
    D xor_uint8[[2]] indexVector = (uint8) publicIndices;
    indexVector = _selectKPar(columnToSort, indexVector, k, (0 :: uint8) - (1 :: uint8));
    publicIndices = (uint) declassify(indexVector);
    D uint8[[3]] out = applyPublicPermutationRowsPar(shuffledMatrix, publicIndices);

    return out;
}

//-------quick sort

template <domain D, type T>
D T[[1]] quickSort (D T[[1]] X) {

    uint64 [[1]] blocks = {0 :: uint64, size(X)};
    uint64 [[1]] sigma  = iota(size(X));

    D xor_uint64 [[1]] dummy = sigma;

    D uint8 [[1]] key(32);
    key = randomize(key);

    X     = shuffle(X,key);
    dummy = shuffle(dummy,key);

    bool ascend = true;
    __syscall("shared3p::stable_sort_$T\_vec", __domainid(D), X, dummy, ascend, __cref blocks, __ref sigma);

    X = applyPermutation(X, sigma);
    return X;
}

template <domain D, type T>
D uint [[1]] quickSortPermutation (D T[[1]] X) {

    uint64 [[1]] blocks = {0 :: uint64, size(X)};
    uint64 [[1]] sigma  = iota(size(X));
    D uint [[1]] pi     = sigma;

    D xor_uint64 [[1]] dummy = sigma;

    D uint8 [[1]] key(32);
    key = randomize(key);

    X     = shuffle(X,key);
    pi    = shuffle(pi,key);
    dummy = shuffle(dummy,key);

    bool ascend = true;
    __syscall("shared3p::stable_sort_$T\_vec", __domainid(D), X, dummy, ascend, __cref blocks, __ref sigma);

    pi = applyPermutation(pi, sigma);
    return pi;
}


template <domain D, type T, type S>
D S [[2]] quickSortPermutationPar (D T[[2]] idx) {

    uint m = shape(idx)[0];
    uint n = shape(idx)[1];

    D uint8 [[1]] key(32);
    key = randomize(key);

    S [[2]] identity = (S)reshape(copyBlock(iota(n),m), m, n);
    D S [[2]] val = identity;

    uint [[2]] pub_dummy = reshape(iota(m * n), m, n);
    D xor_uint64 [[2]] dummy = pub_dummy;

    idx   = shufflePar(idx,key);
    val   = shufflePar(val,key);
    dummy = shufflePar(dummy,key);

    D T [[1]] idx_flat = myFlatten(idx);
    D S [[1]] val_flat = myFlatten(val);
    uint64 [[1]] blocks = iota(m + 1) * n;
    uint64 [[1]] sigma  = iota(m * n);

    bool ascend = true;
    __syscall("shared3p::stable_sort_$T\_vec", __domainid(D), idx_flat, dummy, ascend, __cref blocks, __ref sigma);

    val_flat = applyPermutation(val_flat, sigma);
    val = myReshape(val_flat, m, n);

    return val;
}

//for simplicity, this sorts all the matrices according to the same columns
//3rd dimension: 0 - index, 1 - value, where sorting is done by index
template <domain D, type T>
D T[[3]] quickSortPar (D T[[3]] matrices, uint column) {

    uint m = shape(matrices)[0];
    uint n = shape(matrices)[1];
    uint o = shape(matrices)[2];

    D uint8 [[1]] key(32);
    key = randomize(key);

    D T [[3]] val = shuffleRowsPar(matrices,key);
    D T [[2]] idx = myGetProj(val, column, 2);

    uint [[2]] pub_dummy = reshape(iota(m * n), m, n);
    D xor_uint64 [[2]] dummy = pub_dummy;
    dummy = shufflePar(dummy,key);

    D T [[1]] idx_flat = myFlatten(idx);
    D T [[2]] val_flat = myTranspose(myReshape(val,m * n, o));
    uint64 [[1]] blocks = iota(m + 1) * n;
    uint64 [[1]] sigma  = iota(m * n);

    bool ascend = true;
    __syscall("shared3p::stable_sort_$T\_vec", __domainid(D), idx_flat, dummy, ascend, __cref blocks, __ref sigma);

    val_flat = applyPublicPermutationCols(val_flat, sigma);
    val = myReshape(myTranspose(myReshape(val_flat, o, m * n)), m, n, o);

    return val;
}

// TODO need to include "shared3p::public_sort"
//public sort
/*
template <type T>
uint64 [[1]] inverse_sorting_permutation(T[[1]] vec) {
    uint64 [[1]] sigma(size(vec));
    __syscall("shared3p::public_sort_$T\_vec",  __domainid (pd_shared3p), __ref sigma, __cref vec);
    return sigma;
}
*/

//------------------permutation protocols


// TODO need to include "shared3p::genRandPubPerm"
//a public random permutation
/*
//a public random permutation
uint32 [[1]] public_permutation(uint n){
    uint32 [[1]] pubPerm (n);
    __syscall ("shared3p::genRandPubPerm", __domainid (pd_shared3p), __ref pubPerm);
    return pubPerm;
}
*/

//applying public permutations
template <domain D, type T>
D T [[1]] unapplyPublicPermutation(D T [[1]] a, uint [[1]] indices){
    assert(size(a) == size(indices));
    uint n = shape(indices)[0];
    uint [[1]] source = iota(n);
    uint [[1]] target = indices;
    D T[[1]] b (n);
    b = partialRearrange(a, b, source, target);
    return b;
}

template <domain D, type T>
D T [[2]] unapplyPublicPermutationPar(D T [[2]] A, uint [[2]] indices){
    assert(shape(A)[0] == shape(indices)[0]);
    assert(shape(A)[1] == shape(indices)[1]);
    uint m = shape(indices)[0];
    uint n = shape(indices)[1];
    uint [[1]] source = iota(m * n);
    uint [[1]] target (m * n);
    for (uint i = 0; i < m; ++i) {
        for (uint j = 0; j < n; ++j) {
            target[i * n + j] = i * n + indices[i,j];
        }
    }
    D T[[2]] B (m,n);
    B = partialRearrange(A, B, source, target);
    return B;
}

template <domain D, type T>
D T [[2]] unapplyPublicPermutationRows(D T [[2]] A, uint [[1]] indices){
    assert(shape(A)[0] == size(indices));
    uint m = shape(indices)[0];
    uint n = shape(A)[1];
    uint [[1]] source = iota(m * n);
    uint [[1]] target (m * n);
    for (uint i = 0; i < m; ++i) {
        for (uint j = 0; j < n; ++j) {
            target[i * n + j] = indices[i] * n + j;
        }
    }
    D T[[2]] B (m,n);
    B = partialRearrange(A, B, source, target);
    return B;
}

template <domain D, type T>
D T [[3]] unapplyPublicPermutationRowsPar(D T [[3]] A, uint [[2]] indices){

    uint m = shape(indices)[0];
    uint n1 = shape(indices)[1];
    uint n2 = shape(A)[2];
    uint [[1]] source = iota(m * n1 * n2);
    uint [[1]] target (m * n1 * n2);
    for (uint i = 0; i < m; ++i) {
        for (uint j = 0; j < n1; ++j) {
            for (uint k = 0; k < n2; ++k) {
                target[i * n1 * n2 + j * n2 + k] = i * n1 * n2 + indices[i,j] * n2 + k;
            }
        }
    }
    D T[[3]] B (m,n1,n2);
    B = partialRearrange(A, B, source, target);
    return B;
}

template <domain D, type T>
D T [[2]] unapplyPublicPermutationCols(D T [[2]] A, uint [[1]] indices){

    assert(shape(A)[1] == size(indices));
    uint m = shape(A)[0];
    uint n = shape(indices)[0];
    uint [[1]] source = iota(m * n);
    uint [[1]] target (m * n);
    for (uint i = 0; i < m; ++i) {
        for (uint j = 0; j < n; ++j) {
            target[i * n + j] = i * shape(A)[1] + indices[j];
        }
    }
    D T[[2]] B (m,n);
    B = partialRearrange(A, B, source, target);
    return B;
}

template <domain D, type T>
D T [[2]] unapplyPublicPermutationCols(D T [[2]] A, D T [[2]] B, uint [[1]] indices){

    uint m = shape(A)[0];
    uint n = shape(indices)[0];
    uint [[1]] source = iota(m * n);
    uint [[1]] target (m * n);
    for (uint i = 0; i < m; ++i) {
        for (uint j = 0; j < n; ++j) {
            target[i * n + j] = i * shape(B)[1] + indices[j];
        }
    }
    B = partialRearrange(A, B, source, target);
    return B;
}

template <domain D, type T>
D T [[3]] unapplyPublicPermutationColsPar(D T [[3]] A, D T [[3]] B, uint [[2]] indices){

    uint m = shape(indices)[0];
    uint n1 = shape(A)[1];
    uint n2 = shape(indices)[1];
    uint [[1]] source = iota(m * n1 * n2);
    uint [[1]] target (m * n1 * n2);
    for (uint i = 0; i < m; ++i) {
        for (uint j = 0; j < n1; ++j) {
            for (uint k = 0; k < n2; ++k) {
                target[i * n1 * n2 + j * n2 + k] = i * shape(B)[1] * shape(B)[2] + j * shape(B)[2] + indices[i,k];
            }
        }
    }
    B = partialRearrange(A, B, source, target);
    return B;
}


template <domain D, type T>
D T [[1]] applyPublicPermutation(D T [[1]] a, uint [[1]] indices){
    uint n = shape(indices)[0];
    uint [[1]] source = indices;
    uint [[1]] target = iota(n);
    D T[[1]] b (n);
    b = partialRearrange(a, b, source, target);
    return b;
}

template <domain D, type T>
D T [[2]] applyPublicPermutationPar(D T [[2]] A, uint [[2]] indices){

    uint m = shape(indices)[0];
    uint n = shape(indices)[1];
    uint [[1]] source (m * n);
    uint [[1]] target = iota(m * n);
    for (uint i = 0; i < m; ++i) {
        for (uint j = 0; j < n; ++j) {
            source[i * n + j] = i * shape(A)[1] + indices[i,j];
        }
    }
    D T[[2]] B (m,n);
    B = partialRearrange(A, B, source, target);
    return B;
}

template <domain D, type T>
D T [[2]] applyPublicPermutationRows(D T [[2]] A, uint [[1]] indices){

    uint m = shape(indices)[0];
    uint n = shape(A)[1];
    uint [[1]] source (m * n);
    uint [[1]] target = iota(m * n);
    for (uint i = 0; i < m; ++i) {
        for (uint j = 0; j < n; ++j) {
            source[i * n + j] = indices[i] * shape(A)[1] + j;
        }
    }
    D T[[2]] B (m,n);
    B = partialRearrange(A, B, source, target);
    return B;
}

template <domain D, type T>
D T [[3]] applyPublicPermutationRowsPar(D T [[3]] A, uint [[2]] indices){

    uint m = shape(indices)[0];
    uint n1 = shape(indices)[1];
    uint n2 = shape(A)[2];
    uint [[1]] source (m * n1 * n2);
    uint [[1]] target = iota(m * n1 * n2);
    for (uint i = 0; i < m; ++i) {
        for (uint j = 0; j < n1; ++j) {
            for (uint k = 0; k < n2; ++k) {
                source[i * n1 * n2 + j * n2 + k] = i * shape(A)[1] * shape(A)[2] + indices[i,j] * shape(A)[2] + k;
            }
        }
    }
    D T[[3]] B (m,n1,n2);
    B = partialRearrange(A, B, source, target);
    return B;
}


template <domain D, type T>
D T [[2]] applyPublicPermutationCols(D T [[2]] A, uint [[1]] indices){

    uint m = shape(A)[0];
    uint n = shape(indices)[0];
    uint [[1]] source (m * n);
    uint [[1]] target = iota(m * n);
    for (uint i = 0; i < m; ++i) {
        for (uint j = 0; j < n; ++j) {
            source[i * n + j] = i * shape(A)[1] + indices[j];
        }
    }
    D T[[2]] B (m,n);
    B = partialRearrange(A, B, source, target);
    return B;
}

template <domain D, type T>
D T [[3]] applyPublicPermutationColsPar(D T [[3]] A, uint [[2]] indices){

    uint m = shape(indices)[0];
    uint n1 = shape(A)[1];
    uint n2 = shape(indices)[1];
    uint [[1]] source (m * n1 * n2);
    uint [[1]] target = iota(m * n1 * n2);
    for (uint i = 0; i < m; ++i) {
        for (uint j = 0; j < n1; ++j) {
            for (uint k = 0; k < n2; ++k) {
                source[i * n1 * n2 + j * n2 + k] = i * shape(A)[1] * shape(A)[2] + j * shape(A)[2] + indices[i,k];
            }
        }
    }
    D T[[3]] B (m,n1,n2);
    B = partialRearrange(A, B, source, target);
    return B;
}

//special public permutation (used inside sorting algorithms)

template <domain D, type T>
D T [[1]] _unapplyPublicPermutationOddEven(D T [[1]] a, uint [[1]] indices, uint parity){
    uint n = shape(indices)[0];
    uint [[1]] source = iota(m * n) * (2 :: uint32) + (uint32)parity;
    uint [[1]] target = indices;
    D T[[1]] b (n);
    b = partialRearrange(a, b, source, target);
    return b;
}

template <domain D, type T>
D T [[2]] _unapplyPublicPermutationOddEvenPar(D T [[2]] A, uint [[2]] indices, uint parity){

    uint m = shape(indices)[0];
    uint n = shape(indices)[1];
    uint [[1]] source = iota(m * n) * 2 + parity;
    uint [[1]] target (m * n);
    for (uint i = 0; i < m; ++i) {
        for (uint j = 0; j < n; ++j) {
            target[i * n + j] = i * n + indices[i,j];
        }
    }
    D T[[2]] B (m,n);
    B = partialRearrange(A, B, source, target);
    return B;
}

template <domain D, type T>
D T [[2]] _unapplyPublicPermutationOddEvenRows(D T [[2]] A, uint [[1]] indices, uint parity){

    uint m = shape(indices)[0];
    uint n = shape(A)[1];
    uint [[1]] source = iota(m * n) * 2 + parity;
    uint [[1]] target (m * n);
    for (uint i = 0; i < m; ++i) {
        for (uint j = 0; j < n; ++j) {
            target[i * n + j] = indices[i] * n + j;
        }
    }
    D T[[2]] B (m,n);
    B = partialRearrange(A, B, source, target);
    return B;
}

template <domain D, type T>
D T [[3]] _unapplyPublicPermutationOddEvenRowsPar(D T [[3]] A, uint [[2]] indices, uint parity){

    uint m = shape(indices)[0];
    uint n1 = shape(indices)[1];
    uint n2 = shape(A)[2];
    uint [[1]] source = iota(m * n1 * n2) * 2 + parity;
    uint [[1]] target (m * n1 * n2);
    for (uint i = 0; i < m; ++i) {
        for (uint j = 0; j < n1; ++j) {
            for (uint k = 0; k < n2; ++k) {
                target[i * n1 * n2 + j * n2 + k] = i * n1 * n2 + indices[i,j] * n2 + k;
            }
        }
    }
    D T[[3]] B (m,n1,n2);
    B = partialRearrange(A, B, source, target);
    return B;
}


template <domain D, type T>
D T [[2]] _unapplyPublicPermutationOddEvenCols(D T [[2]] A, D T [[2]] B, uint [[1]] indices, uint parity){

    uint m = shape(A)[0];
    uint n = shape(indices)[0];
    uint [[1]] source = iota(m * n) * 2 + parity;
    uint [[1]] target (m * n);
    for (uint i = 0; i < m; ++i) {
        for (uint j = 0; j < n; ++j) {
            target[i * n + j] = i * shape(B)[1] + indices[j];
        }
    }
    B = partialRearrange(A, B, source, target);
    return B;
}

template <domain D, type T>
D T [[3]] _unapplyPublicPermutationOddEvenColsPar(D T [[3]] A, D T [[3]] B, uint [[2]] indices, uint parity){

    uint m = shape(indices)[0];
    uint n1 = shape(A)[1];
    uint n2 = shape(indices)[1];
    uint [[1]] source = iota(m * n1 * n2) * 2 + parity;
    uint [[1]] target (m * n1 * n2);
    for (uint i = 0; i < m; ++i) {
        for (uint j = 0; j < n1; ++j) {
            for (uint k = 0; k < n2; ++k) {
                target[i * n1 * n2 + j * n2 + k] = i * shape(B)[1] * shape(B)[2] + j * shape(B)[2] + indices[i,k];
            }
        }
    }
    B = partialRearrange(A, B, source, target);
    return B;
}


template <domain D, type T>
D T [[1]] _applyPublicPermutationOddEven(D T [[1]] a, D T [[1]] b, uint [[1]] indices, uint parity){
    uint n = shape(indices)[0];
    uint [[1]] source = (uint32)indices;
    uint [[1]] target = iota(n / (2 :: uint)) * 2 + parity;
    b = partialRearrange(a, b, source, target);
    return b;
}

template <domain D, type T>
D T [[2]] _applyPublicPermutationOddEvenPar(D T [[2]] A, D T [[2]] B, uint [[2]] indices, uint parity){

    uint m = shape(indices)[0];
    uint n = shape(indices)[1];
    uint [[1]] source (m * n);
    uint [[1]] target = iota(m * n) * 2 + parity;
    for (uint i = 0; i < m; ++i) {
        for (uint j = 0; j < n; ++j) {
            source[i * n + j] = (uint32)(i * shape(A)[1] + indices[i,j]);
        }
    }
    B = partialRearrange(A, B, source, target);
    return B;
}

template <domain D, type T>
D T [[2]] _applyPublicPermutationOddEvenRows(D T [[2]] A, D T [[2]] B, uint [[1]] indices, uint parity){

    uint m = shape(indices)[0];
    uint n = shape(A)[1];
    uint [[1]] source (m * n);
    uint [[1]] target = iota(m * n) * 2 + parity;
    for (uint i = 0; i < m; ++i) {
        for (uint j = 0; j < n; ++j) {
            source[i * n + j] = indices[i] * shape(A)[1] + j;
        }
    }
    B = partialRearrange(A, B, source, target);
    return B;
}

template <domain D, type T>
D T [[3]] _applyPublicPermutationOddEvenRowsPar(D T [[3]] A, D T [[3]] B, uint [[2]] indices, uint parity){

    uint m = shape(indices)[0];
    uint n1 = shape(indices)[1];
    uint n2 = shape(A)[2];
    uint [[1]] source (m * n1 * n2);
    uint [[1]] target = iota(m * n1 * n2) * 2 + parity;
    for (uint i = 0; i < m; ++i) {
        for (uint j = 0; j < n1; ++j) {
            for (uint k = 0; k < n2; ++k) {
                source[i * n1 * n2 + j * n2 + k] = i * shape(A)[1] * shape(A)[2] + indices[i,j] * shape(A)[2] + k;
            }
        }
    }
    B = partialRearrange(A, B, source, target);
    return B;
}


template <domain D, type T>
D T [[2]] _applyPublicPermutationOddEvenCols(D T [[2]] A, D T [[2]] B, uint [[1]] indices, uint parity){

    uint m = shape(A)[0];
    uint n = shape(indices)[0];

    uint [[1]] source (m * n);
    uint [[1]] target = iota(m * n) * 2 + parity;
    for (uint i = 0; i < m; ++i) {
        for (uint j = 0; j < n; ++j) {
            source[i * n + j] = i * shape(A)[1] + indices[j];
        }
    }
    B = partialRearrange(A, B, source, target);
    return B;
}

template <domain D, type T>
D T [[3]] _applyPublicPermutationOddEvenColsPar(D T [[3]] A, D T [[3]] B, uint [[2]] indices, uint parity){

    uint m = shape(indices)[0];
    uint n1 = shape(A)[1];
    uint n2 = shape(indices)[1];
    uint [[1]] source (m * n1 * n2);
    uint [[1]] target = iota(m * n1 * n2) * 2 + parity;
    for (uint i = 0; i < m; ++i) {
        for (uint j = 0; j < n1; ++j) {
            for (uint k = 0; k < n2; ++k) {
                source[i * n1 * n2 + j * n2 + k] = i * shape(A)[1] * shape(A)[2] + j * shape(A)[2] + indices[i,k];
            }
        }
    }
    B = partialRearrange(A, B, source, target);
    return B;
}

//applying private permutations
template <domain D, type T, type U>
D T [[1]] applyPrivatePermutation (D T [[1]] data, D U [[1]] pi){

    pd_shared3p uint8 [[1]] key(32);
    key = randomize(key);

    uint [[1]] tau = (uint)declassify(shuffle(pi, key));
    data = applyPublicPermutation(data, tau);
    return inverseShuffle(data, key);
}

template <domain D, type T, type U>
D T [[1]] unapplyPrivatePermutation (D T [[1]] data, D U [[1]] pi){

    pd_shared3p uint8 [[1]] key(32);
    key = randomize(key);

    data = shuffle(data, key);                                                 
    uint [[1]] tau = (uint)declassify(shuffle(pi, key));
    return applyPublicPermutation(data, inversePermutation(tau));
}

template <domain D, type T, type U>
D T [[2]] applyPrivatePermutationRows (D T [[2]] data, D U [[1]] pi){

    pd_shared3p uint8 [[1]] key(32);
    key = randomize(key);

    uint [[1]] tau = (uint)declassify(shuffle(pi, key));
    data = applyPublicPermutationRows(data, tau);
    return inverseShuffleRows(data, key);
}

template <domain D, type T, type U>
D T [[2]] applyPrivatePermutationCols (D T [[2]] data, D U [[1]] pi){

    pd_shared3p uint8 [[1]] key(32);
    key = randomize(key);

    uint [[1]] tau = (uint)declassify(shuffle(pi, key));
    data = applyPublicPermutationRows(myTranspose(data), tau);
    return myTranspose(inverseShuffleRows(data, key));
}

template <domain D, type T, type U>
D T [[2]] unapplyPrivatePermutationRows (D T [[2]] data, D U [[1]] pi){

    pd_shared3p uint8 [[1]] key(32);
    key = randomize(key);

    data = shuffleRows(data, key);                                                 
    uint [[1]] tau = (uint)declassify(shuffle(pi, key));
    return applyPublicPermutationRows(data, inversePermutation(tau));
}

template <domain D, type T, type U>
D T [[2]] unapplyPrivatePermutationCols (D T [[2]] data, D U [[1]] pi){

    pd_shared3p uint8 [[1]] key(32);
    key = randomize(key);

    data = shuffleRows(myTranspose(data), key);                                                 
    uint [[1]] tau = (uint)declassify(shuffle(pi, key));
    return myTranspose(applyPublicPermutationRows(data, inversePermutation(tau)));
}

template <domain D, type T>
D T [[1]] privatePermutationInverse (D T [[1]] pi){

    T [[1]] indices = (T)iota(size(pi));
    pd_shared3p uint8 [[1]] key(32);
    key = randomize(key);
          
    D T [[1]] PR_indices = indices;                            
    D T [[1]] rho = shuffle(PR_indices, key);           
    uint [[1]] tau = (uint)declassify(shuffle(pi, key));
    return applyPublicPermutation(rho, inversePermutation(tau));
}


template <domain D, type T, type U>
D T [[2]] applyPrivatePermutationPar (D T [[2]] data, D U [[2]] pi){

    uint m = shape(pi)[0];
    uint n = shape(pi)[1];
    uint [[1]] indices (m*n);
    for (uint i = 0; i < m; i++){
        indices[i*n : (i+1)*n] = i * n;
    }
    D U [[1]] offsets = (U)indices;
    return myReshape(applyPrivatePermutation(myFlatten(data), myFlatten(pi) + offsets), m, n);
}


template <domain D, type T, type U>
D T [[2]] unapplyPrivatePermutationPar (D T [[2]] data, D U [[2]] pi){

    uint m = shape(pi)[0];
    uint n = shape(pi)[1];
    uint [[1]] indices (m*n);
    for (uint i = 0; i < m; i++){
        indices[i*n : (i+1)*n] = i * n;
    }
    D U [[1]] offsets = (U)indices;
    return myReshape(unapplyPrivatePermutation(myFlatten(data), myFlatten(pi) + offsets), m, n);
}

template <domain D, type T, type U>
D T [[3]] applyPrivatePermutationPar (D T [[3]] data, D U [[2]] pi){

    uint m = shape(data)[0];
    uint n = shape(data)[1];
    uint l = shape(data)[2];
    uint [[1]] indices (m*n);
    for (uint i = 0; i < m; i++){
        indices[i*n : (i+1)*n] = i * n;
    }
    D U [[1]] offsets = (U)indices;
    return myReshape(applyPrivatePermutationRows(myReshape(data ,m * n, l), myFlatten(pi) + offsets), m, n, l);
}

//TODO something could be wrong here
template <domain D, type T, type U>
D T [[3]] unapplyPrivatePermutation (D T [[3]] data, D U [[2]] pi){

    uint m = shape(pi)[0];
    uint n = shape(pi)[1];
    uint l = shape(pi)[2];
    uint [[1]] indices (m*n);
    for (uint i = 0; i < m; i++){
        indices[i*n : (i+1)*n] = i * n;
    }
    D U [[1]] offsets = (U)indices;
    return myReshape(unapplyPrivatePermutationRows(myReshape(data, m * n, l), myFlatten(pi) + offsets), m, n, l);
}

template <domain D, type T>
D T [[2]] privatePermutationInversePar (D T [[2]] pi){

    uint m = shape(pi)[0];
    uint n = shape(pi)[1];
    uint [[1]] indices (m*n);
    for (uint i = 0; i < m; i++){
        indices[i*n : (i+1)*n] = i * n;
    }
    D uint [[1]] offsets = indices;
    return myReshape(privatePermutationInverse(myFlatten(pi) + offsets), m, n);
}

template <domain D, type T>
D T [[2]] shufflePar (D T [[2]] vectors){
    
    pd_shared3p uint8 [[1]] key(32);
    key = randomize(key);
    return shufflePar (vectors, key);
}

// shuffle
template <domain D, type T>
D T [[2]] shufflePar (D T [[2]] vectors, D uint8 [[1]] key){

    uint m = shape(vectors)[0];
    uint n = shape(vectors)[1];

    uint [[1]] indices = iota(m) * n;
    uint [[1]] counts (m); counts = n;

    D T [[1]] data = myFlatten(vectors);
    data = shuffleBlocks(data, indices, counts, key);
    vectors = myReshape(data, m, n);
    return vectors;
}

template <domain D, type T>
D T [[2]] inverseShufflePar (D T [[2]] vectors, D uint8 [[1]] key){

    uint m = shape(vectors)[0];
    uint n = shape(vectors)[1];

    uint [[1]] indices = iota(m) * n;
    uint [[1]] counts (m); counts = n;

    D T [[1]] data = myFlatten(vectors);
    data = inverseShuffleBlocks(data, indices, counts, key);
    vectors = myReshape(data, m, n);
    return vectors;

}


template <domain D, type T>
D T [[3]] shuffleRowsPar (D T [[3]] matrices){

    pd_shared3p uint8 [[1]] key(32);
    key = randomize(key);
    return shuffleRowsPar (matrices, key);
}

template <domain D, type T>
D T [[3]] shuffleRowsPar (D T [[3]] matrices, D uint8 [[1]] key){

    uint m = shape(matrices)[0];
    uint n = shape(matrices)[1];
    uint l = shape(matrices)[2];
    uint mn = m * n;
    uint nl = n * l;

    D T [[2]] data = myReshape(matrices, mn, l);
    data = shuffleRows(data, key);
    D uint [[1]] indices = (uint)iota(m);
    D uint [[1]] privateIndices = myReplicate(indices, n);
    uint [[1]] tags = (uint)declassify(shuffle(privateIndices, key));

    uint [[1]] permutedIndices (mn);
    uint [[1]] counters (m);
    for (uint i = 0; i < mn; i++){
        permutedIndices[i] = tags[i] * n + counters[tags[i]];
        counters[tags[i]]++;
    }
    matrices = myReshape(unapplyPublicPermutationRows(data, permutedIndices), m, n, l);
    return matrices;

}

template <domain D, type T>
D T [[1]] shuffleBlocks (D T [[1]] data, uint [[1]] indices, uint [[1]] counts, D uint8 [[1]] key){
    return bothShuffleBlocks(data, indices, counts, key, false);
}

template <domain D, type T>
D T [[1]] inverseShuffleBlocks (D T [[1]] data, uint [[1]] indices, uint [[1]] counts, D uint8 [[1]] key){
    return bothShuffleBlocks(data, indices, counts, key, true);
}

template <domain D, type T>
D T [[1]] bothShuffleBlocks (D T [[1]] data, uint [[1]] indices, uint [[1]] counts, D uint8 [[1]] key, bool inverse){

    uint m = size(indices);
    uint l = size(data);

    uint [[1]] tags = iota(m);
    uint [[1]] steps (m); steps = 1;

    uint [[1]] publicIndices = myReplicate(tags,    steps, counts);
    uint [[1]] publicOffsets = myReplicate(indices, steps, counts);

    D uint [[1]] privateIndices = publicIndices;
    D uint [[1]] privateOffsets = publicOffsets;

    if (!inverse) data = shuffle(data, key);
    publicIndices = (uint)declassify(shuffle(privateIndices, key));
    publicOffsets = (uint)declassify(shuffle(privateOffsets, key));

    uint [[1]] permutedIndices (l);
    uint [[1]] counters (m);
    for (uint i = 0; i < l; i++){
        permutedIndices[i] = publicOffsets[i] + counters[publicIndices[i]];
        counters[publicIndices[i]]++;
    }
    if (inverse) return inverseShuffle(applyPublicPermutation(data, permutedIndices), key);
    else return unapplyPublicPermutation(data, permutedIndices);
}

template <type T>
T [[1]] inversePermutation (T [[1]] permutation){
    T [[1]] permutation_inverse(size(permutation));
    for (uint i = 0; i < size(permutation); i++){
        permutation_inverse[(uint)permutation[i]] = (T)i;
    }
    return permutation_inverse;
}

//------------------bit extraction protocols

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

// convert any data to an array of shared bools
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
    int [[1]] shifts = (int)iota(n);
    D T [[1]] W = shiftBitsLeftVec(Z, shifts);
    return prefixXOR(W)[n-1];
}

//convert a matrix of bools to a vector of xor_units
template <domain D, type T >
D T [[1]] bool_to_xor(D bool [[2]] X){
    uint m = shape(X)[0];
    uint n = shape(X)[1];
    D T [[2]] Z = (T)X & 1;
    int [[1]] shifts = copiedGlist(m, n);
    D T [[2]] W = shiftBitsLeftVec(Z, shifts);
    return myGetProj(prefixXORPar(W), n-1, 1);
}

//this is only 32-bit in the standard library, but a __syscall does exist!
template <domain D, type T, dim N>
D T[[N]] shiftBitsLeftVec (D T[[N]] bits, int [[1]] shifts) {
    __syscall ("shared3p::shift_left_$T\_vec", __domainid (D), bits, __cref shifts, bits);
    return bits;
}

// the outer product is a matrix Z such that Z[i,j] = X[i] * Y[j]
template <domain D , type T>
D T [[2]] outerProduct(D T [[1]] X, D T [[1]] Y){
    D T [[2]] Z (size(X), size(Y));
    uint one = 1;
    __syscall("shared3p::mat_mult_$T\_vec", __domainid(D), X, Y, Z, __cref size(X), __cref one, __cref size(Y));
    return Z;
}

// the outer product is a matrix Z such that Z[i,j] = X[i] * Y[j]
// compute Z[k,i,j] = X[k,i] * Y[k,j]
// return reshape(Z,k,i * j), can reshape it back later if necessary
template <domain D , type T>
D T [[2]] outerProduct(D T [[2]] X, D T [[2]] Y){

    assert(shape(X)[0] == shape(Y)[0]);

    D T [[2]] Z (shape(X)[0], shape(X)[1] * shape(Y)[1]);
    uint [[1]] mm (shape(X)[0]);
    uint [[1]] nn (shape(X)[0]);
    uint [[1]] pp (shape(X)[0]);
    mm = shape(X)[1];
    nn = 1;
    pp = shape(Y)[1];
    __syscall("shared3p::mat_mult_$T\_vec", __domainid(D), X, Y, Z, __cref mm, __cref nn, __cref pp);
    return Z;
}

// TODO need to include syscall "shared3p::mat_mult_xor_uint8_vec"
//this is a parallelizable stack-based version that computes all the branches simultaneiously
//it returns all the branches alltogether
/*
template <domain D >
D xor_uint8 [[2]] charVecRec(D xor_uint8 [[2]] X){
    print("CV level " + tostring(shape(X)[1]));
    //we have now n parallel algorithm instances running, that covers also the parallel branches
    //all the sizes of X[i] are the same
    //hence we can take if-then-else out and parallelize the inner contents only
    uint n = shape(X)[0];
    uint m = shape(X)[1];
    if(m == 0){
        D xor_uint8 [[2]] W (n,0);
        return W;
    }else if (m == 1){
        return myCat(~X, X, 1);
    }else{
        //the input works like a stack
        //double each stack element and repeat the recursion
        bool cut_last = false;
        D xor_uint8 [[2]] left_X  = mySlice(X, 0 :: uint, shape(X)[0], 0 :: uint, m/2);
        D xor_uint8 [[2]] right_X = mySlice(X, 0 :: uint, shape(X)[0], m/2, shape(X)[1]);

        //if m is odd, then pad all the numbers by a meaningless most significant 0
        if (((m/2) * 2) != m){    
            D xor_uint8 [[2]] zero (n,1);
            left_X = myCat(left_X, zero,1);
            cut_last = true;
        }
        D xor_uint8 [[2]] YZ = charVecRec(myCat(left_X,right_X,0));
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
D xor_uint8 [[2]] charVecPar(D T [[1]] X, uint logm,  uint m){
    uint n = size(X) / (8 :: uint);
    uint r = size(X) % (8 :: uint);
    if (r != 0){
        n = n + 1;
    }

    D T [[1]] Y (8 * n);
    Y = partialRearrange(X,Y);
    uint k = size_in_bits(Y);

    D bool [[2]] W = myTranspose(myReshape(bit_extract(Y),8 :: uint, n * k));
    D xor_uint8 [[1]] Z = bool_to_xor(W);
    D xor_uint8 [[2]] XX = mySlice(myReshape(Z, n, k), 0 :: uint, n, 0 :: uint, logm);
    D xor_uint8 [[2]] YY = charVecRec(XX);
    return mySlice(YY, 0 :: uint, n, 0 :: uint, m);
}
*/

// TODO need to include the syscall "shared3p::mat_multx_xor_uint8_vec"
// the outer product is a matrix Z such that Z[i,j] = X[i] & Y[j]
// compute Z[k,i,j] = X[k,i] & Y[k,j]
// return reshape(Z,k,i * j), can reshape it back later if necessary
/*
template <domain D , type T>
D T [[2]] outerProductAndXor(D T [[2]] X, D T [[2]] Y){
    D T [[2]] Z (shape(X)[1], shape(Y)[1]);
    uint [[1]] dims (3);
    dims[0] = shape(X)[1];
    dims[1] = 1;
    dims[2] = shape(Y)[1];
    __syscall("shared3p::mat_multx_$T\_vec", __domainid(D), X, Y, Z, __cref dims);
    return Z;
}
*/
