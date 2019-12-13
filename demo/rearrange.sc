module rearrange;

import stdlib;
import shared3p;

//this module consists only of local operations!

//some public lists of certain form
uint [[1]] iota(uint n){
    uint [[1]] list (n);
    for (uint i = 0; i < n; i++){
        list[i] = i;
    }
    return list;
}

template <type T>
T [[1]] glistDec(uint a, uint b){
    T [[1]] list (a-b);
    for (uint i = 0; i < (a-b); i++){
        list[i] = (T)(a - i);
    }
    return list;
}

template <type T>
T [[1]] glist(uint a, uint b){
    T [[1]] list (b-a);
    for (uint i = 0; i < (b-a); i++){
        list[i] = (T)(a + i);
    }
    return list;
}

template <type T>
T [[2]] glistsDec(uint m, uint a, uint b){
    T [[2]] list (m, a-b);
    for (uint i = 0; i < m; i++){
      for (uint j = 0; j < (a-b); j++){
        list[i,j] = (T)(a - j);
      }
    }
    return list;
}

template <type T>
T [[2]] glists(uint m, uint a, uint b){
    T [[2]] list (m, b-a);
    for (uint i = 0; i < m; i++){
      for (uint j = 0; j < (b-a); j++){
        list[i,j] = (T)(a + j);
      }
    }
    return list;
}

template <type T>
T [[1]] glist_offset(uint a, uint b, uint k){
    T [[1]] list (b-a);
    for (uint i = 0; i < (b-a); i++){
        list[i] = (T)(a + i * k);
    }
    return list;
}

//-------------------------------------------------------


//the core function
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

template <domain D, type T, type S, dim N, dim M>
D T [[M]] partialRearrange(D T [[N]] a, D T [[M]] b, S [[1]] source){
    assert(size(b) >= size(source));
    S [[1]] target = (S)iota(size(source));
    b = partialRearrange(a, b, source, target);
    return b;
}

template <domain D, type T, type S, dim N>
D T [[1]] partialRearrange(D T [[N]] a, S [[1]] source, S [[1]] target){
    assert(size(source) == size(target));
    D T [[1]] b (size(target));
    b = partialRearrange(a, b, source, target);
    return b;
}

template <domain D, type T, type S, dim N>
D T [[1]] partialRearrange(D T [[N]] a, S [[1]] source){
    D T [[1]] b (size(source));
    S [[1]] target = (S)iota(size(source));
    b = partialRearrange(a, b, source, target);
    return b;
}

template <type T, type S>
T [[1]] partialRearrange(T [[1]] a, S [[1]] source){
    T [[1]] b (size(source));
    S [[1]] target = iota(size(source));
    for (uint i = 0; i < size(target); i++){
        b[(uint)target[i]] = a[source[i]];
    }
    return b;
}

template <type T, type S>
T [[1]] partialRearrange(T [[1]] a, T [[1]] b, S [[1]] source){
    assert(size(b) >= size(source));
    uint [[1]] target = iota(size(a));
    for (uint i = 0; i < size(target); i++){
        b[(uint)target[i]] = a[source[i]];
    }
    return b;
}

template <type T, type S>
T [[1]] partialRearrange(T [[1]] a, T [[1]] b, S [[1]] source, S [[1]] target){
    assert(size(source) == size(target));
    for (uint i = 0; i < size(target); i++){
        b[(uint)target[i]] = a[(uint)source[i]];
    }
    return b;
}

//some various indices rearrangements
uint [[1]] copy_indices(uint [[1]] indices, uint [[1]] counts){

    assert(size(indices) == size(counts));
    uint [[1]] res (sum(counts));
    uint k = 0;
    for (uint j = 0; j < size(indices); j++){
        for (uint i = 0; i < counts[j]; i++){
            res[k] = indices[j];
            k = k + 1;
        }
    }
    return res;
}

