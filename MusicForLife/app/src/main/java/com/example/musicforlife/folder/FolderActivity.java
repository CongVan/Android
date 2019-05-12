package com.example.musicforlife.folder;

import android.annotation.SuppressLint;
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

import com.example.musicforlife.R;
import com.example.musicforlife.listsong.MultiClickAdapterListener;
import com.example.musicforlife.listsong.SongModel;
import com.example.musicforlife.play.PlayService;
import com.example.musicforlife.utilitys.Utility;

import java.util.ArrayList;
import java.util.Objects;

public class FolderActivity extends AppCompatActivity implements MultiClickAdapterListener {

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
    }

    @SuppressLint("SetTextI18n")
    private void initFindViewId() {
        mRcvSongFolder = findViewById(R.id.rcvFolderSong);
        mToolbar = (Toolbar) findViewById(R.id.htab_toolbar);
        mTxtSongName = findViewById(R.id.txtFolderName);
        mTxtNumberOfSong = findViewById(R.id.txtNumberOfSongFolder);
        mAppbarLayoutFolder = findViewById(R.id.htab_appbar);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Utility.setTranslucentStatusBar(this);
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

                if (!mIsLoading && linearLayoutManager != null && linearLayoutManager.getItemCount() - 1 <= linearLayoutManager.findLastVisibleItemPosition()) {
                    loadMore();
                    mIsLoading = true;
                }
            }
        });
    }

    private void loadMore() {
        mSongFolderList.add(null);
        mSongFolderAdapter.notifyItemInserted(mSongFolderList.size());
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mSongFolderList.remove(mSongFolderList.size() - 1);
                mSongFolderAdapter.notifyItemRemoved(mSongFolderList.size());
                ArrayList<SongModel> tempSongs = FolderModel.getSongsFromFolderName(mCurrentFolder.getName(), mSongFolderList.size(), thresholdLoad);
                mSongFolderList.addAll(tempSongs);
                mIsLoading = false;
            }
        });
    }

    @Override
    public void optionMenuClick(View v, int position) {

    }

    @Override
    public void checkboxClick(View v, int position) {

    }

    @Override
    public void layoutItemClick(View v, int position) {

    }

    @Override
    public void layoutItemLongClick(View v, int position) {

    }
}
