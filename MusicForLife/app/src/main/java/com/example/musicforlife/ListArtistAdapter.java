package com.example.musicforlife;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class ListArtistAdapter extends BaseAdapter {
    private Context myContext;
    private List<ArtistModel> artistList;
    public ListArtistAdapter(Context context,List<ArtistModel> list){
        myContext = context;
        artistList = list;
    }
    @Override
    public int getCount() {
        return artistList.size();
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
        LayoutInflater inflater = (LayoutInflater)myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.layout_item_artist,null);

        TextView TVArtistName = (TextView)convertView.findViewById(R.id.txtArtistName);
        TextView TVArtistCount = (TextView)convertView.findViewById(R.id.txtArtistCount);

        TVArtistName.setText(artistList.get(position).getName());
        TVArtistCount.setText(artistList.get(position).getSongCount() + "");

        return convertView;
    }
}
