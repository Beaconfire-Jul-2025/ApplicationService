package org.beaconfire.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.beaconfire.application.dto.*;
import org.beaconfire.application.service.DigitalDocumentService;
import org.beaconfire.application.exception.DocumentNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(DigitalDocumentController.class)
@AutoConfigureMockMvc(addFilters = false)
public class DigitalDocumentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DigitalDocumentService documentService;

    @Autowired
    private ObjectMapper objectMapper;

    private DigitalDocumentResponseDTO sampleDoc;
    private DigitalDocumentRequestDTO validRequest;

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

        validRequest = DigitalDocumentRequestDTO.builder()
                .type("Contract")
                .title("Contract Title")
                .description("Description")
                .required(true)
                .path("s3://bucket/doc.pdf")
                .build();
    }

    // Success: return 200 with all document list
    @Test
    void getAllDocuments_shouldReturn200WithList() throws Exception {
        when(documentService.getAllDocuments()).thenReturn(Arrays.asList(sampleDoc));

        mockMvc.perform(get("/document"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].type", is("Contract")));
    }

    // Success: return 200 with empty list
    @Test
    void getAllDocuments_shouldReturn200WithEmptyList() throws Exception {
        when(documentService.getAllDocuments()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/document"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    // Success: return 201
    @Test
    void createDocument_shouldReturn201_whenValid() throws Exception {
        when(documentService.createDocument(any())).thenReturn(1L);

        mockMvc.perform(post("/document")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.documentId", is(1)))
                .andExpect(jsonPath("$.message", is("Document created successfully")));
    }

    // Failure: return 400
    @Test
    void createDocument_shouldReturn400_whenMissingFields() throws Exception {
        DigitalDocumentRequestDTO invalidRequest = DigitalDocumentRequestDTO.builder()
                .type("") // invalid field
                .build();

        mockMvc.perform(post("/document")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    // Success: found document
    @Test
    void getDocumentById_shouldReturn200_whenFound() throws Exception {
        when(documentService.getDocumentById(1L)).thenReturn(sampleDoc);

        mockMvc.perform(get("/document/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.type", is("Contract")));
    }

    // Failure: document not found, return 404
    @Test
    void getDocumentById_shouldReturn404_whenNotFound() throws Exception {
        when(documentService.getDocumentById(99L))
            .thenThrow(new DocumentNotFoundException(99L));

        mockMvc.perform(get("/document/99"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("Document not found with id: 99")));
    }

    // Success: return 200
    @Test
    void updateDocument_shouldReturn200_whenValid() throws Exception {
        DigitalDocumentUpdateDTO updateDTO = DigitalDocumentUpdateDTO.builder()
            .type("UpdatedType")
            .title("UpdatedTitle")
            .description("Updated Description")
            .required(true)
            .build();

        doNothing().when(documentService).updateDocument(eq(1L), any());

        mockMvc.perform(put("/document/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message", is("Document updated successfully")));
    }

    // Failure: return 400
    @Test
    void updateDocument_shouldReturn400_whenMissingFields() throws Exception {
        DigitalDocumentUpdateDTO invalidDTO = DigitalDocumentUpdateDTO.builder()
            .type("")
            .title("")
            .required(false)
            .build();

        mockMvc.perform(put("/document/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDTO)))
            .andExpect(status().isBadRequest());
    }

    // Failure: return 404, document not found
    @Test
    void updateDocument_shouldReturn404_whenDocumentNotFound() throws Exception {
        DigitalDocumentUpdateDTO updateDTO = DigitalDocumentUpdateDTO.builder()
            .type("UpdatedType")
            .title("UpdatedTitle")
            .description("Updated Description")
            .required(true)
            .build();

        doThrow(new DocumentNotFoundException(999L))
            .when(documentService).updateDocument(eq(999L), any());

        mockMvc.perform(put("/document/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", is("Document not found with id: 999")));
    }

}
