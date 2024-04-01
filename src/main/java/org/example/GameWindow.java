package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameWindow extends JFrame {
    private static final int WINDOW_HEIGHT = 555;
    private static final int WINDOW_WIDTH = 507;
    private static final int WINDOW_POSX = 800;
    private static final int WINDOW_POSY = 300;

    JButton btnStart = new JButton("New game");
    JButton btnExit = new JButton("Exit");
    JButton btnSave = new JButton("Save Game");
    JButton btnLoad = new JButton("Load Game");
    Map map;
    SettingWindow settings;
    GameWindow(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocation(WINDOW_POSX,WINDOW_POSY);
        setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
        setTitle("TIC TAC TOE");
//        setResizable(false);

        map = new Map();
        settings = new SettingWindow(this);
        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settings.setVisible(true);
            }
        });

        btnLoad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadGame();
            }
        });

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveGame();
            }
        });
        JPanel panButton = new JPanel(new GridLayout(2,2));
        panButton.add(btnStart);
        panButton.add(btnExit);
        panButton.add(btnLoad);
        panButton.add(btnSave);
        add(panButton,BorderLayout.SOUTH);
        add(map);
        setVisible(true);
    }

    void startNewGame(int mode,int fSzX,int fSzY,int wLen){
        map.startNewGame(mode,fSzX,fSzY,wLen);
    }

    void loadGame(){
        map.load();
        map.continuePlaying();
    }

    void saveGame(){
        map.saveGame();
    }
}
