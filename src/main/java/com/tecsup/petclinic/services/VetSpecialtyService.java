package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.VetSpecialty;
import java.util.List;

public interface VetSpecialtyService {

    VetSpecialty assignSpecialtyToVet(Integer vetId, Integer specialtyId);

    void removeSpecialtyFromVet(Integer vetId, Integer specialtyId);

    List<VetSpecialty> findSpecialtiesByVet(Integer vetId);

    List<VetSpecialty> findVetsBySpecialty(Integer specialtyId);
}