package com.example.musicforlife.recent;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.musicforlife.MainActivity;
import com.example.musicforlife.R;
import com.example.musicforlife.listsong.FragmentListSong;
import com.example.musicforlife.listsong.ListSongRecyclerAdaper;
import com.example.musicforlife.listsong.MultiClickAdapterListener;
import com.example.musicforlife.listsong.SongModel;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FragmentRecent extends Fragment implements MultiClickAdapterListener {
    private static final String TAG = "FRAGMENT_RECENT";
    public static final String SENDER = "FRAGMENT_RECENT";

    private RecyclerView mRcvSongRecent;
    private ListSongRecentAdaper _listSongAdapter;
    private ArrayList<SongModel> mListSongRecent;
    private Context mContext;
    private MainActivity mMainActivity;

    public FragmentRecent() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mContext = getActivity();
            mMainActivity = (MainActivity) getActivity();
        } catch (IllegalStateException e) {

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_recent, container, false);
        mRcvSongRecent = viewGroup.findViewById(R.id.rcvSongRecent);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mListSongRecent = RecentModel.getRecentSong();
                _listSongAdapter = new ListSongRecentAdaper(mContext, mListSongRecent, FragmentRecent.this);
                mRcvSongRecent.setLayoutManager(new LinearLayoutManager(mContext, LinearLayout.HORIZONTAL, false));
                mRcvSongRecent.setAdapter(_listSongAdapter);

            }
        });
        return viewGroup;
    }

    @Override
    public void optionMenuClick(View v, int position) {

    }

    @Override
    public void checkboxClick(View v, int position) {

    }

    @Override
    public void layoutItemClick(View v, int position) {

    }

    @Override
    public void layoutItemLongClick(View v, int position) {

    }
}
