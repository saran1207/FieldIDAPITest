package com.n4systems.model.columns.saver;

import com.n4systems.model.columns.CustomColumnCategory;
import com.n4systems.model.columns.ReportType;

public class PathCreator {

    public String createPath(String name, ReportType reportType, CustomColumnCategory category) {
        if (reportType == ReportType.ASSET) {
            if (category == CustomColumnCategory.ASSET) {
                return "infoOptions{infoField.name="+name+"}.name";
            }
        } else if (reportType == ReportType.EVENT) {
            if (category == CustomColumnCategory.ASSET) {
                return "asset.infoOptions{infoField.name="+name+"}.name";
            } else if (category == CustomColumnCategory.EVENT) {
                return "infoOptionMap[\""+name+"\"]";
            }
        }

        throw new RuntimeException("Unknown reporttype/category combination: " + reportType + "/" + category);
    }
}
