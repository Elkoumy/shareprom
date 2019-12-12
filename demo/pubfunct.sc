module pubfunct;

import stdlib;
import shared3p;

//this module consists only of local operations!

uint pow(uint a, uint b){
    uint c = 1;
    for (uint i = 0; i < b; i++){
        c = c * a;
    }
    return c;
}

template <domain D, type T>
D T pow(D T a, uint b){
    D T c = 1;
    for (uint i = 0; i < b; i++){
        c = c * a;
    }
    return c;
}

template <domain D, type T>
D T [[1]] pow(D T[[1]] a, uint b){
    D T [[1]] c (shape(a)[0]);
    c = 1;
    for (uint i = 0; i < b; i++){
        c = c * a;
    }
    return c;
}

uint log2 (uint x){
    uint i;
    for (i = 0; i < sizeof(x)*8; i++){
        if (x <= 1){
            break;
        }
        x = x / 2;
    }
    return i;
}
