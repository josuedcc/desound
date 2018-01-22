package cc.devjo.desound.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cc.devjo.desound.R;
import cc.devjo.desound.models.Canciones;
/**
 * Created by Josue on 19/02/2016.
 */
public class MisCancionesAdapter extends RecyclerView.Adapter<MisCancionesAdapter.MisCancionesViewHolder>{

    ArrayList<Canciones> canciones;
    Context context;
    private int img_send=R.mipmap.ic_sendmy;
    private int img_send_touch=R.mipmap.ic_sendtouch;
    private int img_delete=R.mipmap.ic_delete1;
    private int img_delete_touch=R.mipmap.ic_delete2;

    public MisCancionesAdapter(Context context) {
        this.context = context;
        this.canciones = new ArrayList<>();
    }

    @Override
    public MisCancionesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_miscanciones,parent,false);
        return new MisCancionesViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final MisCancionesViewHolder holder, final int position) {
        Canciones actualCancion = canciones.get(position);

        holder.setNameMicancion(actualCancion.getNomMiCancion());
        holder.setDirecMicancion(actualCancion.getDirecMiCancion());

        holder.btn_send.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        holder.btn_send.setImageResource(img_send_touch);
                        Uri TituloActual = Uri.parse(holder.nameMicancion.getText().toString());
                        Uri DirecActual = Uri.parse("file:///" + holder.direcMicancion.getText().toString());
                        String mimeType = "audio/mpeg";
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, DirecActual);
                        shareIntent.setType(mimeType);
                        context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.Share)));
                        return true;
                    case MotionEvent.ACTION_UP:
                        holder.btn_send.setImageResource(img_send);
                        return true;
                }

                return false;
            }
        });

        holder.btn_remove.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        holder.btn_remove.setImageResource(img_delete_touch);
                        Uri DirecActual = Uri.parse(holder.direcMicancion.getText().toString());
                        Dialogo1Click(context, position, DirecActual);
                            return true;

                    case MotionEvent.ACTION_UP:
                        holder.btn_remove.setImageResource(img_delete);
                        return true;
                }
                return false;
            }
        });

        /* holder.btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri DirecActual = Uri.parse(holder.direcMicancion.getText().toString());
                Dialogo1Click(context, position,DirecActual);
               // removeAt(position);
            }
        }); */

    }

    @Override
    public int getItemCount() {
        return canciones.size();
    }

    public void addAll(@NonNull ArrayList<Canciones> canciones){
        if (canciones == null)
            throw  new NullPointerException("No nulo");

        this.canciones.addAll(canciones);
        notifyItemRangeInserted(getItemCount() -1, canciones.size());
    }

    public class MisCancionesViewHolder extends RecyclerView.ViewHolder{
        TextView nameMicancion;
        TextView direcMicancion;
        ImageButton btn_send;
        ImageButton btn_remove;
        public MisCancionesViewHolder(View itemView) {
            super(itemView);
            nameMicancion = (TextView) itemView.findViewById(R.id.name_mysong);
            direcMicancion = (TextView) itemView.findViewById(R.id.direccion_mysong);
            btn_send = (ImageButton) itemView.findViewById(R.id.send_mysong);
            btn_remove = (ImageButton) itemView.findViewById(R.id.delete_song);
        }

        public void setNameMicancion(String name){
            nameMicancion.setText(name);
        }

        public void setDirecMicancion(String direccion){
            direcMicancion.setText(direccion);
        }
    }

    public void removeAt (int position){
        canciones.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, canciones.size());
    }

    public void Dialogo1Click(Context v, final int position, final Uri file){
        AlertDialog.Builder ventana = new AlertDialog.Builder(v);
        ventana.setMessage(v.getString(R.string.msgAlertDelete) );
        ventana.setTitle(v.getString(R.string.TitleMsgDelete));
        ventana.setPositiveButton(v.getString(R.string.btnpositive), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                File file1 = new File(String.valueOf(file));
                boolean deleteF = file1.delete();
                removeAt(position);
            }
        });
        ventana.setNegativeButton(v.getString(R.string.btnnegative), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        ventana.show();
    }

    public void setFilter(List<Canciones> cancionesF){
        canciones = new ArrayList<>();
        canciones.addAll(cancionesF);
        notifyDataSetChanged();
    }
}
