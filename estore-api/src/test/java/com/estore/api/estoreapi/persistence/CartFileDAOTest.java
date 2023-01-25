package com.estore.api.estoreapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import com.estore.api.estoreapi.model.CartItem;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CartFileDAOTest {
    CartFileDAO cartFileDAO;
    HashMap<String, ArrayList<CartItem>> carts;
    ObjectMapper mapper;

    @BeforeEach
    public void setupCartFileDAO() throws IOException {
        mapper = mock(ObjectMapper.class);

        carts = new HashMap<>();

        ArrayList<CartItem> cart = new ArrayList<>();
        cart.add(new CartItem(1, 1));
        carts.put("uno", cart);

        
        // Mocking type references is gross
        when(mapper.readValue(eq(new File("carts.json")), 
            ArgumentMatchers.<TypeReference<HashMap<String, ArrayList<CartItem>>>>any())
        ).thenReturn(carts);
        
        cartFileDAO = new CartFileDAO("carts.json", mapper);
    }
    
    @Test
    public void testCreateCart() {
        assertTrue(cartFileDAO.createCart("dos"));
    }

    @Test
    public void testCreateCartExists() {
        assertFalse(cartFileDAO.createCart("uno"));
    }

    @Test
    public void testGetCart() throws IOException {
        CartItem[] items = cartFileDAO.getCart("uno");

        assertNotNull(items);
        assertEquals(1, items.length);
    }

    @Test
    public void testGetCartNotExists() throws IOException {
        CartItem[] items = cartFileDAO.getCart("dos");
        assertNull(items);
    }

    @Test
    public void testAddToCart() throws IOException {
        CartItem item = new CartItem(0xDEADBEEF, 1);

        assertTrue(cartFileDAO.addToCart("uno", item));

        assertEquals(2, this.carts.get("uno").size());
    }

    @Test
    public void testAddToCartExists() throws IOException {
        CartItem item = this.carts.get("uno").get(0);
        int oldQuantity = item.getQuantity();

        assertTrue(cartFileDAO.addToCart("uno", item));

        assertEquals(oldQuantity + 1, item.getQuantity());
        
    }

    @Test
    public void testClearCart() {
        assertTrue(cartFileDAO.clearCart("uno"));
        assertEquals(0, this.carts.get("uno").size());
    }

    @Test
    public void testSetQuantity() throws IOException {
        CartItem item = new CartItem(1, 5);
        assertTrue(cartFileDAO.setQuantity("uno", item));
        assertEquals(5, this.carts.get("uno").get(0).getQuantity());
    }

    @Test
    public void testSetQuantityInvalid() throws IOException {
        CartItem item = new CartItem(1, -1);
        assertFalse(cartFileDAO.setQuantity("uno", item));
    }

    @Test
    public void testSetQuantityNotExists() throws IOException {
        CartItem item = new CartItem(0xDEADBEEF, 1);
        assertFalse(cartFileDAO.setQuantity("uno", item));
    }

    @Test
    public void testRemoveFromCart() throws IOException {
        CartItem item = this.carts.get("uno").get(0);
        assertTrue(cartFileDAO.removeItemFromCart("uno", item));
        assertEquals(0, this.carts.get("uno").size());
    }

    @Test
    public void testRemoveFromCartNotExists() throws IOException {
        CartItem item = new CartItem(0xDEADBEEF, 1);
        assertFalse(cartFileDAO.removeItemFromCart("uno", item));
        assertEquals(1, this.carts.get("uno").size());
    }

    @Test
    public void testClearJerseysById() {
        cartFileDAO.removeJerseyFromAllCarts(1);
        assertEquals(0, this.carts.get("uno").size());
    }
}
