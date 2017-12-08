package com.company.AnimalShelterManagement.repository;

import com.company.AnimalShelterManagement.model.Animal;
import com.company.AnimalShelterManagement.model.Dog;
import com.company.AnimalShelterManagement.model.Person;
import com.company.AnimalShelterManagement.utils.AnimalFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static com.company.AnimalShelterManagement.model.Animal.Type.DOG;
import static com.company.AnimalShelterManagement.service.HibernateAnimalServiceTest.checkAnimalFieldsEquality;
import static com.company.AnimalShelterManagement.service.HibernatePersonServiceTest.checkPersonFieldsEquality;
import static com.company.AnimalShelterManagement.utils.AnimalFactory.newAvailableForAdoptionDog;
import static com.company.AnimalShelterManagement.utils.AnimalFactory.newlyReceivedDog;
import static com.company.AnimalShelterManagement.utils.TestConstant.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@ActiveProfiles("test")
public class AnimalRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private AnimalRepository animalRepository;

    private Dog testDog;
    private Person testPerson;

    @Test
    public void shouldReturnAllDogsAvailableForAdoptionWithNoDataProvided() {
        createAndPersistTwoDogs();

        Iterable<Animal> animals = animalRepository.findAnimalByAvailableForAdoption();

        assertThat(animals, hasItem(checkAnimalFieldsEquality(DOG_NAME, DOG, DATE_OF_BIRTH_VALUE)));
        assertThat(animals, not(hasItem(checkAnimalFieldsEquality(
                ANOTHER_DOG_NAME, DOG, LocalDate.now()))));
    }

    @Test
    public void shouldPerformReturnAnimalsAvailableForAdoptionByIdentifier() {
        createAndPersistTwoDogs();
        AnimalFactory.generateAnimalIdentifier(testDog);

        Iterable<Animal> animals = animalRepository.findAnimalByAvailableForAdoptionByIdentifier(DOG,
                testDog.getAnimalIdentifier());

        assertThat(animals, hasItem(checkAnimalFieldsEquality(DOG_NAME, DOG, DATE_OF_BIRTH_VALUE)));
    }

    @Test
    public void shouldPerformReturnAnimalsAvailableForAdoptionByName() {
        createAndPersistTwoDogs();

        Iterable<Animal> animals = animalRepository.findAnimalByAvailableForAdoptionByName(DOG, DOG_NAME);

        assertThat(animals, hasItem(checkAnimalFieldsEquality(DOG_NAME, DOG, DATE_OF_BIRTH_VALUE)));
    }

    @Test
    public void shouldReturnAnimalsWithLongestWaitingTime() {
        createAndPersistTwoDogs();

        Page<Animal> animalsWithLongestWaitingTime = animalRepository.findAnimalsWithLongestWaitingTime(
                new PageRequest(0, DOGS_COUNT));

        assertEquals(DOGS_COUNT, animalsWithLongestWaitingTime.getNumberOfElements());
        assertThat(animalsWithLongestWaitingTime, hasItem(checkAnimalFieldsEquality(
                DOG_NAME, DOG, DATE_OF_BIRTH_VALUE)));
    }

    @Test
    public void shouldReturnAnimalsOwnedByPerson() {
        setAsPreviousOwnerAndPersistPerson();

        Iterable<Animal> animalsOwnedByPerson = animalRepository.findAnimalsOwnedByPerson(testPerson.getId());

        assertThat(animalsOwnedByPerson, hasItem(checkAnimalFieldsEquality(
                DOG_NAME, DOG, DATE_OF_BIRTH_VALUE)));
    }

    @Test
    public void shouldReturnPersonRelatedWithAnimal() {
        setAsPreviousOwnerAndPersistPerson();

        Person p = animalRepository.findPersonByAnimalId(testDog.getId());

        assertThat(p.getAnimal(), hasItem(testDog));
        assertThat(testDog.getPreviousOwner(), is(checkPersonFieldsEquality(PERSON_FIRST_NAME, PERSON_LAST_NAME)));
    }

    @Test
    public void shouldReturnAnimalsCountForPeople() {
        setAsPreviousOwnerAndPersistPerson();

        long[] count = animalRepository.findAnimalsCountForPeople();
        int animalsCountForOnlyPerson = (int) count[0];

        assertEquals(EXPECTED_ANIMALS_FOR_PERSON_COUNT, animalsCountForOnlyPerson);
    }

    private void createAndPersistTwoDogs() {
        testDog = newAvailableForAdoptionDog(DOG_NAME, Dog.Race.GERMAN_SHEPERD);
        testDog.setDateOfBirth(DATE_OF_BIRTH_VALUE);
        Dog anotherDog = newlyReceivedDog(ANOTHER_DOG_NAME, Dog.Race.CROSSBREAD);
        anotherDog.setDateOfBirth(LocalDate.now());

        saveTwoDogsInDatabase(testDog, anotherDog);
    }

    private void saveTwoDogsInDatabase(Dog dog, Dog anotherDog) {
        entityManager.persist(dog);
        entityManager.persist(anotherDog);
    }

    private void setAsPreviousOwnerAndPersistPerson() {
        createAndPersistTwoDogs();
        testPerson = new Person(PERSON_FIRST_NAME, PERSON_LAST_NAME);
        entityManager.persist(testPerson);
        testPerson.addAnimal(testDog);
    }
}