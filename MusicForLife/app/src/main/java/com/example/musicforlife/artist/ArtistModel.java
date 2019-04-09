package com.example.musicforlife.artist;

import android.graphics.Bitmap;

public class ArtistModel extends ArtistViewModel {

    public ArtistModel(String name,String path,int count)
    {
        super(name,path,count);
    }
    private Bitmap bitmap = null;

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

}