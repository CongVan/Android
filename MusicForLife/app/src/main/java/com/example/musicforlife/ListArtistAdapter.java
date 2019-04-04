package com.example.musicforlife;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class ListArtistAdapter extends RecyclerView.Adapter<ListArtistAdapter.ArtistViewHolder> {
    private Context myContext;
    private List<ArtistModel> artistList;
    public ListArtistAdapter(Context context,List<ArtistModel> list){
        myContext = context;
        artistList = list;
    }

    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item_artist, viewGroup, false);
        ArtistViewHolder artistViewHolder = new ArtistViewHolder(view);
        return artistViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistViewHolder artistViewHolder, int i) {
        ArtistModel _aArtistModel = artistList.get(i);
        artistViewHolder.BindData(_aArtistModel);
    }
    @Override
    public int getItemCount() {
        return artistList.size();
    }

    public static class ArtistViewHolder extends RecyclerView.ViewHolder{
        TextView TVArtistName;
        TextView TVArtistCount;
        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);
            TVArtistName = (TextView)itemView.findViewById(R.id.txtArtistName);
            TVArtistCount = (TextView)itemView.findViewById(R.id.txtArtistCount);
        }
        public void BindData(ArtistModel artistModel){
            TVArtistName.setText(artistModel.getName());
            TVArtistCount.setText(artistModel.getSongCount() + " Bài hát");
        }
    }
}
