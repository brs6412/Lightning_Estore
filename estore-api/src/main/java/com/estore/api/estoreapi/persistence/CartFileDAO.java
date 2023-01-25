package com.estore.api.estoreapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.estore.api.estoreapi.model.CartItem;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Implements the functionality for JSON file-based persistence for Shopping
 * Carts.
 */
@Component
public class CartFileDAO implements CartDAO {
    /**
     * Local cache of shopping carts via username.
     */
    private Map<String, ArrayList<CartItem>> carts;

    /**
     * Provides conversion between User object and JSON string data.
     */
    private ObjectMapper mapper;

    /**
     * File to read/write JSON data to.
     */
    private String filename;

    public CartFileDAO(@Value("${carts.file}") String filename, ObjectMapper mapper) throws IOException {
        this.filename = filename;
        this.mapper = mapper;
        this.load();
    }

    /**
     * Loads {@linkplain CartItem cart items} from the JSON database into the map.
     * 
     * @throws IOException If underlying storage cannot be accessed
     */
    private void load() throws IOException {
        TypeReference<HashMap<String, ArrayList<CartItem>>> typeReference = new TypeReference<HashMap<String, ArrayList<CartItem>>>() {
        };
        this.carts = mapper.readValue(new File(this.filename), typeReference);
    }

    /**
     * Saves the {@linkplain CartItem cart items} to JSON database.
     * 
     * @return Whether or not the save operation was successful.
     */
    private boolean save() {
        try {
            this.mapper.writeValue(new File(this.filename), this.carts);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean createCart(String username) {
        synchronized (this.carts) {
            if (this.carts.containsKey(username))
                return false;
            this.carts.put(username, new ArrayList<>());
            return this.save();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CartItem[] getCart(String username) {
        synchronized (this.carts) {
            ArrayList<CartItem> items = this.carts.get(username);
            if (items == null)
                return null;
            return items.toArray(CartItem[]::new);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addToCart(String username, CartItem item) {
        synchronized (this.carts) {
            ArrayList<CartItem> items = this.carts.get(username);

            // Increase quantity if it's already in cart.
            int index = items.indexOf(item);
            if (index != -1) {
                CartItem persistentItem = items.get(index);
                persistentItem.setQuantity(persistentItem.getQuantity() + item.getQuantity());
                return this.save();
            }

            items.add(item);
            return this.save();
        }
    }

    /**
     * {@inheritDoc}}
     */
    @Override
    public boolean clearCart(String username) {
        synchronized (this.carts) {
            this.carts.replace(username, new ArrayList<>());
            return this.save();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean setQuantity(String username, CartItem item) {
        synchronized (this.carts) {
            int quantity = item.getQuantity();
            if (quantity <= 0)
                return false;
            ArrayList<CartItem> items = this.carts.get(username);
            int index = items.indexOf(item);
            if (index != -1) {
                CartItem persistentItem = items.get(index);
                persistentItem.setQuantity(quantity);
                return this.save();
            }
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeJerseyFromAllCarts(int jerseyId) {
        CartItem item = new CartItem(jerseyId, 1);
        synchronized(this.carts) {
            for (ArrayList<CartItem> carts : this.carts.values()) {
                if (carts.contains(item))
                    carts.remove(item);
            }
            return this.save();
        }
    }

    /**
     * {@inheritDoc}}
     */
    @Override
    public boolean removeItemFromCart(String username, CartItem item) {
        synchronized (this.carts) {
            ArrayList<CartItem> items = this.carts.get(username);

            int index = items.indexOf(item);
            if (index != -1) {
                items.remove(item);
                return this.save();
            }
            
            return false;
        }
    }
}
