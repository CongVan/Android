package com.example.musicforlife;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;


public class PagerMainAdapter extends FragmentStatePagerAdapter {
    private static final int NUM_PAGES = 6;
    private Fragment mFragmentListSong, mFragmentRecent, mFramentArtist, mFragmentAlbum, mFragmentFolder,mFragmentPlaylist;
    FragmentManager mFragmentManager;

    public PagerMainAdapter(FragmentManager fm) {
        super(fm);
        mFragmentManager = fm;
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = null;
        switch (i) {
            case 0:

                fragment = mFragmentRecent == null ? mFragmentRecent = new FragmentRecent() : mFragmentRecent;
                break;
            case 1:

                fragment = mFragmentListSong == null ? mFragmentListSong = FragmentListSong.newInstance() : mFragmentListSong;
                break;
            case 2:

                fragment = mFramentArtist == null ? mFramentArtist = new FragmentArtist() : mFramentArtist;
                break;
            case 3:
                fragment = mFragmentAlbum == null ? mFragmentAlbum = new FragmentAlbum() : mFragmentAlbum;

                break;
            case 4:
                fragment = mFragmentFolder == null ? mFragmentFolder = new FragmentFolder() : mFragmentFolder;
                break;
            case 5:
                fragment = mFragmentPlaylist == null ? mFragmentPlaylist = new FragmentPlaylist() : mFragmentPlaylist;
                break;
            default:
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }
//        private void loadFragment(Fragment fragment, String tag) {
//        //load fragment
//        FragmentTransaction transaction = mFragmentManager.beginTransaction();
////        fragmentListSong = FragmentListSong.newInstance();
//        transaction.add(R.id.frame_container, fragment, tag);
//        transaction.addToBackStack(tag);
//        transaction.commit();
//
//    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case 0:

                title = "Gần đây";
                break;
            case 1:

                title = "Bài hát";
                break;
            case 2:
                title = "Ca sĩ";

                break;
            case 3:
                title = "Album";


                break;
            case 4:
                title = "Thư mục";

                break;
            case 5:
                title = "Playlist";

                break;
            default:
                break;
        }
        return title;
    }
}
