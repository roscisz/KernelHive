/******************************************************************************
 * A kernel used for brute force MD5 cracking.
 */
 
/**
 * Some general constants
 */
#define MAX_MSG_LEN 64
#define DIGEST_LEN 16
#define ONE_BIT 0x80
#define PADDING_ZEROES 0x00
#define LONG_SIZE sizeof(long)

/**
 * Constants required by the MD5 algorithm.
 */
#define T_MASK ((unsigned int)~0)
#define T1 /* 0xd76aa478 */ (T_MASK ^ 0x28955b87)
#define T2 /* 0xe8c7b756 */ (T_MASK ^ 0x173848a9)
#define T3    0x242070db
#define T4 /* 0xc1bdceee */ (T_MASK ^ 0x3e423111)
#define T5 /* 0xf57c0faf */ (T_MASK ^ 0x0a83f050)
#define T6    0x4787c62a
#define T7 /* 0xa8304613 */ (T_MASK ^ 0x57cfb9ec)
#define T8 /* 0xfd469501 */ (T_MASK ^ 0x02b96afe)
#define T9    0x698098d8
#define T10 /* 0x8b44f7af */ (T_MASK ^ 0x74bb0850)
#define T11 /* 0xffff5bb1 */ (T_MASK ^ 0x0000a44e)
#define T12 /* 0x895cd7be */ (T_MASK ^ 0x76a32841)
#define T13    0x6b901122
#define T14 /* 0xfd987193 */ (T_MASK ^ 0x02678e6c)
#define T15 /* 0xa679438e */ (T_MASK ^ 0x5986bc71)
#define T16    0x49b40821
#define T17 /* 0xf61e2562 */ (T_MASK ^ 0x09e1da9d)
#define T18 /* 0xc040b340 */ (T_MASK ^ 0x3fbf4cbf)
#define T19    0x265e5a51
#define T20 /* 0xe9b6c7aa */ (T_MASK ^ 0x16493855)
#define T21 /* 0xd62f105d */ (T_MASK ^ 0x29d0efa2)
#define T22    0x02441453
#define T23 /* 0xd8a1e681 */ (T_MASK ^ 0x275e197e)
#define T24 /* 0xe7d3fbc8 */ (T_MASK ^ 0x182c0437)
#define T25    0x21e1cde6
#define T26 /* 0xc33707d6 */ (T_MASK ^ 0x3cc8f829)
#define T27 /* 0xf4d50d87 */ (T_MASK ^ 0x0b2af278)
#define T28    0x455a14ed
#define T29 /* 0xa9e3e905 */ (T_MASK ^ 0x561c16fa)
#define T30 /* 0xfcefa3f8 */ (T_MASK ^ 0x03105c07)
#define T31    0x676f02d9
#define T32 /* 0x8d2a4c8a */ (T_MASK ^ 0x72d5b375)
#define T33 /* 0xfffa3942 */ (T_MASK ^ 0x0005c6bd)
#define T34 /* 0x8771f681 */ (T_MASK ^ 0x788e097e)
#define T35    0x6d9d6122
#define T36 /* 0xfde5380c */ (T_MASK ^ 0x021ac7f3)
#define T37 /* 0xa4beea44 */ (T_MASK ^ 0x5b4115bb)
#define T38    0x4bdecfa9
#define T39 /* 0xf6bb4b60 */ (T_MASK ^ 0x0944b49f)
#define T40 /* 0xbebfbc70 */ (T_MASK ^ 0x4140438f)
#define T41    0x289b7ec6
#define T42 /* 0xeaa127fa */ (T_MASK ^ 0x155ed805)
#define T43 /* 0xd4ef3085 */ (T_MASK ^ 0x2b10cf7a)
#define T44    0x04881d05
#define T45 /* 0xd9d4d039 */ (T_MASK ^ 0x262b2fc6)
#define T46 /* 0xe6db99e5 */ (T_MASK ^ 0x1924661a)
#define T47    0x1fa27cf8
#define T48 /* 0xc4ac5665 */ (T_MASK ^ 0x3b53a99a)
#define T49 /* 0xf4292244 */ (T_MASK ^ 0x0bd6ddbb)
#define T50    0x432aff97
#define T51 /* 0xab9423a7 */ (T_MASK ^ 0x546bdc58)
#define T52 /* 0xfc93a039 */ (T_MASK ^ 0x036c5fc6)
#define T53    0x655b59c3
#define T54 /* 0x8f0ccc92 */ (T_MASK ^ 0x70f3336d)
#define T55 /* 0xffeff47d */ (T_MASK ^ 0x00100b82)
#define T56 /* 0x85845dd1 */ (T_MASK ^ 0x7a7ba22e)
#define T57    0x6fa87e4f
#define T58 /* 0xfe2ce6e0 */ (T_MASK ^ 0x01d3191f)
#define T59 /* 0xa3014314 */ (T_MASK ^ 0x5cfebceb)
#define T60    0x4e0811a1
#define T61 /* 0xf7537e82 */ (T_MASK ^ 0x08ac817d)
#define T62 /* 0xbd3af235 */ (T_MASK ^ 0x42c50dca)
#define T63    0x2ad7d2bb
#define T64 /* 0xeb86d391 */ (T_MASK ^ 0x14792c6e)

