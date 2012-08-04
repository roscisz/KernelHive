__kernel void mergeData(__global const int* input, unsigned int dataSize, unsigned int partsCount, __global int* output, unsigned int outputSize) {
 
    // Get the index of the current element to be processed
    int id = get_global_id(0);
    // Get the number of data items per processing thread
    int itemsPerThread = outputSize / get_global_size(0);
    
    for (int i = 0; i < itemsPerThread; i++) {
        output[(id*itemsPerThread)+i] = input[(id*itemsPerThread)+i];
    }        
}

