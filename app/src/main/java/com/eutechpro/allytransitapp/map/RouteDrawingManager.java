package com.eutechpro.allytransitapp.map;

import android.content.Context;
import android.graphics.Color;

import com.eutechpro.allytransitapp.R;
import com.eutechpro.allytransitapp.data.model.Route;
import com.eutechpro.allytransitapp.data.model.RouteSegment;
import com.eutechpro.allytransitapp.data.model.RouteStop;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.ui.IconGenerator;

import java.util.List;

public class RouteDrawingManager implements RouteDrawer {
    public static final int POLYLINE_WIDTH = 10;

    public static final int PARKING_POLYLINE_COLOR = Color.WHITE;
    public static final int CHANGE_POLYLINE_COLOR = Color.CYAN;
    public static final int DRIVING_POLYLINE_COLOR = Color.RED;
    public static final int BUS_POLYLINE_COLOR = Color.YELLOW;
    public static final int WALKING_POLYLINE_COLOR = Color.BLUE;
    public static final int DEFAULT_POLYLINE_COLOR = Color.GRAY;
    public static final int SUBWAY_POLYLINE_COLOR = Color.MAGENTA;

    public static final int STARTING_MAP_ZOOM = 13;

    private final IconGenerator iconFactory;
    private Context context;
    private GoogleMap map;

    public RouteDrawingManager(Context context) {
        iconFactory = new IconGenerator(context);
        iconFactory.setContentRotation(-90);
        iconFactory.setRotation(90);
        iconFactory.setStyle(IconGenerator.STYLE_DEFAULT);
        this.context = context;
    }

    /**
     * Before interactingwith {@link GoogleMap}, it has to be set first.
     * <br/>
     * If not, any try to interacte with {@link GoogleMap} will later result inw {@link IllegalStateException}.
     *
     * @param map {@link GoogleMap} that has to be set to this RouteDrawingManager.
     */
    @Override
    public void setMap(GoogleMap map) {
        this.map = map;
    }

    /**
     * Remove all markers and polylines from map.
     *
     * @throws IllegalStateException If {@link GoogleMap} has not been set before calling this method, {@link IllegalStateException} will be thrown.
     */
    @Override
    public void clearMap() {
        if (map == null) {
            throw new IllegalStateException("You must initialise GoogleMap first");
        }
        map.clear();
    }

    /**
     * For drawing {@link RouteSegment} with appropriate properties.
     *
     * @param route {@link RouteSegment} that has t obe drawn on the {@link GoogleMap}.
     * @throws IllegalStateException If {@link GoogleMap} has not been set before calling this method, {@link IllegalStateException} will be thrown.
     */
    @Override
    public void drawRouteOnMap(Route route) {
        if (map == null) {
            throw new IllegalStateException("You must initialise GoogleMap first");
        }
        //Get all segments
        @SuppressWarnings("unchecked")
        List<RouteSegment> segments = route.getSegments();

        //Place starting marker on the map
        map.addMarker(startingMarker(segments));

        //Iterate through segments and draw them
        for (RouteSegment segment : segments) {
            if (!segment.getTravelMode().equals(RouteSegment.TRAVEL_MODE_CHANGE)) {
                //If not Change type, add segment changin marker
                map.addMarker(breakingPointMarker(segment));
            }

            if (segment.getPolylineRaw() == null) {
                continue;
            }
            //Prepare polyline option for adequate travel mode.
            PolylineOptions drawablePolyline = polylineForSpecifficTravelType(segment.getTravelMode());

            //Decode polyline to list of LatLng and iterate through it. In each itteration, add it's location to polyline options.
            List<LatLng> polylinePoints = PolyUtil.decode(segment.getPolylineRaw());
            for (LatLng newPolylinePoint : polylinePoints) {
                drawablePolyline.add(newPolylinePoint);
            }
            //Draw polyline for this segment to the map
            map.addPolyline(drawablePolyline);
        }

        //Draw finish marker to the map
        map.addMarker(finishMarker(segments));

        //Focus map on starting point
        focusOnSpecifficStopPoint(segments.get(0).getStops().get(0));
    }

