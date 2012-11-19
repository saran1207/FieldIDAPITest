package com.n4systems.fieldid.servlets;

import com.google.common.base.Preconditions;
import com.n4systems.fieldid.service.amazon.S3Service;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;

/**
 * usage  :
 * used as a bridge to get S3/Amazon images.  recall that S3 Url's have key and expiry key so we need to dynamically generate them.
 * @see com.n4systems.fieldid.wicket.components.richText.RichText
 * @see S3Service
 *
 * request parameters =
 *  - path                                  full path including tenant        e.g. /tenants/12345/users/6883/images/photo.png
 */
public class ImageDownloadHandler implements HttpRequestHandler {

    private static final Logger logger= Logger.getLogger(ImageDownloadHandler.class);

    private @Autowired S3Service s3Service;

    public ImageDownloadHandler() {
    }

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Preconditions.checkState(request.getParameter("path")!=null,"you must specify a valid s3 image path");

        String path = request.getParameter("path");

        try {
            URL url = s3Service.generateResourceUrl(path);
            response.sendRedirect(url.toExternalForm());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public static String urlFor(String path) {
        return "/fieldid/imageDownload/image?path="+path;
    }
}
