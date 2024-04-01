package org.example;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.JsonParserDelegate;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;
import java.util.Set;

public class Map extends JPanel {
    // Cell H/W variables
    private int panelWidth;
    private int panelHeight;
    private int cellHeight;
    private int cellWidth;


    //State variables
    private int gameOverType;
    private static final int STATE_DRAW = 0;
    private static final int STATE_WIN_HUMAN = 1;
    private static final int STATE_WIN_AI = 2;

    private boolean isGameOver;
    private boolean isInitialized;

    //Game state messages
    private static final String MSG_WIN_HUMAN = "Human wins!";
    private static final String MSG_WIN_AI = "AI wins";
    private static final String MSG_DRAW = "Draw";
    //AI dots randomer
    private static final Random RANDOM = new Random();

    //Dots
    private final int HUMAN_DOT = 1;
    private final int AI_DOT = 2;
    private final int EMPTY_DOT = 0;

    //Padding for oval render
    private final int DOT_PADDING = 5;
    //Field sizes
    private Integer fieldSizeY;
    private Integer fieldSizeX;
    private Integer dotsToWin;
    private char[][] field;

    public Map() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                update(e);
            }
        });
        isInitialized = false;
    }

    private void update(MouseEvent e) {
        if (isGameOver || !isInitialized) return;
        int cellX = e.getX() / cellWidth;
        int cellY = e.getY() / cellHeight;
        if (!isValidCell(cellX, cellY) || !isEmptyCell(cellX, cellY)) return;
        field[cellY][cellX] = HUMAN_DOT;
        repaint();
        if (checkEndGame(HUMAN_DOT, STATE_WIN_HUMAN)) return;
        aiTurn();
        repaint();
        if (checkEndGame(AI_DOT, STATE_WIN_AI)) return;
    }

    void startNewGame(int mode, int fSzX, int fSzY, int wLen) {
        System.out.printf("Mode: %d;\n Size: x = %d, y = %d;\n Win Length: %d", mode, fSzX, fSzY, wLen);
        initMap();
        isGameOver = false;
        isInitialized = true;
        repaint();
    }

    void continuePlaying(){
        isGameOver = false;
        isInitialized = true;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        render(g);
    }

    private void render(Graphics g) {
        if (!isInitialized) return;
        panelWidth = getWidth();
        panelHeight = getHeight();
        cellHeight = panelHeight / fieldSizeY;
        cellWidth = panelHeight / fieldSizeX;

        g.setColor(Color.BLACK);

        for (int h = 0; h < fieldSizeY; h++) {
            int y = h * cellHeight;
            g.drawLine(0, y, panelWidth, y);
        }

        for (int w = 0; w < fieldSizeX; w++) {
            int x = w * cellWidth;
            g.drawLine(x, 0, x, panelHeight);
        }

        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                if (field[y][x] == EMPTY_DOT) continue;

                if (field[y][x] == HUMAN_DOT) {
                    g.setColor(Color.BLUE);
                    g.fillOval(x * cellWidth + DOT_PADDING,
                            y * cellHeight + DOT_PADDING,
                            cellWidth - DOT_PADDING * 2,
                            cellHeight - DOT_PADDING * 2);
                } else if (field[y][x] == AI_DOT) {
                    g.setColor(Color.RED);
                    g.fillOval(x * cellWidth + DOT_PADDING,
                            y * cellHeight + DOT_PADDING,
                            cellWidth - DOT_PADDING * 2,
                            cellHeight - DOT_PADDING * 2);
                } else throw new RuntimeException("Unexpected value" + field[y][x] + "in cell: x =" + x + " y=" + y);
            }
        }

        if (isGameOver) showMessageGameOver(g);
    }

    private void aiTurn() {
        int x;
        int y;
        do {
            x = RANDOM.nextInt(fieldSizeX);
            y = RANDOM.nextInt(fieldSizeY);
        } while (!isEmptyCell(x, y));
        field[y][x] = AI_DOT;
    }

    private boolean checkWin(int symbol) {

        //horizontal check
        for (int i = 0; i < fieldSizeY; i++) {
            int dotsCounter = 0;
            for (int j = 0; j < fieldSizeX; j++) {
                if (field[i][j] == symbol) {
                    dotsCounter++;
                }
                if (dotsCounter == dotsToWin) return true;
            }
        }
        //vertical check
        for (int i = 0; i < fieldSizeY; i++) {
            int dotsCounter = 0;
            for (int j = 0; j < fieldSizeX; j++) {
                if (field[j][i] == symbol) {
                    dotsCounter++;
                }
                if (dotsCounter == dotsToWin) return true;
            }
        }

        int dotsCounterDiagonal = 0;
        for (int i = 0; i < fieldSizeY; i++) {
            if (field[i][i] == symbol) {
                dotsCounterDiagonal++;
                if (dotsCounterDiagonal == dotsToWin) return true;
            }
        }

        int dotsCounterDiagonalReversed = 0;
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = fieldSizeX - i - 1; j >= 0; j--) {
                if (field[i][j] == symbol) {
                    dotsCounterDiagonalReversed++;
                    if (dotsCounterDiagonalReversed == dotsToWin) return true;
                }
                break;
            }
        }


        return false;
    }

    private void initMap() {


                fieldSizeY = SettingWindow.getfSzY();
                fieldSizeX = SettingWindow.getfSzX();
                dotsToWin = SettingWindow.getwLen();
                field = new char[fieldSizeY][fieldSizeX];
                for (int i = 0; i < fieldSizeY; i++) {
                    for (int j = 0; j < fieldSizeX; j++) {
                        field[i][j] = EMPTY_DOT;
                    }
                }


    }

    private boolean isValidCell(int x, int y) {
        return x >= 0 && x < fieldSizeX && y >= 0 && y < fieldSizeY;
    }

    private boolean isEmptyCell(int x, int y) {
        return field[y][x] == EMPTY_DOT;
    }

    private boolean isMapFull() {
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                if (field[i][j] == EMPTY_DOT) return false;
            }
        }
        return true;
    }

    private boolean checkEndGame(int dot, int gameOverType) {
        if (checkWin(dot)) {
            this.gameOverType = gameOverType;
            isGameOver = true;
            repaint();
            return true;
        }
        if (isMapFull()) {
            this.gameOverType = STATE_DRAW;
            isGameOver = true;
            repaint();
            return true;
        }
        return false;
    }

    private void showMessageGameOver(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 200, getWidth(), 60);
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Times new roman", Font.BOLD, 48));
        switch (gameOverType) {
            case STATE_DRAW -> g.drawString(MSG_DRAW, 180, getHeight() / 2);
            case STATE_WIN_AI -> g.drawString(MSG_WIN_AI, 20, getHeight() / 2);
            case STATE_WIN_HUMAN -> g.drawString(MSG_WIN_HUMAN, 70, getHeight() / 2);
            default -> throw new RuntimeException("Unexpected gameOver state: " + gameOverType);
        }
        load();
    }

    void saveGame() {
        ObjectMapper objectMapper = new ObjectMapper();
        SaveFile saveFile = new SaveFile();
        saveFile.setField(field);
        saveFile.setFieldSizeX(fieldSizeX);
        saveFile.setFieldSizeY(fieldSizeY);
        saveFile.setDotsToWin(dotsToWin);

        String result;
        try {
            result = objectMapper.writeValueAsString(saveFile);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println(result);
        try {

            // Writing to a file
            File file = new File("src/main/resources/save.json");
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            System.out.println("Writing JSON object to file");
            System.out.println("-----------------------");
            System.out.print(result);

            fileWriter.write(result);
            fileWriter.flush();
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void load() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            SaveFile saveFile = objectMapper.readValue(new File("src/main/resources/save.json"), SaveFile.class);
            field = saveFile.getField();
            dotsToWin = saveFile.getDotsToWin();
            fieldSizeX = saveFile.getFieldSizeX();
            fieldSizeY = saveFile.getFieldSizeY();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

