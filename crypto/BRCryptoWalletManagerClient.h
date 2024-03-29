//
//  BRCryptoWalletManagerClient.h
//  BRCrypto
//
//  Created by Michael Carrara on 6/19/19.
//  Copyright © 2019 breadwallet. All rights reserved.
//
//  See the LICENSE file at the project root for license information.
//  See the CONTRIBUTORS file at the project root for a list of contributors.

#ifndef BRCryptoWalletManagerClient_h
#define BRCryptoWalletManagerClient_h

#include "BRCryptoBase.h"
#include "BRCryptoNetwork.h"
#include "BRCryptoAccount.h"
#include "BRCryptoStatus.h"
#include "BRCryptoTransfer.h"
#include "BRCryptoWallet.h"

#include "ethereum/BREthereum.h"
#include "bitcoin/BRWalletManager.h"
#include "generic/BRGenericWalletManager.h"

#ifdef __cplusplus
extern "C" {
#endif

    typedef void *BRCryptoCWMClientContext;

    typedef struct BRCryptoCWMClientCallbackStateRecord *BRCryptoCWMClientCallbackState;

    typedef void
        (*BRCryptoCWMEthGetEtherBalanceCallback) (BRCryptoCWMClientContext context,
                                                  OwnershipGiven BRCryptoWalletManager manager,
                                                  OwnershipGiven BRCryptoCWMClientCallbackState callbackState,
                                                  OwnershipKept const char *network,
                                                  OwnershipKept const char *address);

    typedef void
        (*BRCryptoCWMEthGetTokenBalanceCallback) (BRCryptoCWMClientContext context,
                                                  OwnershipGiven BRCryptoWalletManager manager,
                                                  OwnershipGiven BRCryptoCWMClientCallbackState callbackState,
                                                  OwnershipKept const char *network,
                                                  OwnershipKept const char *address,
                                                  OwnershipKept const char *tokenAddress);

    typedef void
        (*BRCryptoCWMEthGetGasPriceCallback) (BRCryptoCWMClientContext context,
                                              OwnershipGiven BRCryptoWalletManager manager,
                                              OwnershipGiven BRCryptoCWMClientCallbackState callbackState,
                                              OwnershipKept const char *network);

    typedef void
        (*BRCryptoCWMEthEstimateGasCallback) (BRCryptoCWMClientContext context,
                                              OwnershipGiven BRCryptoWalletManager manager,
                                              OwnershipGiven BRCryptoCWMClientCallbackState callbackState,
                                              OwnershipKept const char *network,
                                              OwnershipKept const char *from,
                                              OwnershipKept const char *to,
                                              OwnershipKept const char *amount,
                                              OwnershipKept const char *price,
                                              OwnershipKept const char *data);

    typedef void
        (*BRCryptoCWMEthSubmitTransactionCallback) (BRCryptoCWMClientContext context,
                                                    OwnershipGiven BRCryptoWalletManager manager,
                                                    OwnershipGiven BRCryptoCWMClientCallbackState callbackState,
                                                    OwnershipKept const char *network,
                                                    OwnershipKept const char *transaction);

    typedef void
        (*BRCryptoCWMEthGetTransactionsCallback) (BRCryptoCWMClientContext context,
                                                  OwnershipGiven BRCryptoWalletManager manager,
                                                  OwnershipGiven BRCryptoCWMClientCallbackState callbackState,
                                                  OwnershipKept const char *network,
                                                  OwnershipKept const char *address,
                                                  uint64_t begBlockNumber,
                                                  uint64_t endBlockNumber);

    typedef void
        (*BRCryptoCWMEthGetLogsCallback) (BRCryptoCWMClientContext context,
                                          OwnershipGiven BRCryptoWalletManager manager,
                                          OwnershipGiven BRCryptoCWMClientCallbackState callbackState,
                                          OwnershipKept const char *network,
                                          OwnershipKept const char *contract,
                                          OwnershipKept const char *address,
                                          OwnershipKept const char *event,
                                          uint64_t begBlockNumber,
                                          uint64_t endBlockNumber);

    typedef void
        (*BRCryptoCWMEthGetBlocksCallback) (BRCryptoCWMClientContext context,
                                            OwnershipGiven BRCryptoWalletManager manager,
                                            OwnershipGiven BRCryptoCWMClientCallbackState callbackState,
                                            OwnershipKept const char *network,
                                            OwnershipKept const char *address, // disappears immediately
                                            BREthereumSyncInterestSet interests,
                                            uint64_t blockNumberStart,
                                            uint64_t blockNumberStop);

    typedef void
        (*BRCryptoCWMEthGetTokensCallback) (BRCryptoCWMClientContext context,
                                            OwnershipGiven BRCryptoWalletManager manager,
                                            OwnershipGiven BRCryptoCWMClientCallbackState callbackState);

    typedef void
        (*BRCryptoCWMEthGetBlockNumberCallback) (BRCryptoCWMClientContext context,
                                                 OwnershipGiven BRCryptoWalletManager manager,
                                                 OwnershipGiven BRCryptoCWMClientCallbackState callbackState,
                                                 OwnershipKept const char *network);

    typedef void
        (*BRCryptoCWMEthGetNonceCallback) (BRCryptoCWMClientContext context,
                                           OwnershipGiven BRCryptoWalletManager manager,
                                           OwnershipGiven BRCryptoCWMClientCallbackState callbackState,
                                           OwnershipKept const char *network,
                                           OwnershipKept const char *address);

    typedef struct {
        BRCryptoCWMEthGetEtherBalanceCallback funcGetEtherBalance;
        BRCryptoCWMEthGetTokenBalanceCallback funcGetTokenBalance;
        BRCryptoCWMEthGetGasPriceCallback funcGetGasPrice;
        BRCryptoCWMEthEstimateGasCallback funcEstimateGas;
        BRCryptoCWMEthSubmitTransactionCallback funcSubmitTransaction;
        BRCryptoCWMEthGetTransactionsCallback funcGetTransactions; // announce one-by-one
        BRCryptoCWMEthGetLogsCallback funcGetLogs; // announce one-by-one
        BRCryptoCWMEthGetBlocksCallback funcGetBlocks;
        BRCryptoCWMEthGetTokensCallback funcGetTokens; // announce one-by-one
        BRCryptoCWMEthGetBlockNumberCallback funcGetBlockNumber;
        BRCryptoCWMEthGetNonceCallback funcGetNonce;
    } BRCryptoCWMClientETH;

    typedef void
    (*BRCryptoCWMBtcGetBlockNumberCallback) (BRCryptoCWMClientContext context,
                                             OwnershipGiven BRCryptoWalletManager manager,
                                             OwnershipGiven BRCryptoCWMClientCallbackState callbackState);

    typedef void
    (*BRCryptoCWMBtcGetTransactionsCallback) (BRCryptoCWMClientContext context,
                                              OwnershipGiven BRCryptoWalletManager manager,
                                              OwnershipGiven BRCryptoCWMClientCallbackState callbackState,
                                              OwnershipKept const char **addresses,
                                              size_t addressCount,
                                              uint64_t begBlockNumber,
                                              uint64_t endBlockNumber);

    typedef void
    (*BRCryptoCWMBtcSubmitTransactionCallback) (BRCryptoCWMClientContext context,
                                                OwnershipGiven BRCryptoWalletManager manager,
                                                OwnershipGiven BRCryptoCWMClientCallbackState callbackState,
                                                OwnershipKept uint8_t *transaction,
                                                size_t transactionLength,
                                                OwnershipKept const char *hashAsHex);

    typedef struct {
        BRCryptoCWMBtcGetBlockNumberCallback  funcGetBlockNumber;
        BRCryptoCWMBtcGetTransactionsCallback funcGetTransactions;
        BRCryptoCWMBtcSubmitTransactionCallback funcSubmitTransaction;
    } BRCryptoCWMClientBTC;

    /// MARK: GEN Callbacks

    typedef void
    (*BRCryptoCWMGenGetBlockNumberCallback) (BRCryptoCWMClientContext context,
                                             OwnershipGiven BRCryptoWalletManager manager,
                                             OwnershipGiven BRCryptoCWMClientCallbackState callbackState);

    typedef void
    (*BRCryptoCWMGenGetTransactionsCallback) (BRCryptoCWMClientContext context,
                                              OwnershipGiven BRCryptoWalletManager manager,
                                              OwnershipGiven BRCryptoCWMClientCallbackState callbackState,
                                              OwnershipKept const char *address,
                                              uint64_t begBlockNumber,
                                              uint64_t endBlockNumber);

    typedef void
    (*BRCryptoCWMGenSubmitTransactionCallback) (BRCryptoCWMClientContext context,
                                                OwnershipGiven BRCryptoWalletManager manager,
                                                OwnershipGiven BRCryptoCWMClientCallbackState callbackState,
                                                OwnershipKept uint8_t *transaction,
                                                size_t transactionLength,
                                                OwnershipKept const char *hashAsHex);

    typedef struct {
        BRCryptoCWMGenGetBlockNumberCallback  funcGetBlockNumber;
        BRCryptoCWMGenGetTransactionsCallback funcGetTransactions;
        BRCryptoCWMGenSubmitTransactionCallback funcSubmitTransaction;
    } BRCryptoCWMClientGEN;

    typedef struct {
        BRCryptoCWMClientContext context;
        BRCryptoCWMClientBTC btc;
        BRCryptoCWMClientETH eth;
        BRCryptoCWMClientGEN gen;
    } BRCryptoCWMClient;

    extern BRWalletManagerClient
    cryptoWalletManagerClientCreateBTCClient (OwnershipKept BRCryptoWalletManager cwm);

    extern BREthereumClient
    cryptoWalletManagerClientCreateETHClient (OwnershipKept BRCryptoWalletManager cwm);

    extern BRGenericClient
    cryptoWalletManagerClientCreateGENClient (BRCryptoWalletManager cwm);

    extern void
    cwmAnnounceGetBlockNumberSuccessAsInteger (OwnershipKept BRCryptoWalletManager cwm,
                                               OwnershipGiven BRCryptoCWMClientCallbackState callbackState,
                                               uint64_t blockNumber);

    extern void
    cwmAnnounceGetBlockNumberSuccessAsString (OwnershipKept BRCryptoWalletManager cwm,
                                              OwnershipGiven BRCryptoCWMClientCallbackState callbackState,
                                              OwnershipKept const char *blockNumber);

    extern void
    cwmAnnounceGetBlockNumberFailure (OwnershipKept BRCryptoWalletManager cwm,
                                      OwnershipGiven BRCryptoCWMClientCallbackState callbackState);

    extern void
    cwmAnnounceGetTransactionsItemBTC (OwnershipKept BRCryptoWalletManager cwm,
                                       OwnershipGiven BRCryptoCWMClientCallbackState callbackState,
                                       OwnershipKept uint8_t *transaction,
                                       size_t transactionLength,
                                       uint64_t timestamp,
                                       uint64_t blockHeight);

    extern void
    cwmAnnounceGetTransactionsItemETH (OwnershipKept BRCryptoWalletManager cwm,
                                       OwnershipGiven BRCryptoCWMClientCallbackState callbackState,
                                       OwnershipKept const char *hash,
                                       OwnershipKept const char *from,
                                       OwnershipKept const char *to,
                                       OwnershipKept const char *contract,
                                       OwnershipKept const char *amount, // value
                                       OwnershipKept const char *gasLimit,
                                       OwnershipKept const char *gasPrice,
                                       OwnershipKept const char *data,
                                       OwnershipKept const char *nonce,
                                       OwnershipKept const char *gasUsed,
                                       OwnershipKept const char *blockNumber,
                                       OwnershipKept const char *blockHash,
                                       OwnershipKept const char *blockConfirmations,
                                       OwnershipKept const char *blockTransactionIndex,
                                       OwnershipKept const char *blockTimestamp,
                                       // cumulative gas used,
                                       // confirmations
                                       // txreceipt_status
                                       OwnershipKept const char *isError);

    extern void
    cwmAnnounceGetTransactionsItemGEN (BRCryptoWalletManager cwm,
                                       BRCryptoCWMClientCallbackState callbackState,
                                       uint8_t *transaction,
                                       size_t transactionLength,
                                       uint64_t timestamp,
                                       uint64_t blockHeight);

    extern void
    cwmAnnounceGetTransactionsComplete (OwnershipKept BRCryptoWalletManager cwm,
                                        OwnershipGiven BRCryptoCWMClientCallbackState callbackState,
                                        BRCryptoBoolean success);

    extern void
    cwmAnnounceSubmitTransferSuccess (OwnershipKept BRCryptoWalletManager cwm,
                                      OwnershipGiven BRCryptoCWMClientCallbackState callbackState);

    extern void
    cwmAnnounceSubmitTransferSuccessForHash (OwnershipKept BRCryptoWalletManager cwm,
                                             OwnershipGiven BRCryptoCWMClientCallbackState callbackState,
                                             const char *hash);

    extern void
    cwmAnnounceSubmitTransferFailure (OwnershipKept BRCryptoWalletManager cwm,
                                      OwnershipGiven BRCryptoCWMClientCallbackState callbackState);

    extern void
    cwmAnnounceGetBalanceSuccess (OwnershipKept BRCryptoWalletManager cwm,
                                  OwnershipGiven BRCryptoCWMClientCallbackState callbackState,
                                  const char *balance);

    extern void
    cwmAnnounceGetBalanceFailure (OwnershipKept BRCryptoWalletManager cwm,
                                  OwnershipGiven BRCryptoCWMClientCallbackState callbackState);

    extern void
    cwmAnnounceGetBlocksSuccess (OwnershipKept BRCryptoWalletManager cwm,
                                 OwnershipGiven BRCryptoCWMClientCallbackState callbackState,
                                 int blockNumbersCount,
                                 uint64_t *blockNumbers);

    extern void
    cwmAnnounceGetBlocksFailure (OwnershipKept BRCryptoWalletManager cwm,
                                 OwnershipGiven BRCryptoCWMClientCallbackState callbackState);

    extern void
    cwmAnnounceGetGasPriceSuccess (OwnershipKept BRCryptoWalletManager cwm,
                                   OwnershipGiven BRCryptoCWMClientCallbackState callbackState,
                                   const char *gasPrice);

    extern void
    cwmAnnounceGetGasPriceFailure (OwnershipKept BRCryptoWalletManager cwm,
                                   OwnershipGiven BRCryptoCWMClientCallbackState callbackState);

    extern void
    cwmAnnounceGetGasEstimateSuccess (OwnershipKept BRCryptoWalletManager cwm,
                                      OwnershipGiven BRCryptoCWMClientCallbackState callbackState,
                                      const char *gasEstimate,
                                      const char *gasPrice);

    extern void
    cwmAnnounceGetGasEstimateFailure (OwnershipKept BRCryptoWalletManager cwm,
                                      OwnershipGiven BRCryptoCWMClientCallbackState callbackState,
                                      BRCryptoStatus status);

    extern void
    cwmAnnounceGetLogsItem(OwnershipKept BRCryptoWalletManager cwm,
                           OwnershipGiven BRCryptoCWMClientCallbackState callbackState,
                           OwnershipKept const char *strHash,
                           OwnershipKept const char *strContract,
                           int topicCount,
                           OwnershipKept const char **arrayTopics,
                           OwnershipKept const char *strData,
                           OwnershipKept const char *strGasPrice,
                           OwnershipKept const char *strGasUsed,
                           OwnershipKept const char *strLogIndex,
                           OwnershipKept const char *strBlockNumber,
                           OwnershipKept const char *strBlockTransactionIndex,
                           OwnershipKept const char *strBlockTimestamp);

    extern void
    cwmAnnounceGetLogsComplete(OwnershipKept BRCryptoWalletManager cwm,
                               OwnershipGiven BRCryptoCWMClientCallbackState callbackState,
                               BRCryptoBoolean success);

    extern void
    cwmAnnounceGetTokensItem (OwnershipKept BRCryptoWalletManager cwm,
                              OwnershipGiven BRCryptoCWMClientCallbackState callbackState,
                              OwnershipKept const char *address,
                              OwnershipKept const char *symbol,
                              OwnershipKept const char *name,
                              OwnershipKept const char *description,
                              unsigned int decimals,
                              OwnershipKept const char *strDefaultGasLimit,
                              OwnershipKept const char *strDefaultGasPrice);

    extern void
    cwmAnnounceGetTokensComplete (OwnershipKept BRCryptoWalletManager cwm,
                                  OwnershipGiven BRCryptoCWMClientCallbackState callbackState,
                                  BRCryptoBoolean success);

    extern void
    cwmAnnounceGetNonceSuccess (OwnershipKept BRCryptoWalletManager cwm,
                                OwnershipGiven BRCryptoCWMClientCallbackState callbackState,
                                OwnershipKept const char *address,
                                OwnershipKept const char *nonce);

    extern void
    cwmAnnounceGetNonceFailure (BRCryptoWalletManager cwm,
                                OwnershipGiven BRCryptoCWMClientCallbackState callbackState);

#ifdef __cplusplus
}
#endif

#endif /* BRCryptoWalletManagerClient_h */
