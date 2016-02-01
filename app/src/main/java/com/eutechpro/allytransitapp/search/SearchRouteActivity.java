package com.eutechpro.allytransitapp.search;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.eutechpro.allytransitapp.AllyApplication;
import com.eutechpro.allytransitapp.BaseActivity;
import com.eutechpro.allytransitapp.R;
import com.eutechpro.allytransitapp.data.DataCallback;
import com.eutechpro.allytransitapp.data.DataError;
import com.eutechpro.allytransitapp.data.RetrofitRoutesManager;
import com.eutechpro.allytransitapp.data.RoutesManager;
import com.eutechpro.allytransitapp.data.model.Route;
import com.eutechpro.allytransitapp.search.RoutesListFragment.OnRouteSelected;

import java.util.List;

import javax.inject.Inject;

//import com.facebook.drawee.view.SimpleDraweeView;
//import com.facebook.imagepipeline.core.ImagePipelineConfig;

public class SearchRouteActivity extends BaseActivity {
    public static final int ROUTE_SEARCH_REQUEST_CODE = 1111;
    @Inject
    public RoutesManager routesManager;
    private FrameLayout loadingScreen;
    private FloatingActionButton search;
    private EditText to;
    private EditText from;
    private OnClickListener searchListener;
    private RoutesListFragment routesListFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_route);

        AllyApplication.SearchActivityComponent(this).inject(this);

        initToolbar();
        getViewReferences();

        routesListFragment = (RoutesListFragment) getSupportFragmentManager().findFragmentById(R.id.routes_list_fragment);
        routesListFragment.setOnRouteSelected(new OnRouteSelected() {
            @Override
            public void selectedRoute(Route route) {
                RetrofitRoutesManager.setRouteAsArgument(route);
                setResult(Activity.RESULT_OK);
                finish();
            }
        });

        to.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch(v);
                    return true;
                }
                return false;
            }
        });

        searchListener = new OnClickListener() {
            @Override
            public void onClick(final View v) {
                performSearch(v);
            }
        };

        search.setOnClickListener(searchListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        routesManager.cancelGettingAllRoutes();
        loadingScreen.setVisibility(View.GONE);
    }
    @Override
    protected void initToolbar(){
        super.initToolbar();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Find route you wish:");
        }
    }
    private void performSearch(final View v) {
        if(from.getText().toString().isEmpty()){
            Snackbar.make(v, "You have to insert starting location", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (to.getText().toString().isEmpty()) {
            Snackbar.make(v, "You have to insert destination", Snackbar.LENGTH_LONG).show();
            return;
        }
        routesManager.getAllRoutes(new DataCallback<List<Route>>() {
            @Override
            public void beforeStart() {
                dismissKeyboard(v.getWindowToken());
                loadingScreen.setVisibility(View.VISIBLE);
            }

            @Override
            public void onResponse(List<Route> fetchedRoutes) {
                routesListFragment.setContent(fetchedRoutes);
                loadingScreen.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(DataError dataError) {
                loadingScreen.setVisibility(View.INVISIBLE);
                Snackbar snackbar = Snackbar.make(v, "Operation failed. ", Snackbar.LENGTH_LONG)
                                            .setAction("Retry?", searchListener);
                snackbar.show();
            }
        });
    }

    @Override
    protected void getViewReferences() {
        //        SimpleDraweeView image = (SimpleDraweeView)findViewById(R.id.route_provider);
        from = (EditText) findViewById(R.id.from);
        to = (EditText) findViewById(R.id.to);
        search = (FloatingActionButton) findViewById(R.id.search_button);
        loadingScreen = (FrameLayout) findViewById(R.id.loading_layout);
    }

    private void dismissKeyboard(IBinder windowToken) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(windowToken, 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
