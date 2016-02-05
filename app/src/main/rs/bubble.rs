#pragma version(1)
#pragma rs java_package_name(com.example.brendan.assignment2)

#include "rsUtils.rsh"

rs_allocation image;

uchar4 __attribute__((kernel)) bubble(uchar4 in, uint32_t x, uint32_t y)
{

    float normalizedR;
    float normalizedTheta;
    float newX;
    float newY;
    float newR;

    getNormalizedPolar(&normalizedR, &normalizedTheta, x, y, image);

    // perform the transform
    newR = normalizedR;
    if (normalizedR < 1)
        newR = pow(normalizedR,2);

    getDenormalizedCartesian(&newX, &newY, newR, normalizedTheta, image);

    return rsGetElementAt_uchar4(image, (unsigned int)newX, (unsigned int)newY);

}