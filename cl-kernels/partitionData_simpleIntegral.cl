// The goal of this kernel is to create a range of values which will be used to calculate an integral.
__kernel void partitionData(__global const float* input, unsigned int dataSize, unsigned int partsCount, __global float* output, unsigned int outputSize) {
 
    // Get the index of the current element to be processed
    int id = get_global_id(0);
    // Get the number of data items per processing thread
    int actualItemsCount = outputSize / sizeof(float);
    int itemsPerThread = actualItemsCount / get_global_size(0);
    
    // Get data spread bound:        
    float start = input[0];
    float end = input[1];
    
    // Get the delta:
    float delta = (end - start) / actualItemsCount;
    for (int i = 0; i < itemsPerThread; i++) {
        int idx = (id*itemsPerThread)+i;
        output[idx] = start + (idx * delta);
    } 
}

