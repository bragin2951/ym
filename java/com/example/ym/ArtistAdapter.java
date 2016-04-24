package com.example.ym;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

//ArrayAdapter для ListView
public class ArtistAdapter extends ArrayAdapter<Artist> {
    private final Activity context;
    private final ArrayList<Artist> artist;

    //вызов адаптера
    public ArtistAdapter(Activity context, ArrayList<Artist> artists) {
        super(context, R.layout.list_item, artists);
        this.context = context;
        this.artist = artists;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        ImageView imageCover = (ImageView) convertView.findViewById(R.id.cover);
        TextView textName = (TextView) convertView.findViewById(R.id.name);
        TextView textGenres = (TextView) convertView.findViewById(R.id.genres);
        TextView textAlbumSongs = (TextView) convertView.findViewById(R.id.albumsSongs);
        //скачиваем изображение и вставляем в ImageView
        Picasso.with(context).load(artist.get(position).cover.small).fit().into(imageCover);
        textName.setText(artist.get(position).name);
        //создаем строку для отображения жанров
        String gen = "";
        for (int i = 0; i < artist.get(position).genres.length; i++) {
            gen += artist.get(position).genres[i];
            if (i + 1 != artist.get(position).genres.length)
                gen += ", ";
        }
        textGenres.setText(gen);
        //создание строки для отображения количества альбомов и песен
        String alb = Other.getCountAlbums(artist.get(position).albums), song = Other.getCountSongs(artist.get(position).tracks);
        String albson = artist.get(position).albums + " " + alb + ", " + artist.get(position).tracks + " " + song;
        textAlbumSongs.setText(albson);
        return convertView;
    }
}
