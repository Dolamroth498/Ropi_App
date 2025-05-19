package com.example.ropiapp;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.TeamViewHolder> {
    private List<Team> teamList;

    public TeamAdapter(List<Team> teamList) {
        this.teamList = teamList;
    }

    public static class TeamViewHolder extends RecyclerView.ViewHolder {
        TextView nameTV, playedTV, wonTV, lostTV, pointsTV;

        public TeamViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.teamName);
            playedTV = itemView.findViewById(R.id.gamesPlayed);
            wonTV = itemView.findViewById(R.id.gamesWon);
            lostTV = itemView.findViewById(R.id.gamesLost);
            pointsTV = itemView.findViewById(R.id.points);
        }
    }

    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_row_item, parent, false);
        return new TeamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamViewHolder holder, int position) {
        Team team = teamList.get(position);
        holder.nameTV.setText(team.getName());
        holder.playedTV.setText(String.valueOf(team.getPlayed()));
        holder.wonTV.setText(String.valueOf(team.getWon()));
        holder.lostTV.setText(String.valueOf(team.getLost()));
        holder.pointsTV.setText(String.valueOf(team.getPoints()));
    }

    @Override
    public int getItemCount() {
        return teamList.size();
    }
}
