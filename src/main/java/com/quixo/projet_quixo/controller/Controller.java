package com.quixo.projet_quixo.controller;


import com.quixo.projet_quixo.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Controller {

    public Button btnText;
    private Game game;
    private boolean isIA = false;
    @FXML
    private Label displayingCurrentPlayer;
    QuixoMinMaxThread qm = new QuixoMinMaxThread();
    @FXML
    private Rectangle rect00;
    @FXML
    private Rectangle rect01;
    @FXML
    private Rectangle rect02;
    @FXML
    private Rectangle rect03;
    @FXML
    private Rectangle rect04;
    @FXML
    private Rectangle rect10;
    @FXML
    private Rectangle rect11;
    @FXML
    private Rectangle rect12;
    @FXML
    private Rectangle rect13;
    @FXML
    private Rectangle rect14;
    @FXML
    private Rectangle rect20;
    @FXML
    private Rectangle rect21;
    @FXML
    private Rectangle rect22;
    @FXML
    private Rectangle rect23;
    @FXML
    private Rectangle rect24;
    @FXML
    private Rectangle rect30;
    @FXML
    private Rectangle rect31;
    @FXML
    private Rectangle rect32;
    @FXML
    private Rectangle rect33;
    @FXML
    private Rectangle rect34;
    @FXML
    private Rectangle rect40;
    @FXML
    private Rectangle rect41;
    @FXML
    private Rectangle rect42;
    @FXML
    private Rectangle rect43;
    @FXML
    private Rectangle rect44;

    private Rectangle[] bords = new Rectangle[16];

    private Rectangle[] des = new Rectangle[25];

    private Node firstNode;
    QuixoMinMax q = new QuixoMinMax();

    @FXML
    private void initialize() {
        game = Game.getInstance();

        displayingCurrentPlayer.setText("Joueur "+ game.nomJoeur(game.getCurrentPlayer().getPion()) +" : a vous de jouer !");
        initializeBoard();
        initialiseDes();
        setChooseHelp();
    }

    private void initializeBoard() {
        bords[0]=rect00;
        bords[1]=rect01;
        bords[2]=rect02;
        bords[3]=rect03;
        bords[4]=rect04;
        bords[5]=rect10;
        bords[6]=rect14;
        bords[7]=rect20;
        bords[8]=rect24;
        bords[9]=rect30;
        bords[10]=rect34;
        bords[11]=rect40;
        bords[12]=rect41;
        bords[13]=rect42;
        bords[14]=rect43;
        bords[15]=rect44;
    }
    private void initialiseDes() {
        //Grille
        des[0]=rect00;
        des[1]=rect01;
        des[2]=rect02;
        des[3]=rect03;
        des[4]=rect04;
        des[5]=rect10;
        des[6]=rect11;
        des[7]=rect12;
        des[8]=rect13;
        des[9]=rect14;
        des[10]=rect20;
        des[11]=rect21;
        des[12]=rect22;
        des[13]=rect23;
        des[14]=rect24;
        des[15]=rect30;
        des[16]=rect31;
        des[17]=rect32;
        des[18]=rect33;
        des[19]=rect34;
        des[20]=rect40;
        des[21]=rect41;
        des[22]=rect42;
        des[23]=rect43;
        des[24]=rect44;
    }



    //
    @FXML
    private void onNouvelle(ActionEvent actionEvent) {
        game.newGame();
        resetDes();
        resetAides();
        setChooseHelp();
    }
    private void resetDes() {
        for(int i = 0; i < des.length; i++) {
            des[i].setFill(Color.WHITE);
        }
    }

    // fonction permettant de verifier la fin de la partie
    @FXML
    private void endGame() {
        if (game.player1Won.get() && game.player2Won.get()) {
            displayingCurrentPlayer.setText("Joueur "+ game.nomJoeur(game.getCurrentPlayer().getPion()) +" : a vous de jouer !");
        }

        if (game.player2Won.get()) {
            showResult(game.player2.getNomJoueur());
            game.newGame();
            resetDes();
            resetAides();
            setChooseHelp();
        }

        if (game.player1Won.get()) {
            showResult(game.player1.getNomJoueur());
            game.newGame();
            resetDes();
            resetAides();
            setChooseHelp();
        }
        else {
            displayingCurrentPlayer.setText("Joueur "+ game.nomJoeur(game.getCurrentPlayer().getPion()) +" : a vous de jouer !");
        }
    }
    //Affiche un Dialog lorsque le heu est fini
    public static Alert showResult(String gagnant) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("RESULTAT");
        alert.setHeaderText("Cette partie est termiee");
        alert.setContentText(gagnant+" a gagné la partie !");
        alert.showAndWait();
        return alert;
    }

    // fontion appele lorsque le joue clique sur le plateau
    @FXML
    private void onPress(MouseEvent mouseEvent) {

        Node clickedNode = mouseEvent.getPickResult().getIntersectedNode();
        int x = GridPane.getColumnIndex(clickedNode) != null ? GridPane.getColumnIndex(clickedNode) : 0;
        int y = GridPane.getRowIndex(clickedNode) != null ? GridPane.getRowIndex(clickedNode) : 0;
        if(game.getPos().getX() == -1) {
            try {
                firstNode = clickedNode;
                game.choosePion(x, y);
                resetAides();
                setInsertionHelp(x, y);
            } catch (IllegalArgumentException e) {
            }
        }
        else {
            try {
                game.insertAt(x, y);
                updateTheBoard();
                game.prepareWinningConditions();
                System.out.println(game.getCurrentPlayer().getPion());
                System.out.println(game.player2.getPion());
                System.out.println(game.getCurrentPlayer().getPion() == game.player2.getPion());
                if (isIA && game.getCurrentPlayer().getPion() == game.player2.getPion()) {

                    Move move = q.getBestMove(game, game.player2, game.player1);

                    game.prepareWinningConditions();
                    game.moving(move.getX2(), move.getY2(), new Position(move.getX1(), move.getY1()));;
                    System.out.println("iA playing");
                    game.prepareWinningConditions();
                    updateTheBoard();

                    //jeu.changePlayer();
                }
                endGame();
                resetAides();
                if (!isIA) setChooseHelp();

            } catch (IllegalStateException e) {
            } catch (IllegalArgumentException e) {
            }
        }
    }

    // fontion de mise a jour du tanleau de jeu
    private void updateTheBoard() {
        Color couleur = null;
        for(int x = 0; x < 5; x++) {
            for(int y = 0; y < 5; y++) {
                Pion pion = game.getPlateau(x, y).get();
                if (pion == Pion.CROIX) {
                    couleur = Color.DARKBLUE;
                }else if (pion == Pion.ROND) {
                    couleur = Color.DARKRED;
                }else {
                    couleur = Color.WHITE;
                }
                switch(x) {
                    case 0:
                        switch(y) {
                            case 0:
                                rect00.setFill(couleur);
                                break;
                            case 1:
                                rect01.setFill(couleur);
                                break;
                            case 2:
                                rect02.setFill(couleur);
                                break;
                            case 3:
                                rect03.setFill(couleur);
                                break;
                            case 4:
                                rect04.setFill(couleur);
                                break;
                        }
                    case 1:
                        switch(y) {
                            case 0:
                                rect10.setFill(couleur);
                                break;
                            case 1:
                                rect11.setFill(couleur);
                                break;
                            case 2:
                                rect12.setFill(couleur);
                                break;
                            case 3:
                                rect13.setFill(couleur);
                                break;
                            case 4:
                                rect14.setFill(couleur);
                                break;
                        }
                    case 2:
                        switch(y) {
                            case 0:
                                rect20.setFill(couleur);
                                break;
                            case 1:
                                rect21.setFill(couleur);
                                break;
                            case 2:
                                rect22.setFill(couleur);
                                break;
                            case 3:
                                rect23.setFill(couleur);
                                break;
                            case 4:
                                rect24.setFill(couleur);
                                break;
                        }
                    case 3:
                        switch(y) {
                            case 0:
                                rect30.setFill(couleur);
                                break;
                            case 1:
                                rect31.setFill(couleur);
                                break;
                            case 2:
                                rect32.setFill(couleur);
                                break;
                            case 3:
                                rect33.setFill(couleur);
                                break;
                            case 4:
                                rect34.setFill(couleur);
                                break;
                        }
                    case 4:
                        switch(y) {
                            case 0:
                                rect40.setFill(couleur);
                                break;
                            case 1:
                                rect41.setFill(couleur);
                                break;
                            case 2:
                                rect42.setFill(couleur);
                                break;
                            case 3:
                                rect43.setFill(couleur);
                                break;
                            case 4:
                                rect44.setFill(couleur);
                                break;
                        }
                }
            }
        }
    }

    // fontion permettant d'afficher l'aide sur les positions possible d'insertion
    private void setInsertionHelp(int x, int y) {
        leftHelp(y);
        rightHelp(y);
        topHelp(x);
        bottomHelp(x);
    }

    private void leftHelp(int y) {
        if(game.isLeftPossible()) {
            switch(y) {
                case 0:
                    rect00.setStroke(Color.BLACK);
                    rect00.setStrokeWidth(1.4);
                    break;
                case 1:
                    rect01.setStroke(Color.BLACK);
                    rect01.setStrokeWidth(1.4);
                    break;
                case 2:
                    rect02.setStroke(Color.BLACK);
                    rect02.setStrokeWidth(1.4);
                    break;
                case 3:
                    rect03.setStroke(Color.BLACK);
                    rect03.setStrokeWidth(1.4);
                    break;
                case 4:
                    rect04.setStroke(Color.BLACK);
                    rect04.setStrokeWidth(1.4);
                    break;
            }
        }
    }

    private void rightHelp(int y) {
        if(game.isRightPossible()) {
            switch(y) {
                case 0:
                    rect40.setStroke(Color.BLACK);
                    rect40.setStrokeWidth(1.4);
                    break;
                case 1:
                    rect41.setStroke(Color.BLACK);
                    rect41.setStrokeWidth(1.4);
                    break;
                case 2:
                    rect42.setStroke(Color.BLACK);
                    rect42.setStrokeWidth(1.4);
                    break;
                case 3:
                    rect43.setStroke(Color.BLACK);
                    rect43.setStrokeWidth(1.4);
                    break;
                case 4:
                    rect44.setStroke(Color.BLACK);
                    rect44.setStrokeWidth(1.4);
                    break;
            }
        }
    }

    private void topHelp(int x) {
        if(game.isTopPossible()) {
            switch(x) {
                case 0:
                    rect00.setStroke(Color.BLACK);
                    rect00.setStrokeWidth(1.4);
                    break;
                case 1:
                    rect10.setStroke(Color.BLACK);
                    rect10.setStrokeWidth(1.4);
                    break;
                case 2:
                    rect20.setStroke(Color.BLACK);
                    rect20.setStrokeWidth(1.4);
                    break;
                case 3:
                    rect30.setStroke(Color.BLACK);
                    rect30.setStrokeWidth(1.4);
                    break;
                case 4:
                    rect40.setStroke(Color.BLACK);
                    rect40.setStrokeWidth(1.4);
                    break;
            }
        }
    }

    private void bottomHelp(int x) {
        if(game.isBottomPossible()) {
            switch(x) {
                case 0:
                    rect04.setStroke(Color.BLACK);
                    rect04.setStrokeWidth(1.4);
                    break;
                case 1:
                    rect14.setStroke(Color.BLACK);
                    rect14.setStrokeWidth(1.4);
                    break;
                case 2:
                    rect24.setStroke(Color.BLACK);
                    rect24.setStrokeWidth(1.4);
                    break;
                case 3:
                    rect34.setStroke(Color.BLACK);
                    rect34.setStrokeWidth(1.4);
                    break;
                case 4:
                    rect44.setStroke(Color.BLACK);
                    rect44.setStrokeWidth(1.4);
                    break;
            }
        }
    }

    private void resetAides() {
        for(int i = 0; i < bords.length; i++) {
            bords[i].setStroke(Paint.valueOf("#9d9d9d"));
            bords[i].setStrokeWidth(1.4);
        }
    }

    // fontion permettant de mettre a jour l'affichage d'aide pour le choix
    private void setChooseHelp() {
        for(int x = 0; x < 5; x+=4) {
            for(int y = 0; y < 5; y++) {
                if(game.isChooseAllowed(x, y)) {
                    switch(x) {
                        case 0:
                            switch(y) {
                                case 0:
                                    rect00.setStroke(Color.BLACK);
                                    rect00.setStrokeWidth(1.4);
                                    break;
                                case 1:
                                    rect01.setStroke(Color.BLACK);
                                    rect01.setStrokeWidth(1.4);
                                    break;
                                case 2:
                                    rect02.setStroke(Color.BLACK);
                                    rect02.setStrokeWidth(1.4);
                                    break;
                                case 3:
                                    rect03.setStroke(Color.BLACK);
                                    rect03.setStrokeWidth(1.4);
                                    break;
                                case 4:
                                    rect04.setStroke(Color.BLACK);
                                    rect04.setStrokeWidth(1.4);
                                    break;
                            }
                            break;
                        case 4:
                            switch(y) {
                                case 0:
                                    rect40.setStroke(Color.BLACK);
                                    rect40.setStrokeWidth(1.4);
                                    break;
                                case 1:
                                    rect41.setStroke(Color.BLACK);
                                    rect41.setStrokeWidth(1.4);
                                    break;
                                case 2:
                                    rect42.setStroke(Color.BLACK);
                                    rect42.setStrokeWidth(1.4);
                                    break;
                                case 3:
                                    rect43.setStroke(Color.BLACK);
                                    rect43.setStrokeWidth(1.4);
                                    break;
                                case 4:
                                    rect44.setStroke(Color.BLACK);
                                    rect44.setStrokeWidth(1.4);
                                    break;
                            }
                            break;
                    }
                }
            }
        }
        for(int x = 1; x < 4; x++) {
            for(int y = 0; y < 5; y+=4) {
                if(game.isChooseAllowed(x, y)) {
                    switch(x) {
                        case 1:
                            switch(y) {
                                case 0:
                                    rect10.setStroke(Color.BLACK);
                                    rect10.setStrokeWidth(1.4);
                                    break;
                                case 4:
                                    rect14.setStroke(Color.BLACK);
                                    rect14.setStrokeWidth(1.4);
                                    break;
                            }
                            break;
                        case 2:
                            switch(y) {
                                case 0:
                                    rect20.setStroke(Color.BLACK);
                                    rect20.setStrokeWidth(1.4);
                                    break;
                                case 4:
                                    rect24.setStroke(Color.BLACK);
                                    rect24.setStrokeWidth(1.4);
                                    break;
                            }
                            break;
                        case 3:
                            switch(y) {
                                case 0:
                                    rect30.setStroke(Color.BLACK);
                                    rect30.setStrokeWidth(1.4);
                                    break;
                                case 4:
                                    rect34.setStroke(Color.BLACK);
                                    rect34.setStrokeWidth(1.4);
                                    break;
                            }
                            break;
                    }
                }
            }
        }
    }

    public void newGameWithIA(ActionEvent actionEvent) {
        this.isIA = !isIA;
        if (isIA) {
            this.btnText.setText("Mode 2 Joueur");
        }else {
            this.btnText.setText("1jouer Vs IA");
        }
        game.newGame();
        resetDes();
        resetAides();
        setChooseHelp();
    }
}