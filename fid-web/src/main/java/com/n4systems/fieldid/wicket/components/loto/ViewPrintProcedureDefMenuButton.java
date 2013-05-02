package com.n4systems.fieldid.wicket.components.loto;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.components.menuButton.MenuButton;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.loto.PrintOptions;
import com.n4systems.fieldid.wicket.pages.loto.definition.ProcedureDefinitionPrintPage;
import com.n4systems.model.procedure.ProcedureDefinition;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.PopupSettings;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class ViewPrintProcedureDefMenuButton extends MenuButton<PrintOptions> {

    private IModel<ProcedureDefinition> procedureDefinition;

    public ViewPrintProcedureDefMenuButton(String id, IModel<ProcedureDefinition> procedureDefinition) {
        super(id, new FIDLabelModel("label.view_print"), Lists.newArrayList(PrintOptions.values()));
        withNoAjax();
        this.procedureDefinition = procedureDefinition;
    }

    @Override
    protected WebMarkupContainer populateLink(String linkId, String labelId, final ListItem<PrintOptions> item) {
        BookmarkablePageLink<ProcedureDefinitionPrintPage> link = getLink(linkId, item.getModelObject());
        link.add(new Label(labelId, item.getModelObject().name()));
        return (WebMarkupContainer)link;
    }

    @Override
    protected WebMarkupContainer populateButton(String linkId) {
        return (WebMarkupContainer)getLink(linkId, PrintOptions.Normal);
    }

    private BookmarkablePageLink<ProcedureDefinitionPrintPage> getLink(String linkId, PrintOptions printOption) {
        PageParameters params = PageParametersBuilder.id(procedureDefinition.getObject().getId()).add("mode", printOption);
        PopupSettings popupSettings = new PopupSettings("popupWindow", PopupSettings.SCROLLBARS).setWidth(1000).setTop(1);
        BookmarkablePageLink<ProcedureDefinitionPrintPage> link = new BookmarkablePageLink<ProcedureDefinitionPrintPage>(linkId, ProcedureDefinitionPrintPage.class, params);
        link.setPopupSettings(popupSettings);
        return link;
    }
}
