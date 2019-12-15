//  Created by Ed Gamble on 1/23/2018
//  Copyright (c) 2018 Breadwinner AG.  All right reserved.
//
//  See the LICENSE file at the project root for license information.
//  See the CONTRIBUTORS file at the project root for a list of contributors.

#include <assert.h>
#include "BRCoreJni.h"
#include "bitcoin/BRMerkleBlock.h"
#include "com_breadwallet_core_BRCoreMerkleBlock.h"
#include "openssl/crypto.h"
//bitkanda
#include <openssl/sha.h>
#include "bitcoin/BRMerkleBlock.h"
#include "support/BRInt.h"
#include "support/BRCrypto.h"
#include "strencodings.h"
/*
 * Class:     com_breadwallet_core_BRCoreMerkleBlock
 * Method:    createJniCoreMerkleBlock
 * Signature: ([BI)J
 */
JNIEXPORT jlong JNICALL
Java_com_breadwallet_core_BRCoreMerkleBlock_createJniCoreMerkleBlock
        (JNIEnv *env, jclass thisClass,
         jbyteArray blockArray,
         jint blockHeight) {

    int blockLength   = (*env)->GetArrayLength(env, blockArray);
    jbyte *blockBytes = (*env)->GetByteArrayElements(env, blockArray, 0);

    assert (NULL != blockBytes);
    BRMerkleBlock *block = BRMerkleBlockParse((const uint8_t *) blockBytes, (size_t) blockLength);
    assert (NULL != block);
    if (blockHeight != -1)
        block->height = (uint32_t) blockHeight;

    return (jlong) block;
}

/*
 * Class:     com_breadwallet_core_BRCoreMerkleBlock
 * Method:    createJniCoreMerkleBlockEmpty
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_com_breadwallet_core_BRCoreMerkleBlock_createJniCoreMerkleBlockEmpty
        (JNIEnv *env, jclass thisClass) {
    // Test only
    BRMerkleBlock *block = BRMerkleBlockNew();
    block->height = BLOCK_UNKNOWN_HEIGHT;
    return (jlong) block;
}


/*
 * Class:     com_breadwallet_core_BRCoreMerkleBlock
 * Method:    getBlockHash
 * Signature: ()[B
 */
JNIEXPORT jbyteArray JNICALL
Java_com_breadwallet_core_BRCoreMerkleBlock_getBlockHash
        (JNIEnv *env, jobject thisObject) {
    BRMerkleBlock *block = (BRMerkleBlock *) getJNIReference(env, thisObject);

    UInt256 hash = block->blockHash;

    jbyteArray hashObject = (*env)->NewByteArray (env, sizeof (UInt256));
    (*env)->SetByteArrayRegion (env, hashObject, 0, sizeof (UInt256), (const jbyte *) hash.u8);
    return hashObject;
}

/*
 * Class:     com_breadwallet_core_BRCoreMerkleBlock
 * Method:    getVersion
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL
Java_com_breadwallet_core_BRCoreMerkleBlock_getVersion
        (JNIEnv *env, jobject thisObject)  {
    BRMerkleBlock *block = (BRMerkleBlock *) getJNIReference(env, thisObject);
    return (jlong) block->version;
}

/*
 * Class:     com_breadwallet_core_BRCoreMerkleBlock
 * Method:    getPrevBlockHash
 * Signature: ()[B
 */
JNIEXPORT jbyteArray JNICALL
Java_com_breadwallet_core_BRCoreMerkleBlock_getPrevBlockHash
        (JNIEnv *env, jobject thisObject) {
    BRMerkleBlock *block = (BRMerkleBlock *) getJNIReference(env, thisObject);

    UInt256 hash = block->prevBlock;

    jbyteArray hashObject = (*env)->NewByteArray (env, sizeof (UInt256));
    (*env)->SetByteArrayRegion (env, hashObject, 0, sizeof (UInt256), (const jbyte *) hash.u8);
    return hashObject;
}

/*
 * Class:     com_breadwallet_core_BRCoreMerkleBlock
 * Method:    getRootBlockHash
 * Signature: ()[B
 */
JNIEXPORT jbyteArray JNICALL
Java_com_breadwallet_core_BRCoreMerkleBlock_getRootBlockHash
        (JNIEnv *env, jobject thisObject) {
    BRMerkleBlock *block = (BRMerkleBlock *) getJNIReference(env, thisObject);

    UInt256 hash = block->merkleRoot;

    jbyteArray hashObject = (*env)->NewByteArray (env, sizeof (UInt256));
    (*env)->SetByteArrayRegion (env, hashObject, 0, sizeof (UInt256), (const jbyte *) hash.u8);
    return hashObject;
}

