package com.example.ym;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

//экран исполнителя
public class ArtistActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);
        //кнопка назад в ActionBar
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        ImageView cover = (ImageView)findViewById(R.id.coverArtist);
        TextView genres = (TextView)findViewById(R.id.genresArtist);
        TextView albsongs = (TextView)findViewById(R.id.albumsSongsArtist);
        TextView desc = (TextView)findViewById(R.id.descriptionArtist);
        Intent intent = getIntent();
        //получаем данные из MainActivity
        Artist artist = intent.getParcelableExtra("artist");
        Typeface myFont = Typeface.createFromAsset(getAssets(),"fonts/Roboto.ttf");
        albsongs.setTypeface(myFont);
        //создание строки для отображения количества альбомов и песен
        String alb = Other.getCountAlbums(artist.albums), song = Other.getCountSongs(artist.tracks);
        String albsongsstr = artist.albums + " " + alb + "  " + Html.fromHtml("&#x22C5") + "  " + artist.tracks + " " + song;
        //создаем строку для отображения жанров
        String gen = "";
        for (int i = 0; i < artist.genres.length; i++) {
            gen += artist.genres[i];
            if (i + 1 != artist.genres.length)
                gen += ", ";
        }
        //задаем заголовок экрана
        setTitle(artist.name);
        //скачиваем изображение и вставляем в ImageView
        Picasso.with(getApplicationContext()).load(artist.cover.big).into(cover);
        genres.setText(gen);
        albsongs.setText(albsongsstr);
        desc.setText(artist.description);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //если нажимаем кнопку назад, то удаляем этот Activity из кэш памяти
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
