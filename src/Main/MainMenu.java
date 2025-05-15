package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainMenu extends JPanel {

    public MainMenu() {
        System.out.println("Initializing Main Menu");

        setBackground(Color.BLACK);
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(800, 600));

        JLabel startGameLabel = new JLabel("Start Game");
        customizeLabel(startGameLabel);
        startGameLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Start Game button clicked");
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(MainMenu.this);
                if (topFrame instanceof GameFrame) {
                    ((GameFrame) topFrame).showMapSelectionScreen();
                }
            }
        });

        JLabel highScoreLabel = new JLabel("High Score");
        customizeLabel(highScoreLabel);
        highScoreLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("High Score button clicked");
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(MainMenu.this);
                showHighScoreDialog(topFrame); // Open high score dialog
            }
        });

        JLabel exitLabel = new JLabel("Exit");
        customizeLabel(exitLabel);
        exitLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Exit button clicked");
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(MainMenu.this);
                topFrame.dispose(); // Close the game frame
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0); // Add some vertical spacing between components

        add(startGameLabel, gbc);

        gbc.gridy++;
        add(highScoreLabel, gbc);

        gbc.gridy++;
        add(exitLabel, gbc);
    }

    private void customizeLabel(JLabel label) {
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setForeground(Color.YELLOW); // Set text color to yellow
        label.setBackground(Color.BLACK); // Set background color to black
        label.setOpaque(true);
    }

    private void showHighScoreDialog(JFrame parentFrame) {
        // Simulate showing high scores in a dialog (replace with your actual high score logic)
        JOptionPane.showMessageDialog(parentFrame, "High Scores will be shown here.",
                "High Scores", JOptionPane.INFORMATION_MESSAGE);
    }
}
