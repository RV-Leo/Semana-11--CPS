package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.petclinic.entities.Owner;
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
class OwnerControllerTest {
	@Autowired private MockMvc mockMvc;
	@Autowired private ObjectMapper objectMapper;

	@Test
	void testFindOwner() throws Exception {
		mockMvc.perform(get("/owners/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.lastName", is("Franklin")));
	}

	@Test
	void testFindOwners() throws Exception {
		mockMvc.perform(get("/owners"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].id").exists());
	}

	@Test
	void testFindOwnerNotFound() throws Exception {
		mockMvc.perform(get("/owners/666")).andExpect(status().isNotFound());
	}

	@Test
	void testCreateOwner() throws Exception {
		mockMvc.perform(post("/owners").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(newOwner("Ana", "Lopez"))))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").isNumber())
				.andExpect(jsonPath("$.firstName", is("Ana")))
				.andExpect(jsonPath("$.lastName", is("Lopez")));
	}

	@Test
	void testUpdateOwner() throws Exception {
		long id = createOwner();
		mockMvc.perform(put("/owners/{id}", id).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(newOwner("Ana", "Perez"))))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.lastName", is("Perez")));

		mockMvc.perform(get("/owners/{id}", id))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.lastName", is("Perez")));
	}

	@Test
	void testDeleteOwner() throws Exception {
		long id = createOwner();
		mockMvc.perform(delete("/owners/{id}", id)).andExpect(status().isOk());
		mockMvc.perform(get("/owners/{id}", id)).andExpect(status().isNotFound());
	}

	private long createOwner() throws Exception {
		String response = mockMvc.perform(post("/owners").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(newOwner("Test", "Owner"))))
				.andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
		return objectMapper.readTree(response).get("id").asLong();
	}

	private Owner newOwner(String firstName, String lastName) {
		Owner owner = new Owner();
		owner.setFirstName(firstName);
		owner.setLastName(lastName);
		owner.setAddress("Av. Central 123");
		owner.setCity("Lima");
		owner.setTelephone("5550101");
		return owner;
	}
}
