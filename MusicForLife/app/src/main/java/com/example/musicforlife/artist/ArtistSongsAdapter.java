package com.example.musicforlife.artist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.musicforlife.R;

import java.util.List;

public class ArtistSongsAdapter extends BaseAdapter {
    private List<ArtistSongsModel> mylist;
    private Context myContext;

    public ArtistSongsAdapter(Context context,List<ArtistSongsModel> list){
        myContext = context;
        mylist = list;
    }
    @Override
    public int getCount() {
        return mylist.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.layout_item_artist_song,null);

        TextView TVNumber = (TextView) convertView.findViewById(R.id.artistSongNumber);
        TextView TVNameSong = (TextView) convertView.findViewById(R.id.artistSongNameSong);
        TextView TVNameArtist = (TextView) convertView.findViewById(R.id.artistSongNameArtist);
        TextView TVDuration = (TextView) convertView.findViewById(R.id.artistSongDuration);

        ArtistSongsModel artistSongsModel = mylist.get(position);

        TVNumber.setText(position + "");
        TVNameSong.setText(artistSongsModel.getNameSong());
        TVNameArtist.setText(artistSongsModel.getNameSongArtist());
        TVDuration.setText(artistSongsModel.getDuration());

        return convertView;
    }

}
