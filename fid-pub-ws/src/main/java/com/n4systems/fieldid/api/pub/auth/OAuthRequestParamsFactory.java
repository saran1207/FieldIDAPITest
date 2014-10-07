package com.n4systems.fieldid.api.pub.auth;

import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.uri.UriComponent;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import javax.xml.ws.WebServiceException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.TreeSet;

public class OAuthRequestParamsFactory {
    public static OAuthRequestParams readParams(ContainerRequestContext containerRequestContext) throws IOException {
        UriInfo uriInfo = containerRequestContext.getUriInfo();
        URI uri = uriInfo.getRequestUri();
        OAuthRequestParams params = new OAuthRequestParams();

        params.method(containerRequestContext.getMethod())
                .scheme(uri.getScheme())
                .host(uri.getHost())
                .port(uri.getPort())
                .path(uri.getPath());

        populateOAuthParamsFromHeader(params, containerRequestContext.getHeaderString("Authorization"));
        String contentLengthHeader = containerRequestContext.getHeaderString("Content-Length");
        if(contentLengthHeader != null &&
                containerRequestContext.getMediaType().equals(MediaType.APPLICATION_FORM_URLENCODED_TYPE)) {
            populateOAuthParamsFromContentBody(params, readBodyStream(containerRequestContext));
        }

        MultivaluedMap<String, String> getParams = UriComponent.decodeQuery(uri, true);

        for(String key : new TreeSet<>(getParams.keySet())) {
            for(String value : getParams.get(key)) {
                params.parameter(key, value);
            }
        }

        return params;
    }

	private static byte[] readBodyStream(ContainerRequestContext containerRequestContext) {
		byte[] bytes;
		if (containerRequestContext.getLength() > 0) {
			try {
				bytes = IOUtils.toByteArray(containerRequestContext.getEntityStream());
				containerRequestContext.setEntityStream(new ByteArrayInputStream(bytes));
			} catch (IOException e) {
				throw new WebServiceException(e);
			}
		} else {
			bytes = new byte[0];
		}
		return bytes;
	}

    private static void populateOAuthParamsFromHeader(OAuthRequestParams params, String authHeader) {
        if(authHeader != null && authHeader.length() > 0 && authHeader.startsWith("OAuth")) {
            authHeader = authHeader.substring(authHeader.indexOf("oauth_"));
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

                try
                {
                    params.parameter(key.trim(), URLDecoder.decode(value.trim(), "UTF-8"));
                }
                catch(UnsupportedEncodingException ignored) {} // no way

            }
        }
    }

    private static void populateOAuthParamsFromContentBody(OAuthRequestParams params, byte[] body) {
        if(body.length > 0) {
			//noinspection ResultOfMethodCallIgnored
			String rawContent = new String(body);
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
    }
}
