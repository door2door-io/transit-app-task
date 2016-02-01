package com.eutechpro.allytransitapp.data.model;

import android.support.annotation.StringDef;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class Route<T> {
    @StringDef({PUBLIC_TRANSPORT, CAR_SHARING, PRIVATE_BIKE, BIKE_SHARING, TAXI})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RouteType {}

    public static final String PUBLIC_TRANSPORT = "public_transport";
    public static final String CAR_SHARING = "car_sharing";
    public static final String PRIVATE_BIKE = "private_bike";
    public static final String BIKE_SHARING = "bike_sharing";
    public static final String TAXI = "taxi";

    private String type;
    @JsonIgnore
    private Provider provider;
    private List<RouteSegment> segments;
    private T properties;
    private Price price;

    public int getDurationInMinutes(){
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ");

        RouteStop firstPoint = segments.get(0).getStops().get(0);
        DateTime firstPointDateTime = formatter.parseDateTime(firstPoint.getDateTime());

        RouteSegment lastSegment = segments.get(segments.size() - 1);
        RouteStop lastPoint = lastSegment.getStops().get(lastSegment.getStops().size() - 1);
        DateTime lastPointDateTime = formatter.parseDateTime(lastPoint.getDateTime());

        Period p = new Period(firstPointDateTime, lastPointDateTime);

        return p.getHours() * 60 + p.getMinutes();
    }
    public void setType(@RouteType String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public Provider getProvider() {
        return provider;
    }

    public T getProperties() {
        return properties;
    }

    public void setProperties(T properties) {
        this.properties = properties;
    }

    public List<RouteSegment> getSegments() {
        return segments;
    }

    public Price getPrice() {
        return price;
    }
}
