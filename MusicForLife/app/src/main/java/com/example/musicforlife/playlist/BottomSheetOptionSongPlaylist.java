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
import android.widget.Toast;

import com.example.musicforlife.R;
import com.example.musicforlife.listsong.SongModel;
import com.example.musicforlife.play.PlayService;
import com.example.musicforlife.utilitys.ImageHelper;


@SuppressLint("ValidFragment")
public class BottomSheetOptionSongPlaylist extends BottomSheetDialogFragment implements View.OnClickListener {

    private SongModel mCurrentSong;
    private PlaylistModel mCurrentPlaylist;
    private TextView mTxtTitleSong;
    private TextView mTxtArtist;
    private TableRow mTbrAddQueue;
    private TableRow mTbrAddPlaylist;
    private TableRow mTbrMakeRingTone;
    private TableRow mTbrDeleteSong;
    private TableRow mTbrDeleteSongInPlaylist;
    private ImageView mImgSong;
    private ImageHelper mImageHelper;

    @SuppressLint("ValidFragment")
    public BottomSheetOptionSongPlaylist(SongModel songOption, PlaylistModel playlistOption) {
        mCurrentSong = songOption;
        mCurrentPlaylist = playlistOption;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.bottom_sheet_option_song_playlist, null);
        mImageHelper = new ImageHelper();

        mTxtTitleSong = contentView.findViewById(R.id.txtTitleSong);
        mTxtArtist = contentView.findViewById(R.id.txtArtist);
        mTbrAddQueue = contentView.findViewById(R.id.btnAddSongToQueue);
        mTbrAddPlaylist = contentView.findViewById(R.id.btnAddSongToPlaylist);
        mTbrMakeRingTone = contentView.findViewById(R.id.btnMakeRingTone);
        mTbrDeleteSong = contentView.findViewById(R.id.btnDeleteSong);
        mTbrDeleteSongInPlaylist = contentView.findViewById(R.id.btnDeleteSongInPlaylist);
        mImgSong = contentView.findViewById(R.id.imgSong);


        mTbrAddQueue.setOnClickListener(this);
        mTbrAddPlaylist.setOnClickListener(this);
        mTbrMakeRingTone.setOnClickListener(this);
        mTbrDeleteSong.setOnClickListener(this);
        mTbrDeleteSongInPlaylist.setOnClickListener(this);

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
                long resultAddSong = PlayService.addSongToPlayingList(mCurrentSong);
                if (resultAddSong > 0) {
                    Toast.makeText(getActivity(), "Đã thêm vào danh sách phát", Toast.LENGTH_LONG).show();
                } else if (resultAddSong == 0) {
                    Toast.makeText(getActivity(), "Bài hát đã tồn tại", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Thất bại", Toast.LENGTH_LONG).show();
                }
                BottomSheetOptionSongPlaylist.this.dismiss();
                break;
            case R.id.btnAddSongToPlaylist:
                FragmentDialogPlaylist fragmentDialogPlaylist = new FragmentDialogPlaylist(mCurrentSong);
                fragmentDialogPlaylist.show(getActivity().getSupportFragmentManager(), "ADD_SONG_TO_LIST_QUEUE");
                BottomSheetOptionSongPlaylist.this.dismiss();
                break;
            case R.id.btnMakeRingTone:
                break;
            case R.id.btnDeleteSong:
                break;
            case R.id.btnDeleteSongInPlaylist:

                break;

        }
    }
}
