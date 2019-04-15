package com.example.musicforlife.callbacks;

import com.example.musicforlife.listsong.SongModel;

import java.util.ArrayList;

public interface MainCallbacks {
    void TestMessageFromFragmentToActivity(String sender);
    void playSongFromFragmentListToMain (String Sender, SongModel songModel);
    void playSongsFromFragmentListToMain (String Sender, SongModel songPlay, ArrayList<SongModel> songList);
    void playSongsIdFromFragmentListToMain (String Sender, SongModel songPlay, ArrayList<Integer> songsId);
    void togglePlayingMinimize();
}
