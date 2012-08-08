/*
 * The goal of this kernel is to sum up the results of earlier integral processing,
 * reducing them to a single value which will be the approximated value.
 *
 * The kernel assumes that it's work group size will be equal to the subresults table
 * size.
 */

#define WORK_ITEMS_PER_GROUP 16 
#define WORK_GROUPS_COUNT 8

__kernel void mergeData(
    __global float* input,
    unsigned int dataSize,
    unsigned int partsCount,
    __global float* output,
    unsigned int outputSize)
{
    // Storage for local subresults:
    __local float subresults[WORK_ITEMS_PER_GROUP];
    float localResult = 0.0;
    
    // Acquire necessary indexes:
    int id = get_global_id(0);
    int groupId = get_group_id(0);
    int localId = get_local_id(0);
    // Get the number of data items per processing thread:
    int actualItemsCount = dataSize / sizeof(float);   
    int itemsPerThread = actualItemsCount / get_global_size(0);
    
    // Reset the output to 0
    if (id == 0) {
        output[0] = 0.0;
    }
    barrier(CLK_GLOBAL_MEM_FENCE);
    
    // Reset the subresults.
    subresults[localId] = 0.0;
    barrier(CLK_LOCAL_MEM_FENCE);

    // Calculate subresults for each thread:
    for (int i = 0; i < itemsPerThread; i++) {
        int idx = (id*itemsPerThread)+i;
        subresults[localId] += input[idx];
    } 
    barrier(CLK_LOCAL_MEM_FENCE);
    
    // Sum up subresults on work group level:
    for (int i = 0; i < WORK_ITEMS_PER_GROUP; i++) {
        localResult += subresults[i];
    }
    // Store the subresult in global memory:
    if (localId == 0) {
        input[groupId] = localResult;
    }
    barrier(CLK_GLOBAL_MEM_FENCE);

    // Sum up subresults provided by each work group:
    if (id == 0) {
        for (int i = 0; i < WORK_GROUPS_COUNT; i++) {        
            output[0] += input[i];
        }
    }
}

