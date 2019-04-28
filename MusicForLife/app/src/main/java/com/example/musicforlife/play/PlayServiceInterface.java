package com.example.musicforlife.play;

import com.example.musicforlife.listsong.SongModel;

import java.util.ArrayList;

public interface PlayServiceInterface {
    void playSong(SongModel song);
    void nextSong();
    void prevSong();
    void pauseSong();
    void stopSong();
    void initListPlaying(ArrayList<SongModel> listPlaying);
}
