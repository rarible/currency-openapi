package com.rarible.protocol.currency.api.client.autoconfigure

import com.rarible.protocol.currency.api.client.CompositeWebClientCustomizer
import com.rarible.protocol.currency.api.client.CurrencyApiClientFactory
import com.rarible.protocol.currency.api.client.CurrencyApiServiceUriProvider
import com.rarible.protocol.currency.api.client.DefaultCurrencyWebClientCustomizer
import com.rarible.protocol.currency.api.client.K8sCurrencyApiServiceUriProvider
import com.rarible.protocol.currency.api.client.NoopWebClientCustomizer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer
import org.springframework.context.annotation.Bean

const val CURRENCY_WEB_CLIENT_CUSTOMIZER = "CURRENCY_WEB_CLIENT_CUSTOMIZER"

class CurrencyApiClientAutoConfiguration {

    @Autowired(required = false)
    @Qualifier(CURRENCY_WEB_CLIENT_CUSTOMIZER)
    private var webClientCustomizer: WebClientCustomizer = NoopWebClientCustomizer()

    @Bean
    @ConditionalOnMissingBean(CurrencyApiServiceUriProvider::class)
    fun currencyApiServiceUriProvider(): CurrencyApiServiceUriProvider {
        return K8sCurrencyApiServiceUriProvider()
    }

    @Bean
    @ConditionalOnMissingBean(CurrencyApiClientFactory::class)
    fun currencyApiClientFactory(
        @Value("\${rarible.core.client.name:}") clientName: String,
        currencyApiServiceUriProvider: CurrencyApiServiceUriProvider
    ): CurrencyApiClientFactory {
        val customizers = listOf(DefaultCurrencyWebClientCustomizer(clientName), webClientCustomizer)
        val compositeWebClientCustomizer = CompositeWebClientCustomizer(customizers)
        return CurrencyApiClientFactory(currencyApiServiceUriProvider, compositeWebClientCustomizer)
    }
}
