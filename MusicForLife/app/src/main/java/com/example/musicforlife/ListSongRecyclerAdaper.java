package com.example.musicforlife;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ListSongRecyclerAdaper extends RecyclerView.Adapter<ListSongRecyclerAdaper.ViewHolderRecycler> {

    private static ArrayList<SongModel> mListSong;
    private static Context mContext;

    public ListSongRecyclerAdaper(Context context, ArrayList<SongModel> listSong) {
        this.mContext = context;
        this.mListSong = listSong;
    }

    @NonNull
    @Override
    public ViewHolderRecycler onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item_song, viewGroup, false);
        ViewHolderRecycler viewHolder = new ViewHolderRecycler(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderRecycler viewHolderRecycler, int i) {
        SongModel songModel = mListSong.get(i);
        viewHolderRecycler.bindContent(songModel);
    }


    @Override
    public int getItemCount() {
        return mListSong.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolderRecycler extends RecyclerView.ViewHolder {
        TextView titleSong;
        TextView album;
        TextView artist;
        TextView duration;
        ImageView imageView;

        public ViewHolderRecycler(@NonNull View itemView) {
            super(itemView);
            this.titleSong = (TextView) itemView.findViewById(R.id.txtTitle);
//            this.album=album;
            this.artist = (TextView) itemView.findViewById(R.id.txtArtist);
            this.imageView = (ImageView) itemView.findViewById(R.id.imgSong);
            this.duration = (TextView) itemView.findViewById(R.id.txtDuration);

        }

        public void bindContent(SongModel songModel) {
            this.titleSong.setText(songModel.getTitle());
            this.artist.setText(songModel.getArtist());
            this.duration.setText(songModel.getDuration());

//            if (this.imageView.getDrawable() == null) {
            ParamImageThread paramImageThread = new ParamImageThread(this.imageView, songModel.getPath());
            new loadImageFromStorage().execute(paramImageThread);
//            }


        }


    }

    private static class loadImageFromStorage extends AsyncTask<ParamImageThread, Integer, Bitmap> {
        ParamImageThread paramImageThread;

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            paramImageThread.imageView.setImageBitmap(bitmap);

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        public Bitmap doInBackground(ParamImageThread... paramImageThreads) {
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            this.paramImageThread = paramImageThreads[0];
            mediaMetadataRetriever.setDataSource(paramImageThread.getPath());
            InputStream inputStream;
            Bitmap bitmap;

            if (mediaMetadataRetriever.getEmbeddedPicture() != null) {
                inputStream = new ByteArrayInputStream(mediaMetadataRetriever.getEmbeddedPicture());
                mediaMetadataRetriever.release();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } else {
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.musical_note_light_64);
            }
            return bitmap;
        }


    }

    private static class ParamImageThread {
        private ImageView imageView;
        private String path;

        public ParamImageThread(ImageView imageView, String path) {
            this.imageView = imageView;
            this.path = path;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public void setImageView(ImageView imageView) {
            this.imageView = imageView;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }
}
