# CS-180-Group-11-Project-5: Test Cases

## Test 1: Concurrency/Updating

### Steps:

* Simultaneously launch two instances of MenuGUI.
* Create a new seller account in one and a new buyer account in the other.
* Add two stores with two items each to the seller account.
* Navigate in the seller view to the “View Past Sales” window.
* Use the buyer account to  purchase 1 unit each of two of the items.
* Use the refresh button to refresh the “View Past Sales” window and check whether the purchases appeared.
* Use the buyer account to add the other two items to the cart.
* Navigate in the seller view to the “View Shopping Carts” window.
* Check that the new additions to the shopping cart are reflected there.
* Navigate in the seller view to the “View my Products and Stores” page.
* Choose one item to edit and append (Edited) to the name and description. Add $1.00 to the price, and add 7 to the available quantity.
* Use the refresh button on the buyer window, and check that the changes are reflected.

### Expected result: 
The buyer's purchases are reflected in the seller's "View Past Purchases" menu, the new cart additions are reflected in the seller's "View Shopping Carts" menu, and the edited product information is reflected on the buyer's market. 

### Test status: 
Passes.


## Test 2: Create an Account

### Steps: 

* Launch one instance of MenuGUI.
* User chooses to create a new seller account.
* User selects the email textbox.
* User enters the email “usertest” via the keyboard.
* User selects the password textbox.
* User enters the password “test” via the keyboard.
* Select the “Create Account” button.
* An Error message appears saying “Invalid email or password. Make sure that your password and username are both at least 6 characters and that your email is valid.”
* User re-enters the email “user@test.com”.
* Select the “Create Account” button.
* An Error message appears saying “Invalid email or password. Make sure that your password and username are both at least 6 characters and that your email is valid.”
* User re-enters the password “testpassword”.
* Select the “Create Account” button and get into the Seller Menu.

### Expected result: 
Application ensures the email and password are following the correct format when creating an account.

### Test status: 
Passes.


## Test 3: Login

### Steps:

* Do the equivalent of Test Case 2 in the Buyer Login page.
* Log out from the Buyer menu using the "Log Out" button.
* Launch a new MenuGUI instance
* Select "Seller"
* Enter the account credentials from step 1 and select login.
* Press "Back"
* Press "Buyer"
* Enter the account credntials from step 1 and select login.
### Expected result: 
Application ensures the user is using the correct account to log in.

### Test status: 
Passes.


## Test 4: Store Editing

### Steps:

* Log into the seller account and select “View Market”.
* Create a new store “Store 1”.
* Choose “View/Modify Items in Store”, and select “edit/view items” in the dropdown list.
* A Message saying “Sorry, you don’t have any items in this store right now. Try creating one.”
* Select “Create an Item” and create 2 items: “apple” and “lemon”.
* Select “edit/view an item” and change the quantity of the apple to “2.9”.
* An error message appears and says “Error! Make sure that all numbers are entered in the correct numerical format.\n Error For input string: ‘2.9’”.
* Change the quantity to “5”.
* Select “delete an item” and choose to delete “lemon”.
* Select the "Delete a Store" option on the line with "Store 1".

### Expected result: 
Application verifies the seller can modify stores and items in the market.

### Test status: 
Passes.


## Test 5: Sort in Buyer Market

### Steps:

* Create a new seller account and create two Stores.
* For Store 1, create two items: apple(price: $4.99; quantity: 9) and banana(price: $5.99; quantity: 5).
* For Store 2, create three items: peach(price: $4.99; quantity: 4), watermelon(price: $7.99; quantity: 5) and orange(price: $4.99; quantity: 8).
* Log out from the seller account, then create a new buyer account.
* Access to “View Market” and change the dropdown list of “Don’t sort” to “price”. The order of items should change to “orange”, “peach”, “apple”, “banana”, “watermelon”.
* Change the dropdown list to “Quantity Available”. The order should change to “peach”, “watermelon”, “banana”, “orange”, “apple”.
* Search for “apple” using the text box beside the “Search” button, only “apple” will appear on the screen.
### Expected result: 
Application ensures the buyer can sort the items in the market by either price or quantity available. 

