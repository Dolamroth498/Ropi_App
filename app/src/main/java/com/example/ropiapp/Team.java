package com.example.ropiapp;

public class Teams {
    private String teamName;
    private int points;
    private int rank;

    public Teams(String teamName, int points, int rank) {
        this.teamName = teamName;
        this.points = points;
        this.rank = rank;
    }

    public String getTeamName() { return teamName; }
    public int getPoints() { return points; }
    public int getRank() { return rank; }
}

