/**
 * A data merger for the MD5 calculations unroll node.
 */

__kernel void mergeData(__global unsigned char *input, unsigned int dataSize, unsigned int partsCount, __global unsigned char *output, unsigned int outputSize) {
    int wiId = get_global_id(0);
    int offset;    
    int partSize = dataSize / partsCount;   
    
    int finder[1];
    int result[2];    
    
    int tmp;
    int i, j;
    
    if (wiId == 0) {
        for (i = 0; i < partsCount; i++) {
            offset = i * partSize;
            tmp = *(((int *)input)+offset);
            if (tmp > 0) {
                for (j = 0; j < partSize; j++) {
                    output[j] = input[offset+j];
                }
                break;
            }
        }    
    }
}        

