package com.n4systems.fieldid.servlets;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.n4systems.fieldid.service.amazon.S3Service;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpRequestHandler;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 * spring bean that is coupled with the servlet of the same name. it simply delegates to this class.
 * @see ImageUploadServlet
 *
 * usage  :
 * originally written to be used with @RichText component. all image uploads hit this servlet because it's configured the upload url to point to here.
 * by default, returns a json object with a url to the medium sized image that the RichText component knows how to handle.
 * @see com.n4systems.fieldid.wicket.components.richText.RichText
 *
 * request parameters =
 *  - tenantId                                  required.
 *  - path                                      e.g.   "/events/images/"    should end with a slash
 */
public class ImageUploadHandler implements HttpRequestHandler {

    private static final Logger logger= Logger.getLogger(ImageUploadHandler.class);

    private @Autowired S3Service s3Service;

    public ImageUploadHandler() {
    }

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Preconditions.checkState(request.getParameter("tenantId")!=null,"you must specify a tenantId when uploading an image");
        Preconditions.checkState(request.getParameter("path")!=null,"you must specify a path when uploading an image");

        ServletFileUpload upload = new ServletFileUpload();
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();

        try {
            FileItemIterator iterator = upload.getItemIterator(request);
            while(iterator.hasNext()) {
                FileItemStream item = iterator.next();
                if(!item.isFormField()) {
                    JsonImage jsonObject = createJsonObjectForItem(request, item);
                    response.setContentType("text/html");
                    response.setStatus(HttpStatus.SC_OK);
                    response.getWriter().write(gson.toJson(jsonObject));
                    break;
                }
            }
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_BAD_REQUEST);
            logger.error(e.getMessage());
        }

    }

    private JsonImage createJsonObjectForItem(HttpServletRequest request, FileItemStream item) throws IOException {
        Long tenantId = Long.parseLong(request.getParameter("tenantId"));
        String path = request.getParameter("path");

        InputStream stream = item.openStream();
        BufferedImage bufferedImage = ImageIO.read(stream);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, getFormat(item), baos);
        // CAVEAT : it is possible that name collisions could happen but i'll let the call handle that if need be.
        // (s3 service will just override  existing image).
        // if important, the path parameter should have some unique token inside it.   (e.g. foo-17899364901902367543.png instead of foo.png)
        S3Service.S3ImagePath imagePath = s3Service.uploadImage(baos.toByteArray(), item.getContentType(), createFilePath(path, item), tenantId);
        return new JsonImage(imagePath, bufferedImage, baos.size(), request);
    }

    private String getFormat(FileItemStream item) {
        String contentType = item.getContentType();
        if (contentType.startsWith("image/")) {
            contentType = contentType.substring("image/".length());
        } else {
            logger.warn("can't figure out proper file format for item " + item.getName() + " with type " + item.getContentType());
        }
        if (contentType.startsWith("x-")) {
            return contentType.substring(2);    // convert say, "x-png" to "png".
        }
        return contentType;
    }

    private String createFilePath(String path, FileItemStream item) {
        if (!path.endsWith("/")&&!path.endsWith("\\") ) {
            path = path+"/";
            logger.warn("appending '/' on to upload image path '"+path+"'");
        }
        path = path + item.getName();
        return path;
    }



    class JsonImage implements Serializable {
        private Upload upload = new Upload();

        JsonImage(S3Service.S3ImagePath imagePath, BufferedImage bufferedImage, int size, HttpServletRequest request) {
            String type = request.getParameter("type");
            upload.links.large_thumbnail = makeImageDownloadUrl(request, imagePath.getThumbnailPath());
            upload.links.original = makeImageDownloadUrl(request, imagePath.getMediumPath());
            upload.image.hash = hashCode() + "";
            upload.image.width = bufferedImage.getWidth();
            upload.image.height = bufferedImage.getHeight();
            upload.image.size = size;
            upload.image.datetime = new Date().toString();
        }
    }

    private String makeImageDownloadUrl(HttpServletRequest request, String path) {
        try {
            URL url = new URL(request.getScheme(),
                    request.getServerName(),
                    request.getServerPort(),
                    ImageDownloadHandler.urlFor(path));
            return url.toExternalForm();
        } catch (MalformedURLException e) {
            logger.error("can't generate url for '" + path + "'");
        }
        return null;
    }

    class Upload implements Serializable {
        Image image = new Image();
        Links links = new Links();
    }

    class Image implements Serializable {
        String name;
        String title;
        String caption;
        String hash;
        String deleteHash;
        String datetime;
        String type;
        Boolean animated = false;
        Integer width;
        Integer height;
        Integer size;
        Integer views = 0;
        Integer bandwidth = 0;
    }

    class Links implements  Serializable {
        String original;
        String imgur_page;
        String delete_page;
        String small_square;
        String large_thumbnail;
    }

}
