//
// Created by tom on 19-12-8.
//

#ifndef BITKANDA_WALLET_ANDROID_STRENCODINGS_H
#define BITKANDA_WALLET_ANDROID_STRENCODINGS_H
#include <stdio.h>
#include <string.h>
typedef  char HashString[96] ;
void HexStr(const uint8_t* itbegin, const size_t itsize, int fSpaces, char* out ,const size_t outsize);
#endif //BITKANDA_WALLET_ANDROID_STRENCODINGS_H
