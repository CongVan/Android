package com.example.musicforlife.playlist;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.musicforlife.ImageHelper;
import com.example.musicforlife.R;
import com.example.musicforlife.listsong.SongModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class PlaylistAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static ArrayList<PlaylistModel> mPlaylist;
    private static Context mContext;


    public PlaylistAdapter(Context context, ArrayList<PlaylistModel> playlist) {
        this.mContext = context;
        this.mPlaylist = playlist;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item_playlist, viewGroup, false);
//            ViewHolderRecycler viewHolder = new ViewHolderRecycler(view);
        return new ViewHolderRecycler(view);


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        showSongItem((ViewHolderRecycler) viewHolder, i);

    }

    private void showSongItem(ViewHolderRecycler viewHolder, int position) {
        PlaylistModel playlistModel = mPlaylist.get(position);
        viewHolder.bindContent(playlistModel);
    }

    private void showLoading(LoadingViewHolder viewHolder, int position) {

    }
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolderRecycler viewHolderRecycler, int i) {
//        SongModel songModel = mPlaylist.get(i);
//        viewHolderRecycler.bindContent(songModel);
//    }


    @Override
    public int getItemCount() {
        return mPlaylist == null ? 0 : mPlaylist.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private static class ViewHolderRecycler extends RecyclerView.ViewHolder {
        TextView titlePlaylist;
        TextView numberOfSong;
        ImageView imagePlaylist;

        public ViewHolderRecycler(@NonNull View itemView) {
            super(itemView);
            this.titlePlaylist = (TextView) itemView.findViewById(R.id.txtPlaylistTitle);
//            this.album=album;
            this.numberOfSong = (TextView) itemView.findViewById(R.id.txtNumberSongPlaylist);
            this.imagePlaylist = (ImageView) itemView.findViewById(R.id.imgPlaylist);
        }

        public void bindContent(PlaylistModel playlistModel) {
            this.titlePlaylist.setText(playlistModel.getTitle());
            this.numberOfSong.setText(playlistModel.getNumberOfSongs());
            this.imagePlaylist.setImageBitmap(ImageHelper.getBitmapFromPath(playlistModel.getPathImage(), R.mipmap.album_128));
        }


    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBarCircle);
        }
    }


    private static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskWeakReference;

        public AsyncDrawable(Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask) {
//            super(resources, bitmap);
            bitmapWorkerTaskWeakReference = new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);

        }

        public BitmapWorkerTask getBitmapWorkerTask() {
            return bitmapWorkerTaskWeakReference.get();
        }

    }

    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

    private static boolean cancelPotentialWork(String pathImage, ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
        if (bitmapWorkerTask != null) {
            final String path = bitmapWorkerTask.pathImage;
            if (path == null || pathImage == null) {
                return false;
            }
            if (!path.equals(pathImage)) {
                bitmapWorkerTask.cancel(true);
            } else {
                return false;
            }
        }
        return true;

    }

    private static class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewWeakReference;
        String pathImage;
        Bitmap mBitmap;

        private BitmapWorkerTask(ImageView imageView) {
            this.imageViewWeakReference = new WeakReference<ImageView>(imageView);
        }


        @Override
        protected Bitmap doInBackground(String... strings) {
            pathImage = strings[0];
//            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
//            mediaMetadataRetriever.setDataSource(pathImage);
//            InputStream inputStream;
//            Bitmap bitmap;
//
//            if (mediaMetadataRetriever.getEmbeddedPicture() != null) {
//                inputStream = new ByteArrayInputStream(mediaMetadataRetriever.getEmbeddedPicture());
//                mediaMetadataRetriever.release();
//                bitmap = BitmapFactory.decodeStream(inputStream);
//            } else {
//
//                bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.music_file_128);
//            }

            Bitmap bitmap = ImageHelper.getBitmapFromPath(pathImage, R.mipmap.music_file_128);
            mBitmap = bitmap;
            return bitmap;

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewWeakReference != null && bitmap != null) {
                final ImageView imageView = imageViewWeakReference.get();
                final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
                if (this == bitmapWorkerTask && imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }
}

