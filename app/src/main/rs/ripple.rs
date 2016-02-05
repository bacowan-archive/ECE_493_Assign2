#pragma version(1)
#pragma rs java_package_name(com.example.brendan.assignment2)

#include "rsUtils.rsh"

rs_allocation image;
int rippleNum = 4;

uchar4 __attribute__((kernel)) ripple(uchar4 in, uint32_t x, uint32_t y)
{

    float normalizedR;
    float normalizedTheta;
    float newX;
    float newY;
    float newR;

    getNormalizedPolar(&normalizedR, &normalizedTheta, x, y, image);

    // perform the transform
    newR = normalizedR;
    if (normalizedR < 1) {
        float sinR = (sin((float)rippleNum*2.f*M_PI*normalizedR)+1)/2.f;
        float dist = (1/(2*(float)rippleNum))*pown(sinR,2);
        newR = normalizedR + dist;
        if (newR > 1)
            newR = 1;

    }

    getDenormalizedCartesian(&newX, &newY, newR, normalizedTheta, image);

    return rsGetElementAt_uchar4(image, (unsigned int)newX, (unsigned int)newY);
}

