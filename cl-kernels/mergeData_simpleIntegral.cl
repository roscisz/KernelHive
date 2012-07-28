// The goal of this kernel is to sum the results of earlier integral processing
__kernel void mergeData(__global const float* input, unsigned int dataSize, unsigned int partsCount, __global float* output) {
 
    // Get the index of the current element to be processed
    int id = get_global_id(0);
    // Get the number of data items per processing thread
    int itemsPerThread = dataSize / get_global_size();
    
    __global float result = 0.0;
    
    for (int i = 0; i < itemsPerThread; i++) {
        int idx = (id*itemsPerThread)+i;
        result += input[idx];
    }        
    
    output[0] = result
}

