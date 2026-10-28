package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.VetSpecialty;
import com.tecsup.petclinic.exceptions.DuplicateVetSpecialtyException;
import com.tecsup.petclinic.repositories.VetSpecialtyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Servicio para administrar la experiencia y certificación por especialidad.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class VetSpecialtyServiceImpl implements VetSpecialtyService {

	private final VetSpecialtyRepository vetSpecialtyRepository;

	@Override
	public VetSpecialty assignSpecialtyToVet(Integer vetId, Integer specialtyId) {
		if (vetSpecialtyRepository.existsByVetIdAndSpecialtyId(vetId, specialtyId)) {
			throw new DuplicateVetSpecialtyException("Specialty is already assigned to this veterinarian");
		}
		return vetSpecialtyRepository.save(new VetSpecialty(vetId, specialtyId));
	}

	@Override
	public void removeSpecialtyFromVet(Integer vetId, Integer specialtyId) {
		vetSpecialtyRepository.deleteByVetIdAndSpecialtyId(vetId, specialtyId);
	}

	/**
	 * @param specialtyId identificador de la especialidad
	 * @param minYears años mínimos de experiencia
	 * @return asignaciones que cumplen la experiencia mínima
	 */
	@Override
	public List<VetSpecialty> findVetsByMinExperience(Integer specialtyId, Integer minYears) {

		List<VetSpecialty> assignments = vetSpecialtyRepository
				.findBySpecialtyIdAndYearsExperienceGreaterThanEqual(specialtyId, minYears);

		assignments.forEach(assignment -> log.info("{}", assignment));

		return assignments;
	}

	/**
	 * @param vetId identificador del veterinario
	 * @param specialtyId identificador de la especialidad
	 * @param yearsExperience nuevos años de experiencia
	 * @return cantidad de filas actualizadas
	 */
	@Override
	public int updateYearsExperience(Integer vetId, Integer specialtyId, Integer yearsExperience) {

		int updatedRows = vetSpecialtyRepository
				.updateYearsExperience(vetId, specialtyId, yearsExperience);

		log.info("Asignaciones actualizadas para años de experiencia: {}", updatedRows);

		return updatedRows;
	}

	/**
	 * @param vetId identificador del veterinario
	 * @param specialtyId identificador de la especialidad
	 * @param certificationDate nueva fecha de certificación
	 * @return cantidad de filas actualizadas
	 */
	@Override
	public int updateCertificationDate(Integer vetId, Integer specialtyId, LocalDate certificationDate) {

		int updatedRows = vetSpecialtyRepository
				.updateCertificationDate(vetId, specialtyId, certificationDate);

		log.info("Asignaciones actualizadas para fecha de certificación: {}", updatedRows);

		return updatedRows;
	}
}
