package com.estore.api.estoreapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.estore.api.estoreapi.model.Jersey;

/**
 * Implements the functionality for JSON file-based peristance for Jeresys
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of this
 * class and injects the instance into other classes as needed
 * 
 * @author Team Lightning
 */
@Component
public class JerseyFileDAO implements JerseyDAO {
    Map<Integer,Jersey> jerseys;   // Provides a local cache of the jersey objects
                                // so that we don't need to read from the file
                                // each time
    private ObjectMapper objectMapper;  // Provides conversion between jersey
                                        // objects and JSON text format written
                                        // to the file
    private static int nextId;  // The next Id to assign to a new jersey
    private String filename;    // Filename to read from and write to

    /**
     * Creates a Jersey File Data Access Object
     * 
     * @param filename Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object serialization and deserialization
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    public JerseyFileDAO(@Value("${jerseys.file}") String filename, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        this.load();  // load the jerseys from the file
    }

    /**
     * Generates the next id for a new {@linkplain Jersey jersey}
     * 
     * @return The next id
     */
    private synchronized static int nextId() {
        int id = nextId;
        ++nextId;
        return id;
    }

    /**
     * Generates an array of {@linkplain Jersey jerseys} from the tree map
     * 
     * @return  The array of {@link Jesey jerseys}, may be empty
     */
    private Jersey[] getJerseysArray() {
        return getJerseysArray(null);
    }

    /**
     * Generates an array of {@linkplain Jersey jerseys} from the tree map for any
     * {@linkplain Jersey jerseys} that contains the text specified by containsText
     * <br>
     * If containsText is null, the array contains all of the {@linkplain Jersey jerseys}
     * in the tree map
     * 
     * @return  The array of {@linkplain Jersey jerseys}, may be empty
     */
    private Jersey[] getJerseysArray(String containsText) { // if containsText == null, no filter
        ArrayList<Jersey> jerseyArrayList = new ArrayList<>();

        for (Jersey jersey : jerseys.values()) {
            if (containsText == null || jersey.getName().contains(containsText)) {
                jerseyArrayList.add(jersey);
            }
        }

        Jersey[] jerseyArray = new Jersey[jerseyArrayList.size()];
        jerseyArrayList.toArray(jerseyArray);
        return jerseyArray;
    }
    
    /**
     * Loads {@linkplain Jersey jerseys} from the JSON file into the map
     * <br>
     * Also sets next id to one more than the greatest id found in the file
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    private void load() throws IOException {
        this.jerseys = new TreeMap<>();
        JerseyFileDAO.nextId = 0;
        
        Jersey[] jerseys = this.objectMapper.readValue(new File(this.filename), Jersey[].class);
        for (Jersey jersey : jerseys) {

            // Jersey number works as primary key.
            int id = jersey.getId();

            this.jerseys.put(id, jersey);

            if (id > JerseyFileDAO.nextId)
                JerseyFileDAO.nextId = id;
        }

        ++nextId;
    }

    /**
     * Saves the {@linkplain Jersey jerseys} from the map into the file as an array of JSON objects
     * 
     * @return true if the {@link Jersey jerseys} were written successfully
     * 
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        Jersey[] jerseyArray = getJerseysArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
         
        objectMapper.writeValue(new File(filename),jerseyArray);
        return true;
    }

    @Override
    public Jersey[] getJerseys() {
        synchronized(jerseys){
            return getJerseysArray();
        }
    }

    @Override
    public Jersey[] findJerseys(String containsText) {
        return this.getJerseysArray(containsText);
    }

    @Override
    public Jersey getJersey(int id) {
        synchronized(jerseys) {
            if (jerseys.containsKey(id))
                return jerseys.get(id);
            else
                return null;
        }
    }

    /**
     * Given a Jersey with all the same attributes does not already exist,
     * creates a jersey and puts the jersey into the inventory 
     * 
     * @param jersey the jersey that will be created and added to the inventory 
     * 
     * @return the created jersey that was added to the inventory
     */
    @Override
    public Jersey createJersey(Jersey jersey) throws IOException {
        synchronized(jerseys) {
            if (jerseys.containsValue(jersey))
                return null;

            Jersey newJersey = new Jersey(nextId(),jersey.getNumber(),jersey.getName(),jersey.getPrice(),jersey.getSize(),jersey.getColor(),jersey.getQuantity());
                jerseys.put(newJersey.getId(),newJersey);
                save(); // may throw an IOException
                return newJersey;
        }
    }

    @Override
    public Jersey updateJersey(Jersey jersey) throws IOException {
        synchronized(jerseys) {
            if (!jerseys.containsKey(jersey.getId()) || jerseys.containsValue(jersey))
                return null;

            jerseys.put(jersey.getId(), jersey);
            this.save();
            return jersey;
        }
    }

    @Override
    public boolean deleteJersey(int id) throws IOException {
        synchronized(jerseys) {
            if (jerseys.containsKey(id)) {
                jerseys.remove(id);
                return save();
            }
            else
                return false;
        }
    }

}
