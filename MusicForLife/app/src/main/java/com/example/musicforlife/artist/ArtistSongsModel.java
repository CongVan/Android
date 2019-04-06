package com.example.musicforlife.artist;

import java.util.ArrayList;

public class ArtistSongsModel {

    public ArtistSongsModel(String song,String artist,String duration)
    {
        NameSong = song;
        NameSongArtist = artist;
        Duration = duration;
    }
    private String NameSong;

    public void setNameSong(String nameSong) {
        NameSong = nameSong;
    }

    public String getNameSong() {
        return NameSong;
    }

    private String NameSongArtist;

    public void setNameSongArtist(String nameSongArtist) {
        NameSongArtist = nameSongArtist;
    }

    public String getNameSongArtist() {
        return NameSongArtist;
    }

    private String Duration;

    public void setDuration(String duration) {
        Duration = duration;
    }

    public String getDuration() {
        return Duration;
    }

    public static ArrayList<ArtistSongsModel> getArtistSongs(String artist){
        ArrayList<ArtistSongsModel> arr =  new ArrayList<ArtistSongsModel>();
        arr.add(new ArtistSongsModel("1","1" , "1"));
        arr.add(new ArtistSongsModel("2","2" , "2"));
        arr.add(new ArtistSongsModel("3","3" , "3"));
        arr.add(new ArtistSongsModel("4","4" , "4"));
        return arr;
    }
}
