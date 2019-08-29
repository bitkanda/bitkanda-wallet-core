package com.breadwallet.crypto;

import com.google.common.base.Optional;

public interface PaymentProtocolPaymentAck {

    Optional<String> getMemo();
}
