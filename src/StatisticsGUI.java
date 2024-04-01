import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * A class that displays Buyer's Past Purchases via GUI.
 *
 * @author D.Mayer, L.Ying, I.Kleimans, J.Chen, S.Hou, Lab Section #3, Team #11
 * @version July 31, 2023
 */

public class StatisticsGUI extends Component {

    JButton backButton; //back button that is used to return to the previous menu page
    ServerComm sc; //server communication tool
    //Fields
    private Seller seller; //seller from Seller class
    private final JPanel sellerPanel; //This panel "flows" through all seller-pathway steps. At each step, a new panel
    // is created and passed as this parameter.
    private JFrame mainFrame; //main frame that is used throughout the program


    /**
     * StatisticsGUI constructor.
     *
     * @param seller      from Seller class
     * @param sellerPanel This panel "flows" through all seller-pathway steps.
     */
    public StatisticsGUI(Seller seller, JPanel sellerPanel) {
        new BorderLayout();
        this.seller = seller;
        this.sellerPanel = sellerPanel;
        sc = new ServerComm();
    }

    /**
     * Begin the SellerMarketGUI process.
     *
     * @param mainFrame main frame that is used throughout the program
     */
    public void startStatGUI(JFrame mainFrame) {
        this.mainFrame = mainFrame;
        sellerPanel.setLayout(new BorderLayout());

        JPanel statPanel = new JPanel(new GridLayout(0, 1));
        JPanel topPanel = new JPanel();


        //Back Button
        backButton = new JButton("<Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                back();
            }
        });
        JButton sort = new JButton("Sort Statistics");
        JButton refreshButton = new JButton("Refresh");
        sort.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sellerPanel.removeAll();
                JScrollPane jsp = new JScrollPane();
                JPanel view = new JPanel();
                jsp.setViewportView(view);
                jsp.setPreferredSize(new Dimension(1000, 700));

                JPanel listPanel = getSortedListPanel();
                view.add(listPanel);

                sellerPanel.add(topPanel, BorderLayout.NORTH);
                sellerPanel.add(jsp, BorderLayout.CENTER);
                mainFrame.repaint();
                mainFrame.setVisible(true);
            }
        });

        //Refresh Button
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sellerPanel.removeAll();
                JScrollPane jsp = new JScrollPane();
                JPanel view = new JPanel();
                jsp.setViewportView(view);
                jsp.setPreferredSize(new Dimension(1000, 700));

                JPanel listPanel = getSortedListPanel();
                view.add(listPanel);

                sellerPanel.add(topPanel, BorderLayout.NORTH);
                sellerPanel.add(jsp, BorderLayout.CENTER);
                mainFrame.repaint();
                mainFrame.setVisible(true);
            }
        });

        topPanel.add(backButton);
        topPanel.add(refreshButton);
        topPanel.add(sort);


        //sellerPanel.add(topPanel);
        JScrollPane jsp = new JScrollPane();
        JPanel view = new JPanel();
        jsp.setViewportView(view);
        jsp.setPreferredSize(new Dimension(1000, 700));

        JPanel listPanel = getListPanel();
        view.add(listPanel);

        sellerPanel.add(topPanel, BorderLayout.NORTH);
        sellerPanel.add(jsp, BorderLayout.CENTER);

    }

    /**
     * Sort the panel by number of sales.
     *
     * @return the sorted panel
     */
    public JPanel getSortedListPanel() {
        // Sorting will be by NUMBER OF SALES
        // Update the seller first
        try {
            seller = (Seller) sc.communicateWithServer(39, seller);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Connection error! Check yo" +
                    "ur connection and try again.", "Store App", JOptionPane.ERROR_MESSAGE);
        }

        // To be returned
        JPanel salesListTable = new JPanel(new GridLayout(0, 1));
        // By Buyer
        for (Store s : seller.getStores()) {
            JPanel storePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel storeLabel = new JLabel("Store: " + s.getName());
            storeLabel.setFont(new Font("Courier New", Font.BOLD, 20));
            storePanel.add(storeLabel);
            salesListTable.add(storePanel);
            ArrayList<Buyer> buyers = new ArrayList<>();

            for (Purchase p : s.getSales()) {
                if (!buyers.contains(p.getBuyer())) {
                    buyers.add(p.getBuyer());
                }
            }
            // Now, sort the buyers list by number of sales
            buyers = sortBuyerList(buyers);

            for (Buyer b : buyers) {
                double totalSales = 0.00;
                int numSales = 0;
                for (Purchase p : s.getSales()) {
                    if (p.getBuyer().equals(b)) {
                        totalSales += p.getPricePerUnit() * p.getQuantityPurchased();
                        numSales += p.getQuantityPurchased();
                    }
                }
                JPanel tablePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                JLabel panelText = new JLabel(String.format("Buyer: %s / Purchases: %d  / Total Revenue: $%.2f\n",
                        b.getEmail(), numSales, totalSales));
                panelText.setFont(new Font("Courier New", Font.PLAIN, 15));
                tablePanel.add(panelText);
                salesListTable.add(tablePanel);

            }

            // By Product
            ArrayList<Item> productList = new ArrayList<>();
            for (Item item : s.getItems()) {
                productList.add(item);
            }

            productList = sortProductList(productList);

            for (Item item : productList) {
                int purchases = 0;
                double revenue = 0.00;
                for (Purchase p : s.getSales()) {
                    if (p.getItem().getName().equals(item.getName())) {
                        purchases += p.getQuantityPurchased();
                        revenue += p.getQuantityPurchased() * p.getPricePerUnit();
                    }
                }
                JPanel tablePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                JLabel panelText = new JLabel(String.format("Item: %s / Total Sales: %d / Total Revenue: $%.2f",
                        item.getName(), purchases, revenue));
                panelText.setFont(new Font("Courier New", Font.PLAIN, 15));

                tablePanel.add(panelText);
                salesListTable.add(tablePanel);
            }
            if (productList.size() == 0 && buyers.size() == 0) {
                JLabel nothing = new JLabel("No products or sales yet!");
                nothing.setFont(new Font("Courier New", Font.PLAIN, 15));
                salesListTable.add(nothing);
            }
        }
        if (seller.getStores().size() == 0) {
            JLabel noStores = new JLabel("You don't have any stores!");
            salesListTable.add(noStores);
        }
        return salesListTable;
    }

    /**
     * Sort buyer list.
     *
     * @param buyerList list containing buyer details.
     * @return sorted buyer ArrayList.
     */
    public ArrayList<Buyer> sortBuyerList(ArrayList<Buyer> buyerList) {
        ArrayList<Integer> buyerPurchasesFromSeller = new ArrayList<>();
        for (Buyer b : buyerList) {
            double totalSales = 0.00;
            int numSales = 0;
            for (Purchase p : seller.getPastSales()) {
                if (p.getBuyer().equals(b)) {
                    totalSales += p.getPricePerUnit() * p.getQuantityPurchased();
                    numSales += p.getQuantityPurchased();
                }
            }
            buyerPurchasesFromSeller.add(numSales);
        }
        // Now, actually sort the array
        for (int i = 0; i < buyerList.size(); i++) {
            for (int j = 0; j < buyerList.size(); j++) {
                if (buyerPurchasesFromSeller.get(j) >= buyerPurchasesFromSeller.get(i)) {
                    Buyer temp;
                    temp = buyerList.get(j);
                    buyerList.set(j, buyerList.get(i));
                    buyerList.set(i, temp);

                    int tempVal;
                    tempVal = buyerPurchasesFromSeller.get(j);
                    buyerPurchasesFromSeller.set(j, buyerPurchasesFromSeller.get(i));
                    buyerPurchasesFromSeller.set(i, tempVal);
                }
            }
        }
        return buyerList;
    }

    /**
     * Sort product list.
     *
     * @param productList list containing product details.
     * @return sorted product ArrayList.
     */
    public ArrayList<Item> sortProductList(ArrayList<Item> productList) {
        ArrayList<Integer> productPurchasesFromSeller = new ArrayList<>();
        for (Item item : productList) {
            int purchases = 0;
            double revenue = 0.00;
            for (Purchase p : seller.getPastSales()) {
                if (p.getItem().getName().equals(item.getName())) {
                    purchases += p.getQuantityPurchased();
                    revenue += p.getQuantityPurchased() * p.getPricePerUnit();
                }
            }
            productPurchasesFromSeller.add(purchases);
        }
  
        // Now, actually sort the array
        for (int i = 0; i < productList.size(); i++) {
            for (int j = 0; j < productList.size(); j++) {
                if (productPurchasesFromSeller.get(j) >= productPurchasesFromSeller.get(i)) {
                    Item temp;
                    temp = productList.get(j);
                    productList.set(j, productList.get(i));
                    productList.set(i, temp);

                    int tempVal;
                    tempVal = productPurchasesFromSeller.get(j);
                    productPurchasesFromSeller.set(j, productPurchasesFromSeller.get(i));
                    productPurchasesFromSeller.set(i, tempVal);
                }
            }
        }
        return productList;
    }

    /**
     * Retrieve sale status for buyer and their purchased items.
     *
     * @return sorted and complete list containing relevant information.
     */
    public JPanel getListPanel() {
        try {
            seller = (Seller) sc.communicateWithServer(39, seller);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Connection error! Check yo" +
                            "ur connection and try again. Data may be out of date.", "Store App",
                    JOptionPane.ERROR_MESSAGE);
        }

        JPanel salesListTable = new JPanel(new GridLayout(0, 1));
        // By Buyer
        for (Store s : seller.getStores()) {
            JPanel storePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel storeLabel = new JLabel("Store: " + s.getName());
            storeLabel.setFont(new Font("Courier New", Font.BOLD, 20));
            storePanel.add(storeLabel);
            salesListTable.add(storePanel);
            ArrayList<Buyer> buyers = new ArrayList<>();

            for (Purchase p : s.getSales()) {
                if (!buyers.contains(p.getBuyer())) {
                    buyers.add(p.getBuyer());
                }
            }
            // Now, sort the buyers list by number of sales

            for (Buyer b : buyers) {
                double totalSales = 0.00;
                int numSales = 0;
                for (Purchase p : s.getSales()) {
                    if (p.getBuyer().equals(b)) {
                        totalSales += p.getPricePerUnit() * p.getQuantityPurchased();
                        numSales += p.getQuantityPurchased();
                    }
                }
                JPanel tablePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                JLabel panelText = new JLabel(String.format("Buyer: %s / Purchases: %d  / Revenue: $%.2f\n",
                        b.getEmail(), numSales, totalSales));
                panelText.setFont(new Font("Courier New", Font.PLAIN, 15));

                tablePanel.add(panelText);
                salesListTable.add(tablePanel);

            }

            // By Product
            ArrayList<Item> productList = new ArrayList<>();
            for (Item item : s.getItems()) {
                productList.add(item);
            }


            for (Item item : productList) {
                int purchases = 0;
                double revenue = 0.00;
                for (Purchase p : s.getSales()) {
                    if (p.getItem().getName().equals(item.getName())) {
                        purchases += p.getQuantityPurchased();
                        revenue += p.getQuantityPurchased() * p.getPricePerUnit();
                    }
                }
                JPanel tablePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                JLabel panelText = new JLabel(String.format("Item: %s / Total Sales: %d / Total Revenue: $%.2f",
                        item.getName(), purchases, revenue));
                panelText.setFont(new Font("Courier New", Font.PLAIN, 15));

                tablePanel.add(panelText);
                salesListTable.add(tablePanel);
            }
            if (productList.size() == 0 && buyers.size() == 0) {
                JLabel nothing = new JLabel("No products or sales yet!");
                nothing.setFont(new Font("Courier New", Font.PLAIN, 15));

                salesListTable.add(nothing);
            }
        }
        if (seller.getStores().size() == 0) {
            JLabel noStores = new JLabel("You don't have any stores!");
            salesListTable.add(noStores);
        }
        return salesListTable;
    }

    /**
     * Back button used to return to the previous menu panel.
     */
    private void back() {
        JPanel nPanel = new JPanel();
        SellerMenuGUI smg = new SellerMenuGUI(nPanel, seller);
        smg.startSellerMenu(mainFrame);
        mainFrame.setContentPane(nPanel);
        mainFrame.setVisible(true);
    }


}
