package com.n4systems.fieldid.wicket.pages;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.fieldid.wicket.behavior.Watermark;
import com.n4systems.fieldid.wicket.components.AmountText;
import com.n4systems.fieldid.wicket.components.DateTimePicker;
import com.n4systems.fieldid.wicket.components.MultiSelectDropDownChoice;
import com.n4systems.fieldid.wicket.components.asset.AutoCompleteSearch;
import com.n4systems.fieldid.wicket.components.org.AutoCompleteOrgPicker;
import com.n4systems.fieldid.wicket.components.org.OrgLocationPicker;
import com.n4systems.fieldid.wicket.components.richText.RichText;
import com.n4systems.fieldid.wicket.components.user.AutoCompleteUser;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.Asset;
import com.n4systems.model.UnitType;
import com.n4systems.model.common.AmountWithString;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.user.User;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import javax.measure.quantity.Length;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import static ch.lambdaj.Lambda.on;

public class SecretTestPage extends FieldIDAuthenticatedPage {

    @SpringBean private OrgService orgService;
    
    private Data data = new Data();


    private int id=0;
    
    public SecretTestPage() {
        Form form = new Form("form", new CompoundPropertyModel(this));

        form.add(new RichText("richText", ProxyModel.of(data, on(Data.class).getRichText())) {
            @Override protected String getImagePath() {
                return "/test/page/images/";
            }
        }.withRows(8).withWidth("300px;"));
//        form.add(new Comment("comment", ProxyModel.of(data, on(Data.class).getComment())).setVisible(true));
        form.add(new AmountText<Length>("amountText", new PropertyModel(data, "amount"), UnitType.FEET_INCHES).setVisible(true));
        form.add(new AutoCompleteOrgPicker("autocompleteorg", ProxyModel.of(data, on(Data.class).getOrg())).setVisible(true));
        form.add(new AutoCompleteSearch("autocompletesearch", ProxyModel.of(data, on(Data.class).getAsset())).setVisible(true));
        form.add(new TextField("watermark", ProxyModel.of(data, on(Data.class).getText())).add(new Watermark("enter a value")).setVisible(true));
        form.add(new TextField("watermark2", ProxyModel.of(data, on(Data.class).getText2())).add(new Watermark("enter another value")).setVisible(true));
        form.add(new PasswordTextField("password", ProxyModel.of(data, on(Data.class).getPassword())).add(new Watermark("enter password")).setVisible(true).setVisible(true));
//        form.add(new Agenda("agenda", Model.of(new WorkWidgetConfiguration())).setVisible(true));
        form.add(new AutoCompleteUser("user", ProxyModel.of(data, on(Data.class).getUser())).setVisible(true));
//        form.add(new GoogleMap("map").addLocation(43.65, -79.34).addLocation(42.00, -80.00).addLocation(44.0, -79.7).setVisible(true));
        form.add(new OrgLocationPicker("location", ProxyModel.of(data, on(Data.class).getOrg())).setVisible(false));
        form.add(new MultiSelectDropDownChoice("multiselect", ProxyModel.of(data,on(Data.class).getMultiselect()), Lists.newArrayList(new Foo("a", "b"), new Foo("hello", "goodbye"), new Foo("x", "y"), new Foo("1", "12"))));
//        form.add(new GoogleMap("map"));
        form.add(new DateTimePicker("dateTimePicker", ProxyModel.of(data, on(Data.class).getDate()), true).setVisible(true));
        form.add(new DateTimePicker("datePicker", ProxyModel.of(data, on(Data.class).getDate()), false).setVisible(true));

        form.add(new AjaxSubmitLink("submit") {
            @Override protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit();
            }
            @Override protected void onError(AjaxRequestTarget target, Form<?> form) {
                // NOTE : password is required field.
                super.onError();
            }
        });
        
        add(form);
    }
    
    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/reset.css");
        response.renderCSSReference("style/site_wide.css");
        response.renderCSSReference("style/fieldid.css");
    }
    

    class Data implements Serializable {
        BaseOrg org = orgService.getPrimaryOrgForTenant(getCurrentUser().getTenant().getId());
        Asset asset;
        String text;
        String text2 = "text2";
        Date date;
        String password = "password";
        User user;
        PredefinedLocation location;
        String comment;
        String richText = "<b>asdfasdf</b>";
        List<String> multiselect;
        AmountWithString<Length> amount;

        public Data() {
        }

        public AmountWithString<Length> getAmount() {
            return amount;
        }

        public List<String> getMultiselect() {
            return multiselect;
        }

        public void setMultiselect(List<String> multiselect) {
            this.multiselect = multiselect;
        }

        public Asset getAsset() {
            return asset;
        }

        public String getComment() {
            return comment;
        }

        public Date getDate() {
            return date;
        }

        public PredefinedLocation getLocation() {
            return location;
        }

        public BaseOrg getOrg() {
            return org;
        }

        public OrgService getOrgService() {
            return orgService;
        }

        public String getPassword() {
            return password;
        }

        public String getRichText() {
            return richText;
        }

        public String getText2() {
            return text2;
        }

        public String getText() {
            return text;
        }

        public User getUser() {
            return user;
        }

    }


    class Foo implements Serializable {
        private String x;
        private String y;

        public Foo(String x, String y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "FOO:"+x+y;
        }

        public String getX() {
            return x;
        }

        public void setX(String x) {
            this.x = x;
        }

        public String getY() {
            return y;
        }

        public void setY(String y) {
            this.y = y;
        }
    }


}
