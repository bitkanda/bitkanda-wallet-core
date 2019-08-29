package com.breadwallet.corecrypto;

import com.breadwallet.corenative.crypto.BRCryptoPaymentProtocolPayment;
import com.google.common.base.Optional;

/* package */
final class PaymentProtocolPayment implements com.breadwallet.crypto.PaymentProtocolPayment {

    public static Optional<PaymentProtocolPayment> create(PaymentProtocolRequest request, Transfer transfer, Address target) {
        return BRCryptoPaymentProtocolPayment.create(
                request.getBRCryptoPaymentProtocolRequest(),
                transfer.getCoreBRCryptoTransfer(),
                target.getCoreBRCryptoAddress()
        ).transform(PaymentProtocolPayment::new);
    }

    private final BRCryptoPaymentProtocolPayment core;

    private PaymentProtocolPayment(BRCryptoPaymentProtocolPayment core) {
        this.core = core;
    }

    @Override
    public Optional<byte[]> encode() {
        return core.encode();
    }
}
