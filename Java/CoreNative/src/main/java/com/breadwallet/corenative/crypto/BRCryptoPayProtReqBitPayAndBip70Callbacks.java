/*
 * Created by Michael Carrara <michael.carrara@breadwallet.com> on 5/31/18.
 * Copyright (c) 2018 Breadwinner AG.  All right reserved.
 *
 * See the LICENSE file at the project root for license information.
 * See the CONTRIBUTORS file at the project root for a list of contributors.
 */
package com.breadwallet.corenative.crypto;

import com.breadwallet.corenative.utility.SizeT;
import com.sun.jna.Callback;
import com.sun.jna.Pointer;
import com.sun.jna.StringArray;
import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class BRCryptoPayProtReqBitPayAndBip70Callbacks extends Structure {

    public interface BRCryptoPayProtReqBitPayAndBip70Validator extends Callback {
        int apply(BRCryptoPaymentProtocolRequest req,
                  Pointer context,
                  String pkiType,
                  long expires,
                  Pointer certBytes,
                  Pointer certLengths,
                  SizeT certCount,
                  Pointer digest,
                  SizeT digestLen,
                  Pointer sig,
                  SizeT sigLen);
    }

    public interface BRCryptoPayProtReqBitPayAndBip70CommonNameExtractor extends Callback {
        Pointer apply(BRCryptoPaymentProtocolRequest req,
                      Pointer context,
                      String pkiType,
                      Pointer certBytes,
                      Pointer certLengths,
                      SizeT certCount);
    }

    public Pointer context;
    public BRCryptoPayProtReqBitPayAndBip70Validator validator;
    public BRCryptoPayProtReqBitPayAndBip70CommonNameExtractor nameExtractor;
    public BRCryptoPayProtReqBitPayAndBip70Callbacks() {
        super();
    }

    protected List<String> getFieldOrder() {
        return Arrays.asList("context", "validator", "nameExtractor");
    }

    public BRCryptoPayProtReqBitPayAndBip70Callbacks(Pointer context,
                                                     BRCryptoPayProtReqBitPayAndBip70Validator validator,
                                                     BRCryptoPayProtReqBitPayAndBip70CommonNameExtractor nameExtractor) {
        super();
        this.context = context;
        this.validator = validator;
        this.nameExtractor = nameExtractor;
    }

    public BRCryptoPayProtReqBitPayAndBip70Callbacks(Pointer peer) {
        super(peer);
    }

    public static class ByReference extends BRCryptoPayProtReqBitPayAndBip70Callbacks implements Structure.ByReference {

    }

    public static class ByValue extends BRCryptoPayProtReqBitPayAndBip70Callbacks implements Structure.ByValue {
        public ByValue(Pointer context,
                       BRCryptoPayProtReqBitPayAndBip70Validator validator,
                       BRCryptoPayProtReqBitPayAndBip70CommonNameExtractor nameExtractor) {
            super(context, validator, nameExtractor);
        }
    }
}
