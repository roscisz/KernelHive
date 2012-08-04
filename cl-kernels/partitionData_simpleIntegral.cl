// The goal of this kernel is to create a range of values which will be used to calculate an integral.
__kernel void partitionData(__global const float* input, unsigned int dataSize, unsigned int partsCount, __global float* output, unsigned int outputSize) {
 
    // Get the index of the current element to be processed
    int id = get_global_id(0);
    // Get the number of data items per processing thread
    int itemsPerThread = dataSize / get_global_size();
    
    // Get data spread bound:
    float start = input[0];
    float end = input[1];
    
    // Get the delta:
    float delta = (to - from) / dataSize;
    
    for (int i = 0; i < itemsPerThread; i++) {
        int idx = (id*itemsPerThread)+i;
        output[idx] = from + (idx * delta);
    }        
}

