package com.n4systems.fieldid.sso;


import com.n4systems.services.config.ConfigService;
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

/**
 * Spring SAML SSO checks that the url on the request matches the expected one. A problem arises when a proxy removes the
 * https: and replaces it by http:.  This occurs when Tomcat is behind a proxy that receives the https message which converts
 * it to a http message for use by Tomcat. Normally this works fine but Spring SAML SSO only sees the url received by tomcat
 * (which is http) and compares it what it expects (which is the external url with https) leading to a mismatch in urls
 * and an authentication failure. So we replace the protocol with one specified in the config. This allows us to specify
 * https for those environments where we have https and to specify http for environments such as dev which use http.
 */
public class ProtocolReplacingSamlContextProviderLB extends SAMLContextProviderImpl {

    static public Logger logger = LoggerFactory.getLogger(ProtocolReplacingSamlContextProviderLB.class);

    @Override
    protected void populateGenericContext(HttpServletRequest request, HttpServletResponse response, SAMLMessageContext context) throws MetadataProviderException {

        final String samlProtocol = ConfigService.getInstance().getConfig().getSystem().getSsoSamlProtocol();

        HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper(request) {
            @Override
            public StringBuffer getRequestURL() {
                StringBuffer originalRequest = super.getRequestURL();
                try {

                    URL url = new URL(originalRequest.toString());
                    StringBuffer newUrl = new
                            StringBuffer(samlProtocol + "://" + url.getHost() + url.getPath());
                    return newUrl;
                }
                catch(MalformedURLException ex) {
                    logger.error("Unable to rewrite protocol in SAML url" + ex);
                    return request.getRequestURL();
                }
            }
        };
        super.populateGenericContext(wrapper, response, context);

    }
}
