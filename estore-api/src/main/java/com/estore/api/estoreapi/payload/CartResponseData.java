package com.estore.api.estoreapi.payload;

/**
 * Shopping cart response data for associated API endpoints.
 */
public class CartResponseData {
    private CartItemInstance[] items;
    private double total;

    /**
     * Creates CartResponseData and calculates total cost of all
     * items in the cart.
     * @param items Items that are in shopping cart.
     */
    public CartResponseData(CartItemInstance[] items) {
        this.items = items;
        this.total = 0.0;
        for (CartItemInstance item : items)
            this.total += (item.getJersey().getPrice() * item.getQuantity());
    }

    public CartItemInstance[] getItems() { return this.items; }
    public double getTotal() { return this.total; }
}
