package com.vodafoneziggotechcasescreening.com.pin_system_northbound_api.southbound.activationTerminal.models.activationTerminalResponse;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ActivationTerminalResponse implements ActivationTerminalResponseInterface {
    @NotBlank(message = "Status is mandatory")
    @Size(min = 3, max = 3)
    private int status;

    public ActivationTerminalResponse(int status) {
        this.status = status;
    }

    @Override
    public int getStatus() {
        return this.status;
    }

    @Override
    public boolean statusIsValid() {
        return this.status > 99 && this.status < 1000;
    }
}
