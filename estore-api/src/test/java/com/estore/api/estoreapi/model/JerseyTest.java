package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.estore.api.estoreapi.model.Jersey.Size;
import com.estore.api.estoreapi.model.Jersey.Color;

/**
 * The unit test suite for the Jersey class
 */
@Tag("Model-tier")
public class JerseyTest {

    @Test
    public void testAddJerseysToSet() {
        // Setup
        Jersey jersey1 = new Jersey(1, 99, "name", 13.78, Size.LARGE, Color.BLACK, 1);
        Jersey jersey2 = new Jersey(2, 99, "name", 13.78, Size.LARGE, Color.BLACK, 1);
        Jersey jersey3 = new Jersey(3, 99, "name", 13.78, Size.LARGE, Color.BLACK, 1);
        Jersey jersey4 = new Jersey(4, 77, "name", 13.78, Size.LARGE, Color.BLACK, 1);
        Set<Jersey> jerseys = new HashSet<>();
        Collections.addAll(jerseys, jersey1, jersey2, jersey3, jersey4);
        int expected = 2;

        // Invoke
        int actual = jerseys.size();

        // Analyze
        assertEquals(expected, actual);
        assertTrue(jerseys.contains(jersey1));
        assertTrue(jerseys.contains(jersey2));
        assertTrue(jerseys.contains(jersey3));
        assertTrue(jerseys.contains(jersey4));
    }

    @Test
    public void testAddJerseysToMap() {
        // Setup
        String NAME = "Name";
        int NUMBER = 22;
        double PRICE = 25;
        Size SIZE = Size.MEDIUM;
        Color COLOR = Color.BLUE;
        int QUANTITY = 1;

        Jersey[] j = {new Jersey(1, NUMBER, NAME, PRICE, SIZE, COLOR, QUANTITY),
            new Jersey(2, NUMBER, NAME, PRICE, SIZE, COLOR, QUANTITY),
            new Jersey(3, NUMBER, NAME, PRICE, SIZE, Color.BLACK, QUANTITY)
        };

        Map<Integer,Jersey> jerseys; jerseys = new TreeMap<>();
        
        for (Jersey jersey : j) {
            if (!jerseys.containsValue(jersey)){
                int id = jersey.getId();
                jerseys.put(id, jersey);
            }
        }

        int expected = 2;

        // Invoke
        int actual = jerseys.size();

        // Analyze
        assertEquals(expected, actual);
        assertTrue(jerseys.containsValue(j[0]));
        assertTrue(jerseys.containsValue(j[1]));
        assertTrue(jerseys.containsValue(j[2]));
        
        assertTrue(jerseys.containsKey(1));
        assertTrue(!jerseys.containsKey(2));
        assertTrue(jerseys.containsKey(3));
    }

    @Test
    public void testEqualsTrue() {
        // Setup
        int id1 = 0;
        int id2 = 1;
        int num = 1;
        String name = "Name";
        double price = 25;
        Size size = Size.MEDIUM;
        Color color = Color.BLUE;
        int quantity = 1;
        Boolean expected = true;
        Jersey jersey1 = new Jersey(id1, num, name, price, size, color, quantity);
        Jersey jersey2 = new Jersey(id2, num, name, price, size, color, quantity);

        // Invoke
        Boolean actual = jersey1.equals(jersey2);

        // Analyze
        assertEquals(expected, actual);
    }

    @Test
    public void testEqualsSameInstance() {
        Jersey jersey = new Jersey(1, 1, "Dr. Disney West", 150.0, Size.MEDIUM, Color.BLACK, 1);
        assertTrue(jersey.equals(jersey));
    }

    @Test
    public void testEqualFalseDifferentPrice() {
        Jersey l = new Jersey(1, 1, "Dr. Disney West", 150.0, Size.MEDIUM, Color.BLACK, 1);
        Jersey r = new Jersey(1, 1, "Dr. Disney West", 160.0, Size.MEDIUM, Color.BLACK, 1);
        assertFalse(l.equals(r));
    }

    @Test
    public void testEqualFalseDifferentSize() {
        Jersey l = new Jersey(1, 1, "Dr. Disney West", 150.0, Size.MEDIUM, Color.BLACK, 1);
        Jersey r = new Jersey(1, 1, "Dr. Disney West", 150.0, Size.LARGE, Color.BLACK, 1);
        assertFalse(l.equals(r));
    }

