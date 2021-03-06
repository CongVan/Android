package com.example.musicforlife.artist;

import android.graphics.Bitmap;

import java.io.Serializable;

public class ArtistViewModel extends ArtistModel {
    private Bitmap bitmap = null;

    public ArtistViewModel(String name, String path,int albumid, int count) {
        super(name, path,albumid, count);
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public ArtistModel getArtistModel(){
        return new ArtistModel(super.getName(),super.getPath(),getAlbumId(),super.getSongCount());
    }
}
