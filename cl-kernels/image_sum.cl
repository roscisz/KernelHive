/*
 * Structure of preview object.
 */
struct PreviewObject {
        float f1;
        float f2;
        float f3;
};

/*
 * The goal of this kernel is to sum with weights two images
 */
__kernel void image_sum(__global uchar* src_img1,
                        float w1,
                        __global uchar* src_img2,
                        float w2,
                        __global uchar* dst_img,
                        uint width,
                        uint height,
                        __global struct PreviewObject *previewBuffer)
{
    // how many adjacent pixels each thread should process
    int2 blocks = (int2) (ceil((float)width / get_global_size(0)), ceil((float)height / get_global_size(1)));
    int2 image_coord = (int2) (blocks.x * get_global_id(0), blocks.y * get_global_id(1));

    for(int j = 0; j < blocks.y; j++)
    {
        image_coord.x = blocks.x * get_global_id(0);

        for(int i = 0; i < blocks.x; i++)
        {
            if(image_coord.x < width && image_coord.y < height)
            {
                // load rgba pixel from first image
                uchar4 upixel1 = vload4(image_coord.y * width + image_coord.x, src_img1);
                float4 pixel1 = convert_float4(upixel1);
                // load rgba pixel from second image
                uchar4 upixel2 = vload4(image_coord.y * width + image_coord.x, src_img2);
                float4 pixel2 = convert_float4(upixel2);
                float4 result = pixel1 * w1 + pixel2 * w2;
                uchar4 out = convert_uchar4_sat_rte(result);
                // store added pixels 
                vstore4(out, image_coord.y * width + image_coord.x, dst_img);
            }
            image_coord.x += 1;
        }
        image_coord.y += 1;
    }
}
