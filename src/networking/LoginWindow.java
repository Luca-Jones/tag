package networking;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JPanel;

import game.Main;
import game.SinglePlayerGame;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginWindow extends JFrame {

    private static final String TITLE = "Login";
    private JPanel panel;

    public LoginWindow() {
        setTitle(TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        // setVisible(false);
        
        panel = new JPanel();
        panel.setPreferredSize(new Dimension(1000, 1000));
        panel.setLayout(new GridLayout(3, 1, 10, 10)); // 3 rows, 1 column, 10px vertical and horizontal gaps

        JButton button1 = new JButton("Single Player");
        JButton button2 = new JButton("Join Game");
        JButton button3 = new JButton("Start Server");

        button1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                SinglePlayerGame.main(null);
                dispose();
            }
            
        });

        button2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String username = getUsername();
                Main.joinServer(username);
                dispose();
            }
            
        });

        button3.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Main.startServer();
                String username = getUsername();
                Main.joinServer(username);
            }
            
        });

        panel.add(button1);
        panel.add(button2);
        panel.add(button3);

        add(panel);
        pack();
        setLocationRelativeTo(null); // Center the window on the screen
        setVisible(true);
    }

    private String getUsername() {
        panel.removeAll();
        return JOptionPane.showInputDialog("Please Enter a username: ");
    }

}
