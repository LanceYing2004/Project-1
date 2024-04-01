import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * GUI for Seller's Shopping Cart.
 *
 * @author D.Mayer, L.Ying, I.Kleimans, J.Chen, S.Hou, Lab Section #3, Team #11
 * @version July 31, 2023
 */
public class SellerShoppingCartGUI {
    private JFrame frame;
    private JPanel mainPanel;
    private JButton backButton;
    private final Seller seller;
    private final ServerComm sc;

    /**
     * Constructor for SellerShoppingCartGUI
     *
     * @param seller The seller object
     */
    public SellerShoppingCartGUI(Seller seller) {
        this.seller = seller;
        sc = new ServerComm();
    }

    /**
     * Start the shopping cart GUI
     *
     * @param nPanel    the JPanel to which the shopping cart UI will be added
     * @param mainFrame the main JFrame
     */
    public void startShoppingCartGUI(JPanel nPanel, JFrame mainFrame) {
        nPanel.setLayout(new BorderLayout());
        JPanel topPanel = new JPanel();


        // Create and initialize components
        JPanel backPanel = new JPanel(new GridLayout(0, 1));
        JPanel buttonsPanel = new JPanel(new FlowLayout());
        JButton backButton = new JButton("Back");
        JButton refreshButton = new JButton("Refresh");
        buttonsPanel.add(backButton);
        buttonsPanel.add(refreshButton);

        JPanel labelPanel = new JPanel();
        JLabel cartLabel = new JLabel("Buyer Shopping Carts:");
        labelPanel.add(cartLabel);

        backPanel.add(buttonsPanel);
        backPanel.add(labelPanel);

        // Create cart data panel
        JPanel cartDataPanel = makeCartPanel(mainFrame, nPanel);

        // Create and initialize scroll pane
        JPanel view = new JPanel();
        JScrollPane jsp = new JScrollPane();
        jsp.setPreferredSize(new Dimension(1000, 500));
        jsp.setViewportView(view);
        view.add(cartDataPanel);

        nPanel.add(backPanel, BorderLayout.NORTH);
        nPanel.add(jsp, BorderLayout.CENTER);

        //Back Button
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

        //Refresh Button
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JPanel cartDataPanel = makeCartPanel(mainFrame, nPanel);

                    nPanel.removeAll();
                    JPanel view = new JPanel();
                    JScrollPane jsp = new JScrollPane();
                    jsp.setPreferredSize(new Dimension(1000, 500));
                    jsp.setViewportView(view);
                    view.add(cartDataPanel);
                    view.setBackground(Color.WHITE);
                    nPanel.add(backPanel, BorderLayout.NORTH);
                    nPanel.add(jsp, BorderLayout.CENTER);
                    mainFrame.repaint();
                    mainFrame.setVisible(true);
                } catch (Exception exc) {
                    JOptionPane.showMessageDialog(null, "Connection Error! " +
                            "Check your connection and try again.", "Store App", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    /**
     * Create the panel for the shopping cart
     *
     * @param mainFrame the main JFrame
     * @param nPanel    the JPanel to which the shopping cart UI will be added
     * @return JPanel for the shopping cart
     */
    public JPanel makeCartPanel(JFrame mainFrame, JPanel nPanel) {
        JPanel cartPanel = new JPanel(new GridLayout(0, 1));

        // Communicate with the server to get the buyers list
        ArrayList<Buyer> buyers;
        try {
            buyers = (ArrayList<Buyer>) sc.communicateWithServer(7, null);
        } catch (Exception e) {
            throw new RuntimeException("Connection error!");
        }

        // Loop through the buyers and create panels for each
        int buyerCounter = 0;
        int productsInAllCarts = 0;
        for (Buyer buyer : buyers) {
            JPanel buyerPanel = new JPanel();
            if (buyerCounter != 0) {
                buyerPanel.setBorder(new MatteBorder(2, 0, 0, 0, Color.black));
            }
            buyerCounter++;

            buyerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

            // Adding an item details
            JLabel buyerLabel = new JLabel("<html> <b> Buyer: " + buyer.getEmail() + " </b> </html>");
            buyerPanel.add(buyerLabel);
            buyerPanel.setPreferredSize(new Dimension(1000, 50));
            buyerLabel.setFont(new Font("Courier New", Font.BOLD, 25));
            buyerPanel.setBackground(Color.WHITE);

            // Adding the item panel to the main panel
            cartPanel.add(buyerPanel);

            // Loop through the items in the buyer's shopping cart and create panels for each
            int itemCntr = 0;
            for (Item item : buyer.getShoppingCart()) {
                if (item.getSeller().getEmail().equals(seller.getEmail())) {
                    productsInAllCarts++;
                    
                    // Create a panel for each item
                    JPanel itemPanel = new JPanel();
                    //itemPanel.setBorder(new MatteBorder(1, 0, 1, 0, Color.black));
                    itemPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

                    // Adding an item details
                    JLabel itemLabel = new JLabel("- " + item.getName() + " from " +
                            item.getStore().getName() + " ($" + item.getPrice() + ")");
                    itemLabel.setFont(new Font("Courier New", Font.PLAIN, 20));

                    itemPanel.add(itemLabel);
                    itemPanel.setPreferredSize(new Dimension(1000, 50));
                    itemPanel.setBackground(Color.WHITE);

                    // Adding the item panel to the main panel
                    cartPanel.add(itemPanel);
                    itemCntr++;
                }
            }

            // If no items were added, add a panel indicating that no products are in the buyer's cart
            if (itemCntr == 0) {
                JPanel itemPanel = new JPanel();
                itemPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                JLabel itemLabel = new JLabel("None of your products are in this buyer's cart!");
                itemLabel.setFont(new Font("Courier New", Font.PLAIN, 20));
                itemPanel.add(itemLabel);
                itemPanel.setPreferredSize(new Dimension(1000, 50));
                itemPanel.setBackground(Color.WHITE);
                cartPanel.add(itemPanel);
            }
        }

        // Add a final panel to display the total number of products in all shopping carts
        JPanel allCartsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel allCartsLabel = new JLabel("<html><b>Total: " + productsInAllCarts +
                " items in all carts.</b></html<");
        allCartsPanel.setPreferredSize(new Dimension(1000, 50));
        allCartsLabel.setFont(new Font("Courier New", Font.BOLD, 25));
        allCartsPanel.setBackground(Color.WHITE);
        allCartsPanel.add(allCartsLabel);
        allCartsPanel.setBorder(new MatteBorder(2, 0, 0, 0, Color.black));
        cartPanel.add(allCartsPanel);

        return cartPanel;
    }
}
