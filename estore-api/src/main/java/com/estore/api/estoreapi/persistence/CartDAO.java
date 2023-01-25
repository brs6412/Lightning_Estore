package com.estore.api.estoreapi.persistence;

import com.estore.api.estoreapi.model.CartItem;

/**
 * Defines the interface for Shopping Cart object persistence.
 */
public interface CartDAO {

    /**
     * Initializes a shopping cart for a specified {@linkplain User user}.
     * @param username The username to create a shopping cart for
     * @return True if no error occurs when adding item to cart.
     */
    boolean createCart(String username);

    /**
     * Retrieves a list of {@linkplain CartItem cart items} by username.
     * @param username The username to search for.
     * @return {@linkplain CartItem Items} in {@link User user}'s shopping cart.
     */
    CartItem[] getCart(String username);

    /**
     * Adds a {@linkplain CartItem cart item} to a user's shopping cart.
     * If the item already exists, quantity will be increased.
     * @param username The username of the {@linkplain User user} who owns the shopping cart.
     * @param item Item to add to cart.
     * @return True if no error occurs when adding item to cart.
     */
    boolean addToCart(String username, CartItem item);

    /**
     * Clears a customer's cart given they decide to checkout.
     * @param username The username of the {@linkplain User user} who owns the shopping cart.
     * @return True if no error occurs when clearing the cart.
     */
    boolean clearCart(String username);

    /**
     * Sets a {@linkplain CartItem cart item}'s quantity.
     * @param username The username of the {@linkplain User user} who owns the shopping cart.
     * @param item Item quantity to be set.
     * @return True if no error occurs when setting an item's quantity.
     */
    boolean setQuantity(String username, CartItem item);

    /**
     * Removes a jersey from the cart 
     * @param username The username of the user's shopping cart
     * @param item item being removed 
     * @return 
     */
    boolean removeItemFromCart(String username, CartItem item);

    /**
     * Removes a jersey from all carts in the database.
     * @param jerseyId ID of jersey to remove from carts.
     * @return True if no error occurs when removing jersey from carts.
     */
    boolean removeJerseyFromAllCarts(int jerseyId);
}
