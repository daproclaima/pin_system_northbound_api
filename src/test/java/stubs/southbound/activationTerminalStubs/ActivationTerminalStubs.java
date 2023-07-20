package stubs.southbound.activationTerminalStubs;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.vodafoneziggotechcasescreening.com.pin_system_northbound_api.southbound.activationTerminal.models.activationTerminalRequest.ActivationTerminalRequest;
import org.springframework.http.MediaType;

import java.util.Objects;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;

public class ActivationTerminalStubs {

    private final WireMockServer wireMockServer;

    public ActivationTerminalStubs(WireMockServer wireMockServer) {
        this.wireMockServer = wireMockServer;
    }

    public void stubForSuccessfulBookResponse(ActivationTerminalRequest activationTerminalRequest) {

        if (Objects.equals(activationTerminalRequest.getCustomerId(), "12345")) {
            if (Objects.equals(activationTerminalRequest.getMacAddress(), "AA:BB:CC:DD:EE:FF")) {
                this.wireMockServer.stubFor(
                        WireMock.get("https://southbound.terminal.com")
                                .willReturn(
                                        aResponse()
                                                .withHeader("Content-Type", String.valueOf(MediaType.APPLICATION_JSON))
                                                .withStatus(201)
                                ));
            }

            if (Objects.equals(activationTerminalRequest.getMacAddress(), "AA:BB:CC:DD:EE:AA")) {
                this.wireMockServer.stubFor(
                        WireMock.get("https://southbound.terminal.com")
                                .willReturn(
                                        aResponse()
                                                .withHeader("Content-Type", String.valueOf(MediaType.APPLICATION_JSON))
                                                .withStatus(404)
                                ));
            }

        }

        if (Objects.equals(activationTerminalRequest.getCustomerId(), "11111") && Objects.equals(activationTerminalRequest.getMacAddress(), "AA:BB:CC:DD:EE:FF")) {
            this.wireMockServer.stubFor(
                    WireMock.get("https://southbound.terminal.com")
                            .willReturn(
                                    aResponse()
                                            .withHeader("Content-Type", String.valueOf(MediaType.APPLICATION_JSON))
                                            .withStatus(409)
                            ));
        }
    }
}