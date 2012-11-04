/**
 * A data partitioner for the MD5 calculations unroll node.
 */

__kernel void partitionData(__global unsigned char *input, unsigned int dataSize, unsigned int partsCount, __global unsigned char *output, unsigned int outputSize) {

    int wiId = get_global_id(0);
    int tmpId;
    int wiCount = get_global_size(0);
    int itemsPerThread = partsCount / wiCount;
    int batch = itemsPerThread * wiCount;
    int i, j;
    
    if (itemsPerThread >= 1) {
        for (i = 0; i < itemsPerThread; i++) {
            tmpId = (wiId * itemsPerThread) + i;
        }    
    }
    if (partsCount - batch > 0) {
        tmpId = wiId + batch;
        if (tmpId <= partsCount-1) {
        
        }
    }
    
    for (int i = 0; i < itemsPerThread; i++) {
        //output[(id*itemsPerThread)+i] = input[(id*itemsPerThread)+i];
    }
}

