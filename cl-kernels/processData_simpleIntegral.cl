// The goal of this kernel is to calculate the the values of the integral
__kernel void processData(__global const float* input, unsigned int dataSize, __global float* output, unsigned int outputSize) {
 
    // Get the index of the current element to be processed
    int id = get_global_id(0);
    // Get the number of data items per processing thread
    int itemsPerThread = dataSize / get_global_size();
    
    for (int i = 0; i < itemsPerThread; i++) {
        int idx = (id*itemsPerThread)+i;
        output[idx] = exp(input[idx]);
    }        
}

