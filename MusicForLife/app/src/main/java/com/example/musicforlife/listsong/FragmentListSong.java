package com.example.musicforlife.listsong;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;

import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.LinearLayout;
import android.widget.Toast;


import com.example.musicforlife.callbacks.FragmentCallbacks;
import com.example.musicforlife.playlist.FragmentPlaylist;
import com.example.musicforlife.MainActivity;
import com.example.musicforlife.R;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;


public class FragmentListSong extends Fragment implements FragmentCallbacks {
    MainActivity _mainActivity;
    Context _context;
    ArrayList<SongModel> _listSong;
    RecyclerView _listViewSong;
    ListSongRecyclerAdaper _listSongAdapter;

    private static final String TAG = "FRAGMENT_LIST_SONG";
    public static final String SENDER = "FRAGMENT_LIST_SONG";
    private static final int mThreshHold = 10;
    private static boolean mIsLoading = false;

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
        Bundle args = new Bundle();
        args.putString("Key1", "OK");
        fragmentListSong.setArguments(args);
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
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _listSong = SongModel.getSongsWithThreshold(MainActivity.mDatabaseHelper, 0, mThreshHold);// getAllAudioFromDevice(_context);

//        _layoutListSong = (NestedScrollView) _inflater.inflate(R.layout.fragment_list_song, null);
        _listViewSong = (RecyclerView) view.findViewById(R.id.lsvSongs);
//        _listViewSong.addItemDecoration(new DividerItemDecoration(_listViewSong.getContext(), DividerItemDecoration.VERTICAL));

        _listSongAdapter = new ListSongRecyclerAdaper(_context, _listSong);
        _listViewSong.setLayoutManager(new LinearLayoutManager(_context));
        _listViewSong.setAdapter(_listSongAdapter);

        _listViewSong.setItemViewCacheSize(240);
        _listViewSong.setDrawingCacheEnabled(true);
        _listViewSong.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);

//        _listSongAdapter.notifyItemRangeChanged();

        _listViewSong.addOnItemTouchListener(
                new RecyclerItemClickListener(_context, _listViewSong, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // do whatever
//                        Toast.makeText(_context,"CLICK ITEM SONG SONG ID: "+_listSong.get(position).getSongId(),Toast.LENGTH_SHORT).show();
//call one song
//                        _mainActivity.playSongFromFragmentListToMain(FragmentPlaylist.SENDER,_listSong.get(position));
                        //call play list song
                        _mainActivity.playSongsFromFragmentListToMain(FragmentPlaylist.SENDER, _listSong.get(position), _listSong);
                        //call play list id
//                        ArrayList<Integer> arrSongsId=new ArrayList<>();
//                        for (SongModel song:_listSong){
//                            arrSongsId.add(song.getId());
//                        }
//                        _mainActivity.playSongsIdFromFragmentListToMain(FragmentPlaylist.SENDER,_listSong.get(position),arrSongsId);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                        Toast.makeText(_context, "LONG CLICK ITEM SONG" + position, Toast.LENGTH_SHORT).show();
                    }
                })
        );
        _listViewSong.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                Log.d(TAG, "onScrolled: " + dx + "_" + dy + "___" + linearLayoutManager.findLastCompletelyVisibleItemPosition());
                if (!mIsLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == _listSong.size() - 1) {
                        loadMore();
                        mIsLoading = true;
                    }
                }

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: STARTED CREATE VIEW");
        return inflater.inflate(R.layout.fragment_list_song, container, false);

    }


    @Override
    public void TestMessageFromFragmentToActivity(String message) {

    }

    @Override
    public void playSongFromFragmentListToMain(SongModel songModel) {

    }

    private void loadMore() {
        _listSong.add(null);
        _listSongAdapter.notifyItemInserted(_listSong.size() - 1);
//        Handler handler = new Handler();
        _listViewSong.post(new Runnable() {
            @Override
            public void run() {
                _listSong.remove(_listSong.size() - 1);
                int scollPosition = _listSong.size();
                _listSongAdapter.notifyItemRemoved(scollPosition);
                ArrayList<SongModel> tempAudioList = SongModel.getSongsWithThreshold(MainActivity.mDatabaseHelper, _listSong.size(), mThreshHold);
                _listSong.addAll(tempAudioList);
                _listSongAdapter.notifyDataSetChanged();
                mIsLoading = false;
            }
        });


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

    private class loadImageFromStorage extends AsyncTask<Void, Integer, ArrayList<SongModel>> {

        @Override
        protected void onPostExecute(ArrayList<SongModel> songModels) {
            super.onPostExecute(songModels);
            _listSong.remove(_listSong.size() - 1);
            final int positionStart = _listSong.size() + 1;
            _listSong.addAll(songModels);
            Log.i(TAG, "onPostExecute: SONGS--> " + _listSong.size());
            _listViewSong.postDelayed(new Runnable() {
                @Override
                public void run() {
//                    _listSongAdapter.notifyDataSetChanged();
                    _listSongAdapter.notifyItemRangeChanged(positionStart, _listSong.size());
                    _listSongAdapter.notifyItemRemoved(positionStart);
                    _listSongAdapter.notifyItemChanged(positionStart);
                    _listSongAdapter.notifyItemInserted(positionStart);
                }
            }, 5000);
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
            ArrayList<SongModel> tempAudioList = SongModel.getSongsWithThreshold(MainActivity.mDatabaseHelper, _listSong.size(), mThreshHold);

            return tempAudioList;
        }


    }

}
