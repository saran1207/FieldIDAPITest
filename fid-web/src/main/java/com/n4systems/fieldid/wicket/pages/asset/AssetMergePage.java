package com.n4systems.fieldid.wicket.pages.asset;

import com.n4systems.ejb.AssetManager;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.util.FieldIdDateFormatter;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Date;

//@UserPermissionFilter(userRequiresOneOf={Permissions.TAG})
public class AssetMergePage extends FieldIDFrontEndPage {

    @SpringBean
    private AssetManager assetManager;

    private Long assetId;
    private Asset losingAsset;
    private WebMarkupContainer step1Container;
    private WebMarkupContainer step1ToggledContainer;
    private WebMarkupContainer step2Container;
    private WebMarkupContainer step2ToggledContainer;

    public AssetMergePage(PageParameters params) {
        super(params);
        losingAsset = assetManager.findAssetAllFields(assetId, getSecurityFilter());
        addComponents();
    }

    @Override
    protected void storePageParameters(PageParameters params) {
        assetId = params.get("uniqueID").toLong();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/legacy/steps.css");
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.merge_assets"));
    }

    private void addComponents() {
        step1Container = new WebMarkupContainer("step1Container");
        step1Container.setOutputMarkupId(true);
        add(step1Container);
        step1ToggledContainer = new WebMarkupContainer("step1ToggledContainer");
        step1ToggledContainer.setOutputMarkupId(true);
        step1ToggledContainer.setOutputMarkupPlaceholderTag(true);
        step1Container.add(step1ToggledContainer);

        step2Container = new WebMarkupContainer("step2Container");
        step2Container.setOutputMarkupId(true);
        add(step2Container);
        step2ToggledContainer = new WebMarkupContainer("step2ToggledContainer");
        step2ToggledContainer.setOutputMarkupId(true);
        step2ToggledContainer.setOutputMarkupPlaceholderTag(true);
        step2ToggledContainer.setVisible(false);
        step2Container.add(step2ToggledContainer);

        step1ToggledContainer.add(new Label("identifierLabel", getIdentifierLabel(losingAsset)));
        step1ToggledContainer.add(new Label("losingAsset.identifier", losingAsset.getIdentifier()));
        step1ToggledContainer.add(new Label("losingAsset.rfidNumber", losingAsset.getRfidNumber()));
        step1ToggledContainer.add(new Label("losingAsset.owner.name", losingAsset.getOwner().getName()));
        step1ToggledContainer.add(new Label("losingAsset.type.name", losingAsset.getType().getName()));
        step1ToggledContainer.add(new Label("losingAsset.identified", formatDate(losingAsset.getIdentified(), false)));
        step1ToggledContainer.add(new AjaxLink("step1Button") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                System.out.println("Step1 button clicked");
                step1ToggledContainer.setVisible(false);
                step1Container.add(AttributeModifier.append("class", "stepClosed"));
                target.add(step1Container);
                target.add(step1ToggledContainer);
                step2ToggledContainer.setVisible(true);
                removeCssAttribute(step2Container, "stepClosed");
                //step2Container.add(AttributeModifier.replace("class",
                //        step2Container.getMarkupAttributes().get("class").toString().replaceFirst(" stepClosed", "")));
                target.add(step2Container);
                target.add(step2ToggledContainer);
            }
        });

        step2ToggledContainer.add(new AjaxLink("step2Button") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                System.out.println("Step2 button clicked");
                step2ToggledContainer.setVisible(false);
                step2Container.add(AttributeModifier.append("class", "stepClosed"));
                target.add(step2Container);
                target.add(step2ToggledContainer);
                step1ToggledContainer.setVisible(true);
                removeCssAttribute(step1Container, "stepClosed");
                target.add(step1Container);
                target.add(step1ToggledContainer);
            }
        });



    }

    private void removeCssAttribute(Component component, String cssClass) {
        String currentAttributes = component.getMarkupAttributes().get("class").toString().trim();
        if (currentAttributes.isEmpty())
            return;
        String newAttributes;
        if (currentAttributes.equals(cssClass))
            /* Specified class is the only class currently specified */
            newAttributes = "";
        else
        if (currentAttributes.startsWith(cssClass + " "))
            /* Specified class is the first class of several */
            newAttributes = currentAttributes.replaceFirst(cssClass + " ", "");
        else
            /* Specified class is after the first class or does not appear at all */
            newAttributes = currentAttributes.replaceFirst(" " + cssClass, "");

        component.add(AttributeModifier.replace("class", newAttributes));
    }

    private String getIdentifierLabel(Asset asset) {
        AssetType assetType = asset.getType();
        if (assetType != null && assetType.isIdentifierOverridden())
            return assetType.getIdentifierLabel();
        else
            return getPrimaryOrg().getIdentifierLabel();
    }

    public PrimaryOrg getPrimaryOrg() {
        return getSecurityGuard().getPrimaryOrg();
    }

    public String formatDate(Date date, boolean convertTimeZone) {
        return formatAnyDate(date, convertTimeZone, false);
    }

    protected String formatAnyDate(Date date, boolean convertTimeZone, boolean showTime) {
        return new FieldIdDateFormatter(date, getSessionUser(), convertTimeZone, showTime).format();
    }
}
