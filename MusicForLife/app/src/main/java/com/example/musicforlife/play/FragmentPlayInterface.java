package com.example.musicforlife.play;

import com.example.musicforlife.listsong.SongModel;

public interface FragmentPlayInterface {


    void updateControlPlaying(SongModel songModel);
    void updateSeekbar(int currentDuration);
    void updateButtonPlay();
}
