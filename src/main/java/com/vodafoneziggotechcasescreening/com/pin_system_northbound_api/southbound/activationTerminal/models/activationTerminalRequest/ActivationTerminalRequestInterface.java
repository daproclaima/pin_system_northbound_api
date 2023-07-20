package com.vodafoneziggotechcasescreening.com.pin_system_northbound_api.southbound.activationTerminal.models.activationTerminalRequest;

public interface ActivationTerminalRequestInterface {
    String getCustomerId();

    String getMacAddress();

    boolean customerIdIsValid();
    boolean macAddressIsValid();
}
