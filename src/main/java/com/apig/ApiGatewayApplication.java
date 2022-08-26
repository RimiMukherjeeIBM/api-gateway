package com.apig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.apig.filters.ErrorFilter;
import com.apig.filters.PostFilter;
import com.apig.filters.PreFilter;
import com.apig.filters.RouteFilter;
import com.apig.jwtfilter.JwtFilter;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@SpringBootApplication
@EnableZuulProxy
//@EnableEurekaClient
/*@OpenAPIDefinition(servers = @Server(url = "${zuul.routes.apis.url}"),
		info = @Info(title = "rws-service-test", description = "REST API Endpoints",
		termsOfService = "http://www-03.ibm.com/software/sla/sladb.nsf/sla/bm?Open",
		contact = @Contact(name = "portofrotterdam",
				email = "portofrotterdam@nl.ibm.com"),
		license = @License(name = "Apache License Version 2.0", url = "LICENSE URL"), version = "1.0"))*/
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

	@Bean
	public PreFilter preFilter() {
	    return new PreFilter();
	}
	@Bean
	public PostFilter postFilter() {
	    return new PostFilter();
	}
	@Bean
	public ErrorFilter errorFilter() {
	    return new ErrorFilter();
	}
	@Bean
	public RouteFilter routeFilter() {
	    return new RouteFilter();
	}
	
	/**
	 * Define the bean for Filter registration. Create a new FilterRegistrationBean
	 * object and use setFilter() method to set new instance of JwtFilter object.
	 * Also specifies the Url patterns for registration bean.
	 */
	@Bean
	public FilterRegistrationBean jwtFilter() {
    final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
    // Set the filter on this bean
    registrationBean.setFilter(new JwtFilter("supersecret"));
    // Set the relevant URL pattern
    registrationBean.addUrlPatterns("/api/v1/order/*", "/api/v1/user/*");
    return registrationBean;
	}
	
	@Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Collections.singletonList("*"));
        config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH"));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

	@Autowired
	RouteLocator locator;

	/*@Bean
	public List<GroupedOpenApi> apis() {
		final List<GroupedOpenApi> groups = new ArrayList<>();
		List<Route> definitions = locator.getRoutes();
		definitions.stream().forEach(route -> {
			groups.add(GroupedOpenApi.builder().pathsToMatch(route.getPath()).group(route.getId()).build());
		});
		return groups;
	}*/
}
