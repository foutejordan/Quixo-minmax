package com.quixo.projet_quixo.model;

import javafx.scene.paint.Color;

public class Player {
    String nomJoueur;
    Pion pion;

    public void setNomJoueur(String nomJoueur) {
        this.nomJoueur = nomJoueur;
    }

    public void setCouleur( Pion pion) {
        this.pion = pion;
    }

    public String getNomJoueur() {
        return nomJoueur;
    }

    public Pion getPion() {
        return pion;
    }

    public Player(String nomJoueur, Pion pion) {
        this.nomJoueur = nomJoueur;
        this.pion = pion;
    }

}