uint [[1]] discard_mth(uint [[1]] ms, uint [[1]] starts, uint [[1]] ends, uint n){

    assert(size(starts) == size(ends));
    assert(size(starts) == size(ms));
    uint [[1]] res (n - size(ms));
    uint k = 0;
    for (uint j = 0; j < size(starts); j++){
        for (uint i = starts[j]; i < ends[j]; i++){
            if (i != starts[j] + ms[j]){
                res[k] = i;
                k = k  + 1;
            }
        }
    }
    return res[:k];
}

bool [[1]] discard_mth_mask(uint [[1]] ms, uint [[1]] starts, uint [[1]] ends, uint n){

    assert(size(starts) == size(ends));
    assert(size(starts) == size(ms));
    bool [[1]] mask (n);
    for (uint j = 0; j < size(starts); j++){
        for (uint i = starts[j]; i < ends[j]; i++){
            if (i != starts[j] + ms[j]){
                mask[i] = true;
            }
        }
    }
    return mask;
}


uint [[1]] filter_indices (uint [[1]] starts, uint [[1]] ends){

    assert(size(starts) == size(ends));
    uint [[1]] res (sum(ends - starts));
    uint k = 0;
    for (uint j = 0; j < size(starts); j++){
        for (uint i = starts[j]; i < ends[j]; i++){
            res[k] = i;
            k = k + 1;
        }
    }
    return res;
}

uint [[1]] count_bits (bool [[1]] b, uint [[1]] starts, uint [[1]] ends){

    assert(size(starts) == size(ends));
    uint [[1]] res (size(starts));
    for (uint j = 0; j < size(starts); j++){
        for (uint i = starts[j]; i < ends[j]; i++){
            res[j] = res[j] + (uint)b[i];
        }
    }
    return res;
}

bool [[1]] span_mask (uint [[1]] starts, uint [[1]] ends, uint n){

    assert(size(starts) == size(ends));
    bool [[1]] mask (n);
    for (uint j = 0; j < size(starts); j++){
        for (uint i = starts[j]; i < ends[j]; i++){
            mask[i] = true;
        }
    }
    return mask;
}

//TODO let the argument order be like in the other two functions
template <type T>
T [[1]] sublist_from_bitmask (T [[1]] x, bool [[1]] mask){

    assert(size(mask) == size(x));
    T [[1]] res (sum(mask));
    uint k = 0;
    for (uint i = 0; i < size(mask); i++){
        if (mask[i]){
            res[k] = x[i];
            k = k + 1;
        }
    }
    return res;
}

//sublist (of lists) by a bitmask
template <domain D, type T, dim N>
D T [[N]] sublist_from_bitmask(D T [[N]] a, bool [[1]] bitmask){
    uint32 [[1]] indices (sum(bitmask));
    uint j = 0;
    for (uint i = 0; i < size(bitmask); i++){
        if (bitmask[i]){
            indices[j] = (uint32)i;
            j = j + 1;
        }
    }
    return sublist(a, indices);
}

template <type T >
T [[2]] sublist_from_bitmask(T [[2]] a, bool [[1]] bitmask){
    uint element_length = shape(a)[1];
    T [[2]] b (sum(bitmask), element_length);
    uint j = 0;
    for (uint i = 0; i < size(bitmask); i++) {
        if (bitmask[i]){
            b[j,:] = a[i,:];
            j = j + 1;
        }
    }
    return b;
}

template <domain D, type T>
D T [[1]] discardEveryNth(D T [[1]] x, uint n){
    uint m = size(x);
    uint32 [[1]] indices (m - (m / n));
    uint j = 0;
    for (uint i = 0; i < m; i++){
        if (i % n == 0){
            indices[j] = i;
            j = j + 1;
        }
    }
    return partialRearrange(x, indices);
}

