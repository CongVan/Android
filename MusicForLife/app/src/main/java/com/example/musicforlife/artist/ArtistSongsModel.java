package com.example.musicforlife.artist;

import java.util.ArrayList;

public class ArtistSongsModel {

    public ArtistSongsModel(String songId,String song,String artist,String duration)
    {
        SongId = songId;
        NameSong = song;
        NameSongArtist = artist;
        Duration = duration;
    }
    private String SongId;

    public void setSongId(String songId) {
        SongId = songId;
    }

    public String getSongId() {
        return SongId;
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

}
