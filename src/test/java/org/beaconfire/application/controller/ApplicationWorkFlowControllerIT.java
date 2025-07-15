package org.beaconfire.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.beaconfire.application.dto.ApplicationWorkFlowDto;
import org.beaconfire.application.model.ApplicationWorkFlow;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Testcontainers
class ApplicationWorkFlowControllerIT extends MySqlTestContainer {
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
    @DisplayName("GET /workflows – returns empty page when no data")
    void getAll_empty() throws Exception {
        mvc.perform(get("/workflows"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", empty()));
    }

    @Test
    @DisplayName("POST /workflows – creates workflow")
    void create_workflow() throws Exception {
        ApplicationWorkFlowDto dto = ApplicationWorkFlowDto.builder()
                .employeeId("E-123")
                .status(ApplicationWorkFlow.WorkFlowStatus.PENDING)
                .comment("new hire")
                .build();

        mvc.perform(post("/workflows")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .header("x-User-Id", "1")
                        .header("x-Username", "admin")
                        .header("x-Roles", "HR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.employeeId", is("E-123")));
    }

    @Test
    @DisplayName("GET /workflows/{id} – returns 400 when workflow not found")
    void getById_notFound() throws Exception {
        mvc.perform(get("/workflows/9999"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode", is("400002")));
    }
}
