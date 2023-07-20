package com.vodafoneziggotechcasescreening.com.pin_system_northbound_api.southbound.activationTerminal;

import com.vodafoneziggotechcasescreening.com.pin_system_northbound_api.southbound.activationTerminal.models.activationTerminalRequest.ActivationTerminalRequest;
import com.vodafoneziggotechcasescreening.com.pin_system_northbound_api.southbound.activationTerminal.models.responseToOrchestrator.ResponseToOrchestrator;

import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/activate")
public class ActivationTerminalController {
    private final ActivationTerminalService activationTerminalService;

    public ActivationTerminalController(ActivationTerminalService activationTerminalService) {
        this.activationTerminalService = activationTerminalService;
    }

    @PostMapping
    @Async
    CompletableFuture<ResponseToOrchestrator> activateTerminal(@RequestBody ActivationTerminalRequest activationTerminalRequest) throws Exception {
        CompletableFuture<ResponseToOrchestrator> result = this.activationTerminalService.activateTerminal(activationTerminalRequest);

        ResponseToOrchestrator response = result.get();

        return CompletableFuture.completedFuture(response);
    }
}
