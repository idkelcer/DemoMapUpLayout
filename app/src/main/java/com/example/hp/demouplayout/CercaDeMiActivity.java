
package com.example.hp.demouplayout;

import android.Manifest;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.Response;
import com.example.hp.demouplayout.adapter.ToolbarMenuListAdapter;
import com.example.hp.demouplayout.adapter.PageAdapter;
import com.example.hp.demouplayout.entities.BenefitResponse;
import com.example.hp.demouplayout.entities.CategoryResponse;
import com.example.hp.demouplayout.entities.PlaceResponse;
import com.example.hp.demouplayout.entities.Benefit;
import com.example.hp.demouplayout.entities.Category;
import com.example.hp.demouplayout.entities.Place;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class CercaDeMiActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, CategoryInterface {

    private static final String TAG = "CercaDeMiActivity";
    private GoogleMap mMap;
    private LinearLayout bottomSheetLayout;
    SupportMapFragment mapFragment;
    final int CODE_PERMISSION_FINE_LOCATION = 1;
    Marker userPositionMarker, selectedMarker;
    MarkerOptions userMarkerOptions;
    double currentUserLatitude, currentUserLongitude;
    BottomSheetBehavior bsb;
    Toolbar toolbar;
    List<Category> categoryList;
    PopupWindow popup;
    List<Place> places;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(null);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        bottomSheetLayout = (LinearLayout) findViewById(R.id.bottomSheet);
        TextView text = (TextView) bottomSheetLayout.findViewById(R.id.text);

        bsb = BottomSheetBehavior.from(bottomSheetLayout);
        bsb.setState(BottomSheetBehavior.STATE_HIDDEN);

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bsb.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        getCategoriesFromServer();

        inflatePopupMenu();
    }

    private void inflatePopupMenu() {

        View layout = getLayoutInflater().inflate(R.layout.popup_menu_demo, null);
        popup = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

       /* PopupWindow popup = new PopupWindow(this);
        View layout = getLayoutInflater().inflate(R.layout.popup_menu_demo, null);
        popup.setContentView(layout);
        // Set content width and height
        popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);*/
        // Closes the popup window when touch outside of it - when looses focus

        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        popup.setBackgroundDrawable(new ColorDrawable());
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        mMap = googleMap;
        setupMap();
        mMap.setOnMarkerClickListener(this);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-12.046374, -77.042793), 10.0f));
    }

    private void setupMap() {

        mMap.getUiSettings().setZoomControlsEnabled(true);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                CODE_PERMISSION_FINE_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {

            case CODE_PERMISSION_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                            PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                                    PackageManager.PERMISSION_GRANTED) {

                        Log.i(TAG, "in code permission fine location");

                        mMap.setMyLocationEnabled(true);
                        mMap.getUiSettings().setMyLocationButtonEnabled(true);

                        LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10, new LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {

                                Log.i(TAG, "change location");

                                currentUserLatitude = location.getLatitude();
                                currentUserLongitude = location.getLongitude();

                                if (userPositionMarker != null)
                                    userPositionMarker.remove();

                                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                userMarkerOptions = new MarkerOptions().position(latLng).title("")
                                        .icon((BitmapDescriptorFactory.fromResource(R.drawable.ubication)));
                                ;
                                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                                userPositionMarker = mMap.addMarker(userMarkerOptions);

                                getPlacesFromServer(0);
                            }

                            @Override
                            public void onStatusChanged(String s, int i, Bundle bundle) {

                            }

                            @Override
                            public void onProviderEnabled(String s) {

                            }

                            @Override
                            public void onProviderDisabled(String s) {

                            }
                        });

                    } else {
                        Toast.makeText(this, "error permission map", Toast.LENGTH_LONG).show();
                    }
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_category, menu);
        return true;
    }

    @Override
    public void onBackPressed() {

        Log.i(TAG, "onbackpressed");

        /*if(popup != null && popup.isShowing())
            popup.dismiss();*/

        if (bsb.getState() == BottomSheetBehavior.STATE_EXPANDED || bsb.getState() == BottomSheetBehavior.STATE_COLLAPSED)
            bsb.setState(BottomSheetBehavior.STATE_HIDDEN);
        else
            super.onBackPressed();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        if (selectedMarker != null && selectedMarker.getId().equals(marker.getId())) {

            Log.i(TAG, "visible");
            bsb.setState(BottomSheetBehavior.STATE_HIDDEN);
            selectedMarker = null;
            marker.hideInfoWindow();
            return true;
        }

        if (marker.getTitle().isEmpty()) {
            bsb.setState(BottomSheetBehavior.STATE_HIDDEN);
            return false;
        }

        getBenefitsFromServer(searchPlaceId(marker.getTitle()));

        selectedMarker = marker;

        return false;
    }

    private void setUpBottomSheet(List<Fragment> fragments) {

        PageAdapter pageAdapter = new PageAdapter(getSupportFragmentManager(), fragments);
        ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
        pager.setAdapter(pageAdapter);

        pageAdapter.notifyDataSetChanged();

        if (pageAdapter.getCount() != 1)
            pager.setPageMargin(10);

        bottomSheetLayout.setOnClickListener(null);
    }

    public void getPlacesFromServer(final int categoryId) {

        Log.i(TAG, "category id " + categoryId);

        String url = Constants.serviceUrl + "cercademi";
        String fullUrl = url + "?lat=" + currentUserLatitude + "&lng=" + currentUserLongitude + "&km=" + 2
                + "&tipo_beneficio_id=" + categoryId + "&session=8d1f0c0c975be23cd963ce24b3ffddc7";

        ClubRequestManager.getInstance(this).performJsonRequest(Request.Method.GET, fullUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, response.toString());

                if (response.has("error")) {

                    try {
                        showError(response.getString("error"));
                    } catch (JSONException e) {
                        Log.i(TAG, e.getLocalizedMessage());
                    }
                } else {

                    PlaceResponse placeResponse = new PlaceResponse(response);

                    fillMapWithPlaces(placeResponse.getData());

                    Log.i(TAG, "message 1 " + placeResponse.getData().size());

                    if (popup != null && popup.isShowing())
                        popup.dismiss();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                showError(VolleyErrorHelper.getMessage(error, CercaDeMiActivity.this));
                Log.i(TAG, "message error " + error.getLocalizedMessage());
            }
        });
    }

    private void showError(String errorMessage) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(errorMessage);
        alertDialog.setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    private void fillMapWithPlaces(List<Place> response) {

        mMap.clear();

        response = setUrlIconToPlaces(response);

        new GetBitmap().execute(response);
        /*LatLng latLng;
        places = response;

        for (Place place : places) {
            latLng = new LatLng(place.getLatitud(), place.getLongitud());
            MarkerOptions marker = new MarkerOptions().position(latLng)
                    .title(place.getNombre()).snippet(String.valueOf(place.getEstablecimientoId()));

            mMap.addMarker(marker);
        }

        if (userMarkerOptions != null)
            mMap.addMarker(userMarkerOptions);*/

    }

    private List<Place> setUrlIconToPlaces(List<Place> response) {

        for (Place r : response) {

            for (Category c : categoryList) {

                if (r.getTipoBeneficioId() == c.getId())
                    r.setIconUrl(c.getIcono());
            }
        }

        return response;
    }

    private int searchPlaceId(String s) {

        for (Place place : places) {

            if (place.getNombre().equals(s))
                return place.getEstablecimientoId();
        }

        return 0;
    }

    public void verticalDropDownIconMenu(MenuItem item) {

        Log.i(TAG, "verticaldropdown");

        bsb.setState(BottomSheetBehavior.STATE_HIDDEN);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;

        popup.showAsDropDown(toolbar, width, 0);
    }

    private void setUpMenuPopupRecycler() {

        View layout = popup.getContentView();

        if (categoryList == null || categoryList.size() == 0) {

            Log.i(TAG, "error on menu list");
            return;
        }

        RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.recycler);

        ToolbarMenuListAdapter mAdapter = new ToolbarMenuListAdapter(this, categoryList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    private void getCategoriesFromServer() {

        String url = Constants.serviceUrl + "listTipobeneficio?session=8d1f0c0c975be23cd963ce24b3ffddc7";

        ClubRequestManager.getInstance(this).performJsonRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d(TAG, response.toString());

                        if (response.has("error")) {

                            try {
                                showError(response.getString("error"));
                            } catch (JSONException e) {
                                Log.i(TAG, e.getLocalizedMessage());
                            }
                        } else {

                            CategoryResponse categoryResponse = new CategoryResponse(response);

                            fillCategoryList(categoryResponse.getData());

                            if (popup != null && popup.isShowing())
                                popup.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        showError(VolleyErrorHelper.getMessage(error, CercaDeMiActivity.this));
                        Log.i(TAG, "message error " + error.getLocalizedMessage());
                    }
                });
    }


    private void fillCategoryList(List<Category> response) {

        categoryList = new ArrayList<>();

        Category cat = new Category();
        cat.setNombre("TODAS");
        cat.setDrawable(R.drawable.todas);
        cat.setId(0);

        categoryList.add(cat);
        categoryList.addAll(response);

        setUpMenuPopupRecycler();
    }

    private void getBenefitsFromServer(int placeID) {

        String url = Constants.serviceUrl + "beneficiosBysucursal";
        String fullUrl = url + "?establecimiento_id=" + placeID + "&session=8d1f0c0c975be23cd963ce24b3ffddc7";

        ClubRequestManager.getInstance(this).performJsonRequest(Request.Method.GET, fullUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d(TAG, response.toString());

                        if (response.has("error")) {

                            try {
                                showError(response.getString("error"));
                            } catch (JSONException e) {
                                Log.i(TAG, e.getLocalizedMessage());
                            }
                        } else {

                            BenefitResponse benefitResponse = new BenefitResponse(response);

                            if (benefitResponse.getData().size() == 0) {

                                bsb.setState(BottomSheetBehavior.STATE_HIDDEN);
                                Toast.makeText(toolbar.getContext(), "No hay beneficios", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            List<Fragment> fragments = fillBenefitList(benefitResponse.getData());
                            setUpBottomSheet(fragments);
                            Log.i(TAG, "message 6 ");
                            bsb.setState(BottomSheetBehavior.STATE_COLLAPSED);


                            if (popup != null && popup.isShowing())
                                popup.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        showError(VolleyErrorHelper.getMessage(error, CercaDeMiActivity.this));
                        Log.i(TAG, "message error " + error.getLocalizedMessage());
                    }
                });
    }

    private List<Fragment> fillBenefitList(List<Benefit> benefits) {

        List<Fragment> fList = new ArrayList<>();

        for (int i = 0; i < benefits.size(); i++) {

            fList.add(BottomSheetFragment.newInstance(benefits.get(i)));
        }

        return fList;
    }

    public class GetBitmap extends AsyncTask<List<Place>, Void, List<Place>> {

        protected List<Place> doInBackground(List<Place>... passing) {

            List<Place> passed = passing[0]; //get passed arraylist

            //Some calculations...
            URL url;
            try {

                for (Place p : passed) {
                    Log.i(TAG, "icon  url " + p.getIconUrl());
                    if (p.getIconUrl() != null) {
                        url = new URL(p.getIconUrl());
                        Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        p.setBitmap(bmp);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return passed; //return result
        }

        protected void onPostExecute(List<Place> result) {

            Log.i(TAG, "size " + result.size());

            LatLng latLng;
            places = result;

            for (Place place : places) {
                latLng = new LatLng(place.getLatitud(), place.getLongitud());
                MarkerOptions marker = new MarkerOptions().position(latLng)
                        .title(place.getNombre()).snippet(String.valueOf(place.getEstablecimientoId()));
                if (place.getBitmap() != null)
                    marker.icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(place.getBitmap(), 250, 250)));

                mMap.addMarker(marker);
            }

            if (userMarkerOptions != null)
                mMap.addMarker(userMarkerOptions);
        }

        public Bitmap resizeMapIcons(Bitmap icon, int width, int height) {

            Bitmap resizedBitmap = Bitmap.createScaledBitmap(icon, width, height, false);
            return resizedBitmap;
        }
    }
}

