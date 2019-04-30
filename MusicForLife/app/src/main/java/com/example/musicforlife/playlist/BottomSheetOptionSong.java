package com.example.musicforlife.playlist;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.musicforlife.R;
import com.example.musicforlife.listsong.SongModel;
import com.example.musicforlife.utilitys.ImageHelper;


@SuppressLint("ValidFragment")
public class BottomSheetOptionSong extends BottomSheetDialogFragment implements View.OnClickListener {

    private SongModel mCurrentSong;
    private TextView mTxtTitleSong;
    private TextView mTxtArtist;
    private TableRow mTbrAddQueue;
    private TableRow mTbrAddPlaylist;
    private TableRow mTbrMakeRingTone;
    private TableRow mTbrDeleteSong;
    private ImageView mImgSong;
    private ImageHelper mImageHelper;

    @SuppressLint("ValidFragment")
    public BottomSheetOptionSong(SongModel songOption) {
        mCurrentSong = songOption;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.bottom_sheet_option_song, null);
        mImageHelper = new ImageHelper();

        mTxtTitleSong = contentView.findViewById(R.id.txtTitleSong);
        mTxtArtist = contentView.findViewById(R.id.txtArtist);
        mTbrAddQueue = contentView.findViewById(R.id.btnAddSongToQueue);
        mTbrAddPlaylist = contentView.findViewById(R.id.btnAddSongToPlaylist);
        mTbrMakeRingTone = contentView.findViewById(R.id.btnMakeRingTone);
        mTbrDeleteSong = contentView.findViewById(R.id.btnDeleteSong);
        mImgSong = contentView.findViewById(R.id.imgSong);


        mTbrAddQueue.setOnClickListener(this);
        mTbrAddPlaylist.setOnClickListener(this);
        mTbrMakeRingTone.setOnClickListener(this);
        mTbrDeleteSong.setOnClickListener(this);


        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mTxtTitleSong.setText(mCurrentSong.getTitle());
                mTxtArtist.setText(mCurrentSong.getArtist());
                mImgSong.setImageBitmap(ImageHelper.getBitmapFromPath(mCurrentSong.getPath(), R.mipmap.music_128));
            }
        });


        dialog.setContentView(contentView);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddSongToQueue:
                break;
            case R.id.btnAddSongToPlaylist:
                FragmentDialogPlaylist fragmentDialogPlaylist = new FragmentDialogPlaylist(mCurrentSong);
                fragmentDialogPlaylist.show(getActivity().getSupportFragmentManager(), "ADD_SONG_TO_LIST_QUEUE");
                BottomSheetOptionSong.this.dismiss();
                break;
            case R.id.btnMakeRingTone:
                break;
            case R.id.btnDeleteSong:
                break;


        }
    }
}
