package com.eutechpro.allytransitapp;

import android.util.Log;

import com.eutechpro.allytransitapp.data.DataCallback;
import com.eutechpro.allytransitapp.data.DataError;
import com.eutechpro.allytransitapp.data.RetrofitRoutesManager;
import com.eutechpro.allytransitapp.data.RoutesManager;
import com.eutechpro.allytransitapp.data.rest.retrofit.RetrofitClient;
import com.eutechpro.allytransitapp.data.model.Route;
import com.eutechpro.allytransitapp.data.model.properties.CarSharingRouteProperties;
import com.eutechpro.allytransitapp.utils.AssetsUtil;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Test custom deserializer. We will use mocked JSON and perform regular mocked fetch via MockWebServer.
 */
public class RoutesDeserializerTest {
    @SuppressWarnings("unused")
    private static final String TAG = "RoutesDeserializerTest";
    private static final String BASE_MOCK_FILES_PATH = "mocks/routes/";
    private MockWebServer server;

    @Before
    public void setUp() throws Exception {
        server = new MockWebServer();
    }

    @Test
    public void testReadingMockValuesFromAssets() throws Exception {
        String responseString = AssetsUtil.getStringFromFile(BASE_MOCK_FILES_PATH + "success/200.json");
        assertNotNull("Fetching from assets return null!" + responseString);
        Log.d(TAG, "Assets content:" + responseString);
    }

    @Test
    public void testDeserialiser() throws Exception {
        String responseBody = AssetsUtil.getStringFromFile(BASE_MOCK_FILES_PATH + "success/200.json");
        final int responseHttpCode = 200;

        server.enqueue(new MockResponse().setResponseCode(responseHttpCode).setBody(responseBody));

        final CountDownLatch latch = new CountDownLatch(1);

        RoutesManager routesManager = new RetrofitRoutesManager(new RetrofitClient(server.url("/").toString()));
        routesManager.getAllRoutes(new DataCallback<List<Route>>() {
            @Override
            public void beforeStart() {
                //Unused
            }

            @Override
            public void onResponse(List<Route> routes) {
                System.out.println("");
                assertEquals("Not all routes has been processed.", 5, routes.size());
                Route<CarSharingRouteProperties> route = routes.get(0);

                assertPrice(route);

                assertRouteType(route);

                assertRouteProperties(route);

                assertRouteSegments(route);

                assertRouteProvider(route);

                latch.countDown();
            }

            @Override
            public void onFailure(DataError dataError) {
                System.out.println("");
                latch.countDown();
            }
        });
        latch.await();
        System.out.println("");
    }

    private void assertRouteProvider(Route<CarSharingRouteProperties> route) {
        assertNotNull("Route provider not parsed!", route.getProvider());
        assertEquals("Provider android package parsed badly!", "mock provider android package", route.getProvider().getAndroidPackageName());
        assertEquals("Provider disclaimer parsed badly!", "mock provider disclaimer", route.getProvider().getDisclaimer());
        assertEquals("Provider display name parsed badly!", "mock provider display name", route.getProvider().getDisplayName());
        assertEquals("Provider ios app url parsed badly!", "mock provider ios app url", route.getProvider().getIosAppUrl());
        assertEquals("Provider ios itunes url parsed badly!", "mock provider itunes url", route.getProvider().getIosItunesUrl());
        assertEquals("Provider provider icon url parsed badly!", "mock provider icon url", route.getProvider().getProviderIconUrl());
        assertEquals("Provider provider name parsed badly!", "mock provider", route.getProvider().getProviderType());
    }

    private void assertRouteSegments(Route<CarSharingRouteProperties> route) {
        assertNotNull("Route segments not parsed!", route.getSegments());
        assertEquals("Segment size not good parsed badly!", 4, route.getSegments().size());
        assertEquals("Segment color parsed badly!", "mock segment color", route.getSegments().get(0).getColor());
        assertEquals("Segment description parsed badly!", "mock segment description", route.getSegments().get(0).getDescription());
        assertEquals("Segment icon URL parsed badly!", "mock segment icon url", route.getSegments().get(0).getIconUrl());
        assertEquals("Segment name parsed badly!", "mock segment name", route.getSegments().get(0).getName());
        assertEquals("Segment num of stops parsed badly!", 888, route.getSegments().get(0).getNumOfStops());
        assertEquals("Segment polyline parsed badly!", "mock segment polyline", route.getSegments().get(0).getPolylineRaw());
        assertEquals("Segment travel mode parsed badly!", "mock travel mode", route.getSegments().get(0).getTravelMode());
    }

    private void assertRouteProperties(Route<CarSharingRouteProperties> route) {
        assertNotNull("Route properties not parsed!", route.getProperties());
        assertEquals("Route property address parsed badly!", "mock properties address", route.getProperties().getAddress());
        assertEquals("Route property description parsed badly!", "mock properties description", route.getProperties().getDescription());
        assertEquals("Route property doors parsed badly!", 2, route.getProperties().getDoors());
        assertEquals("Route property engine type parsed badly!", "mock properties engine type", route.getProperties().getEngineType());
        assertEquals("Route property fuel level parsed badly!", 111, route.getProperties().getFuelLevel());
        assertEquals("Route property cleanliness parsed badly!", "mock properties cleanliness", route.getProperties().getInternalCleanliness());
        assertEquals("Route property licence plate parsed badly!", "mock properties license plate", route.getProperties().getLicencePlate());
        assertEquals("Route property model parsed badly!", "mock properties model", route.getProperties().getModel());
        assertEquals("Route property seats number parsed badly!", 1, route.getProperties().getSeats());
    }

    private void assertRouteType(Route<CarSharingRouteProperties> route) {
        assertNotNull("Route type not parsed!", route.getType());
        assertEquals("Route type parsed badly!", "car_sharing", route.getType());
    }

    private void assertPrice(Route<CarSharingRouteProperties> route) {
        assertNotNull("Price not parsed!", route.getPrice());
        assertEquals("Price currecny parsed badly!", "RSD", route.getPrice().getCurrency());
        assertEquals(999, route.getPrice().getAmount());
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }
}
