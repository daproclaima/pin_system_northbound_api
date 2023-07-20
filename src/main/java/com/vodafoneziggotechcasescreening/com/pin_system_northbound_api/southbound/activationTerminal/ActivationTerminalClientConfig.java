package com.vodafoneziggotechcasescreening.com.pin_system_northbound_api.southbound.activationTerminal;

import com.vodafoneziggotechcasescreening.com.pin_system_northbound_api.Application;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Configuration
public class ActivationTerminalClientConfig {
    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    @Bean
    public WebClient activationWebClient() {
        String activationBaseUrl = "https://southbound.terminal.com";

        HttpClient httpClient =
                HttpClient.create()
                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 2_000)
                        .doOnConnected(
                                connection ->
                                        connection
                                                .addHandlerLast(new ReadTimeoutHandler(2))
                                                .addHandlerLast(new WriteTimeoutHandler(2)));

        return WebClient
                .builder()
                .baseUrl(activationBaseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .filters(exchangeFilterFunctions -> {
                    exchangeFilterFunctions.add(logRequest());
                    exchangeFilterFunctions.add(logResponse());
                })
                .build();
    }

    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            if (LOG.isDebugEnabled()) {
                StringBuilder sb = new StringBuilder("Request: \n");

                //append clientRequest method and url
                clientRequest
                        .headers();
//                        .forEach((name, values) -> values.forEach(value -> value));
                LOG.debug(sb.toString());
            }
            return Mono.just(clientRequest);
        });
    }

    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            if (LOG.isDebugEnabled()) {
                StringBuilder sb = new StringBuilder("Request: \n");

                clientResponse
                        .headers();
//                        .forEach((name, values) -> values.forEach(value -> value));
                LOG.debug(sb.toString());
            }
            return Mono.just(clientResponse);
        });
    }
}