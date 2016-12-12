package com.fieldid.bulkarchiver.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

/**
 * This is the base REST Client, which just takes care of any basic activity that happens the same, regardless of the
 * end client.  Typically this is stuff like building the endpoint URL or providing access to the authKey.  Since these
 * values would never change, they're set as final.
 *
 * Created by jheath on 2016-11-21.
 */
public class BaseClient {
    private final String baseUrl;
    private final String authKey;

    public BaseClient(String baseUrl, String authKey) {
        this.baseUrl = baseUrl;
        this.authKey = authKey;
    }

    /**
     * Create a Client pointed at the desired target and resource, with the expected authKey in a query parameter named
     * "k".
     *
     * @param resourcePath A String valu representing the path of the resource to be consumed.
     * @return A WebTarget representing the Client, already calibrated with the "k" parameter, endpoint and resource.
     */
    protected WebTarget createClientForResource(String resourcePath) {
        return ClientBuilder.newClient().target(baseUrl).path(resourcePath).queryParam("k", authKey);
    }
}
