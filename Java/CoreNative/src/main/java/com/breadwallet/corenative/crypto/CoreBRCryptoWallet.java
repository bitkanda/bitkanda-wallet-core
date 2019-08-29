/*
 * Created by Michael Carrara <michael.carrara@breadwallet.com> on 5/31/18.
 * Copyright (c) 2018 Breadwinner AG.  All right reserved.
 *
 * See the LICENSE file at the project root for license information.
 * See the CONTRIBUTORS file at the project root for a list of contributors.
 */
package com.breadwallet.corenative.crypto;

import com.google.common.base.Optional;
import com.sun.jna.Pointer;

import java.util.List;

public interface CoreBRCryptoWallet {

    static CoreBRCryptoWallet createOwned(BRCryptoWallet wallet) {
        return new OwnedBRCryptoWallet(wallet);
    }

    CoreBRCryptoAmount getBalance();

    List<CoreBRCryptoTransfer> getTransfers();

    boolean containsTransfer(CoreBRCryptoTransfer transfer);

    CoreBRCryptoCurrency getCurrency();

    CoreBRCryptoUnit getUnitForFee();

    CoreBRCryptoUnit getUnit();

    int getState();

    void setState(int state);

    CoreBRCryptoFeeBasis getDefaultFeeBasis();

    void setDefaultFeeBasis(CoreBRCryptoFeeBasis feeBasis);

    CoreBRCryptoAddress getSourceAddress(int addressScheme);

    CoreBRCryptoAddress getTargetAddress(int addressScheme);

    CoreBRCryptoTransfer createTransfer(CoreBRCryptoAddress target, CoreBRCryptoAmount amount, CoreBRCryptoFeeBasis estimatedFeeBasis);

    Optional<CoreBRCryptoTransfer> createTransferForWalletSweep(BRCryptoWalletSweeper sweeper, CoreBRCryptoFeeBasis estimatedFeeBasis);

    Optional<CoreBRCryptoTransfer> createTransferForPaymentProtocolRequest(BRCryptoPaymentProtocolRequest request, CoreBRCryptoFeeBasis estimatedFeeBasis);

    void estimateFeeBasis(Pointer cookie, CoreBRCryptoAddress target, CoreBRCryptoAmount amount, CoreBRCryptoNetworkFee fee);

    void estimateFeeBasisForWalletSweep(Pointer cookie, BRCryptoWalletSweeper sweeper, CoreBRCryptoNetworkFee fee);

    void estimateFeeBasisForPaymentProtocolRequest(Pointer cookie, BRCryptoPaymentProtocolRequest request, CoreBRCryptoNetworkFee fee);

    BRCryptoWallet asBRCryptoWallet();
}
