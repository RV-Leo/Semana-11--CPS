package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.VetSpecialty;

import java.time.LocalDate;
import java.util.List;

public interface VetSpecialtyService {

	VetSpecialty assignSpecialtyToVet(Integer vetId, Integer specialtyId);

	void removeSpecialtyFromVet(Integer vetId, Integer specialtyId);

	/**
	 * Busca veterinarios que cumplen la experiencia mínima en una especialidad.
	 *
	 * @param specialtyId identificador de la especialidad
	 * @param minYears años mínimos de experiencia
	 * @return asignaciones que cumplen la experiencia mínima
	 */
	List<VetSpecialty> findVetsByMinExperience(Integer specialtyId, Integer minYears);

	/**
	 * Actualiza los años de experiencia de una asignación.
	 *
	 * @param vetId identificador del veterinario
	 * @param specialtyId identificador de la especialidad
	 * @param yearsExperience nuevos años de experiencia
	 * @return cantidad de filas actualizadas
	 */
	int updateYearsExperience(Integer vetId, Integer specialtyId, Integer yearsExperience);

	/**
	 * Actualiza la fecha de certificación de una asignación.
	 *
	 * @param vetId identificador del veterinario
	 * @param specialtyId identificador de la especialidad
	 * @param certificationDate nueva fecha de certificación
	 * @return cantidad de filas actualizadas
	 */
	int updateCertificationDate(Integer vetId, Integer specialtyId, LocalDate certificationDate);
}
