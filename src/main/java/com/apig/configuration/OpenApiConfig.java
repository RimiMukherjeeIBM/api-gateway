package com.apig.configuration;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springdoc.core.SwaggerUiConfigParameters;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class OpenApiConfig {
    public <SwaggerUiConfig> OpenApiConfig(RouteLocator locator, SwaggerUiConfigParameters swaggerUiConfig) {

        List<Route> routes = locator.getRoutes();

        //deduplication
        Collection<Route> distinctRoutes = routes.stream()
                .collect(Collectors.toMap(Route::getLocation, p -> p, (p, q) -> p)).values();

        //add the service name to the drop-down box
        distinctRoutes.stream().filter(route -> !"apis".equalsIgnoreCase(route.getId())).forEach(route -> {
            swaggerUiConfig.addGroup(route.getId(), route.getId());
        });
    }
}
