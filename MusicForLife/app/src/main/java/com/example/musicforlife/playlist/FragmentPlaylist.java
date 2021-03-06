package com.example.musicforlife.playlist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.musicforlife.MainActivity;
import com.example.musicforlife.R;

import com.example.musicforlife.listsong.RecyclerItemClickListener;
import com.example.musicforlife.listsong.SongModel;
import com.example.musicforlife.play.PlayActivity;

import java.util.ArrayList;

public class FragmentPlaylist extends Fragment {
    private static final String TAG = "FRAGMENT_PLAY_LIST";
    public static final String SENDER = "FRAGMENT_PLAY_LIST";
    private Context mContext;
    private MainActivity mMainActivity;
    private static RecyclerView mRecyclerViewPlaylist;
    private static PlaylistAdapter mPlaylistAdapter;
    private FloatingActionButton mButtonCreatePlaylist;
    private SwipeRefreshLayout mSwpPlaylist;

    private final int mThreshold = 10;
    private static boolean mIsLoading;
    private static ArrayList<PlaylistModel> mPlaylist;

    static String searchValue = "";


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
        mRecyclerViewPlaylist = viewGroup.findViewById(R.id.rcvPlaylist);
        mButtonCreatePlaylist = viewGroup.findViewById(R.id.btnCreatePlaylist);
        mSwpPlaylist = viewGroup.findViewById(R.id.swpPlaylist);
        new Thread(new Runnable() {
            @Override
            public void run() {
                mPlaylist = PlaylistModel.getAllPlaylist(searchValue);
                mPlaylistAdapter = new PlaylistAdapter(mContext, mPlaylist);
                mRecyclerViewPlaylist.setLayoutManager(new LinearLayoutManager(mContext));
                mRecyclerViewPlaylist.setAdapter(mPlaylistAdapter);
            }
        }).start();
        mRecyclerViewPlaylist.addOnItemTouchListener(new RecyclerItemClickListener(mContext, mRecyclerViewPlaylist, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // do whatever
//                Toast.makeText(mContext, "Playlist", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onItemClick: PLAYLIST " + position);
                showPlaylistSongActivity(mPlaylist.get(position).getId());
            }

            @Override
            public void onLongItemClick(View view, int position) {
                // do whatever
//                Toast.makeText(mContext, "LONG CLICK ITEM SONG" + position, Toast.LENGTH_SHORT).show();
            }
        }));
        mButtonCreatePlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogCreatePlaylist = new FragmentDialogCreatePlaylist();
                dialogCreatePlaylist.show(mMainActivity.getSupportFragmentManager(), "CreatePlaylist");
            }
        });

        mSwpPlaylist.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPlaylist.clear();
                mPlaylistAdapter.notifyDataSetChanged();
                ArrayList<PlaylistModel> temp = PlaylistModel.getAllPlaylist(searchValue);
                Log.d(TAG, "onRefresh: "+temp.size());
                if (temp.size()>0){
                    Log.d(TAG, "onRefresh: "+temp.get(temp.size()-1).getNumberOfSongs());
                }
                mPlaylist.addAll(temp);
                mPlaylistAdapter.notifyDataSetChanged();
                mSwpPlaylist.setRefreshing(false);
            }
        });
        return viewGroup;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


//        refreshPlaylist();
    }

    private void showPlaylistSongActivity(int playlistId) {
        Intent intent = new Intent(MainActivity.getMainActivity(), PlaylistSongActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Bundle bundle = new Bundle();
        bundle.putInt("playlistId", playlistId);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    private void loadMore() {
//        _skeletonScreen = Skeleton.bind(_listViewSong).adapter(_listSongAdapter).load(R.layout.layout_item_song).show();
        mPlaylist.add(null);
        mPlaylistAdapter.notifyItemInserted(mPlaylist.size() - 1);
//        Handler handler = new Handler();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mPlaylist.remove(mPlaylist.size() - 1);
                int scollPosition = mPlaylist.size();
                mPlaylistAdapter.notifyItemRemoved(scollPosition);
                ArrayList<PlaylistModel> playlistModels = PlaylistModel.getAllPlaylist(mPlaylist.size(), mThreshold);
                mPlaylist.addAll(playlistModels);
//                mPlaylistAdapter.notifyDataSetChanged();
                mIsLoading = false;
            }
        });
//
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public static void refreshPlaylist() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mPlaylist.clear();
                mPlaylistAdapter.notifyDataSetChanged();
                ArrayList<PlaylistModel> playlistModels = PlaylistModel.getAllPlaylist(searchValue);
                if (playlistModels.size() > 0) {
                    Log.d(TAG, "run: REFRESH PLAYLIST SIZE " + playlistModels.get(playlistModels.size() - 1).getNumberOfSongs());
                }
                mPlaylist.addAll(playlistModels);
//                Log.d(TAG, "run: SIZE PLAYLIST 1 - " + mPlaylist.get(mPlaylist.size() - 1).getNumberOfSongs());
//                mPlaylistAdapter.notifyDataSetChanged();
//                mPlaylistAdapter.notifyDataSetChanged();
//                mPlaylistAdapter.notifyAll();
                mPlaylistAdapter.notifyDataSetChanged();
            }
        });
    }
    public void UpdateSearch(String s){
        if(s == searchValue) return;
        searchValue = s;
        mIsLoading = true;
        ArrayList<PlaylistModel> tempPlayList = PlaylistModel.getAllPlaylist(searchValue);
        mPlaylist.clear();
        mPlaylist.addAll(tempPlayList);
        mPlaylistAdapter.notifyDataSetChanged();
        mIsLoading = false;
    }
}