    private void focusOnSpecifficStopPoint(RouteStop startingStop) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(startingStop.getLat(), startingStop.getLng()), STARTING_MAP_ZOOM));
    }

    private MarkerOptions finishMarker(List<RouteSegment> segments) {
        RouteStop lastStop = segments.get(segments.size() - 1).getStops().get(segments.get(segments.size() - 1).getStops().size() - 1);
        return new MarkerOptions()
                .position(new LatLng(lastStop.getLat(), lastStop.getLng()))
                .title(context.getString(R.string.map_marker_finish));
    }

    private MarkerOptions startingMarker(List<RouteSegment> segments) {
        RouteStop firstStop = segments.get(0).getStops().get(0);
        return new MarkerOptions()
                .position(new LatLng(firstStop.getLat(), firstStop.getLng()))
                .title(context.getString(R.string.map_marker_start));
    }

    private MarkerOptions breakingPointMarker(RouteSegment segment) {
        LatLng point = new LatLng(segment.getStops().get(0).getLat(), segment.getStops().get(0).getLng());
        switch (segment.getTravelMode()) {
            case RouteSegment.TRAVEL_MODE_WALKING:
                return new MarkerOptions()
                        .position(point)
                        .title(context.getString(R.string.map_marker_mode_walking));
            case RouteSegment.TRAVEL_MODE_SUBWAY:
                return new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(context.getString(R.string.map_marker_mode_subway, segment.getName()))))
                        .position(point)
                        .anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());
            case RouteSegment.TRAVEL_MODE_SETUP:
                return new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(context.getString(R.string.map_marker_mode_setup))))
                        .position(point)
                        .anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());
            case RouteSegment.TRAVEL_MODE_BUS:
                return new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(context.getString(R.string.map_marker_mode_bus, segment.getName()))))
                        .position(point)
                        .anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());
            case RouteSegment.TRAVEL_MODE_DRIVING:
                return new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(context.getString(R.string.map_marker_mode_driving))))
                        .position(point)
                        .anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());
            case RouteSegment.TRAVEL_MODE_CHANGE:
                return new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(context.getString(R.string.map_marker_mode_change))))
                        .position(point)
                        .anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());
            case RouteSegment.TRAVEL_MODE_CYCLING:
                return new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(context.getString(R.string.map_marker_mode_cycling))))
                        .position(point)
                        .anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());
            case RouteSegment.TRAVEL_MODE_PARKING:
                return new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(context.getString(R.string.map_marker_mode_parking))))
                        .position(point)
                        .anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());
            default:
                return new MarkerOptions()
                        .position(point)
                        .title("unknown travel mode " + segment.getTravelMode());

        }
    }

    private PolylineOptions polylineForSpecifficTravelType(String travelType) {
        switch (travelType) {
            case RouteSegment.TRAVEL_MODE_WALKING:
                return new PolylineOptions()
                        .width(POLYLINE_WIDTH)
                        .geodesic(true)
                        .color(WALKING_POLYLINE_COLOR);
            case RouteSegment.TRAVEL_MODE_SUBWAY:
                return new PolylineOptions()
                        .width(POLYLINE_WIDTH)
                        .geodesic(true)
                        .color(SUBWAY_POLYLINE_COLOR);
            case RouteSegment.TRAVEL_MODE_SETUP:
                return new PolylineOptions()
                        .width(POLYLINE_WIDTH)
                        .geodesic(true)
                        .color(Color.BLACK);
            case RouteSegment.TRAVEL_MODE_BUS:
                return new PolylineOptions()
                        .width(POLYLINE_WIDTH)
                        .geodesic(true)
                        .color(BUS_POLYLINE_COLOR);
            case RouteSegment.TRAVEL_MODE_DRIVING:
                return new PolylineOptions()
                        .width(POLYLINE_WIDTH)
                        .geodesic(true)
                        .color(DRIVING_POLYLINE_COLOR);
            case RouteSegment.TRAVEL_MODE_CHANGE:
                return new PolylineOptions()
                        .width(POLYLINE_WIDTH)
                        .geodesic(true)
                        .color(CHANGE_POLYLINE_COLOR);
            case RouteSegment.TRAVEL_MODE_CYCLING:
                return new PolylineOptions()
                        .width(POLYLINE_WIDTH)
                        .geodesic(true)
                        .color(Color.GREEN);
            case RouteSegment.TRAVEL_MODE_PARKING:
                return new PolylineOptions()
                        .width(POLYLINE_WIDTH)
                        .geodesic(true)
                        .color(PARKING_POLYLINE_COLOR);
            default:
                return new PolylineOptions()
                        .width(POLYLINE_WIDTH)
                        .geodesic(true)
                        .color(DEFAULT_POLYLINE_COLOR);
        }
    }

}
