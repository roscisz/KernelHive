__kernel void partitionData(__global const int* input, unsigned int dataSize, unsigned int partsCount, __global int* output) {
 
    // Get the index of the current element to be processed
    int id = get_global_id(0);
    // Get the number of data items per processing thread
    int itemsPerThread = dataSize / get_global_size(0);
    
    for (int i = 0; i < itemsPerThread; i++) {
        output[(id*itemsPerThread)+i] = input[(id*itemsPerThread)+i];
    }        
}

