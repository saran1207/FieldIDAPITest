package com.n4systems.fieldid.wicket.components;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.services.ConfigService;
import com.n4systems.util.ConfigEntry;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.interpolator.MapVariableInterpolator;
import rfid.web.helper.SessionUser;

import java.util.HashMap;
import java.util.Map;

public class CustomJavascriptPanel extends Panel {

    @SpringBean
    private ConfigService configService;

    public CustomJavascriptPanel(String id) {
        super(id);
        setRenderBodyOnly(true);

        Label javascript = new Label("js", new CustomJavascriptForTenant());
        javascript.setEscapeModelStrings(false);
        add(javascript);
    }

    private class CustomJavascriptForTenant extends LoadableDetachableModel<String> {

        @Override
        protected String load() {
            String js = configService.getString(ConfigEntry.CUSTOM_JS);

            SessionUser sessionUser = FieldIDSession.get().getSessionUser();
            Map<String,String> variableMap = new HashMap<String, String>();
            variableMap.put("tenant", FieldIDSession.get().getTenant().getName());
            variableMap.put("user", sessionUser.getName());
            variableMap.put("userType", sessionUser.getUserTypeLabel());
            variableMap.put("accountType", sessionUser.getAccountType());

            MapVariableInterpolator interpolator = new MapVariableInterpolator(js, variableMap);

            return interpolator.toString();
        }
    }

}
