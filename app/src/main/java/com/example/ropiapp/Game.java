package com.example.ropiapp;

public class Game {
    private String homeTeam;
    private String awayTeam;
    private String date;   // formátum: "2025-06-01 18:00"
    private String location;

    public Game(String homeTeam, String awayTeam, String date, String location) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.date = date;
        this.location = location;
    }

    public String getHomeTeam() { return homeTeam; }
    public String getAwayTeam() { return awayTeam; }
    public String getDate() { return date; }
    public String getLocation() { return location; }
}

