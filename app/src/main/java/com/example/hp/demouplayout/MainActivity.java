
package com.example.hp.demouplayout;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
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

import com.example.hp.demouplayout.api.CategoryResponse;
import com.example.hp.demouplayout.api.PlaceClient;
import com.example.hp.demouplayout.api.PlaceSearchResponse;
import com.example.hp.demouplayout.api.PlaceService;
import com.example.hp.demouplayout.entities.Place;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, CategoryInterface {

    private static final String TAG = "MainActivity";
    private GoogleMap mMap;
    private LinearLayout bottomSheetLayout;
    SupportMapFragment mapFragment;
    final int CODE_PERMISSION_FINE_LOCATION = 1;
    Marker myPositionMarker;
    MarkerOptions userMarkerOptions;
    double currentUserLatitude, currentUserLongitude;
    BottomSheetBehavior bsb;
    Toolbar toolbar;
    List<Category> categoryList;
    PopupWindow popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
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

        setUpBottomSheet();
    }

    private void setUpBottomSheet() {

        List<Fragment> fragments = getFragments();
        PageAdapter pageAdapter = new PageAdapter(getSupportFragmentManager(), fragments);
        ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
        pager.setAdapter(pageAdapter);

        if (pageAdapter.getCount() != 1)
            pager.setPageMargin(30);

        bottomSheetLayout.setOnClickListener(null);

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        mMap = googleMap;
        setupMap();
        mMap.setOnMarkerClickListener(this);
    }

    private void setupMap() {

        mMap.getUiSettings().setZoomControlsEnabled(true);


        /*LatLng latLng = new LatLng(-12.046374, -77.042793);
        MarkerOptions userMarkerOptions = new MarkerOptions().position(latLng).title("Hello Maps ");
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        mMap.addMarker(userMarkerOptions);

        latLng = new LatLng(-12.046374, -77.052792);
        userMarkerOptions = new MarkerOptions().position(latLng).title("Hello Maps ");
        mMap.addMarker(userMarkerOptions);

        latLng = new LatLng(-12.056375, -77.042793);
        userMarkerOptions = new MarkerOptions().position(latLng).title("Hello Maps ");
        mMap.addMarker(userMarkerOptions);*/

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

                        Log.i(TAG, "position");

                        mMap.setMyLocationEnabled(true);
                        mMap.getUiSettings().setMyLocationButtonEnabled(true);

                        LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 10, new LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {

                                Log.i(TAG, "change location");

                                currentUserLatitude = location.getLatitude();
                                currentUserLongitude = location.getLongitude();

                                if(myPositionMarker != null)
                                    myPositionMarker.remove();

                                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                userMarkerOptions = new MarkerOptions().position(latLng).title("")
                                        .icon((BitmapDescriptorFactory.fromResource(R.drawable.ubication)));;
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                                myPositionMarker = mMap.addMarker(userMarkerOptions);

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

        if(popup != null && popup.isShowing())
            popup.dismiss();

        if (bsb.getState() == BottomSheetBehavior.STATE_EXPANDED || bsb.getState() == BottomSheetBehavior.STATE_COLLAPSED)
            bsb.setState(BottomSheetBehavior.STATE_HIDDEN);
        else
            super.onBackPressed();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        if(marker.getTitle().equals(""))
        {
            bsb.setState(BottomSheetBehavior.STATE_HIDDEN);
            return false;
        }


        bsb.setState(BottomSheetBehavior.STATE_COLLAPSED);
        return false;
    }

    private List<Fragment> getFragments() {

        List<Fragment> fList = new ArrayList<>();
        fList.add(PageFragment.newInstance("Fragment 1"));
        fList.add(PageFragment.newInstance("Fragment 2"));
        fList.add(PageFragment.newInstance("Fragment 3"));
        fList.add(PageFragment.newInstance("Fragment 4"));
        return fList;
    }

    public void getPlacesFromServer(int categoryId) {

        PlaceClient placeClient = new PlaceClient();
        PlaceService placeService = placeClient.getPlaceService();

        Call<PlaceSearchResponse> call = placeService.searchPlaces(currentUserLatitude, currentUserLongitude, 2, categoryId);

        call.enqueue(new Callback<PlaceSearchResponse>() {
            @Override
            public void onResponse(Call<PlaceSearchResponse> call, Response<PlaceSearchResponse> response) {

                if (response.isSuccessful()) {

                    PlaceSearchResponse placeSearchResponse = response.body();

                    fillMapWithPlaces(placeSearchResponse);

                    Log.i(TAG, "message " + placeSearchResponse.getData().size());

                    if(popup != null && popup.isShowing())
                        popup.dismiss();
                } else {

                    Log.i(TAG, "message " + response.message());
                }
            }

            @Override
            public void onFailure(Call<PlaceSearchResponse> call, Throwable t) {

                Log.i(TAG, "message " + t.getLocalizedMessage());
                if(popup != null && popup.isShowing())
                    popup.dismiss();
            }
        });
    }

    private void fillMapWithPlaces(PlaceSearchResponse response) {

        mMap.clear();

        LatLng latLng;

        for (Place place : response.getData()) {
            latLng = new LatLng(Double.parseDouble(place.getLatitud()),Double.parseDouble(place.getLongitud()));
            MarkerOptions marker = new MarkerOptions().position(latLng)
                                        .title(place.getNombre());
            mMap.addMarker(marker);
        }

        if(userMarkerOptions != null)
            mMap.addMarker(userMarkerOptions);

    }

    public void verticalDropDownIconMenu(MenuItem item) {

        bsb.setState(BottomSheetBehavior.STATE_HIDDEN);

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
        //popup.setClippingEnabled(true);
        //popup.setAnimationStyle(android.R.style.Widget_DeviceDefault_DropDownItem);
        //popup.setAnimationStyle(android.R.style.TextAppearance_Holo_Widget_TextView_PopupMenu);
        //popup.setAnimationStyle(android.R.style.Widget_Holo_Light_PopupMenu);
        // Show anchored to button

        popup.setBackgroundDrawable(new ColorDrawable());

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;

        popup.showAsDropDown(toolbar, width, 0);
        
        setUpRecycler(layout);
    }

    private void setUpRecycler(View layout) {

        setupMenuItems();

        RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.recycler);

        MenuListAdapter mAdapter = new MenuListAdapter(this, categoryList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    private void setupMenuItems() {

        int drawableResourceId = getResources().getIdentifier("todas", "drawable", this.getPackageName());

        categoryList = new ArrayList<>();

        Category category = new Category(0, ContextCompat.getDrawable(this, drawableResourceId), "TODAS");
        categoryList.add(category);

        category = new Category(8, ContextCompat.getDrawable(this, R.drawable.restau), "GASTRONOMÍA");
        categoryList.add(category);

        category = new Category(10, ContextCompat.getDrawable(this, R.drawable.entrene), "CULTURA Y ENTRETENIMIENTO");
        categoryList.add(category);

        category = new Category(1, ContextCompat.getDrawable(this, R.drawable.viaj), "TURISMO");
        categoryList.add(category);

        category = new Category(7, ContextCompat.getDrawable(this, R.drawable.mod), "MODA Y BELLEZA");
        categoryList.add(category);

        category = new Category(2, ContextCompat.getDrawable(this, R.drawable.product), "PRODUCTOS Y SERVICIOS");
        categoryList.add(category);

        category = new Category(9, ContextCompat.getDrawable(this, R.drawable.ed), "EDUCACIÓN");
        categoryList.add(category);
    }

    private void getCategoriesFromServer(){

        PlaceClient placeClient = new PlaceClient();
        PlaceService placeService = placeClient.getPlaceService();

        Call<CategoryResponse> call = placeService.getCategories();

        call.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {

                if(response.isSuccessful()){

                       CategoryResponse categoryResponse =  response.body();

                        fillCategoryList(categoryResponse);
                }else
                {
                    Log.i(TAG, response.message());
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                Log.i(TAG, t.getLocalizedMessage());
            }
        });
    }

    private void fillCategoryList(CategoryResponse response) {

        for(com.example.hp.demouplayout.entities.Category category: response.getData()){

        }
    }
}

