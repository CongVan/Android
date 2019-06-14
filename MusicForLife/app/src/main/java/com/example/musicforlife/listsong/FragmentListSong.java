package com.example.musicforlife.listsong;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.content.Context;

import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ContextThemeWrapper;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;


//import com.ethanhua.skeleton.Skeleton;
//import com.ethanhua.skeleton.SkeletonScreen;
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.GlideBuilder;
//import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
//import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
//import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.example.musicforlife.callbacks.FragmentCallbacks;
import com.example.musicforlife.db.DatabaseManager;
import com.example.musicforlife.play.PlayService;
import com.example.musicforlife.playlist.BottomSheetOptionSong;
import com.example.musicforlife.playlist.FragmentPlaylist;
import com.example.musicforlife.MainActivity;
import com.example.musicforlife.R;
import com.example.musicforlife.recent.RecentModel;

import java.util.ArrayList;


public class FragmentListSong extends Fragment implements FragmentCallbacks, MultiClickAdapterListener {
    private MainActivity _mainActivity;
    private Context _context;
    private ArrayList<SongModel> _listSong;
    private RecyclerView _listViewSong;
    private TextView _txtSizeOfListSong;
    private ListSongRecyclerAdaper _listSongAdapter;
    private SwipeRefreshLayout mSwpListSong;


