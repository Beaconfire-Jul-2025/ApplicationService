package org.beaconfire.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.beaconfire.application.dto.DigitalDocumentResponseDTO;
import org.beaconfire.application.service.DigitalDocumentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DigitalDocumentController.class)
@AutoConfigureMockMvc(addFilters = false)
class DigitalDocumentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DigitalDocumentService documentService;

    @Autowired
    private ObjectMapper objectMapper;

    private DigitalDocumentResponseDTO sampleDoc;

    @BeforeEach
    void setup() {
        sampleDoc = DigitalDocumentResponseDTO.builder()
                .id(1L)
                .type("Contract")
                .title("Contract Title")
                .description("Description")
                .required(true)
                .path("s3://bucket/doc.pdf")
                .build();
    }

    // Success: return 200 with List
    @Test
    void getAllDocuments_shouldReturn200WithList() throws Exception {
        when(documentService.getAllDocuments()).thenReturn(Arrays.asList(sampleDoc));

        mockMvc.perform(get("/document"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(sampleDoc.getId()))
                .andExpect(jsonPath("$[0].type").value("Contract"));
    }

    // Success: return 200 with emptyList
    @Test
    void getAllDocuments_shouldReturn200WithEmptyList() throws Exception {
        when(documentService.getAllDocuments()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/document"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}
