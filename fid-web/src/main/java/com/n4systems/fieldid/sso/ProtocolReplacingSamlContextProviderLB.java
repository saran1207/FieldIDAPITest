package com.n4systems.fieldid.sso;


import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.saml.context.SAMLContextProviderImpl;
import org.springframework.security.saml.context.SAMLMessageContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;
import java.net.URL;

public class ProtocolReplacingSamlContextProviderLB extends SAMLContextProviderImpl {

    static public Logger logger = LoggerFactory.getLogger(ProtocolReplacingSamlContextProviderLB.class);

    @Override
    protected void populateGenericContext(HttpServletRequest request, HttpServletResponse response, SAMLMessageContext context) throws MetadataProviderException {

        logger.info("Protocol replacing provider has Incoming request is " + request.getProtocol() + "local entity id " + context.getLocalEntityId());

        HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper(request) {
            @Override
            public StringBuffer getRequestURL() {
                StringBuffer originalRequest = super.getRequestURL();
                try {

                    URL url = new URL(originalRequest.toString());

                    logger.info(" wrapper, original url " + url.toString() + ", protocol is " + url.getProtocol() + ", host is " + url.getHost() +
                        ", path is " + url.getPath() + " file is " + url.getFile());

                    StringBuffer newUrl = new
                            StringBuffer("https" + "://" + url.getHost() + url.getPath());
                    logger.info(" new url '" + newUrl.toString() + "'");
                    return newUrl;
                }
                catch(MalformedURLException ex) {
                    System.out.println("URL IS bad " + ex);
                    return request.getRequestURL();
                }
            }
        };
        super.populateGenericContext(wrapper, response, context);

    }
}
