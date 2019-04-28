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
import com.example.musicforlife.listsong.SongModel;
import com.example.musicforlife.utilitys.CacheHelper;
import com.example.musicforlife.utilitys.ImageCacheHelper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

public class ListArtistAdapter extends RecyclerView.Adapter<ListArtistAdapter.ArtistViewHolder> {
    private Context myContext;
    private List<ArtistViewModel> artistList;
    private ImageCacheHelper mImageCacheHelper;
    public ListArtistAdapter(Context context,List<ArtistViewModel> list){
        myContext = context;
        artistList = list;
        mImageCacheHelper=new ImageCacheHelper(R.mipmap.microphone_128);
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

    public class ArtistViewHolder extends RecyclerView.ViewHolder{
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

            SongModel songModel = new SongModel();
            songModel.setAlbumId(artistModel.getAlbumId());
            songModel.setPath(artistModel.getPath());
            final Bitmap bitmap = mImageCacheHelper.getBitmapCache(songModel.getAlbumId());
            if (bitmap != null) {
                this.IVArtist.setImageBitmap(bitmap);
            } else {
                mImageCacheHelper.loadAlbumArt(IVArtist,songModel);
            }
        }
    }
}
