package com.example.musicforlife.artist;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.musicforlife.R;

import java.util.ArrayList;

public class FragmentArtistSong extends Fragment {
    View view;
    Context context;
    ListView LVArtistSongList;
    String artistQuery = "";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //get context activity
        context = (ArtistSongsActivity)getActivity();

        //get view from infalter
        view = inflater.inflate(R.layout.fragment_artist_song,container,false);

        //get bunlde
        Bundle bundle = getArguments();
        if(bundle != null){
            artistQuery = bundle.getString("artistQuery");
        }

        LVArtistSongList = (ListView)view.findViewById(R.id.lvArtistSongList);
        ArrayList<ArtistSongsModel> artistSongsList = ArtistProvider.getArtistSongs(context,artistQuery);
        ArtistSongsAdapter artistSongsAdapter = new ArtistSongsAdapter(context,artistSongsList);
        LVArtistSongList.setAdapter(artistSongsAdapter);
        return view;
    }
}
