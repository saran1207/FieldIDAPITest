package com.n4systems.fieldid.wicket.components.measure;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.wicket.behavior.ChangeThenClickComponentWhenEnterPressed;
import com.n4systems.fieldid.wicket.behavior.UpdateComponentOnChange;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.UnitOfMeasure;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import java.util.List;

import static ch.lambdaj.Lambda.on;

public class UnitOfMeasureEditor extends FormComponentPanel<InfoOptionBean> {

    @SpringBean private PersistenceService persistenceService;

    WebMarkupContainer editorContainer;
    WebMarkupContainer displayContainer;

    private UnitOfMeasure primaryUnit;
    private UnitOfMeasure secondaryUnit;

    private String primaryValue;
    private String secondaryValue;

    public UnitOfMeasureEditor(String id, final IModel<InfoOptionBean> infoOption) {
        super(id, infoOption);

        IModel<List<UnitOfMeasure>> unitOfMeasuresModel = createUomModel();

        InfoFieldBean infoField = infoOption.getObject().getInfoField();

        if (infoField.getUnitOfMeasure() != null) {
            setUnitOfMeasure(infoField.getUnitOfMeasure());
        } else {
            setUnitOfMeasure(unitOfMeasuresModel.getObject().iterator().next());
        }

        add(editorContainer = new WebMarkupContainer("editorContainer"));
        add(displayContainer = new WebMarkupContainer("displayContainer"));

        editorContainer.setVisible(false);

        editorContainer.setOutputMarkupPlaceholderTag(true);
        displayContainer.setOutputMarkupPlaceholderTag(true);

        displayContainer.add(new TextField<String>("uomTextField", ProxyModel.of(infoOption, on(InfoOptionBean.class).getName())));

        DropDownChoice<UnitOfMeasure> unitOfMeasureSelect = new DropDownChoice<UnitOfMeasure>("unitOfMeasureSelect", new PropertyModel<UnitOfMeasure>(this, "primaryUnit"), unitOfMeasuresModel, new ListableChoiceRenderer<UnitOfMeasure>());
        editorContainer.add(unitOfMeasureSelect);

        final WebMarkupContainer secondaryContainer = new WebMarkupContainer("secondaryContainer") {
            @Override
            public boolean isVisible() {
                return primaryUnit != null && primaryUnit.getChild() != null;
            }
        };
        secondaryContainer.setOutputMarkupPlaceholderTag(true);

        editorContainer.add(new Label("unitOfMeasureLabel", new PropertyModel(this, "primaryUnit.name")));
        secondaryContainer.add(new Label("secondaryUnitOfMeasureLabel", new PropertyModel(this, "secondaryUnit.name")));

        unitOfMeasureSelect.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                setUnitOfMeasure(primaryUnit);
                target.add(editorContainer);
            }
        });

        WebMarkupContainer openEditorImage = new WebMarkupContainer("openEditorImage");
        openEditorImage.add(new AjaxEventBehavior("onclick") {
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                setEditMode(true, target);
            }
        });
        displayContainer.add(openEditorImage);

        editorContainer.add(secondaryContainer);

        AjaxLink storeLink = new AjaxLink("storeLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                infoOption.getObject().setName(convertUomToString());
                primaryValue = secondaryValue = null;
                setEditMode(false, target);
            }
        };

        editorContainer.add(createUpdatingTextField("primaryValue", new PropertyModel<String>(this, "primaryValue"), storeLink));
        secondaryContainer.add(createUpdatingTextField("secondaryValue", new PropertyModel<String>(this, "secondaryValue"), storeLink));

        editorContainer.add(new AjaxLink("cancelLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                setEditMode(false, target);
            }
        });

        editorContainer.add(storeLink);
    }

    private Component createUpdatingTextField(String id, PropertyModel<String> model, final AjaxLink storeLink) {
        return new TextField<String>(id,model) {
            {
                add(new UpdateComponentOnChange());
                add(new ChangeThenClickComponentWhenEnterPressed(storeLink));
            }
        };
    }

    private void setEditMode(boolean editMode, AjaxRequestTarget target) {
        editorContainer.setVisible(editMode);
        displayContainer.setVisible(!editMode);
        target.add(editorContainer, displayContainer);
    }

    private void setUnitOfMeasure(UnitOfMeasure uom) {
        primaryUnit = uom;
        secondaryUnit = uom.getChild();
        primaryValue = secondaryValue = null;
    }

    private LoadableDetachableModel<List<UnitOfMeasure>> createUomModel() {
        return new LoadableDetachableModel<List<UnitOfMeasure>>() {
            @Override
            protected List<UnitOfMeasure> load() {
                QueryBuilder<UnitOfMeasure> query = new QueryBuilder<UnitOfMeasure>(UnitOfMeasure.class, new OpenSecurityFilter());
                return persistenceService.findAll(query);
            }
        };
    }

    @Override
    protected void convertInput() {
        String value = convertUomToString();

        getModelObject().setName(value);
        setConvertedInput(getModelObject());
    }

    private String convertUomToString() {
        StringBuffer convertedInput = new StringBuffer();
        if (!StringUtils.isBlank(primaryValue)) {
            convertedInput.append(primaryValue).append(" ").append(primaryUnit.getShortName());
        }
        if (primaryUnit.getChild()!=null && !StringUtils.isBlank(secondaryValue)) {
            convertedInput.append(" ").append(secondaryValue).append(" ").append(secondaryUnit.getShortName());
        }

        String value;
        if (StringUtils.isBlank(convertedInput.toString())) {
            value = null;
        } else {
            value = convertedInput.toString();
        }
        return value;
    }
}