uint [[1]] widen_indices (uint [[1]] indices, uint m){
    uint k = 0;
    uint [[1]] result (size(indices) * m);
    indices = indices * m;
    for (uint i = 0; i < size(indices); i++){
        for (uint j = 0; j < m; j++){
             result[k] = indices[i] + j;
             k = k + 1;
        }
    }
    return result;
}

//copy the entire block n times
//ms - differences between starting points (steps)
//ns - times to replicate
template <type T>
T [[1]] copyBlock(T [[1]] a, uint [[1]] ms, uint [[1]] ns){

    assert(size(ms) == size(ns));

    T [[1]] b (sum(ms * ns));
    uint32 [[1]] source (size(b));
    uint32 [[1]] target (size(b));

    uint l = 0;
    uint start = 0;

    for (uint k = 0; k < size(ms); k++){
        uint m = ms[k];
        uint n = ns[k];
        for (uint j = 0; j < n; j++){
            for (uint i = 0; i < m; i++){
               b[l] = a[start + i];
               l = l + 1;
           }
       }
       start = start + m;
    }
    return b;
}

template <type T, type T0, dim N>
T [[N]] copyBlock(T [[N]] a, T0 _n){
    uint n = (uint)(_n);
    uint [[1]] ms = {shape(a)[0]};
    uint [[1]] ns = {n};
    return copyBlock(a, ms, ns);
}

template <domain D, type T, type T0, dim N>
D T [[N]] copyBlock(D T [[N]] a, T0 _n){
    uint n = (uint)(_n);
    uint [[1]] ms = {shape(a)[0]};
    uint [[1]] ns = {n};
    return copyBlock(a, ms, ns);
}

template <domain D, type T>
D T [[1]] copyBlock(D T [[1]] a, uint [[1]] ms, uint [[1]] ns){
    assert(size(ms) == size(ns));
    uint [[1]] indices = iota(size(a));
    uint [[1]] source = copyBlock(indices, ms, ns);
    return partialRearrange(a, source);
}

template <domain D, type T>
D T [[2]] copyBlock(D T [[2]] a, uint [[1]] ms, uint [[1]] ns){
    assert(size(ms) == size(ns));
    uint [[1]] source = widen_indices(copyBlock(iota(shape(a)[0]), ms, ns), shape(a)[1]);
    uint [[1]] target = iota(size(source));
    D T [[2]] b (sum(ms * ns), shape(a)[1]);
    return partialRearrange(a, b, source, target);
}


//this is missing from stdlib for some reason
template <domain D, type T, dim N >
D T [[N]] reshare(D T [[N]] x){
    return x;
}

//sublist (of lists) by indices
template <domain D : shared3p, type T, type S>
D T [[1]] sublist(D T [[1]] a, S [[1]] js){
    D T [[1]] b (size(js));
    uint [[1]] source = sublist(iota(size(a)), js);
    uint [[1]] target = iota(size(b));
    b = partialRearrange(a, b, source, target);
    return b;
}

template <domain D : shared3p, type T, type S>
D T [[2]] sublist(D T [[2]] a, S [[1]] js){
    D T [[2]] b (size(js), shape(a)[1]);
    uint [[2]] indices = reshape(iota(size(a)), shape(a)[0], shape(a)[1]);
    uint [[1]] source = flatten(sublist(indices, js));
    uint [[1]] target = iota(size(b));
    b = partialRearrange(a, b, source, target);
    return b;
}

template <type T, type S>
T [[1]] sublist(T [[1]] a, S [[1]] js){
    T [[1]] b (size(js));
    for (uint i = 0; i < size(js); i++) {
        b[i] = a[(uint)js[i]];
    }
    return b;
}

