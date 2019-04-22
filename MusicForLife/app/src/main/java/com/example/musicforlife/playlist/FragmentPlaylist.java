package com.example.musicforlife.playlist;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.musicforlife.MainActivity;
import com.example.musicforlife.R;

import com.example.musicforlife.listsong.RecyclerItemClickListener;

import java.util.ArrayList;

public class FragmentPlaylist extends Fragment {
    private static final String TAG = "FRAGMENT_PLAY_LIST";
    public static final String SENDER = "FRAGMENT_PLAY_LIST";
    private Context mContext;
    private MainActivity mMainActivity;
    private static RecyclerView mRecyclerViewPlaylist;
    private static PlaylistAdapter mPlaylistAdapter;
    private FloatingActionButton mButtonCreatePlaylist;

    private static ArrayList<PlaylistModel> mPlaylist;

    public FragmentPlaylist() {

    }

    public static FragmentPlaylist newInstance() {
        FragmentPlaylist fragmentPlaylist = new FragmentPlaylist();
//        Bundle args = new Bundle();
//        args.putString("Key1", "OK");
//        fragmentListSong.setArguments(args);
        return fragmentPlaylist;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mContext = getActivity();
            mMainActivity = (MainActivity) getActivity();
        } catch (IllegalStateException e) {

        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_playlist, container, false);
        return viewGroup;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPlaylist = PlaylistModel.getAllPlaylist();
        mRecyclerViewPlaylist = view.findViewById(R.id.rcvPlaylist);
        mButtonCreatePlaylist = view.findViewById(R.id.btnCreatePlaylist);

        mPlaylistAdapter = new PlaylistAdapter(mContext, mPlaylist);
        mRecyclerViewPlaylist.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerViewPlaylist.setAdapter(mPlaylistAdapter);
        mRecyclerViewPlaylist.addOnItemTouchListener(new RecyclerItemClickListener(mContext, mRecyclerViewPlaylist, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // do whatever
                Toast.makeText(mContext, "Playlist", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongItemClick(View view, int position) {
                // do whatever
                Toast.makeText(mContext, "LONG CLICK ITEM SONG" + position, Toast.LENGTH_SHORT).show();
            }
        }));
        mButtonCreatePlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogCreatePlaylist = new FragmentDialogCreatePlaylist();
                dialogCreatePlaylist.show(mMainActivity.getSupportFragmentManager(), "CreatePlaylist");
            }
        });
    }

    public static void refreshPlaylist() {
        mPlaylist = PlaylistModel.getAllPlaylist();
        mRecyclerViewPlaylist.post(new Runnable() {
            @Override
            public void run() {
                mPlaylistAdapter.notifyDataSetChanged();
            }
        });

    }

}
