package com.example.ropiapp;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TeamsAdapter extends RecyclerView.Adapter<TeamsAdapter.ViewHolder> {

    private List<Teams> teams;

    public TeamsAdapter(List<Teams> standings) {
        this.teams = standings;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_row_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Teams team = teams.get(position);
        holder.teamName.setText(team.getTeamName());
        holder.points.setText("Pont: " + team.getPoints());
        holder.rank.setText(String.valueOf(team.getRank()));
    }

    @Override
    public int getItemCount() {
        return teams.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView teamName, points, rank;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            teamName = itemView.findViewById(R.id.teamName);
            points = itemView.findViewById(R.id.points);
            rank = itemView.findViewById(R.id.rank);
        }
    }
}

