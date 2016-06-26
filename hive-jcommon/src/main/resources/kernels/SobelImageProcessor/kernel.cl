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

/*
 * The goal of this kernel is to apply filter on the rgba image
 */
__kernel void image_convolution(__global uchar* src_img,
                               __global uchar* dst_img,
                               uint width,
                               uint height,
                               __global float* kw,
                               __local float* matrix,
                               uint matrix_size,
                               float divider,
                               __global struct PreviewObject *previewBuffer)
{
    // copy filter weights to local memory
    event_t event = async_work_group_copy(matrix, kw, matrix_size * matrix_size, 0);

    int r = matrix_size >> 1;
    // how many adjacent pixels each thread should process
    int2 blocks = (int2) (ceil((float)width / get_global_size(0)), ceil((float)height / get_global_size(1)));
    int2 start_image_coord = (int2) (blocks.x * get_global_id(0) - r, blocks.y * get_global_id(1) - r);
    int2 end_image_coord = (int2) (blocks.x * get_global_id(0) + r, blocks.y * get_global_id(1) + r);
    int2 out_image_coord = (int2) (blocks.x * get_global_id(0), blocks.y * get_global_id(1));
    // image with "border"
    uint w = width + 2 * r;

    wait_group_events(1, &event);
    for(int j = 0; j < blocks.y; j++)
    {
        start_image_coord.x = blocks.x * get_global_id(0) - r;
        end_image_coord.x = blocks.x * get_global_id(0) + r;
        out_image_coord.x = blocks.x * get_global_id(0);

        for(int i = 0; i < blocks.x; i++)
        {
            if(out_image_coord.x < width && out_image_coord.y < height)
            {
                // the most often case - 3x3 matrix
                float4 result = (float4) (0.0f, 0.0f, 0.0f, 0.0f);
                if(matrix_size == 3)
                {
                    int2 sic = (int2) (start_image_coord.x + r, start_image_coord.y + r);
                    int2 oic = (int2) (out_image_coord.x + r, out_image_coord.y + r);
                    int2 eic = (int2) (end_image_coord.x + r, end_image_coord.y + r);
                    result += convert_float4(vload4(sic.y * w + sic.x, src_img)) * matrix[0];
                    result += convert_float4(vload4(sic.y * w + oic.x, src_img)) * matrix[1];
                    result += convert_float4(vload4(sic.y * w + eic.x, src_img)) * matrix[2];
                    result += convert_float4(vload4(oic.y * w + sic.x, src_img)) * matrix[3];
                    result += convert_float4(vload4(oic.y * w + oic.x, src_img)) * matrix[4];
                    result += convert_float4(vload4(oic.y * w + eic.x, src_img)) * matrix[5];
                    result += convert_float4(vload4(eic.y * w + sic.x, src_img)) * matrix[6];
                    result += convert_float4(vload4(eic.y * w + oic.x, src_img)) * matrix[7];
                    result += convert_float4(vload4(eic.y * w + eic.x, src_img)) * matrix[8];
                    result = fabs(result / divider);
                }
                else
                {
                    for(int y = start_image_coord.y; y <= end_image_coord.y; y++)
                    {
                        for(int x = start_image_coord.x; x <= end_image_coord.x; x++)
                        {
                            // load rgba
                            uchar4 in = vload4((y + r) * w + x + r, src_img);
                            float4 fin = convert_float4(in);
                            result += fin * matrix[(y - start_image_coord.y) * matrix_size + (x - start_image_coord.x)];
                        }
                    }
                    result = fabs(result / divider);
                }
                uchar4 out = convert_uchar4_sat_rte(result);
                // store processed rgba
                vstore4(out, out_image_coord.y * width + out_image_coord.x, dst_img);
            }
            start_image_coord.x += 1;
            end_image_coord.x += 1;
            out_image_coord.x += 1;
        }
        start_image_coord.y += 1;
        end_image_coord.y += 1;
        out_image_coord.y += 1;
    }
}

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
