//
// Created by tom on 19-12-8.
//

#include "strencodings.h"
#include <stdio.h>
#include <string.h>


void HexStr(const uint8_t* itbegin, const size_t itsize, int fSpaces, char* out ,const size_t outsize)
{
    static const char hexmap[16] = { '0', '1', '2', '3', '4', '5', '6', '7',
                                     '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    memset(out, 0, outsize);
    if(itsize==0)
        return ;
    char*i=out;
    for(int it = itsize-1; it >=0; --it)
    {
        unsigned char val = (unsigned char)(itbegin[it]);
        if(fSpaces && it != 0)
        {
            (*i)=' ';
            i++;
        }
        (*i)=hexmap[val>>4];
        i++;
        (*i)=hexmap[val&15];
        i++;
    }
    (*i)=0;
    return  ;
}