template <type T, type S>
T [[2]] sublist(T [[2]] a, S [[1]] js){
    T [[2]] b (size(js), shape(a)[1]);
    for (uint i = 0; i < size(js); i++) {
        b[i,:] = a[(uint)js[i],:];
    }
    return b;
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

//equivalent of stdlib 'reshape'
//template <domain D, type T, type T1, dim N>
//D T[[1]] _Reshape (D T[[N]] X, T1 _m){
//    uint m = (uint)(_m);
//    assert(size(X) == m);
//    return partialReshape(X, m);
//}

//template <domain D, type T, type T1, type T2, dim N>
//D T[[2]] _Reshape (D T[[N]] X, T1 _m, T2 _n){
//    uint m = (uint)(_m); uint n = (uint)(_n);
//    assert(size(X) == m * n);
//    return partialReshape(X, m, n);
//}

//template <domain D, type T, type T1, type T2, type T3, dim N>
//D T[[3]] _Reshape (D T[[N]] X, T1 _m, T2 _n, T3 _o){
//    uint m = (uint)(_m); uint n = (uint)(_n); uint o = (uint)(_o);
//    assert(size(X) == m * n * o);
//    return partialReshape(X, m, n, o);
//}

//template <domain D, type T, type T1, type T2, type T3, type T4, dim N>
//D T[[4]] _Reshape (D T[[N]] X, T1 _m, T2 _n, T3 _o, T4 _p){
//    uint m = (uint)(_m); uint n = (uint)(_n); uint o = (uint)(_o); uint p = (uint)(_p);
//    assert(size(X) == m * n * o * p);
//    return partialReshape(X, m, n, o, p);
//}

//equivalent of stdlib 'flatten'
template <domain D : shared3p, type T, dim N >
D T[[1]] myFlatten (D T[[N]] X){
    D T[[1]] Y (size(X));
    Y = partialRearrange(X,Y);
    return Y;
}

//slicing indices
uint [[1]] slice_indices(uint m, uint n, uint lb1, uint ub1, uint lb2, uint ub2, uint lb3, uint ub3) {
    assert(lb2 <= m);
    assert(lb3 <= n);
    assert(ub2 <= m);
    assert(ub3 <= n);
    uint [[3]] indices = reshape(iota(ub1 * m * n), ub1, m, n);
    return flatten(indices[lb1 : ub1, lb2 : ub2, lb3 : ub3]);
}

uint [[1]] slice_indices(uint m, uint lb1, uint ub1, uint lb2, uint ub2) {
    assert(lb2 <= m);
    assert(ub2 <= m);
    uint [[2]] indices = reshape(iota(ub1 * m), ub1, m);
    return flatten(indices[lb1 : ub1, lb2 : ub2]);
}

////equivalent of matrix.sc 'transpose'
//template <domain D, type T>
//D T[[2]] _Transpose(D T[[2]] X) {
//    uint[[1]] s = shape(X);

//    uint [[1]] source (size(X));
//    uint [[1]] target = iota(size(X));

//    D T[[2]] Y(s[1], s[0]);

//    for(uint i = 0; i < s[1]; ++i) {
//      for(uint j = 0; j < s[0]; ++j) {
//          source[j + i * s[0]] = j * s[1] + i;
//      }
//    }
//    Y = partialRearrange(X,Y,source,target);
//    return Y;
//}

////this swaps the 2nd and the 3rd coordinates (transposes in parallel)
//template <domain D, type T>
//D T[[3]] _TransposePar(D T[[3]] X) {
//    uint [[1]] s = shape(X);

//    uint [[1]] source (size(X));
//    uint [[1]] target = iota(size(X));

//    D T[[3]] Y (s[0], s[2], s[1]);

//    for(uint i = 0; i < s[0]; ++i) {
//        for(uint j = 0; j < s[1]; ++j) {
//            for(uint k = 0; k < s[2]; ++k) {
//                source[k * s[1] + j + i * s[1] * s[2]] = k + j * s[2] + i * s[1] * s[2];
//            }
//        }
//    }
//    Y = partialRearrange(X,Y,source,target);
//    return Y;
//}


//reverse
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

    uint [[1]] source = myReplicate(iota(size(a)), ms, ns);
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


//equivalent of slicings ... = A[:,:]
//tried to generalize it to N, but could not express a dynamic "reshape"
template<domain D, type T, type T1, type T2, type T3, type T4, type T5, type T6 >
D T[[3]] mySlice(D T[[3]] X, T1 _lb1, T2 _ub1, T3 _lb2, T4 _ub2, T5 _lb3, T6 _ub3) {

    uint lb1 = (uint)(_lb1); uint ub1 = (uint)(_ub1);
    uint lb2 = (uint)(_lb2); uint ub2 = (uint)(_ub2);
    uint lb3 = (uint)(_lb3); uint ub3 = (uint)(_ub3);

    D T[[3]] Y (ub1 -  lb1, ub2 - lb2, ub3 - lb3);
    uint [[1]] source = slice_indices(shape(X)[1], shape(X)[2], lb1, ub1, lb2, ub2, lb3, ub3);
    uint [[1]] target = iota(size(Y));
    Y = partialRearrange(X,Y,source,target);
    return Y;
}

template<domain D, type T, type T1, type T2, type T3, type T4 >
D T[[2]] mySlice(D T[[2]] X, T1 _lb1, T2 _ub1, T3 _lb2, T4 _ub2) {

    uint lb1 = (uint)(_lb1); uint ub1 = (uint)(_ub1);
    uint lb2 = (uint)(_lb2); uint ub2 = (uint)(_ub2);

    D T[[2]] Y (ub1 -  lb1, ub2 - lb2);
    uint [[1]] source = slice_indices(shape(X)[1], lb1, ub1, lb2, ub2);
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

//equivalent of slicing assignments A[:,:] = .....
template<domain D, type T, type T1, type T2, type T3, type T4, type T5, type T6 >
D T[[3]] mySetSlice(D T[[3]] X, D T[[3]] Y, T1 _lb1, T2 _ub1, T3 _lb2, T4 _ub2, T5 _lb3, T6 _ub3) {

    uint lb1 = (uint)(_lb1); uint ub1 = (uint)(_ub1);
    uint lb2 = (uint)(_lb2); uint ub2 = (uint)(_ub2);
    uint lb3 = (uint)(_lb3); uint ub3 = (uint)(_ub3);

    uint [[1]] source = iota(size(X));
    uint [[1]] target = slice_indices(shape(Y)[1], shape(Y)[2], lb1, ub1, lb2, ub2, lb3, ub3);
    Y = partialRearrange(X,Y,source,target);
    return Y;
}

template<domain D, type T, type T1, type T2, type T3, type T4 >
D T[[2]] mySetSlice(D T[[2]] X, D T[[2]] Y, T1 _lb1, T2 _ub1, T3 _lb2, T4 _ub2) {

    uint lb1 = (uint)(_lb1); uint ub1 = (uint)(_ub1);
    uint lb2 = (uint)(_lb2); uint ub2 = (uint)(_ub2);

    uint [[1]] source = iota(size(X));
    uint [[1]] target = slice_indices(shape(Y)[1], lb1, ub1, lb2, ub2);
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

//equivalent of partial slicings ... = A[i,:]
template<domain D, type T, type T1, type T2, type T3, type T4 >
D T[[1]] myGetProj(D T[[2]] X, T1 _lb1, T2 _ub1, T3 _lb2, T4 _ub2) {

    uint lb1 = (uint)(_lb1); uint ub1 = (uint)(_ub1);
    uint lb2 = (uint)(_lb2); uint ub2 = (uint)(_ub2);

    uint [[1]] source = slice_indices(shape(X)[1], lb1, ub1, lb2, ub2);
    uint [[1]] target = iota(size(source));
    D T [[1]] Y (size(source));
    Y = partialRearrange(X,Y,source,target);
    return Y;
}

template<domain D, type T, type T1, type T2, type T3, type T4, type T5, type T6 >
D T[[2]] myGetProj(D T[[3]] X, T1 _lb1, T2 _ub1, T3 _lb2, T4 _ub2, T5 _lb3, T6 _ub3) {

    uint lb1 = (uint)(_lb1); uint ub1 = (uint)(_ub1);
    uint lb2 = (uint)(_lb2); uint ub2 = (uint)(_ub2);
    uint lb3 = (uint)(_lb3); uint ub3 = (uint)(_ub3);

    uint [[1]] source = slice_indices(shape(X)[1], shape(X)[2], lb1, ub1, lb2, ub2, lb3, ub3);
    uint [[1]] target = iota(size(source));

    D T [[2]] Y ((ub1 - lb1 == 1) ? (ub2 - lb2) : (ub1 - lb1), (ub2 - lb2 == 1) ? (ub3 - lb3) : (ub2 - lb2));

    Y = partialRearrange(X,Y,source,target);
    return Y;
}

//equivalent of partial slicing assignments A[i,:] = ....
template<domain D, dim N, type T, type T1, type T2, type T3, type T4 >
D T[[2]] mySetProj(D T[[N]] X, D T [[2]] Y, T1 _lb1, T2 _ub1, T3 _lb2, T4 _ub2) {

    uint lb1 = (uint)(_lb1); uint ub1 = (uint)(_ub1);
    uint lb2 = (uint)(_lb2); uint ub2 = (uint)(_ub2);

    uint [[1]] source = iota(size(X));
    uint [[1]] target = slice_indices(shape(Y)[1], lb1, ub1, lb2, ub2);
    Y = partialRearrange(X,Y,source,target);
    return Y;
}

template<domain D, dim N, type T, type T1, type T2, type T3, type T4, type T5, type T6 >
D T[[3]] mySetProj(D T[[N]] X, D T [[3]] Y, T1 _lb1, T2 _ub1, T3 _lb2, T4 _ub2, T5 _lb3, T6 _ub3) {

    uint lb1 = (uint)(_lb1); uint ub1 = (uint)(_ub1);
    uint lb2 = (uint)(_lb2); uint ub2 = (uint)(_ub2);
    uint lb3 = (uint)(_lb3); uint ub3 = (uint)(_ub3);

    uint [[1]] source = iota(size(X));
    uint [[1]] target = slice_indices(shape(Y)[1], shape(Y)[2], lb1, ub1, lb2, ub2, lb3, ub3);
    Y = partialRearrange(X,Y,source,target);
    return Y;
}

//TODO review this at some moment, this is a more complicated thing
//equivalent of stdlib 'cat'
template <domain D: shared3p, type T>
D T[[3]] MyCat(D T[[3]] X, D T[[3]] Y, int d) {
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

template <type T>
T[[2]] MyCat(T[[2]] X, T[[2]] Y, uint [[1]] ms, uint [[1]] ns) {

        uint k = shape(X)[1];
        T [[2]] Z (sum(ms) + sum(ns), k);
        uint lX = 0;
        uint lY = 0;
        for (uint l = 0; l < size(ns); l++){
            uint n = ns[l];
            uint m = ms[l];
            for (uint i = 0; i < m; i++){
               for (uint j = 0; j < k; j++){
                   Z[lX + lY, j] = X[lX, j];
               }
               lX = lX + 1;
            }
            for (uint i = 0; i < n; i++){
               for (uint j = 0; j < k; j++){
                   Z[lX + lY, j] = Y[lY, j];
               }
               lY = lY + 1;
            }
        }
        return Z;
}

template <domain D, type T>
D T[[1]] MyCat(D T[[1]] X, D T[[1]] Y, uint [[1]] ms, uint [[1]] ns) {

        uint32 [[1]] source_X (sum(ms));
        uint32 [[1]] source_Y (sum(ns));
        uint32 [[1]] target_X (sum(ms));
        uint32 [[1]] target_Y (sum(ns));
        uint lX = 0;
        uint lY = 0;
        for (uint l = 0; l < size(ns); l++){
            uint n = ns[l];
            uint m = ms[l];
            for (uint i = 0; i < m; i++){
                   source_X[lX] = (uint32)(lX);
                   target_X[lX] = (uint32)(lX + lY);
                   lX = lX + 1;
           }
           for (uint i = 0; i < n; i++){
                   source_Y[lY] = (uint32)(lY);
                   target_Y[lY] = (uint32)(lX + lY);
                   lY = lY + 1;
           }
        }
        D T [[1]] Z (lX + lY);
        Z = partialRearrange(X,Z,source_X,target_X);
        Z = partialRearrange(Y,Z,source_Y,target_Y);
        return Z;
}

template <domain D, type T>
D T[[2]] MyCat(D T[[2]] X, D T[[2]] Y, uint [[1]] ms, uint [[1]] ns) {

        uint k = shape(X)[1];
        uint32 [[1]] source_X (sum(ms) * k);
        uint32 [[1]] source_Y (sum(ns) * k);
        uint32 [[1]] target_X (sum(ms) * k);
        uint32 [[1]] target_Y (sum(ns) * k);
        uint lX = 0;
        uint lY = 0;
        for (uint l = 0; l < size(ns); l++){
            uint n = ns[l];
            uint m = ms[l];
            for (uint i = 0; i < m; i++){
               for (uint j = 0; j < k; j++){
                   source_X[lX * k + j] = (uint32)(lX * k + j);
                   target_X[lX * k + j] = (uint32)((lX + lY) * k + j);
               }
               lX = lX + 1;
            }
            for (uint i = 0; i < n; i++){
               for (uint j = 0; j < k; j++){
                   source_Y[lY * k + j] = (uint32)(lY * k + j);
                   target_Y[lY * k + j] = (uint32)((lX + lY) * k + j);
               }
               lY = lY + 1;
            }
        }
        D T [[2]] Z (lX + lY, k);
        Z = partialRearrange(X,Z,source_X,target_X);
        Z = partialRearrange(Y,Z,source_Y,target_Y);
        return Z;
}

template <domain D: shared3p, type T>
D T[[2]] MyCat(D T[[2]] X, D T[[2]] Y, int d) {
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
D T[[1]] MyCat(D T[[1]] X, D T[[1]] Y) {
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

//TODO test if these functions work correctly
template <domain D, type T>
D T [[1]] allSubvectors(D T [[1]] a, uint n){
    uint m = size(a) - n;
    D T [[1]] b (m * n);
    uint32 [[1]] source (m * n);
    uint32 [[1]] target (m * n);
    for (uint i = 0; i < m; i++){
       for (uint j = 0; j < n; j++){
           source[i * n + j] = (uint32)(i + j);
           target[i * n + j] = (uint32)(i * n + j);
       }
    }
    b = partialRearrange(a, b, source, target);
    return b;
}

//TODO test if these functions work correctly
template <domain D, type T, dim N>
D T [[1]] extractSegments(D T [[N]] a, uint segmentLength, uint step, uint startFrom, uint startTo, uint numOfSteps){
    uint [[1]] source (numOfSteps * segmentLength);
    uint [[1]] target (numOfSteps * segmentLength);
    uint addToSource = startFrom;
    uint addToTarget = startTo;

    uint [[1]] sourceIndices = iota(segmentLength);
    uint [[1]] targetIndices = sourceIndices;

    uint span = segmentLength + startTo;

    //for some reason, these loops on public data are slow
    for (uint i = 0; i < numOfSteps; i++){
        for (uint j = 0; j < segmentLength; j++){
            source[i * segmentLength + j] = sourceIndices[j] + addToSource;
            target[i * segmentLength + j] = targetIndices[j] + addToTarget;
        }
        addToSource = addToSource + step;
        addToTarget = addToTarget + span;
    }

    D T [[1]] b (numOfSteps * (startTo + segmentLength));
    b = partialRearrange(a, b, source, target);
    return b;
}
