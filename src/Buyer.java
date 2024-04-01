import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * The Buyer class represents the user's personal information and methods for managing these properties.
 * This class implements the Serializable interface to support an easy way to save and load data.
 * It holds the buyer's email, password, shopping cart, and past purchases.
 *
 * @author D.Mayer, L.Ying, I.Kleimans, J.Chen, S.Hou, Lab Section #3, Team #11
 * @version July 31, 2023
 */

public class Buyer implements Serializable {
    private String email;  // Email used for buyer's account
    private String password;  // Password for buyer's account
    private ArrayList<Item> shoppingCart;  // Items currently in buyer's shopping cart
    private ArrayList<Purchase> pastPurchases;  // Record of buyer's past purchases

    /**
     * Constructs a new Buyer instance with the given email and password.
     * The shoppingCart and pastPurchases are initialized as empty ArrayLists.
     */
    public Buyer(String email, String password) {
        this.email = email;
        this.password = password;
        this.shoppingCart = new ArrayList<>();
        this.pastPurchases = new ArrayList<>();
    }

    // Getters and setters for email and password.
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getter for shopping cart.
    public ArrayList<Item> getShoppingCart() {
        return this.shoppingCart;
    }

    /**
     * Adds a new item to the buyer's shopping cart.
     */
    public void addToShoppingCart(Item newItem) {
        this.shoppingCart.add(newItem);
    }

    /**
     * Removes an item from the buyer's shopping cart.
     */
    public void removeFromShoppingCart(Item item) {
        this.shoppingCart.remove(item);
    }

    /**
     * Adds a new purchase to the buyer's list of past purchases.
     */
    public void addPurchase(Purchase purchase) {
        this.pastPurchases.add(purchase);
    }

    /**
     * Compiles a list of unique store names from where the buyer has made purchases.
     */
    public ArrayList<String> getPurchasedStores() {
        ArrayList<String> stores = new ArrayList<>();
        for (Purchase purchase : this.pastPurchases) {
            if (!stores.contains(purchase.getStore().getName())) {
                stores.add(purchase.getStore().getName());
            }
        }
        return stores;
    }

    /**
     * Counts the number of items bought from a given store.
     */
    public int getNumberOfPurchasesFromStore(String storeName) {
        int count = 0;
        for (Purchase purchase : this.pastPurchases) {
            if (purchase.getStore().getName().equals(storeName)) {
                count += purchase.getQuantityPurchased();
            }
        }
        return count;
    }

    /**
     * Return the past purchases indicator as a boolean.
     * @return boolean
     */
    public ArrayList<Purchase> getPastPurchases() {
        return this.pastPurchases;
    }
    public boolean equals(Object object) {
        if (object instanceof Buyer && ((Buyer) object).getEmail().equals(this.getEmail())) {
            return true;
        }
        return false;
    }
}
