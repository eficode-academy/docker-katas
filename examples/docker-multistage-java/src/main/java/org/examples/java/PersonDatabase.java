package org.examples.java;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import javax.ws.rs.NotFoundException;

@Singleton
public class PersonDatabase {

    List<Person> persons;

    @PostConstruct
    public void init() {
        persons = Arrays.asList(
                new Person("Penny"), 
                new Person("Leonard"), 
                new Person("Sheldon"), 
                new Person("Amy"), 
                new Person("Howard"), 
                new Person("Bernadette"), 
                new Person("Raj"), 
                new Person("Priya"));
    }

    public Person[] currentList() {
        return persons.toArray(new Person[0]);
    }

    public Person getPerson(int id) {
        if (id < persons.size()) {
            return persons.get(id);
        }

        throw new NotFoundException("Person with id \"" + id + "\" not found.");
    }
}
