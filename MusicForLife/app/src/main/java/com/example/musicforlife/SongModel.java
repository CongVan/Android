package com.example.musicforlife;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class SongModel {
    private String path;
    private String title;
    private String album;
    private String artist;
    private Bitmap bitmap;
    private  String duration;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public static ArrayList<SongModel> getAllAudioFromDevice(final Context context) {
        final ArrayList<SongModel> tempAudioList = new ArrayList<SongModel>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//        String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.TITLE, MediaStore.Audio.AudioColumns.ALBUM, MediaStore.Audio.ArtistColumns.ARTIST,};
        Cursor c = context.getContentResolver().query(uri, null, null, null, null);
        int debugLoop = 20;
        if (c != null) {
            int count = 0;
            while (c.moveToNext()) {// && count++<debugLoop
                count++;
//                Log.d(TAG, "getAllAudioFromDevice: " + count);
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
                    bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.musical_note_light_64);
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
    private static String formateMilliSeccond(long milliseconds) {
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
