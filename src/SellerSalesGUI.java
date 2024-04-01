import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A class that contain seller past sales information.
 *
 * @author D.Mayer, L.Ying, I.Kleimans, J.Chen, S.Hou, Lab Section #3, Team #11
 * @version July 31, 2023
 */

public class SellerSalesGUI {

    private Seller seller; //seller from Seller class
    private final JPanel sellerPathwayPanel; //This panel "flows" through all seller-pathway steps.
    // At each step, a new panel is created and passed as this parameter.
    private JFrame mainFrame; //main frame that is used throughout the program
    private final ServerComm sc; //server communication tool

    /**
     * SellerSalesGUI constructor.
     *
     * @param seller             seller from Seller class
     * @param sellerPathwayPanel This panel "flows" through all seller-pathway steps.
     */
    public SellerSalesGUI(Seller seller, JPanel sellerPathwayPanel) {
        this.seller = seller;
        this.sellerPathwayPanel = sellerPathwayPanel;
        sc = new ServerComm();
    }

    /**
     * Begin the SellerMarketGUI process.
     *
     * @param mainFrame the mainframe that is used throughout the program.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void startSellerSalesGUI(JFrame mainFrame) throws IOException, ClassNotFoundException {
        this.mainFrame = mainFrame;

        // Using border layout for top back/search/sort and main content
        JPanel marketPanel = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel();
        JPanel itemPanel = new JPanel(new BorderLayout());
        JPanel mainPanel = new JPanel(new BorderLayout());

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

        JButton refreshButton = new JButton("Refresh");

        topPanel.add(backButton);
        topPanel.add(refreshButton);

        // Start by "refreshing" the list
        JList productsList = refreshList();
        JList storesList = refreshList();
        if (productsList.getModel().getSize() == 0 || storesList.getModel().getSize() == 0) {
            mainPanel.add(new JLabel("No results found!"));
        } else {
            productsList.setFont(new Font("Courier New", Font.PLAIN, 20));
            storesList.setFont(new Font("Courier New", Font.PLAIN, 20));
            mainPanel.add(productsList);
        }

        //Refresh button
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // New server request
                try {
                    // Clear the panel
                    mainPanel.removeAll();
                    // Add the new list
                    JList jl = refreshList();
                    // Check for emptiness
                    if (jl.getModel().getSize() == 0) {
                        mainPanel.add(new JLabel("No results found!"));
                    } else {
                        jl.setFont(new Font("Courier New", Font.PLAIN, 20));
                        mainPanel.add(jl);

                    }
                    // Repaint
                    mainFrame.repaint();
                    mainFrame.setVisible(true);
                } catch (Exception exc) {
                    JOptionPane.showMessageDialog(null, "There was an error refreshing. Try " +
                            "again.", "Store App", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Top JLabel
        JPanel tableLabelPanel = new JPanel();
        JLabel tableLabel = new JLabel(String.format("%-30s %-10s %-20s %-20s", "Item Name",
                "Revenue", "Quantity", "Buyer"));
        tableLabel.setFont(new Font("Courier New", Font.PLAIN, 20));

        tableLabelPanel.add(tableLabel);
        itemPanel.add(tableLabelPanel, BorderLayout.NORTH);
        itemPanel.add(mainPanel, BorderLayout.CENTER);
        JScrollPane itemScrollFrame = new JScrollPane(itemPanel);
        itemPanel.setAutoscrolls(true);
        itemScrollFrame.setPreferredSize(new Dimension(1000, 500));

        marketPanel.add(topPanel, BorderLayout.NORTH);
        marketPanel.add(itemScrollFrame, BorderLayout.CENTER);
        sellerPathwayPanel.add(marketPanel);

    }

    /**
     * Refresh the list/entries within SellerSalesGUI.
     *
     * @return refreshed list.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public JList refreshList() throws IOException, ClassNotFoundException {
        seller = (Seller) sc.communicateWithServer(39, seller);
        String[] JListItems = new String[seller.getStores().size() * 2 + seller.getPastSales().size()];
        ArrayList<Store> stores = seller.getStores();
        int cntr = 0;
        for (Store s : stores) {
            ArrayList<Purchase> purchases = s.getSales();
            JListItems[cntr] = "<html> <b> Store: " + s.getName() + "</b> </html>";
            cntr++;
            int purchaseCntr = 0;
            for (Purchase p : purchases) {
                JListItems[cntr] = p.toString();
                cntr++;
                purchaseCntr++;
            }
            if (purchaseCntr == 0) {
                JListItems[cntr] = "No sales from this store!";
                cntr++;
            }
        }

        JList<String> sales = new JList<String>(JListItems);
        return sales;
    }
}