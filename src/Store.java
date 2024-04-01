import java.io.Serializable;
import java.util.ArrayList;

/**
 * A Store class for creation of Store objects.
 *
 * @author D.Mayer, L.Ying, I.Kleimans, J.Chen, S.Hou, Lab Section #3, Team #11
 * @version July 22, 2023
 */

/*

  Constructor: 
  - Store(String name, Seller storeSeller)
  -> NOTE: Initializes items ArrayList to an empty list. Items must be added individually and later.
 
  Methods:
  - String getName() -> Returns the name of the store.
  - String setName(String name) -> Sets the name of the store.
  - Seller getStoreSeller() -> Returns the Seller who "owns" the store.
  - void setStoreSeller(Seller seller) -> Sets the Seller who "owns" the store.
  - ArrayList<Item> getItems() -> Returns the items in the store.
  - void addNewItem(Item item) -> Appends item to the end of the ArrayList of items in the store.
  - void removeItem(Item item) -> Removes item from the ArrayList of items in the store.

   */

public class Store implements Serializable {
    private String name; //The name of the store
    private final ArrayList<Item> items; //items inside a store
    private Seller storeSeller; //store seller of "owns" the store
    private final ArrayList<Purchase> sales; //sales of an item in that store

    public Store(String name, Seller storeSeller) {
        this.name = name;
        this.storeSeller = storeSeller;
        items = new ArrayList<Item>();
        sales = new ArrayList<>();
    }

    // Setters, getters, and adders
    // Name
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // StoreSeller
    public Seller getStoreSeller() {
        return this.storeSeller;
    }

    public void setStoreSeller(Seller seller) {
        this.storeSeller = seller;
    }

    // Items
    public ArrayList<Item> getItems() {
        return this.items;
    }

    public void addNewItem(Item item) {
        this.items.add(item);
    }

    public void removeItem(Item item) {
        this.items.remove(item);
    }

    public ArrayList<Purchase> getSales() {
        return sales;
    }

    public void addSale(Purchase purchase) {
        sales.add(purchase);
    }

    @Override
    public String toString() {
        return String.format("%s (%d items)", this.name, this.items.size());
    }
}
