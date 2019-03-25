package com.example.musicforlife;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.MediaMetadataRetriever;
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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
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
//        String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.TITLE, MediaStore.Audio.AudioColumns.ALBUM, MediaStore.Audio.ArtistColumns.ARTIST,};
        Cursor c = context.getContentResolver().query(uri, null, null, null, null);
        int debugLoop=20;
        if (c != null) {
            int count=0;
            while (c.moveToNext() && count++<debugLoop) {
                SongModel songModel = new SongModel();
                String path = c.getString(c.getColumnIndex(MediaStore.Audio.AudioColumns.DATA));
                String name = c.getString(c.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE));
                String album = c.getString(c.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM));
                String artist = c.getString(c.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST));
                String duration = c.getString(c.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION));

                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                mediaMetadataRetriever.setDataSource(path);
                InputStream inputStream = null;
                Bitmap bitmap = null;
                try {
                    inputStream = new ByteArrayInputStream(mediaMetadataRetriever.getEmbeddedPicture());
                    mediaMetadataRetriever.release();   
                    bitmap = BitmapFactory.decodeStream(inputStream);
                } catch (Exception ex) {
                    bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.musical_note_light_64);
                }

//                if (mediaMetadataRetriever.getEmbeddedPicture() != null) {
//
//                }


                songModel.setTitle(name);
                songModel.setAlbum(album);
                songModel.setArtist(artist);
                songModel.setPath(path);
                songModel.setBitmap(bitmap);
                songModel.setDuration(formateMilliSeccond(Long.valueOf(duration)));
//                Log.e("Name :" + name, " Album :" + album);
//                Log.e("Path :" + path, " Artist :" + artist);

                tempAudioList.add(songModel);
            }
            c.close();
        }

        return tempAudioList;
    }
    public static String formateMilliSeccond(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);

        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        //      return  String.format("%02d Min, %02d Sec",
        //                TimeUnit.MILLISECONDS.toMinutes(milliseconds),
        //                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
        //                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));

        // return timer string
        return finalTimerString;
    }
}
