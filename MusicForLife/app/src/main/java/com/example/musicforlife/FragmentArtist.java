package com.example.musicforlife;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentArtist extends Fragment {
    private static final String TAG = "FRAGMENT_ARTIST";
    public static final String SENDER="FRAGMENT_ARTIST";

    public FragmentArtist(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup= (ViewGroup)inflater.inflate(R.layout.fragment_artist, container, false);
        return viewGroup;
    }
}
