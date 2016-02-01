package com.eutechpro.allytransitapp;

import com.eutechpro.allytransitapp.data.model.Route;
import com.eutechpro.allytransitapp.map.RouteDrawingI;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class RouteDrawingManagerTest {
    private DevActivity activity;

    @Before
    public void setUp() throws Exception {
        ActivityController<DevActivity> controller = Robolectric.buildActivity(DevActivity.class);
        activity = controller.get();
    }

    @Test(expected = IllegalStateException.class)
    public void testClearingMapWithoutMapSet() throws Exception {
        RouteDrawingI routeDrawing = new com.eutechpro.allytransitapp.map.RouteDrawingManager(activity);
        routeDrawing.clearMap();
    }

    @Test(expected = IllegalStateException.class)
    public void testDrawingMapWithoutMapSet() throws Exception {
        RouteDrawingI routeDrawing = new com.eutechpro.allytransitapp.map.RouteDrawingManager(activity);
        Route route = Mockito.mock(Route.class);
        routeDrawing.drawRouteOnMap(route);
    }

}