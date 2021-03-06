package com.example.musicforlife.playlist;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicforlife.R;

import static android.support.v4.content.ContextCompat.getSystemService;

@SuppressLint("ValidFragment")
public class FragmentDialogEditPlaylist extends DialogFragment implements View.OnClickListener {

    private EditText txtTitlePlaylist;
    private Button btnCancel;
    private Button btnSubmit;
    private PlaylistModel mCurrentPlaylist;
    private PlaylistSongActivity mPlaylistSongActivity;

    @SuppressLint("ValidFragment")
    public FragmentDialogEditPlaylist(PlaylistModel playlistModel, PlaylistSongActivity playlistSongActivity) {
        mCurrentPlaylist = playlistModel;
        mPlaylistSongActivity = playlistSongActivity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.layout_edit_playlist, null);
        txtTitlePlaylist = view.findViewById(R.id.txtTilePlaylistEdit);
        btnCancel = view.findViewById(R.id.btnCancelEditPlaylist);
        btnSubmit = view.findViewById(R.id.btnSubmitEditPlaylist);

        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        txtTitlePlaylist.setText(mCurrentPlaylist.getTitle());
        txtTitlePlaylist.requestFocus();
        txtTitlePlaylist.setSelection(txtTitlePlaylist.length());


        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view);
        // Add action buttons
//                .setPositiveButton("Tạo", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//                        // sign in the user ...
//                        Toast.makeText(getActivity().getApplicationContext(),txtTitlePlaylist.getText(),Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .setNegativeButton("Đóng", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        FragmentDialogCreatePlaylist.this.getDialog().cancel();
//                    }
//                });


        return builder.create();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancelEditPlaylist:
                FragmentDialogEditPlaylist.this.getDialog().cancel();
                break;
            case R.id.btnSubmitEditPlaylist:
                String titleEdit = txtTitlePlaylist.getText().toString();
                if (titleEdit.isEmpty()) {
                    break;
                }
                mCurrentPlaylist.setTitle(titleEdit);
                long result = PlaylistModel.updateTitlePlaylist(mCurrentPlaylist);
                if (result > 0) {
                    Toast.makeText(getActivity().getApplicationContext(), "Sửa tiêu đề thành công", Toast.LENGTH_LONG).show();
                    mPlaylistSongActivity.refreshTitlePlaylist(mCurrentPlaylist.getTitle());
                    FragmentPlaylist.refreshPlaylist();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Thất bại", Toast.LENGTH_LONG).show();
                }
                FragmentDialogEditPlaylist.this.getDialog().cancel();
                break;
            default:
                break;
        }
    }
}
