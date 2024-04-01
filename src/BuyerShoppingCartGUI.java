import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * This class provides a GUI for the buyer's shopping cart.
 *
 * @author D.Mayer, L.Ying, I.Kleimans, J.Chen, S.Hou, Lab Section #3, Team #11
 * @version July 31, 2023
 */

public class BuyerShoppingCartGUI {
    private final Buyer buyer; //buyer from Buyer class
    private final ServerComm sc; //Server communication tool sc from ServerComm class

    /**
     * Constructor for the BuyerShoppingCartGUI class.
     *
     * @param buyer the buyer using this shopping cart GUI.
     */
    public BuyerShoppingCartGUI(Buyer buyer) {
        this.buyer = buyer;
        this.sc = new ServerComm();
        //frame = new JFrame("Shopping Cart");
    }

    /**
     * This method initializes and starts the shopping cart GUI.
     *
     * @param nPanel    the main panel of the GUI
     * @param mainFrame the main frame of the GUI
     */
    public void startShoppingCartGUI(JPanel nPanel, JFrame mainFrame) {
        //mainPanel = new JPanel();
        nPanel.setLayout(new BorderLayout());
        JPanel topPanel = new JPanel(new GridLayout(0, 1));
        JPanel backPanel = new JPanel();
        JButton backButton = new JButton("Back");
        JButton buyAllButton = new JButton("Buy All");

        // Add buttons to the back panel
        backPanel.add(buyAllButton);
        backPanel.add(backButton);
        topPanel.add(backPanel);

        JPanel cartLabelPanel = new JPanel();
        JLabel cartLabel = new JLabel("Shopping Cart:");
        cartLabelPanel.add(cartLabel);
        topPanel.add(cartLabelPanel);
        JPanel cartDataPanel = makeCartPanel(mainFrame, nPanel);
        JPanel view = new JPanel();
        JScrollPane jsp = new JScrollPane();
        jsp.setPreferredSize(new Dimension(1000, 500));
        jsp.setViewportView(view);
        view.add(cartDataPanel);
        nPanel.add(topPanel, BorderLayout.NORTH);
        nPanel.add(jsp, BorderLayout.CENTER);

        // Add action listener to the 'Buy All' button
        buyAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                for (Item item : buyer.getShoppingCart()) {
                    int quantity = 0;
                    while (true) {
                        Object output = JOptionPane.showInputDialog(null,
                                "What quantity of " + item.getName() + " do you want to purchase?",
                                "Store App", JOptionPane.QUESTION_MESSAGE);
                        if (output == null) {
                            return;
                        }
                        try {
                            quantity = Integer.parseInt((String) output);
                            if (quantity > item.getQuantityAvailable() ||
                                    quantity <= 0 ||
                                    (quantity > item.getPerOrderLimit() && item.getPerOrderLimit() != 0)) {
                                throw new RuntimeException("Quantity! " + item.getQuantityAvailable()
                                        + " " + item.getPerOrderLimit());
                            }
                            break;
                        } catch (Exception exc) {
                            JOptionPane.showMessageDialog(null,
                                    "Invalid quantity! Make sure that the quantity you entered" +
                                            " is an integer above 0 and is less than both the quantity available and " +
                                            "the per-purchase limit.", "Store App", JOptionPane.ERROR_MESSAGE);
                        }

                    }
                    try {
                        // Pre-check communications
                        sc.communicateWithServer(3, buyer);
                        // Send it to the server
                        Purchase purchase = new Purchase(item.getPrice(), quantity, item.getName(),
                                item.getDescription(), item.getSeller(), buyer, item.getStore(), item);
                        buyer.addPurchase(purchase);
                        item.setQuantityAvailable(item.getQuantityAvailable() - quantity);
                        sc.communicateWithServer(4, purchase);

                    } catch (Exception exc) {
                        JOptionPane.showMessageDialog(null, "There was a problem" +
                                        " communicating with the server. Try again,", "Store App",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                // Now actually remove the items
                ArrayList<Item> toDelete = new ArrayList<>();
                for (Item item : buyer.getShoppingCart()) {
                    toDelete.add(item);
                }
                for (Item item : toDelete) {
                    buyer.removeFromShoppingCart(item);
                }
                try {
                    sc.communicateWithServer(3, buyer);
                } catch (Exception exc) {
                    JOptionPane.showMessageDialog(null, "There was a problem " +
                                    "communicating your purchase to the server. Try again.", "Store App",
                            JOptionPane.ERROR_MESSAGE);
                }


                nPanel.removeAll();


                JPanel backPanel = new JPanel();
                JButton backButton = new JButton("Back");
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
                JPanel topPanel = new JPanel(new GridLayout(0, 1));

                backPanel.add(backButton);
                topPanel.add(backPanel);
                JPanel cartLabelPanel = new JPanel();
                JLabel cartLabel = new JLabel("Shopping Cart:");
                cartLabelPanel.add(cartLabel);
                topPanel.add(cartLabelPanel);
                JPanel cartDataPanel = makeCartPanel(mainFrame, nPanel);
                JPanel view = new JPanel();
                JScrollPane jsp = new JScrollPane();
                jsp.setPreferredSize(new Dimension(100, 100));
                jsp.setViewportView(view);
                view.add(cartDataPanel);
                nPanel.add(topPanel, BorderLayout.NORTH);
                nPanel.add(jsp, BorderLayout.CENTER);
                mainFrame.repaint();
                mainFrame.setVisible(true);

            }
        });

        // Adding a back button
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

        // nPanel.add(backButton);
    }

