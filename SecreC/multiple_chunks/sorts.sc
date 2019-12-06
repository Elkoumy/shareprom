module sorts;

import stdlib;
import shared3p;
import oblivious;
import shared3p_oblivious;
import shared3p_random;
import shared3p_sort;

import bitextr;
import permutations;
import prefixsums;
import rearrange;

//sorts public arrays that have n instances of each element 0 ... m-1
//this is like a public count sort where counts are all n
template <type T>
uint32 [[1]] sortExtendedGlistPermutation(T [[1]] x, uint m, uint n){
    uint [[1]] pos = iota(m) * n;
    uint32 [[1]] pi (m*n);
    for (uint i = 0; i < m * n; i++){
        uint elem = (uint)x[i];
        pi[i] = (uint32)pos[elem];
        pos[elem] = pos[elem] + (1 :: uint);
    }
    return pi;
}

//some modifications of Riivo sorts since the stdlib functions are not exactly what we want
//------------------------------------------------------------------------------------

//ASSUMPTION: the entries in the 'key' are all either 0 or 1

//we assume that the sorted array contains no more than 2^32 elements
template <domain D, type T, type S>
D S [[1]] countIndices (D T [[1]] key) {

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

//ASSUMPTION: the entries in the 'key' are all either 0 or 1
template <domain D, type T, type S>
D S[[2]] countIndicesPar(D T [[2]] key) {

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

//a modification of Riivo sort since the stdlib functions are not exactly what we want
//ASSUMPTION: the entries in the 'key' are all either 0 or 1
template <domain D, type T, type U>
D T[[1]] countSort(D T[[1]] value, D U [[1]] key) {

    uint n = shape(value)[0];

    D uint32[[1]] indices = countIndices(key);

    D uint8 [[1]] shuffle_key (32);
    shuffle_key = randomize(shuffle_key);

    indices = shuffle(indices, shuffle_key);
    value   = shuffle(value,   shuffle_key);

    uint[[1]] publishedIndexes = (uint)declassify(indices);
    value                      = publicPermute(value, publishedIndexes);

    return value;
}

//this is a 'bounded radix sort' that uses the bits [k1,k2)
template <domain D, type T>
D uint32 [[1]] countSortPermutation(D T [[1]] key) {

    uint n = shape(key)[0];

    //we start from the identity permutation
    uint32 [[1]] ident = (uint32)iota(n);
    D uint32 [[1]] pi = ident;

    D uint32 [[1]] indices = countIndices(key);

    D uint8 [[1]] shuffle_key (32);
    shuffle_key = randomize(shuffle_key);

    indices = shuffle(indices, shuffle_key);
    pi      = shuffle(pi,   shuffle_key);

    uint[[1]] publishedIndexes = (uint)declassify(indices);
    pi                         = publicPermute(pi, publishedIndexes);

    return pi;
}

//ASSUMPTION: the entries in the 'key' are all either 0 or 1
template <domain D, type T, type U>
D T[[2]] countSort(D T[[2]] value, D U [[1]] key) {

    uint n = shape(value)[0];
    uint l = shape(value)[1];

    D uint32[[1]] indices = countIndices(key);

    D uint8 [[1]] shuffle_key (32);
    shuffle_key = randomize(shuffle_key);

    indices = shuffle(indices, shuffle_key);
    value   = shufflePar(value,   shuffle_key);

    uint[[1]] publishedIndexes = (uint)declassify(indices);
    value                      = publicPermutePar(value, publishedIndexes);

    return value;
}

//ASSUMPTION: the entries in the 'value' are all either 0 or 1
template <domain D, type T>
D T [[1]] countSort(D T [[1]] value) {
    return countSort(value, value);
}

//ASSUMPTION: the entries in the 'column' are all either 0 or 1
template <domain D, type T>
D T[[2]] countSort(D T[[2]] matrix, uint column) {
    uint m = shape(matrix)[0];
    return countSort(matrix, myGetProj(matrix, 0 :: uint, m, column, column + (1 :: uint)));
}

//ASSUMPTION: the entries in the 'key' are all either 0 or 1
template <domain D, type T, type U>
D T[[2]] countSortPar(D T[[2]] value, D U [[2]] key) {

    uint m = shape(value)[0];
    uint n = shape(value)[1];

    D U [[2]] indices = countIndicesPar(key);

    D uint8 [[1]] shuffle_key (32);
    shuffle_key = randomize(shuffle_key);


    indices = shufflePar(indices, shuffle_key);
    value   = shufflePar(value,   shuffle_key);

    uint [[2]] publishedIndices = (uint)declassify(indices);
    D T[[2]] sorted             = publicPermutePar(value, publishedIndices);

    return sorted;
}

template <domain D, type T>
D uint32[[2]] countSortPermutationPar(D T [[2]] key) {

    uint m = shape(key)[0];
    uint n = shape(key)[1];

    //we start from the identity permutation
    uint32 [[1]] ident = (uint32)iota(n);
    uint32 [[2]] pub_pi (m, n);
    for (uint i = 0; i < m; i++){
        pub_pi[i,:] = ident;
    }
    D uint32 [[2]] pi = pub_pi;

    D uint32 [[2]] indices = countIndicesPar(key);

    D uint8 [[1]] shuffle_key (32);
    shuffle_key = randomize(shuffle_key);

    indices = shufflePar(indices, shuffle_key);
    pi      = shufflePar(pi,   shuffle_key);

    uint[[2]] publishedIndexes = (uint)declassify(indices);
    pi                         = publicPermutePar(pi, publishedIndexes);

    return pi;
}

//ASSUMPTION: the entries in the 'key' are all either 0 or 1
template <domain D, type T, type U>
D T[[3]] countSortPar(D T[[3]] value, D U [[2]] key) {

    uint m = shape(value)[0];
    uint n = shape(value)[1];
    uint l = shape(value)[2];

    D uint32[[2]] indices = countIndicesPar(key);

    D uint8 [[1]] shuffle_key (32);
    shuffle_key = randomize(shuffle_key);

    indices = shufflePar(indices, shuffle_key);
    value   = shuffleRowsPar(value,   shuffle_key);

    uint[[2]] publishedIndexes = (uint)declassify(indices);
    value                      = publicPermuteRowsPar(value, publishedIndexes);

    return sorted;
}

//ASSUMPTION: the entries in the 'value' are all either 0 or 1
template <domain D, type T>
D T [[2]] countSortPar(D T [[2]] value) {
    return countSortPar(value, value);
}

//ASSUMPTION: the entries in the 'column' are all either 0 or 1
template <domain D, type T>
D T[[3]] countSortPar(D T[[3]] matrix, uint column) {
    uint m = shape(matrix)[0];
    uint n = shape(matrix)[1];
    return countSortPar(matrix, myGetProj(matrix, 0 :: uint, m, 0 :: uint, n, column, column + (1 :: uint)));
}

//this is a 'bounded radix sort' that uses the bits [k1,k2)
template <domain D, type T>
D uint32 [[1]] boundedRadixSortPermutation(D T [[1]] key, uint k1, uint k2) {

    //bit decomposition
    uint n = shape(key)[0];
    D bool [[1]] allBits = bit_extract_all_types(key);
    D bool [[2]] keyBits = myReshape(allBits, n, size(allBits) / n);

    //we start from the identity permutation
    uint32 [[1]] ident = (uint32)iota(n);
    D uint32 [[1]] pi = ident;

    //now sort sequentially k times by each bit
    for (uint i = k1; i < k2; i++){
        D bool [[1]] bits = applyPrivatePermutation(myGetProj(keyBits, 0 :: uint, n, i, i + 1), pi);
        D uint32 [[1]] indices = countIndices(bits);

        D uint8 [[1]] shuffle_key (32);
        shuffle_key = randomize(shuffle_key);

        indices = shuffle(indices, shuffle_key);
        pi      = shuffle(pi,   shuffle_key);

        uint[[1]] publishedIndexes = (uint)declassify(indices);
        pi                         = publicPermute(pi, publishedIndexes);

    }
    return pi;
}

//parallelized radix sort
template <domain D, type T>
D uint32 [[2]] boundedRadixSortPermutationPar(D T[[2]] key, uint k1, uint k2) {

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
    for (uint i = k1; i < k2; i++){
        D bool [[2]] bits = applyPrivatePermutationPar(myGetProj(keyBits, 0 :: uint, m, 0 :: uint, n, i, i + 1), pi);
        D uint32 [[2]] indices = countIndicesPar(bits);

        D uint8 [[1]] shuffle_key (32);
        shuffle_key = randomize(shuffle_key);

        indices = shufflePar(indices, shuffle_key);
        pi      = shufflePar(pi,   shuffle_key);

        uint[[2]] publishedIndexes = (uint)declassify(indices);
        pi                         = publicPermutePar(pi, publishedIndexes);

    }
    return pi;
}

//if only one index k is provided, take up to k LSB bits
template <domain D, type T>
D uint32 [[1]] boundedRadixSortPermutation(D T[[1]] key, uint k) {
    return boundedRadixSortPermutation(key, 0 :: uint, k);
}

template <domain D, type T>
D uint32 [[2]] boundedRadixSortPermutationPar(D T[[2]] key, uint k) {
    return boundedRadixSortPermutationPar(key, 0 :: uint, k);
}

//if keys and values are provided, sort the key only, and then apply the sorting permutation
template <domain D, type T, type U, dim N>
D T [[N]] boundedRadixSort(D T [[N]] value, D U [[1]] key, uint k) {
    D uint32 [[1]] pi = boundedRadixSortPermutation(key, 0 :: uint, k);
    return applyPrivatePermutation(value, pi);
}

template <domain D, type T, type U, dim N>
D T[[N]] boundedRadixSortPar(D T[[N]] value, D U [[2]] key, uint k) {
    D uint32 [[2]] pi = boundedRadixSortPermutationPar(key, 0 :: uint, k);
    return applyPrivatePermutationPar(value, pi);
}

//if only the values are given, then they are the keys
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
    return boundedRadixSort(matrix, myGetProj(matrix, 0 :: uint, m, column, column + (1 :: uint)), k);
}

template <domain D, type T>
D T[[3]] boundedRadixSortPar(D T[[3]] matrix, uint column, uint k) {
    uint m = shape(matrix)[0];
    uint n = shape(matrix)[1];
    return boundedRadixSortPar(matrix, myGetProj(matrix, 0 :: uint, m, 0 :: uint, n, column, column + (1 :: uint)), k);
}

//key1 has higher priority than key2
template <domain D, type T, type S>
uint [[1]] qsort_permutation (D T [[1]] key1, D S [[1]] key2, uint [[1]] starts, uint [[1]] ends) {

    assert(size(key1) == size(key2));
    assert(size(starts) == size(ends));

    uint n = size(key1);
    uint [[1]] num = iota(n);
    uint [[1]] sigma = iota(n);

    //the work should definitely finish in n iterations, we break if it happens earlier
    for (uint iteration = 0; iteration < n; iteration++) {

        //D uint32 [[1]] rnd (size(starts));
        //rnd = randomize(rnd);

        //uint [[1]] pivots = (uint)declassify(rnd);
        //pivots = pivots % (ends - starts);

        uint [[1]] pivots = ends - starts - 1;

        D T [[1]] pivots_cmp_1  = partialRearrange(key1, copy_indices(starts + pivots, ends - starts - 1));
        D T [[1]] others_cmp_1  = partialRearrange(key1, discard_mth(pivots, starts, ends, n));

        D S [[1]] pivots_cmp_2  = partialRearrange(key2, copy_indices(starts + pivots, ends - starts - 1));
        D S [[1]] others_cmp_2  = partialRearrange(key2, discard_mth(pivots, starts, ends, n));

        //print(size(pivots_cmp_1));
        //print(size(others_cmp_1));

        uint [[1]] others_indices = partialRearrange(num, discard_mth(pivots, starts, ends, n));

        bool [[1]] cmp_res = declassify((pivots_cmp_1 > others_cmp_1) || (pivots_cmp_1 == others_cmp_1) && (pivots_cmp_2 > others_cmp_2));
        bool [[1]] res (n);

        res = partialRearrange(cmp_res, res, others_indices);

        uint [[1]] count_smaller = count_bits(res, starts, ends);
        uint [[1]] middle = starts + count_smaller;

        uint [[1]] smaller_source = sublist_from_bitmask(num, res);
        uint [[1]] pivot_source   = starts + pivots;
        uint [[1]] larger_source  = sublist_from_bitmask(num, !res && discard_mth_mask(pivots, starts, ends, n));

        uint [[1]] smaller_target = filter_indices(starts, middle);
        uint [[1]] pivot_target   = middle;
        uint [[1]] larger_target  = filter_indices(middle + 1, ends);

        key1 = partialRearrange(key1, key1, cat(cat(smaller_source, pivot_source), larger_source), cat(cat(smaller_target, pivot_target), larger_target));
        key2 = partialRearrange(key2, key2, cat(cat(smaller_source, pivot_source), larger_source), cat(cat(smaller_target, pivot_target), larger_target));
        sigma = partialRearrange(sigma, sigma, cat(cat(smaller_source, pivot_source), larger_source), cat(cat(smaller_target, pivot_target), larger_target));

        uint [[1]] new_starts(size(starts)*2);
        uint [[1]] new_ends(size(ends)*2);

        uint l = 0;
        for (uint j = 0; j < size(starts); j++){
            if ((int)(middle[j] - starts[j]) > 1){
                new_starts[l]   = starts[j];
                new_ends[l]     = middle[j];
                l = l + 1;
            }
            if ((int)(ends[j] - (middle[j] + 1)) > 1){
                new_starts[l]   = middle[j] + 1;
                new_ends[l]     = ends[j];
                l = l + 1;
            }
        }

        print(tostring(iteration) + " " + tostring(l));
        //if (l < 50){
        //  printArray(new_starts);
        //  printArray(new_ends);
        //}

        if (l == 0) return sigma;
        starts = new_starts[:l];
        ends = new_ends[:l];
    }
    return sigma;
}

template <domain D, type T>
D T[[1]] quickSort (D T[[1]] X) {

    uint64 [[1]] blocks = {0 :: uint64, size(X)};
    uint64 [[1]] sigma  = iota(size(X));

    D xor_uint64 [[1]] dummy = sigma;

    D uint8 [[1]] key(32);
    key = randomize(key);

    X     = shuffle(X,key);
    dummy = shuffle(dummy,key);

    __syscall("shared3p::stable_sort_$T\_vec", __domainid(D), X, dummy, __cref blocks, __ref sigma);

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

    __syscall("shared3p::stable_sort_$T\_vec", __domainid(D), X, dummy, __cref blocks, __ref sigma);

    pi = applyPermutation(pi, sigma);
    return pi;
}


//for simplicity, this sorts all the matrices according to the same columns
//3rd dimension: 0 - index, 1 - value
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

    //If this place fails, possibly need to put "ascend" after "dummy"
    //bool ascend = true;
    __syscall("shared3p::stable_sort_$T\_vec", __domainid(D), idx_flat, dummy, __cref blocks, __ref sigma);

    val_flat = applyPermutation(val_flat, sigma);
    val = myReshape(val_flat, m, n);

    return val;
}

