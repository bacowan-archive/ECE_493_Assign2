#pragma version(1)
#pragma rs java_package_name(com.example.brendan.assignment2)

rs_allocation image;

uchar4 __attribute__((kernel)) bubble(uchar4 in, uint32_t x, uint32_t y)
{

    uint32_t maxX = rsAllocationGetDimX(image)-1;
    uint32_t maxY = rsAllocationGetDimY(image)-1;

    // normalize the x and y values to the ranges of [-1,-1]
    float normX = 2*(float)x/(float)maxX-1;
    float normY = 2*(float)y/(float)maxY-1;

    // convert to polar coordinates
    float r = sqrt(pown(normX,2) + pown(normY,2));
    float theta = atan((float)normY / (float)normX);
    if (normX < 0)
        theta += M_PI;

    // perform the transform
    float newR = r;
    if (r < 1)
        newR = pow(r,2);

    // convert back to cartesian and denormalize
    float newX = (newR*cos(theta)+1)*(float)maxX/2;
    float newY = (newR*sin(theta)+1)*(float)maxY/2;

    return rsGetElementAt_uchar4(image, (unsigned int)newX, (unsigned int)newY);
}
