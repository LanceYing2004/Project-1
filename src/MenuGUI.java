import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Menu GUI that begin the program.
 *
 * @author D.Mayer, L.Ying, I.Kleimans, J.Chen, S.Hou, Lab Section #3, Team #11
 * @version July 31, 2023
 */

public class MenuGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new MainUI());
    }
}

/**
 * Create a subclass that is used to invoke other method inside the program.
 */
class MainUI implements Runnable {

    @Override
    public void run() {
        // Replace the default Swing style with a platform style. 
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Don't do anything. This isn't a critical exception. 
        }

        // Main-level JFrame
        JFrame mainFrame = new JFrame("Store App");
        // Entry JPanel
        JPanel entryPanel = new JPanel(new GridBagLayout());
        // Make the entry panel the content pane
        mainFrame.setContentPane(entryPanel);
        // Add a little bit of content
        JLabel welcomeLabel = new JLabel("Welcome! Are you a buyer or a seller?");
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 25));

        JButton buyer = new JButton("Buyer");
        buyer.setPreferredSize(new Dimension(200, 160));
        buyer.setFont(new Font("Arial", Font.PLAIN, 32));
        JButton seller = new JButton("Seller");
        seller.setPreferredSize(new Dimension(200, 160));
        seller.setFont(new Font("Arial", Font.PLAIN, 32));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        entryPanel.add(welcomeLabel, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 2;
        entryPanel.add(seller, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        entryPanel.add(buyer, gbc);


        // Create the pathway JPanels
        JPanel pathwayPanel = new JPanel();

        // Add the event listeners
        buyer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginGUI lg = new LoginGUI(pathwayPanel);
                mainFrame.setContentPane(pathwayPanel);
                lg.startLogin(mainFrame, true);
                mainFrame.setVisible(true); // Necessary to "show" the buyer panel. 
            }
        });
        seller.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginGUI lg = new LoginGUI(pathwayPanel);
                mainFrame.setContentPane(pathwayPanel);
                lg.startLogin(mainFrame, false);
                mainFrame.setVisible(true); // Necessary to "show" the buyer panel. 
            }
        });
        // Make it all visible
        mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        mainFrame.setSize(1100, 700);
        mainFrame.setLocationRelativeTo(null);

        mainFrame.setVisible(true);
    }
}

