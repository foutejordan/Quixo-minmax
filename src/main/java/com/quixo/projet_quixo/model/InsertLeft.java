package com.quixo.projet_quixo.model;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

public class InsertLeft implements StrategyInsertion{

    @Override
    public void insertion(SimpleObjectProperty<Pion>[][] board, Position pos, Player currentPlayer) {
        if (pos.getX() == -1) throw new IllegalStateException("You have first to choose a piece to move!");
        if (pos.getX() == 0) throw new IllegalArgumentException("You cannot put the piece where it was!");
        int id = pos.getY();;
        for (int x = pos.getX(); x > 0 ; --x) {
            board[id][x].set(board[id][x-1].get());
        }
        board[id][0].set(currentPlayer.getPion());
    }
}
