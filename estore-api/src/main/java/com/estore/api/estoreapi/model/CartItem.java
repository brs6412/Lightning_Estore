package com.estore.api.estoreapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CartItem {
    /**
     * The jersey that's currently in the cart.
     */
    private int jersey;

    /**
     * Number of this item already in the cart.
     */
    private int quantity;

    public CartItem(@JsonProperty("jersey") int jersey, @JsonProperty("quantity") int quantity) {
        this.jersey = jersey;
        this.quantity = quantity;
    }

    public int getQuantity() { return this.quantity; }
    public int getJersey() { return this.jersey; }

    public void setQuantity(int quantity) { 
        
        if(this.quantity >= 0){
            this.quantity = quantity;
        }
        }

    @Override public boolean equals(Object other) {
        if (other == this) return true;
        if (!(other instanceof CartItem)) return false;
        CartItem otherItem = (CartItem) other;
        return otherItem.jersey == this.jersey;
    }

    @Override public int hashCode() { return this.jersey; }
}
