package com.estore.api.estoreapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.estore.api.estoreapi.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Implements the functionality for JSON file-based persistence for Users.
 */
@Component
public class UserFileDAO implements UserDAO {
    /**
     * Local cache of User objects via ID.
     */
    private Map<Integer, User> userIdMap;
    
    /**
     * Local cache of User objects via username.
     */
    private Map<String, User> userNameMap;

    /**
     * Provides conversion between User object and JSON string data.
     */
    private ObjectMapper mapper;

    /**
     * The next ID to assign to a newly created user.
     */
    private static int nextId;

    /**
     * File to read/write JSON data to.
     */
    private String filename;

    public UserFileDAO(@Value("${users.file}") String filename, ObjectMapper mapper) throws IOException {
        this.filename = filename;
        this.mapper = mapper;
        this.load();
    }

    /**
     * Loads {@linkplain User users} from the JSON database into the map.
     * @throws IOException If underlying storage cannot be accessed
     */
    private void load() throws IOException {
        this.userIdMap = new TreeMap<>();
        this.userNameMap = new HashMap<>();
        
        UserFileDAO.nextId = 0;

        User[] users = this.mapper.readValue(new File(this.filename), User[].class);
        for (User user : users) {
            int id = user.getId();

            this.userIdMap.put(id, user);
            this.userNameMap.put(user.getUsername(), user);

            if (id > UserFileDAO.nextId)
                UserFileDAO.nextId = id;
        }

        ++UserFileDAO.nextId;
    }

    /**
     * Saves the {@linkplain User users} to JSON database.
     * @return Whether or not the save operation was successful.
     */
    private boolean save() {
        Collection<User> users = this.userIdMap.values();
        try { this.mapper.writeValue(new File(this.filename), users); } 
        catch (IOException ex) { return false; }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override public User getUser(int id) {
        synchronized(this.userIdMap) {
            return this.userIdMap.get(id);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override public User getUser(String username) {
        synchronized(this.userIdMap) {
            return this.userNameMap.get(username);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override public User createUser(String username, String password) {
        synchronized(this.userIdMap) {
            if (this.userNameMap.containsKey(username))
                return null;
            
            User user = new User(
                UserFileDAO.nextId++, 
                username,
                password
            );

            this.userIdMap.put(user.getId(), user);
            this.userNameMap.put(username, user);
            
            this.save();

            return user;
        }
    }
}
