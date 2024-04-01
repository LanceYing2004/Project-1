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

public class BuyerPastPurchasesGUI {
    private final Buyer buyer; //buyer from Buyer class
    private final JPanel buyerPanel; //This panel "flows" through all buyer-pathway steps. At each step, a new panel
    // is created and passed as this parameter.
    private JFrame mainFrame; //The main frame of the whole program
    private ArrayList<Purchase> pastPurchases; //An ArrayList containing buyer past purchases

    /**
     * Constructor for the BuyerPastPurchasesGUI class.
     *
     * @param buyer,      the buyer using this past purchases GUI.
     * @param buyerPanel, buyerPanel from BuyerMenuGUI.
     */
    public BuyerPastPurchasesGUI(Buyer buyer, JPanel buyerPanel) {

        this.buyer = buyer;
        this.buyerPanel = buyerPanel;
    }

    /**
     * Start the buyer Past Purchases menu.
     *
     * @param mainFrame, main frame that used throughout the program.
     */
    public void startPastPurchases(JFrame mainFrame) {
        pastPurchases = buyer.getPastPurchases();
        this.mainFrame = mainFrame;

        JPanel pastPurchasePanel = new JPanel(new GridLayout(0, 1));

        JPanel topPanel = new JPanel();

        //Back button
        JButton backButton = new JButton("<Back");
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
        topPanel.add(backButton);

        JPanel pastPurTitle = new JPanel(new GridLayout(0, 1));
        JPanel pastPurListPanel = new JPanel(new GridLayout(0, 1));

        //Find and display the past purchases if there's any
        if (pastPurchases == null || pastPurchases.size() == 0) {
            JLabel noPastPur = new JLabel("No past purchase history found!");
            noPastPur.setFont(new Font("Arial", Font.PLAIN, 20));
            pastPurTitle.add(noPastPur);
        } else {
            JLabel title = new JLabel(String.format("%-30s %-20s %-25s %-20s", "Item Name",
                    "Price Per Unit", "Quantity Purchased", "Store Name"));
            title.setFont(new Font("Courier New", Font.BOLD, 15));
            pastPurTitle.add(title);

            for (Purchase purchase : pastPurchases) {
                String purchaseStr = purchase.toBuyerString();//.substring(0, 50);
                JLabel pastPurListLabel = new JLabel(purchaseStr);
                pastPurListLabel.setFont(new Font("Courier New", Font.PLAIN, 15));

                pastPurListPanel.add(pastPurListLabel);

            }
        }
        buyerPanel.setLayout(new BorderLayout());
        pastPurchasePanel.add(pastPurTitle);
        pastPurchasePanel.add(pastPurListPanel);
        JPanel view = new JPanel();
        JScrollPane jsp = new JScrollPane();
        jsp.setPreferredSize(new Dimension(1000, 500));
        jsp.setViewportView(view);
        view.add(pastPurchasePanel);
        buyerPanel.add(jsp, BorderLayout.CENTER);
        buyerPanel.add(topPanel, BorderLayout.NORTH);
    }
}
