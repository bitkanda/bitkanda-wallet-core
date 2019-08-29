package com.breadwallet.crypto;

import com.breadwallet.crypto.errors.FeeEstimationError;
import com.breadwallet.crypto.utility.CompletionHandler;
import com.google.common.base.Optional;

public interface PaymentProtocolRequest {

    boolean isSecure();

    Optional<String> getMemo();

    Optional<String> getPaymentUrl();

    Optional<? extends Amount> getTotalAmount();

    Optional<? extends Address> getPrimaryTarget();

    Optional<String> getCommonName();

    Optional<? extends NetworkFee> getRequiredNetworkFee();

    // TODO(fix): Add isValid()

    void estimate(NetworkFee fee, CompletionHandler<TransferFeeBasis, FeeEstimationError> completion);

    Optional<? extends Transfer> createTransfer(TransferFeeBasis feeBasis);

    boolean signTransfer(Transfer transfer, byte[] phraseUtf8);

    void submitTransfer(Transfer transfer);

    Optional<? extends PaymentProtocolPayment> createPayment(Transfer transfer);
}
