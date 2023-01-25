package com.estore.api.estoreapi.persistence;

import java.io.IOException;
import com.estore.api.estoreapi.model.Jersey;

/**
 * Defines the interface for Jersey object persistence
 * 
 * @author Team Lightning
 */
public interface JerseyDAO {

    Jersey[] getJerseys();

    /**
     * Finds all {@linkplain c} whose name contains the given text
     * 
     * @param containsText The text to match against
     * 
     * @return An array of {@link Jersey jeresy} whose nemes contains the given text, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    Jersey[] findJerseys(String containsText);

    /**
     * Retrieves a {@linkplain Jersey jeresy} with the given id
     * 
     * @param id The id of the {@link Jersey jeresy} to get
     * 
     * @return a {@link Jersey jeresy} object with the matching id
     * <br>
     * null if no {@link Jersey jeresy} with a matching id is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    Jersey getJersey(int id);

    /**
     * Creates and saves a {@linkplain Jersey jeresy} Given a Jersey with all
     * the same attributes does not already exist
     * 
     * @param jersey {@linkplain Jersey jeresy} object to be created and saved
     * <br>
     * The id of the jersey object is ignored and a new uniqe id is assigned
     *
     * @return new {@link Jersey jeresy} if successful, false otherwise 
     * 
     * @throws IOException if an issue with underlying storage
     */
    Jersey createJersey(Jersey jersey) throws IOException;

    /**
     * Updates and saves a {@linkplain Jersey jeresy}
     * 
     * @param {@link Jersey jeresy} object to be updated and saved
     * 
     * @return updated {@link Jersey jeresy} if successful, null if
     * {@link Jersey jeresy} could not be found
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    Jersey updateJersey(Jersey jersey) throws IOException;

    /**
     * Deletes a {@linkplain Jersey jeresy} with the given id
     * 
     * @param id The id of the {@link Jersey jeresy}
     * 
     * @return true if the {@link Jersey jeresy} was deleted
     * <br>
     * false if jersey with the given id does not exist
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean deleteJersey(int id) throws IOException;
}
