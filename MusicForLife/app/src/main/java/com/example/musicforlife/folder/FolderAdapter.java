package com.example.musicforlife.folder;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.musicforlife.R;
import com.example.musicforlife.listsong.SongModel;
import com.example.musicforlife.playlist.PlaylistModel;
import com.example.musicforlife.utilitys.ImageCacheHelper;
import com.example.musicforlife.utilitys.ImageHelper;

import java.util.ArrayList;

public class FolderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static ArrayList<FolderModel> mFolders;
    private static Context mContext;
    private static final String TAG = "PlaylistAdapter";


    public FolderAdapter(Context context, ArrayList<FolderModel> folders) {
        this.mContext = context;
        this.mFolders = folders;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

//        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item_playlist, viewGroup, false);
//        ViewHolderRecycler viewHolder = new ViewHolderRecycler(view);
//        Log.d(TAG, "onCreateViewHolder: PLAYLIST ADAPTER " + viewGroup.getId());
//        return viewHolder;
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item_folder, viewGroup, false);
        return new ViewHolderRecycler(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        showFolderItem((ViewHolderRecycler) viewHolder, i);
    }

    private void showFolderItem(ViewHolderRecycler viewHolder, int position) {
        FolderModel folderModel = mFolders.get(position);
//        Log.d(TAG, "showSongItem: " + playlistModel + " View " + viewHolder);
        if (folderModel != null) {
            viewHolder.bindContent(folderModel);
        }
    }

    @Override
    public int getItemCount() {
        return mFolders == null ? 0 : mFolders.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolderRecycler extends RecyclerView.ViewHolder {
        TextView name;
        TextView numberOfSong;


        public ViewHolderRecycler(@NonNull View itemView) {
            super(itemView);
            this.name = (TextView) itemView.findViewById(R.id.txtFolderName);
            this.numberOfSong = (TextView) itemView.findViewById(R.id.txtNumberOfSongFolder);
        }

        @SuppressLint("SetTextI18n")
        public void bindContent(FolderModel folderModel) {
            this.name.setText(folderModel.getName());
            this.numberOfSong.setText(String.valueOf(folderModel.getNumberOfSong()) + " bài hát");

        }
    }

}

