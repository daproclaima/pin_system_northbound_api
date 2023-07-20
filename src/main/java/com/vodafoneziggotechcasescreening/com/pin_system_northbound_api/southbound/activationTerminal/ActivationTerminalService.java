package com.vodafoneziggotechcasescreening.com.pin_system_northbound_api.southbound.activationTerminal;

import com.vodafoneziggotechcasescreening.com.pin_system_northbound_api.Application;
import com.vodafoneziggotechcasescreening.com.pin_system_northbound_api.southbound.activationTerminal.models.activationTerminalRequest.ActivationTerminalRequest;
import com.vodafoneziggotechcasescreening.com.pin_system_northbound_api.southbound.activationTerminal.models.activationTerminalResponse.ActivationTerminalResponse;
import com.vodafoneziggotechcasescreening.com.pin_system_northbound_api.southbound.activationTerminal.models.responseToOrchestrator.ResponseToOrchestrator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class ActivationTerminalService {
    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    private final ActivationTerminalClient activationTerminalClient;

    @Autowired
    public ActivationTerminalService(ActivationTerminalClient activationTerminalClient) {
        this.activationTerminalClient = activationTerminalClient;
    }

    @Async
    public CompletableFuture<ResponseToOrchestrator> activateTerminal(ActivationTerminalRequest activationTerminalRequest) throws Exception {
        CompletableFuture<ActivationTerminalResponse> result = this.activationTerminalClient.activateTerminal(activationTerminalRequest);

        ActivationTerminalResponse activationTerminalResponse = result.get();

        LOG.info("activationTerminalResponse activationTerminalResponse: ");
        LOG.info(activationTerminalResponse.toString());



        return CompletableFuture.completedFuture(new ResponseToOrchestrator(activationTerminalResponse));
    }
}
