package com.breadwallet.corecrypto;

import com.breadwallet.corenative.crypto.BRCryptoPayProtReqBitPayAndBip70Callbacks;
import com.breadwallet.corenative.crypto.BRCryptoPayProtReqBitPayAndBip70Callbacks.BRCryptoPayProtReqBitPayAndBip70CommonNameExtractor;
import com.breadwallet.corenative.crypto.BRCryptoPayProtReqBitPayAndBip70Callbacks.BRCryptoPayProtReqBitPayAndBip70Validator;
import com.breadwallet.corenative.crypto.BRCryptoPaymentProtocolRequest;
import com.breadwallet.corenative.utility.SizeT;
import com.breadwallet.crypto.TransferFeeBasis;
import com.breadwallet.crypto.errors.FeeEstimationError;
import com.breadwallet.crypto.utility.CompletionHandler;
import com.google.common.base.Optional;
import com.sun.jna.Pointer;

/* package */
final class PaymentProtocolRequest implements com.breadwallet.crypto.PaymentProtocolRequest {

    /* package */ static Optional<PaymentProtocolRequest> createForBip70(Wallet wallet, byte[] serialization) {
        // TODO(fix): Add validate supported

        WalletManager manager = wallet.getWalletManager();
        Network network = manager.getNetwork();
        Currency currency = wallet.getCurrency();
        return BRCryptoPaymentProtocolRequest.createForBip70(
                network.getCoreBRCryptoNetwork(),
                currency.getCoreBRCryptoCurrency(),
                BIT_PAY_AND_BIP_70_CALLBACKS,
                serialization).transform(v -> new PaymentProtocolRequest(v, wallet));
    }

    private final BRCryptoPaymentProtocolRequest core;
    private final WalletManager manager;
    private final Wallet wallet;

    private PaymentProtocolRequest(BRCryptoPaymentProtocolRequest core, Wallet wallet) {
        this.core = core;
        this.manager = wallet.getWalletManager();
        this.wallet = wallet;
    }

    @Override
    public boolean isSecure() {
        return core.isSecure();
    }

    @Override
    public Optional<String> getMemo() {
        return core.getMemo();
    }

    @Override
    public Optional<String> getPaymentUrl() {
        return core.getPaymentUrl();
    }

    @Override
    public Optional<Amount> getTotalAmount() {
        return core.getTotalAmount().transform(Amount::create);
    }

    @Override
    public Optional<? extends Address> getPrimaryTarget() {
        return core.getPrimaryTargetAddress().transform(Address::create);
    }

    @Override
    public Optional<String> getCommonName() {
        return core.getCommonName();
    }

    @Override
    public Optional<? extends NetworkFee> getRequiredNetworkFee() {
        return core.getRequiredNetworkFee().transform(NetworkFee::create);
    }

    @Override
    public void estimate(com.breadwallet.crypto.NetworkFee fee, CompletionHandler<TransferFeeBasis, FeeEstimationError> completion) {
        wallet.estimateFee(core, fee, completion);
    }

    @Override
    public Optional<? extends Transfer> createTransfer(TransferFeeBasis feeBasis) {
        return wallet.createTransfer(core, feeBasis);
    }

    @Override
    public boolean signTransfer(com.breadwallet.crypto.Transfer transfer, byte[] phraseUtf8) {
        return manager.sign(transfer, phraseUtf8);
    }

    @Override
    public void submitTransfer(com.breadwallet.crypto.Transfer transfer) {
        manager.submit(transfer);
    }

    @Override
    public Optional<PaymentProtocolPayment> createPayment(com.breadwallet.crypto.Transfer transfer) {
        return PaymentProtocolPayment.create(this, Transfer.from(transfer), wallet.getTarget());
    }

    /* package */
    BRCryptoPaymentProtocolRequest getBRCryptoPaymentProtocolRequest() {
        return core;
    }

    private static final BRCryptoPayProtReqBitPayAndBip70Validator BIT_PAY_AND_BIP70_VALIDATOR = (req, context,
                                                                                                  pkiType, expires, certBytes, certLengths, certCount, digest, digestLen, sig, sigLen) -> {
        // TODO(fix): Implement me!
        return 0;
    };

    private static final BRCryptoPayProtReqBitPayAndBip70CommonNameExtractor BIT_PAY_AND_BIP_70_COMMON_NAME_EXTRACTOR = (req, context, pkiType, certBytes, certLengths, certCount) -> {
        // TODO(fix): Implement me!
        return null;
    };

    private static final BRCryptoPayProtReqBitPayAndBip70Callbacks.ByValue BIT_PAY_AND_BIP_70_CALLBACKS = new BRCryptoPayProtReqBitPayAndBip70Callbacks.ByValue(
            null,
            BIT_PAY_AND_BIP70_VALIDATOR,
            BIT_PAY_AND_BIP_70_COMMON_NAME_EXTRACTOR
    );
}
