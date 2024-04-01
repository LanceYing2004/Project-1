import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintWriter;


/**
 * This class represents Buyer Menu GUI. The main interface the buyer will see when they finished the login process.
 *
 * @author D.Mayer, L.Ying, I.Kleimans, J.Chen, S.Hou, Lab Section #3, Team #11
 * @version July 31, 2023
 */

public class BuyerMenuGUI {
    private final Buyer buyer; //buyer from the Buyer class
    private final JPanel buyerPathwayPanel; // This panel "flows" through all buyer-pathway steps. At each step, a new panel
    // is created and passed as this parameter.

    /**
     * Constructor for the BuyerMenuGUI class.
     *
     * @param buyer,             the buyer using this Buyer Menu GUI.
     * @param buyerPathwayPanel, Main panel that "flows" through all buyer-pathway steps.
     */
    public BuyerMenuGUI(JPanel buyerPathwayPanel, Buyer buyer) {
        this.buyerPathwayPanel = buyerPathwayPanel;
        this.buyer = buyer;
    }

    /**
     * Start the buyer menu.
     *
     * @param mainFrame, main frame that used throughout the program.
     */
    public void startBuyerMenu(JFrame mainFrame) {
        // Simple login page
        // General JPanel
        JPanel buyerMenu = new JPanel(new GridLayout(0, 2));
        JPanel marketButton = new JPanel();

        //View Market
        JButton viewMarket = new JButton("View Market", new ImageIcon("./src/icons/store50.png"));
        viewMarket.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JPanel nPanel = new JPanel();
                    MarketGUI mgui = new MarketGUI(buyer, nPanel);
                    mgui.startMarketGUI(mainFrame);
                    mainFrame.setContentPane(nPanel);
                    mainFrame.setVisible(true);
                } catch (Exception exc) {
                    JOptionPane.showMessageDialog(null, "Couldn't open Market at the moment." +
                            " Please try again later.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        marketButton.add(viewMarket);

        //Shopping cart
        JPanel shoppingCartButton = new JPanel();
        JButton viewShoppingCart = new JButton("Shopping Cart",
                new ImageIcon("./src/icons/sellercart50.png"));
        shoppingCartButton.add(viewShoppingCart);
        viewShoppingCart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JPanel nPanel = new JPanel();
                    BuyerShoppingCartGUI sGUI = new BuyerShoppingCartGUI(buyer);
                    sGUI.startShoppingCartGUI(nPanel, mainFrame);
                    mainFrame.setContentPane(nPanel);
                    mainFrame.setVisible(true);
                } catch (Exception exc) {
                    JOptionPane.showMessageDialog(null,
                            "Couldn't open Shopping cart at the moment." +
                                    " Please try again later.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        //Dashboard
        JPanel dashboardButton = new JPanel();
        JButton viewDashboard = new JButton("Dashboard", new ImageIcon("./src/icons/dashboard50.png"));
        dashboardButton.add(viewDashboard);
        viewDashboard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JPanel nPanel = new JPanel();
                    DashboardGUI dbgui = new DashboardGUI(buyer, nPanel);
                    dbgui.startDashboardGUI(mainFrame);
                    mainFrame.setContentPane(nPanel);
                    mainFrame.setVisible(true);
                } catch (Exception exc) {
                    JOptionPane.showMessageDialog(null,
                            "Couldn't open Dashboard at the moment." +
                                    " Please try again later.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        //View Past Purchases
        JPanel viewPastPurchasesButton = new JPanel();
        JButton viewPastPurchases = new JButton("Past Purchases",
                new ImageIcon("./src/icons/history50.png"));
        viewPastPurchasesButton.add(viewPastPurchases);
        viewPastPurchases.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JPanel nPanel = new JPanel();
                    BuyerPastPurchasesGUI pastpurGUI = new BuyerPastPurchasesGUI(buyer, nPanel);
                    pastpurGUI.startPastPurchases(mainFrame);
                    mainFrame.setContentPane(nPanel);
                    mainFrame.setVisible(true);
                } catch (Exception exc) {
                    JOptionPane.showMessageDialog(null,
                            "Couldn't view Past Purchases at the moment." +
                                    " Please try again later.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        //Export Past Purchases
        JPanel exportPastPurchasesButton = new JPanel();
        JButton exportPastPurchases = new JButton("Export Past Purchases",
                new ImageIcon("./src/icons/export50.png"));
        exportPastPurchasesButton.add(exportPastPurchases);
        exportPastPurchases.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    browseFilesExport(mainFrame);
                } catch (Exception exc) {
                    JOptionPane.showMessageDialog(null,
                            "Couldn't export Past Purchases at the moment." +
                                    " Please try again later.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        buyerMenu.add(marketButton);
        buyerMenu.add(shoppingCartButton);
        buyerMenu.add(dashboardButton);
        buyerMenu.add(viewPastPurchasesButton);
        buyerMenu.add(exportPastPurchasesButton);
        buyerPathwayPanel.add(buyerMenu);

    }

    /**
     * A sperate browse file to export method that could choose file to export.
     *
     * @param mainFrame, same frame as the login screen.
     */
    public void browseFilesExport(JFrame mainFrame) {
        final JLabel label = new JLabel();

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select (or Create) a File to Export To");

        int option = fileChooser.showSaveDialog(mainFrame);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            try {
                PrintWriter pw = new PrintWriter(file);
                pw.println("Name,PricePerUnit,Quantity,Seller,Store");
                for (Purchase p : buyer.getPastPurchases()) {
                    pw.println(String.format("%s,%.2f,%d,%s,%s", p.getItemName(),
                            p.getPricePerUnit(), p.getQuantityPurchased(),
                            p.getSeller().getEmail(), p.getStore().getName()));
                }
                pw.close();
                JOptionPane.showMessageDialog(null, "Export Successful!",
                        "Store App", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Could not export! Returning...",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            label.setText("File Saved as: " + file.getName());
        } else {
            label.setText("Save command canceled");
        }
    }
}
