/*
 * Created by Michael Carrara <michael.carrara@breadwallet.com> on 7/1/19.
 * Copyright (c) 2019 Breadwinner AG.  All right reserved.
*
 * See the LICENSE file at the project root for license information.
 * See the CONTRIBUTORS file at the project root for a list of contributors.
 */
package com.breadwallet.corecrypto;

import com.breadwallet.corenative.crypto.BRCryptoAddressScheme;
import com.breadwallet.corenative.crypto.BRCryptoAmount;
import com.breadwallet.corenative.crypto.BRCryptoStatus;
import com.breadwallet.corenative.crypto.BRCryptoTransferDirection;
import com.breadwallet.corenative.crypto.BRCryptoTransferState;
import com.breadwallet.corenative.crypto.BRCryptoWalletManagerState;
import com.breadwallet.corenative.crypto.BRCryptoWalletState;
import com.breadwallet.corenative.support.BRSyncDepth;
import com.breadwallet.corenative.support.BRSyncMode;
import com.breadwallet.corenative.support.BRSyncStoppedReason;
import com.breadwallet.crypto.AddressScheme;
import com.breadwallet.crypto.TransferConfirmation;
import com.breadwallet.crypto.TransferDirection;
import com.breadwallet.crypto.TransferState;
import com.breadwallet.crypto.WalletManagerDisconnectReason;
import com.breadwallet.crypto.WalletManagerSyncDepth;
import com.breadwallet.crypto.WalletManagerMode;
import com.breadwallet.crypto.WalletManagerState;
import com.breadwallet.crypto.WalletManagerSyncStoppedReason;
import com.breadwallet.crypto.WalletState;
import com.breadwallet.crypto.errors.FeeEstimationError;
import com.breadwallet.crypto.errors.FeeEstimationServiceFailureError;
import com.breadwallet.crypto.errors.FeeEstimationServiceUnavailableError;
import com.breadwallet.crypto.errors.TransferSubmitPosixError;
import com.breadwallet.crypto.errors.TransferSubmitUnknownError;
import com.google.common.base.Optional;
import com.google.common.primitives.UnsignedLong;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/* package */
final class Utilities {

    /* package */
    static BRSyncMode walletManagerModeToCrypto(WalletManagerMode mode) {
        switch (mode) {
            case API_ONLY: return BRSyncMode.SYNC_MODE_BRD_ONLY;
            case API_WITH_P2P_SUBMIT: return BRSyncMode.SYNC_MODE_BRD_WITH_P2P_SEND;
            case P2P_ONLY: return BRSyncMode.SYNC_MODE_P2P_ONLY;
            case P2P_WITH_API_SYNC: return BRSyncMode.SYNC_MODE_P2P_WITH_BRD_SYNC;
            default: throw new IllegalArgumentException("Unsupported mode");
        }
    }

    /* package */
    static WalletManagerMode walletManagerModeFromCrypto(BRSyncMode mode) {
        switch (mode) {
            case SYNC_MODE_BRD_ONLY: return WalletManagerMode.API_ONLY;
            case SYNC_MODE_BRD_WITH_P2P_SEND: return WalletManagerMode.API_WITH_P2P_SUBMIT;
            case SYNC_MODE_P2P_ONLY: return WalletManagerMode.P2P_ONLY;
            case SYNC_MODE_P2P_WITH_BRD_SYNC: return WalletManagerMode.P2P_WITH_API_SYNC;
            default: throw new IllegalArgumentException("Unsupported mode");
        }
    }

