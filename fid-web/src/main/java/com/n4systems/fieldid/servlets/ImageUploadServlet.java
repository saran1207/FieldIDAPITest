package com.n4systems.fieldid.servlets;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.context.support.HttpRequestHandlerServlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

public class ImageUploadServlet extends HttpRequestHandlerServlet {

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("calling post with req : " + req);
        System.out.println(req.getContentType());
        System.out.println(req.getParameterNames());

        try {

            ServletFileUpload upload = new ServletFileUpload();
            FileItemIterator iterator = upload.getItemIterator(req);
            while(iterator.hasNext()){

                FileItemStream item = (FileItemStream) iterator.next();
                InputStream stream = item.openStream();
                if(item.isFormField()){
                    if(item.getFieldName().equals("vFormName")){

                        byte[] str = new byte[stream.available()];
                        stream.read(str);
                        String full = new String(str, "UTF8");
                    }
                }else{
                    byte[] data = new byte[stream.available()];
                    stream.read(data);
                    //s3Service.uploadActionImage(data, securityContext.getTenantSecurityFilter().getTenantId());
                    //Object base64 = Base64Utils.toBase64(data);
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
        resp.setContentType("application/json");
        resp.getWriter().write("{\"upload\":{\"image\":{\"name\":null,\"title\":null,\"caption\":null,\"hash\":\"z5Gx7\",\"deletehash\":\"e4AuM2xYIUifJdQ\",\"datetime\":\"2012-11-09 17:12:19\",\"type\":\"image\\/png\",\"animated\":\"false\",\"width\":1503,\"height\":728,\"size\":118171,\"views\":0,\"bandwidth\":0},\"links\":{\"original\":\"http:\\/\\/i.imgur.com\\/z5Gx7.png\",\"imgur_page\":\"http:\\/\\/imgur.com\\/z5Gx7\",\"delete_page\":\"http:\\/\\/imgur.com\\/delete\\/e4AuM2xYIUifJdQ\",\"small_square\":\"http:\\/\\/i.imgur.com\\/z5Gx7s.jpg\",\"large_thumbnail\":\"http:\\/\\/i.imgur.com\\/z5Gx7l.jpg\"}}}\n" +
                "\n");

    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void init() throws ServletException {
        super.init();    //To change body of overridden methods use File | Settings | File Templates.
    }

}
