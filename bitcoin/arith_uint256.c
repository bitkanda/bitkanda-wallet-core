//
// Created by tom on 19-12-14.
//

#include "arith_uint256.h"
#include <assert.h>
#include <string.h>
#include <stdlib.h>
#include <endian.h>
unsigned int  bits(arith_uint256* p)
{
for (int pos = arith_uint256_WIDTH - 1; pos >= 0; pos--) {
if (p->pn[pos]) {
for (int nbits = 31; nbits > 0; nbits--) {
if (p->pn[pos] & 1U << nbits)
return 32 * pos + nbits + 1;
}
return 32 * pos + 1;
}
}
return 0;
}

uint64_t GetLow64(arith_uint256* p)
{
assert(arith_uint256_WIDTH >= 2);
return p->pn[0] | (uint64_t)p->pn[1] << 32;
}
//>>=
arith_uint256* operator_R_Q( arith_uint256* thi,unsigned int shift) {
    arith_uint256 a;
    memcpy(&a, thi, sizeof(arith_uint256));

    for (int i = 0; i < arith_uint256_WIDTH; i++)
        thi->pn[i] = 0;
    int k = shift / 32;
    shift = shift % 32;
    for (int i = 0; i < arith_uint256_WIDTH; i++) {
        if (i - k - 1 >= 0 && shift != 0)
            thi->pn[i - k - 1] |= (a.pn[i] << (32 - shift));
        if (i - k >= 0)
            thi->pn[i - k] |= (a.pn[i] >> shift);
    }

    return thi;
}
//>>
arith_uint256 operator_R( arith_uint256*  thi, int shift)
{
    arith_uint256 tmp;
    memcpy(&tmp,thi,sizeof(arith_uint256));
     operator_R_Q(&tmp,shift);
    return tmp;
}

unsigned int GetCompact(arith_uint256* thi,int fNegative)
{
    int nSize = (bits(thi) + 7) / 8;
    unsigned int nCompact = 0;
    if (nSize <= 3) {
        nCompact = GetLow64(thi) << 8 * (3 - nSize);
    } else {
        arith_uint256 bn =operator_R(thi,8 * (nSize - 3)) ;
        nCompact = GetLow64(&bn);
    }
// The 0x00800000 bit denotes the sign.
// Thus, if it is already set, divide the mantissa by 256 and increase the exponent.
    if (nCompact & 0x00800000) {
        nCompact >>= 8;
        nSize++;
    }
    assert((nCompact & ~0x007fffff) == 0);
    assert(nSize < 256);
    nCompact |= nSize << 24;
    nCompact |= (fNegative && (nCompact & 0x007fffff) ? 0x00800000 : 0);
    return nCompact;
}



int CompareTo(const arith_uint256*  thi,const arith_uint256* b)
{
for (int i = arith_uint256_WIDTH - 1; i >= 0; i--) {
if (thi->pn[i] < b->pn[i])
return -1;
if (thi->pn[i] > b->pn[i])
return 1;
}
return 0;
}

//>
int operator_T(const arith_uint256* a, const arith_uint256* b)
{
    return CompareTo(a,b) > 0;
}

//*=
arith_uint256* operator_M_Q(arith_uint256* thi,uint32_t b32)
{
uint64_t carry = 0;
for (int i = 0; i < arith_uint256_WIDTH; i++) {
uint64_t n = carry + (uint64_t)b32 *thi-> pn[i];
    thi->pn[i] = n & 0xffffffff;
carry = n >> 32;
}
return  thi;
}

//base_uint
void arith_uint256_int(arith_uint256* thi,uint64_t b)
{
//assert(arith_uint256_WIDTH/32 > 0 && BITS(thi)%32);

thi->pn[0] = (unsigned int)b;
    thi->pn[1] = (unsigned int)(b >> 32);
for (int i = 2; i < arith_uint256_WIDTH; i++)
    thi->pn[i] = 0;
}

