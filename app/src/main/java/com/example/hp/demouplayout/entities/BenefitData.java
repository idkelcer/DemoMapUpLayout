package com.example.hp.demouplayout.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kelvin on 09/09/16.
 */
public class BenefitData {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("xbeneficios")
    @Expose
    private List<Xbeneficio> xbeneficios = new ArrayList<>();

    /**
     *
     * @return
     * The status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     *
     * @param status
     * The status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     *
     * @return
     * The xbeneficios
     */
    public List<Xbeneficio> getXbeneficios() {
        return xbeneficios;
    }

    /**
     *
     * @param xbeneficios
     * The xbeneficios
     */
    public void setXbeneficios(List<Xbeneficio> xbeneficios) {
        this.xbeneficios = xbeneficios;
    }

}