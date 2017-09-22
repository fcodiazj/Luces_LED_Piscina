package com.example.francisco.luces.com.example.francisco.entities;

/**
 * Created by Francisco on 20-09-2017.
 */

public class Luces{
    private int idLuz;
    private String color;
    private int posFila;
    private int posCol;

    public int getIdLuz() {
        return idLuz;
    }

    public void setIdLuz(int idLuz) {
        this.idLuz = idLuz;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getPosFila() {
        return posFila;
    }

    public void setPosFila(int posFila) {
        this.posFila = posFila;
    }

    public int getPosCol() {
        return posCol;
    }

    public void setPosCol(int posCol) {
        this.posCol = posCol;
    }
}
