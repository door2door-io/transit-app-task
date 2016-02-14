package com.eutechpro.allytransitapp.map;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.eutechpro.allytransitapp.AllyApplication;
import com.eutechpro.allytransitapp.BaseActivity;
import com.eutechpro.allytransitapp.R;
import com.eutechpro.allytransitapp.data.RetrofitRoutesManager;
import com.eutechpro.allytransitapp.search.SearchRouteActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import javax.inject.Inject;

public class MapsActivity extends BaseActivity {
    private GoogleMap map;
    @Inject
    public RouteDrawer routeDrawingManager;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        AllyApplication.MapsActivityComponent(this).inject(this);

        initToolbar();
        getViewReferences();
        initGoogleMap();
        initFab();
    }

    @Override
    protected void getViewReferences() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
    }

    private void initGoogleMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                map.getUiSettings().setMapToolbarEnabled(false);
                routeDrawingManager.setMap(map);
            }
        });
    }
    private void initFab() {
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, SearchRouteActivity.class);
                startActivityForResult(intent, SearchRouteActivity.ROUTE_SEARCH_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SearchRouteActivity.ROUTE_SEARCH_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                routeDrawingManager.clearMap();
                routeDrawingManager.drawRouteOnMap(RetrofitRoutesManager.getRouteAsArgument());
            }
        }
    }
}
