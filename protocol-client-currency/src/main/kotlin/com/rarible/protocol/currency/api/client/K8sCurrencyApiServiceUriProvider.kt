package com.rarible.protocol.currency.api.client

import java.net.URI

class K8sCurrencyApiServiceUriProvider(
    private val env: String
) : CurrencyApiServiceUriProvider {

    override fun getUri(): URI {
        return URI.create("http://protocol-currency-api.${env}-protocol:8080")
    }
}
