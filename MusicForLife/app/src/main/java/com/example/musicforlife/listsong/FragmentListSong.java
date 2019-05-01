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
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.example.musicforlife.callbacks.FragmentCallbacks;
import com.example.musicforlife.db.DatabaseManager;
import com.example.musicforlife.play.PlayService;
import com.example.musicforlife.playlist.BottomSheetOptionSong;
import com.example.musicforlife.playlist.FragmentPlaylist;
import com.example.musicforlife.MainActivity;
import com.example.musicforlife.R;

import java.util.ArrayList;


public class FragmentListSong extends Fragment implements FragmentCallbacks, MultiClickAdapterListener {
    MainActivity _mainActivity;
    Context _context;
    ArrayList<SongModel> _listSong;
    RecyclerView _listViewSong;
    TextView _txtSizeOfListSong;
    ListSongRecyclerAdaper _listSongAdapter;
    //    SkeletonScreen _skeletonScreen;
    private static final String TAG = "FRAGMENT_LIST_SONG";
    public static final String SENDER = "FRAGMENT_LIST_SONG";
    private static final int mThreshHold = 10;
    private static boolean mIsLoading;
    private static PlayService mPlayService;
    private MultiClickAdapterListener myAdapterListener;
    private static Thread mThreadInitListPlaying;

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
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                _listSong = SongModel.getSongsWithThreshold(MainActivity.mDatabaseManager, 0, mThreshHold);
                _listSongAdapter = new ListSongRecyclerAdaper(_context, _listSong, FragmentListSong.this);
                _listViewSong.setLayoutManager(new LinearLayoutManager(_context));
                _listViewSong.setAdapter(_listSongAdapter);
                _txtSizeOfListSong.setText("Tìm thấy " + String.valueOf(SongModel.getRowsSong(MainActivity.mDatabaseManager)) + " bài hát");
            }
        });
//        _listViewSong.addOnItemTouchListener(new RecyclerItemClickListener(_context, _listViewSong, new RecyclerItemClickListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                boolean isOptionMenuClick=false;
//                ImageButton btnOption = view.findViewById(R.id.btnOptionSong);
//                btnOption.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
////                        Toast.makeText(MainActivity.getMainActivity(), "CLICK OPTION MENU", Toast.LENGTH_SHORT).show();
//                        Log.d(TAG, "onClick: OPTION MENU CLICK " + v.getId());
//                    }
//                });
////
//                Toast.makeText(_context, "CLICK SONG " + view.getId() + "_" + view.getTag() + "_" + R.id.btnOptionSong, Toast.LENGTH_SHORT).show();
//////        Log.d(TAG, "onItemClick: CLICK SONG TAG: " + view.getTag());
////                final SongModel songPlay = _listSong.get(position);
////                mPlayService.play(songPlay);
////                new Thread(new Runnable() {
////                    @Override
////                    public void run() {
////                        mPlayService.initListPlaying(SongModel.getAllSongs(DatabaseManager.getInstance()));
////                        Log.d(TAG, "run: ");
////                    }
////                }).start();
////
////                _mainActivity.playSongsFromFragmentListToMain(FragmentPlaylist.SENDER);
//            }
//
//            @Override
//            public void onLongItemClick(View view, int position) {
//
//            }
//        }));
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

                if (!mIsLoading && linearLayoutManager != null && linearLayoutManager.getItemCount() - 1 <= linearLayoutManager.findLastVisibleItemPosition()) {
                    loadMore();
                    mIsLoading = true;
                }


            }
        });

        // getAllAudioFromDevice(_context);

//        _layoutListSong = (NestedScrollView) _inflater.inflate(R.layout.fragment_list_song, null);

//        _listViewSong.addItemDecoration(new DividerItemDecoration(_listViewSong.getContext(), DividerItemDecoration.VERTICAL));


//        _listViewSong.setItemViewCacheSize(240);
//        _listViewSong.setDrawingCacheEnabled(true);
//        _listViewSong.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);

