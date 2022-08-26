package com.apig.filters;

import javax.servlet.http.HttpServletRequest;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

public class RouteFilter extends ZuulFilter{

	private void setOpenApiPath(){
		String requestURI = RequestContext.getCurrentContext().getRequest().getRequestURI();

		if (requestURI.contains("/api-docs")) {
			//extract the last one by the way, that the service name
			String servicePattern = "/v3/api-docs/(?<group>.+)";
			Pattern compile = Pattern.compile(servicePattern);
			Matcher matcher = compile.matcher(requestURI);

			String group = "";
			while (matcher.find()) {
				group = matcher.group("group");
			}

			//Rewrite routing
			String path = "/" + group + "/v3/api-docs";

			RequestContext context = RequestContext.getCurrentContext();
			context.put(FilterConstants.REQUEST_URI_KEY, path);
			System.out.println(context.getRequest().getRequestURI());
		}

	}

	@Override
	public Object run() throws ZuulException {
		// TODO Auto-generated method stub
		System.out.println("Inside Route Filter");

		setOpenApiPath();

	    return null;
	  }

	@Override
	public boolean shouldFilter() {
		// TODO Auto-generated method stub
		String requestURI = RequestContext.getCurrentContext().getRequest().getRequestURI();
		System.out.println("****requestURI*****" + requestURI);
		//return requestURI.matches("/v3/api-docs/.+") && !requestURI.matches("/v3/api-docs/swagger-config");
		return true;
	}

	@Override
	public int filterOrder() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public String filterType() {
		// TODO Auto-generated method stub
		return "route";
	}

}
