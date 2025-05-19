package com.example.ropiapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GamesAdapter extends RecyclerView.Adapter<GamesAdapter.GameViewHolder> {

    private List<Game> gamesList;

    public GamesAdapter(List<Game> gamesList) {
        this.gamesList = gamesList;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_item, parent, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        Game game = gamesList.get(position);
        holder.match.setText(game.getHomeTeam() + " vs " + game.getAwayTeam());
        holder.date.setText(game.getDate());
        holder.location.setText(game.getLocation());
    }

    @Override
    public int getItemCount() {
        return gamesList.size();
    }

    static class GameViewHolder extends RecyclerView.ViewHolder {
        TextView match, date, location;

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            match = itemView.findViewById(R.id.match);
            date = itemView.findViewById(R.id.date);
            location = itemView.findViewById(R.id.location);
        }
    }
}

