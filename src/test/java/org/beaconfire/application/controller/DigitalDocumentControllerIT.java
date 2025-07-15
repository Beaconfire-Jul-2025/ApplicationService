package org.beaconfire.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.beaconfire.application.dto.DigitalDocumentDto;
import org.beaconfire.application.support.MySqlTestContainer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Testcontainers
class DigitalDocumentControllerIT extends MySqlTestContainer {
    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper objectMapper;

    @DynamicPropertySource
    static void mysqlProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MYSQL::getJdbcUrl);
        registry.add("spring.datasource.username", MYSQL::getUsername);
        registry.add("spring.datasource.password", MYSQL::getPassword);
    }

    @Test
    @DisplayName("GET /documents – returns empty page when no data")
    void getAll_empty() throws Exception {
        mvc.perform(get("/documents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", empty()));
    }

    @Test
    @DisplayName("POST /documents – creates document")
    void create_document() throws Exception {
        DigitalDocumentDto dto = DigitalDocumentDto.builder()
                .type("I-9")
                .isRequired(true)
                .path("/docs/i9.pdf")
                .title("I-9 Form")
                .build();

        mvc.perform(post("/documents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .header("x-User-Id", "1")
                        .header("x-Username", "admin")
                        .header("x-Roles", "HR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title", is("I-9 Form")));
    }

    @Test
    @DisplayName("PUT /documents/{id} – updates document")
    void update_document() throws Exception {
        // create
        DigitalDocumentDto dto = DigitalDocumentDto.builder()
                .type("W-4")
                .isRequired(false)
                .path("/docs/w4.pdf")
                .title("W-4 Form")
                .build();
        String body = mvc.perform(post("/documents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .header("x-User-Id", "1")
                        .header("x-Username", "admin")
                        .header("x-Roles", "HR"))
                .andReturn().getResponse().getContentAsString();
        int id = objectMapper.readTree(body).get("data").get("id").asInt();

        // update
        dto.setTitle("Updated W-4");
        mvc.perform(put("/documents/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .header("x-User-Id", "1")
                        .header("x-Username", "admin")
                        .header("x-Roles", "HR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title", is("Updated W-4")));
    }
}