//<<=
arith_uint256 operator_L_Q(arith_uint256* thi,unsigned int shift) {
    arith_uint256 a;
    memcpy(&a, thi, sizeof(arith_uint256));
    for (int i = 0; i < arith_uint256_WIDTH; i++)
        thi->pn[i] = 0;
    int k = shift / 32;
    shift = shift % 32;
    for (int i = 0; i < arith_uint256_WIDTH; i++) {
        if (i + k + 1 < arith_uint256_WIDTH && shift != 0)
            thi->pn[i + k + 1] |= (a.pn[i] >> (32 - shift));
        if (i + k < arith_uint256_WIDTH)
            thi->pn[i + k] |= (a.pn[i] << shift);
    }
    return *thi;
}
//>=
int operator_R_Q_O(const arith_uint256* a, const arith_uint256* b)
{
    return CompareTo(a,b)   >= 0;
}
//++
arith_uint256* operator_A_A(arith_uint256* thi)
{
// prefix operator
int i = 0;
while (i < arith_uint256_WIDTH && ++thi->pn[i] == 0)
i++;
return thi;
}


//-
const arith_uint256 operator_S(arith_uint256* thi)
{
    arith_uint256 ret;
for (int i = 0; i < arith_uint256_WIDTH; i++)
ret.pn[i] = ~thi->pn[i];
    operator_A_A(&ret);
return ret;
}

//+=
arith_uint256* operator_A_Q(arith_uint256* thi,const arith_uint256* b)
{
uint64_t carry = 0;
for (int i = 0; i < arith_uint256_WIDTH; i++)
{
uint64_t n = carry + thi->pn[i] + b->pn[i];
    thi->pn[i] = n & 0xffffffff;
carry = n >> 32;
}
return thi;
}

//-=
arith_uint256* operator_S_Q( arith_uint256* thi, arith_uint256* b) {
    arith_uint256 bt = operator_S(b);
    operator_A_Q(thi, &bt);
    return thi;
}

// /=
arith_uint256* operator_D_Q_int(arith_uint256* thi, const unsigned int b)
{
    arith_uint256 btmp;
    arith_uint256_int(&btmp,b);
    return  operator_D_Q(thi,&btmp);
}

// /=
arith_uint256* operator_D_Q(arith_uint256* thi, const arith_uint256* b)
{
    arith_uint256 div;       // make a copy, so we can shift.
    memcpy(&div, b, sizeof(arith_uint256));
    arith_uint256 num ;  // make a copy, so we can subtract.
    memcpy(&num, thi, sizeof(arith_uint256));
    memset(thi,0,sizeof(arith_uint256));
                   // the quotient.
int num_bits = bits(&num);
int div_bits = bits(&div);
assert (div_bits);

if (div_bits > num_bits) // the result is certainly 0.
return thi;
int shift = num_bits - div_bits;
div =operator_L_Q(&div, shift); // shift so that div and num align.
while (shift >= 0) {
    if (operator_R_Q_O(&num, &div)) {
        operator_S_Q(&num, &div);
        thi->pn[shift / 32] |= (1 << (shift & 31)); // set a bit of the result.
    }
    operator_R_Q(&div, 1); // shift back.
    shift--;
}
// num now contains the remainder of the division.
return thi;
}

arith_uint256* SetCompact(arith_uint256* thi,uint32_t nCompact, int* pfNegative, int* pfOverflow) {
    int nSize = nCompact >> 24;
    uint32_t nWord = nCompact & 0x007fffff;
    if (nSize <= 3) {
        nWord >>= 8 * (3 - nSize);
        arith_uint256_int(thi, nWord);
    } else {
        arith_uint256_int(thi, nWord);
        *thi = operator_L_Q(thi, 8 * (nSize - 3));
    }
    if (pfNegative)
        *pfNegative = nWord != 0 && (nCompact & 0x00800000) != 0;
    if (pfOverflow)
        *pfOverflow = nWord != 0 && ((nSize > 34) ||
                                     (nWord > 0xff && nSize > 33) ||
                                     (nWord > 0xffff && nSize > 32));
    return thi;
}
uint32_t static inline ReadLE32(const unsigned char* ptr)
{
    uint32_t x;
    memcpy((char*)&x, ptr, 4);
    return le32toh(x);
}
arith_uint256 UintToArith256(const uint8_t* a )
{
arith_uint256 b;
for(int x=0; x<arith_uint256_WIDTH; ++x)
b.pn[x] = ReadLE32(a + x*4);
return b;
}

