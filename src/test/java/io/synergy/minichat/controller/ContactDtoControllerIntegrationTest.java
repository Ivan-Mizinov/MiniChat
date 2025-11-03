package io.synergy.minichat.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
@DisplayName("Контроллер для контактов")
class ContactDtoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String CONTACT_JSON = """
            {
                "lastName": "Петров",
                "firstName": "Петр",
                "middleName": "Петрович",
                "phone": "+79991234567"
            }
            """;

    @Test
    @DisplayName("Возвращает пустой список при старте")
    void getContacts_emptyAtStart() throws Exception {

        mockMvc.perform(get("/api/service/Contacts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("Создаёт контакт и возвращает его с ID")
    void createContact_success() throws Exception {

        mockMvc.perform(post("/api/service/Contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CONTACT_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.lastName").value("Петров"))
                .andExpect(jsonPath("$.firstName").value("Петр"))
                .andExpect(jsonPath("$.middleName").value("Петрович"))
                .andExpect(jsonPath("$.phone").value("+79991234567"));
    }

    @Test
    @DisplayName("Получает контакт по ID")
    void getContactById_success() throws Exception {

        mockMvc.perform(post("/api/service/Contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CONTACT_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/service/Contact/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.lastName").value("Петров"))
                .andExpect(jsonPath("$.firstName").value("Петр"))
                .andExpect(jsonPath("$.middleName").value("Петрович"))
                .andExpect(jsonPath("$.phone").value("+79991234567"));
    }

    @Test
    @DisplayName("Обновляет контакт")
    void updateContact_success() throws Exception {

        MvcResult createResult = mockMvc.perform(post("/api/service/Contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CONTACT_JSON))
                .andExpect(status().isOk())
                .andReturn();


        String responseBody = createResult.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode createdContact = objectMapper.readTree(responseBody);
        int createdId = createdContact.get("id").asInt();

        String updatedContactJson = """
                {
                    "id": %d,
                    "lastName": "Сидоров",
                    "firstName": "Сидор",
                    "middleName": "Сидорович",
                    "phone": "+79991112233"
                }
                """.formatted(createdId);

        mockMvc.perform(put("/api/service/Contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedContactJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(createdId))
                .andExpect(jsonPath("$.lastName").value("Сидоров"))
                .andExpect(jsonPath("$.firstName").value("Сидор"))
                .andExpect(jsonPath("$.middleName").value("Сидорович"))
                .andExpect(jsonPath("$.phone").value("+79991112233"));
    }

    @Test
    @DisplayName("Удаляет контакт")
    void deleteContact_success() throws Exception {

        MvcResult createResult = mockMvc.perform(post("/api/service/Contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CONTACT_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = createResult.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode createdContact = objectMapper.readTree(responseBody);
        int createdId = createdContact.get("id").asInt();

        mockMvc.perform(delete("/api/service/Contact/" + createdId))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/service/Contact/" + createdId))
                .andExpect(status().isNotFound());
    }
}