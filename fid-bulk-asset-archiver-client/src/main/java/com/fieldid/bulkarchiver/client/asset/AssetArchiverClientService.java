package com.fieldid.bulkarchiver.client.asset;

import com.fieldid.bulkarchiver.client.BaseClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This Client consumes the bulk Asset archival service.  Basically, it just takes a page of Asset IDs and rams them
 * against the bulk archival service.
 *
 * Created by jheath on 2016-11-21.
 */
@Service
public class AssetArchiverClientService extends BaseClient {
    private static final Logger logger = Logger.getLogger(AssetArchiverClientService.class);

    private static final String ASSET_PATH = "asset";
    private static final String ARCHIVER_PATH = "/secretAssetDeleter/pleaseDelete";

    @Autowired
    public AssetArchiverClientService(@Value("${fieldid.webservice.endpoint}") String baseUrl,
                                      @Value("${fieldid.webservice.authkey}") String authKey) {
        super(baseUrl, authKey);

        logger.info("Authkey is " + authKey);
    }

    public List<Long> archiveAssetsFromFile(String path) {
        List<String> idList = new ArrayList<>();
        try {
            idList = Files.readAllLines(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Iterator<String> stringerator = idList.iterator();
        List<String> idPage = new ArrayList<>();

        List<Long> notArchived = new ArrayList<>();

        int count = 0;

        while(stringerator.hasNext()) {
            if(count >= 100) {
                List<Long> returnedIDs = archiveAssetPage(idPage);
                if(returnedIDs != null) notArchived.addAll(returnedIDs);
                idPage = new ArrayList<>();
                count = 0;
            }
            count++;
            idPage.add(stringerator.next());
        }

        if(!idPage.isEmpty()) {
            List<Long> returnedIDs = archiveAssetPage(idPage);
            if(returnedIDs != null) notArchived.addAll(returnedIDs);
        }

        return notArchived;
    }

    private List<Long> archiveAssetPage(List<String> assetIDs) {
        Response result = createClientForResource((ASSET_PATH + ARCHIVER_PATH))
                                .request()
                                .put(Entity.entity(assetIDs, MediaType.APPLICATION_JSON_TYPE));

        //NOTE: If I hadn't decided to be a smartass and provide a non-standard response code of 420 ("I'm a little
        //      teapot" is not an official response code), we could have used this more human-readable method of
        //      checking the response code... you'd use the Response.Status enum in a switch statement, instead and
        //      would use its list of valid values as your case statements.  VERY readable... I've provided a tiny
        //      example to show this:
//        Response.Status status = Response.Status.fromStatusCode(result.getStatus());
//        switch(status) {
//            case OK:
//                //Handle OK response...
//                break;
//            case ACCEPTED:
//                //Handle ACCEPTED response...
//                break;
//            case BAD_GATEWAY:
//                //Handle BAD_GATEWAY response...
//                break;
//            default:
//                //These are just a few of the potential response codes...
//        }


        switch(result.getStatus()) {
            case 420:
                return handlePartialArchival(result);
            case 200:
                logger.info("Successfully archived all assets!!");
                return null;
            default:
                logger.warn("There was an unexpected Status Code (" + result.getStatus() + ") returned from the Service!");
                logger.warn(result.getEntity());
                return null;
        }
    }

    @SuppressWarnings("unchecked") //Shhhh... it's safe.
    private List<Long> handlePartialArchival(Response response) {
        logger.info("The response MediaType was " + response.getMediaType().toString());
        List<String> idList = (List<String>)response.getEntity();
        List<Long> unarchivedAssets = idList.stream().map(Long::parseLong).collect(Collectors.toList());

        logger.info("There were " + unarchivedAssets.size() + " Asset IDs returned... I don't think those could be archived!!");

        return unarchivedAssets;
    }
}
