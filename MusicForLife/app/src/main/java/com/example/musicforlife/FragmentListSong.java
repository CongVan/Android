package com.example.musicforlife;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;

import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;


import com.example.musicforlife.db.DatabaseHelper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class FragmentListSong extends Fragment implements FragmentCallbacks {
    MainActivity _mainActivity;
    Context _context;
    LayoutInflater _inflater;
    ArrayList<SongModel> _listSong;
    //    CoordinatorLayout _layoutListSong;
    NestedScrollView _layoutListSong;
    //    ListView _listViewSong;
//    ListSongAdapter _listSongAdapter;
    RecyclerView _listViewSong;
    ListSongRecyclerAdaper _listSongAdapter;
    Button btnCheckout;
    Button btnBottomSheet;
    NestedScrollView layoutBottomSheet;

    BottomSheetBehavior sheetBehavior;

    private static final String TAG = "FRAGMENT_LIST_SONG";
    public static final String SENDER = "FRAGMENT_LIST_SONG";

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
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: STARTED");
        new loadImageFromStorage().execute();
//        AlbumModel.getAllAlbumFromDevice(_context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _listSong = new ArrayList<>();// getAllAudioFromDevice(_context);

//        _layoutListSong = (NestedScrollView) _inflater.inflate(R.layout.fragment_list_song, null);
        _listViewSong = (RecyclerView) view.findViewById(R.id.lsvSongs);
//        _listViewSong.addItemDecoration(new DividerItemDecoration(_listViewSong.getContext(), DividerItemDecoration.VERTICAL));

        _listSongAdapter = new ListSongRecyclerAdaper(_context, _listSong);
        _listViewSong.setLayoutManager(new LinearLayoutManager(_context));
        _listViewSong.setAdapter(_listSongAdapter);
        _listViewSong.addOnItemTouchListener(
                new RecyclerItemClickListener(_context, _listViewSong ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever
                        Toast.makeText(_context,"CLICK ITEM SONG"+position,Toast.LENGTH_SHORT).show();
                        _mainActivity.playSongFromFragmentListToMain(FragmentPlaylist.SENDER,_listSong.get(position));
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                        Toast.makeText(_context,"LONG CLICK ITEM SONG"+position,Toast.LENGTH_SHORT).show();
                    }
                })
        );

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.i(TAG, "onCreateView: STARTED CREATE VIEW");
        return inflater.inflate(R.layout.fragment_list_song, container, false);


//        _listViewSong.setNestedScrollingEnabled(true);
//        _listViewSong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.i(TAG, "onItemClick: OKOKOKO"+position);
//                _mainActivity.TestMessageFromFragmentToActivity(SENDER);
//                Toast.makeText(_context,"OKOKO__"+position,Toast.LENGTH_SHORT).show();
//            }
//        });

//        btnCheckout = _layoutListSong.findViewById(R.id.btnCheckout);
//        btnCheckout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(_context, "OK", Toast.LENGTH_SHORT).show();
//            }
//        });
//        btnBottomSheet = _layoutListSong.findViewById(R.id.btn_bottom_sheet);
//        layoutBottomSheet = _layoutListSong.findViewById(R.id.bottom_sheet);
//        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
////        sheetBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);
////        layoutBottomSheet.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
//        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//            @Override
//            public void onStateChanged(@NonNull View bottomSheet, int newState) {
//                switch (newState) {
//                    case BottomSheetBehavior.STATE_HIDDEN:
//                        break;
//                    case BottomSheetBehavior.STATE_EXPANDED: {
//                        btnBottomSheet.setText("Close Sheet");
////                        bottomNavigationView.setSystemUiVisibility(View.INVISIBLE);
//                        layoutBottomSheet.bringToFront();
//                    }
//                    break;
//                    case BottomSheetBehavior.STATE_COLLAPSED: {
//                        btnBottomSheet.setText("Expand Sheet");
////                        bottomNavigationView.setSystemUiVisibility(View.VISIBLE);
//                    }
//                    break;
//                    case BottomSheetBehavior.STATE_DRAGGING:
//                        break;
//                    case BottomSheetBehavior.STATE_SETTLING:
//                        break;
//                }
//            }
//
//            @Override
//            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//
//            }
//        });
//        return _layoutListSong;
    }

