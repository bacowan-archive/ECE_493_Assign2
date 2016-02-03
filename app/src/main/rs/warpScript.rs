(#pragma version(1))
(#pramga rs java_package_name(com.example.brendan))

uchar4 __attribute__((kernel)) saturation(uchar4 in)
{
    return in;
}