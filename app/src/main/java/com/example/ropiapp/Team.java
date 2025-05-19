package com.example.ropiapp;

public class Team {
    private String name;
    private int played;
    private int won;
    private int lost;
    private int points;

    public Team(String name, int played, int won, int lost, int points) {
        this.name = name;
        this.played = played;
        this.won = won;
        this.lost = lost;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public int getPlayed() {
        return played;
    }

    public int getWon() {
        return won;
    }

    public int getLost() {
        return lost;
    }

    public int getPoints() {
        return points;
    }
}
