/**
 * A data merger for the MD5 calculations unroll node.
 */

__kernel void mergeData(__global unsigned char *input, unsigned int dataSize, unsigned int partsCount, __global unsigned char *output, unsigned int outputSize) {
    int wiId = get_global_id(0);
    int offset;    
    int partSize = dataSize / partsCount;      
    
    int tmp;
    int i, j;
    
    if (wiId == 0) {
        for (i = 0; i < partsCount; i++) {
            offset = i * partSize;
            tmp = (input[offset+3] << 24) + (input[offset+2] << 16) + (input[offset+1] << 8) + input[offset];
            if (tmp > 0) {
                for (j = 0; j < partSize; j++) {
                    output[j] = input[offset+j];
                }
                break;
            }
        }    
    }
}        

