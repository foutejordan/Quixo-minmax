package com.quixo.projet_quixo.model;


import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.SimpleObjectProperty;

public final class Game implements Cloneable {

    public static Game uniqueInstance;
    public static final Pion couleurVide = Pion.BLANC;
    public static final Pion blanc = Pion.BLANC;


    // public Player player1 = new Player("jordan",);
    public Player player1 = new Player("BLEU", Pion.CROIX);

    public Player player2 = new Player("ROUGE", Pion.ROND);


    public final String nomJoeur(Pion pion) {
        if (couleurVide.equals(pion)) {
            return "Aucun";
        } else if (player1.getPion().equals(pion)) {
            return player1.getNomJoueur();
        } else if (player2.getPion().equals(pion)) {
            return player2.getNomJoueur();
        }
        return "inconnu";
    }

    public Game clone(){
        try {
            return (Game) super.clone();
        }catch (CloneNotSupportedException e){
            e.printStackTrace();
            return null;
        }
    }

    // creation des deux joeur de la partie (nom + couleur)
    private Player currentPlayer = new Player(player1.getNomJoueur(), player1.getPion());

    public Player getProchainJoueur() {
        return prochainJoueur;
    }

    private Player prochainJoueur = new Player(player2.getNomJoueur(), player2.getPion());

    // Variable qui va contenir la position de la piece choisi
    private  Position pos = new Position(-1,-1);

    public SimpleObjectProperty<Pion>[][] getPlateau() {
        return plateau;
    }

    // initialisation du plateau de jeu 5*5, de type SimpleObjectProperty pour faciliter le dataBinding en javaFx
    // et donc faciliter la synchronisation lorsqu'il y'a une modificaion
    @SuppressWarnings("unchecked")
    private final SimpleObjectProperty<Pion>[][] plateau = new SimpleObjectProperty[5][5];


    // recuperation du joueur courant
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    // isTopPossi retourne vrai si on peut mettre le pion choisi  tout en haut du plateau
    public boolean isTopPossible() {
        return pos.x != -1 && pos.y != 0;
    }
    // isTopPossi retourne vrai si on peut mettre le pion choisi  tout en bas du plateau
    public boolean isBottomPossible() {
        return pos.x != -1 && pos.y != 4;
    }

    // isTopPossi retourne vrai si on peut mettre le pion choisi a gauche du plateau
    public boolean isLeftPossible() {
        return pos.x != 0;
    }

    // isTopPossi retourne vrai si on peut mettre le pion choisi a droite du plateau
    public boolean isRightPossible() {
        return pos.x != 4;
    }
    /** Boolean intermediate expressions to compute the winner */

    private final BooleanExpression[] player1WonLine = new BooleanExpression[5];
    private final BooleanExpression[] player1WonCol = new BooleanExpression[5];
    private final BooleanExpression[] player2WonLine = new BooleanExpression[5];
    private final BooleanExpression[] player2WonCol = new BooleanExpression[5];
    private final BooleanExpression[] player1WonDiagonale = new BooleanExpression[5];
    private final BooleanExpression[] player2WonDiagonale = new BooleanExpression[5];

    public BooleanExpression player1Won;

    public BooleanExpression player2Won;


