package com.example.musicforlife.play;

import com.example.musicforlife.listsong.SongModel;

public interface PlayInterface {
    void controlSong(String sender, SongModel songModel, int action);
    void updateControlPlaying(String sender);
}
