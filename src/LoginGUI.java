import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


/**
 * Login GUI that helps to store all the relevant information regarding the login process.
 *
 * @author D.Mayer, L.Ying, I.Kleimans, J.Chen, S.Hou, Lab Section #3, Team #11
 * @version July 31, 2023
 */
public class LoginGUI {
    private final JPanel pathwayPanel; // This panel "flows" through all buyerpathway steps and should have the previous
    // step removed and the next added.
    private final ServerComm sc; //Server communication tool sc from ServerComm class

    /**
     * Constructor for the BuyerShoppingCartGUI class.
     *
     * @param pathwayPanel used for MenuGUI login process.
     */

    public LoginGUI(JPanel pathwayPanel) {
        this.pathwayPanel = pathwayPanel;
        sc = new ServerComm();
    }

    /**
     * Begin the login process. Focused on the login in screen such as "back", "login" or "create account". Contains
     * verification process that require the user to enter the correct password.
     *
     * @param mainFrame main frame that used throughout the program.
     * @param isBuyer   boolean to determine whether it's buyer or seller.
     */
    public void startLogin(JFrame mainFrame, boolean isBuyer) {
        // Simple login page
        // General JPanel
        JPanel loginPanel = new JPanel(new GridLayout(0, 1));


        // JPanel for the page label
        JPanel pageLabelPanel = new JPanel();
        JLabel loginPageLabel = new JLabel("Login or Create Account");
        loginPageLabel.setFont(new Font("Arial", Font.BOLD, 30));
        pageLabelPanel.add(loginPageLabel);
        // JPanel for the username row
        JPanel usernameRowPanel = new JPanel();
        JLabel usernameLabel = new JLabel("Email:");
        JTextField usernameInput = new JTextField(20);
        usernameRowPanel.add(usernameLabel);
        usernameRowPanel.add(usernameInput);
        // JPanel for the password row
        JPanel passwordRowPanel = new JPanel();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordInput = new JPasswordField(20);
        passwordRowPanel.add(passwordLabel);
        passwordRowPanel.add(passwordInput);
        // JPanel for incorrect responses
        JPanel incorrectResponsePanel = new JPanel();
        JLabel incorrectResponse = new JLabel("Error: Incorrect username/password!");
        incorrectResponsePanel.add(incorrectResponse);
        incorrectResponsePanel.setVisible(false);
        // JPanel for the final buttons
        JPanel finalButtonPanel = new JPanel();
        JButton backButton = new JButton("Back");
        JButton loginButton = new JButton("Login");
        JButton createAccountButton = new JButton("Create Account");
        finalButtonPanel.add(backButton);
        finalButtonPanel.add(loginButton);
        finalButtonPanel.add(createAccountButton);

        //Back button
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainUI menu = new MainUI();
                menu.run();
                mainFrame.dispose();
            }
        });

        //Login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] input = {usernameInput.getText(), passwordInput.getText()};
                if (isBuyer) {
                    Buyer b = null;
                    try {
                        b = (Buyer) sc.communicateWithServer(0, input);
                        if (b == null) {
                            throw new ClassCastException();
                        }
                    } catch (ClassCastException ce) {
                        incorrectResponse.setText("Your username or password was incorrect." +
                                " Try again, or select 'Create Account' to create a new account.");
                        incorrectResponsePanel.setVisible(true);
                    } catch (Exception exc) {
                        incorrectResponse.setText("Connection Error! Try again.");
                        incorrectResponsePanel.setVisible(true);
                    }
                    if (b != null) {
                        advanceToNextBuyer(mainFrame, b); // Advance to the next program screen.
                    }
                } else {
                    Seller s = null;
                    try {
                        s = (Seller) sc.communicateWithServer(30, input);
                        if (s == null) {
                            throw new ClassCastException();
                        }
                    } catch (ClassCastException ce) {
                        incorrectResponse.setText("Your username or password was incorrect." +
                                " Try again, or select 'Create Account' to create a new account.");
                        incorrectResponsePanel.setVisible(true);
                    } catch (Exception exc) {
                        incorrectResponse.setText("There was a problem connecting to the server. Try again.");
                        incorrectResponsePanel.setVisible(true);
                    }
                    if (s != null) {
                        advanceToNextSeller(mainFrame, s); // Advance to the next program screen.
                    }
                }
            }
        });
        //create account button
        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] input = {usernameInput.getText(), passwordInput.getText()};
                if (!usernameInput.getText().contains("@") ||
                        passwordInput.getText().length() < 6 ||
                        usernameInput.getText().split("@").length == 1 ||
                        !usernameInput.getText().contains(".")) {
                    incorrectResponse.setText("Invalid email or password." +
                            " Make sure that your password is at least 6 characters and that your email is valid. ");
                    incorrectResponsePanel.setVisible(true);
                    return;
                }

                if (isBuyer) {
                    Buyer b = null;
                    // Check for duplication
                    try {
                        for (Buyer buyer : (ArrayList<Buyer>) sc.communicateWithServer(7, null)) {
                            if (usernameInput.getText().equals(buyer.getEmail())) {
                                incorrectResponse.setText("This account already exists. Try logging in.");
                                incorrectResponsePanel.setVisible(true);
                                return;
                            }
                        }

                        b = (Buyer) sc.communicateWithServer(1, input);
                        if (b == null) {
                            throw new ClassCastException();
                        }
                    } catch (ClassCastException ce) {
                        incorrectResponse.setText("The response from the server was invalid. Try again.");
                        incorrectResponsePanel.setVisible(true);
                    } catch (Exception exc) {
                        incorrectResponse.setText("There was a problem connecting to the server. Try again.");
                        incorrectResponsePanel.setVisible(true);
                    }
                    if (b != null) {
                        advanceToNextBuyer(mainFrame, b); // Advance to the next program screen. 
                    }

                } else {

                    Seller s = null;
                    try {
                        for (Seller seller : (ArrayList<Seller>) sc.communicateWithServer(41, null)) {
                            if (usernameInput.getText().equals(seller.getEmail())) {
                                incorrectResponse.setText("This account already exists. Try logging in.");
                                incorrectResponsePanel.setVisible(true);
                                return;
                            }
                        }

                        s = (Seller) sc.communicateWithServer(31, input);
                        if (s == null) {
                            throw new ClassCastException();
                        }
                    } catch (ClassCastException ce) {
                        incorrectResponse.setText("The response from the server was invalid. Try again.");
                        incorrectResponsePanel.setVisible(true);
                    } catch (Exception exc) {
                        incorrectResponse.setText("There was a problem connecting to the server. Try again.");
                        incorrectResponsePanel.setVisible(true);
                    }
                    if (s != null) {
                        advanceToNextSeller(mainFrame, s); // Advance to the next program screen. 
                    }
                }
            }
        });

        // Add all the subpanels
        loginPanel.add(pageLabelPanel);
        loginPanel.add(usernameRowPanel);
        loginPanel.add(passwordRowPanel);
        loginPanel.add(finalButtonPanel);
        loginPanel.add(incorrectResponsePanel);

        pathwayPanel.add(loginPanel);

    }

    /**
     * Buyer pathway. If the user is a buyer.
     *
     * @param mainFrame main frame that used throughout the program.
     * @param b         containing buyer personal credentials.
     */
    public void advanceToNextBuyer(JFrame mainFrame, Buyer b) {
        // Add the account bar
        JMenuBar accountBar = new JMenuBar();
        JMenuItem logout = new JMenuItem("Logout");
        JMenuItem help = new JMenuItem("Help");
        JMenuItem about = new JMenuItem("About");

        //Logout button
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Logout successful!", "Logout",
                        JOptionPane.INFORMATION_MESSAGE);
                MainUI menu = new MainUI();
                menu.run();
                mainFrame.dispose();
            }
        });

        //Help button
        help.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "<html>1. Choose the button for the transaction you want" +
                                " (or press back if you don't see it) <br> " +
                                "2. Follow the on-screen prompts within the transaction. <br> " +
                                "3. Make sure that your inputs are valid. For example, integers only in number" +
                                " fields and doubles only in price fields.</html>",
                        "Help", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        //About button
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "<html><b>CS 18000 Project 5 </b>" +
                                "<br> Group 11 <br>Icons from Icons8 (icons8.com)</html>",
                        "About", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        accountBar.add(logout);
        accountBar.add(help);
        accountBar.add(about);
        mainFrame.setJMenuBar(accountBar);

        JPanel nPanel = new JPanel();
        BuyerMenuGUI bmg = new BuyerMenuGUI(nPanel, b);
        bmg.startBuyerMenu(mainFrame);
        mainFrame.setContentPane(nPanel);
        mainFrame.setVisible(true);
    }

    /**
     * Seller pathway. If the user is a seller.
     *
     * @param mainFrame main frame that used throughout the program.
     * @param s         containing seller personal credentials.
     */
    public void advanceToNextSeller(JFrame mainFrame, Seller s) {
        // Add the account bar
        JMenuBar accountBar = new JMenuBar();
        JMenuItem logout = new JMenuItem("Logout");
        JMenuItem help = new JMenuItem("Help");
        JMenuItem about = new JMenuItem("About");

        //Logout button
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "Logout successful!", "Logout", JOptionPane.INFORMATION_MESSAGE);
                MainUI menu = new MainUI();
                menu.run();
                mainFrame.dispose();
            }
        });

        //Help button
        help.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "<html>1. Choose the button for the transaction you want" +
                                " (or press back if you don't see it) <br> " +
                                "2. Follow the on-screen prompts within the transaction. <br> " +
                                "3. Make sure that your inputs are valid. For example, integers only in number" +
                                " fields and doubles only in price fields.</html>",
                        "Help", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        //About button
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "<html><b>CS 18000 Project 5 </b>" +
                                "<br> Group 11 <br>Icons from Icons8 (icons8.com)</html>",
                        "About", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        accountBar.add(logout);
        accountBar.add(help);
        accountBar.add(about);
        mainFrame.setJMenuBar(accountBar);

        JPanel nPanel = new JPanel();
        SellerMenuGUI smg = new SellerMenuGUI(nPanel, s);
        smg.startSellerMenu(mainFrame);
        mainFrame.setContentPane(nPanel);
        mainFrame.setVisible(true);
    }
}
