package com.poc.neo4j.repositories;

import com.poc.neo4j.MyNeo4jTestConfiguration;
import com.poc.neo4j.domain.Movie;
import com.poc.neo4j.domain.Person;
import com.poc.neo4j.domain.Role;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 *
 * @author pdtyreus
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MyNeo4jTestConfiguration.class)
@ActiveProfiles(profiles = "test")
public class MovieRepositoryTest {
    
    @Autowired
    private MovieRepository instance;
     @Autowired
    private PersonRepository personRepository;
    
    public MovieRepositoryTest() {
    }
    
    @Before
    public void initialize() {
       System.out.println("seeding embedded database");
       Movie matrix = new Movie();
       matrix.setTitle("The Matrix");
       matrix.setReleased(1999);
       
       instance.save(matrix);
       
       Person keanu = new Person();
       keanu.setName("Keanu Reeves");
       
       personRepository.save(keanu);
       
       Role neo = new Role();
       neo.setMovie(matrix);
       neo.setPerson(keanu);
       Collection<String> roleNames = new HashSet();
       roleNames.add("Neo");
       neo.setRoles(roleNames);
       
       List<Role> roles = new ArrayList();
       roles.add(neo);
       
       matrix.setRoles(roles);
       
       instance.save(matrix);
    }

    /**
     * Test of findByTitle method, of class MovieRepository.
     */
    @Test
    @DirtiesContext
    public void testFindByTitle() {
        
        System.out.println("findByTitle");
        String title = "The Matrix";
        Movie result = instance.findByTitle(title);
        assertNotNull(result);
        assertEquals(1999, result.getReleased());
    }

    /**
     * Test of findByTitleContaining method, of class MovieRepository.
     */
    @Test
    @DirtiesContext
    public void testFindByTitleContaining() {
        System.out.println("findByTitleContaining");
        String title = "Matrix";
        Collection<Movie> result = instance.findByTitleContaining(title);
        assertNotNull(result);
        assertEquals(1,result.size());
    }

    /**
     * Test of graph method, of class MovieRepository.
     */
    @Test
    @DirtiesContext
    public void testGraph() {
        System.out.println("graph");
        List<Map<String,Object>> graph = instance.graph(5);
        
        assertEquals(1,graph.size());
        
        Map<String,Object> map = graph.get(0);
        
        assertEquals(2,map.size());
        
        String[] cast = (String[])map.get("cast");
        String movie = (String)map.get("movie");
        
        assertEquals("The Matrix",movie);
        assertEquals("Keanu Reeves", cast[0]);
    }
}
