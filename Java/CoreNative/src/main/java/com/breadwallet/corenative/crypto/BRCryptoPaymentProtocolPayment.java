package com.breadwallet.corenative.crypto;

import com.breadwallet.corenative.CryptoLibrary;
import com.breadwallet.corenative.utility.SizeTByReference;
import com.google.common.base.Optional;
import com.google.common.primitives.UnsignedInts;
import com.google.common.primitives.UnsignedLong;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;

public class BRCryptoPaymentProtocolPayment extends PointerType {

    public static Optional<BRCryptoPaymentProtocolPayment> create(BRCryptoPaymentProtocolRequest request,
                                                                  CoreBRCryptoTransfer transfer,
                                                                  CoreBRCryptoAddress refundAddress) {
        return Optional.fromNullable(
                CryptoLibrary.INSTANCE.cryptoPaymentProtocolPaymentCreate(
                        request,
                        transfer.asBRCryptoTransfer(),
                        refundAddress.asBRCryptoAddress())
        );
    }

    public BRCryptoPaymentProtocolPayment(Pointer address) {
        super(address);
    }

    public BRCryptoPaymentProtocolPayment() {
        super();
    }

    public Optional<byte[]> encode() {
        SizeTByReference length = new SizeTByReference(UnsignedLong.ZERO);
        return Optional.fromNullable(CryptoLibrary.INSTANCE.cryptoPaymentProtocolPaymentEncode(this, length))
                .transform(v -> v.getByteArray(0, UnsignedInts.checkedCast(length.getValue().longValue())));
    }

    public static class OwnedBRCryptoPaymentProtocolPayment extends BRCryptoPaymentProtocolPayment {

        public OwnedBRCryptoPaymentProtocolPayment(Pointer address) {
            super(address);
        }

        public OwnedBRCryptoPaymentProtocolPayment() {
            super();
        }

        @Override
        protected void finalize() {
            if (null != getPointer()) {
                CryptoLibrary.INSTANCE.cryptoPaymentProtocolPaymentGive(this);
            }
        }
    }
}
