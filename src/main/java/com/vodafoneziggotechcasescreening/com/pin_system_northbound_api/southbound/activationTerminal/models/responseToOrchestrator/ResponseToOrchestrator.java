package com.vodafoneziggotechcasescreening.com.pin_system_northbound_api.southbound.activationTerminal.models.responseToOrchestrator;

import com.vodafoneziggotechcasescreening.com.pin_system_northbound_api.southbound.activationTerminal.models.activationTerminalResponse.ActivationTerminalResponse;
import jakarta.validation.constraints.Pattern;

public class ResponseToOrchestrator implements ResponseToOrchestratorInterface {
    private final String INACTIVE_STATUS = "INACTIVE";
    private final String ACTIVE_STATUS = "ACTIVE";

    @Pattern(regexp = "^(ACTIVE|INACTIVE)")
    private String status;
    private int code;

    public ResponseToOrchestrator(ActivationTerminalResponse activationTerminalResponse) throws Exception {
        if(activationTerminalResponse.getStatus() == 404 || activationTerminalResponse.getStatus() == 409) this.status = this.INACTIVE_STATUS;

        if(activationTerminalResponse.getStatus() == 201) this.status = this.ACTIVE_STATUS;

        boolean isStatusValid = this.statusIsValid();
        if(!isStatusValid) {
            throw new Exception("ActivationResponseToOrchestrator status is not valid.");
        }
    }

    public String getStatus() {
        return status;
    }

    @Override
    public boolean statusIsValid() {
        return this.status == this.INACTIVE_STATUS || this.status == this.ACTIVE_STATUS;
    }

}
