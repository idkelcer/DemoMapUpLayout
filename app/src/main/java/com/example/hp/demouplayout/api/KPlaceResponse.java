package com.example.hp.demouplayout.api;

import com.example.hp.demouplayout.entities.Place;

import org.json.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kelvin on 13/09/16.
 */
public class KPlaceResponse {

    private Integer status;

    private String message;

    private List<Place> data = new ArrayList<>();

    public KPlaceResponse(JSONObject jsonObject){


        try {

            status =  jsonObject.getInt("status");
            message = jsonObject.getString("message");
            JSONArray array = jsonObject.getJSONArray("data");

            for(int i = 0; i < array.length(); i++) {

                JSONObject jsonObject1 = (JSONObject) array.get(i);

                Place place = new Place();
                place.setSuscursalId(jsonObject1.getInt("suscursal_id"));
                place.setEstablecimientoId(jsonObject1.getInt("establecimiento_id"));
                place.setNombre(jsonObject1.getString("nombre"));
                place.setDireccion(jsonObject1.getString("direccion"));
                place.setLatitud(jsonObject1.getDouble("latitud"));
                place.setLongitud(jsonObject1.getDouble("longitud"));
                place.setTipoBeneficioId(jsonObject1.getInt("tipo_beneficio_id"));
                place.setDistance(jsonObject1.getDouble("distance"));

                data.add(place);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<Place> getData() {
        return data;
    }

    public void setData(List<Place> data) {
        this.data = data;
    }
}
