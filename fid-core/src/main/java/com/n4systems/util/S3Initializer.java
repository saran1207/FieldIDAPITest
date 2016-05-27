package com.n4systems.util;

import com.amazonaws.SDKGlobalConfiguration;
import com.n4systems.services.Initializer;

/**
 * Created by mightychip on 2016-03-07.
 */
public class S3Initializer implements Initializer {
    @Override
    public void initialize() throws Exception {
        System.setProperty(SDKGlobalConfiguration.ENABLE_S3_SIGV4_SYSTEM_PROPERTY, "true");
    }

    @Override
    public void uninitialize() {
        //Do nothing, look pretty.
    }
}
