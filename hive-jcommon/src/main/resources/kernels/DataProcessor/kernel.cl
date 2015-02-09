/*
 * Structure of preview object.
 */
struct PreviewObject {
        float f1;
        float f2;
        float f3;
};

/*
 * The goal of this kernel is to calculate the the fields of rectangles which
 * approximate the integral value.
 */
__kernel void processData(
    __global float* input,
    unsigned int dataSize,
    __global float* output,
    unsigned int outputSize,
    __global struct PreviewObject *previewBuffer)
{ 
    // Get the index of the current element to be processed
    int id = get_global_id(0);
    // Get the number of data items per processing thread
    int actualItemsCount = outputSize / sizeof(float);
    int itemsPerThread = actualItemsCount / get_global_size(0);
 
    // Get the delta between values:   
    float delta = input[1] - input[0];
    
    // Calculate the fields:
    for (int i = 0; i < itemsPerThread; i++) {
        int idx = (id*itemsPerThread)+i;
        output[idx] = input[idx] * delta;
    }        
}