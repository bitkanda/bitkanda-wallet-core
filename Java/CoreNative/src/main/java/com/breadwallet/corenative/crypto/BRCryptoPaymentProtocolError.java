package com.breadwallet.corenative.crypto;

import com.google.common.collect.ImmutableMap;

import static com.google.common.base.Preconditions.checkState;

public enum BRCryptoPaymentProtocolError {

    CRYPTO_PAYMENT_PROTOCOL_ERROR_NONE {
        @Override
        public int toNative() {
            return 0;
        }
    },

    CRYPTO_PAYMENT_PROTOCOL_ERROR_CERT_MISSING {
        @Override
        public int toNative() {
            return 1;
        }
    },

    CRYPTO_PAYMENT_PROTOCOL_ERROR_CERT_NOT_TRUSTED {
        @Override
        public int toNative() {
            return 2;
        }
    },

    CRYPTO_PAYMENT_PROTOCOL_ERROR_SIGNATURE_TYPE_NOT_SUPPORTED {
        @Override
        public int toNative() {
            return 3;
        }
    },

    CRYPTO_PAYMENT_PROTOCOL_ERROR_SIGNATURE_VERIFICATION_FAILED {
        @Override
        public int toNative() {
            return 4;
        }
    },

    CRYPTO_PAYMENT_PROTOCOL_ERROR_EXPIRED{
        @Override
        public int toNative() {
            return 5;
        }
    };

    private static final ImmutableMap<Integer, BRCryptoPaymentProtocolError> LOOKUP;

    static {
        ImmutableMap.Builder<Integer, BRCryptoPaymentProtocolError> b = ImmutableMap.builder();

        b.put(CRYPTO_PAYMENT_PROTOCOL_ERROR_NONE.toNative(),                          CRYPTO_PAYMENT_PROTOCOL_ERROR_NONE);
        b.put(CRYPTO_PAYMENT_PROTOCOL_ERROR_CERT_MISSING.toNative(),                  CRYPTO_PAYMENT_PROTOCOL_ERROR_CERT_MISSING);
        b.put(CRYPTO_PAYMENT_PROTOCOL_ERROR_CERT_NOT_TRUSTED.toNative(),              CRYPTO_PAYMENT_PROTOCOL_ERROR_CERT_NOT_TRUSTED);
        b.put(CRYPTO_PAYMENT_PROTOCOL_ERROR_SIGNATURE_TYPE_NOT_SUPPORTED.toNative(),  CRYPTO_PAYMENT_PROTOCOL_ERROR_SIGNATURE_TYPE_NOT_SUPPORTED);
        b.put(CRYPTO_PAYMENT_PROTOCOL_ERROR_SIGNATURE_VERIFICATION_FAILED.toNative(), CRYPTO_PAYMENT_PROTOCOL_ERROR_SIGNATURE_VERIFICATION_FAILED);
        b.put(CRYPTO_PAYMENT_PROTOCOL_ERROR_EXPIRED.toNative(),                       CRYPTO_PAYMENT_PROTOCOL_ERROR_EXPIRED);

        LOOKUP = b.build();
    }

    public static BRCryptoPaymentProtocolError fromNative(int nativeValue) {
        BRCryptoPaymentProtocolError status = LOOKUP.get(nativeValue);
        checkState(null != status);
        return status;
    }

    public abstract int toNative();
}
