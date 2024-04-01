package org.example;

import java.util.Arrays;

public class SaveFile {
    private char[][] field;
    private int fieldSizeY;
    private int fieldSizeX;
    private int dotsToWin;

    public char[][] getField() {
        return field;
    }

    public void setField(char[][] field) {
        this.field = field;
    }

    public int getFieldSizeY() {
        return fieldSizeY;
    }

    public void setFieldSizeY(int fieldSizeY) {
        this.fieldSizeY = fieldSizeY;
    }

    public int getFieldSizeX() {
        return fieldSizeX;
    }

    public void setFieldSizeX(int fieldSizeX) {
        this.fieldSizeX = fieldSizeX;
    }

    public int getDotsToWin() {
        return dotsToWin;
    }

    public void setDotsToWin(int dotsToWin) {
        this.dotsToWin = dotsToWin;
    }

    @Override
    public String toString() {
        return "SaveFile{" +
                "field=" + Arrays.toString(field) +
                ", fieldSizeY=" + fieldSizeY +
                ", fieldSizeX=" + fieldSizeX +
                ", dotsToWin=" + dotsToWin +
                '}';
    }
}
