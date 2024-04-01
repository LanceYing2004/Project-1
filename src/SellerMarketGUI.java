import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A class that would allow seller to view their markets.
 *
 * @author D.Mayer, L.Ying, I.Kleimans, J.Chen, S.Hou, Lab Section #3, Team #11
 * @version July 31, 2023
 */

public class SellerMarketGUI {

    ServerComm sc; //Server communication tool
    private final Seller seller; //Seller from seller class
    private final JPanel sellerPathwayPanel; //This panel "flows" through all seller-pathway steps. At each step, a new panel
    // is created and passed as this parameter.
    private JFrame mainFrame; //main frame that is used throughout the program


    /**
     * SellerMarketGUI constructor.
     *
     * @param seller             from Seller class
     * @param sellerPathwayPanel This panel "flows" through all seller-pathway steps
     */
    public SellerMarketGUI(Seller seller, JPanel sellerPathwayPanel) {
        this.seller = seller;
        this.sellerPathwayPanel = sellerPathwayPanel;
        sc = new ServerComm();

    }

    /**
     * Begin the SellerMarketGUI process.
     *
     * @param mainFrame the mainframe that is used throughout the program.
     */
    public void startSellerMarketGUI(JFrame mainFrame) {
        this.mainFrame = mainFrame;


        // Using border layout for top back/search/sort and main content
        JPanel marketPanel = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel();
        JPanel itemPanel = new JPanel(new BorderLayout());
        JPanel mainPanel = new JPanel(new BorderLayout());
        // TESTING
        // Label for stores block
        JPanel storesLabelPanel = new JPanel();
        JLabel yourStores = new JLabel("Your Stores:");
        storesLabelPanel.add(yourStores);
        storesLabelPanel.setBackground(Color.WHITE);
        mainPanel.add(storesLabelPanel, BorderLayout.NORTH);
        mainPanel.add(getStoresPanel(mainPanel), BorderLayout.CENTER);

        // TESTING

        // Top Panel
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel nPanel = new JPanel();
                SellerMenuGUI smg = new SellerMenuGUI(nPanel, seller);
                smg.startSellerMenu(mainFrame);
                mainFrame.setContentPane(nPanel);
                mainFrame.setVisible(true);
            }
        });


        JButton storeButton = new JButton("Create a Store");
        storeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                while (true) {
                    Object jOptionPaneObject = JOptionPane.showInputDialog(null,
                            "What is the name of the store?");
                    String storeName = "";
                    if (jOptionPaneObject != null) {
                        storeName = (String) jOptionPaneObject;
                        if (!storeName.isEmpty()) {
                            Store newStore = new Store(storeName, seller);
                            try {
                                seller.addStore(newStore);
                                sc.communicateWithServer(32, seller);
                            } catch (Exception exc) {
                                seller.deleteStore(newStore);
                                JOptionPane.showMessageDialog(null,
                                        "Error: Problem communicating with server. Try again.",
                                        "Store App", JOptionPane.ERROR_MESSAGE);
                                continue;
                            }
                            break;
                        } else {
                            JOptionPane.showMessageDialog(null,
                                    "Error: The store name can't be blank.", "Store App",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        break;
                    }
                }
                // Update the store list to account for the change
                mainPanel.removeAll();
                JPanel storesLabelPanel = new JPanel();
                JLabel yourStores = new JLabel("Your Stores:");
                storesLabelPanel.add(yourStores);
                storesLabelPanel.setBackground(Color.WHITE);
                mainPanel.add(storesLabelPanel, BorderLayout.NORTH);
                mainPanel.add(getStoresPanel(mainPanel), BorderLayout.CENTER);
                mainFrame.setVisible(true);

            }

        });


        // add to the top panel
        topPanel.add(backButton);
        //topPanel.add(itemButton);
        topPanel.add(storeButton);
        marketPanel.add(topPanel, BorderLayout.NORTH);
        marketPanel.setBackground(Color.WHITE);
        JScrollPane itemScrollFrame = new JScrollPane();
        JPanel view = new JPanel();
        itemScrollFrame.setPreferredSize(new Dimension(1000, 500));
        itemScrollFrame.setViewportView(view);
        view.add(mainPanel);
        view.setBackground(Color.WHITE);
        marketPanel.add(itemScrollFrame, BorderLayout.CENTER);

        sellerPathwayPanel.add(marketPanel);

    }

    /**
     * Allowing seller to view/edit the item.
     *
     * @param item from Item class
     */
    private void viewEditItem(Item item) {
        // View/Edit Item
        // Show a JOptionPane
        JTextField nameField = new JTextField(item.getName(), 20);
        JTextField priceField = new JTextField(String.valueOf(item.getPrice()), 20);
        JTextArea descriptionField = new JTextArea(item.getDescription(), 3, 20);
        JTextField quantityField = new JTextField(String.valueOf(item.getQuantityAvailable()), 20);
        JTextField perOrderLimitField = new JTextField((item.getPerOrderLimit() == 0) ? "" :
                String.valueOf(item.getPerOrderLimit()), 10);
        Object[] message = {
                "Product Name:", nameField,
                "Price:", priceField,
                "Description:", descriptionField,
                "Quantity Available:", quantityField,
                "Maximum Per-Order Quantity (Optional):", perOrderLimitField,
        };
        String[] options = {"Save", "Cancel"};
        int option;// = JOptionPane.showOptionDialog(null, message, "Store App",
        // JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);
        while (true) {
            option = JOptionPane.showOptionDialog(null, message, "Store App",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);
            if (option == 0) {
                // Now, do some error checking
                double price = 0.00;
                int quantityAvailable = 0;
                String name = "";
                String description = "";
                int quantityLimit = 0;
                try {
                    if (priceField.getText().isEmpty()) {
                        throw new RuntimeException("Price can't be empty.");
                    }
                    if (quantityField.getText().isEmpty()) {
                        throw new RuntimeException("Quantity can't be empty.");
                    }
                    if (nameField.getText().isEmpty()) {
                        throw new RuntimeException("Name can't be empty.");
                    }
                    if (descriptionField.getText().isEmpty()) {
                        throw new RuntimeException("Description can't be empty.");
                    }
                    if (perOrderLimitField.getText().isEmpty()) {
                        quantityLimit = 0;
                    } else {
                        quantityLimit = Integer.parseInt(perOrderLimitField.getText());
                        if (quantityLimit == 0) {
                            throw new RuntimeException("Per-order quantity limit cannot be 0." +
                                    " Leave the field blank for unlimited.");
                        }
                    }
                    price = Double.parseDouble(priceField.getText());
                    name = nameField.getText();
                    description = descriptionField.getText();
                    quantityAvailable = Integer.parseInt(quantityField.getText());
                    if (price < 0 || quantityAvailable < 0 || quantityLimit < 0) {
                        throw new RuntimeException("Price, quantity, and per-order limit cannot be less than 0.");
                    }
                    item.setName(name);
                    item.setDescription(description);
                    item.setPrice(price);
                    item.setQuantityAvailable(quantityAvailable);
                    item.setPerOrderLimit(quantityLimit);
                    // Send to server
                    sc.communicateWithServer(32, seller);
                    JOptionPane.showMessageDialog(null, "Product updated successfully!",
                            "Store App", JOptionPane.INFORMATION_MESSAGE);
                    break;
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null, "Error!" +
                            " Make sure that all numbers are entered in the correct numerical format. \nError "
                            + nfe.getMessage() + ".", "Store App", JOptionPane.ERROR_MESSAGE);
                } catch (IOException ioe) {
                    JOptionPane.showMessageDialog(null, "Server Error!" +
                                    " Your changes were not saved successfully. Try again.", "Store App",
                            JOptionPane.ERROR_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error! " + e.getMessage(),
                            "Store App", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                return;
            }
        }
    }

    /**
     * Allowing the user to create item from a store.
     *
     * @param store from Store class. Use to determine which store is item is located.
     */
    private void createItem(Store store) {
        JTextField nameField = new JTextField(20);
        JTextField priceField = new JTextField(20);
        JTextArea descriptionField = new JTextArea(3, 20);
        JTextField quantityField = new JTextField(20);
        JTextField perOrderLimitField = new JTextField(10);
        Object[] message = {
                "Product Name:", nameField,
                "Price:", priceField,
                "Description:", descriptionField,
                "Quantity Available:", quantityField,
                "Maximum Per-Order Quantity (Optional):", perOrderLimitField,
        };
        String[] options = {"Save", "Cancel"};
        int option;// = JOptionPane.showOptionDialog(null, message, "Store App",
        // JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);
        while (true) {
            option = JOptionPane.showOptionDialog(null, message, "Store App",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);
            if (option == 0) {
                // Now, do some error checking
                double price = 0.00;
                int quantityAvailable = 0;
                String name = "";
                String description = "";
                int quantityLimit = 0;
                Item newItem = null;
                try {
                    if (priceField.getText().isEmpty()) {
                        throw new RuntimeException("Price can't be empty.");
                    }
                    if (quantityField.getText().isEmpty()) {
                        throw new RuntimeException("Quantity can't be empty.");
                    }
                    if (nameField.getText().isEmpty()) {
                        throw new RuntimeException("Name can't be empty.");
                    }
                    if (descriptionField.getText().isEmpty()) {
                        throw new RuntimeException("Description can't be empty.");
                    }
                    if (perOrderLimitField.getText().isEmpty()) {
                        quantityLimit = 0;
                    } else {
                        quantityLimit = Integer.parseInt(perOrderLimitField.getText());
                        if (quantityLimit == 0) {
                            throw new RuntimeException("Per-order quantity limit cannot be 0." +
                                    " Leave the field blank for unlimited.");
                        }
                    }
                    price = Double.parseDouble(priceField.getText());
                    name = nameField.getText();
                    description = descriptionField.getText();
                    quantityAvailable = Integer.parseInt(quantityField.getText());
                    if (price < 0 || quantityAvailable < 0 || quantityLimit < 0) {
                        throw new RuntimeException("Price, quantity, and per-order limit cannot be less than 0.");
                    }
                    newItem = new Item(name, description, seller, price, quantityAvailable, quantityLimit, store);
                    store.addNewItem(newItem);
                    // Send to server
                    sc.communicateWithServer(32, seller);
                    JOptionPane.showMessageDialog(null, "Product updated successfully!",
                            "Store App", JOptionPane.INFORMATION_MESSAGE);
                    break;
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null,
                            "Error! Make sure that all numbers are entered in the correct numerical format." +
                                    " \nError " + nfe.getMessage() + ".", "Store App", JOptionPane.ERROR_MESSAGE);
                } catch (IOException ie) {
                    store.removeItem(newItem);
                    JOptionPane.showMessageDialog(null, "Server error! Try again.",
                            "Store App", JOptionPane.ERROR_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error! " + e.getMessage(),
                            "Store App", JOptionPane.ERROR_MESSAGE);

                }
            } else {
                return;
            }
        }
    }

    /**
     * View/Edit/Create all the stores available.
     *
     * @param mainPanel the panel that is used throughout the program
     * @return
     */
    private JPanel getStoresPanel(JPanel mainPanel) {
        JPanel allStoresPanel = new JPanel(new GridLayout(0, 1));
        allStoresPanel.setBackground(Color.WHITE);
        // items options
        String[] itemOptions = {"Edit/View Items", "Create an Item", "Delete an Item"};

        int cntr = 0;
        if (seller.getStores().size() > 0) {
            for (Store s : seller.getStores()) {
                JPanel storePanel = new JPanel(new FlowLayout());
                JLabel storeName = new JLabel(String.format("%-20s", s.getName()));
                storeName.setFont(new Font("Courier New", Font.BOLD, 30));
                JButton viewEditItemsInStore = new JButton("View/Modify Items in Store");
                viewEditItemsInStore.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Get the list of items in the store
                        ArrayList<Item> itemList = s.getItems();
                        String[] itemNames = new String[itemList.size()];
                        for (int i = 0; i < itemList.size(); i++) {
                            itemNames[i] = itemList.get(i).getName();
                        }
                        Object itemsObj;
                        String items;
                        itemsObj = JOptionPane.showInputDialog(null, "What do you want to do?",
                                "Item Modification",
                                JOptionPane.QUESTION_MESSAGE, null, itemOptions, itemOptions[0]);
                        if (itemsObj == null) {
                            return;
                        } else {
                            items = (String) itemsObj;
                        }
                        if (items.equals("Edit/View Items")) {
                            if (itemNames.length == 0) {
                                JOptionPane.showMessageDialog(null, "Sorry," +
                                                " you don't have any items in this store right now. Try creating one.",
                                        "Store App", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                String edit;
                                edit = (String) JOptionPane.showInputDialog(null, null,
                                        "Edit Items", JOptionPane.QUESTION_MESSAGE,
                                        null, itemNames, itemNames[0]);
                                // Match the item to be edited
                                Item toEdit = null;
                                for (Item i : s.getItems()) {
                                    if (i.getName().equals(edit)) {
                                        toEdit = i;
                                    }
                                }
                                if (toEdit != null) {
                                    viewEditItem(toEdit);
                                } else {
                                }
                            }
                        } else if (items.equals("Create an Item")) {
                            createItem(s);
                        } else if (items.equals("Delete an Item")) {
                            Object deleteResult;
                            deleteResult = JOptionPane.showInputDialog(null,
                                    "Which item do you want to delete?",
                                    "Delete Items", JOptionPane.QUESTION_MESSAGE, null,
                                    itemNames, itemNames[0]);
                            if (deleteResult == null) {
                                return;
                            }
                            String deleteString = (String) deleteResult;
                            // Match the item
                            Item toDelete = null;
                            for (Item i : s.getItems()) {
                                if (i.getName().equals(deleteString)) {
                                    toDelete = i;
                                }
                            }
                            if (toDelete != null) {
                                int result = JOptionPane.showConfirmDialog(null,
                                        "Are you sure you want to delete '" + toDelete.getName() + "'?",
                                        "Confirm Deletion",
                                        JOptionPane.YES_NO_OPTION);
                                if (result == JOptionPane.YES_OPTION) {
                                    s.removeItem(toDelete);
                                }
                            }
                            try {
                                sc.communicateWithServer(32, seller);
                                JOptionPane.showMessageDialog(null,
                                        "'" + deleteString + "' has been deleted from this store.",
                                        "Deletion Successful",
                                        JOptionPane.INFORMATION_MESSAGE);
                            } catch (Exception exc) {
                                s.addNewItem(toDelete);
                                JOptionPane.showMessageDialog(null,
                                        "Couldn't delete the product.", "Store App",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                });

                JButton deleteStore = new JButton("Delete Store");
                deleteStore.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            int result = JOptionPane.showConfirmDialog(null,
                                    "Are you sure you want to delete '" + s.getName() + "'?",
                                    "Confirm Deletion",
                                    JOptionPane.YES_NO_OPTION);
                            if (result == JOptionPane.YES_OPTION) {
                                seller.deleteStore(s);
                            }
                            sc.communicateWithServer(32, seller);
                            JOptionPane.showMessageDialog(null,
                                    "'" + s + "' has been deleted.",
                                    "Store App",
                                    JOptionPane.INFORMATION_MESSAGE);
                        } catch (Exception exc) {
                            seller.addStore(s);
                            JOptionPane.showMessageDialog(null, "Couldn't delete the store!",
                                    "Store App", JOptionPane.ERROR_MESSAGE);
                        }
                        // Fix the main screen
                        mainPanel.removeAll();
                        JPanel storesLabelPanel = new JPanel();
                        JLabel yourStores = new JLabel("Your Stores:");
                        storesLabelPanel.setBackground(Color.WHITE);
                        storesLabelPanel.add(yourStores);
                        mainPanel.add(storesLabelPanel, BorderLayout.NORTH);
                        mainPanel.add(getStoresPanel(mainPanel), BorderLayout.CENTER);
                        mainFrame.setVisible(true);
                    }
                });


                storePanel.add(storeName);
                storePanel.add(viewEditItemsInStore);
                storePanel.add(deleteStore);
                if (++cntr != seller.getStores().size()) {
                    //storePanel.setBorder(new MatteBorder(1, 0, 1, 0, Color.black));
                }
                storePanel.setPreferredSize(new Dimension(1000, 50));
                storePanel.setBackground(Color.WHITE);
                allStoresPanel.add(storePanel);
            }
            return allStoresPanel;
        } else {
            JPanel noStoresPanel = new JPanel();
            JLabel noStores = new JLabel("You don't have any stores yet!");
            noStoresPanel.add(noStores);
            noStoresPanel.setBackground(Color.WHITE);
            return noStoresPanel;
        }
    }

}