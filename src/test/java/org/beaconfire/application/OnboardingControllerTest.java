package org.beaconfire.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.beaconfire.application.controller.OnboardingController;
import org.beaconfire.application.dto.OnboardingSubmitDTO;
import org.beaconfire.application.entity.ApplicationWorkFlow;
import org.beaconfire.application.service.ApplicationWorkflowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@WebMvcTest(OnboardingController.class)
@AutoConfigureMockMvc(addFilters = false)
public class OnboardingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApplicationWorkflowService applicationWorkflowService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    private String loadJson(String filename) throws Exception {
        return Files.lines(Paths.get("src/test/resources/testdata/" + filename))
                .collect(Collectors.joining("\n"));
    }

    @Test
    public void testSubmitOnboardingForm_WithFullJson_ReturnsCreated() throws Exception {
        String json = loadJson("valid_onboarding.json");

        OnboardingSubmitDTO dto = objectMapper.readValue(json, OnboardingSubmitDTO.class);

        Mockito.when(applicationWorkflowService.saveApplicationWorkflow(any(), any(), any()))
                .thenReturn(ApplicationWorkFlow.builder()
                        .employeeId(dto.getId())
                        .applicationType(dto.getApplicationType())
                        .status("PENDING")
                        .build());

        mockMvc.perform(post("/api/onboarding/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                        .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void testSubmitOnboardingForm_MissingRequiredField_ReturnsBadRequest() throws Exception {
        String invalidJson = loadJson("invalid_onboarding.json");

        mockMvc.perform(post("/api/onboarding/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                        .andDo(print())
                .andExpect(status().isBadRequest());
    }
}

