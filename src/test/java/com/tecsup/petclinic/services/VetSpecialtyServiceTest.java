package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.VetSpecialty;
import com.tecsup.petclinic.entities.VetSpecialtyId;
import com.tecsup.petclinic.exceptions.DuplicateVetSpecialtyException;
import com.tecsup.petclinic.repositories.VetSpecialtyRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.NestedTestConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Slf4j
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

	// Pruebas del integrante D: experiencia y certificaciones
	@Nested
	@SpringBootTest
	@Transactional
	@NestedTestConfiguration(NestedTestConfiguration.EnclosingConfiguration.OVERRIDE)
	class ExperienceAndCertificationTests {

		@Autowired
		private VetSpecialtyService vetSpecialtyService;

		@Autowired
		private VetSpecialtyRepository vetSpecialtyRepository;

		/**
		 * Busca veterinarios que cumplen la experiencia mínima en una especialidad.
		 */
		@Test
		void testFindVetsByMinExperience() {
			int SPECIALTY_ID = 1;
			int MIN_YEARS = 7;

			List<VetSpecialty> assignments = this.vetSpecialtyService
					.findVetsByMinExperience(SPECIALTY_ID, MIN_YEARS);

			assertEquals(2, assignments.size());
			for (VetSpecialty assignment : assignments) {
				assertTrue(assignment.getYearsExperience() >= MIN_YEARS);
				assertEquals(SPECIALTY_ID, assignment.getSpecialtyId());
			}

			log.info("Asignaciones que cumplen la experiencia mínima: {}", assignments);
		}

		/**
		 * Actualiza los años de experiencia de un veterinario en una especialidad.
		 */
		@Test
		void testUpdateYearsExperience() {
			int VET_ID = 2;
			int SPECIALTY_ID = 1;
			int YEARS_EXPERIENCE = 10;

			int updatedRows = this.vetSpecialtyService
					.updateYearsExperience(VET_ID, SPECIALTY_ID, YEARS_EXPERIENCE);

			VetSpecialty updatedAssignment = this.vetSpecialtyRepository
					.findById(new VetSpecialtyId(VET_ID, SPECIALTY_ID))
					.orElse(null);

			assertEquals(1, updatedRows);
			assertNotNull(updatedAssignment);
			assertEquals(YEARS_EXPERIENCE, updatedAssignment.getYearsExperience());
		}

		/**
		 * Actualiza la fecha de certificación de un veterinario en una especialidad.
		 */
		@Test
		void testUpdateCertificationDate() {
			int VET_ID = 2;
			int SPECIALTY_ID = 1;
			LocalDate certificationDate = LocalDate.of(2026, 10, 27);

			int updatedRows = this.vetSpecialtyService
					.updateCertificationDate(VET_ID, SPECIALTY_ID, certificationDate);

			VetSpecialty updatedAssignment = this.vetSpecialtyRepository
					.findById(new VetSpecialtyId(VET_ID, SPECIALTY_ID))
					.orElse(null);

			assertEquals(1, updatedRows);
			assertNotNull(updatedAssignment);
			assertEquals(certificationDate, updatedAssignment.getCertificationDate());
		}
	}
}