    /* package */
    static WalletManagerState walletManagerStateFromCrypto(BRCryptoWalletManagerState state) {
        switch (state.type()) {
            case CRYPTO_WALLET_MANAGER_STATE_CREATED: return WalletManagerState.CREATED();
            case CRYPTO_WALLET_MANAGER_STATE_DELETED: return WalletManagerState.DELETED();
            case CRYPTO_WALLET_MANAGER_STATE_CONNECTED: return WalletManagerState.CONNECTED();
            case CRYPTO_WALLET_MANAGER_STATE_SYNCING: return WalletManagerState.SYNCING();
            case CRYPTO_WALLET_MANAGER_STATE_DISCONNECTED:
                switch (state.u.disconnected.reason.type()) {
                    case DISCONNECT_REASON_REQUESTED: return WalletManagerState.DISCONNECTED(
                            WalletManagerDisconnectReason.REQUESTED()
                    );
                    case DISCONNECT_REASON_UNKNOWN: return WalletManagerState.DISCONNECTED(
                            WalletManagerDisconnectReason.UNKNOWN()
                    );
                    case DISCONNECT_REASON_POSIX: return WalletManagerState.DISCONNECTED(
                            WalletManagerDisconnectReason.POSIX(
                                    state.u.disconnected.reason.u.posix.errnum,
                                    state.u.disconnected.reason.getMessage().orNull()
                            )
                    );
                    default: throw new IllegalArgumentException("Unsupported reason");
                }
            default: throw new IllegalArgumentException("Unsupported state");
        }
    }

    /* package */
    static WalletManagerSyncStoppedReason walletManagerSyncStoppedReasonFromCrypto(BRSyncStoppedReason reason) {
        switch (reason.type()) {
            case SYNC_STOPPED_REASON_COMPLETE: return WalletManagerSyncStoppedReason.COMPLETE();
            case SYNC_STOPPED_REASON_REQUESTED: return WalletManagerSyncStoppedReason.REQUESTED();
            case SYNC_STOPPED_REASON_UNKNOWN: return WalletManagerSyncStoppedReason.UNKNOWN();
            case SYNC_STOPPED_REASON_POSIX: return WalletManagerSyncStoppedReason.POSIX(
                    reason.u.posix.errnum,
                    reason.getMessage().orNull()
            );
            default: throw new IllegalArgumentException("Unsupported reason");
        }
    }

    /* package */
    static WalletState walletStateFromCrypto(BRCryptoWalletState state) {
        switch (state) {
            case CRYPTO_WALLET_STATE_CREATED: return WalletState.CREATED;
            case CRYPTO_WALLET_STATE_DELETED: return WalletState.DELETED;
            default: throw new IllegalArgumentException("Unsupported state");
        }
    }

    /* package */
    static TransferDirection transferDirectionFromCrypto(BRCryptoTransferDirection direction) {
        switch (direction) {
            case CRYPTO_TRANSFER_RECEIVED: return TransferDirection.RECEIVED;
            case CRYPTO_TRANSFER_SENT: return TransferDirection.SENT;
            case CRYPTO_TRANSFER_RECOVERED: return TransferDirection.RECOVERED;
            default: throw new IllegalArgumentException("Unsupported direction");
        }
    }

    /* package */
    static TransferState transferStateFromCrypto(BRCryptoTransferState state) {
        switch (state.type()) {
            case CRYPTO_TRANSFER_STATE_CREATED: return TransferState.CREATED();
            case CRYPTO_TRANSFER_STATE_DELETED: return TransferState.DELETED();
            case CRYPTO_TRANSFER_STATE_SIGNED: return TransferState.SIGNED();
            case CRYPTO_TRANSFER_STATE_SUBMITTED: return TransferState.SUBMITTED();
            case CRYPTO_TRANSFER_STATE_ERRORED:
                switch (state.u.errored.error.type()) {
                    case TRANSFER_SUBMIT_ERROR_UNKNOWN: return TransferState.FAILED(
                            new TransferSubmitUnknownError()
                    );
                    case TRANSFER_SUBMIT_ERROR_POSIX: return TransferState.FAILED(
                            new TransferSubmitPosixError(
                                    state.u.errored.error.u.posix.errnum,
                                    state.u.errored.error.getMessage().orNull()
                            )
                    );
                    default: throw new IllegalArgumentException("Unsupported error");
                }
            case CRYPTO_TRANSFER_STATE_INCLUDED: return TransferState.INCLUDED(
                    new TransferConfirmation(
                            UnsignedLong.fromLongBits(state.u.included.blockNumber),
                            UnsignedLong.fromLongBits(state.u.included.transactionIndex),
                            UnsignedLong.fromLongBits(state.u.included.timestamp),
                            Optional.fromNullable(state.u.included.fee)
                                    .transform(Amount::create)
                    )
            );
            default: throw new IllegalArgumentException("Unsupported state");
        }
    }

