package com.example.musicforlife;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentRecent extends Fragment {
    private static final String TAG = "FRAGMENT_RECENT";
    public static final String SENDER="FRAGMENT_RECENT";

    public FragmentRecent(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup= (ViewGroup)inflater.inflate(R.layout.fragment_recent, container, false);
        return viewGroup;
    }
}
