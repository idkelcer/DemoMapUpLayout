
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
import com.example.hp.demouplayout.api.CercaDeMiClient;
import com.example.hp.demouplayout.api.PlaceSearchResponse;
import com.example.hp.demouplayout.api.CercaDeMiService;
import com.example.hp.demouplayout.entities.Category;
import com.example.hp.demouplayout.entities.OldCategory;
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
    List<OldCategory> oldCategoryList;
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

        getCategoriesFromServer();
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
        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(-12.046374, -77.042793) , 10.0f) );
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
                                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
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

        if(marker.getTitle().isEmpty())
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

        Log.i(TAG, "category id " + categoryId);

        CercaDeMiClient placeClient = new CercaDeMiClient();
        CercaDeMiService placeService = placeClient.getPlaceService();

        Call<PlaceSearchResponse> call = placeService.searchPlaces(currentUserLatitude, currentUserLongitude, 2, categoryId);

        call.enqueue(new Callback<PlaceSearchResponse>() {
            @Override
            public void onResponse(Call<PlaceSearchResponse> call, Response<PlaceSearchResponse> response) {

                if (response.isSuccessful()) {

                    PlaceSearchResponse placeSearchResponse = response.body();

                    fillMapWithPlaces(placeSearchResponse);

                    Log.i(TAG, "message 1 " + placeSearchResponse.getData().size());

                    if(popup != null && popup.isShowing())
                        popup.dismiss();
                } else {

                    Log.i(TAG, "message 2 " + response.message());

                    if(popup != null && popup.isShowing())
                        popup.dismiss();
                }
            }

            @Override
            public void onFailure(Call<PlaceSearchResponse> call, Throwable t) {

                Log.i(TAG, "message 3" + t.getLocalizedMessage());
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
        popup.setBackgroundDrawable(new ColorDrawable());

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;

        popup.showAsDropDown(toolbar, width, 0);
        
        setUpRecycler(layout);
    }

    private void setUpRecycler(View layout) {

        //setupMenuItems();
        if(categoryList == null || categoryList.size() == 0){

            Log.i(TAG, "error on menu list");
            return;
        }

        RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.recycler);

        //MenuListAdapter mAdapter = new MenuListAdapter(this, oldCategoryList);
        MenuListAdapter mAdapter = new MenuListAdapter(this, categoryList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    private void setupMenuItems() {

        int drawableResourceId = getResources().getIdentifier("todas", "drawable", this.getPackageName());

        oldCategoryList = new ArrayList<>();

        OldCategory category = new OldCategory(0, ContextCompat.getDrawable(this, drawableResourceId), "TODAS");
        oldCategoryList.add(category);

        category = new OldCategory(8, ContextCompat.getDrawable(this, R.drawable.restau), "GASTRONOMÍA");
        oldCategoryList.add(category);

        category = new OldCategory(10, ContextCompat.getDrawable(this, R.drawable.entrene), "CULTURA Y ENTRETENIMIENTO");
        oldCategoryList.add(category);

        category = new OldCategory(1, ContextCompat.getDrawable(this, R.drawable.viaj), "TURISMO");
        oldCategoryList.add(category);

        category = new OldCategory(7, ContextCompat.getDrawable(this, R.drawable.mod), "MODA Y BELLEZA");
        oldCategoryList.add(category);

        category = new OldCategory(2, ContextCompat.getDrawable(this, R.drawable.product), "PRODUCTOS Y SERVICIOS");
        oldCategoryList.add(category);

        category = new OldCategory(9, ContextCompat.getDrawable(this, R.drawable.ed), "EDUCACIÓN");
        oldCategoryList.add(category);
    }

    private void getCategoriesFromServer() {

        CercaDeMiClient placeClient = new CercaDeMiClient();
        CercaDeMiService placeService = placeClient.getPlaceService();

        Call<CategoryResponse> call = placeService.getCategories();

        call.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {

                if(response.isSuccessful()){

                       CategoryResponse categoryResponse =  response.body();

                        fillCategoryList(categoryResponse);
                }else
                {
                    Log.i(TAG, "message 4 " + response.message());
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                Log.i(TAG, "message 5 " + t.getLocalizedMessage());
            }
        });
    }

    private void fillCategoryList(CategoryResponse response) {

        categoryList = new ArrayList<>();

        for(Category category: response.getData()){

            categoryList.add(category);
        }
    }
}

