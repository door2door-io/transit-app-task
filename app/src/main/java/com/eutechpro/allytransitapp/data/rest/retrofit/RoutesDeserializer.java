package com.eutechpro.allytransitapp.data.rest.retrofit;

import android.support.annotation.NonNull;

import com.eutechpro.allytransitapp.data.model.properties.BikeSharingRouteProperties;
import com.eutechpro.allytransitapp.data.model.properties.CarSharingRouteProperties;
import com.eutechpro.allytransitapp.data.model.Provider;
import com.eutechpro.allytransitapp.data.model.Route;
import com.eutechpro.allytransitapp.data.model.properties.TaxiRouteProperties;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom deserializer for complex JSON response.
 * <br/>
 * The idea is to parse route properties depending on route type and attach it to {@link Route} object.
 * Also, {@link Provider} object hast to be parsed and attached to the {@link Route}.
 */
public class RoutesDeserializer extends JsonDeserializer<RoutesResponse> {
    private final ObjectMapper objectMapper;
    private ObjectNode providersNode;

    public RoutesDeserializer() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
    }

    @Override
    @SuppressWarnings("unchecked")
    public RoutesResponse deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode mainNode = jp.getCodec().readTree(jp);
        if (mainNode == null) {
            return null;
        }
        providersNode = (ObjectNode) mainNode.get("provider_attributes");
        ArrayNode routesNodes = (ArrayNode) mainNode.get("routes");

        List<Route> routes = new ArrayList<>();
        for (Object node : routesNodes) {
            ObjectNode routeNode = (ObjectNode) node;
            if (routeNode.get("type").toString().contains("car_sharing")) {
                Route<CarSharingRouteProperties> route = generateCarSharingRoute(routeNode);
                routes.add(route);
            } else if (routeNode.get("type").toString().contains("bike_sharing")) {
                Route<BikeSharingRouteProperties> route = generateBikeSharingRoute(routeNode);
                routes.add(route);
            } else if (routeNode.get("type").toString().contains("taxi")) {
                Route<TaxiRouteProperties> route = generateTaxiRoute(routeNode);
                routes.add(route);
            } else if (routeNode.get("type").toString().contains("public_transport")) {
                Route route = generatePublicTransportRoute(routeNode);
                routes.add(route);
            } else if (routeNode.get("type").toString().contains("private_bike")) {
                Route route = generatePrivateBikeRoute(routeNode);
                routes.add(route);
            } else {
                Route route = objectMapper.treeToValue(routeNode, Route.class);
                routes.add(route);
            }

            System.out.println();
        }
        RoutesResponse routesResponse = new RoutesResponse();
        routesResponse.routes = routes;
        return routesResponse;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    private Route<TaxiRouteProperties> generateTaxiRoute(ObjectNode routeNode) throws com.fasterxml.jackson.core.JsonProcessingException {
        TaxiRouteProperties properties = objectMapper.treeToValue(routeNode.get("properties"), TaxiRouteProperties.class);
        Route<TaxiRouteProperties> route = objectMapper.treeToValue(routeNode, Route.class);
        route.setProperties(properties);

        Provider provider = extractProvider(routeNode);
        if(provider.getDisplayName() == null){
            provider.setDisplayName("Taxi");
        }
        route.setProvider(provider);

        return route;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    private Route<BikeSharingRouteProperties> generateBikeSharingRoute(ObjectNode routeNode) throws com.fasterxml.jackson.core.JsonProcessingException {
        BikeSharingRouteProperties properties = objectMapper.treeToValue(routeNode.get("properties"), BikeSharingRouteProperties.class);
        Route<BikeSharingRouteProperties> route = objectMapper.treeToValue(routeNode, Route.class);
        route.setProperties(properties);

        Provider provider = extractProvider(routeNode);
        route.setProvider(provider);

        return route;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    private Route<CarSharingRouteProperties> generateCarSharingRoute(ObjectNode routeNode) throws com.fasterxml.jackson.core.JsonProcessingException {
        CarSharingRouteProperties properties = objectMapper.treeToValue(routeNode.get("properties"), CarSharingRouteProperties.class);
        Route<CarSharingRouteProperties> route = objectMapper.treeToValue(routeNode, Route.class);
        route.setProperties(properties);

        Provider provider = extractProvider(routeNode);
        route.setProvider(provider);

        return route;
    }

    @NonNull
    private Route generatePublicTransportRoute(ObjectNode routeNode) throws com.fasterxml.jackson.core.JsonProcessingException {
        Route route = objectMapper.treeToValue(routeNode, Route.class);

        Provider provider = extractProvider(routeNode);
        route.setProvider(provider);
        if(provider.getDisplayName() == null){
            provider.setDisplayName("VBB");
        }
        return route;
    }

    @NonNull
    private Route generatePrivateBikeRoute(ObjectNode routeNode) throws com.fasterxml.jackson.core.JsonProcessingException {
        Route route = objectMapper.treeToValue(routeNode, Route.class);

        Provider provider = extractProvider(routeNode);
        route.setProvider(provider);
        if (provider.getDisplayName() == null) {
            provider.setDisplayName("Personal bicycle");
        }
        return route;
    }
    @NonNull
    private Provider extractProvider(ObjectNode routeNode) throws com.fasterxml.jackson.core.JsonProcessingException {
        Provider provider = objectMapper.treeToValue(providersNode.get(routeNode.get("provider").toString().replace("\"", "")), Provider.class);
        provider.setProviderType(routeNode.get("provider").toString().replace("\"",""));
        return provider;
    }
}
