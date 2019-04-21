package com.example.musicforlife.album;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.musicforlife.MainActivity;
import com.example.musicforlife.R;

import java.util.ArrayList;

public class FragmentAlbum extends Fragment {
    private static final String TAG = "FRAGMENT_ALBUM";
    public static final String SENDER="FRAGMENT_ALBUM";

    View view;
    ArrayList<AlbumViewModel> arrAlbum;
    RecyclerView RCalbum;
    Context context;
    public FragmentAlbum(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //get context activity
        context = (MainActivity)getActivity();

        //get view from infalter
        view = inflater.inflate(R.layout.fragment_album,container,false);

        //get list artist from db
        arrAlbum = AlbumProvider.getListAlbum(context);

        //get RecyclerView Album by id
        RCalbum = (RecyclerView)view.findViewById(R.id.rvAlbumList);

        //map layout with adapter
        AlbumListAdapter albumListAdapter = new AlbumListAdapter(context,arrAlbum);
        RCalbum.setLayoutManager(new LinearLayoutManager(context));
        RCalbum.setAdapter(albumListAdapter);
        return view;
    }
}
