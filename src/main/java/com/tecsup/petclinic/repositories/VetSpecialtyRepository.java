package com.tecsup.petclinic.repositories;

import com.tecsup.petclinic.entities.VetSpecialty;
import com.tecsup.petclinic.entities.VetSpecialtyId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VetSpecialtyRepository extends JpaRepository<VetSpecialty, VetSpecialtyId> {

	boolean existsByVetIdAndSpecialtyId(Integer vetId, Integer specialtyId);

	void deleteByVetIdAndSpecialtyId(Integer vetId, Integer specialtyId);

	List<VetSpecialty> findBySpecialtyIdAndYearsExperienceGreaterThanEqual(
			Integer specialtyId, Integer yearsExperience);

	@Modifying
	@Transactional
	@Query("UPDATE VetSpecialty vs SET vs.yearsExperience = :yearsExperience "
			+ "WHERE vs.vetId = :vetId AND vs.specialtyId = :specialtyId")
	int updateYearsExperience(@Param("vetId") Integer vetId,
							  @Param("specialtyId") Integer specialtyId,
							  @Param("yearsExperience") Integer yearsExperience);

	@Modifying
	@Transactional
	@Query("UPDATE VetSpecialty vs SET vs.certificationDate = :certificationDate "
			+ "WHERE vs.vetId = :vetId AND vs.specialtyId = :specialtyId")
	int updateCertificationDate(@Param("vetId") Integer vetId,
								@Param("specialtyId") Integer specialtyId,
								@Param("certificationDate") LocalDate certificationDate);
}
