package ru.alov.market.auth.tests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void createAuthTokenTest() throws Exception {
        String validJsonRequest = "{\n" +
                "\t\"username\": \"bob\",\n" +
                "\t\"password\": \"100\"\n" +
                "}";
        mvc.perform(
                post("/authenticate")
                        .content(validJsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());

        String invalidJsonRequest = "{\n" +
                "\t\"username\": \"bobrtf\",\n" +
                "\t\"password\": \"1005485\"\n" +
                "}";

        mvc.perform(
                post("/authenticate")
                        .content(invalidJsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isUnauthorized());

    }
}