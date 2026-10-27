package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.VetSpecialty;
import com.tecsup.petclinic.exceptions.DuplicateVetSpecialtyException;
import com.tecsup.petclinic.repositories.VetSpecialtyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
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
}
