package com.n4systems.fieldid.wicket.pages.loto;

import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class ProceduresPage extends LotoPage {

    public ProceduresPage(PageParameters params) {
        super(params);

        add(new BookmarkablePageLink<VersionsPage>("versionsLink", VersionsPage.class, PageParametersBuilder.uniqueId(getAssetId())));

        add(new Link("copyExisitingLink") {
            @Override
            public void onClick() {
            }
        });

        add(new Link("startBlankLink") {
            @Override
            public void onClick() {
            }
        });
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderJavaScriptReference("javascript/new_procedure_list.js");
        response.renderCSSReference("style/newCss/component/matt_buttons.css");
        response.renderCSSReference("style/newCss/loto/procedures.css");
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.procedures"));
    }

}
