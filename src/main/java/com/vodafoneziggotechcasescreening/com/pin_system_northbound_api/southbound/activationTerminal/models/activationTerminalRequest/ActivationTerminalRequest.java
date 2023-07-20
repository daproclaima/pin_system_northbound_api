package com.vodafoneziggotechcasescreening.com.pin_system_northbound_api.southbound.activationTerminal.models.activationTerminalRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ActivationTerminalRequest implements ActivationTerminalRequestInterface {
    @NotBlank(message = "CustomerId is mandatory")
    @Size(min = 2, max = 30)
    private String customerId;

    @NotBlank(message = "MacAddress is mandatory")
    @Size(min = 2, max = 30)
    private String macAddress;

    public ActivationTerminalRequest(String customerId, String macAddress) {
        this.customerId = customerId;
        this.macAddress = macAddress;
    }

    public String getCustomerId() {
        return customerId;
    }


    public String getMacAddress() {
        return macAddress;
    }

    @Override
    public boolean customerIdIsValid() {
        return customerId != null;
    }

    @Override
    public boolean macAddressIsValid() {
        return macAddress != null;
    }
}
