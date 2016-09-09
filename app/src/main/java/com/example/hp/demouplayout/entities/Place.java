package com.example.hp.demouplayout.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by HP on 06/09/2016.
 */
public class Place {

    @SerializedName("suscursal_id")
    @Expose
    private String suscursalId;
    @SerializedName("establecimiento_id")
    @Expose
    private String establecimientoId;
    @SerializedName("nombre")
    @Expose
    private String nombre;
    @SerializedName("direccion")
    @Expose
    private String direccion;
    @SerializedName("latitud")
    @Expose
    private String latitud;
    @SerializedName("longitud")
    @Expose
    private String longitud;
    @SerializedName("tipo_beneficio_id")
    @Expose
    private String tipoBeneficioId;
    @SerializedName("distance")
    @Expose
    private String distance;

    /**
     *
     * @return
     * The suscursalId
     */
    public String getSuscursalId() {
        return suscursalId;
    }

    /**
     *
     * @param suscursalId
     * The suscursal_id
     */
    public void setSuscursalId(String suscursalId) {
        this.suscursalId = suscursalId;
    }

    /**
     *
     * @return
     * The establecimientoId
     */
    public String getEstablecimientoId() {
        return establecimientoId;
    }

    /**
     *
     * @param establecimientoId
     * The establecimiento_id
     */
    public void setEstablecimientoId(String establecimientoId) {
        this.establecimientoId = establecimientoId;
    }

    /**
     *
     * @return
     * The nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     *
     * @param nombre
     * The nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     *
     * @return
     * The direccion
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     *
     * @param direccion
     * The direccion
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     *
     * @return
     * The latitud
     */
    public String getLatitud() {
        return latitud;
    }

    /**
     *
     * @param latitud
     * The latitud
     */
    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    /**
     *
     * @return
     * The longitud
     */
    public String getLongitud() {
        return longitud;
    }

    /**
     *
     * @param longitud
     * The longitud
     */
    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    /**
     *
     * @return
     * The tipoBeneficioId
     */
    public String getTipoBeneficioId() {
        return tipoBeneficioId;
    }

    /**
     *
     * @param tipoBeneficioId
     * The tipo_beneficio_id
     */
    public void setTipoBeneficioId(String tipoBeneficioId) {
        this.tipoBeneficioId = tipoBeneficioId;
    }

    /**
     *
     * @return
     * The distance
     */
    public String getDistance() {
        return distance;
    }

    /**
     *
     * @param distance
     * The distance
     */
    public void setDistance(String distance) {
        this.distance = distance;
    }

}