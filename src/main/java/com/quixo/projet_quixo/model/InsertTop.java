package com.quixo.projet_quixo.model;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

public class InsertTop implements StrategyInsertion{

    @Override
    public void insertion(SimpleObjectProperty<Pion>[][] board, Position pos, Player currentPlayer) {
        if (pos.getX() == -1) throw new IllegalStateException("Vous devez d'abord choisir la piece a deplacer");
        if (pos.getY() == 0) throw new IllegalStateException("Vous ne pouvez pas poser la piece ou c'etait");
        int id = pos.getX();;
        for (int y = pos.getY(); y > 0 ; --y) {
            board[y][id].set(board[y-1][id].get());
        }
        board[0][id].set(currentPlayer.getPion());
    }
}
