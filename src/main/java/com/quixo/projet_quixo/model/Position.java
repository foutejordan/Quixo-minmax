package com.quixo.projet_quixo.model;

public class Position {

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    int x;

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }


}