/**
 * Calculates an MD5 sum of provided message.
 *
 * @param msg the message to digest
 * @param msgLen the number of characters in the message
 * @param digest an initialized placeholder for the results, assumed to be of size 16
 */
void md5(unsigned char *msg, unsigned long msgLen, unsigned char *digest) {
    // Support variables
    unsigned int i, tmp;
    unsigned int *X;
    unsigned char *chrPtr;
    // Initial values for the results
    unsigned int h[4] = { 0x67452301, 0xefcdab89, 0x98badcfe, 0x10325476 };
    // Temporary results holders
    unsigned int a, b, c, d, t;
    
    // Append the "1" bit to the msg
    msg[msgLen] = ONE_BIT;   
    // Padd the msg with zeroes until 448 bits are reached    
    for (i = msgLen + 1; i < MAX_MSG_LEN - 8; i++) {
        msg[i] = PADDING_ZEROES;
    }
    // Append the original msg length to the end of the msg
    tmp = 0;
    for (; i < MAX_MSG_LEN; i++) {
        msg[i] = ((8 * msgLen) >> (8 * tmp)) & 0xFF;
        tmp++;
    }  
    // Set initial values
    a = h[0];
    b = h[1];
    c = h[2];
    d = h[3];    
    
    // Perform the actual calculations, process as 32-bit chunks
    X = (unsigned int *)msg;    
    
#define ROTATE_LEFT(x, n) (((x) << (n)) | ((x) >> (32 - (n))))

    /* Round 1. */
    /* Let [abcd k s i] denote the operation
       a = b + ((a + F(b,c,d) + X[k] + T[i]) <<< s). */
#define F(x, y, z) (((x) & (y)) | (~(x) & (z)))
#define SET(a, b, c, d, k, s, Ti)\
  t = a + F(b,c,d) + X[k] + Ti;\
  a = ROTATE_LEFT(t, s) + b
    SET(a, b, c, d,  0,  7,  T1);
    SET(d, a, b, c,  1, 12,  T2);
    SET(c, d, a, b,  2, 17,  T3);
    SET(b, c, d, a,  3, 22,  T4);
    SET(a, b, c, d,  4,  7,  T5);
    SET(d, a, b, c,  5, 12,  T6);
    SET(c, d, a, b,  6, 17,  T7);
    SET(b, c, d, a,  7, 22,  T8);
    SET(a, b, c, d,  8,  7,  T9);
    SET(d, a, b, c,  9, 12, T10);
    SET(c, d, a, b, 10, 17, T11);
    SET(b, c, d, a, 11, 22, T12);
    SET(a, b, c, d, 12,  7, T13);
    SET(d, a, b, c, 13, 12, T14);
    SET(c, d, a, b, 14, 17, T15);
    SET(b, c, d, a, 15, 22, T16);
#undef SET

     /* Round 2. */
     /* Let [abcd k s i] denote the operation
          a = b + ((a + G(b,c,d) + X[k] + T[i]) <<< s). */
#define G(x, y, z) (((x) & (z)) | ((y) & ~(z)))
#define SET(a, b, c, d, k, s, Ti)\
  t = a + G(b,c,d) + X[k] + Ti;\
  a = ROTATE_LEFT(t, s) + b
     /* Do the following 16 operations. */
    SET(a, b, c, d,  1,  5, T17);
    SET(d, a, b, c,  6,  9, T18);
    SET(c, d, a, b, 11, 14, T19);
    SET(b, c, d, a,  0, 20, T20);
    SET(a, b, c, d,  5,  5, T21);
    SET(d, a, b, c, 10,  9, T22);
    SET(c, d, a, b, 15, 14, T23);
    SET(b, c, d, a,  4, 20, T24);
    SET(a, b, c, d,  9,  5, T25);
    SET(d, a, b, c, 14,  9, T26);
    SET(c, d, a, b,  3, 14, T27);
    SET(b, c, d, a,  8, 20, T28);
    SET(a, b, c, d, 13,  5, T29);
    SET(d, a, b, c,  2,  9, T30);
    SET(c, d, a, b,  7, 14, T31);
    SET(b, c, d, a, 12, 20, T32);
#undef SET

     /* Round 3. */
     /* Let [abcd k s t] denote the operation
          a = b + ((a + H(b,c,d) + X[k] + T[i]) <<< s). */
#define H(x, y, z) ((x) ^ (y) ^ (z))
#define SET(a, b, c, d, k, s, Ti)\
  t = a + H(b,c,d) + X[k] + Ti;\
  a = ROTATE_LEFT(t, s) + b
     /* Do the following 16 operations. */
    SET(a, b, c, d,  5,  4, T33);
    SET(d, a, b, c,  8, 11, T34);
    SET(c, d, a, b, 11, 16, T35);
    SET(b, c, d, a, 14, 23, T36);
    SET(a, b, c, d,  1,  4, T37);
    SET(d, a, b, c,  4, 11, T38);
    SET(c, d, a, b,  7, 16, T39);
    SET(b, c, d, a, 10, 23, T40);
    SET(a, b, c, d, 13,  4, T41);
    SET(d, a, b, c,  0, 11, T42);
    SET(c, d, a, b,  3, 16, T43);
    SET(b, c, d, a,  6, 23, T44);
    SET(a, b, c, d,  9,  4, T45);
    SET(d, a, b, c, 12, 11, T46);
    SET(c, d, a, b, 15, 16, T47);
    SET(b, c, d, a,  2, 23, T48);
#undef SET

     /* Round 4. */
     /* Let [abcd k s t] denote the operation
          a = b + ((a + I(b,c,d) + X[k] + T[i]) <<< s). */
#define I(x, y, z) ((y) ^ ((x) | ~(z)))
#define SET(a, b, c, d, k, s, Ti)\
  t = a + I(b,c,d) + X[k] + Ti;\
  a = ROTATE_LEFT(t, s) + b
     /* Do the following 16 operations. */
    SET(a, b, c, d,  0,  6, T49);
    SET(d, a, b, c,  7, 10, T50);
    SET(c, d, a, b, 14, 15, T51);
    SET(b, c, d, a,  5, 21, T52);
    SET(a, b, c, d, 12,  6, T53);
    SET(d, a, b, c,  3, 10, T54);
    SET(c, d, a, b, 10, 15, T55);
    SET(b, c, d, a,  1, 21, T56);
    SET(a, b, c, d,  8,  6, T57);
    SET(d, a, b, c, 15, 10, T58);
    SET(c, d, a, b,  6, 15, T59);
    SET(b, c, d, a, 13, 21, T60);
    SET(a, b, c, d,  4,  6, T61);
    SET(d, a, b, c, 11, 10, T62);
    SET(c, d, a, b,  2, 15, T63);
    SET(b, c, d, a,  9, 21, T64);
#undef SET    
    
    // Add the subresults to the hash    
    h[0] += a;
    h[1] += b;
    h[2] += c;
    h[3] += d;
    // Copy the resulting hash to output variable
    chrPtr = (unsigned char *)h;
    for (i = 0; i < DIGEST_LEN; i++) {
        digest[i] = chrPtr[i];
    }
}

