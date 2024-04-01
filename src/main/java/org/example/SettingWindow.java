package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingWindow extends JFrame {
    private static final int WINDOW_HEIGHT = 230;
    private static final int WINDOW_WIDTH = 350;
    private static final int mode = 0;
    private static int fSzX = 3;
    private static int fSzY = 3;
    private static int wLen = 3;
    JButton btnStart = new JButton("Start new game");

    JRadioButton three_three = new JRadioButton("3 X 3");
    JRadioButton four_four = new JRadioButton("4 X 4");
    JRadioButton five_five = new JRadioButton("5 X 5");

    SettingWindow(GameWindow gameWindow) {
        setLocationRelativeTo(gameWindow);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameWindow.startNewGame(mode, fSzX, fSzY, wLen);
                setVisible(false);
            }
        });
        GridLayout gridLayout = new GridLayout(1, 3);
        JPanel jPanel = new JPanel();
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(three_three);
        buttonGroup.add(four_four);
        buttonGroup.add(five_five);
        jPanel.add(three_three);
        jPanel.add(four_four);
        jPanel.add(five_five);
        three_three.setSelected(true);
        jPanel.setLayout(gridLayout);
        getContentPane().add(jPanel);
        add(btnStart, BorderLayout.SOUTH);
        pack();

        three_three.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fSzX = 3;
                fSzY = 3;
                wLen = 3;
            }
        });

        four_four.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fSzX = 4;
                fSzY = 4;
                wLen = 4;
            }
        });

        five_five.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fSzX = 5;
                fSzY = 5;
                wLen = 5;
            }
        });
    }

    public static int getfSzX() {
        return fSzX;
    }

    public static int getfSzY() {
        return fSzY;
    }

    public static int getwLen() {
        return wLen;
    }
}
