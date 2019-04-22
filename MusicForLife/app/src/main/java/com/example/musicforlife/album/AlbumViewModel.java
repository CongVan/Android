package com.example.musicforlife.album;

import android.graphics.Bitmap;

public class AlbumViewModel extends AlbumModel {
    public AlbumViewModel(String title, String artist,String path, int numberOfSongs ) {
        super(title, artist, numberOfSongs,path);
    }
    private Bitmap bitmap = null;

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public AlbumModel getAlbumModel(){
        return new AlbumModel(super.getTitle(),super.getArtist(),super.getNumberOfSongs(),super.getPath());
    }

}