/**
 * Compares two hashes and tells whether they are the same. Assumes both arrays
 * are of size 16 (length of MD5 digest). 
 * 
 * @param calculated the calculated hash
 * @param digest the provided hash
 * @param result the result of comparison - 1 means equal, 0 means not equal
 */
void compareHashes(unsigned char *calculated, unsigned char *digest, unsigned int *result) {
    unsigned int tmp = 1, i;
    for (i = 0; i < DIGEST_LEN; i++) {
        tmp &= (calculated[i] == digest[i]);
    }
    result[0] = tmp;
}

#define ALPH_LEN 26

void initState(long state, unsigned char *message, unsigned long *length) {
    unsigned char alphabet[ALPH_LEN] = {
        'a', 'b', 'c', 'd',
        'e', 'f', 'g', 'h',
        'i', 'j', 'k', 'l',
        'm', 'n', 'o', 'p',
        'q', 'r', 's', 't',
        'u', 'v', 'w', 'x',
        'y', 'z' 
    };
    unsigned char tmp[MAX_MSG_LEN];
    int diff, n = ALPH_LEN, chr = 0;
    while (state >= 0) {
        diff = state % n;
        tmp[chr] = alphabet[diff];
        chr++;
        state -= (diff + n);
        state /= n;
    }
    n = chr - 1;
    while (chr >= 0) {
        message[n - chr] = tmp[chr];
        chr--;
    }
    length[0] = n + 1;
}

