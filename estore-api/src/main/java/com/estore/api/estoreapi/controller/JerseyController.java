package com.estore.api.estoreapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.estore.api.estoreapi.model.Jersey;
import com.estore.api.estoreapi.persistence.CartDAO;
import com.estore.api.estoreapi.persistence.JerseyDAO;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles the REST API requests for the Jersey resource
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST API
 * method handler to the Spring framework
 * 
 * @author Team Lightning
 */

@RestController
@RequestMapping("jerseys")
public class JerseyController {
    private static final Logger LOG = Logger.getLogger(JerseyController.class.getName());
    private JerseyDAO jerseyDao;
    private CartDAO cartDao;

    /**
     * Creates a REST API controller to reponds to requests
     * 
     * @param jerseyDao The {@link JerseyDAO Jersey Data Access Object} to perform CRUD operations
     * @param cartDao The {@link CartDAO Cart Data Access Object} for cart related operations.
     * <br>
     * This dependency is injected by the Spring Framework
     */
    public JerseyController(JerseyDAO jerseyDao, CartDAO cartDao) {
        this.jerseyDao = jerseyDao;
        this.cartDao = cartDao;
    }

    /**
     * Responds to the GET request for a {@linkplain Jersey jersey} for the given id
     * 
     * @param id The id used to locate the {@link Jersey jersey}
     * 
     * @return ResponseEntity with {@link Jersey jersey} object and HTTP status of OK if found
     * ResponseEntity with HTTP status of NOT_FOUND if not found
     */
    @GetMapping("/{id}")
    protected ResponseEntity<Jersey> getJersey(@PathVariable int id) {
        LOG.info("GET /jerseys/" + id);
        Jersey jersey = jerseyDao.getJersey(id);
        if (jersey != null)
            return new ResponseEntity<Jersey>(jersey,HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Responds to the GET request for all {@linkplain Jersey jerseys}
     * 
     * @return ResponseEntity with array of {@link Jersey jersey} objects (may be empty) and
     * HTTP status of OK<br>
     */
    @GetMapping("")
    protected ResponseEntity<Jersey[]> getJerseys() {
        LOG.info("GET /jerseys");
        Jersey[] jerseys = jerseyDao.getJerseys();
        return new ResponseEntity<>(jerseys, HttpStatus.OK);

    }

    /**
     * Responds to the GET request for all {@linkplain Jersey jerseys} whose name contains
     * the text in name
     * 
     * @param name The name parameter which contains the text used to find the {@link Jersey jerseys}
     * 
     * @return ResponseEntity with array of {@link Jersey jersey} objects (may be empty) and
     * HTTP status of OK<br>
     * <p>
     * Example: Find all Jerseys that contain the text "ma"
     * GET http://localhost:8080/Jerseys/?name=ma
     */
    @GetMapping("/")
    protected ResponseEntity<Jersey[]> searchJerseys(@RequestParam String name) {
        LOG.info("GET /jerseys/?name="+name);
        Jersey[] jerseys = jerseyDao.findJerseys(name);
        return new ResponseEntity<>(jerseys, HttpStatus.OK);
    }

    /**
     * Creates a {@linkplain Jersey jersey} with the provided jersey object
     * 
     * @param jersey - The {@link Jersey jersey} to create
     * 
     * @return ResponseEntity with created {@link Jersey jersey} object and HTTP status of CREATED<br>
     * ResponseEntity with HTTP status of CONFLICT if {@link Jersey jersey} object already exists<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("")
    protected ResponseEntity<Jersey> createJersey(@RequestBody Jersey jersey){
        LOG.info("POST /jerseys " + jersey);
        try{
            Jersey createdJersey = jerseyDao.createJersey(jersey);
            if(createdJersey != null){
                return new ResponseEntity<>(createdJersey, HttpStatus.CREATED);
            }
            else{
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            
        }
       catch (Exception e) {
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }

    /**
     * Updates the {@linkplain Jersey jersey} with the provided {@linkplain Jersey jersey} object, if it exists
     * 
     * @param jersey The {@link Jersey jersey} to update
     * 
     * @return ResponseEntity with updated {@link Jersey jersey} object and HTTP status of OK if updated<br>
     * ResponseEntity with HTTP status of CONFLICT if the changes will make the Jersey equivalent to another
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("")
    protected ResponseEntity<Jersey> updateJersey(@RequestBody Jersey jersey) {
        LOG.info("PUT /jerseys " + jersey);
        try {
            jersey = jerseyDao.updateJersey(jersey);
            if (jersey == null)
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            return new ResponseEntity<>(jersey, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a {@linkplain Jersey jersey} with the given id
     * 
     * @param id The id of the {@link Jersey jersey} to deleted
     * 
     * @return ResponseEntity HTTP status of OK if deleted<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/{id}")
    protected ResponseEntity<Jersey> deleteJersey(@PathVariable int id) {
        LOG.info("DELETE /jerseys/" + id);
        try{
            if(jerseyDao.deleteJersey(id)){
                cartDao.removeJerseyFromAllCarts(id);
                return new ResponseEntity<Jersey>(HttpStatus.OK);
            }
            return new ResponseEntity<Jersey>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
