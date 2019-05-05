package com.example.musicforlife.play;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
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

import java.util.ArrayList;


public class FragmentListPlaying extends Fragment implements FragmentPlayInterface, MultiClickAdapterListener {
    private MainActivity mMainActivity;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            mContext = getActivity();
            mPlayActivity = (PlayActivity) getActivity();
            loadListPlaying = new LoadListPlaying();
            mListSelectedSong = new ArrayList<>();

        } catch (IllegalStateException e) {

        }

    }

    public static FragmentListPlaying newInstance() {
        FragmentListPlaying fragmentListPlaying = new FragmentListPlaying();
//        Bundle args = new Bundle();
//        args.putString("Key1", "OK");
//        fragmentListPlaying.setArguments(args);
        mSongPlaying = PlayService.getCurrentSongPlaying();

        return fragmentListPlaying;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: STARTED");
//        new LoadListPlaying().execute();
        updateListPlaying();

    }

    @Nullable
    @Override()
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

//        ViewGroup viewGroup= (ViewGroup)inflater.inflate(R.layout.fragment_playlist, container, false);
        View view = inflater.inflate(R.layout.fragment_list_playing, container, false);
        mPlayActivity.updateToolbarTitle();
        return view;
//        Log.i(TAG, "onCreateView PLAYLIST: OKOKOKO");
//        mListSong = new ArrayList<>();// getAllAudioFromDevice(_context);
//        mInflater = inflater;
//        mLayoutListSong = (LinearLayout) mInflater.inflate(R.layout.fragment_list_playing, container, false);
//        mListViewSong = mLayoutListSong.findViewById(R.id.lsvSongs);
//        mListSongAdapter = new ListSongAdapter(mContext, mListSong);
//        mListViewSong.setAdapter(mListSongAdapter);
//        mListViewSong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });
//        LoadListPlaying.execute();
//        return mLayoutListSong;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        txtSizePlayingList = mPlayActivity.findViewById(R.id.txtSizePlayingList);
        mListViewSong = (RecyclerView) view.findViewById(R.id.lsvPlaying);
        mLayoutDeleteSong = view.findViewById(R.id.layoutDeleteSong);
        mBtnDeleteAllSongPlaying = view.findViewById(R.id.btnDeleteAllSongPlaying);
        mSelectedAllSongPlaying = view.findViewById(R.id.selectedAllSongPlaying);
        mListSong = new ArrayList<>();// getAllAudioFromDevice(_context);

//        _layoutListSong = (NestedScrollView) _inflater.inflate(R.layout.fragment_list_song, null);

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
                                        int resultAll = 0;
                                        Log.d(TAG, "run: LIST SONG PLAYING BEFORE DELETE : " + mListSong.size());
                                        for (int songPositionSelected : mListSelectedSong) {
                                            if (songPositionSelected > mListSong.size()) {
                                                break;
                                            }

                                            int songId = mListSong.get(songPositionSelected).getSongId();
                                            long result = PlayModel.deleteSongInListPlaying(songId);
                                            if (result > 0) {
                                                resultAll++;


                                            } else {
                                                Toast.makeText(PlayActivity.getActivity(), "Thất bại", Toast.LENGTH_LONG).show();
                                            }

                                        }
                                        Toast.makeText(PlayActivity.getActivity(), "Đã xóa " + resultAll + " bài hát", Toast.LENGTH_LONG).show();
                                        mListSelectedSong.clear();
                                        mListSong.clear();
                                        mListSong.addAll(PlayModel.getSongPlayingList());
                                        Log.d(TAG, "run: LIST SONG PLAYING AFTER DELETE : " + mListSong.size());
                                        mListViewSong.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                mListSongAdapter.notifyDataSetChanged();
                                            }
                                        });

                                        hideViewDeleteAllSong();
                                        PlayService.updatePlayingList();
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
                mListSelectedSong.clear();
                if (!isChecked) {
                    for (SongModel song : mListSong) {
                        song.setChecked(false);
                    }
                } else {
                    for (SongModel song : mListSong) {
                        song.setChecked(true);
                        mListSelectedSong.add(song.getId());
                    }
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
        } else {
            if (mListSelectedSong.indexOf(position) > -1) {
                mListSelectedSong.remove(mListSelectedSong.indexOf(position));
            }

        }
        updateViewSelectAll();
