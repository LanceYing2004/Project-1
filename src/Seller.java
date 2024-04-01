import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class for creation of Seller objects. Those include seller's login credentials such as email and passwords. Other
 * information containing 2 ArrayList of stores and pastSales containing current store and past purchase information.
 *
 * @author D.Mayer, L.Ying, I.Kleimans, J.Chen, S.Hou, Lab Section #3, Team #11
 * @version July 31, 2023
 */

public class Seller implements Serializable /* "Serializable" allows for storage */ {
    /*

  Constructor: 
  - Seller(String email, String password)
 
  Methods:
  - String getEmail() -> Returns the seller's email.
  - void setEmail(String email) -> Sets the seller's email.
  - String getPassword() -> Returns the seller's password.
  - void setPassword(String password) -> Sets the user's password.
  - void addSale(Purchase purchase) -> Adds a new purchase to the seller's Sales ArrayList.
  - ArrayList<Purchase> getPastSales() -> Returns the seller's past sales.
  - ArrayList<Store> getStores() -> Returns the seller's stores.
  - void addStore(Store store) -> Appends the new store the the seller's Stores ArrayList. 

   */
    // Fields:
    private String email; // seller's login email
    private String password; // seller's login password
    private final ArrayList<Store> stores; // seller's stores
    private final ArrayList<Purchase> pastSales; // Add all past purchases for a given Seller to this array.

    //Constructors

    /**
     * Constructs a new Seller instance with the given email and password.
     * The stores and pastSales are initialized as empty ArrayLists.
     */
    public Seller(String email, String password) {
        // Constructor to create Buyer objects.
        this.email = email;
        this.password = password;
        // Initialize shoppingCart and pastSales to empty Arrays. ALWAYS ADD/REMOVE.
        stores = new ArrayList<Store>();
        pastSales = new ArrayList<Purchase>();
    }


    // Getters, setters, and adders.
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

    // Sales
    public void addSale(Purchase purchase) {
        pastSales.add(purchase);
    }

    public ArrayList<Purchase> getPastSales() {
        return this.pastSales;
    }

    // Stores
    public ArrayList<Store> getStores() {
        return this.stores;
    }

    public void addStore(Store store) {
        this.stores.add(store);
    }

    public void deleteStore(Store store) {
        this.stores.remove(store);
    }

    /**
     * Allow seller to sign out from their account
     */
    public void signOut() {
        System.out.println("See you next time!");
    }

    /**
     * Gather all the previous sales in one arraylist and return it.
     *
     * @return Arraylist allSales
     */
    public ArrayList<Purchase> getAllSales() {
        ArrayList<Purchase> allSales = new ArrayList<>();
        for (Store store : stores) {
            allSales.addAll(store.getStoreSeller().getPastSales());
        }
        return allSales;
    }

    /**
     * This method is used to get a list of all items that are currently in customers' shopping carts.
     *
     * @return A list of Item objects representing the items currently in customers' shopping carts.
     */


}
