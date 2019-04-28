package com.example.musicforlife.play;

import com.example.musicforlife.listsong.SongModel;

public interface FragmentPlayInterface {

    //Fragment ListPlaying
    void updateListPlaying();
    void refreshListPlaying();
    //Fragment Playing
    void updateControlPlaying(SongModel songModel);
    void updateSeekbar(int currentDuration);
    void updateButtonPlay();
}
