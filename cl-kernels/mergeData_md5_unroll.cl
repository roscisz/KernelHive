/**
 * A data merger for the MD5 calculations unroll node.
 */
 
#pragma OPENCL EXTENSION cl_khr_global_int32_base_atomics : enable
#pragma OPENCL EXTENSION cl_khr_local_int32_base_atomics : enable

__kernel void mergeData(__global unsigned char *input, unsigned int dataSize, unsigned int partsCount, __global unsigned char *output, unsigned int outputSize) {
 
    int wiId = get_global_id(0);
    int tmpId;
    int offset;    
    int wiCount = get_global_size(0);
    int partSize = dataSize / partsCount;
    
    int itemsPerThread = partsCount / wiCount;
    int batch = itemsPerThread * wiCount;
    
    __local int finder[1];
    __local int result[2];    
    
    int tmp;
    int i;
    
    if (itemsPerThread >= 1) {
        for (i = 0; i < itemsPerThread; i++) {
            tmpId = (wiId * itemsPerThread) + i;
            offset = tmpId * partSize;
            tmp = *((int *)input[offset]);
            if (tmp > 0) {
                *finder = wiId;
                result[0] = offset;
                result[1] = tmp;
            }
        }    
    }
    if (partsCount - batch > 0) {
        tmpId = wiId + batch;
        offset = tmpId * partSize;
        if (tmpId <= partsCount-1) {
            tmp = *((int *)input[offset]);
            if (tmp > 0) {
                *finder = wiId;
                result[0] = offset;
                result[1] = tmp;
            }
        }
    }
    barrier(CLK_LOCAL_MEM_FENCE);
    
    if (wiId == *finder) {
        offset = result[0];
        for (i = 0; i < result[1]; i++) {
            output[i] = input[offset+i];
        }
    }    
}        

