package com.n4systems.fieldid.wicket.components.timeline;

import java.io.Serializable;
import java.util.Date;

public interface TimePointInfoProvider<T> extends Serializable {

    public Date getDate(T item);
    public String getTitle(T item);
    public String getText(T item);
    public String getUrl(T item);

}
