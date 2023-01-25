package com.estore.api.estoreapi.controller;

import com.estore.api.estoreapi.model.CartItem;
import com.estore.api.estoreapi.model.Jersey;
import com.estore.api.estoreapi.model.User;
import com.estore.api.estoreapi.model.Jersey.Color;
import com.estore.api.estoreapi.model.Jersey.Size;
import com.estore.api.estoreapi.payload.CartItemInstance;
import com.estore.api.estoreapi.payload.CartResponseData;
import com.estore.api.estoreapi.persistence.CartDAO;
import com.estore.api.estoreapi.persistence.JerseyDAO;
import com.estore.api.estoreapi.security.service.UserDetailsInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Test the Cart Controller class
 */
@Tag("Controller-tier")
public class CartControllerTest {
    private CartController controller;
    private CartDAO mockCartDAO;
    private JerseyDAO mockJerseyDAO;
    private UserDetailsInstance details;

    @BeforeEach
    void setupMockData() throws IOException {
        this.mockCartDAO = mock(CartDAO.class);
        this.mockJerseyDAO = mock(JerseyDAO.class);

        // Fake user
        this.details = UserDetailsInstance.build(new User(1, "user", "password"));

        // Pre-fill some dummy jersey data for mappings
        when(this.mockJerseyDAO.getJersey(0)).thenReturn(new Jersey(0, 1, "Disney",1 ,Size.SMALL , Color.BLUE,1));
        when(this.mockJerseyDAO.getJersey(1)).thenReturn(new Jersey(1, 2, "Tanner",2, Size.SMALL , Color.BLUE,1));
        when(this.mockJerseyDAO.getJersey(2)).thenReturn(new Jersey(2, 3, "Alden",3, Size.SMALL , Color.BLUE,0));

        this.controller = new CartController(this.mockCartDAO, this.mockJerseyDAO);
    }

    @Test void testGetCartEmpty() {
        when(this.mockCartDAO.getCart(this.details.getUsername())).thenReturn(new CartItem[0]);

        ResponseEntity<CartResponseData> response = this.controller.getShoppingCart(this.details);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0.0, response.getBody().getTotal());
    }

    @Test void testGetCartWithItem() {
        when(this.mockCartDAO.getCart(this.details.getUsername())).thenReturn(new CartItem[] { new CartItem(0, 1) });

        ResponseEntity<CartResponseData> response = this.controller.getShoppingCart(this.details);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        CartResponseData data = response.getBody();
        assertEquals(data.getTotal(), 1.0);

        CartItemInstance[] items = data.getItems();

        assertEquals(1, items.length);
        assertEquals("Disney", items[0].getJersey().getName());
    }

    @Test void testAddItem() {
        CartItem item = new CartItem(1, 1);
        when(this.mockCartDAO.getCart(this.details.getUsername())).thenReturn(new CartItem[] { item });
        when(this.mockCartDAO.addToCart(this.details.getUsername(), item)).thenReturn(true);

        ResponseEntity<CartResponseData> response = this.controller.addItemToCart(item, this.details);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        CartResponseData data = response.getBody();
        assertEquals(data.getTotal(), 2.0);

        CartItemInstance[] items = data.getItems();

        assertEquals(1, items.length);
        assertEquals("Tanner", items[0].getJersey().getName());
    }

    @Test void testAddItemOutOfStock() {
        CartItem item = new CartItem(2, 1);

        ResponseEntity<CartResponseData> response = this.controller.addItemToCart(item, this.details);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test void testAddItemInternalError() {
        CartItem item = new CartItem(1, 1);
        when(this.mockCartDAO.getCart(this.details.getUsername())).thenReturn(new CartItem[] { item });
        when(this.mockCartDAO.addToCart(this.details.getUsername(), item)).thenReturn(false);

        ResponseEntity<CartResponseData> response = this.controller.addItemToCart(item, this.details);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test void testAddItemInvalidQuantity() {
        ResponseEntity<CartResponseData> response = this.controller.addItemToCart(
            new CartItem(0, 0),
            this.details
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test void testAddItemNotFound() {
        ResponseEntity<CartResponseData> response = this.controller.addItemToCart(
            new CartItem(123456, 1),
            this.details
        );
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test void testSetQuantityHandleExceptionBadRequest()  {
        ResponseEntity<?> response = this.controller.setCartItemQuantity(
            new CartItem(123456, -1),
            this.details
        );
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test void testSetQuantity() {
        CartItem item = new CartItem(1, 1);
        when(this.mockCartDAO.getCart(this.details.getUsername())).thenReturn(new CartItem[] { item });
        when(this.mockCartDAO.addToCart(this.details.getUsername(), item)).thenReturn(true);
        when(this.mockCartDAO.setQuantity(this.details.getUsername(), item)).thenReturn(true);

        ResponseEntity<?> response = this.controller.setCartItemQuantity(item, this.details);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test void testCheckoutCart(){
        CartItem item = new CartItem(1, 1);
        when(this.mockCartDAO.getCart(this.details.getUsername())).thenReturn(new CartItem[] { item });
        when(this.mockCartDAO.clearCart(this.details.getUsername())).thenReturn(true);

        ResponseEntity<CartResponseData> response = this.controller.checkoutCart(this.details);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test void testCheckoutCartError() {
        CartItem item = new CartItem(1, 1);
        when(this.mockCartDAO.getCart(this.details.getUsername())).thenReturn(new CartItem[] { item });
        when(this.mockCartDAO.clearCart(this.details.getUsername())).thenReturn(false);

        ResponseEntity<CartResponseData> response = this.controller.checkoutCart(this.details);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test void testRemoveFromCart() {
        CartItem item = new CartItem(1, 1);
        when(this.mockCartDAO.getCart(this.details.getUsername())).thenReturn(new CartItem[] { item });
        when(this.mockCartDAO.removeItemFromCart(this.details.getUsername(), item)).thenReturn(true);

        ResponseEntity<CartResponseData> response = this.controller.removeItemFromCart(item, this.details);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test void testRemoveFromCartFail() {
        CartItem item = new CartItem(1, 1);
        when(this.mockCartDAO.getCart(this.details.getUsername())).thenReturn(new CartItem[] {});
        when(this.mockCartDAO.removeItemFromCart(this.details.getUsername(), item)).thenReturn(false);

        ResponseEntity<CartResponseData> response = this.controller.removeItemFromCart(item, this.details);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}
