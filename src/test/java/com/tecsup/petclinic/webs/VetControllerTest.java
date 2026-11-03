package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.petclinic.entities.Vet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("h2")
class VetControllerTest {
	@Autowired private MockMvc mockMvc;
	@Autowired private ObjectMapper objectMapper;

	@Test
	void testFindVet() throws Exception {
		mockMvc.perform(get("/vets/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.lastName", is("Carter")));
	}

	@Test
	void testFindVets() throws Exception {
		mockMvc.perform(get("/vets"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].id").exists());
	}

	@Test
	void testFindVetNotFound() throws Exception {
		mockMvc.perform(get("/vets/666")).andExpect(status().isNotFound());
	}

	@Test
	void testCreateVet() throws Exception {
		mockMvc.perform(post("/vets").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(newVet("Ana", "Lopez"))))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").isNumber())
				.andExpect(jsonPath("$.firstName", is("Ana")))
				.andExpect(jsonPath("$.lastName", is("Lopez")));
	}

	@Test
	void testUpdateVet() throws Exception {
		int id = createVet();
		Vet updated = newVet("Ana", "Perez");

		mockMvc.perform(put("/vets/{id}", id).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updated)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.lastName", is("Perez")));

		mockMvc.perform(get("/vets/{id}", id))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.lastName", is("Perez")));
	}

	@Test
	void testDeleteVet() throws Exception {
		int id = createVet();
		mockMvc.perform(delete("/vets/{id}", id)).andExpect(status().isOk());
		mockMvc.perform(get("/vets/{id}", id)).andExpect(status().isNotFound());
	}

	private int createVet() throws Exception {
		String response = mockMvc.perform(post("/vets").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(newVet("Test", "Veterinarian"))))
				.andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
		return objectMapper.readTree(response).get("id").asInt();
	}

	private Vet newVet(String firstName, String lastName) {
		Vet vet = new Vet();
		vet.setFirstName(firstName);
		vet.setLastName(lastName);
		vet.setEmail("vet" + System.nanoTime() + "@example.test");
		vet.setPhone("5550101");
		vet.setActive(true);
		return vet;
	}
}
