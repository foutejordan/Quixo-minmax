package com.quixo.projet_quixo.model;

import javafx.beans.property.SimpleObjectProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class QuixoMinMaxThread {

    // profondeur de l'arbre de recherche
    public static final int MAX_DEPTH = 4;

    public static final int MIN_VALUE = (int) - Double.POSITIVE_INFINITY;
    public static final int MAX_VALUE = (int) Double.POSITIVE_INFINITY;


    public QuixoMinMaxThread() {}

    public  Move getBestMove(Game game, Player player, Player player2) {
        // variable qui va contenir le meilleur coups possible
        Move bestMove = null;


        // le meilleur score est initialisee avec MIN_VALUE si c'est le joueur ROND ( IA ) qu'on veur maximiser et MIN_VALUE dans le cas contraire
        int bestScore = (player.pion == Pion.ROND) ? MIN_VALUE : MAX_VALUE;
        //pour chaque coups possible, on teste le coup et on regarde tout ce qui se passe a partir de ce coups la, minmax()
        for(Move move : getPossiblesMoves(game.getPlateau(), player)) {
            //simulation de la partie, on fait une copie du jeu pour na pas affecter l'etat actuel du plateau

            Game lastGameState = new Game(game);
            //on simule le deplacement a partir de la fonction moving qui se trouve dans le Game
            // on passe en parametre les position de depart et d'arriver du deplacement
            lastGameState.moving(move.x2, move.y2, new Position(move.x1, move.y1));
            //calcul du score
            //la premiere simulation etait pour l'IA(joeur ROND), on passe isMaximizing a false pour chercher a minimiser car c'est au tour du joueur
            int score = minimax(lastGameState, MAX_DEPTH , player, player2, false);

            //on regarde si notre score actuel est superieur a l'ancien, si c'est le cas on retourne le score avec le mouvement associee
            if ((player.pion == Pion.ROND && score > bestScore)|| (player.pion == Pion.CROIX && score < bestScore)) {
                bestMove = move;
                bestScore = score;
            }
        }
        return bestMove;
    }
    // execute recursivement tous les coups possibles tour par tour avec une profondeur definie,
    // jusqu'a ce que on arrive a la profondeur maximale ou bien un joueur gagne la partie lors de la simulation
    private  int minimax(Game game, int depth, Player player, Player player2, boolean isMaximizing) {
        System.out.println("jeu.gameIsOver()dep");
        if (depth == 0 || game.gameIsOver()) {
            return game.evaluate(depth, isMaximizing);
        }
        // si on est entrain de vouloir maximiser, on va chercher un score superieur a MIN_VALUE, dans le cas contraire on cher he une score inferieur a MAX_VALUE
        AtomicInteger score = new AtomicInteger();
        AtomicInteger bestScore = new AtomicInteger((isMaximizing) ? MIN_VALUE : MAX_VALUE);
        Player currP = (isMaximizing) ? player : player2;
        System.out.println("movzbles");

        List<Move> moves = getPossiblesMoves(game.getPlateau(), currP);
        CountDownLatch latch = new CountDownLatch(moves.size());

        ThreadPoolExecutor executor = new ThreadPoolExecutor(moves.size(), moves.size(), 0, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>());
        for (Move move: moves) {
            executor.execute(() -> {
                // copie du jeu
                Game lastGameState = new Game(game);
// simulation du jeu, mais cette fois si c'est le tour du joueu opposee
                lastGameState.moving(move.x2, move.y2, new Position(move.x1, move.y1));
                score.set(minimax(lastGameState, depth - 1, player, player2, !isMaximizing));
// mettre à jour le meilleur score
                if (isMaximizing) {
                    bestScore.set(Math.max(bestScore.get(), score.get()));
                } else {
                    bestScore.set(Math.min(bestScore.get(), score.get()));
                }

                latch.countDown();

            });
        }

        try {
            // on attent que toutes les taches s'achevent
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // arret des threads
        executor.shutdown();
        return bestScore.get();
    }
    private  List<Position> getPossiblesChoose(SimpleObjectProperty<Pion>[][] plateau, Player player) {
        List<Position> positions = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Pion pion = plateau[i][j].get();
                if (isPossible(i, j, plateau, player) && (pion == Pion.BLANC || pion == player.getPion())){
                    positions.add(new Position(i, j));
                }
            }
        }
        return positions;
    }
    private static List<Position> getPossiblesDestination(int x, int y) {
        List<Position> destination = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (x == i){
                    if(y != j){
                        if (0 == j) {
                            destination.add(new Position(i, 0));
                        } else if (j == 4) {
                            destination.add(new Position(i, 4));
                        }
                    }
                } else if (y == j) {
                    if (i == 0) {
                        destination.add(new Position(0, j));
                    } else if (i == 4) {
                        destination.add(new Position(4, j));
                    }
                }
            }
        }
        return destination;
    }


    //pour chque choix possible, on enregistre toute les destinations possible, pour avoir ainsi tous les mouvement de deplacement possible
    public  List<Move> getPossiblesMoves(SimpleObjectProperty<Pion>[][] plateau, Player player) {
        List<Move> possiblesMove = new ArrayList<>();
        for(Position choose : getPossiblesChoose(plateau, player) ){
            List<Position> destinations = getPossiblesDestination(choose.getX(),choose.getY());
            for (Position destination: destinations){
                possiblesMove.add(new Move(choose.getX(), choose.getY(), destination.x, destination.y));
            }
        }
        return possiblesMove;
    }
    private  boolean isPossible(int x1, int y1, SimpleObjectProperty<Pion>[][] plateau,Player player) {
        return  ((x1 == 0 || x1 == 4 || y1 == 0 || y1 == 4));
    }
}

