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
    @SerializedName("benefits")
    @Expose
    private List<Benefit> benefits = new ArrayList<>();

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
     * The benefits
     */
    public List<Benefit> getBenefits() {
        return benefits;
    }

    /**
     *
     * @param benefits
     * The benefits
     */
    public void setBenefits(List<Benefit> benefits) {
        this.benefits = benefits;
    }

}