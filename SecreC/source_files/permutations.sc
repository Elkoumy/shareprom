module permutations;

import stdlib;
import shared3p;
import shared3p_random;
import rearrange;

domain pd_shared3p shared3p;

//TODO
// publicPermuteRows is an inverse of applyPermutation, this is not very nice, they could be consistent

//a public random permutation
uint32 [[1]] public_permutation(uint n){
    uint32 [[1]] pubPerm (n);
    __syscall ("shared3p::genRandPubPerm", __domainid (pd_shared3p), __ref pubPerm);
    return pubPerm;
}

//applying public permutations
template <domain D, type T>
D T [[1]] publicPermute(D T [[1]] a, uint [[1]] indices){
    assert(size(a) == size(indices));
    uint n = shape(indices)[0];
    uint [[1]] source = iota(n);
    uint [[1]] target = indices;
    D T[[1]] b (n);
    b = partialRearrange(a, b, source, target);
    return b;
}

template <domain D, type T>
D T [[2]] publicPermutePar(D T [[2]] A, uint [[2]] indices){
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
D T [[2]] publicPermuteRows(D T [[2]] A, uint [[1]] indices){
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
D T [[3]] publicPermuteRowsPar(D T [[3]] A, uint [[2]] indices){

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
D T [[2]] publicPermuteCols(D T [[2]] A, uint [[1]] indices){

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
D T [[2]] publicPermuteCols(D T [[2]] A, D T [[2]] B, uint [[1]] indices){

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
D T [[3]] publicPermuteColsPar(D T [[3]] A, D T [[3]] B, uint [[2]] indices){

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

/////////////

template <domain D, type T>
D T [[1]] publicPermuteOddEven(D T [[1]] a, uint [[1]] indices, uint parity){
    uint n = shape(indices)[0];
    uint [[1]] source = iota(m * n) * (2 :: uint32) + (uint32)parity;
    uint [[1]] target = indices;
    D T[[1]] b (n);
    b = partialRearrange(a, b, source, target);
    return b;
}

template <domain D, type T>
D T [[2]] publicPermuteOddEvenPar(D T [[2]] A, uint [[2]] indices, uint parity){

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
D T [[2]] publicPermuteOddEvenRows(D T [[2]] A, uint [[1]] indices, uint parity){

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
D T [[3]] publicPermuteOddEvenRowsPar(D T [[3]] A, uint [[2]] indices, uint parity){

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
D T [[2]] publicPermuteOddEvenCols(D T [[2]] A, D T [[2]] B, uint [[1]] indices, uint parity){

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
D T [[3]] publicPermuteOddEvenColsPar(D T [[3]] A, D T [[3]] B, uint [[2]] indices, uint parity){

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

/////////////

template <domain D, type T>
D T [[1]] invPublicPermute(D T [[1]] a, uint [[1]] indices){
    uint n = shape(indices)[0];
    uint [[1]] source = indices;
    uint [[1]] target = iota(n);
    D T[[1]] b (n);
    b = partialRearrange(a, b, source, target);
    return b;
}

template <domain D, type T>
D T [[2]] invPublicPermutePar(D T [[2]] A, uint [[2]] indices){

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
D T [[2]] invPublicPermuteRows(D T [[2]] A, uint [[1]] indices){

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
D T [[3]] invPublicPermuteRowsPar(D T [[3]] A, uint [[2]] indices){

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
D T [[2]] invPublicPermuteCols(D T [[2]] A, uint [[1]] indices){

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
D T [[3]] invPublicPermuteColsPar(D T [[3]] A, uint [[2]] indices){

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

/////////////

template <domain D, type T>
D T [[1]] invPublicPermuteOddEven(D T [[1]] a, D T [[1]] b, uint [[1]] indices, uint parity){
    uint n = shape(indices)[0];
    uint [[1]] source = (uint32)indices;
    uint [[1]] target = iota(n / (2 :: uint)) * 2 + parity;
    b = partialRearrange(a, b, source, target);
    return b;
}

template <domain D, type T>
D T [[2]] invPublicPermuteOddEvenPar(D T [[2]] A, D T [[2]] B, uint [[2]] indices, uint parity){

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
D T [[2]] invPublicPermuteOddEvenRows(D T [[2]] A, D T [[2]] B, uint [[1]] indices, uint parity){

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
D T [[3]] invPublicPermuteOddEvenRowsPar(D T [[3]] A, D T [[3]] B, uint [[2]] indices, uint parity){

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
D T [[2]] invPublicPermuteOddEvenCols(D T [[2]] A, D T [[2]] B, uint [[1]] indices, uint parity){

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
D T [[3]] invPublicPermuteOddEvenColsPar(D T [[3]] A, D T [[3]] B, uint [[2]] indices, uint parity){

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

template <domain D, type T, type U>
D T [[1]] applyPrivatePermutation (D T [[1]] data, D U [[1]] pi){

    pd_shared3p uint8 [[1]] key(32);
    key = randomize(key);

    uint [[1]] tau = (uint)declassify(shuffle(pi, key));
    data = applyPermutation(data, tau);
    return inverseShuffle(data, key);
}

template <domain D, type T, type U>
D T [[1]] unapplyPrivatePermutation (D T [[1]] data, D U [[1]] pi){

    pd_shared3p uint8 [[1]] key(32);
    key = randomize(key);

    data = shuffle(data, key);                                                 
    uint [[1]] tau = (uint)declassify(shuffle(pi, key));
    return applyPermutation(data, inversePermutation(tau));
}

template <domain D, type T, type U>
D T [[2]] applyPrivatePermutationRows (D T [[2]] data, D U [[1]] pi){

    pd_shared3p uint8 [[1]] key(32);
    key = randomize(key);

    uint [[1]] tau = (uint)declassify(shuffle(pi, key));
    data = invPublicPermuteRows(data, tau);
    return inverseShuffleRows(data, key);
}

template <domain D, type T, type U>
D T [[2]] applyPrivatePermutationCols (D T [[2]] data, D U [[1]] pi){

    pd_shared3p uint8 [[1]] key(32);
    key = randomize(key);

    uint [[1]] tau = (uint)declassify(shuffle(pi, key));
    data = invPublicPermuteRows(myTranspose(data), tau);
    return myTranspose(inverseShuffleRows(data, key));
}

template <domain D, type T, type U>
D T [[2]] unapplyPrivatePermutationRows (D T [[2]] data, D U [[1]] pi){

    pd_shared3p uint8 [[1]] key(32);
    key = randomize(key);

    data = shuffleRows(data, key);                                                 
    uint [[1]] tau = (uint)declassify(shuffle(pi, key));
    return invPublicPermuteRows(data, inversePermutation(tau));
}

template <domain D, type T, type U>
D T [[2]] unapplyPrivatePermutationCols (D T [[2]] data, D U [[1]] pi){

    pd_shared3p uint8 [[1]] key(32);
    key = randomize(key);

    data = shuffleRows(myTranspose(data), key);                                                 
    uint [[1]] tau = (uint)declassify(shuffle(pi, key));
    return myTranspose(invPublicPermuteRows(data, inversePermutation(tau)));
}

template <domain D, type T>
D T [[1]] privatePermutationInverse (D T [[1]] pi){

    T [[1]] indices = (T)iota(size(pi));
    pd_shared3p uint8 [[1]] key(32);
    key = randomize(key);
          
    D T [[1]] PR_indices = indices;                            
    D T [[1]] rho = shuffle(PR_indices, key);           
    uint [[1]] tau = (uint)declassify(shuffle(pi, key));
    return applyPermutation(rho, inversePermutation(tau));
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
    if (inverse) return inverseShuffle(invPublicPermute(data, permutedIndices), key);
    else return publicPermute(data, permutedIndices);
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
    D uint [[1]] iotaPr = iota(m);
    D uint [[1]] privateIndices = myReplicate(iotaPr, n);
    uint [[1]] tags = (uint)declassify(shuffle(privateIndices, key));

    uint [[1]] permutedIndices (mn);
    uint [[1]] counters (m);
    for (uint i = 0; i < mn; i++){
        permutedIndices[i] = tags[i] * n + counters[tags[i]];
        counters[tags[i]]++;
    }
    matrices = myReshape(publicPermuteRows(data, permutedIndices), m, n, l);
    return matrices;

}

template <domain D, type T, dim N>
D T[[N]] applyPermutation(D T[[N]] values, uint[[1]] permutation) {
    assert(size(values) == size(permutation));
    D T[[N]] res = partialRearrange(values, values, permutation);
    return res;
}

template <type T>
T [[1]] inversePermutation (T [[1]] permutation){
    T [[1]] permutation_inverse(size(permutation));
    for (uint i = 0; i < size(permutation); i++){
        permutation_inverse[(uint)permutation[i]] = (T)i;
    }
    return permutation_inverse;
}
