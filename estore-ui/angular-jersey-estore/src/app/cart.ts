import { Jersey } from "./jersey";

export interface ShoppingCartItem {
    jersey: Jersey;
    quantity: number;
}

export interface ShoppingCart {
    items: ShoppingCartItem[],
    total: number
}