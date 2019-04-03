package com.example.musicforlife.play;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.musicforlife.ListSongAdapter;
import com.example.musicforlife.PlayActivity;
import com.example.musicforlife.R;
import com.example.musicforlife.SongModel;

import java.util.ArrayList;


public class FragmentListPlaying extends Fragment {

    PlayActivity mPlayActivity;
    Context mContext;
    LayoutInflater mInflater;
    ArrayList<SongModel> mListSong;
    LinearLayout mLayoutListSong;
    ListView mListViewSong;
    ListSongAdapter mListSongAdapter;
    LoadImageFromStorage loadImageFromStorage;
    private static final String TAG = "FragmentListPlaying";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mContext = getActivity();
            mPlayActivity = (PlayActivity) getActivity();
            loadImageFromStorage=new LoadImageFromStorage();
        } catch (IllegalStateException e) {

        }

    }

    public static FragmentListPlaying newInstance() {
        FragmentListPlaying fragmentListPlaying = new FragmentListPlaying();
        Bundle args = new Bundle();
        args.putString("Key1", "OK");
        fragmentListPlaying.setArguments(args);
        return fragmentListPlaying;
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: STARTED");

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

//        ViewGroup viewGroup= (ViewGroup)inflater.inflate(R.layout.fragment_playlist, container, false);

        Log.i(TAG, "onCreateView PLAYLIST: OKOKOKO");
        mListSong = new ArrayList<>();// getAllAudioFromDevice(_context);
        mInflater = inflater;
        mLayoutListSong = (LinearLayout) mInflater.inflate(R.layout.fragment_list_playing, container, false);
        mListViewSong = mLayoutListSong.findViewById(R.id.lsvSongs);
        mListSongAdapter = new ListSongAdapter(mContext, mListSong);
        mListViewSong.setAdapter(mListSongAdapter);
        mListViewSong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        loadImageFromStorage.execute();
        return mLayoutListSong;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        loadImageFromStorage.cancel(true);
    }

    private class LoadImageFromStorage extends AsyncTask<Void, Integer, ArrayList<SongModel>> {

        @Override
        protected void onPostExecute(ArrayList<SongModel> songModels) {
            super.onPostExecute(songModels);
            mListSong.addAll(songModels);
            Log.i(TAG, "onPostExecute: SONGS--> " + mListSong.size());
            mListViewSong.post(new Runnable() {
                @Override
                public void run() {
                    mListSongAdapter.notifyDataSetChanged();
                }
            });
            Log.i(TAG, "onPostExecute: FINISHED");
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        public ArrayList<SongModel> doInBackground(Void... voids) {

            return SongModel.getAllAudioFromDevice(mContext);
        }


    }


}
