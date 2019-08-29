package com.breadwallet.corenative.crypto;

import com.breadwallet.corenative.CryptoLibrary;
import com.breadwallet.corenative.utility.SizeT;
import com.google.common.base.Optional;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;

public class BRCryptoPaymentProtocolRequest extends PointerType {

    public static Optional<BRCryptoPaymentProtocolRequest> createForBip70(CoreBRCryptoNetwork cryptoNetwork,
                                                                          CoreBRCryptoCurrency cryptoCurrency,
                                                                          BRCryptoPayProtReqBitPayAndBip70Callbacks.ByValue callbacks,
                                                                          byte[] serialization) {
        return Optional.fromNullable(
                CryptoLibrary.INSTANCE.cryptoPaymentProtocolRequestCreateForBip70(
                        cryptoNetwork.asBRCryptoNetwork(),
                        cryptoCurrency.asBRCryptoCurrency(),
                        callbacks,
                        serialization,
                        new SizeT(serialization.length))
        );
    }

    public BRCryptoPaymentProtocolRequest(Pointer address) {
        super(address);
    }

    public BRCryptoPaymentProtocolRequest() {
        super();
    }

    public boolean isSecure() {
        return BRCryptoBoolean.CRYPTO_TRUE == CryptoLibrary.INSTANCE.cryptoPaymentProtocolRequestIsSecure(this);
    }

    public Optional<String> getMemo() {
        return Optional.fromNullable(CryptoLibrary.INSTANCE.cryptoPaymentProtocolRequestGetMemo(this))
                .transform(v -> v.getString(0, "UTF-8"));
    }

    public Optional<String> getPaymentUrl() {
        return Optional.fromNullable(CryptoLibrary.INSTANCE.cryptoPaymentProtocolRequestGetPaymentURL(this))
                .transform(v -> v.getString(0, "UTF-8"));
    }

    public Optional<CoreBRCryptoAmount> getTotalAmount() {
        return Optional.fromNullable(CryptoLibrary.INSTANCE.cryptoPaymentProtocolRequestGetTotalAmount(this))
                .transform(OwnedBRCryptoAmount::new);
    }

    public Optional<CoreBRCryptoNetworkFee> getRequiredNetworkFee() {
        return Optional.fromNullable(CryptoLibrary.INSTANCE.cryptoPaymentProtocolRequestGetRequiredNetworkFee(this))
                .transform(OwnedBRCryptoNetworkFee::new);
    }

    public Optional<CoreBRCryptoAddress> getPrimaryTargetAddress() {
        return Optional.fromNullable(CryptoLibrary.INSTANCE.cryptoPaymentProtocolRequestGetPrimaryTargetAddress(this))
                .transform(OwnedBRCryptoAddress::new);
    }

    public Optional<String> getCommonName() {
        Pointer returnValue = CryptoLibrary.INSTANCE.cryptoPaymentProtocolRequestGetCommonName(this);
        try {
            return Optional.fromNullable(returnValue)
                    .transform(v -> v.getString(0, "UTF-8"));
        } finally {
            if (returnValue != null) Native.free(Pointer.nativeValue(returnValue));
        }
    }

    public BRCryptoPaymentProtocolError isValid() {
        return BRCryptoPaymentProtocolError.fromNative(CryptoLibrary.INSTANCE.cryptoPaymentProtocolRequestIsValid(this));
    }

    public static class OwnedBRCryptoPaymentProtocolRequest extends BRCryptoPaymentProtocolRequest {

        public OwnedBRCryptoPaymentProtocolRequest(Pointer address) {
            super(address);
        }

        public OwnedBRCryptoPaymentProtocolRequest() {
            super();
        }

        @Override
        protected void finalize() {
            if (null != getPointer()) {
                CryptoLibrary.INSTANCE.cryptoPaymentProtocolRequestGive(this);
            }
        }
    }
}