//for simplicity, this sorts all the matrices according to the same columns
//3rd dimension: 0 - index, 1 - value
template <domain D, type T>
D T[[3]] quickSortPar (D T[[3]] matrices) {

print("--local");
    D uint8 [[1]] key(32);
    key = randomize(key);

    D T [[2]] idx = myGetProj(matrices, 0 :: uint, shape(matrices)[0], 0 :: uint, shape(matrices)[1], 0 :: uint, 1 :: uint);
    D T [[2]] val = myGetProj(matrices, 0 :: uint, shape(matrices)[0], 0 :: uint, shape(matrices)[1], 1 :: uint, 2 :: uint);

    uint [[2]] pub_dummy = reshape(iota(shape(matrices)[0] * shape(matrices)[1]), shape(matrices)[0], shape(matrices)[1]);
    D xor_uint64 [[2]] dummy = pub_dummy;

print("--shuffle");
    idx   = shufflePar(idx,key);
    val   = shufflePar(val,key);
    dummy = shufflePar(dummy,key);

print("--local");
    D T [[1]] idx_flat = myFlatten(idx);
    D T [[1]] val_flat = myFlatten(val);
    uint64 [[1]] blocks = iota(shape(matrices)[0] + 1) * shape(matrices)[1];
    uint64 [[1]] sigma  = iota(shape(matrices)[0] * shape(matrices)[1]);

print("--sort");
    __syscall("shared3p::stable_sort_$T\_vec", __domainid(D), idx_flat, dummy, __cref blocks, __ref sigma);

print("--unshuffle");
    idx_flat = applyPermutation(idx_flat, sigma);
    val_flat = applyPermutation(val_flat, sigma);

    idx = myReshape(idx_flat, shape(matrices)[0], shape(matrices)[1]);
    val = myReshape(val_flat, shape(matrices)[0], shape(matrices)[1]);
   
    //idx = inverseShufflePar(idx,key);
    //val = inverseShufflePar(idx,key); 

print("--local");
    matrices = mySetProj(idx, matrices, 0 :: uint, shape(matrices)[0], 0 :: uint, shape(matrices)[1], 0 :: uint, 1 :: uint);
    matrices = mySetProj(val, matrices, 0 :: uint, shape(matrices)[0], 0 :: uint, shape(matrices)[1], 1 :: uint, 2 :: uint);

print("--end");
    return matrices;
}
//-----------------------------------------------------------------------
// Sorting network related sorts