    /**
     * This method focused on action within the item inside the Shopping cart, such as "remove" and "buy now" button.
     *
     * @param mainFrame, main frame that used throughout the program.
     * @param nPanel,    buyerPanel from BuyerMenuGUI.
     * @return cartPanel containing newest information.
     */
    public JPanel makeCartPanel(JFrame mainFrame, JPanel nPanel) {
        JPanel cartPanel = new JPanel(new GridLayout(0, 1));
        cartPanel.setBackground(Color.WHITE);
        JPanel upperLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel upperLabel = new JLabel(String.format("%-15s  %-15s %-10s", "Store", "Item Name", "Price"));
        upperLabel.setFont(new Font("Courier New", Font.BOLD, 25));
        upperLabelPanel.add(upperLabel);


        cartPanel.add(upperLabelPanel);
        int items = 0;
        for (Item item : buyer.getShoppingCart()) {
            items++;
            // Create a panel for each item
            JPanel itemPanel = new JPanel();
            //itemPanel.setBorder(new MatteBorder(1, 0, 1, 0, Color.black));
            itemPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            itemPanel.setBackground(Color.WHITE);

            // Adding an item details
            JLabel itemLabel = new JLabel(String.format("%-20s %-20s $%-10.2f", item.getStore().getName(),
                    item.getName(), item.getPrice()));
            itemLabel.setFont(new Font("Courier New", Font.PLAIN, 20));
            itemPanel.add(itemLabel);

            // Adding a button to remove the item
            JButton removeButton = new JButton("Remove");
            JButton buyNowButton = new JButton("Buy Now");
            removeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        buyer.removeFromShoppingCart(item);
                        sc.communicateWithServer(3, buyer);
                    } catch (Exception exc) {
                        buyer.addToShoppingCart(item);
                        JOptionPane.showMessageDialog(null, "Couldn't delete the item!",
                                "Store App", JOptionPane.ERROR_MESSAGE);
                    }
                    // Fix the main screen
                    nPanel.removeAll();

                    JPanel backPanel = new JPanel();
                    JButton backButton = new JButton("Back");
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
                    JPanel topPanel = new JPanel(new GridLayout(0, 1));

                    backPanel.add(backButton);
                    topPanel.add(backPanel);
                    JPanel cartLabelPanel = new JPanel();
                    JLabel cartLabel = new JLabel("Shopping Cart:");
                    cartLabelPanel.add(cartLabel);
                    topPanel.add(cartLabelPanel);
                    JPanel cartDataPanel = makeCartPanel(mainFrame, nPanel);
                    JPanel view = new JPanel();
                    JScrollPane jsp = new JScrollPane();
                    jsp.setPreferredSize(new Dimension(1000, 500));
                    jsp.setViewportView(view);
                    view.add(cartDataPanel);
                    nPanel.add(topPanel, BorderLayout.NORTH);
                    nPanel.add(jsp, BorderLayout.CENTER);
                    mainFrame.repaint();
                    mainFrame.setVisible(true);
                }
            });

            buyNowButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int quantity = 0;
                    while (true) {
                        Object output = JOptionPane.showInputDialog(null,
                                "What quantity of this item do you want to purchase?",
                                "Store App", JOptionPane.QUESTION_MESSAGE);
                        if (output == null) {
                            return;
                        }
                        try {
                            quantity = Integer.parseInt((String) output);
                            if (quantity > item.getQuantityAvailable() || quantity <= 0 ||
                                    (quantity > item.getPerOrderLimit() && item.getPerOrderLimit() != 0)) {
                                throw new RuntimeException("Quantity! " + item.getQuantityAvailable() +
                                        " " + item.getPerOrderLimit());
                            }
                            break;
                        } catch (Exception exc) {
                            JOptionPane.showMessageDialog(null, "Invalid quantity!" +
                                            " Make sure that the quantity you entered is an integer above 0 and is" +
                                            " less than both the quantity available and the per-purchase limit.",
                                    "Store App", JOptionPane.ERROR_MESSAGE);
                        }

                    }
                    try {
                        // Pre-check communications
                        sc.communicateWithServer(3, buyer);
                        // Send it to the server
                        Purchase purchase = new Purchase(item.getPrice(), quantity, item.getName(),
                                item.getDescription(), item.getSeller(), buyer, item.getStore(), item);
                        buyer.addPurchase(purchase);
                        item.setQuantityAvailable(item.getQuantityAvailable() - quantity);
                        buyer.removeFromShoppingCart(item);
                        sc.communicateWithServer(3, buyer);
                        sc.communicateWithServer(4, purchase);

                    } catch (Exception exc) {
                        JOptionPane.showMessageDialog(null, "There was a problem" +
                                        " communicating with the server. Try again,", "Store App",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }


                    // Fix the main screen
                    nPanel.removeAll();


                    JPanel backPanel = new JPanel();
                    JButton backButton = new JButton("Back");
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
                    JPanel topPanel = new JPanel(new GridLayout(0, 1));

                    backPanel.add(backButton);
                    topPanel.add(backPanel);
                    JPanel cartLabelPanel = new JPanel();
                    JLabel cartLabel = new JLabel("Shopping Cart:");
                    cartLabelPanel.add(cartLabel);
                    topPanel.add(cartLabelPanel);
                    JPanel cartDataPanel = makeCartPanel(mainFrame, nPanel);
                    JPanel view = new JPanel();
                    JScrollPane jsp = new JScrollPane();
                    jsp.setPreferredSize(new Dimension(1000, 500));
                    jsp.setViewportView(view);
                    view.add(cartDataPanel);
                    nPanel.add(topPanel, BorderLayout.NORTH);
                    nPanel.add(jsp, BorderLayout.CENTER);
                    mainFrame.repaint();
                    mainFrame.setVisible(true);
                }
            });
            itemPanel.add(removeButton);
            itemPanel.add(buyNowButton);

            // Adding the item panel to the main panel
            cartPanel.add(itemPanel);
        }
        if (items == 0) {
            JLabel nothing = new JLabel("No items in your cart!");
            nothing.setFont(new Font("Courier New", Font.PLAIN, 20));
            cartPanel.add(nothing);
        }
        return cartPanel;
    }

}
