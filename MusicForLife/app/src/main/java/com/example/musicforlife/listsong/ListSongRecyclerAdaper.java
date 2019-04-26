package com.example.musicforlife.listsong;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.musicforlife.ImageHelper;
import com.example.musicforlife.MainActivity;
import com.example.musicforlife.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class ListSongRecyclerAdaper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "ListSongRecyclerAdaper";
    private static ArrayList<SongModel> mListSong;
    private static Context mContext;

    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_LOADING = 1;

    public ListSongRecyclerAdaper(Context context, ArrayList<SongModel> listSong) {
        mContext = context;
        mListSong = listSong;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item_song, viewGroup, false);
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
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
//        Log.d(TAG, "onViewRecycled: ");

    }

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
        ImageView imageView;

        ViewHolderRecycler(@NonNull View itemView) {
            super(itemView);
            this.titleSong = itemView.findViewById(R.id.txtTitle);
//            this.album=album;
            this.artist = itemView.findViewById(R.id.txtArtist);
            this.imageView = itemView.findViewById(R.id.imgSong);
            this.duration = itemView.findViewById(R.id.txtDuration);

        }

        @SuppressLint("SetTextI18n")
        void bindContent(SongModel songModel) {
            Log.d(TAG, "bindContent: BIND CONTENT");
            this.titleSong.setText(songModel.getTitle());
            this.artist.setText(songModel.getArtist() + "_" + songModel.getAlbumId());
            this.duration.setText(SongModel.formateMilliSeccond(songModel.getDuration()));
//GLIDE
//            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
//            mediaMetadataRetriever.setDataSource(songModel.getPath());
//            RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL);
//            Glide
//                    .with(mContext)
//                    .load(mediaMetadataRetriever.getEmbeddedPicture())
////                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
////                    .dontTransform()
//                    .override(68, 68)
//                    .thumbnail(1.0f)
//                    .into(new SimpleTarget<Drawable>() {
//                        @Override
//                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                            imageView.setImageDrawable(resource);
//                            imageView.setDrawingCacheEnabled(true);
//
//                        }
//                    });
//\GLIDE
//PICASSO
//            Uri sArtworkUri = Uri.parse("content://media//external/audio/albumart");
//            Uri imageUri = Uri.withAppendedPath(sArtworkUri, String.valueOf(songModel.getAlbumId()));
//            Bitmap bitmap = null;
//            try {
//                bitmap = BitmapFactory.decodeStream(MainActivity.getMainActivity().getContentResolver().openInputStream(imageUri));
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            Log.d(TAG, "bindContent: BITMAP URL"+ (bitmap != null ? bitmap.getByteCount() : 0));
//            imageView.setImageBitmap(bitmap);
//            imageView.setImageURI(imageUri);
//            Log.d(TAG, "bindContent: URI IMAGE" + imageUri.toString());
//            Picasso.get().load(imageUri).into(imageView);

//\PICASSO
//BITMAP WITH ASYNC TASK
            if (cancelPotentialWork(songModel.getPath(), imageView)) {
                final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
                final AsyncDrawable asyncDrawable = new AsyncDrawable(null, task);
                imageView.setImageDrawable(asyncDrawable);
                task.execute(songModel.getPath());
            }

//BITMAP WITH ASYNC TASK
//BITMAP CACHE
//            Bitmap bitmap = CacheHelper.Instance().getBitmapFromMemCache(songModel.getPath());
//            if (bitmap != null){
//                imageView.setImageBitmap(bitmap);
//            }
//            else{
//                CacheHelper.Instance().addBitmapToMemoryCache(songModel.getPath());
//                imageView.setImageResource(R.mipmap.music_file_128);
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

        LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBarCircle);
        }
    }


    private static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskWeakReference;

        AsyncDrawable(Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask) {
//            super(resources, bitmap);
            bitmapWorkerTaskWeakReference = new WeakReference<>(bitmapWorkerTask);

        }

        BitmapWorkerTask getBitmapWorkerTask() {
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

            if (bitmap != null && imageViewWeakReference != null) {
                final ImageView imageView = imageViewWeakReference.get();
                final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
                if (this == bitmapWorkerTask && imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }
}
