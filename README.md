


# CS-180-Group-11-Project-5

# Introduction
* README for Purdue University CS 18000 Project 5, Option 3: Marketplace
* Created by:
  * Junan Chen (chen4842)
  * Sally Hou (hou151)
  * Ian Kelimans (ikleiman)
  * Daniel Mayer (mayer68)
  * Lance Ying (ying43).
* Group 11, Lab 3
# Icon Attribution:
Icons are from Icons8: [Link to Icons8](https://icons8.com)  
*(This attribution is also shown in the "About" menu of the program.)*


# Compilation and Running
Compile the `MenuGUI.java` and `Server.java` files in `src` using JDK 9 or greater on a graphical system. Run the `Server` class first, then run the `MenuGUI` class and follow the on-screen prompts to use the program.
* Using `javac`, `java`, and `git` on Linux, macOS, or Windows:
```  
$ git clone https://github.com/daniel-s-mayer/CS-180-Group-11-Project-5.git  
$ cd CS-180-Group-11-Project-5  
$ cd src  
$ javac MenuGUI.java  
$ javac Server.java  
$ java Server  
$ java MenuGUI  
```  
* Using a commercial IDE on all systems:
* Clone the git repository at [https://github.com/daniel-s-mayer/CS-180-Group-11-Project-5.git](https://github.com/daniel-s-mayer/CS-180-Group-11-Project-5.git)
* Compile and run the `Server.java` file.
* Compile and run the `MenuGUI.java` file.
* Follow the on-screen prompts to use the program.

# Note on Test Cases:
All test cases must be run in order from an originally "blank" system unless otherwise specified. To "clear" the system, delete the `buyers.bin` and `sellers.bin` files in the main project directory.
Error cases must also be run from a "blank" system.
# Submission
* Daniel Mayer (mayer68) - submitted code on Vocareum workspace
* Ian Kleimans (ikleiman) - Brightspace submission of Project 5 Report.
* Junan Chen (chen4842) - submitted a fifteen minute, pre-recorded video presenting our work via Brightspace.
* Daniel Mayer (mayer68) - emailed a YouTube link to the presentation video in case of problems with Brightspace. 

# Classes:
### Object Classes:
* Store.java
* Item.java
* Buyer.java
* Seller.java
* Purchase.java

### GUI Classes:
* MenuGUI.java
* DashboardGUI.java
* LoginGUI.java
* MarketGUI.java
* BuyerMenuGUI.java
* BuyerShoppingCartGUI.java
* SellerImportExportGUI.java
* SellerMarketGUI.java
* SellerMenuGUI.java
* SellerSalesGUI.java
* SellerShoppingCartGUI.java
* StatisticsGUI.java
### Server Classes:
* Server.java
* ServerComm.java

# Descriptions of Classes:
## Object Classes:
### Buyer.java
* **Description**: The `Buyer` class stores buyer objects, each containing the following fields: email address (String), password (String), past purchases (ArrayList<Purchase>), and shopping cart contents (ArrayList <Item>). Setters and getters are provided for all of these fields, in addition to methods to print to the screen, export all past purchases made by the buyer, or create a list of unique stores from which the buyer has made a purchase. Objects of this class are created the `BuyerPathway` class and accessed as necessary from both the `BuyerPathway` and `SellerPathway` classes. Likewise, this class implements the `Serializable` interface to facilitate export to program data files.

* **Testing:** This class was tested via general program testing with simulated user input, as objects of this class are created, accessed, and modified throughout normal program operation.
### Seller.java
* **Description:** The `Seller` class is largely similar to the `Buyer` class, except that this class contains seller objects and contains fields for past sales (ArrayList) and stores (ArrayList) instead of a shopping cart or past purchases. Setters and getters are provided for each field, and objects of this class are created by the `SellerPathway` class and accessed by both the `BuyerPathway` and `SellerPathway` classes. Like the `Buyer` class, this class implements the `Serializable` interface to facilitate file storage.

* **Testing:** Like the `Buyer` class, this class was tested with general program testing.
### Purchase.java
* **Description:** This class is used to create objects for each `Purchase` made by a user. These objects are stored within ArrayLists in `Buyer`, `Seller`, and `Store` objects, allowing for organized storage of key purchase information. The class contains all necessary fields to represent a purchase, including price per unit (double), quantity purchased (int), item name (String), description (String), seller (Seller), buyer (Buyer), store (Store), and revenue (double). Setters and getters are included for each field in the class, and objects of this class are created in the `BuyerPathway` class and accessed by both the `BuyerPathway` and `SellerPathway` classes. Like all other object classes, this class implements the `Serializable` interface to facilitate file storage.

* **Testing:** This class was tested by recording values on paper, making sample purchases with the recorded values, and accessing the objects for those purchases from various parts of the program.
### Item.java
* **Description:** This class is used for `Item` objects throughout the program. These `Item` objects are stored in ArrayLists throughout most classes of the program (notably the `Store` class), with each `Item` object containing the follow fields: name (String), price (double), quantity for sale price (int), sale price (double), quantity available (int), quantity purchased so far (int), per-order quantity limit (int), review (ArrayList), seller (Seller), description (String), store (Store). Setters and getters for each of these items are included, as is a `productPageTest()` method to get formatted text for the product information page. Like the other object classes, this class implements the `Serializable` interface to permit items to be saved to files.

* **Testing:** This class was tested by creating, editing, viewing, deleting, and purchasing items throughout the program.
### Store.java
* **Description:** This class is used to create and access `Store` objects throughout the buyer and seller pathways. Each `Store` object contains all necessary fields to fully represent a unique store (i.e. name, items, seller associated with the store, and sales), all of which have setters and getters. `Store` objects are created within the `SellerPathway` class and stored in each `Seller` object, and these `Store` objects are accesed by both the `BuyerPathway` and `SellerPathway` classes. Like all other object classes, this class implements the `Serializable` interface to allow other classes to save these objects to files.

* **Testing:** This class was tested by creating, editing, viewing, and deleting stores and their member items and sales throughout general program execution.
## Interface Classes:
### MenuGUI:
* **Description:** This class serves as the GUI entry point into the program. It launches the main `JFrame` that is used  
  throughout the program, and displays buttons for the user to select whether they are a buyer or a seller. Once a button  
  is selected, the class calls the `LoginGUI` class to begin the correct (i.e. buyer or seller) "pathway."
* **Testing:** This class was tested by trying to access both the buyer and seller "pathways" of the program through it.
* Screenshot:  
![Login Screen](https://i.ibb.co/8jPhh56/Menu-GUI2-73.png)
### LoginGUI:
* **Description**: This class allows the user to log in to the program or create a new account. By taking a `boolean isBuyer` argument from the calling class, this class supports both buyer and seller logins. After a login is complete or the user has successfully created a new account (including compliance with the requirements of 6-character minimum usernames and passwords and a mandatory `@` symbol in the username), this class calls `BuyerMenuGUI` or `SellerMenuGUI` to continue the relevant pathway. This class also calls the `ServerComm` class (which, in turn, makes `Socket` calls to the `Server` class) in order to check login information and/or log the creation of a new account.
* **Testing:** This class was tested by creating numerous accounts for both buyers and sellers. To ensure that error checking also works, invalid username/passwords inputs of all types (e.g. inputs that were too short or lacked required elements) were also tested.
* **Screenshot**:  
  ![Login Screen](https://i.ibb.co/sbxg9Bf/Login-GUI-73.png "Login Screen")
### BuyerMenuGUI:
* **Description:** This class allows the buyer to select transactions to perform after logging in. The buyer can select one of the following options by choosing the appropriate button, and the appropriate class will be started (and passed the main `JFrame`, a `JPanel`, and a `Buyer` object) to conduct that transaction: View Market, Dashboard, Shopping Cart, or Past Purchases. This class receives input of a `JFrame` and a `JPanel` from the `LoginGUI` class, and that `JPanel` forms the interface on which the buttons are drawn.
* **Testing:** This class was tested by selecting each button and checking that the correct class was started. The class was also tested by clicking outside of the buttons and checking that no action occurred.
* **Screenshot**: ![TO BE ADDED once icons are complete.](https://i.ibb.co/55hh3Xc/Buyer-Menu-GUI-1-73.png)
### SellerMenuGUI:
* **Description:** This class is analogous in functionality and appearance to the `BuyerMenuGUI` class, except that the following options are presented and a `Seller` object is passed to the next class instead of a `Buyer` object: View My Stores and Items, View Shopping Carts, View Past Sales, View Statistics, and Import/Export Products and Stores.
* **Testing:** Like the `BuyerMenuGUI` class, this class was tested by selecting each button and checking that the correct class was started. The class was also tested by clicking outside of buttons to check that no action would occur.
* **Screenshot:** ![TO BE ADDED once icons are complete.](https://i.ibb.co/KGFXvt0/Seller-Menu-GUI-73.png)
### MarketGUI:
* **Description:** This class allows the user to view the market, purchase items, or add items to their cart. The class also provides searching, sorting, and refresh functionalities for the list of products. Upon selection of a product, a user is presented with the product information (i.e. name, price, store, seller, description, quantity available, and per-order limit) and offered the choice of whether to purchase the product, add the product to their shopping cart, or cancel the transaction. Upon selection of the purchase option, the user is prompted for input of a quantity that is then checked against the per-order limit and available quantity of the product. This class is called from (and returns to upon selection of the back button) the `BuyerMenuGUI` class, and this class also utilizes the `ServerComm` class to request the product list from the `Server` class and to log new purchases and additions to the buyer's shopping cart.
* **Testing:** This class was tested by creating multiple items from various sellers and attempting to purchase them/add them to the shopping cart from the `MarketGUI`. The search functionalities of the class were also tested by using various sample search terms and checking that only relevant results appeared, while the sort functionality was tested by sorting the product list and checking that items appeared in the proper order. In order to test the error-checking functions of the class, invalid inputs (e.g. negative and zero quantities of products) were also used in all inputs.
* **Screenshots:**  
  ![enter image description here](https://i.ibb.co/gRyDZtH/Market-GUI1.png)  
  ![enter image description here](https://i.ibb.co/j4gg0Ps/Market-GUI3.png)  
  ![enter image description here](https://i.ibb.co/VvFDjdV/Market-GUI2.png)
### BuyerShoppingCartGUI:
* **Description:** This class allows buyers to view the products in their shopping cart, remove items from their cart, and buy products from their cart. When a buyer chooses to buy a product from their cart, they are prompted for the quantity they want to purchase (which is checked for compliance with the available and maximum per-order quantities), and the item is removed from their cart. Like `MarketGUI`, this class is called from the `BuyerMenuGUI` class and utilizes the `ServerComm` class to communicate changes in the buyer's shopping cart and new purchases to the `Server` class, which stores the changes in files.
* **Testing:** This class was tested by adding various items to the shopping cart via the `Market` class and checking if they appeared (with accurate information) in the `BuyerShoppingCartGUI` class. Some of these products were then removed or purchased, and the main shopping cart was checked to see if these changes had been properly reflected. In order to test the persistence of changes, the buyer was also logged out and re-logged in to verify that changes made from the shopping cart page had not been lost. Invalid input was also tested by using the "Close" button on the quantity dialog and by trying invalid (i.e. negative, zero, or non-integer) inputs in the quantity to purchase.
* **Screenshots:**  
  ![enter image description here](https://i.ibb.co/zS6VTpt/Shopping-Cart-GUI-73.png)
### BuyerPastPurchasesGUI:
* **Description:** This class allows buyers to view their past purchases. Each purchase is displayed with the name of the item purchased, the total price of the purchase, the quantity purchased, and the name of the store. This class is called from the `BuyerMenuGUI`, and this class calls the `ServerComm` class (which, in turn, communicates over a `Socket` with the `Server` class) in order to get the latest version of the `Buyer` object whose purchases are to be viewed.
* **Testing:** This class was tested by making various purchases from the `MarketGUI` and `BuyerShoppingCartGUI` classes and checking that they were properly reflected on the `BuyerPastPurchasesGUI` page.
* **Screenshot:** ![ADD ONCE COMPLETE](https://i.ibb.co/2y6C5tR/Buyer-Past-Purchases-GUI-73.png)
### DashboardGUI:
* **Description:** This class allows buyers to view a list of all stores they have purchased products from and the quantity of items they have purchased from each store. The class also allows buyers to view a list of all stores that exist and the quantity of items that all users have purchased from each store. This class is called by the `BuyerMenuGUI` class and calls the `ServerComm` class to request the latest dashboard data from the `Server` class via a `Socket`.
* **Testing:** This class was tested by conducting various purchases from multiple user accounts and checking whether the numbers shown on the `DashboardGUI` page properly reflected these purchases (and that the individual buyer's statistics did not reflect purchases made by other buyers).
* **Screenshot:** ![enter image description here](https://i.ibb.co/QcwZ3yD/Seller-Dashboard-GUI-73.png)
### SellerMarketGUI:
* **Description:** This class allows sellers to view all of their stores, create new stores, delete stores, and create, view, edit, and delete products within each store. All of the seller's stores are initially displayed in a list once the class starts, each with buttons to "View/Modify Items in Store" or "Delete Store". If "View/Modify Items in Store" is selected, the user is given a choice of whether to create a new item, view/edit existing items, or to delete an item. If "Delete Store" is selected, the store is immediately deleted, as are all of its products. This class is called by the `SellerMenuGUI` class, and the class calls the `ServerComm` class as necessary to communicate changes in stores and items to the server.
* **Testing:** This class was tested by using it to create various stores and products within those stores, before going back and editing, viewing, and deleting products and stores. In order to verify persistence of data entered via this screen, buyer accounts were used via the `Market` class to verify that changes on this screen actually took effect.
* **Screenshot:**  
  ![enter image description here](https://i.ibb.co/kKgGGFv/Seller-Market-GUI1-73.png)
### SellerShoppingCartGUI:
* **Description:** This class allows sellers to view the placement of their products in buyer shopping carts. For each buyer, this class displays each of the seller's products that are in the buyer's shopping carts (or displays that none of the seller's products are in the buyer's shopping cart). Additionally, the class displays the overall total number of the seller's products that are in all buyer shopping carts. This class is called by the `SellerMenuGUI` class, and the class calls the `ServerComm` class to request the latest version of multiple objects from the `Sever` class over a `Socket` connection.
* **Testing:** This class was tested by adding items to various buyer shopping carts via the relevant classes and then checking whether the items were reflected on the `SellerShoppingCartGUI` page. The class was also tested by adding products from other sellers to buyer shopping carts and verifying that they did not appear on the first seller's `SellerShoppingCartGUI` page.
* **Screenshot:** ![](https://i.ibb.co/K2CsYys/Seller-Shopping-Cart-GUI-73.png)
### SellerSalesGUI:
* **Description:** This class allows sellers to view their past sales to all buyers. Each sale is displayed with the store name, item name, quantity of items purchased, total revenue, and the username of the buyer. The list of sales can also be refreshed using a refresh button. This class is called by the `SellerMenuGUI` class, and this class calls the `ServerComm` class (which make requests via `Socket`s to the `Server` class) to get the latest sales statistics. Only the logged-in seller's sales are displayed in this class.
* **Testing:** This class was tested by using various buyer accounts to buy a seller's products and then checking whether those purchases were properly reflected on the seller's `SellerSalesGUI` page. The class was also tested by purchasing items from other multiple sellers and verifying that each seller's `SellerSalesGUI` page only showed purchases from that specific seller.
* **Screenshot:** ![ADD ONCE COMPLETE](https://i.ibb.co/KXhJsZP/Seller-Sales-GUI-73.png)
### SellerImportExportGUI:
* **Description:** This class allows sellers to export products from and import products to their stores via a CSV file. To select a file to export products to or import products from, the user is shown a `JFileChooser` and the returned file is opened and read from or exported to. If the stores in the CSV import file match stores that the seller actually has, the products will be imported to those stores. Otherwise, new stores will be created to accept the imported products. This class is called by the `SellerMenuGUI` in response to the user selecting the `Import/Export Products and Stores` option, and this class calls methods in the `Server` class via the `ServerComm` class in order to obtain data to export or to save imported data.
* **Testing:** This class was tested by exporting various stores and re-importing them on different devices. This class was also tested by deliberately created improperly-formatted files (e.g. letters in numeric fields) and ensuring that the import would fail.
* **Screenshot:** ![ADD ONCE COMPLETE](https://i.ibb.co/wLWLgNL/Seller-Import-Export-GUI-73.png)
### StatisticsGUI:
* **Description:** This class allows sellers to view summary statistics about sales of their items. Statistics are organized by store, with the class displaying the number of purchases and total revenue for each item in each of a seller's stores and the number of purchases and total revenue from each buyer for each of a seller's stores (representing that buyer's purchases of all products in the store). This class is called from the `SellerMenuGUI` class upon user selection of the `View Statistics` option, and the class calls methods in the `Server` class via `ServerComm` to get the most recent sales statistics.
* **Testing:** This class was tested by creating various stores and items in each store, buying those items from multiple user accounts, and checking that the purchases were properly reflected in the overall seller statistics on the `StatisticsGUI` page.
* **Screenshot:** ![enter image description here](https://i.ibb.co/gMfN1mM/Seller-Statistics-GUI.png)
## Server Classes:
### Server:
* **Description:** This class allows client instances (i.e. instances of the `MenuGUI` class) to access and update the objects and data used throughout the program. This class also performs the program's data persistence functions by immediately saving each change that a client submits to a pair of data files (`buyers.bin` and `sellers.bin`). The `Server` class is organized around a system of commands and responses sent and received via a `ServerSocket`, all running on one thread per client. Each thread receives commands (i.e. integers and input objects in an `Object[]`) via the `ServerSocket` from the clients connected to it, calls the relevant server methods, and returns the relevant data over the `ServerSocket` via an `ObjectOutputStream`. Most client classes access this class via the `ServerComm` class, and this class makes up an integral part of program functionality.
* **Testing:** This class was tested through general use of the program, which relies heavily on the class for nearly all functionality. For example, buyers and sellers were created, logged out, and then re-logged in -- operations that required methods in the server class. In order to test the concurrent elements of this class, IntelliJ's concurrent run feature was used to simultaneously run up to three client instances at once, and the propagation across all instances of changes in any single instance was visually checked.
* **List of Commands: (in lieu of screenshots)**:
```  
Buyer Commands:  
0: Check a login to an existing buyer account  
Input: String[] of email and password  
Output: Buyer object (null if invalid login details)  
1: Create a new buyer account  
Input: String[] of email and password.  
Output: Buyer object  
2: Get market contents  
Input: null object  
Output: ArrayList<Item> of market contents  
3: Update an existing Buyer object  
Input: Buyer object  
Output: null object  
4: Log a sale  
Input: Purchase object  
Output: null object  
5: Get dashboard data  
Input: null object  
Output: Object[] of two ArrayList<Strings> containing dashboard data.  
6: Get the latest version of a buyer object stored on the server  
Input: Buyer object  
Output: Buyer object  
7: Get the list of buyers  
Input: null object  
Output: ArrayList<Buyer> of buyers  
Seller Commands:  
30: Login to an existing seller account  
Input: String[] containing email and password.  
Output: Seller object  
31: Create a new seller  
Input: String[] containing email and password.  
Output: Seller object  
32: Update a Seller object  
Input: Seller object  
Output: null object  
37: Get shopping carts containing a seller's products  
Input: Seller object  
Output: ArrayList<String> of shopping cart information.  
38: Get the seller's past sales  
Input: Seller object  
Output: ArrayList<Purchase> of past sales.  
39: Get the latest server-side version of a Seller object  
Input: Seller object  
Output: Seller object (updated)  
40: Get the seller statistics view data  
Input: Seller object  
Output: Object[] of four arrays: ArrayList<String> of sales statistics by store, ArrayList<String> of sales statistics by buyer, ArrayList<Integer> of number of sales by store, and ArrayList<Integer> of nums of sales by buyer.  
41: Get the seller list  
Input: null object  
Output: ArrayList<Seller> of sellers  
```  
### ServerComm:
* **Description:** This class allows client instances to make commands to the `Server` class by creating a `Socket` to the server and using an `ObjectOutputStream` and an `ObjectInputStream` over that `Socket`. This class is called by most client classes and serves as the exclusive method for other client classes to access and modify data on the server component.
* **Testing:** This class was tested by running numerous commands through it in the course of normal program operation. The class was also tested with sudden disconnects and reconnects to ensure that these potential errors would not result in data loss or a program crash.
* Screenshots are not relevant for this class.
