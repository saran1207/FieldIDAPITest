package com.n4systems.fieldid.wicket;

import org.apache.wicket.util.file.File;
import org.apache.wicket.util.file.IResourcePath;
import org.apache.wicket.util.file.WebApplicationPath;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;

import javax.servlet.ServletContext;
import java.lang.annotation.Annotation;

public class DynamicWebApplicationPath implements IResourcePath {

    private final WebApplicationPath delegate;

    public DynamicWebApplicationPath(ServletContext servletContext) {
        this.delegate = new WebApplicationPath(servletContext);
    }

    @Override
    public void add(String folder) {
        delegate.add(folder);
    }

    @Override
    public IResourceStream find(Class<?> clazz, String pathname) {
        for (Annotation annotation:clazz.getAnnotations()) {
            if (annotation.annotationType() == ComponentWithExternalHtml.class) {
                return find(clazz,pathname,getDynamicTemplateDirectory());
            }
        }
        return delegate.find(clazz, pathname);
    }

    private IResourceStream find(Class<?> clazz, String pathname, String dir) {
        //note : i strip off all the packaging crap....so com/foo/bar/File.html  -->  File.html
        final File file = new File(dir, new File(pathname).getName());
        if (file.exists()) {
            return new FileResourceStream(file);
        }
        return null;
    }

    private String getDynamicTemplateDirectory() {
        return "/var/fieldid/common/templates/wicket";
    }
}
