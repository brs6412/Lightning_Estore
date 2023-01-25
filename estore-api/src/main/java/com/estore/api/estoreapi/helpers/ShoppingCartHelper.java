package com.estore.api.estoreapi.helpers;

import com.estore.api.estoreapi.model.CartItem;
import com.estore.api.estoreapi.payload.CartItemInstance;
import com.estore.api.estoreapi.persistence.JerseyDAO;

/**
 * Utilities for aiding with shopping cart response data.
 */
public class ShoppingCartHelper {
    /**
     * Maps CartItem database objects to a more end-user friendly format, populating
     * the Jersey data.
     * @param dao JerseyDAO helper to search Jersey objects by ID
     * @param items A user's shopping cart.
     * @return Populated shopping cart.
     */
    public static CartItemInstance[] toInstances(JerseyDAO dao, CartItem[] items) {
        CartItemInstance[] cart = new CartItemInstance[items.length];
        for (int i = 0; i < items.length; ++i) {
            CartItem item = items[i];
            cart[i] = new CartItemInstance(
                dao.getJersey(item.getJersey()),
                item.getQuantity()
            );
        }
        return cart;
    }
}
