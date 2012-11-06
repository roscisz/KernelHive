/**
 * A data partitioner for the MD5 calculations unroll node.
 */
 
#define DIGEST_LEN 16
#define LONG_LEN sizeof(long)

__kernel void partitionData(__global unsigned char *input, unsigned int dataSize, unsigned int partsCount, __global unsigned char *output, unsigned int outputSize) {
    int wiId = get_global_id(0);
    int offset;
    
    unsigned char digest[DIGEST_LEN];
    unsigned char fromTmp[LONG_LEN], toTmp[LONG_LEN];
    long from, to;
    long subRangeLen;
    int reminder;
    
    int i, j;
    long tmp;
    
    for (i = 0; i < DIGEST_LEN; i++) {
        digest[i] = input[i];
    }
    for (i = 0; i < LONG_LEN; i++) {
        fromTmp[i] = input[i+DIGEST_LEN];
        toTmp[i] = input[i+DIGEST_LEN+LONG_LEN];
    }
    from = *((long *)fromTmp);
    to = *((long *)toTmp);
    
    subRangeLen = (to - from) / partsCount;
    reminder = (to - from) % partsCount;
    
    if (wiId == 0) {
        for (i = 0; i < partsCount; i++) {
            offset = i * dataSize;
            printf("[%d] offset: %d\n", wiId, offset);
            for (j = 0; j < DIGEST_LEN; j++) {
                output[offset + j] = digest[j];
            }           
            tmp = from + (subRangeLen * i);
            *((long *)fromTmp) = tmp;
            printf("[%d] from: %ld\n", i, tmp);
            tmp = from + (subRangeLen * (i + 1)) + reminder;
            *((long *)toTmp) = tmp;
            printf("[%d] to: %ld\n", i, tmp);
            for (j = 0; j < LONG_LEN; j++) {
                output[j+offset+DIGEST_LEN] = fromTmp[j];
                output[j+offset+DIGEST_LEN+LONG_LEN] = toTmp[j];
            }
        }        
    }
}

