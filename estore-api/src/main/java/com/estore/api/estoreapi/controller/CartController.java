
package com.estore.api.estoreapi.controller;
import java.util.Arrays;
import java.util.logging.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.estore.api.estoreapi.helpers.ShoppingCartHelper;
import com.estore.api.estoreapi.model.CartItem;
import com.estore.api.estoreapi.model.Jersey;
import com.estore.api.estoreapi.payload.CartItemInstance;
import com.estore.api.estoreapi.payload.CartResponseData;
import com.estore.api.estoreapi.persistence.CartDAO;
import com.estore.api.estoreapi.persistence.JerseyDAO;
import com.estore.api.estoreapi.security.service.UserDetailsInstance;
/**
 * Handles the REST API requests for Shopping Carts.
 */
@RestController
@RequestMapping("users/me/cart")
public class CartController {
    private static final Logger LOG = Logger.getLogger(CartController.class.getName());
    private CartDAO cartDao;
    private JerseyDAO jerseyDao;

    public CartController(CartDAO cartDAO, JerseyDAO jerseyDAO) {
        this.cartDao = cartDAO;
        this.jerseyDao = jerseyDAO;
    }
    /**
     * Responds to the GET request for a shopping cart.
     * @return ResponseEntity with user's {@link CartResponseData shopping cart} and HTTP status of OK if no errors occur.
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR if some problem occurs.
     */
    @GetMapping()
    protected ResponseEntity<CartResponseData> getShoppingCart(@AuthenticationPrincipal UserDetailsInstance details) {
        LOG.info("GET /users/me/cart");
        CartItem[] items = this.cartDao.getCart(details.getUsername());
        CartItemInstance[] cart = ShoppingCartHelper.toInstances(this.jerseyDao, items);
        return ResponseEntity.ok(new CartResponseData(cart));
    }
    /**
     * Responds to the POST request for adding an {@linkplain CartItem item} to a user's cart.
     * @param item The item to add to the cart
     * @return ResponseEntity with user's new {@link CartResponseData shopping cart} and HTTP status of OK if the item was added successfully.
     * ResponseEntity with HTTP status of BAD_REQUEST if quantity is invalid, e.g. 0.
     * ResponseEntity with HTTP status of NOT_FOUND if either user or item isn't found in system.
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR if some error occurs.
     */
    @PostMapping()
    protected ResponseEntity<CartResponseData> addItemToCart(@RequestBody CartItem item, @AuthenticationPrincipal UserDetailsInstance details) {
        LOG.info("POST /users/me/cart");
        if (item.getQuantity() <= 0)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Jersey jersey = jerseyDao.getJersey(item.getJersey()) ;
        if (jersey == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        // Check if the item already exists in cart,
        // if it does add that to our quantity check.
        CartItem[] items = this.cartDao.getCart(details.getUsername());
        int index = Arrays.asList(items).indexOf(item);
        int quantity = item.getQuantity();
        if (index != -1)
            quantity += items[index].getQuantity();
                
        if (jersey.getQuantity() <= 0 || quantity > jersey.getQuantity())
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        
        if (this.cartDao.addToCart(details.getUsername(), item)) {
            CartItemInstance[] cart = ShoppingCartHelper.toInstances(this.jerseyDao, this.cartDao.getCart(details.getUsername()));
            return ResponseEntity.ok(new CartResponseData(cart));
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @PutMapping()
    protected ResponseEntity<CartResponseData> checkoutCart(@AuthenticationPrincipal UserDetailsInstance details){
        LOG.info("PUT /users/me/cart");
        CartItem[] items = this.cartDao.getCart(details.getUsername());
        Jersey jersey;
        if (this.cartDao.clearCart(details.getUsername())){
            for (CartItem item:items) {
                jersey = jerseyDao.getJersey(item.getJersey());
                jersey.setQuantity(jersey.getQuantity()-item.getQuantity());
            }
            CartItemInstance[] cart = 
                ShoppingCartHelper.toInstances(this.jerseyDao, this.cartDao.getCart(details.getUsername()));
            return ResponseEntity.ok(new CartResponseData(cart));
            
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    /**
     * Responds to the PUT request for setting a {@linkplain CartItem item}'s quantity.
     * @param item The item that's quantity is to be set.
     * @return ResponseEntity with user's new {@link CartResponseData shopping cart} and HTTP status of OK if the item's quantity was changed successfully.
     * ResponseEntity with HTTP status of BAD_REQUEST if quantity is invalid, e.g. 0.
     * ResponseEntity with HTTP status of NOT_FOUND if either user or item isn't found in system.
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR if some error occurs.
     */
    //@RequestMapping("users/me/cart/item")
    @PutMapping("/item")
    protected ResponseEntity<?> setCartItemQuantity(@RequestBody CartItem item, @AuthenticationPrincipal UserDetailsInstance details) {
        LOG.info("PUT /users/me/cart/item/");
        
        if (item.getQuantity() <= 0)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        
        int inventoryQuantity = jerseyDao.getJersey(item.getJersey()).getQuantity();
        if (inventoryQuantity < item.getQuantity())
            return new ResponseEntity<Integer>(inventoryQuantity, HttpStatus.CONFLICT);
        
        if (this.cartDao.setQuantity(details.getUsername(), item)) {
            CartItemInstance[] cart = 
                ShoppingCartHelper.toInstances(this.jerseyDao, this.cartDao.getCart(details.getUsername()));
            return ResponseEntity.ok(new CartResponseData(cart));
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @DeleteMapping()
    protected ResponseEntity<CartResponseData> removeItemFromCart(@RequestBody CartItem item, @AuthenticationPrincipal UserDetailsInstance details) {
        LOG.info("POST /users/me/cart");
        if (this.cartDao.removeItemFromCart(details.getUsername(), item)){
            CartItemInstance[] cart = ShoppingCartHelper.toInstances(this.jerseyDao,this.cartDao.getCart(details.getUsername()));
            return ResponseEntity.ok(new CartResponseData(cart));
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
