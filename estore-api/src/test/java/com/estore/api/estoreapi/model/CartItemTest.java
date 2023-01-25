package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * The unit test suite for the CartItem class
 */
@Tag("Model-tier")
public class CartItemTest {
    
    // Cart Items are equal if their IDs are the same
    @Test
    public void testEqualsTrue() {
        // Setup
        int quantity = 3;
        CartItem item1 = new CartItem(1, quantity);
        CartItem item2 = new CartItem(1, quantity);
        boolean expected = true;

        // Invoke
        boolean actual = item1.equals(item2);

        // Analyze
        assertEquals(expected, actual);
    }

    @Test
    public void testEqualsFalse() {
        // Setup
        int quantity = 3;
        CartItem item1 = new CartItem(1, quantity);
        CartItem item2 = new CartItem(2, quantity);
        boolean expected = false;

        // Invoke
        boolean actual = item1.equals(item2);

        // Analyze
        assertEquals(expected, actual);
    }

    @Test
    public void testSetQuantity() {
        // Setup
        int quantity = 3;
        CartItem item = new CartItem(1, quantity);
        int expected = 44;

        // Invoke
        item.setQuantity(expected);
        int actual = item.getQuantity();

        // Analyze
        assertEquals(expected, actual);
    }

    @Test
    public void testEqualsSameObject() {
        // Setup
        int quantity = 3;
        CartItem item1 = new CartItem(1, quantity);
        CartItem item2 = item1;
        boolean expected = true;

        // Invoke
        boolean actual = item1.equals(item2);

        // Analyze
        assertEquals(expected, actual);
    }

    @Test
    public void testEqualsSameId() {
        // Setup
        int quantity = 3;
        CartItem item1 = new CartItem(1, quantity);
        CartItem item2 = new CartItem(1, quantity);
        boolean expected = true;

        // Invoke
        boolean actual = item1.equals(item2);

        // Analyze
        assertEquals(expected, actual);
    }

    @Test
    public void testEqualsDiffId() {
        // Setup
        int quantity = 3;
        CartItem item1 = new CartItem(1, quantity);
        CartItem item2 = new CartItem(5, quantity);
        boolean expected = false;

        // Invoke
        boolean actual = item1.equals(item2);

        // Analyze
        assertEquals(expected, actual);
    }

    @Test
    public void testEqualsDiffType() {
        // Setup
        int quantity = 3;
        CartItem item1 = new CartItem(1, quantity);
        Jersey item2 = new Jersey(0, 1, "test", 14, Jersey.Size.LARGE, Jersey.Color.BLUE, 1);
        boolean expected = false;

        // Invoke
        boolean actual = item1.equals(item2);

        // Analyze
        assertEquals(expected, actual);
    }

    @Test
    public void testHashCode() {
        // Setup
        int quantity = 3;
        int id = 12;
        CartItem item = new CartItem(12, quantity);
        int expected =  id;

        // Invoke
        int actual = item.hashCode();

        // Analyze
        assertEquals(expected, actual);
    }

}
