/**
 * A kernel used for brute force MD5 cracking.
 */
 
#define MAX_MSG_LEN 64
#define DIGEST_LEN 16

#define ONE_BIT 0x80
#define PADDING_ZEROES 0x00
 
__constant unsigned int r[64] = {
    7, 12, 17, 22, 7, 12, 17, 22, 7, 12, 17, 22, 7, 12, 17, 22,
    5,  9, 14, 20, 5,  9, 14, 20, 5,  9, 14, 20, 5,  9, 14, 20,
    4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23,
    6, 10, 15, 21, 6, 10, 15, 21, 6, 10, 15, 21, 6, 10, 15, 21
};

__constant unsigned int k[64] = {
    0xd76aa478, 0xe8c7b756, 0x242070db, 0xc1bdceee,
    0xf57c0faf, 0x4787c62a, 0xa8304613, 0xfd469501,
    0x698098d8, 0x8b44f7af, 0xffff5bb1, 0x895cd7be,
    0x6b901122, 0xfd987193, 0xa679438e, 0x49b40821,
    0xf61e2562, 0xc040b340, 0x265e5a51, 0xe9b6c7aa,
    0xd62f105d, 0x02441453, 0xd8a1e681, 0xe7d3fbc8,
    0x21e1cde6, 0xc33707d6, 0xf4d50d87, 0x455a14ed,
    0xa9e3e905, 0xfcefa3f8, 0x676f02d9, 0x8d2a4c8a,
    0xfffa3942, 0x8771f681, 0x6d9d6122, 0xfde5380c,
    0xa4beea44, 0x4bdecfa9, 0xf6bb4b60, 0xbebfbc70,
    0x289b7ec6, 0xeaa127fa, 0xd4ef3085, 0x04881d05,
    0xd9d4d039, 0xe6db99e5, 0x1fa27cf8, 0xc4ac5665,
    0xf4292244, 0x432aff97, 0xab9423a7, 0xfc93a039,
    0x655b59c3, 0x8f0ccc92, 0xffeff47d, 0x85845dd1,
    0x6fa87e4f, 0xfe2ce6e0, 0xa3014314, 0x4e0811a1,
    0xf7537e82, 0xbd3af235, 0x2ad7d2bb, 0xeb86d391
};

unsigned int rot(unsigned int x, unsigned int c) {
    return (x << c) | (x >> (32 - c));
}

void crack(unsigned char *msg, unsigned long msgLen, unsigned char *digest, unsigned int *outcome) {
    // Support variables
    unsigned int i, tmp;
    unsigned int *ptr;
    unsigned char *chrPtr;
    // Initial values for the results
    unsigned int h[4] = { 0x67452301, 0xefcdab89, 0x98badcfe, 0x10325476 };
    // Temporary results holders
    __private unsigned int a, b, c, d, e, f, g;
    
    // Reset the result:
    *outcome = 0;
    
    // Append the "1" bit to the msg
    msg[msgLen] = ONE_BIT;
    
    // Padd the msg with zeroes until 448 bits are reached    
    for (i = msgLen + 1; i < MAX_MSG_LEN - 8; i++) {
        msg[i] = PADDING_ZEROES;
    }
    // Append the original msg length to the end of the msg
    tmp = 7;
    for (; i < MAX_MSG_LEN; i++) {
        msg[i] = (msgLen >> (8 * tmp)) & 0xFF;
        tmp--;
    }
    
    a = h[0];
    b = h[1];
    c = h[2];
    d = h[3];
    
    // Perform the actual calculations, process as 32-bit chunks
    ptr = (unsigned int *)msg;
    for (i = 0; i < 16; i++) {
        f = (b & c) | ((~b) & d);
        g = i;
    }
    for (; i < 32; i++) {
        f = (d & b) | ((~d) & c);
        g = (5*i + 1) % 16;
    }
    for (; i < 47; i++) {
        f = b ^ c ^ d;
        g = (3*i + 5) % 16;
    }
    for (; i < 64; i++) {
        f = c ^ (b | (~d));
        g = (7*i) % 16;
    }
    tmp = d;
    d = c;
    c = b;
    b = b + rot((a + f + k[i] + ptr[g]), r[i]);
    a = tmp;
    
    // Add the subresults to the hash    
    h[0] = h[0] + a;
    h[1] = h[1] + b;
    h[2] = h[2] + c;
    h[3] = h[3] + d;
    
    // Check the result
    chrPtr = (unsigned char *)h;
    tmp = 1;
    for (i = 0; i < DIGEST_LEN; i++) {
        tmp = tmp & (chrPtr[i] == digest[i]);
    }
    barrier(CLK_GLOBAL_MEM_FENCE);
    // WTF
    *outcome = tmp;
}

__kernel void processData(__global unsigned char *input, unsigned int dataSize, __global unsigned char *output, unsigned int outputSize) {
    __private unsigned char msg[MAX_MSG_LEN];
    __private unsigned char digest[DIGEST_LEN];
    __private unsigned int outcome[1] = { 0 };
    crack(msg, 12, digest, outcome);
    // WARNING: Below will not run on all devices
    //printf("%02x%02x%02x%02x%02x%02x%02x%02x\n", msg[56], msg[57], msg[58], msg[59], msg[60], msg[61], msg[62], msg[63]);
}

