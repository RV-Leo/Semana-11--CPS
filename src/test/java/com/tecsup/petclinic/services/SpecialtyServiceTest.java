package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.VetSpecialty;
import com.tecsup.petclinic.repositories.VetSpecialtyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VetSpecialtyServiceTest {

	@Mock
	private VetSpecialtyRepository vetSpecialtyRepository;

	@InjectMocks
	private VetSpecialtyServiceImpl vetSpecialtyService;

	@Test
	void testFindSpecialtiesByVet() {
		VetSpecialty vs1 = new VetSpecialty(1, 10);
		VetSpecialty vs2 = new VetSpecialty(1, 20);
		List<VetSpecialty> lista = List.of(vs1, vs2);

		when(vetSpecialtyRepository.findByVetId(1)).thenReturn(lista);

		List<VetSpecialty> resultado = vetSpecialtyService.findSpecialtiesByVet(1);

		assertEquals(2, resultado.size());
		verify(vetSpecialtyRepository).findByVetId(1);
	}

	@Test
	void testFindVetsBySpecialty() {
		VetSpecialty vs1 = new VetSpecialty(1, 5);
		VetSpecialty vs2 = new VetSpecialty(2, 5);
		List<VetSpecialty> lista = List.of(vs1, vs2);

		when(vetSpecialtyRepository.findBySpecialtyId(5)).thenReturn(lista);

		List<VetSpecialty> resultado = vetSpecialtyService.findVetsBySpecialty(5);

		assertEquals(2, resultado.size());
		verify(vetSpecialtyRepository).findBySpecialtyId(5);
	}

	@Test
	void testFindSpecialtiesByVet_Empty() {
		when(vetSpecialtyRepository.findByVetId(999)).thenReturn(List.of());

		List<VetSpecialty> resultado = vetSpecialtyService.findSpecialtiesByVet(999);

		assertTrue(resultado.isEmpty());
		verify(vetSpecialtyRepository).findByVetId(999);
	}
}