package com.example.musicforlife.artist;

import android.app.Fragment;
import android.app.RemoteInput;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.musicforlife.MainActivity;
import com.example.musicforlife.R;
import com.example.musicforlife.listsong.SongModel;
import com.example.musicforlife.playlist.FragmentPlaylist;

import java.util.ArrayList;

public class FragmentArtistSong extends Fragment {
    View view;
    Context context;
    ListView LVArtistSongList;
    String artistQuery = "";
    ArtistSongsActivity _artistSongsActivity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //
        _artistSongsActivity = (ArtistSongsActivity)getActivity();
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
        final ArrayList<SongModel> artistSongsList = ArtistProvider.getArtistSongs(context,artistQuery);
        ArtistSongsAdapter artistSongsAdapter = new ArtistSongsAdapter(context,artistSongsList);
        LVArtistSongList.setAdapter(artistSongsAdapter);
        LVArtistSongList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SongModel songModel = artistSongsList.get(position);
                Intent intent=new Intent();
                intent.putExtra(ArtistModel.RequestCodeString,songModel);
                _artistSongsActivity.setResult(ArtistModel.RequestCode,intent);
                _artistSongsActivity.finish();
            }
        });

        return view;
    }
}
