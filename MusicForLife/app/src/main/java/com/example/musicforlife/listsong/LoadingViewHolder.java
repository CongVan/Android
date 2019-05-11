package com.example.musicforlife.listsong;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.example.musicforlife.R;

public class LoadingViewHolder extends RecyclerView.ViewHolder {
    ProgressBar progressBar;

    public LoadingViewHolder(@NonNull View itemView) {
        super(itemView);
        progressBar = itemView.findViewById(R.id.progressBarCircle);
    }
}
