package com.n4systems.fieldid.wicket.util;

import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import org.apache.wicket.Component;
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.application.IComponentInitializationListener;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.request.cycle.RequestCycle;

import java.io.Serializable;

public class PagePerformanceListener implements IComponentInitializationListener, Serializable {

    private boolean debug = false;

    public PagePerformanceListener(boolean debug) {
        this.debug = debug;
    }

    public PagePerformanceListener() {
        this(false);
    }

    @Override
    public void onInitialize(Component component) {
        if (monitor(component)) {
            component.add(new PerformanceMonitorBehavior());
        }
    }

    protected boolean monitor(Component component) {
        // suggestion : or instanceof PerformanceMonitoredMarkerIF??
        return component instanceof FieldIDFrontEndPage;
    }


    class PerformanceMonitorBehavior extends Behavior implements Serializable {
        public final MetaDataKey<Long> RENDER_TIME = new MetaDataKey<Long>() {};

        @Override
        public void beforeRender(Component component) {
            Long start = System.currentTimeMillis();
            component.setMetaData(RENDER_TIME, start);
            super.beforeRender(component);
        }

        @Override
        public void afterRender(Component component) {
            RequestCycle requestCycle = RequestCycle.get();
            requestCycle.getResponse().write(getDebugString(component, isDebug(requestCycle)));
            // XXX : if some arbitrary log setting is enabled, spit this out to a performance log appender?
            super.afterRender(component);
        }

        private boolean isDebug(RequestCycle requestCycle) {
            return (debug || !requestCycle.getRequest().getRequestParameters().getParameterValue("debug").isNull());
        }

        private String getDebugString(Component component, boolean debugModeEnabled) {
            long start = component.getMetaData(RENDER_TIME);
            long duration = System.currentTimeMillis() - start;
            String css = debugModeEnabled ? "debug-info" : "hide";
            return String.format("<div class='%s'> %s : %f seconds", css, component.getClass().getSimpleName(), duration / 1000.0);
        }
    }
}
