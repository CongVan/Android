package com.example.musicforlife;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListSongAdapter extends BaseAdapter {
    private ArrayList<SongModel> _listSong;
    private Context _context;

    public ListSongAdapter(Context context, ArrayList<SongModel> listSong) {
        this._context = context;
        this._listSong = listSong;
    }

    @Override
    public int getCount() {
        return _listSong.size();
    }

    @Override
    public Object getItem(int position) {
        return _listSong.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        TextView titleSong;
        TextView album;
        TextView artist;
        LayoutInflater layout=(LayoutInflater)_context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {

            convertView = layout.inflate(R.layout.layout_item_song, parent, false);
            titleSong = convertView.findViewById(R.id.txtTitle);
            album=convertView.findViewById(R.id.txtAlbum);
            artist=convertView.findViewById(R.id.txtArtist);
            viewHolder = new ViewHolder(titleSong,album,artist);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            titleSong= viewHolder.titleSong;
            album= viewHolder.album;
            artist=viewHolder.artist;
        }
        SongModel SongModel=_listSong.get(position);
        titleSong.setText(SongModel.getTitle());
        album.setText(SongModel.getAlbum());
        artist.setText(SongModel.getArtist());
        return convertView;
    }
    private class ViewHolder {
        TextView titleSong;
        TextView album;
        TextView artist;
        public  ViewHolder(TextView titleSong,TextView album,TextView artist){
            this.titleSong=titleSong;
            this.album=album;
            this.artist=artist;
        }
    }
}
