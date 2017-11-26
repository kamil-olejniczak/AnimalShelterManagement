package com.company.AnimalShelterManagement.controller;

import com.company.AnimalShelterManagement.model.Dog;
import com.company.AnimalShelterManagement.model.dto.DogDTO;
import com.company.AnimalShelterManagement.service.interfaces.DogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping(value = "/api", produces = APPLICATION_JSON_UTF8_VALUE)
public class DogController {

    private final DogService dogService;

    @Autowired
    public DogController(DogService dogService) {
        this.dogService = dogService;
    }

    @GetMapping("${rest.dog.getDogs}")
    public Iterable<DogDTO> returnDogs() {
        return dogService.returnDogs();
    }

    @GetMapping("${rest.dog.getDog}")
    public DogDTO returnDog(@PathVariable Long dogId) {
        return mapToDTO(dogService.returnDog(dogId));
    }

    @PostMapping(value = "${rest.dog.postDog}", consumes = APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(CREATED)
    public DogDTO saveDog(@Valid @RequestBody DogDTO dog) {
        return dogService.saveDog(dog);
    }

    @PutMapping(value = "${rest.dog.putDog}", consumes = APPLICATION_JSON_UTF8_VALUE)
    public DogDTO updateDog(@RequestBody DogDTO dogDTO) {
        return dogService.updateDog(dogDTO);
    }

    @DeleteMapping("${rest.dog.deleteDog}")
    public ResponseEntity<Void> deleteDog(@PathVariable Long dogId) {
        dogService.deleteDog(dogId);
        return ResponseEntity.status(OK).build();
    }

    private DogDTO mapToDTO(Dog dog) {
        return dogService.mapToDTO(dog);
    }
}