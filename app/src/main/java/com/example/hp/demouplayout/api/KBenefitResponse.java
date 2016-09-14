package com.example.hp.demouplayout.api;

import com.example.hp.demouplayout.entities.Benefit;
import com.example.hp.demouplayout.entities.Category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kelvin on 14/09/16.
 */
public class KBenefitResponse {

    private Integer status;

    private String message;

    private List<Benefit> data = new ArrayList<>();

    public KBenefitResponse(JSONObject jsonObject){


        try {

            status =  jsonObject.getInt("status");
            message = jsonObject.getString("message");

            JSONArray array = jsonObject.getJSONArray("data");

            for(int i = 0; i < array.length(); i++) {

                JSONObject jsonObject1 = (JSONObject) array.get(i);

                Benefit benefit = new Benefit();

                benefit.setId(jsonObject1.getInt("id"));
                benefit.setTitulo(jsonObject1.getString("titulo"));
                benefit.setChapita(jsonObject1.getString("chapita"));
                benefit.setPathLogo(jsonObject1.getString("path_logo"));

                data.add(benefit);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<Benefit> getData() {
        return data;
    }

    public void setData(List<Benefit> data) {
        this.data = data;
    }
}
