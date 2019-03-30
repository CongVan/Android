package com.example.musicforlife;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class AlbumModel {
    private String title;
    private String artist;
    private int numberOfSongs;

    public  AlbumModel(String title,String artist,int numberOfSongs){
        this.title=title;
        this.artist=artist;
        this.numberOfSongs=numberOfSongs;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getnumberOfSongs() {
        return numberOfSongs;
    }

    public void setnumberOfSongs(int numberOfSongs) {
        this.numberOfSongs = numberOfSongs;
    }

    public static ArrayList<AlbumModel> getAllAlbumFromDevice(final Context context) {
        final ArrayList<AlbumModel> albumsList = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.AlbumColumns.ALBUM,
                MediaStore.Audio.AudioColumns.ARTIST,
                MediaStore.Audio.AudioColumns.COMPOSER,

        };
//        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
//        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0 ";
        Cursor c = context.getContentResolver().query(uri, projection, null, null, null);
        int debugLoop = 40;
        if (c != null) {
            int count = 0;
            while (c.moveToNext()&& count++<debugLoop) {// && count++<debugLoop
                count++;
//                Log.d(TAG, "getAllAudioFromDevice: " + count);

                String title = c.getString(0);
                String artist = c.getString(1);
                String numberOfSongs = c.getString(2);
                AlbumModel albumModel = new AlbumModel(title,artist, Integer.valueOf(numberOfSongs));
                albumsList.add(albumModel);

//                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
//                mediaMetadataRetriever.setDataSource(path);
//                InputStream inputStream ;
//                Bitmap bitmap;
//
//
//                if (mediaMetadataRetriever.getEmbeddedPicture() != null) {
//                    inputStream = new ByteArrayInputStream(mediaMetadataRetriever.getEmbeddedPicture());
//                    mediaMetadataRetriever.release();
//                    bitmap = BitmapFactory.decodeStream(inputStream);
//                } else {
//                    bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.musical_note_light_64);
//                }
//                songModel.setTitle(name);
//                songModel.setAlbum(album);
//                songModel.setArtist(artist);
//                songModel.setPath(path);
//                songModel.setBitmap(bitmap);
//                songModel.setDuration(formateMilliSeccond(Long.valueOf(duration)));
////                Log.e("Name :" + name, " Album :" + album);
////                Log.e("Path :" + path, " Artist :" + artist);
//
//                tempAudioList.add(songModel);
            }
            c.close();
        }
        return albumsList;
    }
    private static  String formateMilliSeccond(long milliseconds) {
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
