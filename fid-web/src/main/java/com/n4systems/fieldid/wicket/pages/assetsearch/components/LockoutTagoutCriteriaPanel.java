package com.n4systems.fieldid.wicket.pages.assetsearch.components;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.util.persistence.search.AssetLockoutTagoutStatus;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * Created by rrana on 2015-10-12.
 */
public class LockoutTagoutCriteriaPanel extends Panel {

   public LockoutTagoutCriteriaPanel(String id,  IModel<?> model) {
       super(id, model);

       add(new FidDropDownChoice<AssetLockoutTagoutStatus>("assetLockoutTagout", new PropertyModel<AssetLockoutTagoutStatus>(getDefaultModel(), "assetLockoutTagoutStatus"),
               Lists.newArrayList(AssetLockoutTagoutStatus.values()), new IChoiceRenderer<AssetLockoutTagoutStatus>() {
           @Override
           public Object getDisplayValue(AssetLockoutTagoutStatus object) {
               return object.getLabel();
           }

           @Override
           public String getIdValue(AssetLockoutTagoutStatus object, int index) {
               return object.toString();
           }


       }).setNullValid(true));

   }
}
