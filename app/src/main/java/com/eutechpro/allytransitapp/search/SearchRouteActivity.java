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
import com.eutechpro.allytransitapp.data.DataError;
import com.eutechpro.allytransitapp.data.RetrofitRoutesManager;
import com.eutechpro.allytransitapp.data.RoutesManager;
import com.eutechpro.allytransitapp.data.model.Route;
import com.eutechpro.allytransitapp.search.RoutesListFragment.OnRouteSelected;

import java.util.List;

import javax.inject.Inject;

public class SearchRouteActivity extends BaseActivity implements SearchRouteView {
    public static final int ROUTE_SEARCH_REQUEST_CODE = 1111;
    @Inject
    public RoutesManager routesManager;
    private FrameLayout loadingScreen;
    private FloatingActionButton search;
    private EditText to;
    private EditText from;
    private OnClickListener searchListener;
    private RoutesListFragment routesListFragment;
    private SearchRouteActivityPresenter presenter;

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
        presenter = new SearchRouteActivityPresenter(this);
        presenter.addDataRepository(routesManager);
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.stopEverything();
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
        dismissKeyboard(v.getWindowToken());
        if(from.getText().toString().isEmpty()){
            Snackbar.make(v, "You have to insert starting location", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (to.getText().toString().isEmpty()) {
            Snackbar.make(v, "You have to insert destination", Snackbar.LENGTH_LONG).show();
            return;
        }
        showLoadingViews();
        presenter.performSearch();
    }

    private void hideLoadingViews() {
        loadingScreen.setVisibility(View.INVISIBLE);
    }

    private void showLoadingViews() {
        loadingScreen.setVisibility(View.VISIBLE);
    }

    @Override
    protected void getViewReferences() {
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


    //region SearchRouteView implementation
    @Override
    public void searchSuccessfull(List<Route> fetchedRoutes) {
        routesListFragment.setContent(fetchedRoutes);
        hideLoadingViews();
    }

    @Override
    public void searchFailed(DataError dataError) {
        Snackbar.make(null, "Operation failed. ", Snackbar.LENGTH_LONG).setAction("Retry?", searchListener).show();
        hideLoadingViews();
    }

    @Override
    public void routeSelected(Route selectedRoute) {
        RetrofitRoutesManager.setRouteAsArgument(selectedRoute);
        setResult(Activity.RESULT_OK);
        finish();
    }
    //endregion
}
