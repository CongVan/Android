package com.example.musicforlife.album;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.musicforlife.MainActivity;
import com.example.musicforlife.R;
import com.example.musicforlife.listsong.RecyclerItemClickListener;

import java.util.ArrayList;

public class FragmentAlbum extends Fragment {
    private static final String TAG = "FRAGMENT_ALBUM";
    public static final String SENDER="FRAGMENT_ALBUM";

    View view;
    ArrayList<AlbumViewModel> arrAlbum;
    RecyclerView RCalbum;
    Context context;
    AlbumListAdapter albumListAdapter;
    static boolean mIsLoading;
    static int take = 10;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //get context activity
        context = (MainActivity)getActivity();

        //get view from infalter
        view = inflater.inflate(R.layout.fragment_album,container,false);

        //get list artist from db
        arrAlbum = AlbumProvider.getAlbumModelPaging(context,0,20);

        //get RecyclerView Album by id
        RCalbum = (RecyclerView)view.findViewById(R.id.rvAlbumList);

        //map layout with adapter
        albumListAdapter = new AlbumListAdapter(context,arrAlbum);
        RCalbum.setLayoutManager(new LinearLayoutManager(context));
        RCalbum.setAdapter(albumListAdapter);
        RCalbum.addOnItemTouchListener(new RecyclerItemClickListener(context, RCalbum, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(context,AlbumSongsActivity.class);
                AlbumModel albumModel = (AlbumModel)arrAlbum.get(position).getAlbumModel();
                intent.putExtra("infoAlbum", albumModel);
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        RCalbum.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (!mIsLoading && linearLayoutManager != null && linearLayoutManager.getItemCount() - 1 <= linearLayoutManager.findLastVisibleItemPosition()) {
                    loadMore();
                    mIsLoading = true;
                }
            }
        });
        return view;
    }
    private void loadMore() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                ArrayList<AlbumViewModel> tempAudioList = AlbumProvider.getAlbumModelPaging(context, arrAlbum.size(), take);
                arrAlbum.addAll(tempAudioList);
                albumListAdapter.notifyItemInserted(arrAlbum.size());
                mIsLoading = false;
            }
        });
    }

    public static FragmentAlbum newInstance() {
        FragmentAlbum fragmentAlbum = new FragmentAlbum();
        return fragmentAlbum;
    }

}
