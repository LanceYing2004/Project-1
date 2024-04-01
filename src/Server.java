import java.io.*;
import java.net.*;
import java.util.*;


/**
 * This class handles all data and computation intensive tasks for the program. It does so by accepting connections
 * over a ServerSocket, creating a new thread for each, and conducting operations on that thread.
 *
 * @author Daniel Mayer, Lab Section #3, Team #11
 * @version July 31, 2023
 */

public class Server {

    /**
     * Begin and initialize the server here.
     */
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(11000); // ServerSocket to accept client connections
            FileOperations fileOps = new FileOperations(); // FileOps object to access files/synchronize methods. 
            while (true) {
                Socket connectSocket = serverSocket.accept(); // Socket to take input from the client instances.
                ConnectionThread ct = new ConnectionThread(connectSocket, fileOps); // Thread object for each client's 
                // tasks to run on.
                ct.start();
            }
        } catch (Exception e) {
            System.out.println("Communications error! Make sure that port 11000 is not in use and that your firewall" +
                    "is not blocking this program.");
        }
    }
}

/**
 * Class for the unique thread used by each client process. Started in the main server method each time a new 
 * ServerSocket connection with a client is opened. 
 *
 * @author D.Mayer, L.Ying, I.Kleimans, J.Chen, S.Hou, Lab Section #3, Team #11
 * @version July 31, 2023
 */
class ConnectionThread extends Thread {
    // Each thread has ONE buyer/seller.
    private Socket connectSocket; //Socket to be connected
    private ObjectInputStream ois; //Object Input Stream
    private ObjectOutputStream oos; //Object Output Stream
    private FileOperations fileOps; //File Operations


    /**
     * Basic constructor for each connection thread.
     *
     * @param connectSocket
     * @param fileOps
     */
    public ConnectionThread(Socket connectSocket, FileOperations fileOps) {
        this.connectSocket = connectSocket;
        this.fileOps = fileOps;
    }
    
