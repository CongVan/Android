package com.example.musicforlife.play;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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

    public ListPlayingAdapter(Context context, ArrayList<SongModel> listSong) {
        this.mContext = context;
        this.mListSong = listSong;
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

    private static class ViewHolderRecycler extends RecyclerView.ViewHolder {
        TextView titleSong;
        TextView album;
        TextView artist;
        TextView duration;
//        ImageView imageView;
        ImageView imgStatusPlaying;

        public ViewHolderRecycler(@NonNull View itemView) {
            super(itemView);
            this.titleSong = (TextView) itemView.findViewById(R.id.txtTitle);
//            this.album=album;
            this.artist = (TextView) itemView.findViewById(R.id.txtArtist);
//            this.imageView = (ImageView) itemView.findViewById(R.id.imgSong);
            this.duration = (TextView) itemView.findViewById(R.id.txtDuration);
            this.imgStatusPlaying = (ImageView) itemView.findViewById(R.id.imgStatusPlaying);
        }

        public void bindContent(SongModel songModel) {
            this.titleSong.setText(songModel.getTitle());
            this.artist.setText(songModel.getArtist());
            this.duration.setText(SongModel.formateMilliSeccond(songModel.getDuration()));
            Log.d(TAG, "bindContent: " + imgStatusPlaying);
            if (songModel != null && PlayService.getCurrentSongPlaying() != null) {
                if (songModel.getSongId() == PlayService.getCurrentSongPlaying().getSongId()) {
                    this.imgStatusPlaying.setVisibility(View.VISIBLE);
                    this.titleSong.setTextColor(mContext.getResources().getColor(R.color.colorTitleWhitePrimary));
                } else {
                    this.titleSong.setTextColor(mContext.getResources().getColor(R.color.colorTitleWhitePrimary));
                    this.imgStatusPlaying.setVisibility(View.GONE);
                }
            }

//            if (imageView.getResources()==null){
//            if (cancelPotentialWork(songModel.getPath(), imageView)) {
//                final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
//                final AsyncDrawable asyncDrawable = new AsyncDrawable(null, task);
//                imageView.setImageDrawable(asyncDrawable);
//                task.execute(songModel.getPath());
//            }
//            }

//            new BitmapWorkerTask(imageView).execute(songModel.getPath());

//            if (this.imageView.getDrawable() == null) {
//            ParamImageThread paramImageThread = new ParamImageThread(this.imageView, songModel.getPath());
//            new loadImageFromStorage().execute(paramImageThread);
//            }


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
