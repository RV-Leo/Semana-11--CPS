package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("h2")
class VetSpecialtyControllerTest {
	@Autowired private MockMvc mockMvc;
	@Autowired private ObjectMapper objectMapper;

	@Test
	void testAssignSpecialtyToVet() throws Exception {
		mockMvc.perform(post("/vet-specialties")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(Map.of("vetId", 1, "specId", 3))))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.vetId", is(1)))
				.andExpect(jsonPath("$.specialtyId", is(3)));
	}

	@Test
	void testRemoveSpecialtyFromVet() throws Exception {
		mockMvc.perform(post("/vet-specialties")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(Map.of("vetId", 1, "specId", 2))))
				.andExpect(status().isCreated());

		mockMvc.perform(delete("/vet-specialties/1/2"))
				.andExpect(status().isOk());
	}

	@Test
	void testAssignDuplicate_ShouldFail() throws Exception {
		// La combinación (2, 1) está precargada en data.sql.
		mockMvc.perform(post("/vet-specialties")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(Map.of("vetId", 2, "specId", 1))))
				.andExpect(status().isConflict());
	}
}
