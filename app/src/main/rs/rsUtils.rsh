static uint32_t getMaxY(rs_allocation image)
{
    return rsAllocationGetDimY(image)-1;
}

static uint32_t getMaxX(rs_allocation image)
{
    return rsAllocationGetDimX(image)-1;
}

static void getNormalizedPolar(float* rOut, float* thetaOut, uint32_t x, uint32_t y, rs_allocation image)
{

    uint32_t maxX = getMaxX(image);
    uint32_t maxY = getMaxY(image);

    // normalize the x and y values to the ranges of [-1,-1]
    float normX = 2*(float)x/(float)maxX-1;
    float normY = 2*(float)y/(float)maxY-1;

    // convert to polar coordinates
    *rOut = sqrt(pown(normX,2) + pown(normY,2));
    *thetaOut = atan((float)normY / (float)normX);
    if (normX < 0)
        *thetaOut += M_PI;
}

static void getDenormalizedCartesian(float* xOut, float* yOut, float r, float theta, rs_allocation image)
{
    *xOut = (r*cos(theta)+1)*(float)getMaxX(image)/2;
    *yOut = (r*sin(theta)+1)*(float)getMaxY(image)/2;
}