package com.example.musicforlife.album;

import java.io.Serializable;

public class AlbumModel implements Serializable {
    private String title;
    private String artist;
    private int numberOfSongs;
    private String Path;

    public AlbumModel(String title,String artist,int numberOfSongs,String path){
        this.title=title;
        this.artist=artist;
        this.numberOfSongs=numberOfSongs;
        this.Path = path;
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

    public void setNumberOfSongs(int numberOfSongs) {
        this.numberOfSongs = numberOfSongs;
    }

    public int getNumberOfSongs() {
        return numberOfSongs;
    }

    public void setPath(String path) {
        Path = path;
    }

    public String getPath() {
        return Path;
    }
}
