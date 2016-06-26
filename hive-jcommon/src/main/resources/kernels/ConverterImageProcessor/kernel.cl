/*
 * Structure of preview object.
 */
struct PreviewObject {
        float f1;
        float f2;
        float f3;
};

/*
 * The goal of this kernel is to convert RGBA image into gray scale
 */
__kernel void image_conversion(__global uchar* src_img,
                               __global uchar* dst_img,
                               uint width,
                               uint height,
                               __global struct PreviewObject *previewBuffer)
{
    // how many adjacent pixels each thread should process
    int2 blocks = (int2) (ceil((float)width / get_global_size(0)), ceil((float)height / get_global_size(1)));
    // initial coordinates of these pixels
    int2 image_coord = (int2) (blocks.x * get_global_id(0), blocks.y * get_global_id(1));

    for(int j = 0; j < blocks.y; j++)
    {
        image_coord.x = blocks.x * get_global_id(0);

        for(int i = 0; i < blocks.x; i++)
        {
            if(image_coord.x < width && image_coord.y < height)
            {
                // load rgba
                uchar4 upixel = vload4(image_coord.y * width + image_coord.x, src_img);
                float4 pixel = convert_float4(upixel);
                float result = 0.21f * pixel.x + 0.72f * pixel.y + 0.07f * pixel.z;
                uchar4 out = convert_uchar4_sat_rte((float4)(result, result, result, 1.0));
                // store (gray, gray, gray, 1.0)
                vstore4(out, image_coord.y * width + image_coord.x, dst_img);
            }
            image_coord.x += 1;
        }
        image_coord.y += 1;
    }
}
