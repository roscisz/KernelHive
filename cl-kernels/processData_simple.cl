__kernel void processData(__global const int* input, unsigned int dataSize, __global int* output) {
 
    // Get the index of the current element to be processed
    int i = get_global_id(0);
    
    // Right now just copy the input to output
    output[i] = input[i];
    
}