    @Test
    public void testEqualFalseDifferentColor() {
        Jersey l = new Jersey(1, 1, "Dr. Disney West", 150.0, Size.MEDIUM, Color.BLACK, 1);
        Jersey r = new Jersey(1, 1, "Dr. Disney West", 150.0, Size.MEDIUM, Color.WHITE, 1);
        assertFalse(l.equals(r));
    }

    @Test
    public void testEqualsDifferentType() {
        Jersey jersey = new Jersey(1, 1, "Dr. Disney West", 150.0, Size.MEDIUM, Color.BLACK, 1);
        assertFalse(jersey.equals("A Lone Horse in the Distance"));
    }

    @Test
    public void testEqualsDifferentQuantity() {
        // Jerseys with different quantities available in inventory are still technically the same,
        // so long as their other attributes match.
        
        Jersey l = new Jersey(1, 1, "Dr. Disney West", 150.0, Size.MEDIUM, Color.WHITE, 0xDEAD);
        Jersey r = new Jersey(1, 1, "Dr. Disney West", 150.0, Size.MEDIUM, Color.WHITE, 0xBEEF);

        assertTrue(l.equals(r));
    }

    @Test
    public void testEqualsFalse() {
        // Setup
        int id1 = 0;
        int id2 = 1;
        int num = 1;
        String name1 = "Name";
        String name2 = "Nameeeee";
        double price = 25;
        Size size = Size.MEDIUM;
        Color color = Color.BLUE;
        int quantity = 1;
        Boolean expected = false;
        Jersey jersey1 = new Jersey(id1, num, name1, price, size, color, quantity);
        Jersey jersey2 = new Jersey(id2, num, name2, price, size, color, quantity);

        // Invoke
        Boolean actual = jersey1.equals(jersey2);

        // Analyze
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCreateJersey() {
        // Setup
        int expectedId = 0;
        int expectedNum = 1;
        String expectedName = "Name";
        double expectedPrice = 25;
        Size expectedSize = Size.MEDIUM;
        Color expectedColor = Color.BLUE;
        int expectedQuantity = 1;

        // Invoke
        Jersey jersey = new Jersey(expectedId, expectedNum, expectedName, expectedPrice, expectedSize, expectedColor, expectedQuantity);

        // Analyze
        assertEquals(expectedId, jersey.getId());
        assertEquals(expectedNum, jersey.getNumber());
        assertEquals(expectedName, jersey.getName());
        assertEquals(expectedPrice, jersey.getPrice());
        assertEquals(expectedSize, jersey.getSize());
        assertEquals(expectedColor, jersey.getColor());
        assertEquals(expectedQuantity, jersey.getQuantity());
    }

    @Test
    public void testSetName() {
        // Setup
        int id = 0;
        int num = 1;
        String name = "Name";
        double price = 25;
        Size size = Size.MEDIUM;
        Color color = Color.BLUE;
        int quantity = 1;

        Jersey jersey = new Jersey(id, num, name, price, size, color, quantity);
        String expectedName = "New Name";

        // Invoke
        jersey.setName(expectedName);

        // Analyze
        assertEquals(expectedName, jersey.getName());
    }

    @Test
    public void testSetQuantity() {
        Jersey jersey = new Jersey(1, 1, "Dr. Disney West", 150.0, Size.MEDIUM, Color.BLACK, 0xDEAD);
        
        jersey.setQuantity(0xBEEF);

        assertEquals(0xBEEF, jersey.getQuantity());
    }

    @Test
    public void testToString() {
        // Setup
        int id = 0;
        int num = 1;
        String name = "Name";
        double price = 25;
        Size size = Size.MEDIUM;
        Color color = Color.BLUE;
        int quantity = 1;

        Jersey jersey = new Jersey(id, num, name, price, size, color, quantity);
        String expectedString = String.format(Jersey.STRING_FORMAT, id, num, name, price, size, color);

        // Invoke
        String actualString = jersey.toString();
        System.out.println(expectedString);
        System.out.println(actualString);

        // Analyze
        assertEquals(expectedString, actualString);

    }   

}
