/*
 * Created by Michael Carrara <michael.carrara@breadwallet.com> on 7/1/19.
 * Copyright (c) 2019 Breadwinner AG.  All right reserved.
*
 * See the LICENSE file at the project root for license information.
 * See the CONTRIBUTORS file at the project root for a list of contributors.
 */
package com.breadwallet.crypto.blockchaindb.apis;

import com.google.common.base.Optional;

import org.json.JSONObject;

public interface ObjectResponseParser<T> {
    Optional<T> parse(JSONObject json);
}
