package com.example.musicforlife.play;

import com.example.musicforlife.R;

public enum FragmentModel {
    PLAYLIST("Playlist", R.layout.fragment_playlist),
    PLAYING("Playing",R.layout.fragment_playing);

    private String titleName;
    private int layoutId;
    FragmentModel(String s, int fragmentid) {
        titleName=s;
        layoutId=fragmentid;
    }

    public String getTitleName() {
        return titleName;
    }

//    public void setTitleName(String titleName) {
//        this.titleName = titleName;
//    }

    public int getLayoutId() {
        return layoutId;
    }

//    public void setLayoutId(int layoutId) {
//        this.layoutId = layoutId;
//    }
}
