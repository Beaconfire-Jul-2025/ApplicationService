package org.beaconfire.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.beaconfire.application.dto.ApplicationWorkflowRequestDTO;
import org.beaconfire.application.entity.ApplicationWorkFlow;
import org.beaconfire.application.exception.ApplicationAlreadyExistsException;
import org.beaconfire.application.service.ApplicationWorkflowService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ApplicationWorkflowController.class)
@AutoConfigureMockMvc(addFilters = false)
class ApplicationWorkflowControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApplicationWorkflowService applicationWorkflowService;

    @Autowired
    private ObjectMapper objectMapper;

    // Success: Create
    @Test
    void createApplication_shouldReturn201_whenValidRequest() throws Exception {
        ApplicationWorkflowRequestDTO requestDTO = ApplicationWorkflowRequestDTO.builder()
                .employeeId("EMP100")
                .applicationType("Onboarding")
                .build();

        ApplicationWorkFlow saved = ApplicationWorkFlow.builder()
                .id(1L)
                .employeeId("EMP100")
                .applicationType("Onboarding")
                .status("Pending")
                .build();

        when(applicationWorkflowService.createApplicationWorkflow(any()))
                .thenReturn(saved);

        mockMvc.perform(post("/application")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.applicationId").value(1))
                .andExpect(jsonPath("$.message").value("Application Created"));
    }

    // Failure: Exist
    @Test
    void createApplication_shouldReturn400_whenApplicationExists() throws Exception {
        ApplicationWorkflowRequestDTO requestDTO = ApplicationWorkflowRequestDTO.builder()
                .employeeId("EMP101")
                .applicationType("Onboarding")
                .build();

        when(applicationWorkflowService.createApplicationWorkflow(any()))
                .thenThrow(new ApplicationAlreadyExistsException("EMP101"));

        mockMvc.perform(post("/application")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Application already exists for employee: EMP101"));
    }

    // Failure: empty employeeId
    @Test
    void createApplication_shouldReturn400_whenEmployeeIdBlank() throws Exception {
        ApplicationWorkflowRequestDTO requestDTO = ApplicationWorkflowRequestDTO.builder()
                .employeeId(" ")
                .applicationType("Onboarding")
                .build();

        mockMvc.perform(post("/application")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.employeeId").exists());
    }

    // Failure: empty applicationType
    @Test
    void createApplication_shouldReturn400_whenApplicationTypeBlank() throws Exception {
        ApplicationWorkflowRequestDTO requestDTO = ApplicationWorkflowRequestDTO.builder()
                .employeeId("EMP200")
                .applicationType(" ")
                .build();

        mockMvc.perform(post("/application")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.applicationType").exists());
    }
}
