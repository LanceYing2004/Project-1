import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This class provides the buyer's GUI for the market, including saving changes to the server
 * component of the program and searching and sorting of products.
 *
 * @author D.Mayer, L.Ying, I.Kleimans, J.Chen, S.Hou, Lab Section #3, Team #11
 * @version July 31, 2023
 */
public class MarketGUI {
    public ServerComm sc; // A ServerComm object to simplify communication with the server component
    private final Buyer buyer; // Buyer object for purchases/cart changes to be logged to.
    private final JPanel buyerPathwayPanel; // JPanel created in BuyerMenuGUI class on which all
    // components are drawn.
    private JFrame mainFrame; // The main JFrame the application is running in.
    // of the application.

    /**
     * Constructor for the MarketGUI class.
     *
     * @param buyer             the buyer viewing the market.
     * @param buyerPathwayPanel the JPanel on which all GUI widgets should be rendered.
     */
    public MarketGUI(Buyer buyer, JPanel buyerPathwayPanel) {
        this.buyer = buyer;
        this.buyerPathwayPanel = buyerPathwayPanel;
        sc = new ServerComm();
    }

    /**
     * Starts the Market GUI part of the program by drawing widgets and adding event listeners.
     *
     * @param mainFrame the main JFrame in which the program GUI is being displayed.
     */
    public void startMarketGUI(JFrame mainFrame) throws IOException, ClassNotFoundException {
        this.mainFrame = mainFrame;
        String[] sortOptions = {
                "Don't Sort", "Price", "Quantity Available"}; // Sort options to display to the user.
        JPanel marketPanel = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel();
        JPanel itemPanel = new JPanel(new BorderLayout());
        JPanel mainPanel = new JPanel();
        JButton backButton = new JButton("Back"); // A "back" button to go to the previous screen.
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel nPanel = new JPanel();
                BuyerMenuGUI bmg = new BuyerMenuGUI(nPanel, buyer);
                bmg.startBuyerMenu(mainFrame);
                mainFrame.setContentPane(nPanel);
                mainFrame.setVisible(true);
            }
        });
        JTextField searchTerms =
                new JTextField(20); // A JTextField for the term the user wants to search for.
        JButton searchButton = new JButton("Search"); // A button to conduct a search.
        JComboBox<String> sortBox =
                new JComboBox<>(sortOptions); // A JComboBox to display the sort options available.
        JButton refreshButton =
                new JButton("Refresh"); // A button to request an instantaneous data refresh.

        // Add to the top panel
        topPanel.add(backButton);
        topPanel.add(searchTerms);
        topPanel.add(searchButton);
        topPanel.add(sortBox);
        topPanel.add(refreshButton);

        // Start by "refreshing" the list
        JList productsList = refreshList(); // The main products list that is displayed.

        if (productsList.getModel().getSize() == 0) {
            mainPanel.add(new JLabel("No results found!"));
        } else {
            productsList.setFont(new Font("Courier New", Font.PLAIN, 20));
            mainPanel.add(productsList);
        }

        // Now, add the refresh listener
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // New server request
                try {
                    // Clear the panel
                    mainPanel.removeAll();
                    JList jl = refreshList(); // The new list that will be displayed post-refresh.
                    // Check for emptiness
                    if (jl.getModel().getSize() == 0) {
                        mainPanel.add(new JLabel("No results found!"));
                    } else {
                        jl.setFont(new Font("Courier New", Font.PLAIN, 20));
                        mainPanel.add(jl);
                    }
                    // Get rid of the search text (refresh overrides search)
                    searchTerms.setText("");
                    // Clear the selection box
                    sortBox.setSelectedIndex(0);
                    // Repaint
                    mainFrame.repaint();
                    mainFrame.setVisible(true);
                } catch (Exception exc) {
                    JOptionPane.showMessageDialog(null,
                            "An error occurred while refreshing. "
                                    + "Try again.",
                            "Store App", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Clear the panel
                    mainPanel.removeAll();
                    // Add the new list
                    JList jl = searchList(
                            searchTerms.getText()); // The JList that will be displayed post-search.
                    // Check for emptiness
                    if (jl.getModel().getSize() == 0) {
                        mainPanel.add(new JLabel("No results found!"));
                    } else {
                        jl.setFont(new Font("Courier New", Font.PLAIN, 20));
                        mainPanel.add(jl);
                    }
                    // Change sort
                    sortBox.setSelectedIndex(0);
                    // Repaint
                    mainFrame.repaint();
                    mainFrame.setVisible(true);
                } catch (Exception exc) {
                    JOptionPane.showMessageDialog(null,
                            "An error occurred while searching. "
                                    + "Try again.",
                            "Store App", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        sortBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String choice =
                        (String) sortBox.getSelectedItem(); // The user's selected sorting option.
                try {
                    // Clear the panel
                    mainPanel.removeAll();
                    // Add the new list
                    if (searchTerms.getText().isEmpty()) {
                        if (choice != null && choice.equals("Price")) {
                            JList jl =
                                    sortList(true); // The new JList that will be displayed post-sort.
                            if (jl.getModel().getSize() == 0) {
                                mainPanel.add(new JLabel("No results found!"));
                            } else {
                                jl.setFont(new Font("Courier New", Font.PLAIN, 20));
                                mainPanel.add(jl);
                            }
                        } else if (choice != null && choice.equals("Quantity Available")) {
                            JList jl =
                                    sortList(false); // The new JList that will be displayed post-sort.
                            if (jl.getModel().getSize() == 0) {
                                mainPanel.add(new JLabel("No results found!"));
                            } else {
                                jl.setFont(new Font("Courier New", Font.PLAIN, 20));
                                mainPanel.add(jl);
                            }
                        } else {
                            JList jl =
                                    refreshList(); // The new JList that will be displayed post-sort.
                            if (jl.getModel().getSize() == 0) {
                                mainPanel.add(new JLabel("No results found!"));
                            } else {
                                jl.setFont(new Font("Courier New", Font.PLAIN, 20));
                                mainPanel.add(jl);
                            }
                        }
                    } else {
                        if (choice != null && choice.equals("Price")) {
                            JList jl = searchAndSort(
                                    searchTerms.getText(), true); // The new JList that will be
                            // displayed post-sort.
                            if (jl.getModel().getSize() == 0) {
                                mainPanel.add(new JLabel("No results found!"));
                            } else {
                                jl.setFont(new Font("Courier New", Font.PLAIN, 20));
                                mainPanel.add(jl);
                            }
                        } else if (choice != null && choice.equals("Quantity Available")) {
                            JList jl = searchAndSort(
                                    searchTerms.getText(), false); // The new JList that will be
                            // displayed post-sort.
                            if (jl.getModel().getSize() == 0) {
                                mainPanel.add(new JLabel("No results found!"));
                            } else {
                                jl.setFont(new Font("Courier New", Font.PLAIN, 20));
                                mainPanel.add(jl);
                            }

                        } else {
                            JList jl = searchList(
                                    searchTerms
                                            .getText()); // The JList that will be displayed post-sort.
                            if (jl.getModel().getSize() == 0) {
                                mainPanel.add(new JLabel("No results found!"));
                            } else {
                                jl.setFont(new Font("Courier New", Font.PLAIN, 20));

                                mainPanel.add(jl);
                            }
                        }
                    }
                    // Repaint
                    mainFrame.repaint();
                    mainFrame.setVisible(true);
                } catch (Exception exc) {
                    JOptionPane.showMessageDialog(null,
                            "There was an error while sorting. "
                                    + "Try again.",
                            "Store App", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JPanel tableLabelPanel = new JPanel(); // JPanel for label to display above the table.
        JLabel tableLabel = new JLabel(String.format("%-20s  %-25s  %-20s %10s", "Store",
                "Item Name", "Price", "Item ID")); // The JLabel to display above the table.
        tableLabel.setFont(new Font("Courier New", Font.PLAIN, 20));
        tableLabelPanel.add(tableLabel);
        itemPanel.add(tableLabelPanel, BorderLayout.NORTH);
        itemPanel.add(mainPanel, BorderLayout.CENTER);
        JScrollPane itemScrollFrame = new JScrollPane(itemPanel);
        itemPanel.setAutoscrolls(true);
        itemScrollFrame.setPreferredSize(new Dimension(1000, 500));
        marketPanel.add(topPanel, BorderLayout.NORTH);
        marketPanel.add(itemScrollFrame, BorderLayout.CENTER);
        buyerPathwayPanel.add(marketPanel);
    }

    /**
     * Refreshes the Market list and returns an updated JList to the caller.
     * .
     */
    public JList refreshList() throws IOException, ClassNotFoundException {
        ArrayList<Item> marketItems =
                (ArrayList<Item>) sc.communicateWithServer(2, null); // ArrayList
        // of all items in the market.
        Item[] items = new Item[marketItems.size()]; // Standard array of items in the market.
        String[] itemStrings = new String[marketItems.size()]; // Strings representing each market
        // item; to be displayed
        // later.
        int cntr = 0; // Counter to put items in the proper location.
        for (Item i : marketItems) {
            items[cntr] = i;
            itemStrings[cntr] = i.toString() + String.format(" %20d", (cntr + 1));
            cntr++;
        }
        JList<String> productsList =
                new JList<String>(itemStrings); // JList of products to display to the user.
        productsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ListListener ll =
                new ListListener(productsList, items, itemStrings, mainFrame, buyer); // ListListener to
        // handle user selections on the list of products.
        productsList.addListSelectionListener(ll);
        return productsList;
    }

    /**
     * Searches the names and descriptions of all products in the markets and returns a JList
     * containing those that match the search term.
     *
     * @param searchTerm the term that the user wants to search for. .
     */
    public JList searchList(String searchTerm) throws IOException, ClassNotFoundException {
        ArrayList<Item> marketItems =
                (ArrayList<Item>) sc.communicateWithServer(2, null); // ArrayList
        // of items in the market for searching purposes.
        Item[] items = new Item[marketItems.size()]; // Standard array of items in the market for
        // searching purposes.
        String[] itemStrings =
                new String[marketItems.size()]; // Array of strings for each item in the market
        // for searching purposes.
        int cntr = 0; // Counter to place values in the correct position.
        for (Item i : marketItems) {
            items[cntr] = i;
            itemStrings[cntr] = i.toString() + String.format(" %20d", (cntr + 1));
            cntr++;
        }
        ArrayList<String> matches = new ArrayList<String>(); // ArrayList of item strings in the
        // market that match the search
        for (int i = 0; i < itemStrings.length; i++) {
            if (itemStrings[i].toLowerCase().contains(searchTerm.toLowerCase())
                    || items[i].getDescription().toLowerCase().contains(searchTerm.toLowerCase())) {
                matches.add(itemStrings[i]);
            }
        }
        String[] matchArr = new String[matches.size()]; // Traditional array of item strings in the
        // market that match
        // the search
        for (int i = 0; i < matches.size(); i++) {
            matchArr[i] = matches.get(i);
        }
        JList<String> productsList =
                new JList<String>(matchArr); // JList to display the matching products in order.
        productsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ListListener ll = new ListListener(
                productsList, items, itemStrings, mainFrame, buyer); // LListListener to
        // allow users to select products from the search.
        productsList.addListSelectionListener(ll);
        return productsList;
    }

    /**
     * Sorts the Market by the requested field and returns a sorted JList.
     *
     * @param price whether or not the user wants to sort by price.
     */
    public JList sortList(boolean price) throws IOException, ClassNotFoundException {
        ArrayList<Item> marketItems =
                (ArrayList<Item>) sc.communicateWithServer(2, null); // ArrayList
        // containing all items in the market.
        Item[] items = new Item[marketItems.size()]; // Traditional array of items in the market.
        String[] itemStrings =
                new String[marketItems.size()]; // String array of item strings for each item in the
        // market.

        int cntr = 0; // Counter to ensure that values are put in the correct locations.
        for (Item i : marketItems) {
            items[cntr] = i;
            itemStrings[cntr] = i.toString() + String.format(" %20d", (cntr + 1));
            cntr++;
        }
        if (price) {
            // Sort by price
            for (int i = 0; i < items.length; i++) {
                for (int j = 0; j < items.length; j++) {
                    if (items[j].getPrice() >= items[i].getPrice()) {
                        Item temp; // Temporary variable for swap.
                        temp = items[j];
                        items[j] = items[i];
                        items[i] = temp;

                        String tempString; // Temporary variable for swap.
                        tempString = itemStrings[j];
                        itemStrings[j] = itemStrings[i];
                        itemStrings[i] = tempString;
                    }
                }
            }
        } else {
            for (int i = 0; i < items.length; i++) {
                for (int j = 0; j < items.length; j++) {
                    if (items[j].getQuantityAvailable() >= items[i].getQuantityAvailable()) {
                        Item temp; // Temporary variable for swap.
                        temp = items[j];
                        items[j] = items[i];
                        items[i] = temp;

                        String tempString; // Temporary variable for swap.
                        tempString = itemStrings[j];
                        itemStrings[j] = itemStrings[i];
                        itemStrings[i] = tempString;
                    }
                }
            }
        }

        JList<String> productsList =
                new JList<String>(itemStrings); // JList displaying sorted product names.
        productsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ListListener ll =
                new ListListener(productsList, items, itemStrings, mainFrame, buyer); // ListListener to
        // handle user selection of products.
        productsList.addListSelectionListener(ll);
        return productsList;
    }

    /**
     * Starts the Market GUI part of the program by drawing widgets and adding event listeners.
     *
     * @param searchTerm the term the user wants to search for.
     * @param price      whether the user wants to sort by price.
     */
    public JList searchAndSort(String searchTerm, boolean price)
            throws IOException, ClassNotFoundException {
        ArrayList<Item> marketItems =
                (ArrayList<Item>) sc.communicateWithServer(2, null); // ArrayList
        // of market items for searching and sorting.
        Item[] items =
                new Item[marketItems.size()]; // Array of item objects for searching and sorting.
        String[] itemStrings = new String[marketItems.size()]; // String array of item strings for
        // searching and sorting.
        int cntr = 0; // Counter to ensure that values are put in the correct locations.
        for (Item i : marketItems) {
            items[cntr] = i;
            itemStrings[cntr] = i.toString() + String.format(" %20d", (cntr + 1));
            cntr++;
        }

        ArrayList<String> matches = new ArrayList<String>(); // ArrayList of matching item strings.
        ArrayList<Item> matchItems = new ArrayList<Item>(); // ArrayList of matching items.
        for (int i = 0; i < itemStrings.length; i++) {
            if (itemStrings[i].toLowerCase().contains(searchTerm.toLowerCase())
                    || items[i].getDescription().toLowerCase().contains(searchTerm.toLowerCase())) {
                matches.add(itemStrings[i]);
                matchItems.add(items[i]);
            }
        }
        String[] matchArr =
                new String[matches.size()]; // Traditional string array of matching items.
        for (int i = 0; i < matches.size(); i++) {
            matchArr[i] = matches.get(i);
        }
        if (price) {
            // Sort by price
            for (int i = 0; i < matchItems.size(); i++) {
                for (int j = 0; j < matchItems.size(); j++) {
                    if (matchItems.get(j).getPrice() >= matchItems.get(i).getPrice()) {
                        Item temp; // Temporary variable for swap.
                        temp = matchItems.get(j);
                        matchItems.set(j, matchItems.get(i));
                        matchItems.set(i, temp);

                        String tempString; // Temporary variable for swap.
                        tempString = matchArr[j];
                        matchArr[j] = matchArr[i];
                        matchArr[i] = tempString;
                    }
                }
            }
        } else {
            for (int i = 0; i < matchItems.size(); i++) {
                for (int j = 0; j < matchItems.size(); j++) {
                    if (matchItems.get(j).getQuantityAvailable()
                            >= matchItems.get(i).getQuantityAvailable()) {
                        Item temp; // Temporary variable for swap.
                        temp = matchItems.get(j);
                        matchItems.set(j, matchItems.get(i));
                        matchItems.set(i, temp);

                        String tempString; // Temporary variable for swap.
                        tempString = matchArr[j];
                        matchArr[j] = matchArr[i];
                        matchArr[i] = tempString;
                    }
                }
            }
        }

        JList<String> productsList =
                new JList<String>(matchArr); // Searched and sorted products JList to display.
        productsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ListListener ll =
                new ListListener(productsList, items, itemStrings, mainFrame, buyer); // A ListListener
        // to handle user selection of products.
        productsList.addListSelectionListener(ll);
        return productsList;
    }
}

/**
 * This class provides a ListSelectionListener so that the program can detect user selecitons on the
 * product list.
 *
 * @author D.Mayer, L.Ying, I.Kleimans, J.Chen, S.Hou, Lab Section #3, Team #11
 * @version July 31, 2023
 */
class ListListener implements ListSelectionListener {
    private final JList
            productsList; // The JList of products on which the ListListener listens for events.
    private final Item[] items; // An Item[] of all the items in the market.
    private final String[] itemStrings; // A String[] of strings for each item in the market.
    private final Buyer buyer; // The buyer object on which operations take place.

    /**
     * Constructor for the ListListener used to handle user selections of products.
     *
     * @param productsList the JList on which the ListListener listens for actions.
     * @param items        an array of items in the market
     * @param itemStrings  an array of strings for the items in the market
     * @param mainFrame    the main frame in which the program is displayed.
     * @param buyer        the buyer who is involved in the list events.  .
     */
    public ListListener(
            JList productsList, Item[] items, String[] itemStrings, JFrame mainFrame, Buyer buyer) {
        this.productsList = productsList;
        this.items = items;
        this.itemStrings = itemStrings;
        this.buyer = buyer;
    }

    /**
     * Interface method to handle handle specific selections on the list.
     *
     * @param e The specific user selection on the JList.
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        ServerComm sc = new ServerComm();
        if (!e.getValueIsAdjusting() && productsList.getSelectedValue() != null) {
            Item currentItem = null; // The current item for loop checking purposes.
            for (int i = 0; i < itemStrings.length; i++) {
                if (productsList.getSelectedValue().equals(itemStrings[i])) {
                    currentItem = items[i];
                }
            }
            if (currentItem == null) {
                return;
            }
            int choice = JOptionPane.showOptionDialog(null,
                    "Product Details:\n" + currentItem.productPageText(),
                    "Store App", 0, 0,
                    new ImageIcon("./src/Placeholder.png"),
                    new String[]{"Close", "Add to Cart", "Buy Now"},
                    "Close"); // The user's selected option.
            if (choice == 1) {
                boolean inCart =
                        false; // Boolean representing whether the item is already in the buyer's cart.
                for (Item i : buyer.getShoppingCart()) {
                    if (i.getName().equals(currentItem.getName())
                            && i.getStore().getName().equals(currentItem.getStore().getName())) {
                        inCart = true;
                        break;
                    }
                }
                if (inCart) {
                    JOptionPane.showMessageDialog(null, "That product is already in your cart!",
                            "Store App", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                try {
                    buyer.addToShoppingCart(currentItem);
                    sc.communicateWithServer(3, buyer);
                } catch (Exception exc) {
                    buyer.removeFromShoppingCart(currentItem);
                    JOptionPane.showMessageDialog(null,
                            "Error communicating with the server. "
                                    + "Please try to add your item to the cart again.",
                            "Store App", JOptionPane.ERROR_MESSAGE);
                }
            } else if (choice == 2) {
                // Get a quantity
                int quantity; // The quantity the user wants to purchase.
                while (true) {
                    try {
                        String quantityString = JOptionPane.showInputDialog(null,
                                "What quantity of " + currentItem.getName() + " do you want to buy?",
                                "Store App", JOptionPane.QUESTION_MESSAGE);
                        if (quantityString == null) {
                            productsList.setSelectedValue(null, false);
                            return; // The user selected "cancel" or "close"
                        }
                        quantity = Integer.parseInt(quantityString);
                    } catch (Exception exc) {
                        // Must not have entered an integer
                        JOptionPane.showMessageDialog(null,
                                "Error: Enter an integer for the "
                                        + "product quantity.",
                                "Store App", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }
                    if (quantity == 0) {
                        JOptionPane.showMessageDialog(null,
                                "Error: Enter a non-zero intege"
                                        + "r for the quantity.",
                                "Store App", JOptionPane.ERROR_MESSAGE);

                    } else if (quantity < 0) {
                        JOptionPane.showMessageDialog(null,
                                "Error: Enter a positive numbe"
                                        + "r for the quantity.",
                                "Store App", JOptionPane.ERROR_MESSAGE);
                    } else if (quantity > currentItem.getQuantityAvailable()
                            || (quantity > currentItem.getPerOrderLimit()
                            && currentItem.getPerOrderLimit() != 0)) {
                        JOptionPane.showMessageDialog(null,
                                "Error: That quantity exceeded"
                                        + " the amount available or the per-purchase limit. Try again.",
                                "Store App", JOptionPane.ERROR_MESSAGE);
                    } else {

                        break;
                    }
                }
                Purchase purchase = new Purchase(currentItem.getPrice(), quantity,
                        currentItem.getName(), currentItem.getDescription(), currentItem.getSeller(),
                        buyer, currentItem.getStore(),
                        currentItem); // Purchase object for the current purchase.
                buyer.addPurchase(purchase);
                try {
                    sc.communicateWithServer(4, purchase);
                    sc.communicateWithServer(3, buyer);
                    currentItem.setQuantityAvailable(currentItem.getQuantityAvailable() - quantity);
                    JOptionPane.showMessageDialog(null, "Order Successful!", "Store App",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,
                            "Error communicating with the server."
                                    + " Please try your purchase again.",
                            "Store App", JOptionPane.ERROR_MESSAGE);
                }
            }
            productsList.setSelectedValue(null, false);
        }
    }
}
