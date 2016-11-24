package com.fieldid.bulkarchiver;

import com.fieldid.bulkarchiver.client.asset.AssetArchiverClientService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * This application calls a web service for bulk archival of Assets... but really you could use it for any bulk calls
 * of a web service.  You'll just need to extend the BaseClient class and make an appropriate client, then call it down
 * below.
 *
 * Created by jheath on 2016-11-18.
 */
@SpringBootApplication
public class BulkArchiverApplication implements CommandLineRunner {
    private static final Logger logger = Logger.getLogger(BulkArchiverApplication.class);

    @Autowired
    private AssetArchiverClientService assetArchiver;

    @Override
    public void run(String... args) throws Exception {
        logger.info("Let's archive some assets!!");
        if(!(args.length >= 1 && args.length <= 2)) {
            logger.error("You have not provided the necessary arguments... I cannot run.  Goodbye!!");
            for (String arg : args) {
                logger.info(arg);
            }
        } else {
            logger.warn("Running with file " + args[0]);

            List<Long> unarchivedAssets = assetArchiver.archiveAssetsFromFile(args[0]);
            Path path = Paths.get("/tmp/unarchivedAssets.out");

            try(BufferedWriter writer = Files.newBufferedWriter(path)) {
                for (Long unarchivedAsset : unarchivedAssets) {
                    writer.write(String.format("%d", unarchivedAsset));
                    writer.newLine();
                }
            }
        }
    }

    public static void main(String... args) {
        //The idea here is that we run and then immediately close the application.  This allows us to use all that fancy
        //Spring Boot trim without worrying about manually "logging in" and killing the process when complete.
        new SpringApplicationBuilder(BulkArchiverApplication.class)
                .run(args)
                //When it's finished running, we kill it... this is more a tool than it is an application.
                .close();
    }
}