/*
 * Class:     com_breadwallet_core_BRCoreMerkleBlock
 * Method:    getTimestamp
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL
Java_com_breadwallet_core_BRCoreMerkleBlock_getTimestamp
        (JNIEnv *env, jobject thisObject) {
    BRMerkleBlock *block = (BRMerkleBlock *) getJNIReference(env, thisObject);
    return (jlong) block->timestamp;
}

/*
 * Class:     com_breadwallet_core_BRCoreMerkleBlock
 * Method:    getTarget
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL
Java_com_breadwallet_core_BRCoreMerkleBlock_getTarget
        (JNIEnv *env, jobject thisObject) {
    BRMerkleBlock *block = (BRMerkleBlock *) getJNIReference(env, thisObject);
    return (jlong) block->target;
}

/*
 * Class:     com_breadwallet_core_BRCoreMerkleBlock
 * Method:    getNonce
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL
Java_com_breadwallet_core_BRCoreMerkleBlock_getNonce
        (JNIEnv *env, jobject thisObject) {
    BRMerkleBlock *block = (BRMerkleBlock *) getJNIReference(env, thisObject);
    return (jlong) block->nonce;
}

/*
 * Class:     com_breadwallet_core_BRCoreMerkleBlock
 * Method:    getTransactionCount
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL
Java_com_breadwallet_core_BRCoreMerkleBlock_getTransactionCount
        (JNIEnv *env, jobject thisObject) {
    BRMerkleBlock *block = (BRMerkleBlock *) getJNIReference(env, thisObject);
    return (jlong) block->totalTx;
}

/*
 * Class:     com_breadwallet_core_BRCoreMerkleBlock
 * Method:    getHeight
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL
Java_com_breadwallet_core_BRCoreMerkleBlock_getHeight
        (JNIEnv *env, jobject thisObject) {
    BRMerkleBlock *block = (BRMerkleBlock *) getJNIReference(env, thisObject);
    return (jlong) block->height;
}

/*
 * Class:     com_breadwallet_core_BRCoreMerkleBlock
 * Method:    serialize
 * Signature: ()[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_breadwallet_core_BRCoreMerkleBlock_serialize
        (JNIEnv *env, jobject thisObject) {
    BRMerkleBlock *block = (BRMerkleBlock *) getJNIReference(env, thisObject);

    size_t      byteArraySize     = BRMerkleBlockSerialize(block, NULL, 0);
    jbyteArray  byteArray         = (*env)->NewByteArray (env, (jsize) byteArraySize);
    jbyte      *byteArrayElements = (*env)->GetByteArrayElements (env, byteArray, JNI_FALSE);

    BRMerkleBlockSerialize(block, (uint8_t *) byteArrayElements, byteArraySize);

    // Ensure ELEMENTS 'written' back to byteArray
    (*env)->ReleaseByteArrayElements (env, byteArray, byteArrayElements, JNI_COMMIT);

    return byteArray;
}


/*
 * Class:     com_breadwallet_core_BRCoreMerkleBlock
 * Method:    isValid
 * Signature: (J)Z
 */
JNIEXPORT jboolean JNICALL
Java_com_breadwallet_core_BRCoreMerkleBlock_isValid
        (JNIEnv *env, jobject thisObject, jlong currentTime) {
    BRMerkleBlock *block = (BRMerkleBlock *) getJNIReference(env, thisObject);
    return (jboolean) BRMerkleBlockIsValid (block, (uint32_t) currentTime);
}

/*
 * Class:     com_breadwallet_core_BRCoreMerkleBlock
 * Method:    containsTransactionHash
 * Signature: ([B)Z
 */
JNIEXPORT jboolean JNICALL
Java_com_breadwallet_core_BRCoreMerkleBlock_containsTransactionHash
        (JNIEnv *env, jobject thisObject, jbyteArray hashByteArray) {
    BRMerkleBlock *block = (BRMerkleBlock *) getJNIReference(env, thisObject);

    UInt256 *hash = (UInt256 *) (*env)->GetByteArrayElements (env, hashByteArray, JNI_FALSE);
    return (jboolean) BRMerkleBlockContainsTxHash (block, *hash);
}

/*
 * Class:     com_breadwallet_core_BRCoreMerkleBlock
 * Method:    disposeNative
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_com_breadwallet_core_BRCoreMerkleBlock_disposeNative
        (JNIEnv *env, jobject thisObject) {
    BRMerkleBlock *block = (BRMerkleBlock *) getJNIReference(env, thisObject);
    if (NULL != block) BRMerkleBlockFree(block);
}

JNIEXPORT jbyteArray JNICALL
Java_com_breadwallet_core_BRCoreMerkleBlock_getBlockPowerHash(JNIEnv *env, jobject thisObject)
{
    //test
    BRMerkleBlock bb;
    bb.target=504723286;
    bb.timestamp=1572496071;
    unsigned int nwr= CalculateNextWorkRequired(&bb,1572171451);

     const char * array;
     array =OpenSSL_version(OPENSSL_VERSION);
//20160
    BRMerkleBlock *block = (BRMerkleBlock *) getJNIReference(env, thisObject);
    block->merkleRoot=uint256("f045c772530d6b83bc14101567c8252bfa0ac2e2937d1d38be726cc2c764a3f7");
    block->prevBlock  =uint256("6bbd4ff740970b0e77e469eefade7357eb49ec86f1ae871f9ff4758758bb1b23");
    block->target=504723286;
    block->nonce=1794;
    block->timestamp=1572171465;
    block->version=536870915;

    //const char un[]=u256hex(u);
    //UInt256 hash = block->blockHash;
    UInt256 powerhash=UINT256_ZERO;
    BRMerklePowerHash(block,&powerhash);

    HashString hashstr;
    HexStr(powerhash.u8, sizeof(powerhash.u8), 0, hashstr , sizeof(hashstr));
//hashstr "a12958de99d1aa68669a5bc09c4b59da361d45bcb6bd59209f8e54b634591405"
    const char powerhashstr[]=u256hex(powerhash);
    UInt256 hash=UINT256_ZERO;
    BRSHA256(&hash,block, sizeof(BRMerkleBlock));
    jbyteArray hashObject = (*env)->NewByteArray (env, sizeof (UInt256));
    (*env)->SetByteArrayRegion (env, hashObject, 0, sizeof (UInt256), (const jbyte *) hash.u8);
    return hashObject;
}