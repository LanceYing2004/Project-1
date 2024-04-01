import java.io.Serializable;

/**
 * A Item class that used to create Item objects.
 *
 * @author D.Mayer, L.Ying, I.Kleimans, J.Chen, S.Hou, Lab Section #3, Team #11
 * @version July 22, 2023
 */

  /*
  - Item(String name, String description, Seller seller, double price, int quantityAvailable)
   -> No limits or quantity-based sales.
  - Item(String name, String description, Seller seller, double price, int quantityAvailable, int perOrderLimit)
  -> No quantity-based sales, but a limit.
  - Item(String name, String description, Seller seller, double price, int quantityAvailable, int saleQuantity,
   double salePrice) -> No limit, but a quantity-based sale.
  - Item(String name, String description, Seller seller, double price, int quantityAvailable, int saleQuantity,
   double salePrice, int perOrderLimit) -> A limit and a quantity-based sale.
  
  Methods:
  - String getName() -> Returns the item name.
  - void setName(String name) -> Sets the item name.
  - double getPrice() -> Returns the item price.
  - void setPrice(double price) -> Sets the item price. 
  - int getSaleQuantity() -> Return's the sale quantity (i.e. the number of units sold before the seller's specified
  sale activates).
  - void setSaleQuantity(int saleQuantity) -> Sets the item's sale quantity. 
  - double getSalePrice() -> Returns the item's sale price.
  - void setSalePrice(double salePrice) -> Sets the item's sale price.
  - int getQuantityAvailable() -> Returns the quantity of the item available.
  - void setQuantityAvailable(int quantityAvailable) -> Sets the quantity of the item available.
  - int getPurchasedSoFar() -> Returns the quantity purchased so far (for determination of whether salePrice should
  be activated)
  - void setPurchasedSoFar(int purchasedSoFar) -> Sets the quantity purchased so far.
  - int getPerOrderLimit() -> Returns the item's per-order quantity limit.
  - void setPerOrderLimit(int perOrderLimit) -> Sets the item's per-order quantity limit.
  - ArrayList<Review> getReviews() -> Returns the item's reviews.
  - void addReview(Review review) -> Adds review to the item's reviews.
  - Seller getSeller() -> Returns the item's seller.
  - void setSeller(Seller seller) -> Sets the item's seller.
  - String getDescription() -> Returns the item's description.
  - void setDescription(String description) -> Sets the item's description.
   */
public class Item implements Serializable {
    private String name; //The name of an item
    private double price; //The price of an item
    private int saleQuantity; //Amount of number sold before the seller's specified sale activates
    private double salePrice; //Current sale price

    private int quantityAvailable; //the quantity of the item that is available
    private int purchasedSoFar; //quantity purchased so far
    private int perOrderLimit; //item order quantity limit
    private Seller seller; //sellers the Seller.
    private String description; //Description of the product
    private Store store; //store from Store class

    //Constructor 1, no limits or quantity-based sales
    public Item(String name, String description, Seller seller, double price, int quantityAvailable, Store store) {
        this.name = name;
        this.description = description;
        this.seller = seller;
        this.price = price;
        this.quantityAvailable = quantityAvailable;
        this.store = store;
    }

    //Constructor 2, no quantity-based sales, but a limit.
    public Item(String name, String description, Seller seller, double price, int quantityAvailable,
                int perOrderLimit, Store store) {
        this.name = name;
        this.description = description;
        this.seller = seller;
        this.price = price;
        this.quantityAvailable = quantityAvailable;
        this.perOrderLimit = perOrderLimit;
        this.store = store;

    }

    //Constructor 3, no limit, but a quantity-based sale.
    public Item(String name, String description, Seller seller, double price, int quantityAvailable, int saleQuantity,
                double salePrice, Store store) {
        this.name = name;
        this.description = description;
        this.seller = seller;
        this.price = price;
        this.quantityAvailable = quantityAvailable;
        this.saleQuantity = saleQuantity;
        this.salePrice = salePrice;
        this.store = store;
    }

    //Constructor 4, limit set and quantity-based sale set
    public Item(String name, String description, Seller seller, double price, int quantityAvailable, int saleQuantity,
                double salePrice, int perOrderLimit, Store store) {
        this.name = name;
        this.description = description;
        this.seller = seller;
        this.price = price;
        this.quantityAvailable = quantityAvailable;
        this.saleQuantity = saleQuantity;
        this.salePrice = salePrice;
        this.perOrderLimit = perOrderLimit;
        this.store = store;
    }

    // Methods
    // Name
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Item Price
    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // Sale Quantity (i.e. quantity after which special price takes effect)
    public int getSaleQuantity() {
        return this.saleQuantity;
    }

    public void setSaleQuantity(int saleQuantity) {
        this.saleQuantity = saleQuantity;
    }

    // Sale Price (i.e. special price after given saleQuantity units sold)
    public double getSalePrice() {
        return this.salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    // Quantity Available
    public int getQuantityAvailable() {
        return this.quantityAvailable;
    }

    public void setQuantityAvailable(int quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    // Quantity purchased so far (for determination of whether salePrice should be active).
    public int getPurchasedSoFar() {
        return this.purchasedSoFar;
    }

    public void setPurchasedSoFar(int purchasedSoFar) {
        this.purchasedSoFar = purchasedSoFar;
    }

    // Per-order limit
    public int getPerOrderLimit() {
        return this.perOrderLimit;
    }

    public void setPerOrderLimit(int perOrderLimit) {
        this.perOrderLimit = perOrderLimit;
    }

    // Reviews


    // seller
    public Seller getSeller() {
        return this.seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    // Description
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Store getStore() {
        return this.store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    /**
     * Format & combine the fields and return as a String.
     * <p>
     * Ex: "TestStore Product1 $4.56 ($1.23 after 20 purchased)
     *
     * @return, String, after it has been properly formatted.
     */
    // "
    @Override
    public String toString() {

        return String.format("%-20s  %-25s  %-10s\n", this.store.getName(), this.getName(), (saleQuantity > 0) ?
                String.format("$%.2f ($%.2f after %d purchased)", price, salePrice, saleQuantity) :
                String.format("$%.2f", price));
    }


    /**
     * Format & combine the fields and return as a String.
     * <p>
     * Ex:
     * Name: Excellent Product
     * Seller: A Seller
     * Store: A Store
     * Price: $6.78 ($2.24 after 10 purchased)
     * Description: The description for the product.
     * Quantity Available: 4
     * <p>
     * Review 1 (4 stars): This product was good, but it had some flaws.
     * Review 2 (1 star): Complete trash!
     * Review 3 (5 stars): The best I have ever seen.
     *
     * @return, String, after it has been properly formatted
     */
    public String productPageText() {
        String formatString = "Name: %s \nSeller: %s \nStore: %s \nPrice: %s \n" +
                "Description: %s \nQuantity Available: %d \n";
        String outString = String.format(formatString, name, seller.getEmail(), store.getName(), (saleQuantity > 0)
                ? String.format("$%.2f ($%.2f after %d)", price, salePrice, saleQuantity) :
                String.format("$%.2f", price), description, quantityAvailable);
        // Add the count limit
        if (perOrderLimit > 0) {
            outString = outString.concat(String.format("Warning: Per-order quantity limit of %d \n", perOrderLimit));
        }
        outString = outString.concat("\n");
        // Concatenate any Reviews

        return outString;
    }
}
