package cc.devjo.desound.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import cc.devjo.desound.R;
import cc.devjo.desound.models.Song;

/**
 * Created by Josue on 01/02/2016.
 */
public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.ViewHolder> {

    public List<Song> songs;
    private MediaPlayer mediaPlayer;

    public SongsAdapter(List<Song> songs) {
        this.songs = songs;
    }

   /* private ArrayList<Song> songs;
    private int itemLayout;

    public SongsAdapter(ArrayList<Song> songs, int itemLayout) {
        this.songs = songs;
        this.itemLayout = itemLayout;
    }*/

    @Override
    public SongsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_songs, parent, false);
        SongsAdapter.ViewHolder svh = new SongsAdapter.ViewHolder(v);
        return svh;
    }

    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Song song = songs.get(position);
        holder.nameSong.setText(song.getSongName());
        holder.idVideo.setText(song.getIdVideo());
        holder.urlPlay.setText(song.getUrlPlay());
        holder.urlPlay2.setText(song.getUrlPlay2());
        Uri uri = Uri.parse(song.getUrlImgDef());
        final Context cont = holder.urlImgDef.getContext();
        Picasso.with(cont).load(uri).into(holder.urlImgDef);
    }

    @Override
    public int getItemCount() {
        if (songs != null) {
            return songs.size();
        }
        return 0;
    }

    public void setFilter(List<Song> songModels){
        songs = new ArrayList<>();
        songs.addAll(songModels);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        public TextView nameSong;
        public ImageView urlImgDef;
        public TextView idVideo;
        public TextView urlPlay;
        public TextView urlPlay2;
        public RatingBar stars;
        Button btn_Play;
        public View thisView;
        public RelativeLayout relativeLayout;


        public ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.card_view);
            nameSong = (TextView) itemView.findViewById(R.id.name_song);
            urlImgDef = (ImageView) itemView.findViewById(R.id.avatar_song);
            idVideo = (TextView) itemView.findViewById(R.id.idVideo);
            urlPlay = (TextView) itemView.findViewById(R.id.urlPlay);
            urlPlay2 = (TextView) itemView.findViewById(R.id.urlPlay2);
            thisView = itemView;
            //artist = (TextView) itemView.findViewById(R.id.artist);
            //stars = (RatingBar) itemView.findViewById(R.id.ratingBar);
        }
    }

    public void setFilterSongs(List<Song> songsF){
        songs = new ArrayList<>();
        songs.addAll(songsF);
        notifyDataSetChanged();
    }

}
