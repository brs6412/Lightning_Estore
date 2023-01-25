package com.estore.api.estoreapi.controller;

import com.estore.api.estoreapi.model.Jersey;
import com.estore.api.estoreapi.model.Jersey.Color;
import com.estore.api.estoreapi.model.Jersey.Size;
import com.estore.api.estoreapi.persistence.CartDAO;
import com.estore.api.estoreapi.persistence.JerseyDAO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Test the Jersey Controller class
 */
@Tag("Controller-tier")
public class JerseyControllerTest {
    private JerseyController jerseyController;
    private JerseyDAO mockJerseyDAO;
    private CartDAO mockCartDAO;

    /**
     * Before each test, create a new JerseyController object and inject
     * a mock JerseyDAO
     */
    @BeforeEach
    void setupJerseyController() {
        mockJerseyDAO = mock(JerseyDAO.class);
        mockCartDAO = mock(CartDAO.class);
        jerseyController = new JerseyController(mockJerseyDAO, mockCartDAO);
        when(mockCartDAO.removeJerseyFromAllCarts(anyInt())).thenReturn(true);
    }

    @Test
    void testCreateJerseyFailed() throws IOException {
        // Setup
        Jersey jersey = new Jersey(0, 1, "name", 1, Size.SMALL, Color.BLACK, 1);

        when(mockJerseyDAO.createJersey(jersey)).thenReturn(null);

        // Invoke
        ResponseEntity<Jersey> response = jerseyController.createJersey(jersey);

        // Analyze
        assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
    }

    /*
     * @author Andrew Apollo
     */
    @Test
    void testGetJersey() {
        // setup
        Jersey jeresy = new Jersey(0, 1, "name", 1, Size.SMALL, Color.BLACK, 1);
        when(mockJerseyDAO.getJersey(jeresy.getNumber())).thenReturn(jeresy);
        
        // Invoke
        ResponseEntity<Jersey> response = jerseyController.getJersey(jeresy.getNumber());
        
        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(jeresy,response.getBody());
    }   

