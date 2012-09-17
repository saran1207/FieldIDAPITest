package com.n4systems.fieldid.wicket.model;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import java.io.File;

// Returns the actual file name after any directories
public class FileNameModel extends LoadableDetachableModel<String> {

    private IModel<String> filePathModel;

    public FileNameModel(IModel<String> filePathModel) {
        this.filePathModel = filePathModel;
    }

    @Override
    protected String load() {
        String filePath = filePathModel.getObject();
        if (filePath == null) {
            return null;
        }

        String pathSeparator = String.valueOf(File.separatorChar);

        if (filePath.endsWith(pathSeparator)) {
            return filePath;
        }

        return filePath.substring(filePath.lastIndexOf(pathSeparator) + 1);
    }

}
