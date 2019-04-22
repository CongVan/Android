package com.example.musicforlife.album;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.MediaMetadataRetriever;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.musicforlife.R;
import com.example.musicforlife.artist.ArtistModel;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

public class AlbumListAdapter extends RecyclerView.Adapter<AlbumListAdapter.AlbumViewHolder>{
    private Context myContext;
    private List<AlbumViewModel> albumList;

    public AlbumListAdapter(Context context,List<AlbumViewModel> list)
    {
        myContext = context;
        albumList = list;
    }
    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item_album, viewGroup, false);
        AlbumViewHolder albumViewHolder = new AlbumViewHolder(view);
        return albumViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder albumViewHolder, int i) {
        albumViewHolder.BindData(albumList,i);
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public class AlbumViewHolder extends RecyclerView.ViewHolder {
        TextView TVAlbumName;
        TextView TVAlbumCount;
        TextView TVAlbumArtist;
        ImageView IVAlbum;
        public AlbumViewHolder(@NonNull View itemView) {
            super(itemView);
            TVAlbumName = (TextView)itemView.findViewById(R.id.txtAlbumName);
            TVAlbumCount = (TextView)itemView.findViewById(R.id.txtAlbumSongCount);
            TVAlbumArtist = (TextView)itemView.findViewById(R.id.txtAlbumArtist);
            IVAlbum = (ImageView)itemView.findViewById(R.id.imgAlbum);
        }
        public void BindData(List<AlbumViewModel> albumList,int position){
            AlbumViewModel albumViewModel = albumList.get(position);

            TVAlbumName.setText(albumViewModel.getTitle());
            TVAlbumCount.setText(albumViewModel.getNumberOfSongs() + " Bài hát");
            TVAlbumArtist.setText(albumViewModel.getArtist());

            if(albumViewModel.getBitmap() == null){
                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                mediaMetadataRetriever.setDataSource(albumViewModel.getPath());
                if (mediaMetadataRetriever.getEmbeddedPicture() != null) {
                    InputStream inputStream = new ByteArrayInputStream(mediaMetadataRetriever.getEmbeddedPicture());
                    mediaMetadataRetriever.release();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    IVAlbum.setImageBitmap(bitmap);
                    //set bitmap for list
                    albumList.get(position).setBitmap(bitmap);
                } else {
                    //set default if can't getEmbeddedPicture()
                    IVAlbum.setImageResource(R.mipmap.album_128);
                }
            }
            else{
                //set Image from bitmap model
                IVAlbum.setImageBitmap(albumViewModel.getBitmap());
            }
        }
    }
}
