package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.VetSpecialty;
import com.tecsup.petclinic.exceptions.DuplicateVetSpecialtyException;
import com.tecsup.petclinic.repositories.VetSpecialtyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VetSpecialtyServiceTest {

	@Mock
	private VetSpecialtyRepository vetSpecialtyRepository;

	@InjectMocks
	private VetSpecialtyServiceImpl vetSpecialtyService;

	@Test
	void testAssignSpecialtyToVet() {
		VetSpecialty assignment = new VetSpecialty(7, 3);
		when(vetSpecialtyRepository.existsByVetIdAndSpecialtyId(7, 3)).thenReturn(false);
		when(vetSpecialtyRepository.save(new VetSpecialty(7, 3))).thenReturn(assignment);

		VetSpecialty result = vetSpecialtyService.assignSpecialtyToVet(7, 3);

		assertNotNull(result);
		assertEquals(7, result.getVetId());
		assertEquals(3, result.getSpecialtyId());
		verify(vetSpecialtyRepository).save(new VetSpecialty(7, 3));
	}

	@Test
	void testRemoveSpecialtyFromVet() {
		vetSpecialtyService.removeSpecialtyFromVet(7, 3);

		verify(vetSpecialtyRepository).deleteByVetIdAndSpecialtyId(7, 3);
	}

	@Test
	void testAssignDuplicate_ShouldFail() {
		when(vetSpecialtyRepository.existsByVetIdAndSpecialtyId(7, 3)).thenReturn(true);

		assertThrows(DuplicateVetSpecialtyException.class,
				() -> vetSpecialtyService.assignSpecialtyToVet(7, 3));

		verify(vetSpecialtyRepository, never()).save(new VetSpecialty(7, 3));
	}
}
