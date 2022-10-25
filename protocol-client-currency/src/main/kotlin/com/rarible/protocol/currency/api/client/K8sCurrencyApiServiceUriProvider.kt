package com.rarible.protocol.currency.api.client

import java.net.URI

class K8sCurrencyApiServiceUriProvider() : CurrencyApiServiceUriProvider {

    override fun getUri(): URI {
        return URI.create("http://protocol-currency-api:8080")
    }
}
