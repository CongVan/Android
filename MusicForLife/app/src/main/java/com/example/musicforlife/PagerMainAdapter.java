package com.example.musicforlife;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.example.musicforlife.artist.FragmentArtist;
import com.example.musicforlife.album.FragmentAlbum;
import com.example.musicforlife.folder.FragmentFolder;
import com.example.musicforlife.listsong.FragmentListSong;
import com.example.musicforlife.playlist.FragmentPlaylist;
import com.example.musicforlife.recent.FragmentRecent;


public class PagerMainAdapter extends FragmentStatePagerAdapter {
    private static final int NUM_PAGES = 6;
    private Fragment mFragmentListSong, mFragmentRecent, mFragmentArtist, mFragmentAlbum, mFragmentFolder, mFragmentPlaylist;
    FragmentManager mFragmentManager;
//    private int[] iconsId = {R.id.navigation_recent, R.id.navigation_song, R.id.navigation_album, R.id.navigation_artist, R.id.navigation_album, R.id.navigation_folder};

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
                fragment = mFragmentPlaylist == null ? mFragmentPlaylist = FragmentPlaylist.newInstance() : mFragmentPlaylist;
                break;
            case 3:

                fragment = mFragmentArtist == null ? mFragmentArtist = FragmentArtist.newInstance() : mFragmentArtist;
                break;
            case 4:
                fragment = mFragmentAlbum == null ? mFragmentAlbum = FragmentAlbum.newInstance() : mFragmentAlbum;

                break;
            case 5:
                fragment = mFragmentFolder == null ? mFragmentFolder = new FragmentFolder() : mFragmentFolder;
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
        return null;
//        String title = "";
//        Drawable image = ContextCompat.getDrawable(MainActivity.getMainActivity(), R.mipmap.tab_recent);
//        image.setBounds(0, 0, 48, 48);
//        SpannableString sb = new SpannableString(" ");
//        ImageSpan imageSpan = new ImageSpan(image);
//        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        return sb;
//        switch (position) {
//            case 0:
//                title = "Gần đây";
//                break;
//            case 1:
//                title = "Bài hát";
//                break;
//            case 2:
//                title = "Playlist";
//                break;
//            case 3:
//                title = "Ca sĩ";
//                break;
//
//            case 4:
//                title = "Album";
//                break;
//            case 5:
//                title = "Thư mục";
//                break;
//
//            default:
//                break;
//        }
//        return " ";
    }


    public Fragment getFragmentAtIndex(int index) {
        Fragment fragment = null;
        switch (index) {
            case 0:
                fragment = mFragmentRecent;
                break;
            case 1:

                fragment = mFragmentListSong;
                break;
            case 2:
                fragment = mFragmentPlaylist;
                break;
            case 3:

                fragment = mFragmentArtist;
                break;
            case 4:
                fragment = mFragmentAlbum;

                break;
            case 5:
                fragment = mFragmentFolder;
                break;

            default:
                break;
        }
        return fragment;
    }
}
