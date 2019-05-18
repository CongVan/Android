package com.example.musicforlife.play;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicforlife.MainActivity;
import com.example.musicforlife.R;
import com.example.musicforlife.listsong.MultiClickAdapterListener;
import com.example.musicforlife.listsong.RecyclerItemClickListener;
import com.example.musicforlife.listsong.SongModel;
import com.example.musicforlife.playlist.FragmentPlaylist;
import com.example.musicforlife.playlist.PlaylistModel;
import com.example.musicforlife.playlist.PlaylistSongActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class FragmentListPlaying extends Fragment implements FragmentPlayInterface, MultiClickAdapterListener {

    private PlayActivity mPlayActivity;
    private Context mContext;
    private LayoutInflater mInflater;
    private static ArrayList<SongModel> mListSong;
    private LinearLayout mLayoutListSong;
    private RecyclerView mListViewSong;
    private ListPlayingAdapter mListSongAdapter;
    private LoadListPlaying loadListPlaying;
    private TextView txtSizePlayingList;
    private LinearLayout mLayoutDeleteSong;
    private Button mBtnDeleteAllSongPlaying;
    private AppCompatCheckBox mSelectedAllSongPlaying;
    private static SongModel mSongPlaying = null;

    public static final String SENDER = "FRAGMENT_PLAYING_LIST";
    private static final String TAG = "FragmentListPlaying";
    private MultiClickAdapterListener myAdapterListener;
    private ArrayList<Integer> mListSelectedSong;
    private ArrayList<Integer> mListIdSelectedSong;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            mContext = getActivity();
            mPlayActivity = (PlayActivity) getActivity();
            loadListPlaying = new LoadListPlaying();
            mListSelectedSong = new ArrayList<>();
            mListIdSelectedSong = new ArrayList<>();

        } catch (IllegalStateException e) {

        }

    }

    public static FragmentListPlaying newInstance() {
        FragmentListPlaying fragmentListPlaying = new FragmentListPlaying();
        mSongPlaying = PlayService.getCurrentSongPlaying();
        return fragmentListPlaying;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: STARTED");
        updateListPlaying();
    }

    @Nullable
    @Override()
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_playing, container, false);
        mPlayActivity.updateToolbarTitle();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListViewSong = (RecyclerView) view.findViewById(R.id.lsvPlaying);
        mLayoutDeleteSong = view.findViewById(R.id.layoutDeleteSong);
        mBtnDeleteAllSongPlaying = view.findViewById(R.id.btnDeleteAllSongPlaying);
        mSelectedAllSongPlaying = view.findViewById(R.id.selectedAllSongPlaying);
        mListSong = new ArrayList<>();
        mListSongAdapter = new ListPlayingAdapter(mContext, mListSong, FragmentListPlaying.this);
        mListViewSong.setLayoutManager(new LinearLayoutManager(mContext));
        mListViewSong.setAdapter(mListSongAdapter);
        mBtnDeleteAllSongPlaying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(PlayActivity.getActivity(), R.style.DialogPrimary)
