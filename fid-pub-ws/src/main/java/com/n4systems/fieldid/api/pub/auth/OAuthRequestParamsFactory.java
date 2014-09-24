package com.n4systems.fieldid.api.pub.auth;

import org.glassfish.jersey.uri.UriComponent;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.TreeSet;

/**
 * Created by kirillternovsky on 2014-09-24.
 */
public class OAuthRequestParamsFactory {
    public static OAuthRequestParams readParams(ContainerRequestContext containerRequestContext) {
        UriInfo uriInfo = containerRequestContext.getUriInfo();
        URI uri = uriInfo.getRequestUri();
        OAuthRequestParams params = new OAuthRequestParams();

        params.method(containerRequestContext.getMethod())
                .scheme(uri.getScheme())
                .host(uri.getHost())
                .port(uri.getPort())
                .path(uri.getPath());

        populateOAuthParamsFromHeader(params, containerRequestContext.getHeaderString("Authorization"));
        populateOAuthParamsFromContentBody(params, containerRequestContext.getEntityStream(), containerRequestContext.getLength());

        MultivaluedMap<String, String> getParams = UriComponent.decodeQuery(uri, true);

        for(String key : new TreeSet<>(getParams.keySet())) {
            for(String value : getParams.get(key)) {
                params.parameter(key, value);
            }
        }

        return params;
    }

    private static void populateOAuthParamsFromHeader(OAuthRequestParams params, String authHeader) {
        if(authHeader != null && authHeader.length() > 0 && authHeader.startsWith("OAuth")) {
            String[] headerParams = authHeader.split(",");

            for(String headerParam : headerParams) {
                int equalsIdx = headerParam.indexOf('=');
                String key = headerParam.substring(0, equalsIdx);
                String value;

                if(headerParam.charAt(equalsIdx + 1) == '"') {
                    value = headerParam.substring(equalsIdx + 2, headerParam.indexOf('"', equalsIdx + 2));
                } else {
                    value = headerParam.substring(equalsIdx  + 1);
                }

                params.parameter(key, value);
            }
        }
    }

    private static void populateOAuthParamsFromContentBody(OAuthRequestParams params, InputStream bodyStream, int contentLength) {
        if(contentLength > 0) {
            byte[] rawData = new byte[contentLength];
            try
            {
                //noinspection ResultOfMethodCallIgnored
                bodyStream.read(rawData, 0, contentLength);
                String rawContent = new String(rawData);
                String[] splitParams = rawContent.split("&");

                for (String splitParam : splitParams) {
                    int equalsIdx = splitParam.indexOf('=');
                    String key, value;
                    if (equalsIdx == -1) {
                        key = splitParam;
                        value = "";
                    } else {
                        key = splitParam.substring(0, equalsIdx);
                        value = splitParam.substring(equalsIdx + 1);
                    }
                    params.parameter(key, value);
                }
            }
            catch(IOException ignored) {}
        }
    }
}
