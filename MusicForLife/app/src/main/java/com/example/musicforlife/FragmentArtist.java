package com.example.musicforlife;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class FragmentArtist extends Fragment {
    private static final String TAG = "FRAGMENT_ARTIST";
    public static final String SENDER="FRAGMENT_ARTIST";

    View view;
    ArrayList<ArtistModel> arrArtist;
    RecyclerView LVArtist;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //get view from infalter
        view = inflater.inflate(R.layout.fragment_artist,container,false);

        //get recyclerview
        LVArtist = (RecyclerView) view.findViewById(R.id.lvArtistList);

        //get list artist from db
        arrArtist = ArtistModel.getArtistModel(getActivity());

        //map layout with adapter
        ListArtistAdapter listArtistAdapter = new ListArtistAdapter(getActivity(),arrArtist);
        LVArtist.setLayoutManager(new LinearLayoutManager(getActivity()));
        LVArtist.setAdapter(listArtistAdapter);

        return view;
    }

    public static FragmentArtist newInstance() {
        FragmentArtist fragmentArtist = new FragmentArtist();
        Bundle args = new Bundle();
        args.putString("Key2", "OK");
        fragmentArtist.setArguments(args);
        return fragmentArtist;
    }
}
