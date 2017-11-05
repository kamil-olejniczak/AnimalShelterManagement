package com.company.AnimalShelterManagement.repository;

import com.company.AnimalShelterManagement.model.Animal;
import org.springframework.data.repository.CrudRepository;

public interface AnimalRepository<T extends Animal> extends CrudRepository<T, Long> {
}