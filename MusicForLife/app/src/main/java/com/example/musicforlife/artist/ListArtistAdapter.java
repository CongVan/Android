package com.example.musicforlife.artist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.musicforlife.R;
import com.example.musicforlife.utilitys.CacheHelper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

public class ListArtistAdapter extends RecyclerView.Adapter<ListArtistAdapter.ArtistViewHolder> {
    private Context myContext;
    private List<ArtistViewModel> artistList;
    public ListArtistAdapter(Context context,List<ArtistViewModel> list){
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
        artistViewHolder.BindData(artistList,i);
    }
    @Override
    public int getItemCount() {
        return artistList.size();
    }

    public static class ArtistViewHolder extends RecyclerView.ViewHolder{
        TextView TVArtistName;
        TextView TVArtistCount;
        ImageView IVArtist;
        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);
            TVArtistName = (TextView)itemView.findViewById(R.id.txtArtistName);
            TVArtistCount = (TextView)itemView.findViewById(R.id.txtArtistCount);
            IVArtist = (ImageView)itemView.findViewById(R.id.imgartist);
        }
        public void BindData(List<ArtistViewModel> artistList,int position){
            ArtistViewModel artistModel = artistList.get(position);

            TVArtistName.setText(artistModel.getName());
            TVArtistCount.setText(artistModel.getSongCount() + " Bài hát");

            //new code
//            String path = artistList.get(position).getPath();
//            Bitmap bitmap = CacheHelper.Instance().getBitmapFromMemCache(path);
//            if (bitmap != null){
//                IVArtist.setImageBitmap(bitmap);
//            }
//            else{
//                CacheHelper.Instance().addBitmapToMemoryCache(path);
//                IVArtist.setImageResource(R.mipmap.musical_note_light_64);
//            }
            //new code
            //get Image from path if exists getBitmap()
            if(artistModel.getBitmap() == null){
                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                mediaMetadataRetriever.setDataSource(artistList.get(position).getPath());
                if (mediaMetadataRetriever.getEmbeddedPicture() != null) {
                    InputStream inputStream = new ByteArrayInputStream(mediaMetadataRetriever.getEmbeddedPicture());
                    mediaMetadataRetriever.release();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    IVArtist.setImageBitmap(bitmap);
                    //set bitmap for list
                    artistList.get(position).setBitmap(bitmap);
                } else {
                    //set default if can't getEmbeddedPicture()
                    IVArtist.setImageResource(R.mipmap.musical_note_light_64);
                }
            }
            else{
                //set Image from bitmap model
                IVArtist.setImageBitmap(artistModel.getBitmap());
            }
        }
    }
}