//    public void toggleBottomSheet(View view) {
//        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
//            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//            btnBottomSheet.setText("Close sheet");
//        } else {
//            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//            btnBottomSheet.setText("Expand sheet");
//        }
//    }

    public ArrayList<SongModel> getAllAudioFromDevice(final Context context) {
        final ArrayList<SongModel> tempAudioList = new ArrayList<SongModel>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//        String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.TITLE, MediaStore.Audio.AudioColumns.ALBUM, MediaStore.Audio.ArtistColumns.ARTIST,};
        Cursor c = context.getContentResolver().query(uri, null, null, null, null);
        int debugLoop = 20;
        if (c != null) {
            int count = 0;
            while (c.moveToNext()) {// && count++<debugLoop
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

    @Override
    public void TestMessageFromFragmentToActivity(String message) {

    }

    @Override
    public void playSongFromFragmentListToMain(SongModel songModel) {

    }

    private class loadImageFromStorage extends AsyncTask<Void, Integer, ArrayList<SongModel>> {

        @Override
        protected void onPostExecute(ArrayList<SongModel> songModels) {
            super.onPostExecute(songModels);
            _listSong.addAll(songModels);
            Log.i(TAG, "onPostExecute: SONGS--> " + _listSong.size());
            _listViewSong.post(new Runnable() {
                @Override
                public void run() {
                    _listSongAdapter.notifyDataSetChanged();
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
//            DatabaseHelper databaseHelper=DatabaseHelper.newInstance(_context);
//            SongModel.getSong(databaseHelper,58267);
//            final ArrayList<SongModel> tempAudioList = new ArrayList<SongModel>();
//            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
////        String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.TITLE, MediaStore.Audio.AudioColumns.ALBUM, MediaStore.Audio.ArtistColumns.ARTIST,};
//            Cursor c = _context.getContentResolver().query(uri, null, null, null, null);
//            int debugLoop = 40;
//            if (c != null) {
//                int count = 0;
//                while (c.moveToNext() && count++ < debugLoop) {// && count++<debugLoop
//                    SongModel songModel = new SongModel();
//                    String path = c.getString(c.getColumnIndex(MediaStore.Audio.AudioColumns.DATA));
//                    String name = c.getString(c.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE));
//                    String album = c.getString(c.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM));
//                    String artist = c.getString(c.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST));
//                    String duration = c.getString(c.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION));
//
//                    MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
//                    mediaMetadataRetriever.setDataSource(path);
//                    InputStream inputStream = null;
//                    Bitmap bitmap = null;
////                    try {
////
////                    } catch (Exception ex) {
////
////                    }
//
//                    if (mediaMetadataRetriever.getEmbeddedPicture() != null) {
//                        inputStream = new ByteArrayInputStream(mediaMetadataRetriever.getEmbeddedPicture());
//                        mediaMetadataRetriever.release();
//                        bitmap = BitmapFactory.decodeStream(inputStream);
//                    } else {
//                        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.musical_note_light_64);
//                    }
//
//
//                    songModel.setTitle(name);
//                    songModel.setAlbum(album);
//                    songModel.setArtist(artist);
//                    songModel.setPath(path);
//                    songModel.setBitmap(bitmap);
//                    songModel.setDuration(formateMilliSeccond(Long.valueOf(duration)));
////                Log.e("Name :" + name, " Album :" + album);
////                Log.e("Path :" + path, " Artist :" + artist);
//
//                    tempAudioList.add(songModel);
//                }
//                c.close();
//            }
//            ArrayList<SongModel> tempAudioList=SongModel.getAllAudioFromDevice(_context);
            ArrayList<SongModel> tempAudioList = SongModel.getAllSongs(MainActivity.mDatabaseHelper);
//            Log.i(TAG, "doInBackground: "+tempAudioList.size());
            return tempAudioList;
        }


    }

}
