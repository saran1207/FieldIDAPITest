package com.n4systems.fieldid.ws.v1.resources.minimumversion;

import com.n4systems.services.config.ConfigService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;

/**
 * Created by rrana on 2018-03-19.
 */

@Component
@Path("/minimumVersion")
public class ApiMinimumVersionResource {

    protected static String ERROR_MESSAGE = "Your current application is not the latest version.  Please update the application and try again.";
    protected static String SUCCESS_MESSAGE = "Ok!";

    @Autowired
    protected ConfigService configService;

    private Logger logger = Logger.getLogger(ApiMinimumVersionResource.class);

    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String minimumVersion(@QueryParam("tag") String tag) {
        String minimumVersion = configService.getConfig().getSystem().getMinimumAppVersion();

        String[] tagSplit = tag.split("\\.");
        String[] minimumVersionSplit = minimumVersion.split("\\.");

        try {
            Date app = new Date(Integer.valueOf(tagSplit[0]), Integer.valueOf(tagSplit[1]), Integer.valueOf(tagSplit[2]));
            Date minVersion = new Date(Integer.valueOf(minimumVersionSplit[0]), Integer.valueOf(minimumVersionSplit[1]), Integer.valueOf(minimumVersionSplit[2]));

                if(app.compareTo(minVersion) >= 0){
                    return SUCCESS_MESSAGE;
                } else {
                    return ERROR_MESSAGE;
                }
        } catch (Exception e) {
            logger.error(String.format("There was an error determining minimum versions with tag: [%s] and minimumAppVersion: [%s]", tag, minimumVersion));
            return ERROR_MESSAGE;
        }
    }
}