    /**
     * Overrides the Thread class run() method. Handles most logic for each thread (i.e. each client), calling
     * other methods and classes as necessary to complete its tasks. 
     *
     */
    public void run() {
        try {
            while (true) {
                try {
                    InputStream is = connectSocket.getInputStream(); // Input stream to read data from the client.
                    ois = new ObjectInputStream(is);
                    Object[] objects = (Object[]) ois.readObject(); // Object[] of data read from the client.
                    int command = (Integer) objects[0]; // The command number read from the client input.
                    Object commandInput = objects[1]; // The object read from the client input.
                    Object returnObject = null; // The object that will be returned to the client.
                    BuyerCommands bc = new BuyerCommands(fileOps); // A BuyerCommands object to call relevant methods.
                    SellerCommands sc = new SellerCommands(fileOps); // A SellerCommands object to call relevant methods
                    if (command < 30) {
                        switch (command) {
                            // Buyer commands
                            case 0:
                                // Check a login to an existing account
                                // Input Object: String[] containing email, password
                                // Return: Buyer object
                                returnObject = bc.buyerLogin(commandInput);
                                break;
                            case 1:
                                // Create a new buyer
                                // Input Object: String[] containing email, password
                                // Return: Buyer object
                                returnObject = bc.newBuyer(commandInput);
                                break;
                            case 2:
                                // Show the market
                                // Input Object: Blank object (null);
                                // Return: ArrayList<Item> of market data
                                returnObject = bc.getMarket();
                                break;
                            case 3:
                                // Update Buyer object
                                // Input object: Buyer buyer
                                // Return: Nothing
                                returnObject = bc.updateBuyer(commandInput);
                                break;
                            case 4:
                                // Log a sale
                                // Input object: Purchase purchase
                                // Return: Nothing
                                returnObject = bc.logSale(commandInput);
                                break;
                            case 5:
                                // Get dashboard data
                                // Input object: null
                                // Return Object: Object[] of two ArrayList<String>s, 
                                // first one containing the individual buyer's purchases and the second containing 
                                // all purchases.
                                returnObject = bc.getDashboardInfo(commandInput);
                                break;
                            case 6:
                                // Get the latest server-side version of a buyer object
                                // Input object: Buyer buyer
                                // Return object: Buyer latestBuyer representing the most up-to-date version of buyer.
                                returnObject = bc.getLatestBuyer(commandInput);
                                break;
                            case 7:
                                // Get the buyer list
                                // Input object: null
                                // Return object: ArrayList<Buyer> of buyers
                                returnObject = bc.getBuyerList(commandInput);
                                break;
                        }
                        // Buyer commands
                        
                    } else {
                        switch (command) {
                            // Seller commands
                            case 30:
                                // Check a login to an existing account
                                // Input Object: String[] containing email, password
                                // Return: Seller object
                                returnObject = sc.sellerLogin(commandInput);
                                break;
                            case 31:
                                // Create a new seller
                                // Input Object: String[] containing email, password
                                // Return: Seller object
                                returnObject = sc.newSeller(commandInput);
                                break;
                            case 32:
                               // Update the seller
                                // Input object: Seller seller
                                // Return null object
                                returnObject = sc.updateSeller(commandInput);
                                break;
                            case 37:
                                // Get shopping carts containing the seller's products.
                                // Input object: Seller seller
                                // Return ArrayList<String>
                                returnObject = sc.getCartContents(commandInput);
                                break;
                            case 38:
                                // Get the seller's past sales
                                // Input object: Seller seller
                                // Return ArrayList<Purchase>
                                returnObject = sc.getPastSales(commandInput);
                                break;
                            case 39:
                                // Get the latest server-side version of a seller object
                                // Input object: Seller seller
                                // Return object: Seller seller representing the most up-to-date version of seller.
                                returnObject = sc.getLatestSeller(commandInput);    
                                break;
                            case 40:
                                // Return the seller statistics view
                                // Input object: Seller seller
                                // Return object: Object[] of four arrays: [ArrayList<String> statsByStore, 
                                // ArrayList<String> statsByBuyer, ArrayList<Integer> numsByStore, 
                                // ArrayList<Integer> numsByBuyer]
                                returnObject = sc.getSellerStatistics(commandInput);
                                break;
                            case 41:
                                // Get the seller list
                                // Input object: null
                                // Return object: ArrayList<Seller> of sellers
                                returnObject = sc.getSellerList(commandInput);
                                break;
                        }
                    }
       
                    ObjectOutputStream oos = 
                            new ObjectOutputStream(new BufferedOutputStream(connectSocket.getOutputStream())); // Output
                    // stream to return data to the client. 
                    oos.writeObject(returnObject);
                    oos.flush();
                } catch (Exception e) {
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("There has been a serious error. Check that port 11000 is still available.");
        }

    }
}

/**
 * This class handles all file access operations for the program. Methods are synchronized using objects defined
 * externally, as necessary. 
 *
 * @author D.Mayer, L.Ying, I.Kleimans, J.Chen, S.Hou, Lab Section #3, Team #11
 * @version July 31, 2023
 */
class FileOperations {
    private ObjectInputStream buyerOis; // ObjectInputStream to read from the buyers.bin file
    private ObjectInputStream sellerOis; // ObjectInputStream to read from the sellers.bin file
    private ArrayList<Buyer> buyersList; // ArrayList of buyers for processing
    private ArrayList<Seller> sellersList; // ArrayList of sellers for processing. 
    /**
     * Constructor for FileOperations; opens the data files and reads them into memory for later access. 
     *
     */
    public FileOperations() throws IOException {
        File sellerData = new File("sellers.bin"); // Seller data file object for reading. 
        sellerData.createNewFile();
        FileInputStream sellerFis = new FileInputStream(sellerData); // FileInputStream to actually read from the 
        // seller file. 
        try {
            sellerOis = new ObjectInputStream(sellerFis);
            sellersList = (ArrayList<Seller>) sellerOis.readObject();
        } catch (Exception e) {
            // The file must have been empty/nonexistent
            sellersList = new ArrayList<>();
            if (sellerOis != null) {
                sellerOis.close();
            }
            dumpToSellerFile();
        }
        if (sellerOis != null) {
            sellerOis.close();
        }
        
        File buyerData = new File("buyers.bin"); // Buyer data file object for reading.
        buyerData.createNewFile();
        FileInputStream buyerFis = new FileInputStream(buyerData); // FileInputStream to actually read from the buyer
        // data file. 
        try {
            buyerOis = new ObjectInputStream(buyerFis);
            buyersList = (ArrayList<Buyer>) buyerOis.readObject();
        } catch (Exception e) {
            // The file must have been empty/nonexistent
            buyersList = new ArrayList<>();
            if (buyerOis != null) {
                buyerOis.close();
            }
            dumpToBuyerFile();
        }
        if (buyerOis != null) {
            buyerOis.close();
        }
        
    }
    /**
     * Gets the buyer array list for processing elsewhere. Synchronized to avoid race conditions with list modification
     *
     */
    public synchronized ArrayList<Buyer> getBuyerList() {
        return this.buyersList;
    }
    /**
     * Gets the seller array list for processing elsewhere. Synchronized to avoid race conditions with list modification
     *
     */
    public synchronized ArrayList<Seller> getSellerList() {
        return this.sellersList;
    }
    /**
     * Adds a Buyer object to the buyers list and saves it to the proper data file; synchronized to avoid race 
     * conditions with other modification and reading operations. 
     * 
     * @param buyer The buyer to add to the list.
     */
    public synchronized void addBuyer(Buyer buyer) {
        buyersList.add(buyer);
        dumpToBuyerFile();
    }
    /**
     * Adds a Seller object to the buyers list and saves it to the proper data file; synchronized to avoid race 
     * conditions with other modification and reading operations. 
     *
     * @param seller The seller to add to the list.
     */
    public synchronized void addSeller(Seller seller) {
        sellersList.add(seller);
        dumpToSellerFile();
    }
    /**
     * Saves the current sellersList ArrayList to the 'sellers.bin' file using an ObjectOutputStream.
     * Synchronized to avoid race conditions with reading and modification operations. 
     *
     */
    public synchronized void dumpToSellerFile() {
        try {
            File sellerFile = new File("sellers.bin"); // File object for the seller data file.
            FileOutputStream sellerFos = new FileOutputStream(sellerFile); // FileOutputStream to write to the seller
            // data file
            ObjectOutputStream sellerOos = new ObjectOutputStream(sellerFos); // ObjectOutputStream to write objects
            // to the seller data file. 
            sellerOos.writeObject(sellersList);
            sellerOos.close();
        } catch (Exception e) {
            return;
        }
    }
    /**
     * Saves the current buyersList ArrayList to the 'buyers.bin' file using an ObjectOutputStream.
     * Synchronized to avoid race conditions with reading and modification operations. 
     *
     */
    public synchronized void dumpToBuyerFile() {
        try {
            File buyerFile = new File("buyers.bin"); // File object for the buyer data file.
            FileOutputStream buyerFos = new FileOutputStream(buyerFile); // FileOutputStream to write to the buyer
            // data file.
            ObjectOutputStream buyerOos = new ObjectOutputStream(buyerFos); // ObjectOutputStream to write objects
            // to the buyer data file. 
            buyerOos.writeObject(buyersList);
            buyerOos.close();
        } catch (Exception e) {
            return;
        }
    }
}
/**
 * This class provides methods to handle buyer commands defined above (in the run() method, commands 0-29). 
 *
 * @author D.Mayer, L.Ying, I.Kleimans, J.Chen, S.Hou, Lab Section #3, Team #11
 * @version July 31, 2023
 */
class BuyerCommands {
    private FileOperations fileOps; // FileOperations object for file/data access and synchronization. 
    /**
     * Constructor for the BuyerCommands class; initializes the local fileOps field.
     * 
     * @param fileOps FileOperations object for synchronization and file access. 
     */
    public BuyerCommands(FileOperations fileOps) {
        this.fileOps = fileOps;
    }
    /**
     * Process a buyer login from the client and return the correct Buyer object if the buyer indeed exists (and is 
     * present in the buyersList).
     *
     * @param inputObject A String[] containing {email, password} so that the program can check their validity.  
     */
    public Buyer buyerLogin(Object inputObject) {
        String[] buyerInfo = (String[]) inputObject; // Casting the input object to a usable form. 
        for (Buyer b : fileOps.getBuyerList()) {
            if (b.getEmail().equals(buyerInfo[0]) && b.getPassword().equals(buyerInfo[1])) {
                return b;
            }
        }
        return null;
    }
    /**
     * Create a new Buyer using client input and return that object.
     *
     * @param inputObject A String[] containing the {email, password} that are used for the object. 
     */
    public Buyer newBuyer(Object inputObject) {
        String[] buyerStringArr = (String[]) inputObject; // Casting the input object to a usable form.
        Buyer nBuyer = new Buyer(buyerStringArr[0], buyerStringArr[1]); // The new Buyer object to add/return.
        fileOps.addBuyer(nBuyer);
        return nBuyer;
    }
    /**
     * Returns an ArrayList of all items present in all stores; used to show the buyer market.
     *
     */
    public ArrayList<Item> getMarket() {
        ArrayList<Item> marketList = new ArrayList<>(); // The ArrayList that will contain all market items. 
        for (Seller s : fileOps.getSellerList()) {
            for (Store store : s.getStores()) {
                for (Item i : store.getItems()) {
                    marketList.add(i);
                }
            }
        }
        return marketList;
    }
    /**
     * Updates an existing buyer object in the BuyersList and buyer data file; used to log purchases and store 
     * shopping cart data. 
     *
     * @param inputObject The latest version of the Buyer object being updated.  
     */
    public Object updateBuyer(Object inputObject) {
        Buyer buyerToUpdate = (Buyer) inputObject; // The Buyer object read from the client.
        ArrayList<Buyer> buyerList = fileOps.getBuyerList(); // The list of all buyers.
        for (int i = 0; i < buyerList.size(); i++) {
            if (buyerList.get(i).getEmail().equals(buyerToUpdate.getEmail())) {
                buyerList.set(i, buyerToUpdate);
            }
        }
        fileOps.dumpToBuyerFile();
        return null;
    }
    /**
     * Logs a purchase sent from the item to all relevant locations (i.e. the seller's sale log,
     * the quantity indicator, and the store sale log).
     *
     * @param inputObject A Purchase object.  
     */
    public Object logSale(Object inputObject) {
        Purchase purchase = (Purchase) inputObject; // The purchase object received from the client.
        ArrayList<Seller> sellerList = fileOps.getSellerList(); // The list of all sellers. 
        synchronized (fileOps) {
            for (int i = 0; i < sellerList.size(); i++) {
                if (sellerList.get(i).getEmail().equals(purchase.getSeller().getEmail())) {
                    sellerList.get(i).addSale(purchase);
                    for (int j = 0; j < sellerList.get(i).getStores().size(); j++) {
                        if (sellerList.get(i).getStores().get(j).getName().equals(purchase.getStore().getName())) {
                            sellerList.get(i).getStores().get(j).addSale(purchase);
                            for (int k = 0; k < sellerList.get(i).getStores().get(j).getItems().size(); k++) {
                                if (sellerList.get(i).getStores().get(j).getItems().get(k).getName().equals(purchase.
                                        getItem().getName())) {
                                    sellerList.get(i).getStores().get(j).getItems().get(k).setQuantityAvailable(
                                            sellerList.get(i).getStores().get(j).getItems().get(k).
                                                    getQuantityAvailable() - purchase.getQuantityPurchased());
                                }
                            }
                        }
                    }
                }
            }
        }
        fileOps.dumpToSellerFile();
        return null;
    }
    /**
     * Gets the list of all buyers using FileOps and returns it to the client. 
     *
     * @param inputObject Unused object for standardization of form.
     */
    public ArrayList<Buyer> getBuyerList(Object inputObject) {
        return fileOps.getBuyerList();
    }
    /**
     * Returns ArrayLists of Strings and Integers representing data that is to be displayed (word-for-word) on the
     * buyer's dashboard page.
     *
     * @param inputObject The Buyer object of the Buyer requesting the dashboard.  
     */
    public Object[] getDashboardInfo(Object inputObject) {
        
        Buyer buyer = (Buyer) inputObject; // The Buyer requesting the dashboard.
        ArrayList<String> dashboardInfo = new ArrayList<>(); // Strings for 'all buyers' statistics.
        ArrayList<String> buyerData = new ArrayList<>(); // Strings for 'this buyer' statistics.
        ArrayList<Integer> dashboardPurchaseCounts = new ArrayList<>(); // Integers for 'all buyers' statistics.
        ArrayList<Integer> buyerPurchaseCounts = new ArrayList<>(); // Integers for 'this buyer' statistics.

        // Display purchase sale and quantity for the current user from each store
        ArrayList<Seller> sellers = fileOps.getSellerList(); // ArrayList of all sellers. 
        for (Seller seller : sellers) {
            for (Store store : seller.getStores()) {
                int cntr = 0; // Counter to insert values in the correct place.
                double storeRevenue = 0.0; // The revenue collected by this store from all buyers.
                int storeQuantity = 0; // The quantity sold by this store to all buyers.
                int buyerQuantity = 0; // The quantity sold by this store to this buyer.
                double buyerRevenue = 0.0; // The revenue collected by this store from this buyer.
                for (Purchase p : store.getSales()) {
                    if (p.getBuyer().equals(buyer)) {
                        cntr++;
                        buyerRevenue += p.getQuantityPurchased() * p.getPricePerUnit();
                        buyerQuantity += p.getQuantityPurchased();
                    } else {
                        cntr++;
                        storeRevenue += p.getQuantityPurchased() * p.getPricePerUnit();
                        storeQuantity += p.getQuantityPurchased();
                    }
                }


                if (cntr == 0) {
                    System.out.println("No sales from this store!");
                    buyerData.add("You have not made any purchases from this store!");
                    dashboardInfo.add("This store has not yet had any sales!");
                } else {
                    buyerData.add(String.format("You have purchased %d items from %s, with the total cost of $%.2f",
                            buyerQuantity, store, buyerRevenue));
                    dashboardInfo.add(String.format("Everyone has purchased %d items from %s, with the total cost of " +
                                    "$%.2f",
                            storeQuantity, store, storeRevenue));
                    dashboardPurchaseCounts.add(storeQuantity);
                    buyerPurchaseCounts.add(buyerQuantity);
                    
                }
            }
        }
        
        return new Object[]{dashboardInfo, buyerData, dashboardPurchaseCounts, buyerPurchaseCounts};
    }


    /**
     * Gets the latest version of a Buyer object that is stored on the server side. 
     *
     * @param inputObject A Buyer object for which the client wants the latest version.  
     */
    public Buyer getLatestBuyer(Object inputObject) {
        Buyer buyer = (Buyer) inputObject; // The buyer object sent by the client.
        ArrayList<Buyer> buyerList = fileOps.getBuyerList(); // List of all buyers.
        for (Buyer b : buyerList) {
            if (b.getEmail().equals(buyer.getEmail())) {
                return b;
            }
        }
        return buyer;
    }
}

/**
 * This class provides methods to handle seller commands defined above (in the run() method, commands 30+). 
 *
 * @author D.Mayer, L.Ying, I.Kleimans, J.Chen, S.Hou, Lab Section #3, Team #11
 * @version July 31, 2023
 */
class SellerCommands {
    private FileOperations fileOps; // FileOperations object for file access and synchronization.
    /**
     * Constructor for the SellerCommands class. Initializes fileOps to the specified parameter. 
     *
     * @param fileOps The FileOperations object to initialize fileOps to.  
     */
    public SellerCommands(FileOperations fileOps) {
        this.fileOps = fileOps;
    }
    /**
     * Checks seller login information and returns the correct Seller object if it is correct. 
     *
     * @param inputObject A String[] containing the email and password of the seller.  
     */
    public Seller sellerLogin(Object inputObject) {
        String[] sellerInfo = (String[]) inputObject; // Seller email and password in usable form. 
        for (Seller s : fileOps.getSellerList()) {
            if (s.getEmail().equals(sellerInfo[0]) && s.getPassword().equals(sellerInfo[1])) {
                return s;
            }
        }
        return null;
    }
    /**
     * Creates a new Seller using information received from the client.  
     *
     * @param inputObject A String[] containing the email and password of the new Seller to be created.  
     */
    public Seller newSeller(Object inputObject) {
        String[] sellerStringArr = (String[]) inputObject; // The seller's username and password, as collected 
        // from the client. 
        Seller nSeller = new Seller(sellerStringArr[0], sellerStringArr[1]); // The new seller object.
        fileOps.addSeller(nSeller);
        return nSeller;
    }
    /**
     * Updates the server-side version of a Seller object to a version received from the client. 
     *
     * @param inputObject A Seller object to update.  
     */
    public Object updateSeller(Object inputObject) {
       
        Seller sellerToUpdate = (Seller) inputObject; // The Seller object that is to be updated. 
        ArrayList<Seller> sellerList = fileOps.getSellerList(); // The list of all sellers registered in the program.
        for (int i = 0; i < sellerList.size(); i++) {
            if (sellerList.get(i).getEmail().equals(sellerToUpdate.getEmail())) {
                sellerList.set(i, sellerToUpdate);
            }
        }
        fileOps.dumpToSellerFile();
        return null;
    }
    /**
     * Gets the placement of the input seller's items in all buyer shopping carts; used in the "View Shopping Carts" 
     * screen. 
     *
     * @param inputObject A Seller object whose products' cart placement is to be reported.  
     */
    public ArrayList<String> getCartContents(Object inputObject) {
        Seller seller = (Seller) inputObject; // The Seller whose products' cart placement is to be reported.
        ArrayList<Buyer> buyers = fileOps.getBuyerList(); // An ArrayList of all registered buyers.
        ArrayList<String> cartContents = new ArrayList<>(); // The ArrayList<String> of cart contents to return.
        for (Buyer b : buyers) {
            for (Item i : b.getShoppingCart()) {
                if (i.getSeller().getEmail().equals(seller.getEmail())) {
                    cartContents.add("Buyer: " + b.getEmail() + " / Product: " + i.getName() + " / Store: " +
                            i.getStore().getName() + " / Price: $" + String.format("%.2f", i.getPrice()));
                }
            }
        }
        return cartContents;
    }
    /**
     * Gets all of a specific seller's past sales. 
     *
     * @param inputObject A Seller object whose past sales are to be reported.   
     */
    public ArrayList<Purchase> getPastSales(Object inputObject) {
        Seller seller = (Seller) inputObject; // The Seller object whose past sales are to be reported.
        ArrayList<Seller> sellers = fileOps.getSellerList(); // An ArrayList of all registered sellers.
        ArrayList<Purchase> retSales = new ArrayList<>(); // An ArrayList of sales to return to the client.
        for (Seller s : sellers) {
            if (s.getEmail().equals(seller.getEmail())) {
                retSales = s.getPastSales();
            }
        }
        return retSales;
    }
    /**
     * Gets the latest server-side version of a Seller object.
     *
     * @param inputObject The Seller object for which the latest version is requested.  
     */
    public Seller getLatestSeller(Object inputObject) {
        Seller seller = (Seller) inputObject; // The Seller in question
        ArrayList<Seller> sellerList = fileOps.getSellerList(); // A list of all registered Sellers.
        for (Seller s : sellerList) {
            if (s.getEmail().equals(seller.getEmail())) {
                return s;
            }
        }
        return seller;
    }
    /**
     * Gets strings that can be shown on the Seller Statistics page of the client program. 
     *
     * @param inputObject A Seller object whose statistics are to be reported.  
     */
    public Object[] getSellerStatistics(Object inputObject) {
        Seller seller = (Seller) inputObject; // The Seller whose statistics are to be reported. 
        ArrayList<Buyer> buyers = new ArrayList<>(); // An ArrayList of all registered buyers. 
        ArrayList<String> sellerSaleByBuyer = new ArrayList<>(); // ArrayList of Strings representing the seller's sales
        // organized by buyer.
        ArrayList<String> sellerSaleByProduct = new ArrayList<>(); // ArrayList of Strings representing the seller's
        // sales, organized by product.
        ArrayList<Double> buyerTotalSale = new ArrayList<>(); // ArrayList of Doubles represent each buyer's total
        // purchases.
        ArrayList<Double> productTotalSale = new ArrayList<>(); // ArrayList of Doubles representing each product's
        // total sales.
        for (Purchase p : seller.getPastSales()) {
            if (!buyers.contains(p.getBuyer())) {
                buyers.add(p.getBuyer());
            }
        }
        for (Buyer b : buyers) {
            double totalSales = 0.00; // The total revenue of sales to this buyer. 
            int numSales = 0; // The number of sales to this buyer.
            for (Purchase p : seller.getPastSales()) {
                if (p.getBuyer().equals(b)) {
                    totalSales += p.getPricePerUnit() * p.getQuantityPurchased();
                    numSales += p.getQuantityPurchased();
                }
            }
            sellerSaleByBuyer.add(String.format("Buyer: %s / Purchases: %d  / Revenue: $%.2f\n", 
                    b.getEmail(), numSales, totalSales));
            buyerTotalSale.add(totalSales);
        }
        ArrayList<Item> productList = new ArrayList<>(); // List of all products sold by the seller.
        for (Store store : seller.getStores()) {
            for (Item item : store.getItems()) {
                productList.add(item);
            }
        }
        for (Item item : productList) {
            double totalSaleRevenue = 0.00; // Total revenue from this item. 
            int numSales = 0; // Number of sales of this item.
            for (Purchase p : seller.getPastSales()) {
                if (p.getItemName().equals(item.getName())) {
                    totalSaleRevenue += p.getPricePerUnit() * p.getQuantityPurchased();
                    numSales += p.getQuantityPurchased();
                }
            }
            sellerSaleByProduct.add(String.format("Product: %s / Purchases: %d / Amount: $%.2f\n", item.getName(),
                    numSales, totalSaleRevenue));
            productTotalSale.add(totalSaleRevenue);
            
        }
        return new Object[]{sellerSaleByBuyer, sellerSaleByProduct, buyerTotalSale, productTotalSale};
    }
    /**
     * Gets the current server-side list of all sellers.
     *
     * @param inputObject An object that is unused at present.  
     */
    public ArrayList<Seller> getSellerList(Object inputObject) {
        return fileOps.getSellerList();
    }
}