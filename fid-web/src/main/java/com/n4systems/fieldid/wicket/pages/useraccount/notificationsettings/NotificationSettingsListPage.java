package com.n4systems.fieldid.wicket.pages.useraccount.notificationsettings;

import com.n4systems.fieldid.service.notificationsetting.NotificationSettingService;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.useraccount.AccountSetupPage;
import com.n4systems.model.notificationsettings.NotificationSetting;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class NotificationSettingsListPage extends AccountSetupPage {

    @SpringBean
    private NotificationSettingService notificationSettingService;

    public NotificationSettingsListPage() {

        add(new BookmarkablePageLink<>("addNotificationLink", AddEditNotificationSettingPage.class));

        add(new ListView<NotificationSetting>("notificationSetting", getNotificationSettingsList()) {
            @Override
            protected void populateItem(ListItem<NotificationSetting> item) {

                item.add(new FlatLabel("name", new PropertyModel<>(item.getModel(), "name")));
                item.add(new FlatLabel("frequency", new FIDLabelModel(new PropertyModel<>(item.getModel(), "frequency.label"))));
                item.add(new FlatLabel("start", new FIDLabelModel(new PropertyModel<>(item.getModel(), "periodStart.label"))));
                item.add(new FlatLabel("end", new FIDLabelModel(new PropertyModel<>(item.getModel(), "periodEnd.label"))));
                item.add(new Link<Void>("editLink") {
                    @Override
                    public void onClick() {
                        setResponsePage(AddEditNotificationSettingPage.class, PageParametersBuilder.uniqueId(item.getModelObject().getId()));
                    }
                });

                item.add(new AjaxLink<Void>("deleteLink") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        notificationSettingService.remove(item.getModelObject());
                        setResponsePage(NotificationSettingsListPage.class);
                    }
                });

            }
        });

    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("nav.notification_settings"));
    }

    private LoadableDetachableModel<List<NotificationSetting>> getNotificationSettingsList() {
        return new LoadableDetachableModel<List<NotificationSetting>>() {
            @Override
            protected List<NotificationSetting> load() {
                return notificationSettingService.findAllUserNotifications(getCurrentUser());
            }
        };
    }
}
