//
// Created by tom on 19-12-14.
//

#ifndef BITKANDA_WALLET_ANDROID_ARITH_UINT256_H
#define BITKANDA_WALLET_ANDROID_ARITH_UINT256_H
#include <stdlib.h>

#define arith_uint256_WIDTH 8
typedef struct
{
    uint32_t pn[arith_uint256_WIDTH];

}arith_uint256;
unsigned int  bits(arith_uint256* p);
uint64_t GetLow64(arith_uint256* p);
//>>=
arith_uint256* operator_R_Q( arith_uint256* thi,unsigned int shift);
//>>
arith_uint256 operator_R( arith_uint256*  thi, int shift);
uint32_t GetCompact(arith_uint256* thi,int fNegative);
arith_uint256* SetCompact(arith_uint256* thi,uint32_t nCompact, int* pfNegative, int* pfOverflow);
int CompareTo(const arith_uint256*  thi,const arith_uint256* b);
//*=
arith_uint256* operator_M_Q(arith_uint256* thi,uint32_t b32);
arith_uint256* operator_D_Q(arith_uint256* thi,const arith_uint256* b);
//>
int operator_T(const arith_uint256* a, const arith_uint256* b);
arith_uint256 UintToArith256(const uint8_t* a );
// /=
arith_uint256* operator_D_Q_int(arith_uint256* thi, const unsigned int b);
#endif //BITKANDA_WALLET_ANDROID_ARITH_UINT256_H
