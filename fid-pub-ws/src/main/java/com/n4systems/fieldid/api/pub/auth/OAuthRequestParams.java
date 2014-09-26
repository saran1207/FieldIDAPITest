package com.n4systems.fieldid.api.pub.auth;

import com.google.common.base.Preconditions;

import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by kirillternovsky on 2014-09-23.
 */
public class OAuthRequestParams {
    private List<AbstractMap.SimpleEntry<String, String>> parameters;
    private String method;
    private String scheme;
    private String host;
    private String path;
    private int port;
    private boolean hasPort;
    private boolean isSorted;
    private static ParamComparator comparator = new ParamComparator();
    private OAuthParams oauthParams;

    public OAuthRequestParams() {
        parameters = new ArrayList<>();
        oauthParams = new OAuthParams();
        hasPort = false;
        isSorted = false;
    }

    public OAuthRequestParams method(String method) {
        Preconditions.checkNotNull(method);
        this.method = method;
        return this;
    }

    public OAuthRequestParams scheme(String scheme) {
        Preconditions.checkNotNull(scheme);
        this.scheme = scheme;
        return this;
    }

    public OAuthRequestParams host(String host) {
        Preconditions.checkNotNull(host);
        this.host = host;
        return this;
    }

    public OAuthRequestParams port(int port) {
        this.port = port;
        hasPort = port != 0;
        return this;
    }

    public OAuthRequestParams path(String path) {
        this.path = path;
        return this;
    }

    public OAuthRequestParams parameter(String key, String value) {
        isSorted = false;

        if("realm".equals(key)) {
            return this;
        }

        if(key.startsWith("oauth_")) {
            switch (key) {
                case "oauth_consumer_key":
                    oauthParams.setConsumerKey(value);
                    parameters.add(new AbstractMap.SimpleEntry<>(key, value));
                    break;
                case "oauth_token":
                    oauthParams.setTokenKey(value);
                    parameters.add(new AbstractMap.SimpleEntry<>(key, value));
                    break;
                case "oauth_signature_method":
                    oauthParams.setSignatureMethod(value);
                    parameters.add(new AbstractMap.SimpleEntry<>(key, value));
                    break;
                case "oauth_nonce":
                    oauthParams.setNonce(value);
                    parameters.add(new AbstractMap.SimpleEntry<>(key, value));
                    break;
                case "oauth_timestamp":
                    oauthParams.setTimestamp(value);
                    parameters.add(new AbstractMap.SimpleEntry<>(key, value));
                    break;
                case "oauth_signature":
                    oauthParams.setSignature(value);
                    break;
                case "oauth_version":
                    oauthParams.setVersion(value);
                    parameters.add(new AbstractMap.SimpleEntry<>(key, value));
                    break;
            }
        } else {
            parameters.add(new AbstractMap.SimpleEntry<>(key, value));
        }

        return this;
    }

    public String getSignatureString() {
        if(!isSorted) {
            parameters.sort(comparator);
            isSorted = true;
        }

        StringBuilder sigBuilder = new StringBuilder();

        sigBuilder.append(method);
        sigBuilder.append('&');
        sigBuilder.append(OAuthEncoder.encode(scheme + "://"));
        sigBuilder.append(OAuthEncoder.encode(host));

        if(hasPort && (("http".equals(scheme) && port != 80) ||
                    ("https".equals(scheme) && port != 443))) {

            sigBuilder.append("%3A");
            sigBuilder.append(port);
                    }

        if(this.path != null) {
            sigBuilder.append(OAuthEncoder.encode(path));
        }

        StringBuilder paramListBuilder = new StringBuilder();
        for(AbstractMap.SimpleEntry<String, String> entry : parameters) {
            if("oauth_signature".equals(entry.getKey()) || "OAuth realm".equals(entry.getKey()))
                continue;

            paramListBuilder.append('&');
            paramListBuilder.append(entry.getKey());
            paramListBuilder.append('=');
            paramListBuilder.append(entry.getValue());
        }

        sigBuilder.append('&');
        sigBuilder.append(OAuthEncoder.encode(paramListBuilder.toString().substring(1)));

        return sigBuilder.toString();
    }

    public OAuthParams getOAuthParams() {
        return oauthParams;
    }

    private static class ParamComparator implements Comparator<AbstractMap.SimpleEntry<String, String>> {

        @Override
        public int compare(SimpleEntry<String, String> p1,
                SimpleEntry<String, String> p2)
        {
            int diff = p1.getKey().compareTo(p2.getKey());

            if(diff == 0) {
                diff = p2.getValue().compareTo(p2.getValue());
            }

            return diff;
        }
    }
}
