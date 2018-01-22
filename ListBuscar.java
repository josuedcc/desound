package cc.devjo.desound;

import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.*;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import cc.devjo.desound.adapter.SongsAdapter;
import cc.devjo.desound.models.Song;
import cc.devjo.desound.utils.ItemOffsetDecoration;
import cc.devjo.desound.utils.RecyclerItemClickListener;
import cc.devjo.desound.utils.util;

import static cc.devjo.desound.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ListBuscar extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private List<Song> mDataset = new ArrayList<>();
    public String Titulo = null;
    private int lenght = 0;
    private String idVideo = null;
    public String urlPlay = null;
    public String urlPlay2 = null;
    MediaPlayer mediaPlayer;
    private ImageButton btn_repro;
    private ImageButton btn_download;
    private int imgPause;
    private int imgPlay;
    private int imgDownTouch;
    private int imgDownDef;
    public static String downloadPath = "/DeSound";
    private SongsAdapter songsAdapter;
    URL urlActual;
    TextView txtActualSound;
    WebView webView;
    View btDownload;
    int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_buscar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        webView = (WebView) findViewById(R.id.webView);
        webView.loadUrl("http://youtubeinmp3.com/download/?video=https://www.youtube.com/watch?v=CDq6aGsSqLg");
        btDownload = findViewById(R.id.btn_download);
        mediaPlayer = new MediaPlayer();

        final RecyclerView recyclerView= (RecyclerView) findViewById(R.id.my_recycler_view_song);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        songsAdapter = new SongsAdapter(mDataset);
        recyclerView.setAdapter(songsAdapter);
        recyclerView.addItemDecoration(new ItemOffsetDecoration(this, R.integer.offset));

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        final ProgressDialog progressDialog = ProgressDialog.show(this, getString(R.string.msgProgres), getString(R.string.msgProgresContent));
        progressDialog.setCancelable(true);

        txtActualSound = (TextView) findViewById(R.id.name_song);
        Intent intent = getIntent();
        String pedido = URLEncoder.encode(intent.getStringExtra("getsearch"));
        final String url = (util.pedidoURL+util.key+"&q="+pedido);
        btn_repro = (ImageButton) findViewById(R.id.btn_play);
        imgPause = R.mipmap.ic_pause;
        imgPlay = R.mipmap.ic_play;
        imgDownDef = R.mipmap.ic_download;
        imgDownTouch = R.mipmap.ic_downloadtouch;

        Log.e("Ver", String.valueOf(mDataset));

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                progressDialog.cancel();
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String songName = jsonObject.getString("title");
                        String idVideo = jsonObject.getString("id");
                        String urlImgDef = jsonObject.getString("imgDef");
                        String urlPlay = jsonObject.getString("url");
                        String urlPlay2 = jsonObject.getString("urlsecondary");
                        Song song = new Song(songName, idVideo, urlImgDef, urlPlay, urlPlay2);
                        mDataset.add(i, song);
                    }
                    songsAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(777777,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonArrayRequest);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                idVideo = mDataset.get(position).getIdVideo().toString();
                Titulo = mDataset.get(position).getSongName().toString();
                urlPlay = mDataset.get(position).getUrlPlay().toString();
                urlPlay2 = mDataset.get(position).getUrlPlay2().toString();
                btn_repro.setImageResource(imgPlay);
                mediaPlayer.reset();
                btDownload.setVisibility(View.GONE);
                try {
                    urlActual = new URL(urlPlay);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                if (idVideo.equals("777")) {
                    try {
                        exeMediaplayer(urlActual);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        //webView.loadUrl("http://youtubeinmp3.com/download/?video=https://www.youtube.com/watch?v=" + idVideo);
                        urlActual = new URL(urlPlay2);
                        exeMediaplayerYoutube(urlActual, idVideo);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }));

        btn_repro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayerPause("DeSound");
            }
        });

        btn_download = (ImageButton) findViewById(R.id.btn_download);

        btn_download.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        btn_download.setImageResource(imgDownTouch);

                        if (!(Titulo == "") || !(urlActual.toString() == null)) {

                            isExternalStorageWritable(Titulo, urlActual);
                        } else {
                            Toast.makeText(getBaseContext(), R.string.msgError, Toast.LENGTH_LONG).show();
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        btn_download.setImageResource(imgDownDef);
                        return true;
                }
                return false;
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        private boolean validarPermisos(){
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            return true;
        }
        if (checkSelfPermission(WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            return true;
        }
        if(shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)){
            cargarDialogoPermiso();
        } else {
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE}, 100);
        }
            return false;
        }

    private void cargarDialogoPermiso() {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(ListBuscar.this);
        dialogo.setTitle("Permiso");
        dialogo.setMessage("Me Amas?");
        dialogo.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE}, 100);

            }
        });
        dialogo.show();
    }

    private void exeMediaplayer(URL urlObject) throws IOException{
        txtActualSound.setText(R.string.load);
        mediaPlayer.reset();
        mediaPlayer.setDataSource(String.valueOf(urlObject));
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.reset();
                }
                btn_repro.setImageResource(imgPause);
                String tiempo = String.format("%d:%d",
                        TimeUnit.MILLISECONDS.toMinutes(mediaPlayer.getDuration()),
                        TimeUnit.MILLISECONDS.toSeconds(mediaPlayer.getDuration())
                                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(mediaPlayer.getDuration()))
                );
                TextView txtTotal = (TextView) findViewById(R.id.txtTotal);
                txtTotal.setText(tiempo);
                txtActualSound.setText(Titulo);
                txtActualSound.startAnimation(AnimationUtils.loadAnimation(ListBuscar.this, android.R.anim.slide_in_left));
                mediaPlayer.start();
                btDownload.setVisibility(View.VISIBLE);
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                btn_repro.setImageResource(imgPlay);
            }
        });

        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Toast.makeText(getBaseContext(), R.string.msgError, Toast.LENGTH_LONG).show();
                txtActualSound.setText("DeSound");
                return true;
            }
        });

        mediaPlayer.prepareAsync();
    }

    private void exeMediaplayerYoutube(URL urlObject, final String id) throws IOException{
        txtActualSound.setText(R.string.load);
        mediaPlayer.reset();
        mediaPlayer.setDataSource(String.valueOf(urlObject));
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.reset();
                }
                btn_repro.setImageResource(imgPause);
                String tiempo = String.format("%d:%d",
                        TimeUnit.MILLISECONDS.toMinutes(mediaPlayer.getDuration()),
                        TimeUnit.MILLISECONDS.toSeconds(mediaPlayer.getDuration())
                                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(mediaPlayer.getDuration()))
                );
                TextView txtTotal = (TextView) findViewById(R.id.txtTotal);
                txtTotal.setText(tiempo);
                txtActualSound.setText(Titulo);
                txtActualSound.startAnimation(AnimationUtils.loadAnimation(ListBuscar.this, android.R.anim.slide_in_left));
                mediaPlayer.start();
                btDownload.setVisibility(View.VISIBLE);
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                btn_repro.setImageResource(imgPlay);
            }
        });

        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                webView.loadUrl("http://youtubeinmp3.com/download/?video=https://www.youtube.com/watch?v=" + id);
                Dialogo1Click(getBaseContext());
                txtActualSound.setText("DeSound");
                return true;
            }
        });

        mediaPlayer.prepareAsync();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menubuscador, menu);

        final MenuItem item = menu.findItem(R.id.action_searchbusca);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        Drawable newIcon = (Drawable) item.getIcon();
        newIcon.mutate().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
        item.setIcon(newIcon);

        final MenuItem itemsaves = menu.findItem(R.id.action_irdescargas);
        Drawable newIconsaves = (Drawable) itemsaves.getIcon();
        newIconsaves.mutate().setColorFilter(getResources().getColor(R.color.colorPrimaryDark),PorterDuff.Mode.SRC_IN);
        itemsaves.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(getApplicationContext(),MisCanciones.class);
                intent.putExtra("id","1");
                startActivity(intent);
                return true;
            }
        });


        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {

                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                songsAdapter.setFilterSongs(mDataset);
                return true;
            }
        });

        return true;
    }

    public void Dialogo1Click(Context v){
        AlertDialog.Builder ventana = new AlertDialog.Builder(this);
        ventana.setMessage(getString(R.string.msgYoutube));
        ventana.setTitle(getString(R.string.titlemsgYoutube));
        ventana.setPositiveButton(getString(R.string.btnpositive), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                count = count + 1;
                if (count == 3) {
                    count = 1;
                    try {
                        urlActual = new URL(urlPlay);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    exeMediaplayerYoutube(urlActual, idVideo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        ventana.setNegativeButton(getString(R.string.btnnegative), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                count = 1;
            }
        });
        ventana.show();
    }

    private void mediaPlayerPause(String title){
        if (title.equals(txtActualSound.getText().toString())){
            Toast.makeText(getBaseContext(), getString(R.string.msgErrorempety), Toast.LENGTH_LONG).show();
        }else  {
            if(mediaPlayer.isPlaying()){
                mediaPlayer.pause();
                lenght=mediaPlayer.getCurrentPosition();
                btn_repro.setImageResource(imgPlay);
            }else {
                mediaPlayer.seekTo(lenght);
                mediaPlayer.start();
                btn_repro.setImageResource(imgPause);
            }
        }
    }

    public void isExternalStorageWritable(String title,URL urlDownload){
            //File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)+File.separator+"desound");
            File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)+File.separator);
            File folderDeSound = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)+File.separator+"desound");
            String pathFolder = String.valueOf(folderDeSound);
            boolean success = true;

            if (folder.exists()){
                Log.e("Si", "Si existe");
                if (!folderDeSound.exists()){
                    folderDeSound.mkdir();
                }
            } else if (!folder.exists()){
                Log.e("No","no existe");
                folder.mkdir();
                if (!folderDeSound.exists()){
                    folderDeSound.mkdir();
                }
            }
            if (success){
                DownloadFile(title,urlDownload,pathFolder);
            }
    }

    public void DownloadFile(String title,URL urlDownload,String downloadPath){
        String textoprueba = "DeSound";
        if (textoprueba.equals(txtActualSound.getText().toString())){
            Toast.makeText(getBaseContext(), R.string.msgErrorempety, Toast.LENGTH_LONG).show();
        }else {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(String.valueOf(urlDownload)));
            request.setTitle(title);
            request.setDescription(getString(R.string.msgDescarga));
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            String nomFile = title+".mp3";
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC+File.separator+"desound", nomFile);
            DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        this.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private List<Song> filter(List<Song> models, String query){
        query = query.toLowerCase();
        final List<Song> filterdSongsList = new ArrayList<>();
        for(Song songs1:models){
            final String text = songs1.getSongName().toLowerCase();
            if (text.contains(query)){
                filterdSongsList.add(songs1);
            }
        }
        return filterdSongsList;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<Song> songsList = filter(mDataset,newText);
        songsAdapter.setFilterSongs(songsList);
        Log.e("Veree", String.valueOf(songsList).toString());
        return true;
    }
}
