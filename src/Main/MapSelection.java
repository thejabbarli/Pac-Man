package Main;

import Main.GameFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MapSelection extends JPanel {

    public MapSelection(GameFrame gameFrame) {
        setBackground(Color.BLACK);
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(800, 600));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel backButton = new JLabel("<");
        customizeLabel(backButton);
        backButton.setFont(new Font("Arial", Font.BOLD, 20));
        backButton.setForeground(Color.YELLOW);
        backButton.setBackground(Color.BLACK);
        backButton.setOpaque(true);
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                gameFrame.showMainMenu();
            }
        });

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 0;
        add(backButton, gbc);

        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridy = 1;

        JLabel map1Label = new JLabel("   10x10   ");
        customizeLabel(map1Label);
        map1Label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                gameFrame.setMapConfig("map10x10");
                gameFrame.startGame("res/maps/map10x10.txt");
            }
        });
        add(map1Label, gbc);

        JLabel map2Label = new JLabel("   13x10   ");
        customizeLabel(map2Label);
        map2Label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                gameFrame.setMapConfig("map13x10");
                gameFrame.startGame("res/maps/map13x10.txt");
            }
        });
        gbc.gridy++;
        add(map2Label, gbc);

        JLabel map3Label = new JLabel("   testmap 6x6   ");
        customizeLabel(map3Label);
        map3Label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                gameFrame.setMapConfig("testmap6x6");
                gameFrame.startGame("res/maps/testmap6x6.txt");
            }
        });
        gbc.gridy++;
        add(map3Label, gbc);

        JLabel map4Label = new JLabel("   20x20   ");
        customizeLabel(map4Label);
        map4Label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                gameFrame.setMapConfig("map20x20");
                gameFrame.startGame("res/maps/map20x20.txt");
            }
        });
        gbc.gridy++;
        add(map4Label, gbc);

    }

    private void customizeLabel(JLabel label) {
        label.setFont(new Font("Arial", Font.BOLD, 30));
        label.setForeground(Color.YELLOW);
        label.setBackground(Color.BLACK);
        label.setOpaque(true);
    }
}
