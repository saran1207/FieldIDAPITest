package com.n4systems.fieldid.servlets;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.n4systems.fieldid.service.amazon.S3Service;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
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
import java.util.Date;

/**
 * usage  :
 * meant to be used with @RichText component. all image uploads hit this servlet because it's configured the upload url to point to here.
 * by default, returns a json object with a url to the medium sized image.
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
                    response.setContentType("application/json");
                    response.getWriter().write(gson.toJson(jsonObject));
                    break;
                }
            }
        } catch (Exception e) {
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
        return new JsonImage(imagePath, bufferedImage, baos.size(), request.getParameter("type"));
    }

    private String getFormat(FileItemStream item) {
        String contentType = item.getContentType();
        if (contentType.startsWith("image/")) {
            return contentType.substring("image/".length());
        } else {
            logger.warn("can't figure out proper file format for item " + item.getName() + " with type " + item.getContentType());
            return contentType;
        }
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

        JsonImage(S3Service.S3ImagePath imagePath, BufferedImage bufferedImage, int size, String type) {
            upload.links.large_thumbnail = imagePath.getThumbnailUrl().toString();
            upload.links.original = imagePath.getOrigUrl().toString();
            upload.image.hash = hashCode() + "";
            upload.image.width = bufferedImage.getWidth();
            upload.image.height = bufferedImage.getHeight();
            upload.image.size = size;
            upload.image.datetime = new Date().toString();
        }
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
