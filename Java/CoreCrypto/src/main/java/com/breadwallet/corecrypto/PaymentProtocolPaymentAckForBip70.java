package com.breadwallet.corecrypto;

import com.breadwallet.corenative.crypto.BRCryptoPaymentProtocolPaymentAck;
import com.google.common.base.Optional;

/* package */
final class PaymentProtocolPaymentAckForBip70 implements com.breadwallet.crypto.PaymentProtocolPaymentAck {

    private final BRCryptoPaymentProtocolPaymentAck core;

    private PaymentProtocolPaymentAckForBip70(BRCryptoPaymentProtocolPaymentAck core) {
        this.core = core;
    }

    @Override
    public Optional<String> getMemo() {
        return core.getMemo();
    }
}
