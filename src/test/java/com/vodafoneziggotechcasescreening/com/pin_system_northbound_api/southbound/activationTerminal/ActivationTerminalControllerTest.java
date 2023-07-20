package com.vodafoneziggotechcasescreening.com.pin_system_northbound_api.southbound.activationTerminal;

import com.vodafoneziggotechcasescreening.com.pin_system_northbound_api.southbound.activationTerminal.models.activationTerminalRequest.ActivationTerminalRequest;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.scheduling.annotation.Async;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.google.gson.Gson;

@SpringBootTest
@AutoConfigureMockMvc
public class ActivationTerminalControllerTest {
    @MockBean
    private ActivationTerminalService activationTerminalService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Async
    void shouldReturnActiveStatusWhenGivenRightCustomerIdAndRightMacAddress() throws Exception {

        ActivationTerminalRequest stub = new ActivationTerminalRequest("12345", "AA:BB:CC:DD:EE:FF");

        Gson gson = new Gson();
        String json = gson.toJson(stub);

        MockHttpServletResponse mvcResult = this.mockMvc.perform(post("/activate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andReturn()
                .getResponse();

        assertThat(mvcResult.getStatus()).isEqualTo(201);
        assertThat(mvcResult.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(mvcResult.getContentAsString()).isEqualTo("{'status': 'ACTIVE'}");
    }

    @Test
    void shouldreturnUnactiveStatusWhenGivenRightCustomerIdAndWrongMacAddress() throws Exception {

        ActivationTerminalRequest stub = new ActivationTerminalRequest("12345", "AA:BB:CC:DD:EE:AA");

        Gson gson = new Gson();
        String json = gson.toJson(stub);

        MockHttpServletResponse mvcResult = this.mockMvc.perform(post("/activate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn()
                .getResponse();

        assertThat(mvcResult.getStatus()).isEqualTo(404);
        assertThat(mvcResult.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(mvcResult.getContentAsString()).isEqualTo("{'status': 'UNACTIVE'}");
    }

    @Test
    void shouldreturnUnactiveStatusWhenGivenWrongCustomerIdAndRightMacAddress() throws Exception {

        ActivationTerminalRequest stub = new ActivationTerminalRequest("11111", "AA:BB:CC:DD:EE:FF");

        Gson gson = new Gson();
        String json = gson.toJson(stub);

        MockHttpServletResponse mvcResult = this.mockMvc.perform(post("/activate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn()
                .getResponse();

        assertThat(mvcResult.getStatus()).isEqualTo(409);
        assertThat(mvcResult.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(mvcResult.getContentAsString()).isEqualTo("{'status': 'UNACTIVE'}");
    }
}