    private Game() {
        // Builds the board with 5*5 neutral dice
        for (int y=0; y<5; ++y)
            for (int x=0; x<5; ++x) {
                plateau[y][x] = new SimpleObjectProperty<>(Pion.BLANC);
            }
        // Prepares the winning conditions
        prepareWinningConditions();

    }
    public void prepareWinningConditions() {
        player2WonDiagonale[0] = plateau[0][0].isEqualTo(player2.getPion());

        player2WonDiagonale[1] = plateau[4][0].isEqualTo(player2.getPion());

        player1WonDiagonale[0] = plateau[0][0].isEqualTo(player1.getPion());

        player1WonDiagonale[1] = plateau[4][0].isEqualTo(player1.getPion());

        for (int i=0; i<5; ++i) {
            player2WonCol[i] = plateau[0][i].isEqualTo(player2.getPion());
            player1WonCol[i] = plateau[0][i].isEqualTo(player1.getPion());

            player2WonLine[i] = plateau[i][0].isEqualTo(player2.getPion());
            player1WonLine[i] = plateau[i][0].isEqualTo(player1.getPion());
        }

        for (int i=1; i<5; ++i) {
            player2WonDiagonale[0] = player2WonDiagonale[0].and(plateau[i][i].isEqualTo(player2.getPion()));
            player2WonDiagonale[1] = player2WonDiagonale[1].and(plateau[4-i][i].isEqualTo(player2.getPion()));

            player1WonDiagonale[0] = player1WonDiagonale[0].and(plateau[i][i].isEqualTo(player1.getPion()));
            player1WonDiagonale[1] = player1WonDiagonale[1].and(plateau[4-i][i].isEqualTo(player1.getPion()));
            for (int j=0; j<5; ++j) {
                player2WonCol[j] = player2WonCol[j].and(plateau[i][j].isEqualTo(player2.getPion()));
                player1WonCol[j] = player1WonCol[j].and(plateau[i][j].isEqualTo(player1.getPion()));

                player2WonLine[j] = player2WonLine[j].and(plateau[j][i].isEqualTo(player2.getPion()));
                player1WonLine[j] = player1WonLine[j].and(plateau[j][i].isEqualTo(player1.getPion()));
            }
        }
        BooleanExpression player1Won = player1WonDiagonale[0].or(player1WonDiagonale[1]);
        BooleanExpression player2Won = player2WonDiagonale[0].or(player2WonDiagonale[1]);

        for (int i = 0; i < 5; i++) {
            player1Won = player1Won.or(player1WonCol[i]).or(player1WonLine[i]);
            player2Won = player2Won.or(player2WonCol[i]).or(player2WonLine[i]);
        }
        this.player1Won = player1Won;
        this.player2Won = player2Won;
    }
    // creation d'une copie du jeu
    public Game(Game game) {
        this();
        this.pos = game.pos;
        this.prochainJoueur = game.prochainJoueur;
        this.currentPlayer = game.currentPlayer;
        this.player1 = game.player1;
        this.player2 = game.player2;
        this.player1Won = game.player1Won;
        this.player2Won = game.player2Won;
        for (int y=0; y<5; ++y)
            for (int x=0; x<5; ++x)
                this.plateau[y][x].set(game.plateau[y][x].get());
        if (this.currentPlayer.getPion() != game.currentPlayer.getPion())
            changePlayer();
    }

    public boolean gameIsOver() {
        return this.player1Won.get() || this.player2Won.get();
    }

