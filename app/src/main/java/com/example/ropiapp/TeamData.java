package com.example.ropiapp;

import java.util.ArrayList;
import java.util.List;

public class TeamData {
    public static List<Team> getTeams() {
        List<Team> teams = new ArrayList<>();

        teams.add(new Team("MÁV Előre Foxconn", 22, 20, 2, 60));
        teams.add(new Team("Fino Kaposvár", 22, 18, 4, 54));
        teams.add(new Team("GreenPlan-VRCK", 22, 16, 6, 48));
        teams.add(new Team("MAFC", 22, 15, 7, 45));
        teams.add(new Team("DEAC", 22, 13, 9, 39));
        teams.add(new Team("Kecskeméti RC", 22, 12, 10, 36));
        teams.add(new Team("Pénzügyőr SE", 22, 10, 12, 30));
        teams.add(new Team("TFSE", 22, 8, 14, 24));
        teams.add(new Team("Dág KSE", 22, 6, 16, 18));
        teams.add(new Team("MEAFC-Peka Bau", 22, 5, 17, 15));
        teams.add(new Team("Vidux-Szegedi RSE", 22, 4, 18, 12));
        teams.add(new Team("DKSE", 22, 2, 20, 6));

        return teams;
    }
}

