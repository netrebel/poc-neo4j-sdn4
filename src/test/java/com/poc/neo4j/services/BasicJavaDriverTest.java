package com.poc.neo4j.services;

import org.neo4j.driver.v1.*;

public class BasicJavaDriverTest {
    public static void main(String[] args) {
        Driver driver = GraphDatabase.driver("bolt://localhost", AuthTokens.basic("neo4j", "1234"));
        Session session = driver.session();

        session.run("CREATE (a:Person {name:'Arthur', title:'King'})");

        StatementResult result = session.run("MATCH (a:Person) WHERE a.name = 'Arthur' RETURN a.name AS name, a.title AS title");
        while (result.hasNext()) {
            Record record = result.next();
            System.out.println(record.get("title").asString() + " " + record.get("name").asString());
        }

        session.close();
        driver.close();
    }
}
