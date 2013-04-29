package com.n4systems.fieldid.wicket.components.timeline;

import org.apache.wicket.Component;

import java.io.Serializable;
import java.util.Date;

public interface TimePointInfoProvider<T> extends Serializable {

    public Date getDate(T item);
    public String getTitle(T item);
    public String getText(T item);
    public String getMediaUrl(T item);
    public String getThumbnailUrl(T item);

}
