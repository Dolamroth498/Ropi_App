package com.example.ropiapp;

import java.util.ArrayList;
import java.util.List;

public class GameData {

    public static List<Game> getAllGames() {
        List<Game> games = new ArrayList<>();

        games.add(new Game("GreenPlan-VRCK", "MAFC-BME", "2025-04-11 18:00", "Kazincbarcika"));
        games.add(new Game("MAFC-BME", "GreenPlan-VRCK", "2025-04-15 18:00", "Budapest"));
        games.add(new Game("GreenPlan-VRCK", "MAFC-BME", "2025-04-19 18:00", "Kazincbarcika"));
        games.add(new Game("Fino Kaposvár", "MÁV Előre Foxconn", "2025-04-21 18:00", "Kaposvár"));

        games.add(new Game("MÁV Előre Foxconn", "Fino Kaposvár", "2025-05-01 17:30", "Székesfehérvár"));
        games.add(new Game("Fino Kaposvár", "MÁV Előre Foxconn", "2025-05-05 18:00", "Kaposvár"));
        games.add(new Game("MÁV Előre Foxconn", "Fino Kaposvár", "2025-05-10 19:45", "Székesfehérvár"));


        return games;
    }
}

