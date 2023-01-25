package com.estore.api.estoreapi.persistence;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.estore.api.estoreapi.model.Jersey;
import com.estore.api.estoreapi.model.Jersey.Color;
import com.estore.api.estoreapi.model.Jersey.Size;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test the JerseyFileDAO class
 */
@Tag("Persistence-tier")
public class JerseyFileDAOTest {
    JerseyFileDAO jerseyFileDAO;
    Jersey[] jerseys;
    ObjectMapper mapper;

    @BeforeEach
    public void setupJerseyFileDAO() throws IOException {
        mapper = mock(ObjectMapper.class);

        jerseys = new Jersey[2];

        jerseys[0] = new Jersey(0, 1, "Jimenez Jersey",1 ,Size.SMALL , Color.BLUE, 1);
        jerseys[1] = new Jersey(1, 2, "Saldanha Jersey",2, Size.SMALL , Color.BLUE, 1);

        when(mapper.readValue(new File("jerseys.json"), Jersey[].class))
            .thenReturn(jerseys);
        jerseyFileDAO = new JerseyFileDAO("jerseys.json", mapper);
    }

    @Test
    public void testUpdateJersey() throws IOException {
        Jersey jersey = new Jersey(0, 1, "Dr. Disney West Jersey",1 ,Size.SMALL , Color.BLUE, 1);

        Jersey result = assertDoesNotThrow(() -> 
            jerseyFileDAO.updateJersey(jersey), "Unexpected exception thrown");
        
        assertNotNull(result);
        Jersey actual = jerseyFileDAO.getJersey(jersey.getId());
        assertEquals(actual, jersey);
    }

    @Test
    public void testUpdateJerseyNotFound() throws IOException {
        Jersey jersey = new Jersey(0xDEADBEEF, 2, "Dr. Carter West Jersey",1 ,Size.SMALL , Color.BLUE, 1);
        
        Jersey result = assertDoesNotThrow(() -> 
            jerseyFileDAO.updateJersey(jersey), "Unexpected exception thrown");
        
        assertNull(result);
    }

    @Test
    public void testSearchJerseysNoTerm() throws IOException {
        Jersey[] result = assertDoesNotThrow(() -> 
            jerseyFileDAO.findJerseys(null), "Unexpected exception thrown");


        assertNotNull(result);
        assertArrayEquals(jerseys, result);
    }

    @Test
    public void testSearchJerseys() throws IOException {
        Jersey[] result = assertDoesNotThrow(() -> 
            jerseyFileDAO.findJerseys("Jimenez"), "Unexpected exception thrown");

        assertNotNull(result);
        assertArrayEquals(new Jersey[] { jerseys[0] }, result);
    }

    @Test
    public void testGetJersey() throws IOException {
        Jersey result = assertDoesNotThrow(() ->
            jerseyFileDAO.getJersey(jerseys[0].getId()), "Unexpected exception thrown");

        assertNotNull(result);
        assertEquals(jerseys[0], result);
    }

    @Test
    public void testGetJerseys() throws IOException {
        Jersey[] result = assertDoesNotThrow(() -> 
            jerseyFileDAO.getJerseys(), "Unexpected exception thrown");

        assertNotNull(result);
        assertArrayEquals(jerseys, result);
    }

    @Test
    public void testGetJerseyNotFound() throws IOException {
        Jersey result = assertDoesNotThrow(() ->
            jerseyFileDAO.getJersey(0xDEADBEEF), "Unexpected exception thrown");
        
        assertNull(result);
    }

    @Test
    public void testDeleteJersey() throws IOException {
        boolean result = assertDoesNotThrow(() ->
            jerseyFileDAO.deleteJersey(jerseys[0].getId()), "Unexpected exception thrown");

        assertTrue(result);
    }

    @Test
    public void testDeleteJerseyNotFound() throws IOException {
        boolean result = assertDoesNotThrow(() ->
            jerseyFileDAO.deleteJersey(0xDEADBEEF), "Unexpected exception thrown");

        assertFalse(result);
    }

    @Test
    public void testCreateJersey() throws IOException {
        Jersey jersey = new Jersey(0xDEADBEEF, 2, "Dr. Carter West Jersey",1 ,Size.SMALL , Color.BLUE, 1);

        Jersey actual = assertDoesNotThrow(() ->
            jerseyFileDAO.createJersey(jersey), "Unexpected exception thrown");

        assertNotNull(actual);
        assertEquals(jersey, actual);
        assertEquals(jerseyFileDAO.getJersey(actual.getId()), jersey);
    }

    @Test
    public void testCreateJerseyAlreadyExists() throws IOException {
        Jersey actual = assertDoesNotThrow(() ->
            jerseyFileDAO.createJersey(jerseys[0]), "Unexpected exception thrown");

        assertNull(actual);
    }

}
