#pragma version(1)
#pragma rs java_package_name(com.example.brendan.assignment2)

#include "rsUtils.rsh"

rs_allocation image;

uchar4 __attribute__((kernel)) twirl(uchar4 in, uint32_t x, uint32_t y)
{

    float normalizedR;
    float normalizedTheta;
    float newX;
    float newY;
    float newTheta;

    getNormalizedPolar(&normalizedR, &normalizedTheta, x, y, image);

    // perform the transform
    newTheta = normalizedTheta;
    if (normalizedR < 1)
        newTheta = normalizedTheta + M_PI * (1-normalizedR);

    getDenormalizedCartesian(&newX, &newY, normalizedR, newTheta, image);

    return rsGetElementAt_uchar4(image, (unsigned int)newX, (unsigned int)newY);
}

