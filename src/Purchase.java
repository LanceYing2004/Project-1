import java.io.Serializable;

/**
 * Class for creation of Purchase objects to represent specific purchases.
 *
 * @author D.Mayer, L.Ying, I.Kleimans, J.Chen, S.Hou, Lab Section #3, Team #11
 * @version July 31, 2023
 **/
public class Purchase implements Serializable {
    /*

    Constructor:
    - Purchase(double pricePerUnit, int quantity, String itemName, String description, Seller seller, Buyer buyer)

    Methods:
    - double getPricePerUnit() -> Returns the price per unit of the purchased item, when it was purchased.
    - void setPricePerUnit(double pricePerUnit) -> Sets the price per unit of the purchased item, when it was purchased.
    - int getQuantityPurchased() -> Returns the quantity of the item purchased in the given transaction.
    - void setQuantityPurchased(int quantityPurchased)
    -> Sets the quantity of the item purchased in the given transaction.
    - String getItemName() -> Returns the name of the item at the time of the purchase.
    - void setItemName(String itemName) -> Sets the name of the item at the time of the purchase.
    - String getDescription() -> Returns the description of the item at the time of the purchase.
    - void setDescription(String description) -> Sets the description of the item at the time of the purchase.
    - Seller getSeller() -> Returns the seller of the purchased item.
    - void setSeller(Seller seller) -> Sets the seller of the purchased item.
    - Buyer getBuyer() -> Returns the buyer of the purchased item.
    - void setBuyer(Buyer buyer) -> Sets the buyer of the purchased item.

     */
    private double pricePerUnit; //cost of a single item in the store
    private int quantity; //number of quantity available
    private String itemName; //name of that item
    private String description; //description of that item
    private Seller seller; //seller from Seller class
    private Buyer buyer; //buyer from Buyer class
    private Store store; //store from Store class
    private double revenue; //amount of revenue generated
    private Item item; //item from Item class

    /**
     * Constructor of Purchase class
     *
     * @param pricePerUnit, price per unit
     * @param quantity,     amount of quantity available
     * @param itemName,     name of that item
     * @param description,  description of that item
     * @param seller,       name of the seller
     * @param buyer,        name of the buyer
     * @param store,        name of the store
     * @param item,         name of that item
     */
    public Purchase(double pricePerUnit, int quantity, String itemName, String description, Seller seller,
                    Buyer buyer, Store store, Item item) {
        this.pricePerUnit = pricePerUnit;
        this.quantity = quantity;
        this.itemName = itemName;
        this.description = description;
        this.seller = seller;
        this.buyer = buyer;
        this.revenue = revenue;
        this.store = store;
        this.item = item;
    }

    // Setters and getters
    // pricePerUnit
    public double getPricePerUnit() {
        return this.pricePerUnit;
    }

    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    // Quantity Purchased
    public int getQuantityPurchased() {
        return this.quantity;
    }

    public void setQuantityPurchased(int quantityPurchased) {
        this.quantity = quantityPurchased;
    }

    // Item Name
    public String getItemName() {
        return this.itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    // Description
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // seller
    public Seller getSeller() {
        return this.seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    // Customer name
    public Buyer getBuyer() {
        return this.buyer;
    }

    public void setBuyer(Buyer buyer) {
        this.buyer = buyer;
    }

    public double getRevenue() {
        return revenue;
    }

    public Store getStore() {
        return this.store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Item getItem() {
        return this.item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    /**
     * To string method, convert relevant information to string as desired format.
     *
     * @return formatted string.
     */
    @Override
    public String toString() {
        return String.format("%-30s $%-10.2f %-20d %-20s",
                this.itemName, this.pricePerUnit * this.quantity, this.quantity, this.getBuyer().getEmail());
    }

    /**
     * To string method, convert relevant information to string as desired format.
     *
     * @return formatted string.
     */
    public String toBuyerString() {
        return String.format("%-30s $%-20.2f %-25d %-20s",
                this.itemName, this.pricePerUnit * this.quantity, this.quantity, this.getStore().getName());
    }
}
