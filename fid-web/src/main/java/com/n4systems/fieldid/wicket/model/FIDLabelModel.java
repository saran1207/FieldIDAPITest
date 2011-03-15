package com.n4systems.fieldid.wicket.model;

import com.n4systems.fieldid.wicket.FieldIDSession;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class FIDLabelModel implements IModel<String> {

    private static Properties props = new Properties();

    static {
        InputStream propsStream = FIDLabelModel.class.getResourceAsStream("/com/n4systems/fieldid/actions/package.properties");
        try {
            props.load(propsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private IModel<String> propertyKeyModel;

    public FIDLabelModel(IModel<?> model, String propertyName) {
        this.propertyKeyModel = new PropertyModel<String>(model, propertyName);
    }

    public FIDLabelModel(IModel<String> propertyKeyModel) {
        this.propertyKeyModel = propertyKeyModel;
    }

    public FIDLabelModel(String propertyKey) {
        this.propertyKeyModel = new Model<String>(propertyKey);
    }

    @Override
    public String getObject() {
        String propertyKey = propertyKeyModel.getObject();
        String label = (hasTenantOverride()) ? getMessage(propertyKey) : props.getProperty(propertyKey);
        if (label == null)
            label = propertyKey;
        return label;
    }

    private boolean hasTenantOverride() {
        return getLangOverrides().containsKey(propertyKeyModel.getObject());
    }

    private Map<String, String> getLangOverrides() {
        return FieldIDSession.get().getTenantLangOverrides();
    }

	private String getMessage(String key) {
        return getLangOverrides().get(key);
	}

    @Override
    public void setObject(String object) {
    }

    @Override
    public void detach() {
        propertyKeyModel.detach();
    }

}
