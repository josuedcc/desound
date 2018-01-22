package cc.devjo.desound.models;

/**
 * Created by Josue on 19/02/2016.
 */
public class Canciones {
    public Canciones(String nomMiCancion, String direcMiCancion) {
        this.nomMiCancion = nomMiCancion;
        this.direcMiCancion = direcMiCancion;
    }

    String nomMiCancion;
    String direcMiCancion;


    public String getNomMiCancion() {
        return nomMiCancion;
    }

    public void setNomMiCancion(String nomMiCancion) {
        this.nomMiCancion = nomMiCancion;
    }

    public String getDirecMiCancion() {
        return direcMiCancion;
    }

    public void setDirecMiCancion(String direcMiCancion) {
        this.direcMiCancion = direcMiCancion;
    }
}
