package com.rarible.protocol.currency.api.client.autoconfigure

import com.rarible.core.application.ApplicationEnvironmentInfo
import com.rarible.protocol.currency.api.client.CompositeWebClientCustomizer
import com.rarible.protocol.currency.api.client.CurrencyApiClientFactory
import com.rarible.protocol.currency.api.client.CurrencyApiServiceUriProvider
import com.rarible.protocol.currency.api.client.DefaultCurrencyWebClientCustomizer
import com.rarible.protocol.currency.api.client.K8sCurrencyApiServiceUriProvider
import com.rarible.protocol.currency.api.client.NoopWebClientCustomizer
import com.rarible.protocol.currency.api.client.SwarmCurrencyApiServiceUriProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer
import org.springframework.context.annotation.Bean

const val CURRENCY_WEB_CLIENT_CUSTOMIZER = "CURRENCY_WEB_CLIENT_CUSTOMIZER"

class CurrencyApiClientAutoConfiguration(
    private val applicationEnvironmentInfo: ApplicationEnvironmentInfo
) {

    @Autowired(required = false)
    @Qualifier(CURRENCY_WEB_CLIENT_CUSTOMIZER)
    private var webClientCustomizer: WebClientCustomizer = NoopWebClientCustomizer()

    @Bean
    @ConditionalOnMissingBean(CurrencyApiServiceUriProvider::class)
    fun currencyApiServiceUriProvider(
        @Value("\${rarible.core.client.k8s:false}") k8s: Boolean
    ): CurrencyApiServiceUriProvider {
        return if (k8s)
            K8sCurrencyApiServiceUriProvider(applicationEnvironmentInfo.name)
        else
            SwarmCurrencyApiServiceUriProvider(applicationEnvironmentInfo.name)
    }

    @Bean
    @ConditionalOnMissingBean(CurrencyApiClientFactory::class)
    fun currencyApiClientFactory(currencyApiServiceUriProvider: CurrencyApiServiceUriProvider): CurrencyApiClientFactory {
        val compositeWebClientCustomizer = CompositeWebClientCustomizer(listOf(DefaultCurrencyWebClientCustomizer(), webClientCustomizer))
        return CurrencyApiClientFactory(currencyApiServiceUriProvider, compositeWebClientCustomizer)
    }
}
