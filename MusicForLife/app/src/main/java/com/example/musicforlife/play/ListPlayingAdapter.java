package com.example.musicforlife.play;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.musicforlife.listsong.MultiClickAdapterListener;
import com.example.musicforlife.utilitys.ImageHelper;
import com.example.musicforlife.R;
import com.example.musicforlife.listsong.SongModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class ListPlayingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static ArrayList<SongModel> mListSong;
    private static Context mContext;

    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_LOADING = 1;
    private static final String TAG = "ListPlayingAdapter";

    private MultiClickAdapterListener mMultiClickListener;

    public ListPlayingAdapter(Context context, ArrayList<SongModel> listSong, MultiClickAdapterListener mMultiClickListener) {
        this.mContext = context;
        this.mListSong = listSong;
        this.mMultiClickListener = mMultiClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item_song_playing, viewGroup, false);
//            ViewHolderRecycler viewHolder = new ViewHolderRecycler(view);
            return new ViewHolderRecycler(view);
        } else {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.progressbar_circle, viewGroup, false);
            return new LoadingViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof ViewHolderRecycler) {
            showSongItem((ViewHolderRecycler) viewHolder, i);
        } else if (viewHolder instanceof LoadingViewHolder) {
            showLoading((LoadingViewHolder) viewHolder, i);
        }

    }

    private void showSongItem(ViewHolderRecycler viewHolder, int position) {
        SongModel songModel = mListSong.get(position);
        viewHolder.bindContent(songModel);
    }

    private void showLoading(LoadingViewHolder viewHolder, int position) {

    }
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolderRecycler viewHolderRecycler, int i) {
//        SongModel songModel = mListSong.get(i);
//        viewHolderRecycler.bindContent(songModel);
//    }


    @Override
    public int getItemViewType(int position) {
        return mListSong.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return mListSong == null ? 0 : mListSong.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolderRecycler extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView titleSong;
        TextView album;
        TextView artist;
        TextView duration;
        //        ImageView imageView;
        ImageView imgStatusPlaying;
        AppCompatCheckBox chkSong;
        CardView layoutItemSong;

        public ViewHolderRecycler(@NonNull View itemView) {
            super(itemView);
            this.titleSong = (TextView) itemView.findViewById(R.id.txtTitle);
//            this.album=album;
            this.artist = (TextView) itemView.findViewById(R.id.txtArtist);
//            this.imageView = (ImageView) itemView.findViewById(R.id.imgSong);
            this.duration = (TextView) itemView.findViewById(R.id.txtDuration);
            this.imgStatusPlaying = (ImageView) itemView.findViewById(R.id.imgStatusPlaying);
            chkSong = itemView.findViewById(R.id.checkBoxSong);
            layoutItemSong = itemView.findViewById(R.id.layoutItemSongPlaying);
            chkSong.setOnClickListener(this);
            layoutItemSong.setOnClickListener(this);
        }

        public void bindContent(SongModel songModel) {
            this.titleSong.setText(songModel.getTitle());
            this.artist.setText(songModel.getArtist());
            this.duration.setText(SongModel.formateMilliSeccond(songModel.getDuration()));
//            Log.d(TAG, "bindContent: " + imgStatusPlaying);
            if (songModel != null && PlayService.getCurrentSongPlaying() != null) {
                if (songModel.getSongId() == PlayService.getCurrentSongPlaying().getSongId()) {
                    this.imgStatusPlaying.setVisibility(View.VISIBLE);
                    this.duration.setVisibility(View.GONE);
                    this.titleSong.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
                } else {
                    this.titleSong.setTextColor(mContext.getResources().getColor(R.color.colorTitleWhitePrimary));
                    this.imgStatusPlaying.setVisibility(View.GONE);
                    this.duration.setVisibility(View.VISIBLE);
                }
            }
            if (songModel.isChecked()) {
                this.chkSong.setChecked(true);
            } else {
                this.chkSong.setChecked(false);
            }

//
        }


        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.checkBoxSong) {
                mMultiClickListener.checkboxClick(v, getAdapterPosition());
            } else {
                mMultiClickListener.layoutItemClick(v, getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBarCircle);
        }
    }


}
