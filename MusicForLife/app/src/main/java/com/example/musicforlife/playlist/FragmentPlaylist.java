package com.example.musicforlife.playlist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.musicforlife.R;

public class FragmentPlaylist extends Fragment {
    private static final String TAG = "FRAGMENT_PLAY_LIST";
    public static final String SENDER="FRAGMENT_PLAY_LIST";

    public FragmentPlaylist(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup= (ViewGroup)inflater.inflate(R.layout.fragment_playlist, container, false);
        return viewGroup;
    }
}
