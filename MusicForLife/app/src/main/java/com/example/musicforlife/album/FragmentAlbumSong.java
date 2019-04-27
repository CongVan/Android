package com.example.musicforlife.album;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.Nullable;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.musicforlife.MainActivity;
import com.example.musicforlife.R;
import com.example.musicforlife.listsong.SongModel;
import com.example.musicforlife.playlist.FragmentPlaylist;

import java.util.ArrayList;

public class FragmentAlbumSong extends Fragment {
    View view;
    Context context;
    ListView LVAlbumSongList;
    String albumQuery = "";
    AlbumSongsActivity _albumSongsActivity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        _albumSongsActivity = (AlbumSongsActivity)getActivity();

        context = (AlbumSongsActivity)getActivity();

        view = inflater.inflate(R.layout.fragment_album_song,container,false);

        Bundle bundle = getArguments();
        if(bundle != null){
            albumQuery = bundle.getString("albumQuery");
        }

        LVAlbumSongList = view.findViewById(R.id.lvAlbumSongList);
        final ArrayList<SongModel> albumSongsList = AlbumProvider.getALbumSongs(context,albumQuery);
        AlbumSongAdapter albumSongAdapter = new AlbumSongAdapter(context,albumSongsList);
        LVAlbumSongList.setAdapter(albumSongAdapter);
        LVAlbumSongList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SongModel songModel = albumSongsList.get(position);
                MainActivity _mainActivity =  MainActivity.getMainActivity();
                _mainActivity.playSongsFromFragmentListToMain(FragmentPlaylist.SENDER,songModel,albumSongsList);
            }
        });
        return view;
    }
}
