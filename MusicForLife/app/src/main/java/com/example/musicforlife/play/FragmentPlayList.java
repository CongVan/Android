package com.example.musicforlife.play;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.musicforlife.FragmentListSong;
import com.example.musicforlife.ListSongAdapter;
import com.example.musicforlife.MainActivity;
import com.example.musicforlife.PlayActivity;
import com.example.musicforlife.R;
import com.example.musicforlife.SongModel;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;


public class FragmentPlayList extends Fragment {

    PlayActivity mPlayActivity;
    Context mContext;
    LayoutInflater mInflater;
    ArrayList<SongModel> mListSong;
    LinearLayout mLayoutListSong;
    ListView mListViewSong;
    ListSongAdapter mListSongAdapter;
    private static final String TAG = "FragmentPlayList";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mContext = getActivity();
            mPlayActivity = (PlayActivity) getActivity();
        } catch (IllegalStateException e) {

        }

    }

    public static FragmentPlayList newInstance() {
        FragmentPlayList fragmentPlayList = new FragmentPlayList();
        Bundle args = new Bundle();
        args.putString("Key1", "OK");
        fragmentPlayList.setArguments(args);
        return fragmentPlayList;
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: STARTED");

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

//        ViewGroup viewGroup= (ViewGroup)inflater.inflate(R.layout.fragment_playlist, container, false);

        Log.i(TAG, "onCreateView PLAYLIST: OKOKOKO");
        mListSong = new ArrayList<>();// getAllAudioFromDevice(_context);
        mInflater = inflater;
        mLayoutListSong = (LinearLayout) mInflater.inflate(R.layout.fragment_playlist, container, false);
        mListViewSong = mLayoutListSong.findViewById(R.id.lsvSongs);
        mListSongAdapter = new ListSongAdapter(mContext, mListSong);
        mListViewSong.setAdapter(mListSongAdapter);
        mListViewSong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        new loadImageFromStorage().execute();
        return mLayoutListSong;
    }
    public ArrayList<SongModel> getAllAudioFromDevice(final Context context) {
        final ArrayList<SongModel> tempAudioList = new ArrayList<SongModel>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//        String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.TITLE, MediaStore.Audio.AudioColumns.ALBUM, MediaStore.Audio.ArtistColumns.ARTIST,};
        Cursor c = context.getContentResolver().query(uri, null, null, null, null);
        int debugLoop = 20;
        if (c != null) {
            int count = 0;
            while (c.moveToNext() && count++<debugLoop) {// && count++<debugLoop
                count++;
                Log.d(TAG, "getAllAudioFromDevice: " + count);
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


                if (mediaMetadataRetriever.getEmbeddedPicture() != null) {
                    inputStream = new ByteArrayInputStream(mediaMetadataRetriever.getEmbeddedPicture());
                    mediaMetadataRetriever.release();
                    bitmap = BitmapFactory.decodeStream(inputStream);
                } else {
                    bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.musical_note_light_64);
                }


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
    private class loadImageFromStorage extends AsyncTask<Void, Integer, ArrayList<SongModel>> {

        @Override
        protected void onPostExecute(ArrayList<SongModel> songModels) {
            super.onPostExecute(songModels);
            mListSong.addAll(songModels);
            Log.i(TAG, "onPostExecute: SONGS--> " + mListSong.size());
            mListViewSong.post(new Runnable() {
                @Override
                public void run() {
                    mListSongAdapter.notifyDataSetChanged();
                }
            });
            Log.i(TAG, "onPostExecute: FINISHED");
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        public ArrayList<SongModel> doInBackground(Void... voids) {
            final ArrayList<SongModel> tempAudioList = new ArrayList<SongModel>();
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//        String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.TITLE, MediaStore.Audio.AudioColumns.ALBUM, MediaStore.Audio.ArtistColumns.ARTIST,};
            Cursor c = mContext.getContentResolver().query(uri, null, null, null, null);
            int debugLoop = 40;
            if (c != null) {
                int count = 0;
                while (c.moveToNext() && count++ < debugLoop) {// && count++<debugLoop
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
//                    try {
//
//                    } catch (Exception ex) {
//
//                    }

                    if (mediaMetadataRetriever.getEmbeddedPicture() != null) {
                        inputStream = new ByteArrayInputStream(mediaMetadataRetriever.getEmbeddedPicture());
                        mediaMetadataRetriever.release();
                        bitmap = BitmapFactory.decodeStream(inputStream);
                    } else {
                        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.musical_note_light_64);
                    }


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
