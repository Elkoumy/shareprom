import profiling;
import shared3p;

domain pd_shared3p shared3p;

void main() {

    //create a label for this particular test
    uint32 section = newSectionType("Testing speed of multiplication protocol");

    // run tests with different input data sizes
    uint[[1]] sizes = {10, 100, 1000, 10000};
    uint sizesCount = size(sizes);

    //run the tests for all data sizes
    for (uint i = 0; i < sizesCount; i++) {

        print("Testing vectors of size ", sizes[i]);

        pd_shared3p uint32[[1]] xs(sizes[i]);
        pd_shared3p uint32[[1]] ys(sizes[i]);

        //start measuring time
        uint32 id = startSection(section, sizes[i]);

        //the operations that we are testing
        pd_shared3p uint32[[1]] zs = xs * ys;

        //end measuring time
        endSection (id);
    }
}