    /*
     * @author Andrew Apollo
     */
    @Test
    void testGetJerseyNotFound() {
        // Setup
        int jerseyId = 42;
        when(mockJerseyDAO.getJersey(jerseyId)).thenReturn(null);
        
        // Invoke
        ResponseEntity<Jersey> response = jerseyController.getJersey(jerseyId);
        
        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    /*
     * @author Ben Sippel
     */
    @Test
    void testSearchJerseys() {
        // setup
        Jersey[] jerseys = new Jersey[] {
            new Jersey(0, 1, "Name 1", 1, Size.SMALL, Color.BLACK, 1),
            new Jersey(0, 2, "Name 2", 1, Size.SMALL, Color.BLACK, 1),
        };

        when(mockJerseyDAO.findJerseys("Name")).thenReturn(jerseys);
        
        // Invoke
        ResponseEntity<Jersey[]> response = jerseyController.searchJerseys("Name");
        
        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(jerseys, response.getBody());
    }   

    /*
     * @author Ben Sippel
     */
    @Test
    void testSearchJerseysNotFound() {
        // setup
        Jersey[] jerseys = new Jersey[] {};

        when(mockJerseyDAO.findJerseys("Greenscreen")).thenReturn(jerseys);
        
        // Invoke
        ResponseEntity<Jersey[]> response = jerseyController.searchJerseys("Greenscreen");
        
        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(0, response.getBody().length);
    }

    /*
     * @author Jessica Ahls
     */
    @Test
    void testUpdateJersey() throws IOException { // updateJersey may throw IOException
        // Setup
        Jersey jersey = new Jersey(0, 1, "Men's Dr. Disney West Jersey (Medium Size)",25,Size.LARGE,Color.BLUE,1);

        when(mockJerseyDAO.updateJersey(jersey)).thenReturn(jersey);
        ResponseEntity<Jersey> response = jerseyController.updateJersey(jersey);

        jersey.setName("Jersey + Craftsman M105 140cc 21-Inch 3-in-1 Gas Powered Push Lawn Mower with Bagger Combo");

        // Invoke
        response = jerseyController.updateJersey(jersey);

        // Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(jersey, response.getBody());
    }

    /*
     * @author Jessica Ahls
     */
    @Test
    void testUpdateNonExistentJersey() throws IOException {
        // Setup
        Jersey jersey = new Jersey(0, 1, "Men's Dr. Disney West Jersey (Medium Size)",25,Size.LARGE,Color.BLUE,1);

        when(mockJerseyDAO.updateJersey(jersey)).thenReturn(null);

        // Invoke
        ResponseEntity<Jersey> response = jerseyController.updateJersey(jersey);

        // Analyze
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void testUpdateJerseyHandleException() throws IOException {  
        // Setup
        Jersey jersey = new Jersey(0, 1, "test",25,Size.LARGE,Color.BLUE,1);

        doThrow(new IOException()).when(mockJerseyDAO).updateJersey(jersey);
        
        // Invoke
        ResponseEntity<Jersey> response = jerseyController.updateJersey(jersey);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    /*
     * @author Aidan Ruiz 
     */
    @Test
    void testGetJerseysWithProducts() {
        // Setup
        Jersey[] jerseys = new Jersey[2];
        jerseys[0] = new Jersey(0, 99,"Jersey 99", 3, Size.SMALL, Color.BLUE,1);
        jerseys[1] = new Jersey(1, 100,"Jersey 100", 4, Size.SMALL, Color.BLUE,1);

        when(mockJerseyDAO.getJerseys()).thenReturn(jerseys);

        // Invoke
        ResponseEntity<Jersey[]> response = jerseyController.getJerseys();

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(jerseys,response.getBody());
    }

    /*
     * @author Aidan Ruiz 
     */
    @Test
    void testGetJerseysWithNoProducts() {
        // Setup
        Jersey[] jerseys = new Jersey[0];
        
        when(mockJerseyDAO.getJerseys()).thenReturn(jerseys);

        // Invoke
        ResponseEntity<Jersey[]> response = jerseyController.getJerseys();

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(jerseys,response.getBody());
    }

    @Test
    void testCreateJersey() throws IOException {  
        // Setup
        Jersey jersey = new Jersey(0, 1, "test",25,Size.LARGE,Color.BLUE,1);

        when(mockJerseyDAO.createJersey(jersey)).thenReturn(jersey);

        // Invoke
        ResponseEntity<Jersey> response = jerseyController.createJersey(jersey);

        // Analyze
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(jersey,response.getBody());
    }

    @Test
    void testCreateJerseyDuplicate() throws IOException {  
        // Setup
        Jersey jersey1 = new Jersey(0, 1, "test",25,Size.LARGE,Color.BLUE,1);
        Jersey jersey2 = new Jersey(10000, 1, "test",25,Size.LARGE,Color.BLUE,1);

        when(mockJerseyDAO.createJersey(jersey1)).thenReturn(jersey1);
        when(mockJerseyDAO.createJersey(jersey2)).thenReturn(null);

        // Invoke
        jerseyController.createJersey(jersey1);
        ResponseEntity<Jersey> response = jerseyController.createJersey(jersey2);

        // Analyze
        assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
    }

    @Test
    void testCreateJerseyHandleException() throws IOException {  
        // Setup
        Jersey jersey = new Jersey(0, 1, "test",25,Size.LARGE,Color.BLUE,1);

        doThrow(new IOException()).when(mockJerseyDAO).createJersey(jersey);

        // Invoke
        ResponseEntity<Jersey> response = jerseyController.createJersey(jersey);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    void testDeleteJersey() throws IOException {  
        // Setup
        int jerseyId = 1;

        when(mockJerseyDAO.deleteJersey(jerseyId)).thenReturn(true);
        
        // Invoke
        ResponseEntity<Jersey> response = jerseyController.deleteJersey(jerseyId);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    void testDeleteJerseyNotFound() throws IOException {  
        // Setup
        int jerseyId = 1;

        when(mockJerseyDAO.deleteJersey(jerseyId)).thenReturn(false);
        
        // Invoke
        ResponseEntity<Jersey> response = jerseyController.deleteJersey(jerseyId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    void testDeleteJerseyHandleException() throws IOException {  
        // Setup
        int jerseyId = 1;

        doThrow(new IOException()).when(mockJerseyDAO).deleteJersey(jerseyId);
        
        // Invoke
        ResponseEntity<Jersey> response = jerseyController.deleteJersey(jerseyId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

}