package com.example.musicforlife.artist;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //get context activity
        context = (ArtistSongsActivity)getActivity();

        //get view from infalter
        view = inflater.inflate(R.layout.fragment_artist_song,container,false);

        LVArtistSongList = (ListView)view.findViewById(R.id.lvArtistSongList);
        ArrayList<ArtistSongsModel> artistSongsList = ArtistSongsModel.getArtistSongs("");
        ArtistSongsAdapter artistSongsAdapter = new ArtistSongsAdapter(context,artistSongsList);
        LVArtistSongList.setAdapter(artistSongsAdapter);
        return view;
    }
}
