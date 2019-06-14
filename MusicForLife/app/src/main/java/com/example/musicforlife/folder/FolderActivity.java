package com.example.musicforlife.folder;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicforlife.MainActivity;
import com.example.musicforlife.R;
import com.example.musicforlife.album.AlbumSongsActivity;
import com.example.musicforlife.db.DatabaseManager;
import com.example.musicforlife.listsong.MultiClickAdapterListener;
import com.example.musicforlife.listsong.SongModel;
import com.example.musicforlife.minimizeSong.MinimizeSongFragment;
import com.example.musicforlife.play.PlayActivity;
import com.example.musicforlife.play.PlayService;
import com.example.musicforlife.playlist.BottomSheetOptionSong;
import com.example.musicforlife.playlist.FragmentPlaylist;
import com.example.musicforlife.utilitys.Utility;

import java.util.ArrayList;
import java.util.Objects;

public class FolderActivity extends AppCompatActivity implements MultiClickAdapterListener, MinimizeSongFragment.OnFragmentInteractionListener {

    private Toolbar mToolbar;
    private CoordinatorLayout mLayoutSongFolder;
    private RecyclerView mRcvSongFolder;
    private TextView mTxtSongName;
    private TextView mTxtNumberOfSong;
    private AppBarLayout mAppbarLayoutFolder;
    private ArrayList<SongModel> mSongFolderList;
    private SongFolderAdapter mSongFolderAdapter;
    private FolderModel mCurrentFolder;
    private static PlayService mPlayService;
    private MinimizeSongFragment mMinimizeSongFragment;

    private final int thresholdLoad = 10;
    private static final String TAG = "FolderActivity";
    private boolean mIsLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mCurrentFolder = (FolderModel) bundle.getSerializable("folderModel");
        }
        Log.d(TAG, "onCreate: " + mCurrentFolder.getName());
        if (mCurrentFolder == null) {
            Toast.makeText(this, "Không tìm thấy thư mục", Toast.LENGTH_LONG).show();
            finish();
        }
        initFindViewId();
        initMimimizeSong();
    }

    @SuppressLint("SetTextI18n")
    private void initFindViewId() {
        mRcvSongFolder = findViewById(R.id.rcvFolderSong);
        mToolbar = (Toolbar) findViewById(R.id.htab_toolbar);
        mTxtSongName = findViewById(R.id.txtFolderName);
        mTxtNumberOfSong = findViewById(R.id.txtNumberOfSongFolder);
        mAppbarLayoutFolder = findViewById(R.id.htab_appbar);
        mLayoutSongFolder = findViewById(R.id.layoutContentFolderSong);
        mPlayService = PlayService.newInstance();
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Utility.setTransparentStatusBar(this);
        mLayoutSongFolder.setPadding(0, Utility.getStatusbarHeight(this), 0, 0);
//        getSupportActionBar().setTitle("Thư mục " + mCurrentFolder.getName());
        mTxtSongName.setText(mCurrentFolder.getName());
        mTxtNumberOfSong.setText(mCurrentFolder.getNumberOfSong() + " bài hát");


//        getSupportActionBar().setTitle(mCurrentFolder.getName());
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mSongFolderList = FolderModel.getSongsFromFolderName(mCurrentFolder.getName(), 0, thresholdLoad);
                mSongFolderAdapter = new SongFolderAdapter(FolderActivity.this, mSongFolderList, FolderActivity.this);
                mRcvSongFolder.setLayoutManager(new LinearLayoutManager(FolderActivity.this));
                mRcvSongFolder.setAdapter(mSongFolderAdapter);
            }
        });

        mAppbarLayoutFolder.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (Math.abs(i) - appBarLayout.getTotalScrollRange() == 0) {
                    Log.d(TAG, "onOffsetChanged: COLLPASED");
                    //Collapsed

                    getSupportActionBar().setTitle(mCurrentFolder.getName());

                } else {
                    //Expanded
//                    Log.d(TAG, "onOffsetChanged: EXPANDED");
                    getSupportActionBar().setTitle(" ");

                }
            }
        });
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do something here, such as start an Intent to the parent activity.
//                finish();
                onBackPressed();
            }
        });


        mRcvSongFolder.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//               if (linearLayoutManager != null) {
//                   Log.d(TAG, "onScrolled: " + dx + "_" + dy + "___" + linearLayoutManager.getItemCount() + "_" + linearLayoutManager.findLastVisibleItemPosition());
//               }

                if (!mIsLoading && linearLayoutManager != null && linearLayoutManager.getItemCount() - 1 == linearLayoutManager.findLastVisibleItemPosition()) {
                    loadMore();
                    mIsLoading = true;
                }
            }
        });
    }

    private void initMimimizeSong() {
        mMinimizeSongFragment = MinimizeSongFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.frgMinimizeSong, mMinimizeSongFragment).commit();

    }

    private void loadMore() {
        mSongFolderList.add(null);
        mSongFolderAdapter.notifyItemInserted(mSongFolderList.size());
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                //
                ArrayList<SongModel> tempSongs = FolderModel.getSongsFromFolderName(mCurrentFolder.getName(), mSongFolderList.size(), thresholdLoad);
                mSongFolderList.remove(mSongFolderList.size() - 1);
                mSongFolderAdapter.notifyItemRemoved(mSongFolderList.size());
                mSongFolderList.addAll(tempSongs);
                mIsLoading = false;
            }
        });
    }

    @Override
    public void optionMenuClick(View v, int position) {
        final SongModel songChose = mSongFolderList.get(position);
        showBottomSheetOptionSong(songChose);
    }

    @Override
    public void checkboxClick(View v, int position) {

    }

    @Override
    public void layoutItemClick(View v, int position) {
        final SongModel songChose = mSongFolderList.get(position);
        playSong(songChose);
    }

    private void playSong(SongModel songPlay) {
        //gọi play
        mPlayService.play(songPlay);
        new Thread(new Runnable() {
            @Override
            public void run() {
                //tạo ds phát
                mPlayService.initListPlaying(FolderModel.getAllSongsFromFolderName(mCurrentFolder.getName()));
            }
        }).start();

        MainActivity.getMainActivity().playSongsFromFragmentListToMain(FragmentPlaylist.SENDER);
    }

    @Override
    public void layoutItemLongClick(View v, int position) {
        final SongModel songChose = mSongFolderList.get(position);
        showBottomSheetOptionSong(songChose);
    }

    private void showBottomSheetOptionSong(SongModel song) {

        BottomSheetOptionSong bottomSheetDialogFragment = new BottomSheetOptionSong(song);
        bottomSheetDialogFragment.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMinimizeSongFragment.refreshControls(-1);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentRefreshNotification(int action) {
        if (MainActivity.getMainActivity() != null) {
            MainActivity.getMainActivity().refreshNotificationPlaying(action);
        }
    }

    @Override
    public void onFragmentShowPlayActivity() {
        Intent mIntentPlayActivity = new Intent(FolderActivity.this, PlayActivity.class);
        mIntentPlayActivity.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        startActivity(mIntentPlayActivity);
    }

    @Override
    public void onFragmentLoaded(final int heightLayout) {
        mRcvSongFolder.setPadding(0,0,0,heightLayout);
    }
}