uint[[1]] generateMergingNetwork (uint arraysize) {
    uint snsize = 0;
    __syscall("MergingNetwork_serializedSize", arraysize, __return snsize);
    uint[[1]] sn (snsize);
    __syscall("MergingNetwork_serialize", arraysize, __ref sn);
    return sn;
}

//for simplicity, this sorts all the matrices according to the same columns
template <domain D, type T>
D T[[3]] mergingNetworkSortPar (D T[[3]] matrices, uint column) {
    uint[[1]] matShape = shape(matrices);

    // Generate sorting network
    uint[[1]] sortnet = generateMergingNetwork (matShape[1]/2);

    // We will use this offset to decode the sorting network
    uint offset = 0;

    // Extract the number of stages
    uint numOfStages = sortnet[offset++];
    //print("merge numOfStages: " + tostring(numOfStages));

    for (uint stage = 0; stage < numOfStages; stage++) {
        uint sizeOfStage = sortnet[offset++];
        //print("sizeOfStage " + tostring(stage) + ": " + tostring(sizeOfStage));

        D T[[3]] firstVectors  (matShape[0], sizeOfStage, matShape[2]);
        D T[[3]] secondVectors (matShape[0], sizeOfStage, matShape[2]);
        D bool [[2]] exchangeFlagsVectors (matShape[0], sizeOfStage);

        uint32 [[1]] source (matShape[0]*sizeOfStage*matShape[2]);
        uint32 [[1]] target (matShape[0]*sizeOfStage*matShape[2]);

        // Set up first comparison vector
        uint l = 0;
        for (uint j = 0; j < sizeOfStage; ++j) {
          for (uint i = 0; i < matShape[0]; ++i) {
            for (uint k = 0; k < matShape[2]; ++k) {
              source[l] = (uint32)(i * matShape[1] * matShape[2] + sortnet[offset] * matShape[2] + k);
              target[l] = (uint32)(i * sizeOfStage * matShape[2] + j * matShape[2] + k);
              l = l + 1;
            }
          }
          offset++;
        }
        firstVectors = partialRearrange(matrices, firstVectors, source, target);

        // Set up second comparison vector
        l = 0;
        for (uint j = 0; j < sizeOfStage; ++j) {
          for (uint i = 0; i < matShape[0]; ++i) {
            for (uint k = 0; k < matShape[2]; ++k) {
              source[l] = (uint32)(i * matShape[1] * matShape[2] + sortnet[offset] * matShape[2] + k);
              l = l + 1;
            }
          }
          offset++;
        }
        secondVectors = partialRearrange(matrices, secondVectors, source, target);

        // Perform compares
        D T [[2]]  firstVectorsProjection (matShape[0], sizeOfStage);
        D T [[2]] secondVectorsProjection (matShape[0], sizeOfStage);
        target = iota(matShape[0] * sizeOfStage);
        source = iota(matShape[0] * sizeOfStage) * (uint32)(matShape[2]) + (uint32)column;
 
        firstVectorsProjection  = partialRearrange(firstVectors,  firstVectorsProjection,  source, target);
        secondVectorsProjection = partialRearrange(secondVectors, secondVectorsProjection, source, target);

        exchangeFlagsVectors = firstVectorsProjection <= secondVectorsProjection;

        D bool[[3]] expandedExchangeFlagsVectors (matShape[0], 2 * sizeOfStage, matShape[2]);

        source = reshape(0,matShape[0]*2*sizeOfStage*matShape[2]);
        target = reshape(0,matShape[0]*2*sizeOfStage*matShape[2]);

        uint counter = 0;
        l = 0;
        for(uint j = 0; j < 2 * sizeOfStage; j = j + 2){
          for(uint i = 0; i < matShape[0]; i++){
            for(uint k = 0; k < matShape[2]; k++){
              source[l]   = (uint32)(i * sizeOfStage + counter); 
              source[l+1] = (uint32)(i * sizeOfStage + counter);
              target[l]   = (uint32)(i * 2 * sizeOfStage * matShape[2] +     j * matShape[2] + k);
              target[l+1] = (uint32)(i * 2 * sizeOfStage * matShape[2] + (j+1) * matShape[2] + k);
              l = l + 2;
            }
          }
          counter++;
        }

        expandedExchangeFlagsVectors = partialRearrange(exchangeFlagsVectors, expandedExchangeFlagsVectors, source, target);

        // Perform exchanges
        D T[[3]] firstFactors (matShape[0], 2 * sizeOfStage, matShape[2]);
        D T[[3]] secondFactors (matShape[0], 2 * sizeOfStage, matShape[2]);

        source = reshape(0, matShape[0]*sizeOfStage*matShape[2]);
        uint32 [[1]] target1 (matShape[0]*sizeOfStage*matShape[2]);
        uint32 [[1]] target2 (matShape[0]*sizeOfStage*matShape[2]);

        counter = 0;
        l = 0;
        for (uint j = 0; j < 2 * sizeOfStage; j = j + 2) {
          for(uint i = 0; i < matShape[0]; i++){
            for(uint k = 0; k < matShape[2]; k++){
              source[l]   = (uint32)(i * sizeOfStage * matShape[2] + counter * matShape[2] + k);
              target1[l]   = (uint32)(i * 2 * sizeOfStage * matShape[2] +     j * matShape[2] + k);
              target2[l]   = (uint32)(i * 2 * sizeOfStage * matShape[2] + (j+1) * matShape[2] + k);
              l = l + 1;
            }
          }
          counter++;
        }

        firstFactors = partialRearrange(firstVectors,  firstFactors,  source, target1);
        firstFactors = partialRearrange(secondVectors, firstFactors,  source, target2);
        secondFactors = partialRearrange(secondVectors, secondFactors, source, target1);
        secondFactors = partialRearrange(firstVectors,  secondFactors, source, target2);

        // Run the largest multiplication this side of Dantoiine
        D T[[3]] choiceResults (matShape[0], 2 * sizeOfStage, matShape[2]);

        choiceResults = choose(expandedExchangeFlagsVectors,firstFactors,secondFactors);

        // Finalize oblivious choices
        source = reshape(0, matShape[0]*2*sizeOfStage*matShape[2]);
        target = reshape(0, matShape[0]*2*sizeOfStage*matShape[2]);
        l = 0;
        for (uint j = 0; j < 2 * sizeOfStage; j = j + 2) {
          for (uint i = 0; i < matShape[0]; i++) {
            for (uint k = 0; k < matShape[2]; k++) {
              source[l] = (uint32)(i * 2 * sizeOfStage * matShape[2] +           j * matShape[2] + k);
              target[l] = (uint32)(i * matShape[1] * matShape[2] + sortnet[offset] * matShape[2] + k);
              l = l + 1;
            }
          }
          offset = offset + 1;
        }
        for (uint j = 1; j < (2 * sizeOfStage + 1); j = j + 2) {
          for (uint i = 0; i < matShape[0]; i++) {
            for (uint k = 0; k < matShape[2]; k++) {
              source[l] = (uint32)(i * 2 * sizeOfStage * matShape[2] +           j * matShape[2] + k);
              target[l] = (uint32)(i * matShape[1] * matShape[2] + sortnet[offset] * matShape[2] + k);
              l = l + 1;
            }
          }
          offset = offset + 1;
        }
        matrices = partialRearrange(choiceResults, matrices, source, target);

    }
    return matrices;
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
    //print("sortnet numOfStages: " + tostring(numOfStages));

    for (uint stage = 0; stage < numOfStages; stage++) {

        uint sizeOfStage = sortnet[offset++];
        //print("sizeOfStage " + tostring(stage) + ": " + tostring(sizeOfStage));

        D T[[2]] firstVector (n,sizeOfStage);
        D T[[2]] secondVector (n,sizeOfStage);
        D bool[[2]] exchangeFlagsVector (n,sizeOfStage);

        firstVector = invPublicPermuteCols(arrays,sortnet[offset : offset + sizeOfStage]);
        offset = offset + sizeOfStage;

        secondVector = invPublicPermuteCols(arrays,sortnet[offset : offset + sizeOfStage]);
        offset = offset + sizeOfStage;

        // Perform compares
        exchangeFlagsVector = firstVector <= secondVector;
        D bool[[2]] expandedExchangeFlagsVector (n,2 * sizeOfStage);
        uint counter = 0;

        uint [[1]] indices = iota(shape(exchangeFlagsVector)[1])[counter : counter + sizeOfStage];
        expandedExchangeFlagsVector = invPublicPermuteOddEvenCols(exchangeFlagsVector, expandedExchangeFlagsVector, indices, 0 :: uint);
        expandedExchangeFlagsVector = invPublicPermuteOddEvenCols(exchangeFlagsVector, expandedExchangeFlagsVector, indices, 1 :: uint);
        counter = counter + sizeOfStage;

        // Perform exchanges
        D T[[2]] firstFactor (n,2 * sizeOfStage);
        D T[[2]] secondFactor (n,2 * sizeOfStage);

        counter = 0;

        indices = iota(shape(firstVector)[1])[counter : counter + sizeOfStage];

        firstFactor = invPublicPermuteOddEvenCols(firstVector, firstFactor, indices, 0 :: uint);
        firstFactor = invPublicPermuteOddEvenCols(secondVector, firstFactor, indices, 1 :: uint);

        secondFactor = invPublicPermuteOddEvenCols(secondVector, secondFactor, indices, 0 :: uint);
        secondFactor = invPublicPermuteOddEvenCols(firstVector, secondFactor, indices, 1 :: uint);

        counter = counter + sizeOfStage;

        D T[[2]] choiceResults (n,2 * sizeOfStage);

        choiceResults = choose(expandedExchangeFlagsVector,firstFactor,secondFactor);

        // Finalize oblivious choices
        indices = sortnet[offset : offset + sizeOfStage];
        arrays = publicPermuteOddEvenCols(choiceResults, arrays, indices, 0 :: uint);
        offset = offset + sizeOfStage;
        indices = sortnet[offset : offset + sizeOfStage];
        arrays = publicPermuteOddEvenCols(choiceResults, arrays, indices, 1 :: uint);
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
    //print("sortnet numOfStages: " + tostring(numOfStages));

    for (uint stage = 0; stage < numOfStages; stage++) {

        uint sizeOfStage = sortnet[offset++];
        //print("sizeOfStage " + tostring(stage) + ": " + tostring(sizeOfStage));

        uint [[1]] source = sortnet[offset : offset + sizeOfStage];
        D T[[2]] first1 = invPublicPermuteCols(vector, source);
        D T[[2]] first2 = invPublicPermuteCols(indices, source);
        D T[[2]] first = MyCat(first1, first2, 1);
        offset = offset + sizeOfStage;

        source = sortnet[offset : offset + sizeOfStage];
        D T[[2]] second1 = invPublicPermuteCols(vector, source);
        D T[[2]] second2 = invPublicPermuteCols(indices, source);
        D T[[2]] second = MyCat(second1, second2, 1);
        offset = offset + sizeOfStage;

        D bool[[2]] exchangeFlagsVector = first[:,:sizeOfStage] <= second[:,:sizeOfStage];
        //exchangeFlagsVector = cat(exchangeFlagsVector, exchangeFlagsVector,1);
        exchangeFlagsVector = MyCat(exchangeFlagsVector, exchangeFlagsVector,1);

        D T[[2]] results  = choose(exchangeFlagsVector, first, second);

        second = results ^ first ^ second;
        first = results;

        uint [[1]] target = sortnet[offset : offset + sizeOfStage];
        first1 = mySlice(first, 0 :: uint, m, 0 :: uint, sizeOfStage);
        first2 = mySlice(first, 0 :: uint, m, sizeOfStage, (2 :: uint) * sizeOfStage);
        vector = publicPermuteCols(first1, vector, target);
        indices = publicPermuteCols(first2, indices, target);
        offset = offset + sizeOfStage;

        target = sortnet[offset : offset + sizeOfStage];
        second1 = mySlice(second, 0 :: uint, m, 0 :: uint, sizeOfStage);
        second2 = mySlice(second, 0 :: uint, m, sizeOfStage, (2 :: uint) * sizeOfStage);
        vector = publicPermuteCols(second1, vector, target);
        indices = publicPermuteCols(second2, indices, target);
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
    D xor_uint16[[2]] columnToSort = reshare(shuffledMatrix[:,:,column]);

    
    uint[[2]] publicIndices (m, shape(columnToSort)[1]);
    for (uint i = 0; i < m; i++){
        publicIndices[i,:] = iota(shape(columnToSort)[1]);
    }
    D xor_uint16[[2]] indexVector = (uint16) publicIndices;
    indexVector = _sortingNetworkSortPar(columnToSort, indexVector);
    publicIndices = (uint) declassify(indexVector);

    D uint16[[3]] out = invPublicPermuteRowsPar(shuffledMatrix, publicIndices);
    return out;
}

template <domain D>
D uint32[[3]] sortingNetworkSortPar (D uint32[[3]] matrix, uint column) {
    uint m = shape(matrix)[0];
    if (shape(matrix)[1] <= 1)
        return matrix;

    D uint32[[3]] shuffledMatrix (m, shape(matrix)[1],shape(matrix)[2]);
    shuffledMatrix = shuffleRowsPar(matrix);
    D xor_uint32[[2]] columnToSort = reshare(shuffledMatrix[:,:,column]);

    
    uint[[2]] publicIndices (m, shape(columnToSort)[1]);
    for (uint i = 0; i < m; i++){
        publicIndices[i,:] = iota(shape(columnToSort)[1]);
    }
    D xor_uint32[[2]] indexVector = (uint32) publicIndices;
    indexVector = _sortingNetworkSortPar(columnToSort, indexVector);
    publicIndices = (uint) declassify(indexVector);

    D uint32[[3]] out = invPublicPermuteRowsPar(shuffledMatrix, publicIndices);

    return out;
}

template <domain D>
D uint64[[3]] sortingNetworkSortPar (D uint64[[3]] matrix, uint column) {
    uint m = shape(matrix)[0];
    if (shape(matrix)[1] <= 1)
        return matrix;

    D uint64[[3]] shuffledMatrix (m, shape(matrix)[1],shape(matrix)[2]);
    shuffledMatrix = shuffleRowsPar(matrix);
    D xor_uint64[[2]] columnToSort = reshare(shuffledMatrix[:,:,column]);

    
    uint[[2]] publicIndices (m, shape(columnToSort)[1]);
    for (uint i = 0; i < m; i++){
        publicIndices[i,:] = iota(shape(columnToSort)[1]);
    }
    D xor_uint64[[2]] indexVector = (uint64) publicIndices;
    indexVector = _sortingNetworkSortPar(columnToSort, indexVector);
    publicIndices = (uint) declassify(indexVector);

    D uint64[[3]] out = invPublicPermuteRowsPar(shuffledMatrix, publicIndices);

    return out;
}

template <domain D>
D uint8[[3]] sortingNetworkSortPar (D uint8[[3]] matrix, uint column) {
    uint m = shape(matrix)[0];
    if (shape(matrix)[1] <= 1)
        return matrix;

    D uint8[[3]] shuffledMatrix (m, shape(matrix)[1],shape(matrix)[2]);
    shuffledMatrix = shuffleRowsPar(matrix);
    D xor_uint8[[2]] columnToSort = reshare(shuffledMatrix[:,:,column]);

    
    uint[[2]] publicIndices (m, shape(columnToSort)[1]);
    for (uint i = 0; i < m; i++){
        publicIndices[i,:] = iota(shape(columnToSort)[1]);
    }
    D xor_uint8[[2]] indexVector = (uint8) publicIndices;
    indexVector = _sortingNetworkSortPar(columnToSort, indexVector);
    publicIndices = (uint) declassify(indexVector);

    D uint8[[3]] out = invPublicPermuteRowsPar(shuffledMatrix, publicIndices);

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
    vector = cat(vector,padding,1);
    indices = cat(indices,padding,1);

    uint[[1]] sortnet = generateTopKSortingNetwork (n, k);
    uint offset = 0;
    uint numOfStages = sortnet[offset++];
    print("selectKnet numOfStages: " + tostring(numOfStages));

    for (uint stage = 0; stage < numOfStages; stage++) {

        uint sizeOfStage = sortnet[offset++];
        print("sizeOfStage " + tostring(stage) + ": " + tostring(sizeOfStage));

        uint [[1]] source1 (sizeOfStage);
        uint [[1]] source2 (sizeOfStage);

        for (uint i = 0; i < sizeOfStage; ++i) {
            source1[i] = sortnet[offset+0];
            source2[i] = sortnet[offset+1];
            offset += 2;
        }

        D T[[2]] first1 = invPublicPermuteCols(vector, source1);
        D T[[2]] first2 = invPublicPermuteCols(indices, source1);
        D T[[2]] second1 = invPublicPermuteCols(vector, source2);
        D T[[2]] second2 = invPublicPermuteCols(indices, source2);
        D T[[2]] first = MyCat(first1, first2, 1);
        D T[[2]] second = MyCat(second1, second2, 1);


        D bool[[2]] exchangeFlagsVector = first[:,:sizeOfStage] <= second[:,:sizeOfStage];
        exchangeFlagsVector = MyCat (exchangeFlagsVector, exchangeFlagsVector,1);

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
        first1  = mySlice(first, 0 :: uint, m, 0 :: uint, sizeOfStage);
        first2  = mySlice(first, 0 :: uint, m, sizeOfStage, (2 :: uint) * sizeOfStage);
        second1 = mySlice(second, 0 :: uint, m, 0 :: uint, sizeOfStage);
        second2 = mySlice(second, 0 :: uint, m, sizeOfStage, (2 :: uint) * sizeOfStage);

        vector = publicPermuteCols(first1, vector, target1);
        indices = publicPermuteCols(first2, indices, target1);
        vector = publicPermuteCols(second1, vector, target2);
        indices = publicPermuteCols(second2, indices, target2);

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
    D xor_uint32[[2]] columnToSort = reshare(shuffledMatrix[:,:,column]);

    uint[[2]] publicIndices (m, shape(columnToSort)[1]);
    for (uint i = 0; i < m; i++){
        publicIndices[i,:] = iota(shape(columnToSort)[1]);
    }
    D xor_uint32[[2]] indexVector = (uint32) publicIndices;
    indexVector = _selectKPar(columnToSort, indexVector, k, (0 :: uint32) - (1 :: uint32));
    publicIndices = (uint) declassify(indexVector);
    D uint32[[3]] out = invPublicPermuteRowsPar(shuffledMatrix, publicIndices);

    return out;
}

template <domain D>
D uint16[[3]] selectKPar (D uint16[[3]] matrix, uint k, uint column) {
    uint m = shape(matrix)[0];
    if (shape(matrix)[1] <= 1)
        return matrix;

    D uint16[[3]] shuffledMatrix (m, shape(matrix)[1],shape(matrix)[2]);
    shuffledMatrix = shuffleRowsPar(matrix);
    D xor_uint16[[2]] columnToSort = reshare(shuffledMatrix[:,:,column]);

    uint[[2]] publicIndices (m, shape(columnToSort)[1]);
    for (uint i = 0; i < m; i++){
        publicIndices[i,:] = iota(shape(columnToSort)[1]);
    }
    D xor_uint16[[2]] indexVector = (uint16) publicIndices;
    indexVector = _selectKPar(columnToSort, indexVector, k, (0 :: uint16) - (1 :: uint16));
    publicIndices = (uint) declassify(indexVector);
    D uint16[[3]] out = invPublicPermuteRowsPar(shuffledMatrix, publicIndices);

    return out;
}

template <domain D>
D uint8[[3]] selectKPar (D uint8[[3]] matrix, uint k, uint column) {
    uint m = shape(matrix)[0];
    if (shape(matrix)[1] <= 1)
        return matrix;

    D uint8[[3]] shuffledMatrix (m, shape(matrix)[1],shape(matrix)[2]);
    shuffledMatrix = shuffleRowsPar(matrix);
    D xor_uint8[[2]] columnToSort = reshare(shuffledMatrix[:,:,column]);

    uint[[2]] publicIndices (m, shape(columnToSort)[1]);
    for (uint i = 0; i < m; i++){
        publicIndices[i,:] = iota(shape(columnToSort)[1]);
    }
    D xor_uint8[[2]] indexVector = (uint8) publicIndices;
    indexVector = _selectKPar(columnToSort, indexVector, k, (0 :: uint8) - (1 :: uint8));
    publicIndices = (uint) declassify(indexVector);
    D uint8[[3]] out = invPublicPermuteRowsPar(shuffledMatrix, publicIndices);

    return out;
}


// public quicksort (parallelized iterative version)
//TODO a public sort would be faster if implemented in C++
template <type T, type S>
S [[1]] qsort (T [[1]] key, S [[1]] val) {

    uint n = size(key);

    uint [[1]] starts (1); starts = 0;
    uint [[1]] ends (1);   ends = n;

    //the work should definitely finish in n iterations,
    //we return the result if it happens earlier
    for (uint iteration = 0; iteration < n; iteration++) {

        T [[1]] new_key = key;
        S [[1]] new_val = val;

        uint [[1]] new_starts(size(starts)*2);
        uint [[1]] new_ends(size(ends)*2);

        uint l = 0;
        for (uint j = 0; j < size(starts); j++){

            uint start = starts[j];
            uint end = ends[j];
            T a = key[start];

            uint k = start;

            //smaller or equal to a
            for (uint i = start + 1; i < end; i++){
                if (key[i] <= a){
                    new_key[k] = key[i];
                    new_val[k] = val[i];
                    k++;
                }
            }

            //a itself
            uint middle = k;
            new_key[k] = key[start];
            new_val[k] = val[start];
            k++;

            //larger than a
            for (uint i = start + 1; i < end; i++){
                if (key[i] > a){
                    new_key[k] = key[i];
                    new_val[k] = val[i];
                    k++;
               }
            }

            //the new segment starts and ends to process on the next iteration
            if (middle - starts[j] > 1){
                new_starts[l]   = starts[j];
                new_ends[l]     = middle;
                l = l + 1;
            }
            if (ends[j] - (middle + 1) > 1){
                new_starts[l]   = middle + 1;
                new_ends[l]     = ends[j];
                l = l + 1;
            }
        }
        //proceed to the next iteration if there is anything to sort yet
        if (l == 0) return new_val;

        key = new_key;
        val = new_val;

        starts = new_starts[:l];
        ends   = new_ends[:l];
    }
    return val;
}

//sorting permutation of public data
template <type T>
uint[[1]] sorting_permutation(T[[1]] vec) {
    uint [[1]] pi = iota(size(vec));
    return (uint)inversePermutation(qsort(vec,pi));
}