//                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Xác nhận?")
                        .setMessage("Bạn có chắc muốn xóa  " + mListSelectedSong.size() + " bài hát khỏi danh sách phát?")
                        .setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.d(TAG, "run: LIST SONG PLAYING BEFORE DELETE : " + mListSong.size());
                                        String Ids = TextUtils.join(", ",mListIdSelectedSong);
                                        PlayModel.deleteListSongInListPlaying(Ids);
                                        Toast.makeText(PlayActivity.getActivity(), "Đã xóa " + mListIdSelectedSong.size() + " bài hát", Toast.LENGTH_LONG).show();
                                        mSelectedAllSongPlaying.setChecked(false);
                                        mListSelectedSong.clear();
                                        mListIdSelectedSong.clear();
                                        mListSong.clear();
                                        mListSong.addAll(PlayModel.getSongPlayingList());
                                        Log.d(TAG, "run: LIST SONG PLAYING AFTER DELETE : " + mListSong.size());
                                        mListViewSong.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                mListSongAdapter.notifyDataSetChanged();
                                            }
                                        });
                                        updateSongAfterDelete(Ids);
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Đóng", null)
                        .show();
            }
        });
        mSelectedAllSongPlaying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = mSelectedAllSongPlaying.isChecked();
                for(int i = 0 ; i < mListSong.size() ; i++){
                    mListSelectedSong.add(i);
                    mListIdSelectedSong.add(mListSong.get(i).getSongId());
                    mListSong.get(i).setChecked(isChecked);
                }
                if(!isChecked){
                    mListSelectedSong.clear();
                    mListIdSelectedSong.clear();
                }
                mListSongAdapter.notifyDataSetChanged();
                updateViewSelectAll();

            }

        });
        updateListPlaying();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState: LIST PLAYING " + mListSong.size());
        outState.putSerializable("playList", mListSong);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        loadListPlaying.cancel(true);
    }


    @Override
    public void updateListPlaying() {
        new LoadListPlaying().execute();
    }

    @Override
    public void refreshListPlaying() {
        mPlayActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mListSongAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void updateControlPlaying(SongModel songModel) {

    }

    @Override
    public void updateSeekbar(int currentDuration) {

    }

    @Override
    public void updateButtonPlay() {

    }

    @Override
    public void optionMenuClick(View v, int position) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void checkboxClick(View v, int position) {
        Toast.makeText(mContext, "checkboxClick " + position, Toast.LENGTH_SHORT).show();
        boolean isChecked = mListSong.get(position).isChecked();
        mListSong.get(position).setChecked(!isChecked);
        mListSongAdapter.notifyItemChanged(position);
        if (!isChecked) {
            mListSelectedSong.add(position);
            mListIdSelectedSong.add(mListSong.get(position).getSongId());
        } else {
            if (mListSelectedSong.indexOf(position) > -1) {
                mListSelectedSong.remove(mListSelectedSong.indexOf(position));
                mListIdSelectedSong.remove(mListIdSelectedSong.indexOf(position));
            }

        }
        updateViewSelectAll();
        Log.d(TAG, "checkboxClick: Position: " + position + ", Check: " + isChecked + ", Size selected: " + mListSelectedSong.size());
    }

    private void updateViewSelectAll() {
        if (mListSelectedSong.size() > 0) {
            mBtnDeleteAllSongPlaying.setVisibility(View.VISIBLE);
        } else {
            mBtnDeleteAllSongPlaying.setVisibility(View.GONE);
        }
        mSelectedAllSongPlaying.setText("Chọn tất cả (" + mListSelectedSong.size() + ")");
    }

    private boolean isShowOfViewDeleteSong() {
        return mLayoutDeleteSong.getVisibility() == View.VISIBLE
                && mBtnDeleteAllSongPlaying.getVisibility() == View.VISIBLE;
    }

    private void hideViewDeleteAllSong() {
        mBtnDeleteAllSongPlaying.setVisibility(View.VISIBLE);
    }

    private void updateSongAfterDelete(String Ids){
        hideViewDeleteAllSong();
        PlayService.updatePlayingList();
        if(mListSong.size() == 0)
        {
            PlayService.newInstance().pause();
            mPlayActivity.finish();
            MainActivity.getMainActivity().isHideMinimize = true;
            return;
        }
        if(mSongPlaying != null && Ids.contains(String.valueOf(mSongPlaying.getSongId())))
        {
            mSongPlaying = mListSong.get(0);
            PlayService.newInstance().play(mSongPlaying);
        }
        updateViewSelectAll();
    }

    @Override
    public void layoutItemClick(View v, int position) {
        Toast.makeText(mContext, "CLICK ITEM SONG" + position, Toast.LENGTH_SHORT).show();
        mSongPlaying = mListSong.get(position);
        mPlayActivity.controlSong(FragmentListPlaying.SENDER, mSongPlaying, PlayService.ACTION_PLAY);
        mPlayActivity.updateControlPlaying(SENDER, mSongPlaying);
        mListSongAdapter.notifyDataSetChanged();

    }

    @Override
    public void layoutItemLongClick(View v, int position) {

    }

    private class LoadListPlaying extends AsyncTask<Void, Integer, ArrayList<SongModel>> {

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(ArrayList<SongModel> songModels) {
            super.onPostExecute(songModels);
            if (songModels == null) {
                return;
            }
            mListSong.clear();
            mListSong.addAll(songModels);

            mListIdSelectedSong.clear();
            mListSelectedSong.clear();
            for (int i = 0 ; i < songModels.size() ; i++){
                if(songModels.get(i).isChecked()){
                    mListIdSelectedSong.add(songModels.get(i).getSongId());
                    mListSelectedSong.add(i);
                }
            }
            updateViewSelectAll();

            Log.i(TAG, "onPostExecute: SONGS--> " + mListSong.size());
            mListViewSong.post(new Runnable() {
                @Override
                public void run() {
                    mListSongAdapter.notifyDataSetChanged();
                }
            });
            Log.i(TAG, "onPostExecute: FINISHED");
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        public ArrayList<SongModel> doInBackground(Void... voids) {

            return PlayService.getListPlaying();
        }
    }

    public static ArrayList<SongModel> getPlayingList() {
        return mListSong;
    }

}
