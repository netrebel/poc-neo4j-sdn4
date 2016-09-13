package com.poc.neo4j.repositories;

import com.poc.neo4j.domain.Person;
import org.springframework.data.neo4j.repository.GraphRepository;

public interface PersonRepository extends GraphRepository<Person> {

    Person findByName(String name);
}
