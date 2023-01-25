package com.estore.api.estoreapi.payload;

import com.estore.api.estoreapi.model.Jersey;

/**
 * A variant of the CartItem entity that contains the populated
 * Jersey object when responding to shopping cart requests.
 */
public class CartItemInstance {
    private Jersey jersey;
    private int quantity;

    public CartItemInstance(Jersey jersey, int quantity) {
        this.jersey = jersey;
        this.quantity = quantity;
    }

    public int getQuantity() { return this.quantity; }
    public Jersey getJersey() { return this.jersey; }
}