//        _listSongAdapter.notifyItemRangeChanged();
//        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
//        itemAnimator.setAddDuration(1000);
//        itemAnimator.setRemoveDuration(1000);
//        _listViewSong.setItemAnimator(itemAnimator);


//        buildGlide();
//        Glide.get(_context).clearMemory();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                Glide.get(_context).clearDiskCache();
//            }
//        }).start();
        return view;

    }

    @Override
    public void updateSizeOfListSong() {
        _txtSizeOfListSong.setText("Tìm thấy " + String.valueOf(SongModel.getRowsSong(MainActivity.mDatabaseManager)) + " bài hát");
    }

    private void loadMore() {
//        _skeletonScreen = Skeleton.bind(_listViewSong).adapter(_listSongAdapter).load(R.layout.layout_item_song).show();
        _listSong.add(null);
        _listSongAdapter.notifyItemInserted(_listSong.size() - 1);
//        Handler handler = new Handler();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                _listSong.remove(_listSong.size() - 1);
                int scollPosition = _listSong.size();
                _listSongAdapter.notifyItemRemoved(scollPosition);
                ArrayList<SongModel> tempAudioList = SongModel.getSongsWithThreshold(MainActivity.mDatabaseManager, _listSong.size(), mThreshHold);
                _listSong.addAll(tempAudioList);

//                _listSongAdapter.notifyDataSetChanged();
                mIsLoading = false;
            }
        });
//        _listViewSong.post(new Runnable() {
//            @Override
//            public void run() {
//                _listSong.remove(_listSong.size() - 1);
//                int scollPosition = _listSong.size();
//                _listSongAdapter.notifyItemRemoved(scollPosition);
//                ArrayList<SongModel> tempAudioList = SongModel.getSongsWithThreshold(MainActivity.mDatabaseManager, _listSong.size(), mThreshHold);
//                _listSong.addAll(tempAudioList);
////                _listSongAdapter.notifyDataSetChanged();
//                mIsLoading = false;
////                _skeletonScreen.hide();
//            }
//        });


//        Log.i(TAG, "onPostExecute: SONGS--> " + _listSong.size());
//        _listViewSong.post(new Runnable() {
//            @Override
//            public void run() {
//                _listSongAdapter.notifyDataSetChanged();
//            }
//        });
//        Log.i(TAG, "onPostExecute: FINISHED");

//        new loadImageFromStorage().execute();
    }

    //    @Override
//    public void onItemClick(View view, final int position) {
//
////        ImageButton btnOption = view.findViewById(R.id.btnOptionSong);
////        btnOption.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////
////                Toast.makeText(MainActivity.getMainActivity(), "CLICK OPTION MENU", Toast.LENGTH_SHORT).show();
////                Log.d(TAG, "onClick: OPTION MENU CLICK " + v.getId());
////            }
////        });
//
//        Toast.makeText(_context, "CLICK SONG " + view.getId() + "_" + view.getTag() + "_" + R.id.btnOptionSong, Toast.LENGTH_SHORT).show();
////        Log.d(TAG, "onItemClick: CLICK SONG TAG: " + view.getTag());
//
//    }

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

//    @Override
//    public void onLongItemClick(View view, final int position) {
//        final SongModel songChose = _listSong.get(position);
//        showBottomSheetOptionSong(songChose);
//
////        Context wrapper = new ContextThemeWrapper(_mainActivity, R.style.PopupMenu);
////
////        PopupMenu popup = new PopupMenu(wrapper, view);
////        popup.getMenuInflater().inflate(R.menu.menu_option_song, popup.getMenu());
////
////        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
////            public boolean onMenuItemClick(MenuItem item) {
////                Toast.makeText(_mainActivity, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
////                return true;
////            }
////        });
////        popup.show();//showing popup menu
//
//
//    }

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
            ArrayList<SongModel> tempAudioList = SongModel.getSongsWithThreshold(MainActivity.mDatabaseManager, _listSong.size(), mThreshHold);

            return tempAudioList;
        }


    }

}
