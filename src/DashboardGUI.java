import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * This class provides a GUI for the buyer's Dashboard.
 *
 * @author D.Mayer, L.Ying, I.Kleimans, J.Chen, S.Hou, Lab Section #3, Team #11
 * @version July 31, 2023
 */

public class DashboardGUI extends Component {
    JButton backButton; //backButton used to return to the last directory
    JButton refreshButton; //Refresh button used for concurrency
    //Fields
    private final Buyer buyer; //buyer from Buyer class
    private final JPanel buyerPanel; //This panel "flows" through all buyer-pathway steps. At each step, a new panel
    // is created and passed as this parameter.
    private JFrame mainFrame; //The main frame of the whole program
    private ArrayList<Seller> sellersList; //An ArrayList containing seller list
    private ServerComm sc; //Server communication tool sc from ServerComm class

    /**
     * Constructor for the BuyerShoppingCartGUI class.
     *
     * @param buyer the buyer using this shopping cart GUI.
     */
    public DashboardGUI(Buyer buyer, JPanel buyerPanel) {
        new BorderLayout();
        this.buyer = buyer;
        this.buyerPanel = buyerPanel;
    }

    /**
     * This method initializes and starts the dashboard GUI.
     *
     * @param mainFrame the main frame of the GUI
     */
    public void startDashboardGUI(JFrame mainFrame) {
        this.mainFrame = mainFrame;
        buyerPanel.setLayout(new BorderLayout());
        JPanel topPanel = new JPanel();

        backButton = new JButton("<Back");
        //backButton.setSize(new Dimension(100, 60));
        refreshButton = new JButton("Refresh");
        topPanel.add(backButton);
        topPanel.add(refreshButton);

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
        JPanel dashboardPanel = getDashboardPanel();

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JPanel dashboardPanel = getDashboardPanel();
                    buyerPanel.removeAll();
                    buyerPanel.add(topPanel, BorderLayout.NORTH);
                    JPanel view = new JPanel();
                    JScrollPane jsp = new JScrollPane();
                    jsp.setViewportView(view);
                    jsp.setPreferredSize(new Dimension(1000, 700));
                    view.add(dashboardPanel);

                    buyerPanel.add(jsp, BorderLayout.CENTER);
                    mainFrame.repaint();
                    mainFrame.setVisible(true);
                } catch (Exception exc) {
                    JOptionPane.showMessageDialog(null, "Connection Error! " +
                            "Check your connection and try again.", "Store App", JOptionPane.ERROR_MESSAGE);
                }

            }
        });


        //Add mainpanel to display panel
        buyerPanel.add(topPanel, BorderLayout.NORTH);
        JPanel view = new JPanel();
        JScrollPane jsp = new JScrollPane();
        jsp.setViewportView(view);
        jsp.setPreferredSize(new Dimension(1000, 700));
        view.add(dashboardPanel);

        buyerPanel.add(jsp, BorderLayout.CENTER);
    }

    /**
     * Retrieve the newest information on Dashboard Panel using communicate-with-server tool.
     *
     * @return cartPanel containing newest information.
     */
    public JPanel getDashboardPanel() {
        JPanel dashboardPanel = new JPanel(new GridLayout(0, 1));
        ArrayList<String> stores = buyer.getPurchasedStores();

        JPanel purchaseTitlePanel = new JPanel();
        JPanel purchasePanel = new JPanel(new GridLayout(0, 1));
        JPanel countTitlePanel = new JPanel();
        JPanel countPanel = new JPanel(new GridLayout(0, 1));

        JLabel purchaseTitleLabel = new JLabel("Your Purchases:");
        purchaseTitleLabel.setFont(new Font("Courier New", Font.BOLD, 20));
        purchaseTitlePanel.add(purchaseTitleLabel);

        if (stores.size() > 0) {
//    public Object communicateWithServer(int command, Object inObj) throws IOException, ClassNotFoundException {
            for (String store : stores) {
                int purchases = buyer.getNumberOfPurchasesFromStore(store);
                JLabel purchaseLabel = new JLabel("Store: " + store +
                        ". Number of products purchased: " + purchases);
                purchaseLabel.setFont(new Font("Courier New", Font.PLAIN, 12));
                purchasePanel.add(purchaseLabel);
            }
        } else {
            JLabel purchaseLabel = new JLabel("No purchases yet!");
            purchaseLabel.setFont(new Font("Courier New", Font.PLAIN, 20));
            purchasePanel.add(purchaseLabel);
        }
        JLabel countTitleLabel = new JLabel("Overall Purchases:");
        countTitleLabel.setFont(new Font("Courier New", Font.BOLD, 20));
        countTitlePanel.add(countTitleLabel);

        sc = new ServerComm();
        try {
            sellersList = (ArrayList<Seller>) sc.communicateWithServer(41, null);
        } catch (Exception e) {
            throw new RuntimeException();
        }
        if (sellersList != null && sellersList.size() != 0) {
            for (Seller seller : sellersList) {
                for (Store store : seller.getStores()) {
                    int sales = 0;
                    for (Purchase p : store.getSales()) {
                        sales += p.getQuantityPurchased();
                    }
                    JLabel purchaseLabel = new JLabel("Store: " + store +
                            ". Number of products purchased: " + sales);
                    purchaseLabel.setFont(new Font("Courier New", Font.PLAIN, 12));
                    countPanel.add(purchaseLabel);
                }
            }

        } else {
            JLabel countLabel = new JLabel("No stores exist!");
            countLabel.setFont(new Font("Courier New", Font.PLAIN, 20));
            countPanel.add(countLabel);
        }
        //Add subpanels to mainpanel
        dashboardPanel.add(purchaseTitlePanel);
        dashboardPanel.add(purchasePanel);
        dashboardPanel.add(countTitlePanel);
        dashboardPanel.add(countPanel);
        return dashboardPanel;
    }
}