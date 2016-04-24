//Проект создан Правда Романом
package com.example.ym;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//главный экран
public class ArtistsActivity extends AppCompatActivity {

    ArrayList<Artist> artistArray = new ArrayList<>(); //ArrayList, неодхлдимый для хранения результатов десериализации json
    ArtistAdapter adapter; //Адаптер, необходимый для того, чтобы передать ArrayList в ListView
    ListView lv;
    LinearLayout layout;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artists);
        //создаем заголовок экрана
        setTitle("Исполнители");
        lv = (ListView) findViewById(R.id.listArtists);
        layout = (LinearLayout) findViewById(R.id.linear);
        //флаг, показывающий, есть файл или нет
        boolean exists = fileExists(getExternalFilesDir(null).toString() + "/json.json");
        //флаг, показывающий, есть соединение с интернетом
        boolean connection = Other.hasConnection(getApplicationContext());
        //проверяем, есть ли соединение
        if (connection) {
            //если есть
            //проверяем существование файла
            if (!exists)
                //если он не существует, то скачиваем его
                downloadFile("json.json");
            //создаем путь до файла с данными
            File oldFile = new File(getExternalFilesDir(null), "json.json");
            //скачиваем файл с сервера
            downloadFile("json_new.json");
            //создаем путь до скачанного файла
            File newFile = new File(getExternalFilesDir(null), "json_new.json");
            //созадем md5-хэш для файла с данными
            String hash = MD5.calculateMD5(oldFile);
            //сравниваем два файла по md5-хэшу
            boolean equals = MD5.checkMD5(hash, newFile);
            if (!equals)
                //если файлы не совпадают, то скачиваем и заменяем файл с данными
                downloadFile("json.json");
            //инициализируем переводчик из json в объекты java
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            //пытаемся считать данные из файла
            try {
                //открываем файл в поток
                FileInputStream stream = new FileInputStream(oldFile);
                InputStreamReader reader = new InputStreamReader(stream);
                //инициализируем парсер
                JsonParser parser = new JsonParser();
                //парсим поток в json-массив
                JsonArray jsonArray = parser.parse(reader).getAsJsonArray();
                //переводим элементы из json-массива в ArrayList
                for (JsonElement jsonElement : jsonArray) {
                    Artist artist = gson.fromJson(jsonElement, Artist.class);
                    artistArray.add(artist);
                }
                //добавляем ArrayList в адаптер и ListView
                adapter = new ArtistAdapter(this, artistArray);
                lv.setAdapter(adapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //по нажатию на элемент списка, переходим на экран исполнителя с объектом с данными исполнителя
                        Intent intent = new Intent(ArtistsActivity.this, ArtistActivity.class);
                        intent.putExtra("artist", artistArray.get(position));
                        startActivity(intent);
                    }
                });
            } catch (FileNotFoundException e) {
                //если файл не найден, то выполняем все онлайн
                online();
            }
        } else {
            //если соединения нет, проверяем существование файла
            if (exists) {
                //если есть
                //создаем путь до файла с данными
                File oldFile = new File(getExternalFilesDir(null), "json.json");
                //инициализируем переводчик из json в объекты java
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                //пытаемся считать данные из файла
                try {
                    //открываем файл в поток
                    FileInputStream stream = new FileInputStream(oldFile);
                    InputStreamReader reader = new InputStreamReader(stream);
                    //инициализируем парсер
                    JsonParser parser = new JsonParser();
                    //парсим поток в json-массив
                    JsonArray jsonArray = parser.parse(reader).getAsJsonArray();
                    //переводим элементы из json-массива в ArrayList
                    for (JsonElement jsonElement : jsonArray) {
                        Artist artist = gson.fromJson(jsonElement, Artist.class);
                        artistArray.add(artist);
                    }
                    //добавляем ArrayList в адаптер и ListView
                    adapter = new ArtistAdapter(this, artistArray);
                    lv.setAdapter(adapter);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //по нажатию на элемент списка, переходим на экран исполнителя с объектом с данными исполнителя
                            Intent intent = new Intent(ArtistsActivity.this, ArtistActivity.class);
                            intent.putExtra("artist", artistArray.get(position));
                            startActivity(intent);
                        }
                    });
                } catch (FileNotFoundException e) {
                    //если не смогли считать, то выводим "Проверьте соединение с интернетом"
                    text = new TextView(this);
                    text.setText("Проверьте соединение с интернетом");
                    layout.addView(text);
                }
            } else {
                //иначе выводим "Проверьте соединение с интернетом"
                text = new TextView(this);
                text.setText("Проверьте соединение с интернетом");
                layout.addView(text);
            }
        }
    }

    //скачивание файла
    public void downloadFile(final String fileName) {
        //создаем интерфейс для подключения
        MyInterface api = MyInterface.retrofit.create(MyInterface.class);
        api.download("http://download.cdn.yandex.net/mobilization-2016/artists.json").enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //если удалось подключиться, то пытаемся скачать файл
                try {
                    File path = getExternalFilesDir(null);
                    File file = new File(path, fileName);
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    IOUtils.write(response.body().bytes(), fileOutputStream);
                } catch (Exception e) {
                    //если не удалось скачать файл, то делаем Toast
                    Toast.makeText(ArtistsActivity.this, "Не удалось создать файл", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //если не удалось подключиться, то делаем Toast
                Toast.makeText(ArtistsActivity.this, "Не удалось скачать файл", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //проверка существования файла
    public boolean fileExists(String path) {
        try {
            FileInputStream ifStream = new FileInputStream(path);
            ifStream.close();
            //если удалось открыть файл, то возвращаем true
            return true;
        } catch (FileNotFoundException e) {
            //если не нашли фал, то возвращаем false
            return false;
        } catch (IOException e) {
            return true;
        }
    }

    //работа online
    public void online() {
        //создаем интерфейс для подключения
        OnlineInterface service = OnlineInterface.retrofit.create(OnlineInterface.class);
        service.onlineReport().enqueue(new Callback<ArrayList<Artist>>() {
            @Override
            public void onResponse(Call<ArrayList<Artist>> call, Response<ArrayList<Artist>> response) {
                try {
                    //если удалось подключиться, то получаем данные
                    artistArray = response.body();
                    //добавляем ArrayList в адаптер и ListView
                    adapter = new ArtistAdapter(ArtistsActivity.this, artistArray);
                    lv.setAdapter(adapter);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //по нажатию на элемент списка, переходим на экран исполнителя с объектом с данными исполнителя
                            Intent intent = new Intent(ArtistsActivity.this, ArtistActivity.class);
                            intent.putExtra("artist", artistArray.get(position));
                            startActivity(intent);
                        }
                    });
                } catch (Exception e) {
                    //если не смогли считать, то выводим "Что-то пошло не так:("
                    text = new TextView(ArtistsActivity.this);
                    text.setText("Проверьте соединение с интернетом");
                    layout.addView(text);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Artist>> call, Throwable t) {
                //если не смогли считать, то выводим "Проверьте соединение с интернетом"
                text = new TextView(ArtistsActivity.this);
                text.setText("Проверьте соединение с интернетом");
                layout.addView(text);
            }
        });
    }
}