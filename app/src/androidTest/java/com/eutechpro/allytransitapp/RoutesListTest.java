package com.eutechpro.allytransitapp;

import android.support.test.rule.ActivityTestRule;
import android.support.v4.app.FragmentTransaction;

import com.eutechpro.allytransitapp.data.model.Price;
import com.eutechpro.allytransitapp.data.model.Provider;
import com.eutechpro.allytransitapp.data.model.Route;
import com.eutechpro.allytransitapp.data.model.RouteSegment;
import com.eutechpro.allytransitapp.search.RoutesListFragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class RoutesListTest {

    @Rule
    public ActivityTestRule<DevActivity> activityRule = new ActivityTestRule<>(DevActivity.class);
    private RoutesListFragment fragment;
    private DevActivity activity;

    @Before
    public void setUp() throws Exception {
        activity = activityRule.getActivity();
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();

        fragment = new RoutesListFragment();
        transaction.add(R.id.testing_fragment_container, fragment);

        transaction.commit();

    }

    @Test
    public void testDrawingList() throws Exception {
        final List<Route> routes = Mockito.mock(List.class);

        Route route = Mockito.mock(Route.class);
        List<RouteSegment> segments = Mockito.mock(List.class);
        Provider provider = Mockito.mock(Provider.class);
        Price price = Mockito.mock(Price.class);

        Mockito.when(provider.getProviderIconUrl()).thenReturn("");
        Mockito.when(route.getDurationInMinutes()).thenReturn(99);
        Mockito.when(routes.size()).thenReturn(1);
        Mockito.when(routes.get(0)).thenReturn(route);
        Mockito.when(segments.size()).thenReturn(1);
        Mockito.when(route.getSegments()).thenReturn(segments);
        Mockito.when(price.getAmount()).thenReturn(270);
        Mockito.when(route.getPrice()).thenReturn(price);
        Mockito.when(provider.getDisplayName()).thenReturn("Test provider name");
        Mockito.when(route.getProvider()).thenReturn(provider);

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fragment.setContent(routes);
            }
        });

        onView(withId(R.id.route_type)).check(matches(isDisplayed()));
        onView(withId(R.id.route_type)).check(matches(withText("Test provider name")));

        onView(withId(R.id.route_duration)).check(matches(isDisplayed()));
        onView(withId(R.id.route_duration)).check(matches(withText("Duration: 99 min")));

        onView(withId(R.id.route_price)).check(matches(isDisplayed()));
        onView(withId(R.id.route_price)).check(matches(withText("Price: 2.7 €")));
    }
}
