package com.example.hp.demouplayout.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by kelvin on 09/09/16.
 */
public class Benefit implements Serializable{

    private Integer id;
    private String titulo;
    private String chapita;
    private String pathLogo;

    /**
     *
     * @return
     * The id
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The titulo
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     *
     * @param titulo
     * The titulo
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     *
     * @return
     * The chapita
     */
    public String getChapita() {
        return chapita;
    }

    /**
     *
     * @param chapita
     * The chapita
     */
    public void setChapita(String chapita) {
        this.chapita = chapita;
    }

    /**
     *
     * @return
     * The pathLogo
     */
    public String getPathLogo() {
        return pathLogo;
    }

    /**
     *
     * @param pathLogo
     * The path_logo
     */
    public void setPathLogo(String pathLogo) {
        this.pathLogo = pathLogo;
    }

}