    public static Game getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new Game();
        }
        return uniqueInstance;
    }

    public Position getPos() { return  pos ;}

    public SimpleObjectProperty<Pion> getPlateau(int x, int y) {
        return plateau[y][x];
    }

    public void changePlayer() {
        Player p = currentPlayer;
        currentPlayer = prochainJoueur;
        prochainJoueur = p;
    }
    public void newGame() {
        for (int y=0; y<5; ++y)
            for (int x=0; x<5; ++x)
                plateau[y][x].set(Pion.BLANC);
        pos.setX(-1); pos.setY(-1);
        if (currentPlayer.getPion() != Pion.CROIX) {
            changePlayer();
        }
    } // public newGame

    //fontion pour verifier la prise s'un pion sur le plateau
    public boolean isChooseAllowed(int x, int y) {
        return (pos.x == -1) // no piece chosen yet
                && (x == 0 || x == 4 || y == 0 || y == 4) // on the side of the board
                && (plateau[y][x].get() != prochainJoueur.getPion()); // neutral or current player's piece

    }

    public void choosePion(int x, int y) {
        if (isChooseAllowed(x,y)) {
            pos.setX(x);
            pos.setY(y);
        } else
            throw new IllegalStateException("Vous ne pouvez pas choisir cette piece");
    }
    public void cancelChoice() {
        pos.setX(-1);
        pos.setY(-1);
    }

    public void move(int x1, int y1, int x2, int y2) {
        choosePion(x1, y1);
        insertAt(x2, y2);
    }
    public void insertAt(int x, int y) {

        InsertionContext insertionContextTop = new InsertionContext(new InsertTop());
        InsertionContext insertionContextBottom = new InsertionContext(new InsertBottom());
        InsertionContext insertionContextLeft = new InsertionContext(new InsertLeft());
        InsertionContext insertionContextRight = new InsertionContext(new InsertRight());

        if (pos.getX() == -1) throw new IllegalStateException("You have first to choose a piece to move!");
        if (pos.getX() == x)
            if (pos.getY() == y) throw new IllegalArgumentException("You cannot put the piece where it was!");
            else if (y == 0) insertionContextTop.getInsertion(plateau, pos, currentPlayer);
            else if (y == 4) insertionContextBottom.getInsertion(plateau, pos, currentPlayer);
            else throw new IllegalArgumentException("You have to place your piece at one end of the column!");
        else if (pos.getY() == y)
            if (x == 0)  insertionContextLeft.getInsertion(plateau, pos, currentPlayer);
            else if (x == 4) insertionContextRight.getInsertion(plateau, pos, currentPlayer);
            else throw new IllegalArgumentException("You have to place your piece at one end of the row!");
        else throw new IllegalArgumentException("You have to play on the same row/column as the piece you chose!");
        cancelChoice();
        changePlayer();
    }

    public void moving(int x2, int y2,Position pos) {

        InsertionContext insertionContextTop = new InsertionContext(new InsertTop());
        InsertionContext insertionContextBottom = new InsertionContext(new InsertBottom());
        InsertionContext insertionContextLeft = new InsertionContext(new InsertLeft());
        InsertionContext insertionContextRight = new InsertionContext(new InsertRight());

        if (pos.getX() == -1) throw new IllegalStateException("You have first to choose a piece to move!");
        if (pos.getX() == x2)
            if (pos.getY() == y2) throw new IllegalArgumentException("You cannot put the piece where it was!");
            else if (y2 == 0) insertionContextTop.getInsertion(plateau, pos, currentPlayer);
            else if (y2 == 4) insertionContextBottom.getInsertion(plateau, pos, currentPlayer);
            else throw new IllegalArgumentException("You have to place your piece at one end of the column!");
        else if (pos.getY() == y2)
            if (x2 == 0)  insertionContextLeft.getInsertion(plateau, pos, currentPlayer);
            else if (x2 == 4) insertionContextRight.getInsertion(plateau, pos, currentPlayer);
            else throw new IllegalArgumentException("You have to place your piece at one end of the row!");
        else throw new IllegalArgumentException("You have to play on the same row/column as the piece you chose!");
        cancelChoice();
        changePlayer();

    }

    // fonction pour determiner la qualite d'un etat de jeu QUIXO,
    public int evaluate(int depth, boolean isMaximizing) {
        int value = 0;
        if (player1Won.get()) {
            return 100;
        } else if (player2Won.get()) {
            return -100;
        }else {
            for (int i = 0; i < 5; i++) {
                int croix = 0;
                int rond = 0;
                for (int j = 0; j < 5; j++) {
                    if (plateau[i][j].get() == Pion.CROIX) {
                        croix++;
                    } else if (plateau[i][j].get() == Pion.ROND) {
                        rond++;
                    }
                }
                value += evaluateLine(croix, rond);
            }
            // evaluer les lignes verticales
            for (int j = 0; j < 5; j++) {
                int croix = 0;
                int rond = 0;
                for (int i = 0; i < 5; i++) {
                    if (plateau[i][j].get() == Pion.CROIX) {
                        croix++;
                    } else if (plateau[i][j].get() == Pion.ROND) {
                        rond++;
                    }
                }
                value += evaluateLine(croix, rond);
            }
            // evaluer les diagonales
            int croix = 0;
            int rond = 0;
            for (int i = 0; i < 5; i++) {
                if (plateau[i][i].get() == Pion.CROIX) {
                    croix++;
                } else if (plateau[i][i].get() == Pion.ROND) {
                    rond++;
                }
            }
            value += evaluateLine(croix, rond);
            croix = 0;
            rond = 0;
            for (int i = 0; i < 5; i++) {
                if (plateau[i][4 - i].get() == Pion.CROIX) {
                    croix++;
                } else if (plateau[i][4 - i].get() == Pion.ROND) {
                    rond++;
                }
            }
            value += evaluateLine(croix, rond);
            // ajouter un facteur de profondeur pour favoriser les mouvements qui finissent rapidement le jeu
            value += depth * 10;
            if (!isMaximizing) {
                value = -value;
            }
            System.out.println(value);
        }
        // evaluer les lignes horizontales
        return value;
    }


    private int evaluateLine(int croix, int rond) {
        int value = 0;
        if (croix == 5) {
            value = 100;
        } else if (rond == 5) {
            value = -100;
        } else if (croix == 4 && rond == 0) {
            value = 10;
        } else if (rond == 4 && croix == 0) {
            value = -10;
        }
        return value;
    }

}
