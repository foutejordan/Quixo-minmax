package com.quixo.projet_quixo.model;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

public class InsertBottom implements StrategyInsertion{

    @Override
    public void insertion(SimpleObjectProperty<Pion>[][] board, Position pos, Player currentPlayer) {
        if (pos.getX() == -1) throw new IllegalStateException("You have first to choose a piece to move!");
        if (pos.getY() == 4) throw new IllegalArgumentException("You cannot put the piece where it was!");
        int id = pos.getX();;
        for (int y = pos.getY(); y <4 ; ++y) {
            board[y][id].set(board[y+1][id].get());
        }
        board[4][id].set(currentPlayer.getPion());
    }
}
