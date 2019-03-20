package com.example.musicforlife;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class FragmentListSong extends Fragment {
    MainActivity _mainActivity;
    Context _context;
    ArrayList<SongModel> _listSong;
    LinearLayout _layoutListSong;
    ListView _listViewSong;
    ListSongAdapter _listSongAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            _context = getActivity();
            _mainActivity = (MainActivity) getActivity();
        } catch (IllegalStateException e) {

        }

    }
    public static FragmentListSong newInstance() {
        FragmentListSong fragmentListSong = new FragmentListSong();
        Bundle args = new Bundle();
        args.putString("Key1", "OK");
        fragmentListSong.setArguments(args);
        return fragmentListSong;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _listSong = getAllAudioFromDevice(_context);
        _layoutListSong = (LinearLayout) inflater.inflate(R.layout.layout_list_song, null);
        _listViewSong = (ListView) _layoutListSong.findViewById(R.id.lsvSongs);
        _listSongAdapter = new ListSongAdapter(_context, _listSong);
        _listViewSong.setAdapter(_listSongAdapter);
        _listViewSong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });


        return _layoutListSong;
    }

    public ArrayList<SongModel> getAllAudioFromDevice(final Context context) {
        final ArrayList<SongModel> tempAudioList = new ArrayList<SongModel>();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.TITLE, MediaStore.Audio.AudioColumns.ALBUM, MediaStore.Audio.ArtistColumns.ARTIST,};
        Cursor c = context.getContentResolver().query(uri, null, null, null, null);

        if (c != null) {
            while (c.moveToNext()) {
                SongModel SongModel = new SongModel();
                String path = c.getString(c.getColumnIndex(MediaStore.Audio.AudioColumns.DATA));
                String name = c.getString(c.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE));
                String album = c.getString(c.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM));
                String artist = c.getString(c.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST));

                SongModel.setTitle(name);
                SongModel.setAlbum(album);
                SongModel.setArtist(artist);
                SongModel.setPath(path);

//                Log.e("Name :" + name, " Album :" + album);
//                Log.e("Path :" + path, " Artist :" + artist);

                tempAudioList.add(SongModel);
            }
            c.close();
        }

        return tempAudioList;
    }
}
