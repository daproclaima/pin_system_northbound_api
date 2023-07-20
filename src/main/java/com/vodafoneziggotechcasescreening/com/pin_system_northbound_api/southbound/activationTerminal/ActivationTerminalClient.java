package com.vodafoneziggotechcasescreening.com.pin_system_northbound_api.southbound.activationTerminal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vodafoneziggotechcasescreening.com.pin_system_northbound_api.southbound.activationTerminal.models.activationTerminalRequest.ActivationTerminalRequest;
import com.vodafoneziggotechcasescreening.com.pin_system_northbound_api.southbound.activationTerminal.models.activationTerminalResponse.ActivationTerminalResponse;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;


@Component
public class ActivationTerminalClient {

    private final WebClient activationTerminalClient;

    @Autowired
    public ActivationTerminalClient(WebClient activationTerminalClient) {
        this.activationTerminalClient = activationTerminalClient;
    }

    @Async
    public CompletableFuture<ActivationTerminalResponse> activateTerminal(ActivationTerminalRequest activationTerminalRequest) throws JsonProcessingException {

        ObjectNode result =
                activationTerminalClient
                        .post()
                        .uri("/activate")
                        .body(Mono.just(activationTerminalRequest), ActivationTerminalRequest.class)
                        .retrieve()
                        .bodyToMono(ObjectNode.class)
                        .retryWhen(Retry.fixedDelay(2, Duration.ofMillis(200)))
                        .block();

        assert result != null;
        String data = result.toString();

        ActivationTerminalResponse activationTerminalResponse = new ObjectMapper().readValue(data, ActivationTerminalResponse.class);

        return CompletableFuture.completedFuture(activationTerminalResponse);
    }


    // Spring Boot autoconfigures a `WebClient.Builder` instance with nice defaults and customizations.
    // We can use it to create a dedicated `WebClient` for our component.
//    public activationTerminalClient(WebClient.Builder builder, WebClient activationTerminalClient) {
//        this.activationTerminalClient = activationTerminalClient;
//        this.client = builder.baseUrl(this.BASE_URL)
//                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
//                .build();
//    }

//    public Mono<ActivationResponse> postactivationTerminalRequest(activationTerminalRequest activationTerminalRequest) {
//        return this.client.post()
//                .uri("/activate")
//                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
//                .body(Mono.just(activationTerminalRequest), activationTerminalRequest.class)
//                .retrieve()
//                .bodyToMono(ActivationResponse.class);
//    }

}