### Test status: 
Passes.


## Test 6: Search in Buyer Market and Concurrency
### Steps:
* Simultaneously launch buyer and seller instances of the application. 
* Create a new seller account and add a store to it.
* Add three products to the store, each containing a unique keyword.
* Log in to the buyer instance of the application using one of the previously-created buyer accounts.
* Navigate to the "View Market" option.
* Enter the unique keyword into the search bar.
* Select the "Search" button.

### Expected result:
Only results containing the keyword in the product name or product description are shown.

### Test status:
Passed

## Test 7: Import/Export Store Information and Persistence
### Steps:
* Launch a seller instance of the application.
* Log in using the account from Test Case 6.
* Select "Import/Export Products and Stores"
* Select "Export to File"
* Enter the name of a new CSV file.
* Close the seller window.
* Launch a new seller instance of the application.
* Create a new seller account.
* Select "Import/Export Products and Stores".
* Select "Import from File"
* Select (using the JFileChooser) the file exported earlier in the case.
* Log out
* Open a new buyer instance of the application.
* Log in using any existing account.
* Navigate to the market by pressing "View Market"
* View the seller for each product by clicking on it.
### Expected result:
The exported products and stores appear under both the new seller's email and the previous seller's email (one copy under each email).
### Test status:
Passed

## Test 8: Statistics
* Concurrently launch a buyer and seller instance of the application.
* Log into the buyer instance with a pre-existing buyer account.
* Log into the seller instance with a pre-existing seller account.
* Navigate in the buyer instance to the "Dashboard" page. Record (e.g. picture) the data on that page.
* Navigate in the seller instance to the "View Statistics" page. Record (e.g. picture) the data on that page.
* In the buyer instance, press "Back," then press "View Market" to access the market
* Purchase an item sold by the seller logged in on the other instance.
* Return to the seller instance. Press refresh on the statistics view.
* In the buyer instance, press "Back," then press "Dashboard."
### Expected result:
The statistics on the seller page should have changed to reflect the new purchase (remember that quantity is used!). The statistics on the buyer page should have changed to reflect the new purchase.
### Test status:
Passed

## Test 9: Shopping Cart
### Steps:
* Launch a buyer instance of the application.
* Login with an existing account.
* Navigate to the market by pressing "View Market".
* Add the first item to the shopping cart by pressing on it and selecting "Add to Cart".
* Press "Back", then press "Shopping Cart" to view the shopping cart.
* Press "Buy Now" on the line of the item that was just added to the shopping cart.
* Enter a valid quantity.
### Expected result:
The purchase should take place and the item should be removed from the cart.
### Test status:
Passed

## Error Case 1: Seller Side
### Steps:
* Login using an existing seller account.
* Select "View My Stores and Products", then select "Create New Store."
* Press ENTER
* Press "OK" on the popup that appears.
* Enter a valid (i.e. non-blank) character string.
* Go to the newly-created store in the store list and select "View/Modify Items in Store".
* Use the dropdown to select "Create an Item"
* Enter a character string in each field.
* Press "Save"
* Press "OK" on the resulting popup.
* Enter character strings in product name and description, positive integers in quantity and maximum per-order quantity, and a positive double in price.
* Press "Save"
### Expected result:
The product is successfully created only once all fields contain valid input.
### Test status:
Passed

## Error Case 2: Buyer Side
### Steps:
* Launch a buyer instance of the application.
* Create a new buyer account.
* Choose the first item.
* Press "Buy now".
* Enter a negative number on the resulting menu.
* Press "OK"
* Press "OK" on the resulting menu.
* Enter a positive number below the quantity available and per-order quantity limit (if applicable) on the resulting menu.
* Press "OK".
* Press "OK" on the resulting menu.
* Press "Back".
* Press "Past Purchases."
  
### Expected result:
The purchase is successful and os reflected on the past purchases page. (With an "Order Successful" message).
### Test status:
Passed
