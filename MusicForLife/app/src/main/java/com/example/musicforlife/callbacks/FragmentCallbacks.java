package com.example.musicforlife.callbacks;

import com.example.musicforlife.listsong.SongModel;

public interface FragmentCallbacks {
     void TestMessageFromFragmentToActivity(String message);
     void playSongFromFragmentListToMain(SongModel songModel);

}
