package com.breadwallet.corenative.crypto;

import com.breadwallet.corenative.CryptoLibrary;
import com.breadwallet.corenative.utility.SizeT;
import com.google.common.base.Optional;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;

public class BRCryptoPaymentProtocolPaymentAck extends PointerType {

    public static Optional<BRCryptoPaymentProtocolPaymentAck> createForBip70(byte[] serialization) {
        return Optional.fromNullable(
                CryptoLibrary.INSTANCE.cryptoPaymentProtocolPaymentACKCreateForBip70(serialization, new SizeT(serialization.length))
        );
    }

    public BRCryptoPaymentProtocolPaymentAck(Pointer address) {
        super(address);
    }

    public BRCryptoPaymentProtocolPaymentAck() {
        super();
    }

    public Optional<String> getMemo() {
        return Optional.fromNullable(CryptoLibrary.INSTANCE.cryptoPaymentProtocolPaymentACKGetMemo(this))
                .transform(v -> v.getString(0, "UTF-8"));
    }

    public static class OwnedBRCryptoPaymentProtocolPaymentAck extends BRCryptoPaymentProtocolPaymentAck {

        public OwnedBRCryptoPaymentProtocolPaymentAck(Pointer address) {
            super(address);
        }

        public OwnedBRCryptoPaymentProtocolPaymentAck() {
            super();
        }

        @Override
        protected void finalize() {
            if (null != getPointer()) {
                CryptoLibrary.INSTANCE.cryptoPaymentProtocolPaymentACKGive(this);
            }
        }
    }
}