    //    SkeletonScreen _skeletonScreen;
    private static final String TAG = "FRAGMENT_LIST_SONG";
    public static final String SENDER = "FRAGMENT_LIST_SONG";
    private static final int mThreshHold = 10;
    private static boolean mIsLoading;
    private static PlayService mPlayService;
    private MultiClickAdapterListener myAdapterListener;
    private static Thread mThreadInitListPlaying;
    static String searchValue = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            _context = getActivity();
            _mainActivity = (MainActivity) getActivity();
        } catch (IllegalStateException e) {

        }

    }

    public static FragmentListSong newInstance() {
        FragmentListSong fragmentListSong = new FragmentListSong();
//        Bundle args = new Bundle();
//        args.putString("Key1", "OK");
//        fragmentListSong.setArguments(args);
        mPlayService = PlayService.newInstance();

        return fragmentListSong;
    }

    @Override
    public void onResume() {
        super.onResume();
//        Log.i(TAG, "onResume: STARTED");
//        if (_listSong != null || _listSong.size() == 0) {
//            new loadImageFromStorage().execute();
//        }

//        AlbumModel.getAllAlbumFromDevice(_context);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                _txtSizeOfListSong.setText("Tìm thấy " + String.valueOf(SongModel.getRowsSong(MainActivity.mDatabaseManager)) + " bài hát");
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: STARTED CREATE VIEW");
        View view = inflater.inflate(R.layout.fragment_list_song, container, false);
        _txtSizeOfListSong = view.findViewById(R.id.txtSizeOfListSong);
        _listViewSong = view.findViewById(R.id.lsvSongs);
        mSwpListSong = view.findViewById(R.id.swpListSong);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                _listSong = SongModel.getSongsWithThreshold(MainActivity.mDatabaseManager,searchValue, 0, 20);
                _listSongAdapter = new ListSongRecyclerAdaper(_context, _listSong, FragmentListSong.this);
                _listViewSong.setLayoutManager(new LinearLayoutManager(_context));
                _listViewSong.setAdapter(_listSongAdapter);
                _txtSizeOfListSong.setText("Tìm thấy " + String.valueOf(SongModel.getRowsSong(MainActivity.mDatabaseManager)) + " bài hát");
            }
        });

        _listViewSong.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (linearLayoutManager != null) {
                    Log.d(TAG, "onScrolled: " + dx + "_" + dy + "___" + linearLayoutManager.getItemCount() + "_" + linearLayoutManager.findLastVisibleItemPosition());
                }

                if (!mIsLoading && linearLayoutManager != null && linearLayoutManager.getItemCount() - 1 == linearLayoutManager.findLastVisibleItemPosition()) {
                    loadMore();
                    mIsLoading = true;
                }


            }
        });
        mSwpListSong.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ArrayList<SongModel> tempSongs = SongModel.getSongsWithThreshold(MainActivity.mDatabaseManager,searchValue, 0, mThreshHold);
                _listSong.clear();
                _listSongAdapter.notifyDataSetChanged();
                _listSong.addAll(tempSongs);
                _listSongAdapter.notifyDataSetChanged();
                mSwpListSong.setRefreshing(false);
            }
        });

        return view;

    }

    @Override
    public void updateSizeOfListSong() {
        _txtSizeOfListSong.setText("Tìm thấy " + String.valueOf(SongModel.getRowsSong(MainActivity.mDatabaseManager)) + " bài hát");
    }

    private void loadMore() {
//        _skeletonScreen = Skeleton.bind(_listViewSong).adapter(_listSongAdapter).load(R.layout.layout_item_song).show();
        _listSong.add(null);
        _listSongAdapter.notifyItemInserted(_listSong.size());
//        Handler handler = new Handler();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                ArrayList<SongModel> tempAudioList = SongModel.getSongsWithThreshold(MainActivity.mDatabaseManager,searchValue, _listSong.size(), mThreshHold);
                _listSong.remove(_listSong.size() - 1);
                _listSongAdapter.notifyItemRemoved(_listSong.size());
                _listSong.addAll(tempAudioList);
                _listSongAdapter.notifyItemInserted(_listSong.size());
//                _listSongAdapter.notifyDataSetChanged();
                mIsLoading = false;
            }
        }, 0);

    }


    private void playSong(SongModel songPlay) {
        mPlayService.play(songPlay);

        if (mThreadInitListPlaying != null && mThreadInitListPlaying.isAlive()) {
            mThreadInitListPlaying.interrupt();
        }

        mThreadInitListPlaying = new Thread(new Runnable() {
            @Override
            public void run() {
                mPlayService.initListPlaying(SongModel.getAllSongs(DatabaseManager.getInstance()));
            }
        });
        mThreadInitListPlaying.start();
        _mainActivity.playSongsFromFragmentListToMain(FragmentPlaylist.SENDER);
    }

    private void showBottomSheetOptionSong(SongModel song) {

        BottomSheetOptionSong bottomSheetDialogFragment = new BottomSheetOptionSong(song);
        bottomSheetDialogFragment.show(_mainActivity.getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
    }


    @Override
    public void optionMenuClick(View v, int position) {
        final SongModel songChose = _listSong.get(position);
        showBottomSheetOptionSong(songChose);
    }

    @Override
    public void checkboxClick(View v, int position) {

    }

    @Override
    public void layoutItemClick(View v, int position) {
        final SongModel songChose = _listSong.get(position);
        playSong(songChose);

    }

    @Override
    public void layoutItemLongClick(View v, int position) {
        final SongModel songChose = _listSong.get(position);
        showBottomSheetOptionSong(songChose);
    }

    private class loadImageFromStorage extends AsyncTask<Void, Integer, ArrayList<SongModel>> {

        @Override
        protected void onPostExecute(ArrayList<SongModel> songModels) {
            super.onPostExecute(songModels);
            _listSong.remove(_listSong.size() - 1);
            final int positionStart = _listSong.size() + 1;
            _listSong.addAll(songModels);
            Log.i(TAG, "onPostExecute: SONGS--> " + _listSong.size());
            _listViewSong.post(new Runnable() {
                @Override
                public void run() {
//                    _listSongAdapter.notifyDataSetChanged();
                    _listSongAdapter.notifyItemRangeChanged(positionStart, _listSong.size());
                    _listSongAdapter.notifyItemRemoved(positionStart);
                    _listSongAdapter.notifyItemChanged(positionStart);
                    _listSongAdapter.notifyItemInserted(positionStart);
                }
            });
            Log.i(TAG, "onPostExecute: FINISHED");
            mIsLoading = false;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        public ArrayList<SongModel> doInBackground(Void... voids) {
            _listSong.add(null);
            _listSongAdapter.notifyItemInserted(_listSong.size());
            ArrayList<SongModel> tempAudioList = SongModel.getSongsWithThreshold(MainActivity.mDatabaseManager,searchValue, _listSong.size(), mThreshHold);

            return tempAudioList;
        }


    }

    public void UpdateSearch(String s){
        if(s == searchValue) return;
        searchValue = s;
        mIsLoading = true;
        ArrayList<SongModel> tempAudioList = SongModel.getSongsWithThreshold(MainActivity.mDatabaseManager,searchValue, 0, mThreshHold);
        _listSong.clear();
        _listSongAdapter.notifyDataSetChanged();
        _listSong.addAll(tempAudioList);
        _listSongAdapter.notifyDataSetChanged();
        mIsLoading = false;
    }


}