    /* package */
    static BRCryptoAddressScheme addressSchemeToCrypto(AddressScheme scheme) {
        switch (scheme) {
            case BTC_LEGACY: return BRCryptoAddressScheme.CRYPTO_ADDRESS_SCHEME_BTC_LEGACY;
            case BTC_SEGWIT: return BRCryptoAddressScheme.CRYPTO_ADDRESS_SCHEME_BTC_SEGWIT;
            case ETH_DEFAULT: return BRCryptoAddressScheme.CRYPTO_ADDRESS_SCHEME_ETH_DEFAULT;
            case GEN_DEFAULT: return BRCryptoAddressScheme.CRYPTO_ADDRESS_SCHEME_GEN_DEFAULT;
            default: throw new IllegalArgumentException("Unsupported scheme");
        }
    }

    /* package */
    static AddressScheme addressSchemeFromCrypto(BRCryptoAddressScheme scheme) {
        switch (scheme) {
            case CRYPTO_ADDRESS_SCHEME_BTC_LEGACY: return AddressScheme.BTC_LEGACY;
            case CRYPTO_ADDRESS_SCHEME_BTC_SEGWIT: return AddressScheme.BTC_SEGWIT;
            case CRYPTO_ADDRESS_SCHEME_ETH_DEFAULT: return AddressScheme.ETH_DEFAULT;
            case CRYPTO_ADDRESS_SCHEME_GEN_DEFAULT: return AddressScheme.GEN_DEFAULT;
            default: throw new IllegalArgumentException("Unsupported scheme");
        }
    }

    /* package */
    static FeeEstimationError feeEstimationErrorFromStatus(BRCryptoStatus status) {
        if (status == BRCryptoStatus.CRYPTO_ERROR_NODE_NOT_CONNECTED) {
            return new FeeEstimationServiceUnavailableError();
        }
        return new FeeEstimationServiceFailureError();
    }

    /* package */
    static BRSyncDepth syncDepthToCrypto(WalletManagerSyncDepth depth) {
        switch (depth) {
            case FROM_LAST_CONFIRMED_SEND: return BRSyncDepth.SYNC_DEPTH_FROM_LAST_CONFIRMED_SEND;
            case FROM_LAST_TRUSTED_BLOCK:  return BRSyncDepth.SYNC_DEPTH_FROM_LAST_TRUSTED_BLOCK;
            case FROM_CREATION:            return BRSyncDepth.SYNC_DEPTH_FROM_CREATION;
            default: throw new IllegalArgumentException("Unsupported depth");
        }
    }

    static WalletManagerSyncDepth syncDepthFromCrypto(BRSyncDepth depth) {
        switch (depth) {
            case SYNC_DEPTH_FROM_LAST_CONFIRMED_SEND: return WalletManagerSyncDepth.FROM_LAST_CONFIRMED_SEND;
            case SYNC_DEPTH_FROM_LAST_TRUSTED_BLOCK:  return WalletManagerSyncDepth.FROM_LAST_TRUSTED_BLOCK;
            case SYNC_DEPTH_FROM_CREATION:            return WalletManagerSyncDepth.FROM_CREATION;
            default: throw new IllegalArgumentException("Unsupported depth");
        }
    }

    /* package */
    static UnsignedLong dateAsUnixTimestamp(Date date) {
        long timestamp = TimeUnit.MILLISECONDS.toSeconds(date.getTime());
        return timestamp > 0 ? UnsignedLong.valueOf(timestamp) : UnsignedLong.ZERO;
    }
}
