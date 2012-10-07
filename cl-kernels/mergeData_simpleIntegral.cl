// The goal of this kernel is to sum the results of earlier integral processing
__kernel void mergeData(__global float* input, unsigned int dataSize, unsigned int partsCount, __global float* output, unsigned int outputSize) {
 
    __local float result[16];
    float localResult = 0.0;
    
    // Get the index of the current element to be processed
    int id = get_global_id(0);
    int groupId = get_group_id(0);
    int localId = get_local_id(0);
    // Get the number of data items per processing thread
    int actualItemsCount = dataSize / sizeof(float);
    
    int itemsPerThread = actualItemsCount / get_global_size(0);
    
    if (id == 0) {
        output[0] = 0.0;
    }
    barrier(CLK_GLOBAL_MEM_FENCE);
    
    result[localId] = 0.0;
    barrier(CLK_LOCAL_MEM_FENCE);

    for (int i = 0; i < itemsPerThread; i++) {
        int idx = (id*itemsPerThread)+i;
        // TODO Logging
        printf("[Kernel] chunk %d : %f\n", idx, input[idx]);
        result[localId] += input[idx];
    } 
    barrier(CLK_LOCAL_MEM_FENCE);
    
    for (int i = 0; i < 16; i++) {
        localResult += result[i];
    }
    if (localId == 0) {
        input[groupId] = localResult;
    }
    barrier(CLK_GLOBAL_MEM_FENCE);

    if (id == 0) {
        for (int i = 0; i < 8; i++) {        
            output[0] += input[i];
        }
    }
    // TODO Logging
    barrier(CLK_GLOBAL_MEM_FENCE);
    printf("[Kernel] final : %f\n", output[0]);
}

