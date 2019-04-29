package com.example.musicforlife.playlist;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;

import com.example.musicforlife.R;
import com.example.musicforlife.listsong.SongModel;


@SuppressLint("ValidFragment")
public class BottomSheetOptionSong extends BottomSheetDialogFragment {

    private SongModel mCurrentSong;

    @SuppressLint("ValidFragment")
    public BottomSheetOptionSong(SongModel songOption) {
        mCurrentSong = songOption;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.bottom_sheet_option_song, null);





        dialog.setContentView(contentView);
    }


}
