package com.apig.filters;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

public class PreFilter extends ZuulFilter{

	@Override
	public Object run() throws ZuulException {
		// TODO Auto-generated method stub
		RequestContext ctx = RequestContext.getCurrentContext();
	    HttpServletRequest request = ctx.getRequest();
		System.out.println("**Route host***" + ctx.getRouteHost());
	 /*
		final String authHeader = request.getHeader("Authorization");

		final Enumeration<String> headers = request.getHeaderNames();
		
		System.out.println("********within do filter1***********");

		// We get the Authorization Header of the incoming request
		
		System.out.println("********within do filter2***********");

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			throw new ZuulException(new Exception(), 401, "Not a valid authentication authHeader");
		}

		// and retrieve the token
		String compactJws = authHeader.substring(7);

		try {
	           final Claims claims = Jwts.parser().setSigningKey("secretkey").parseClaimsJws(compactJws).getBody();
	            request.setAttribute("claims", claims);
		} catch (SignatureException ex) {
		   // HttpServletResponse hsr = new Http;
		   // hsr.setStatus(401);
			throw new ZuulException(new Exception(), 401, "Invalid Token");
		} catch (MalformedJwtException ex) {
		   // HttpServletResponse hsr = (HttpServletResponse) response;
		   // hsr.setStatus(401);
			throw new ZuulException(new Exception(), 401, "JWT is malformed");
		}*/

	    System.out.println("Request Method : " + request.getMethod() + " Request URL : " + request.getRequestURL().toString());
	    return null;
	  }
	
	private void sendResponse(int responseCode, String responseBody) {
		final RequestContext ctx = RequestContext.getCurrentContext();
		ctx.setResponseBody(responseBody);
		ctx.setResponseStatusCode(responseCode);


		try {
			ctx.getResponse().sendError(responseCode, responseBody);
		} catch (IOException e) {
			//log.error(e.getMessage(), e);
		}
	}

	@Override
	public boolean shouldFilter() {
		// TODO Auto-generated method stub
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
		return "pre";
	}

}
