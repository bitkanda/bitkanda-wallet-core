package com.breadwallet.corecrypto;

import com.breadwallet.corenative.crypto.BRCryptoCWMClient;
import com.breadwallet.corenative.crypto.BRCryptoCWMListener;
import com.breadwallet.corenative.crypto.BRCryptoKey;
import com.breadwallet.corenative.crypto.CoreBRCryptoWallet;
import com.breadwallet.corenative.crypto.CoreBRCryptoWalletManager;
import com.breadwallet.crypto.AddressScheme;
import com.breadwallet.crypto.WalletManagerMode;
import com.breadwallet.crypto.WalletManagerState;
import com.breadwallet.crypto.blockchaindb.errors.QueryError;
import com.breadwallet.crypto.blockchaindb.models.bdb.Transaction;
import com.breadwallet.crypto.errors.WalletSweeperError;
import com.breadwallet.crypto.errors.WalletSweeperQueryError;
import com.breadwallet.crypto.utility.CompletionHandler;
import com.google.common.base.Optional;
import com.google.common.primitives.UnsignedLong;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkState;

/* package */
final class WalletManager implements com.breadwallet.crypto.WalletManager {

    /* package */
    static WalletManager create(BRCryptoCWMListener.ByValue listener,
                                BRCryptoCWMClient.ByValue client,
                                Account account,
                                Network network,
                                WalletManagerMode mode,
                                AddressScheme addressScheme,
                                String path,
                                System system,
                                SystemCallbackCoordinator callbackCoordinator) {
        CoreBRCryptoWalletManager core = CoreBRCryptoWalletManager.create(
                listener,
                client,
                account.getCoreBRCryptoAccount(),
                network.getCoreBRCryptoNetwork(),
                Utilities.walletManagerModeToCrypto(mode),
                Utilities.addressSchemeToCrypto(addressScheme),
                path);

        return new WalletManager(core, system, callbackCoordinator);
    }

    /* package */
    static WalletManager create(CoreBRCryptoWalletManager core, System system, SystemCallbackCoordinator callbackCoordinator) {
        return new WalletManager(core, system, callbackCoordinator);
    }

    private CoreBRCryptoWalletManager core;
    private final System system;
    private final SystemCallbackCoordinator callbackCoordinator;

    private final Account account;
    private final Network network;
    private final Currency networkCurrency;
    private final Unit networkBaseUnit;
    private final Unit networkDefaultUnit;
    private final String path;
    private final NetworkFee networkFee;

    private WalletManager(CoreBRCryptoWalletManager core, System system, SystemCallbackCoordinator callbackCoordinator) {
        this.core = core;
        this.system = system;
        this.callbackCoordinator = callbackCoordinator;

        this.account = Account.create(core.getAccount());
        this.network = Network.create(core.getNetwork());
        this.networkCurrency = network.getCurrency();
        this.path = core.getPath();

        // TODO(fix): Unchecked get here
        this.networkBaseUnit = network.baseUnitFor(networkCurrency).get();
        this.networkDefaultUnit = network.defaultUnitFor(networkCurrency).get();
        this.networkFee = network.getMinimumFee();
    }

    @Override
    public void createSweeper(com.breadwallet.crypto.Wallet wallet,
                              com.breadwallet.crypto.Key key,
                              CompletionHandler<com.breadwallet.crypto.WalletSweeper, WalletSweeperError> completion) {
        WalletSweeper.create(this, Wallet.from(wallet), Key.from(key), system.getBlockchainDb(), completion);
    }

    @Override
    public void connect() {
        core.connect();
    }

    @Override
    public void disconnect() {
        core.disconnect();
    }

    @Override
    public void sync() {
        core.sync();
    }

    /* package */
    boolean sign(com.breadwallet.crypto.Transfer transfer, byte[] phraseUtf8) {
        Transfer cryptoTransfer = Transfer.from(transfer);
        Wallet cryptoWallet = cryptoTransfer.getWallet();
        return core.sign(cryptoWallet.getCoreBRCryptoWallet(), cryptoTransfer.getCoreBRCryptoTransfer(), phraseUtf8);
    }

