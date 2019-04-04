package com.example.musicforlife;

public interface MainCallbacks {
    void TestMessageFromFragmentToActivity(String sender);
    void playSongFromFragmentListToMain (String Sender,SongModel songModel);
}
