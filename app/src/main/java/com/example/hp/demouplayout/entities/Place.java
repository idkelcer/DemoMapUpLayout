package com.example.hp.demouplayout.entities;

import android.graphics.Bitmap;

/**
 * Created by HP on 06/09/2016.
 */
public class Place {


    private int suscursalId;
    private int establecimientoId;
    private String nombre;
    private String direccion;
    private double latitud;
    private double longitud;
    private int tipoBeneficioId;
    private double distance;
    private String iconUrl;
    private Bitmap bitmap;


    /**
     * @return The suscursalId
     */
    public int getSuscursalId() {
        return suscursalId;
    }

    /**
     * @param suscursalId The suscursal_id
     */
    public void setSuscursalId(int suscursalId) {
        this.suscursalId = suscursalId;
    }

    /**
     * @return The establecimientoId
     */

    public int getEstablecimientoId() {
        return establecimientoId;
    }

    /**
     * @param establecimientoId The establecimiento_id
     */

    public void setEstablecimientoId(int establecimientoId) {
        this.establecimientoId = establecimientoId;
    }

    /**
     * @return The nombre
     */

    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre The nombre
     */

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return The direccion
     */

    public String getDireccion() {
        return direccion;
    }

    /**
     * @param direccion The direccion
     */

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * @return The latitud
     */

    public double getLatitud() {
        return latitud;
    }

    /**
     * @param latitud The latitud
     */

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    /**
     * @return The longitud
     */

    public double getLongitud() {
        return longitud;
    }

    /**
     * @param longitud The longitud
     */

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    /**
     * @return The tipoBeneficioId
     */

    public int getTipoBeneficioId() {
        return tipoBeneficioId;
    }

    /**
     * @param tipoBeneficioId The tipo_beneficio_id
     */

    public void setTipoBeneficioId(int tipoBeneficioId) {
        this.tipoBeneficioId = tipoBeneficioId;
    }

    /**
     * @return The distance
     */

    public double getDistance() {
        return distance;
    }

    /**
     * @param distance The distance
     */

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}