__kernel void processData(__global unsigned char *input, unsigned int dataSize, __global unsigned char *output, unsigned int outputSize) {
    // Work-item memory variables declarations
    unsigned char tmp[LONG_SIZE];
    unsigned char msg[MAX_MSG_LEN];
    unsigned char digest[DIGEST_LEN];
    unsigned char calculated[DIGEST_LEN];
    unsigned int outcome[1] = { 0 };
    unsigned long msgLen[1] = { 0 };
    
    long from[1] = { 0 };
    long to[1] = { 0 };
    long state = 0;
    
    long step = 0;
    int wiCount = get_global_size(0);
    int wiId = get_global_id(0);
    int finder = -1;
    
    int i;
    
    
    // Work-group variables declarations
    __local int passLen;
    
    // Read the MD5 hash of the password from input array
    for (i = 0; i < DIGEST_LEN; i++) {
        digest[i] = input[i];
    }
    barrier(CLK_GLOBAL_MEM_FENCE);
    // Read the value from which we should begin generating hashes
    for (i = 0; i < LONG_SIZE; i++) {
        tmp[i] = input[i+DIGEST_LEN];
    }
    barrier(CLK_GLOBAL_MEM_FENCE);
    *from = *((long *)tmp);
    // Read the value on which we should stop generating hashes
    for (i = 0; i < LONG_SIZE; i++) {
        tmp[i] = input[i+DIGEST_LEN+LONG_SIZE];
    }
    barrier(CLK_GLOBAL_MEM_FENCE);
    *to = *((long *)tmp);
    
    // Set the initial state
    state = *from;
    passLen = 0;
    barrier(CLK_LOCAL_MEM_FENCE);
    
    //printf("[%d] from: %lu, to: %lu\n", wiId, *from, *to);

    while (passLen == 0 && state <= *to) {
        //printf("[%d] state: %lu\n", wiId, state);
        initState(state, msg, msgLen);
        md5(msg, msgLen[0], calculated);    
        compareHashes(calculated, digest, outcome);
        barrier(CLK_GLOBAL_MEM_FENCE);
        if (outcome[0] > 0) {
            passLen = msgLen[0];
            finder = wiId;
            break;
        }
        step++;
        state = *from + (step * wiCount) + wiId;
    }    
    barrier(CLK_LOCAL_MEM_FENCE);
    // WARNING: printf below will not run on all devices
    /*for (i = 0; i < passLen; i++) {
        printf("%c", msg[i]);
    }
    printf("\n");*/
    // Copy the result
    if (wiId == finder) {
        output[0] = (passLen) & 0xFF;
        output[1] = (passLen >> 8) & 0xFF;
        output[2] = (passLen >> 16) & 0xFF;
        output[3] = (passLen >> 24) & 0xFF;
        for (i = 0; i < passLen; i++) {
            output[i+4] = msg[i];
        }
    } else if (wiId == 0) {
        output[0] = 0;
        output[1] = 0;
        output[2] = 0;
        output[3] = 0;
    }
}

/* Test hashes:
    abc
    unsigned char digest[DIGEST_LEN] = { 0x90, 0x01, 0x50, 0x98, 0x3c, 0xd2, 0x4f, 0xb0, 0xd6, 0x96, 0x3f, 0x7d, 0x28, 0xe1, 0x7f, 0x72 };
    abcd
    unsigned char digest[DIGEST_LEN] = { 0xe2, 0xfc, 0x71, 0x4c, 0x47, 0x27, 0xee, 0x93, 0x95, 0xf3, 0x24, 0xcd, 0x2e, 0x7f, 0x33, 0x1f };
    haslo
    unsigned char digest[DIGEST_LEN] = { 0x20, 0x70, 0x23, 0xcc, 0xb4, 0x4f, 0xeb, 0x4d, 0x7d, 0xad, 0xca, 0x00, 0x5c, 0xe2, 0x9a, 0x64 };
*/

