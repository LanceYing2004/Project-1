import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class represents Seller Menu GUI. The main interface the seller will see when they finished the login process.
 *
 * @author D.Mayer, L.Ying, I.Kleimans, J.Chen, S.Hou, Lab Section #3, Team #11
 * @version July 31, 2023
 */
public class SellerMenuGUI {
    private final Seller seller; //seller from Seller class
    private final JPanel sellerPathwayPanel; //This panel "flows" through all seller-pathway steps.
    // At each step, a new panel is created and passed as this parameter.

    /**
     * SellerMenuGUI constructor.
     *
     * @param sellerPathwayPanel This panel "flows" through all seller-pathway steps
     * @param seller             seller from Seller class
     */
    public SellerMenuGUI(JPanel sellerPathwayPanel, Seller seller) {
        this.sellerPathwayPanel = sellerPathwayPanel;
        this.seller = seller;
    }

    /**
     * Initialize and open Seller Menu.
     *
     * @param mainFrame the main frame that has been throughout the program.
     */
    public void startSellerMenu(JFrame mainFrame) {
        // Simple login page
        // General JPanel
        JPanel menuSample = new JPanel(new GridLayout(0, 2));
        JPanel viewMarketPanel = new JPanel();
        JButton viewMarket = new JButton("View My Stores and Products",
                new ImageIcon("./src/icons/store50.png"));

        //View market option
        viewMarket.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JPanel nPanel = new JPanel();
                    SellerMarketGUI sgui = new SellerMarketGUI(seller, nPanel);
                    sgui.startSellerMarketGUI(mainFrame);
                    mainFrame.setContentPane(nPanel);
                    mainFrame.setVisible(true);
                } catch (Exception exc) {
                    JOptionPane.showMessageDialog(null, "The Market page couldn't be loade"
                            +
                            "d. Check your network connection and try again.", "Store App", JOptionPane.
                            ERROR_MESSAGE);
                }
            }
        });
        viewMarketPanel.add(viewMarket);
        viewMarketPanel.setSize(500, 100);

        //View past sales option
        JPanel viewPastSalesPanel = new JPanel();
        JButton viewPastSales = new JButton("View Past Sales", new ImageIcon("./src/icons/history50.png"));
        viewPastSales.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JPanel nPanel = new JPanel();
                    SellerSalesGUI sgui = new SellerSalesGUI(seller, nPanel);
                    sgui.startSellerSalesGUI(mainFrame);
                    mainFrame.setContentPane(nPanel);
                    mainFrame.setVisible(true);
                } catch (Exception exc) {
                    JOptionPane.showMessageDialog(null, "The Past Sales page couldn't be loade"
                            +
                            "d. Check your network connection and try again.", "Store App", JOptionPane.
                            ERROR_MESSAGE);
                }
            }
        });
        viewPastSalesPanel.add(viewPastSales);
        viewPastSalesPanel.setSize(500, 100);

        //View shopping cart option
        JPanel viewShoppingCartsPanel = new JPanel();
        JButton viewShoppingCarts = new JButton("View Shopping Carts",
                new ImageIcon("./src/icons/sellercart50.png"));
        viewShoppingCarts.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JPanel nPanel = new JPanel();
                    SellerShoppingCartGUI sGUI = new SellerShoppingCartGUI(seller);
                    sGUI.startShoppingCartGUI(nPanel, mainFrame);
                    mainFrame.setContentPane(nPanel);
                    mainFrame.setVisible(true);
                } catch (Exception exc) {
                    JOptionPane.showMessageDialog(null, "Connection Error! Check your connect" +
                            "ion and try again.", "Store App", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        viewShoppingCartsPanel.add(viewShoppingCarts);

        //View statistics panel option
        JPanel viewStatisticsPanel = new JPanel();
        JButton viewStatistics = new JButton("View Statistics",
                new ImageIcon("./src/icons/sellerstats50.png"));
        viewStatisticsPanel.add(viewStatistics);
        viewStatistics.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JPanel nPanel = new JPanel();
                    StatisticsGUI statGUI = new StatisticsGUI(seller, nPanel);
                    statGUI.startStatGUI(mainFrame);
                    mainFrame.setContentPane(nPanel);
                    mainFrame.setVisible(true);
                } catch (Exception exc) {
                    JOptionPane.showMessageDialog(null, "Error when trying to view Statistics!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        //View import & export option
        JPanel importExportPanel = new JPanel();
        JButton importExport = new JButton("Import/Export Products and Stores",
                new ImageIcon("./src/icons/export50.png"));
        importExportPanel.add(importExport);
        importExport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    JPanel nPanel = new JPanel();
                    SellerImportExportGUI sellerIE = new SellerImportExportGUI(seller, nPanel);
                    sellerIE.SellerImportExportGUI(mainFrame);
                    mainFrame.setContentPane(nPanel);
                    mainFrame.setVisible(true);
                } catch (Exception exc) {
                    JOptionPane.showMessageDialog(null,
                            "Error when trying to view Import/Export page!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        //Add all the sub-panels to main panel
        menuSample.add(viewMarketPanel);
        menuSample.add(viewPastSalesPanel);
        menuSample.add(viewShoppingCartsPanel);
        menuSample.add(viewStatisticsPanel);
        menuSample.add(importExportPanel);
        sellerPathwayPanel.add(menuSample);

    }
}
