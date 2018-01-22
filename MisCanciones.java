package cc.devjo.desound;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cc.devjo.desound.R;
import cc.devjo.desound.adapter.MisCancionesAdapter;
import cc.devjo.desound.models.Canciones;

public class MisCanciones extends AppCompatActivity implements android.support.v7.widget.SearchView.OnQueryTextListener {

    private RecyclerView mMiscancionesList;
    private MisCancionesAdapter misCancionesAdapter;
    private List<File> miscanciones = new ArrayList<>();
    private ArrayList<Canciones> canciones;
    public String Titulo = null;
    public String Direccion = null;
    private ImageButton imageButtonSend;
    public String idstatic;
    public Boolean mostrarmenu;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_canciones);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAdView = (AdView) findViewById(R.id.ad_viewmy);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("B4229E0CEE99B7CDB8394A5989091BEC")
                .build();
        mAdView.loadAd(adRequest);

        mMiscancionesList = (RecyclerView) findViewById(R.id.mis_canciones);

        misCancionesAdapter = new MisCancionesAdapter(this);
        Intent intent = getIntent();
        idstatic = intent.getStringExtra("id");

        if (idstatic.equals("0")){
            mostrarmenu = false;
        }else {
            mostrarmenu = true;
        }

        File foldermissongs = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)+File.separator+"desound");
        if (foldermissongs.exists()){
            miscanciones = findsongs(foldermissongs);

            if (!miscanciones.isEmpty()){
                setupMisCancionesList();
            }else {
                Toast.makeText(getApplicationContext(),"You do not have downloads",Toast.LENGTH_SHORT).show();
                this.finish();
            }
        }else {
            Toast.makeText(getApplicationContext(),"You do not have downloads",Toast.LENGTH_SHORT).show();
            this.finish();
        }


         canciones= new ArrayList<>();

        for (int i=0;i<miscanciones.size();i++){
            canciones.add(new Canciones(miscanciones.get(i).getName().toString().replace(".mp3",""),miscanciones.get(i).getAbsolutePath().toString()));
        }
        misCancionesAdapter.addAll(canciones);

        //setDummyContent();




      /*
        imageButtonSend.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        //Titulo = miscanciones.get(mMiscancionesList.getChildPosition(v)).getName().toString();
                        Log.e("ver", Titulo);
                        return true;
                    case MotionEvent.ACTION_UP:
                        return true;
                }
                return false;
            }
        }); */


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        final MenuItem item = menu.findItem(R.id.action_search);

        Drawable drawable = (Drawable) item.getIcon();
        drawable.mutate().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_IN);
        item.setIcon(drawable);


        final MenuItem item1 = menu.findItem(R.id.action_back);
        Drawable drawable1 = (Drawable) item1.getIcon();
        drawable1.mutate().setColorFilter(getResources().getColor(R.color.colorPrimaryDark),PorterDuff.Mode.SRC_IN);
        item1.setIcon(drawable1);
        item1.setVisible(mostrarmenu);

        item1.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item1) {
                finish();
                return true;
            }
        });


        final android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                misCancionesAdapter.setFilter(canciones);
                return true;
            }
        });

        return true;
    }

    private void setupMisCancionesList(){
        mMiscancionesList.setLayoutManager(new LinearLayoutManager(this));
        mMiscancionesList.setAdapter(misCancionesAdapter);
    }



    private void setDummyContent(){
        ArrayList<Canciones> canciones = new ArrayList<>();
        for (int i = 0; i< 10;i++){
            canciones.add(new Canciones("Cancion " + i," Path"));
        }
        misCancionesAdapter.addAll(canciones);
    }

    public List<File> findsongs(File root){
        List<File> al = new ArrayList<File>();
        File[] files = root.listFiles();
        for (File singleFile : files){
            if (singleFile.isDirectory() && !singleFile.isHidden()){
                al.addAll(findsongs(singleFile));
            }else  {
                if (singleFile.getName().endsWith(".mp3")){
                    al.add(singleFile);
                }
            }
        }
        return al;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<Canciones> cancionesList = filter(canciones,newText);
        misCancionesAdapter.setFilter(cancionesList);
        return true;
    }

    private List<Canciones> filter(List<Canciones> models, String query){
        query = query.toLowerCase();

        final List<Canciones> filteredCancionesList = new ArrayList<>();
        for (Canciones canciones: models){
            final String text = canciones.getNomMiCancion().toLowerCase();
            if (text.contains(query)){
                filteredCancionesList.add(canciones);
            }
        }
        return filteredCancionesList;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
        // AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
        //AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
}
