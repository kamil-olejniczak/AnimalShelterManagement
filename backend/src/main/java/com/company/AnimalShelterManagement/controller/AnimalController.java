package com.company.AnimalShelterManagement.controller;

import com.company.AnimalShelterManagement.model.Animal;
import com.company.AnimalShelterManagement.model.Person;
import com.company.AnimalShelterManagement.service.interfaces.AnimalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/api")
public class AnimalController {

    private final AnimalService animalService;

    @Autowired
    public AnimalController(@Qualifier("defaultAnimalService") AnimalService animalService) {
        this.animalService = animalService;
    }

    @GetMapping(value = "${rest.animal.getAnimals}", produces = APPLICATION_JSON_UTF8_VALUE)
    public Iterable<Animal> returnAnimals() {
        return animalService.returnAnimals();
    }

    @GetMapping(value = "${rest.animal.getAnimalsAvailableForAdoption}",
            produces = APPLICATION_JSON_UTF8_VALUE)
    public Iterable<Animal> returnAnimalsAvailableForAdoption() {
        return animalService.returnAnimalsAvailableForAdoption();
    }

    @GetMapping(value = "${rest.animal.previousOwner}",
            produces = APPLICATION_JSON_UTF8_VALUE)
    public Person returnPreviousOwner(@PathVariable Long animalId) {
        return animalService.returnPreviousOwner(animalId);
    }

    @GetMapping(value = "${rest.animal.animalsCount}")
    public long returnAnimalsCount() {
        return animalService.countAnimals();
    }
}