//        if (mListSelectedSong.size() > 0 && !isShowOfViewDeleteSong()) {
//            mLayoutDeleteSong.setVisibility(View.VISIBLE);
//            mBtnDeleteAllSongPlaying.setVisibility(View.VISIBLE);
//        } else if (isShowOfViewDeleteSong() && mListSelectedSong.size() == 0) {
//            mLayoutDeleteSong.setVisibility(View.GONE);
//            mBtnDeleteAllSongPlaying.setVisibility(View.GONE);
//        }
//        if (mListSelectedSong.size() == mListSong.size() && !mSelectedAllSongPlaying.isChecked()) {
//            mSelectedAllSongPlaying.setChecked(true);
//        } else if (mSelectedAllSongPlaying.isChecked()) {
//            mSelectedAllSongPlaying.setChecked(false);
//        }
//        mSelectedAllSongPlaying.setText("Chọn tất cả (" + mListSelectedSong.size() + ")");
        Log.d(TAG, "checkboxClick: Position: " + position + ", Check: " + isChecked + ", Size selected: " + mListSelectedSong.size());
    }

    private void updateViewSelectAll() {
        if (mListSelectedSong.size() > 0) {
//            mLayoutDeleteSong.setVisibility(View.VISIBLE);
            mBtnDeleteAllSongPlaying.setVisibility(View.VISIBLE);
        } else {
//            mLayoutDeleteSong.setVisibility(View.GONE);
            mBtnDeleteAllSongPlaying.setVisibility(View.GONE);
        }
        mSelectedAllSongPlaying.setText("Chọn tất cả (" + mListSelectedSong.size() + ")");
    }

    private boolean isShowOfViewDeleteSong() {
        return mLayoutDeleteSong.getVisibility() == View.VISIBLE
                && mBtnDeleteAllSongPlaying.getVisibility() == View.VISIBLE;
    }

    private void hideViewDeleteAllSong() {
//        mLayoutDeleteSong.setVisibility(View.VISIBLE);
        mBtnDeleteAllSongPlaying.setVisibility(View.VISIBLE);
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
//            txtSizePlayingList.setText(" (" + mListSong.size() + ") ");
            Log.i(TAG, "onPostExecute: SONGS--> " + mListSong.size());
            mListViewSong.post(new Runnable() {
                @Override
                public void run() {
                    mListSongAdapter.notifyDataSetChanged();
                }
            });
            Log.i(TAG, "onPostExecute: FINISHED");
            //play song if songPlaying !=null
//            if (playFirst) {
//                mPlayActivity.controlSong(FragmentListPlaying.SENDER, mSongPlaying, PlayService.ACTION_PLAY);
//                mPlayActivity.updateControlPlaying(SENDER, mSongPlaying);
//            }
//
//            playFirst = false;
//            if (mSongPlaying != null && !PlayService.isPlaying()) {
//                Log.d(TAG, "onPostExecute: DURATION " + PlayService.getCurrentDuration());
//                if (PlayService.getCurrentSongPlaying() != null && PlayService.getCurrentDuration() > 0) {
//                    Log.d(TAG, "onPostExecute: DURATION " + PlayService.getCurrentDuration());
//                    Log.i(TAG, "onPostExecute: RESUME PLAY IN LIST "
//                            + mSongPlaying.getSongId() + "__"
//                            + PlayService.getCurrentSongPlaying().getSongId()
//                            + mSongPlaying + "__"
//                            + PlayService.isPlaying()
//                    );
//                } else {
//                    mPlayActivity.controlSong(FragmentListPlaying.SENDER, mSongPlaying, PlayService.ACTION_PLAY);
//                    mPlayActivity.updateControlPlaying(SENDER, mSongPlaying);
//                }
////                Log.i(TAG, "onPostExecute: RESUME PLAY IN LIST " + mSongPlaying.getSongId() + "__" + PlayService.getCurrentSongPlaying().getSongId());
//            }
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
