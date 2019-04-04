package com.example.musicforlife.play;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.musicforlife.R;

public class FragmentPlayList extends Fragment {
    Context _context;
    NestedScrollView _layoutPlayList;
    public  FragmentPlayList(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        _context=getActivity();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        _layoutPlayList = (NestedScrollView) inflater.inflate(R.layout.fragment_playlist, null);
        return _layoutPlayList;
    }
}
