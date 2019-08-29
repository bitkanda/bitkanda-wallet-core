package com.breadwallet.crypto;

import com.google.common.base.Optional;

public interface PaymentProtocolPayment {

    Optional<byte[]> encode();
}
