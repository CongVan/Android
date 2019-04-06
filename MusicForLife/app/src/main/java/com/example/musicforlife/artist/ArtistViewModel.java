package com.example.musicforlife.artist;

import java.io.Serializable;

public class ArtistViewModel implements Serializable {

    public ArtistViewModel(String name,String path,int count)
    {
        Name = name;
        Path = path;
        SongCount = count;
    }

    private String Path;

    public void setPath(String path) {
        Path = path;
    }

    public String getPath() {
        return Path;
    }

    private String Name;

    public void setName(String name) {
        Name = name;
    }

    public String getName() {
        return Name;
    }

    private int SongCount;

    public void setSongCount(int songCount) {
        SongCount = songCount;
    }

    public int getSongCount() {
        return SongCount;
    }
}
