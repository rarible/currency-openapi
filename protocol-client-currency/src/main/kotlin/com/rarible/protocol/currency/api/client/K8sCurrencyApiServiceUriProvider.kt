package com.rarible.protocol.currency.api.client

import java.net.URI

class K8sCurrencyApiServiceUriProvider(
    private val namespace: String?
) : CurrencyApiServiceUriProvider {

    companion object {

        const val INTERNAL_URL = "http://protocol-currency-api"
        const val PORT = 8080
    }

    override fun getUri(): URI {
        val url = namespace?.let {
            "$INTERNAL_URL.$namespace:$PORT"
        } ?: "$INTERNAL_URL:$PORT"

        return URI.create(url)
    }
}
