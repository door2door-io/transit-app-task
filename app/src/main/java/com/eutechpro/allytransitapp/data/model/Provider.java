package com.eutechpro.allytransitapp.data.model;

import android.support.annotation.StringDef;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class Provider {
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @StringDef({VBB, DRIVE_NOW, CAR_2_GO, GOOGLE, NEXT_BIKE, CALL_A_BIKE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ProviderType {}

    public static final String VBB         = "vbb";
    public static final String DRIVE_NOW   = "drivenow";
    public static final String CAR_2_GO    = "car2go";
    public static final String GOOGLE      = "google";
    public static final String NEXT_BIKE   = "nextbike";
    public static final String CALL_A_BIKE = "callabike";

    private String providerType;
    @JsonProperty("display_name")
    private String displayName;
    private String disclaimer;
    @JsonProperty("provider_icon_url")
    private String providerIconUrl;
    @JsonProperty("ios_itunes_url")
    private String iosItunesUrl;
    @JsonProperty("ios_app_url")
    private String iosAppUrl;
    @JsonProperty("android_package_name")
    private String androidPackageName;

    public void setProviderType(String providerType) {
        this.providerType = providerType;
    }

    public String getProviderType() {
        return providerType;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    public String getProviderIconUrl() {
        return providerIconUrl;
    }

    public String getIosItunesUrl() {
        return iosItunesUrl;
    }

    public String getIosAppUrl() {
        return iosAppUrl;
    }

    public String getAndroidPackageName() {
        return androidPackageName;
    }
}
