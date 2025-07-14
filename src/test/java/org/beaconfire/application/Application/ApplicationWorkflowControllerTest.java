package org.beaconfire.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.beaconfire.application.dto.*;
import org.beaconfire.application.entity.ApplicationWorkFlow;
import org.beaconfire.application.exception.*;
import org.beaconfire.application.service.ApplicationWorkflowService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Arrays;
import java.util.Collections;


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

    // Success: Application List
    @Test
    void getPendingApplications_shouldReturnList_whenPendingExists() throws Exception {
        List<ApplicationWorkFlow> pendingList = Arrays.asList(
                ApplicationWorkFlow.builder()
                .id(1L)
                .employeeId("EMP001")
                .applicationType("Onboarding")
                .status("Pending")
                .build(),
                ApplicationWorkFlow.builder()
                .id(2L)
                .employeeId("EMP002")
                .applicationType("StatusChange")
                .status("Pending")
                .build()
                );

        when(applicationWorkflowService.getApplicationsByStatus("Pending"))
            .thenReturn(pendingList);

        mockMvc.perform(get("/application/pending"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].employeeId").value("EMP001"))
            .andExpect(jsonPath("$[1].employeeId").value("EMP002"));
    }

    // Success: empty List
    @Test
    void getPendingApplications_shouldReturnEmptyList_whenNoPending() throws Exception {
        when(applicationWorkflowService.getApplicationsByStatus("Pending"))
            .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/application/pending"))
            .andExpect(status().isOk())
            .andExpect(content().json("[]"));
    }

    // Success: Updated
    @Test
    void updateApplication_shouldReturn200_whenValidRequest() throws Exception {
        Long id = 1L;
        ApplicationStatusUpdateDTO dto = ApplicationStatusUpdateDTO.builder()
            .status("COMPLETED")
            .comment("Approved by HR")
            .build();

        mockMvc.perform(put("/application/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Application updated"));

        verify(applicationWorkflowService).updateApplicationStatus(id, "COMPLETED", "Approved by HR");
    }

    // Failure: Invalid Status
    @Test
    void updateApplication_shouldReturn400_whenStatusInvalid() throws Exception {
        Long id = 1L;
        ApplicationStatusUpdateDTO dto = ApplicationStatusUpdateDTO.builder()
            .status("APPROVED")
            .comment("Not allowed status")
            .build();

        mockMvc.perform(put("/application/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Status error"));

        verify(applicationWorkflowService, never()).updateApplicationStatus(any(), any(), any());
    }

    // Failure: Application ID not found
    @Test
    void updateApplication_shouldReturn404_whenApplicationNotFound() throws Exception {
        Long id = 999L;
        ApplicationStatusUpdateDTO dto = ApplicationStatusUpdateDTO.builder()
            .status("REJECTED")
            .comment("Not eligible")
            .build();

        doThrow(new ApplicationNotFoundException(id))
            .when(applicationWorkflowService).updateApplicationStatus(id, "REJECTED", "Not eligible");

        mockMvc.perform(put("/application/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("Application not found with id: 999"));
    }
}
