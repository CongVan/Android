package com.example.musicforlife.recent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.musicforlife.R;
import com.example.musicforlife.listsong.HolderSongRecyclerView;
import com.example.musicforlife.listsong.LoadingViewHolder;
import com.example.musicforlife.listsong.MultiClickAdapterListener;
import com.example.musicforlife.listsong.SongModel;
import com.example.musicforlife.utilitys.ImageCacheHelper;

import java.util.ArrayList;

public class ListSongRecentAdaper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "ListSongRecyclerAdaper";
    private static ArrayList<SongModel> mListSong;
    private static Context mContext;

    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_LOADING = 1;

    private ImageCacheHelper mImageCacheHelper;
    public MultiClickAdapterListener mListener;



    public ListSongRecentAdaper(Context context, ArrayList<SongModel> listSong, MultiClickAdapterListener listener) {
        mContext = context;
        mListSong = listSong;
        mImageCacheHelper = new ImageCacheHelper(R.mipmap.music_128);
        mListener = listener;
//        int maxSize = (int) (Runtime.getRuntime().maxMemory() / 1024);
//        // Divide the maximum size by eight to get a adequate size the LRU cache should reach before it starts to evict bitmaps.
//        int cacheSize = maxSize / 8;
//        mBitmapCache = new LruCache<Long, Bitmap>(cacheSize) {
//            @Override
//            protected int sizeOf(Long key, Bitmap value) {
//                // returns the size of bitmaps in kilobytes.
//                return value.getByteCount() / 1024;
//            }
//        };
//        mPlaceholder = (BitmapDrawable) mContext.getResources().getDrawable();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item_song, viewGroup, false);
//            ViewHolderRecycler viewHolder = new ViewHolderRecycler(view);
            return new HolderSongRecent(view, mListener);
        } else {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.progressbar_circle, viewGroup, false);
            return new LoadingViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        showSongItem((HolderSongRecent) viewHolder, i);

    }

    private void showSongItem(HolderSongRecent viewHolder, int position) {
        SongModel songModel = mListSong.get(position);
        viewHolder.bindContent(songModel);
//        viewHolder.btnOptionSong.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick: OPTION MENU CLICK " + v.getId());
////                Toast.makeText(MainActivity.getMainActivity(),"CLICK OPTION MENU",Toast.LENGTH_SHORT).show();
//            }
//        });
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

    private class HolderSongRecent extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private ImageCacheHelper mImageCacheHelper = new ImageCacheHelper(R.mipmap.music_128);
        private MultiClickAdapterListener mListener;
        private TextView titleSong;
        private TextView artist;
        private TextView duration;
        private ImageView imageView;
        private ImageButton btnOptionSong;
        private CardView layoutItemSong;
        private int albumId = -1;


        public HolderSongRecent(@NonNull View itemView, MultiClickAdapterListener listenerCustom) {
            super(itemView);
            mListener = listenerCustom;
            this.titleSong = itemView.findViewById(R.id.txtTitle);
            this.artist = itemView.findViewById(R.id.txtArtist);
            this.imageView = itemView.findViewById(R.id.imgSong);
            this.duration = itemView.findViewById(R.id.txtDuration);
            this.btnOptionSong = itemView.findViewById(R.id.btnOptionSong);
            layoutItemSong = itemView.findViewById(R.id.layoutItemSong);

            btnOptionSong.setOnClickListener(this);
            layoutItemSong.setOnClickListener(this);
            layoutItemSong.setOnLongClickListener(this);

        }

        @SuppressLint("SetTextI18n")
        public void bindContent(SongModel songModel) {

            this.titleSong.setText(songModel.getTitle());
            this.artist.setText(songModel.getArtist());//+ "_" + songModel.getAlbumId()
            this.duration.setText(SongModel.formateMilliSeccond(songModel.getDuration()));
//            final Bitmap bitmap = mImageCacheHelper.getBitmapCache(songModel.getAlbumId());//  mBitmapCache.get((long) songModel.getAlbumId());
            mImageCacheHelper.loadAlbumArt(imageView, songModel);
            albumId = songModel.getAlbumId();
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btnOptionSong) {
                mListener.optionMenuClick(v, getAdapterPosition());
            } else {
                mListener.layoutItemClick(v, getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
//        Log.d(TAG, "onLongClick: " + v.getId());
            mListener.layoutItemLongClick(v, getAdapterPosition());

            return true;
        }
    }

}