    @Override
    public void submit(com.breadwallet.crypto.Transfer transfer, byte[] phraseUtf8) {
        Transfer cryptoTransfer = Transfer.from(transfer);
        Wallet cryptoWallet = cryptoTransfer.getWallet();
        core.submit(cryptoWallet.getCoreBRCryptoWallet(), cryptoTransfer.getCoreBRCryptoTransfer(), phraseUtf8);
    }

    /* package */
    void submit(com.breadwallet.crypto.Transfer transfer, BRCryptoKey key) {
        Transfer cryptoTransfer = Transfer.from(transfer);
        Wallet cryptoWallet = cryptoTransfer.getWallet();
        core.submit(cryptoWallet.getCoreBRCryptoWallet(), cryptoTransfer.getCoreBRCryptoTransfer(), key);
    }

    /* package */
    void submit(com.breadwallet.crypto.Transfer transfer) {
        Transfer cryptoTransfer = Transfer.from(transfer);
        Wallet cryptoWallet = cryptoTransfer.getWallet();
        core.submit(cryptoWallet.getCoreBRCryptoWallet(), cryptoTransfer.getCoreBRCryptoTransfer());
    }

    @Override
    public boolean isActive() {
        WalletManagerState state = getState();
        return state == WalletManagerState.CREATED || state == WalletManagerState.SYNCING;
    }

    @Override
    public System getSystem() {
        return system;
    }

    @Override
    public Account getAccount() {
        return account;
    }

    @Override
    public Network getNetwork() {
        return network;
    }

    @Override
    public Wallet getPrimaryWallet() {
        return Wallet.create(core.getWallet(), this, callbackCoordinator);
    }

    @Override
    public List<Wallet> getWallets() {
        List<Wallet> wallets = new ArrayList<>();

        for (CoreBRCryptoWallet wallet: core.getWallets()) {
            wallets.add(Wallet.create(wallet, this, callbackCoordinator));
        }

        return wallets;
    }

    @Override
    public WalletManagerMode getMode() {
        return Utilities.walletManagerModeFromCrypto(core.getMode());
    }

    @Override
    public void setMode(WalletManagerMode mode) {
        core.setMode(Utilities.walletManagerModeToCrypto(mode));
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public Currency getCurrency() {
        return networkCurrency;
    }

    @Override
    public String getName() {
        return networkCurrency.getCode();
    }

    @Override
    public Unit getBaseUnit() {
        return networkBaseUnit;
    }

    @Override
    public Unit getDefaultUnit() {
        return networkDefaultUnit;
    }

    @Override
    public NetworkFee getDefaultNetworkFee() {
        return networkFee;
    }

    @Override
    public WalletManagerState getState() {
        return Utilities.walletManagerStateFromCrypto(core.getState());
    }

    @Override
    public void setAddressScheme(AddressScheme scheme) {
        checkState(system.supportsAddressScheme(network, scheme));
        core.setAddressScheme(Utilities.addressSchemeToCrypto(scheme));
    }

    @Override
    public AddressScheme getAddressScheme() {
        return Utilities.addressSchemeFromCrypto(core.getAddressScheme());
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof WalletManager)) {
            return false;
        }

        WalletManager that = (WalletManager) object;
        return core.equals(that.core);
    }

    @Override
    public int hashCode() {
        return Objects.hash(core);
    }

    @Override
    public String toString() {
        return getName();
    }

    /* package */
    Optional<Wallet> getWallet(CoreBRCryptoWallet wallet) {
        return core.containsWallet(wallet) ?
                Optional.of(Wallet.create(wallet, this, callbackCoordinator)):
                Optional.absent();
    }

    /* package */
    Optional<Wallet> getWalletOrCreate(CoreBRCryptoWallet wallet) {
        Optional<Wallet> optional = getWallet(wallet);
        if (optional.isPresent()) {
            return optional;

        } else {
            return Optional.of(Wallet.create(wallet, this, callbackCoordinator));
        }
    }

    /* package */
    CoreBRCryptoWalletManager getCoreBRCryptoWalletManager() {
        return core;
    }
}
