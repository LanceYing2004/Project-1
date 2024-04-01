import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

/**
 * A class that could export data's regarding import and export, using Java file chooser.
 *
 * @author D.Mayer, L.Ying, I.Kleimans, J.Chen, S.Hou, Lab Section #3, Team #11
 * @version July 31, 2023
 */

public class SellerImportExportGUI extends Component {

    JButton importButton; //import button used for importing information
    JButton exportButton; //export button to export to file
    JButton backButton; //return to the previous directory
    private final Seller seller; //seller from Seller class
    private final JPanel sellerPanel; //This panel "flows" through all seller-pathway steps. At each step, a new panel
    // is created and passed as this parameter.
    private JFrame mainFrame; //main frame that is used throughout the program
    private File file1; //file opened by user
    private final ServerComm sc; //Server communication tool

    /**
     * Constructor for SellerImportExportGUI.
     *
     * @param seller      seller from Seller class
     * @param sellerPanel This panel "flows" through all seller-pathway steps
     */
    public SellerImportExportGUI(Seller seller, JPanel sellerPanel) {
        new BorderLayout();
        this.seller = seller;
        this.sellerPanel = sellerPanel;
        sc = new ServerComm();
    }

    /**
     * Begin SellerImportExportGUI method. User could click "back", "import from file" or "export to file".
     *
     * @param mainFrame main frame that is used throughout the program
     */
    public void SellerImportExportGUI(JFrame mainFrame) {
        this.mainFrame = mainFrame;
        //mainFrame.setSize(640,480);

        backButton = new JButton("<Back");
        importButton = new JButton("Import from file...");
        exportButton = new JButton("Export to file...");

        sellerPanel.add(backButton);
        sellerPanel.add(importButton);
        sellerPanel.add(exportButton);

        mainFrame.add(sellerPanel, BorderLayout.NORTH);

        //All the action listener involving this class are placed here.
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //Import Button
                if (e.getSource() == importButton) {
                    file1 = browseFilesOpen();
                    if (file1 == null) {
                        return;
                    }
                    try {
                        sc.communicateWithServer(32, seller); //Pre-check for connectivity before continuing.
                        FileReader fr = new FileReader(file1);
                        BufferedReader bfr = new BufferedReader(fr);

                        bfr.readLine(); // Clear the top line
                        String nextLine = bfr.readLine();
                        while (nextLine != null) {
                            String[] lineSplit = nextLine.split(",");
                            if (lineSplit.length != 8) {
                                throw new RuntimeException();
                            }

                            String productName = lineSplit[0];
                            float price = Float.parseFloat(lineSplit[1]);
                            int quantity = Integer.parseInt(lineSplit[2]);
                            String description = lineSplit[3];
                            float salePrice = Float.parseFloat(lineSplit[4]);
                            int saleQuantity = Integer.parseInt(lineSplit[5]);
                            String store = lineSplit[6];
                            int perOrderLimit = Integer.parseInt(lineSplit[7]);

                            Store addToStore = null;
                            for (Store s : seller.getStores()) {
                                if (s.getName().equals(store)) {
                                    addToStore = s;
                                }
                            }

                            if (addToStore == null) {
                                addToStore = new Store(store, seller);
                                seller.addStore(addToStore);
                            }
                            // Add the product: check if it already exists
                            boolean alreadyExists = false;
                            for (Item i : addToStore.getItems()) {
                                if (i.getName().equals(productName)) {
                                    alreadyExists = true;
                                    break;
                                }
                            }

                            nextLine = bfr.readLine();
                            if (alreadyExists) {
                                continue;
                            }
                            // The product doesn't already exist, so add it
                            addToStore.addNewItem(new Item(productName, description, seller, price,
                                    quantity, saleQuantity, salePrice, perOrderLimit, addToStore));
                        }
                        sc.communicateWithServer(32, seller); // Store the updated data on the server.
                        // This replaces dumpToFile()
                        JOptionPane.showMessageDialog(null,
                                "Import successful! Go to 'View Market' to see your imported products.",
                                "Import Successful", JOptionPane.INFORMATION_MESSAGE);

                    } catch (IOException ioex) {
                        JOptionPane.showMessageDialog(null,
                                "There was a problem communicating with the server. Try again.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (ClassCastException cce) {
                        JOptionPane.showMessageDialog(null,
                                "There was a problem communicating with the server. Try again.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null,
                                "The file was empty, didn't exist, or contained bad data! Try again.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }

                }
                //Export button
                if (e.getSource() == exportButton) {
                    browseFilesExport();
                }
                //Back button
                if (e.getSource() == backButton) {
                    back();
                }

            }
        };

        //Reading it, try/catch
        importButton.addActionListener(actionListener);
        exportButton.addActionListener(actionListener);
        backButton.addActionListener(actionListener);


    }

    /**
     * Open the file selected by the user.
     *
     * @return the opened file, otherwise return null
     */
    public File browseFilesOpen() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Open file");
        int status = fc.showOpenDialog(this);
        if (status == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            // JOptionPane.showMessageDialog(null, file.getName() + "opened successfully.",
            // "Success", JOptionPane.INFORMATION_MESSAGE); -- This was showing up even when the file didn't exist.
            return file;
        } else {
            JOptionPane.showMessageDialog(null, "You cancelled the file!",
                    "Error", JOptionPane.INFORMATION_MESSAGE);
        }
        return null;

    }

    /**
     * Browse the file to export.
     */
    public void browseFilesExport() {
        final JLabel label = new JLabel();

        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showSaveDialog(mainFrame);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            try {

                PrintWriter pw = new PrintWriter(file);
                pw.println("Name,Price,Quantity,Description,SalePrice,SaleQuantity,Store,PerOrderLimit");
                for (Store store : seller.getStores()) {
                    for (Item item : store.getItems()) {
                        pw.println(String.format("%s,%.2f,%d,%s,%.2f,%d,%s,%d", item.getName(),
                                item.getPrice(), item.getQuantityAvailable(), item.getDescription(),
                                item.getSalePrice(), item.getSaleQuantity(), item.getStore().getName(),
                                item.getPerOrderLimit()));
                    }
                }
                pw.close();
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

    /**
     * Back button, return to the previous menu.
     */
    public void back() {
        JPanel nPanel = new JPanel();
        SellerMenuGUI smg = new SellerMenuGUI(nPanel, seller);
        smg.startSellerMenu(mainFrame);
        mainFrame.setContentPane(nPanel);
        mainFrame.setVisible(true);
    }

}
