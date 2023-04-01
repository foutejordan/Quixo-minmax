package com.quixo.projet_quixo.model;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

public class InsertionContext {

    private StrategyInsertion strategyInsertion;

    public InsertionContext(StrategyInsertion strategyInsertion){
        this.strategyInsertion = strategyInsertion;
    }

    public void getInsertion(SimpleObjectProperty<Pion>[][] board , Position pos, Player currentPlayer) {
        strategyInsertion.insertion(board, pos, currentPlayer);
    }
}
