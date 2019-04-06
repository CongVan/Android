package com.example.musicforlife.callbacks;

import com.example.musicforlife.listsong.SongModel;

public interface MainCallbacks {
    void TestMessageFromFragmentToActivity(String sender);
    void playSongFromFragmentListToMain (String Sender, SongModel songModel);
}
