package com.example.musicforlife;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
    ListView LVArtist;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_artist,container,false);
        LVArtist = (ListView) view.findViewById(R.id.lvArtistList);
        arrArtist = ArtistModel.getArtistModel(getActivity());
        ListArtistAdapter listArtistAdapter = new ListArtistAdapter(getActivity(),arrArtist);
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
