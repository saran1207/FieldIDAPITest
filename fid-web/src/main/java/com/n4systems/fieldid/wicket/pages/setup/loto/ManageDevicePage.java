package com.n4systems.fieldid.wicket.pages.setup.loto;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.procedure.ProcedureDefinitionService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.modal.DialogModalWindow;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.model.IsolationPointSourceType;
import com.n4systems.model.procedure.PreconfiguredDevice;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class ManageDevicePage extends FieldIDTemplatePage {

    @SpringBean
    private ProcedureDefinitionService procedureDefinitionService;
    @SpringBean
    private PersistenceService persistenceService;

    private IsolationPointSourceType sourceType;

    private WebMarkupContainer deviceList;
    private ModalWindow addOrEditDeviceWindow;
    private ListView<PreconfiguredDevice> listView;

    public ManageDevicePage(PageParameters params) {

        sourceType = params.get("type").toString().equals("all") ? null : IsolationPointSourceType.valueOf(params.get("type").toString());

        add(addOrEditDeviceWindow = new DialogModalWindow("addOrEditDeviceWindow").setInitialWidth(350));

        add(deviceList = new WebMarkupContainer("deviceList"));
        deviceList.setOutputMarkupId(true);
        deviceList.add(listView = new ListView<PreconfiguredDevice>("device", getDevices(sourceType)) {
            @Override
            protected void populateItem(ListItem<PreconfiguredDevice> item) {

                item.add(new FlatLabel("name", new PropertyModel<>(item.getModelObject(), "device")));
                item.add(new FlatLabel("method", new PropertyModel<>(item.getModelObject(), "method")));

                item.add(new AjaxLink<Void>("editLink") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        addOrEditDeviceWindow.setContent(createAddEditDevicePanel(item.getModel()));
                        addOrEditDeviceWindow.show(target);
                    }
                });

                item.add(new Link<Void>("deleteLink") {
                    @Override
                    public void onClick() {
                        persistenceService.delete(item.getModelObject());
                        setResponsePage(new ManageDevicePage(PageParametersBuilder.param("type", sourceType == null ? "all" : sourceType.name())));
                    }
                });
            }
        }.setReuseItems(false));
    }

    private AddEditDevicePanel createAddEditDevicePanel(IModel<PreconfiguredDevice> preconfiguredDevice) {
        return new AddEditDevicePanel(addOrEditDeviceWindow.getContentId(), preconfiguredDevice) {
            @Override
            protected void onEditComplete(AjaxRequestTarget target, PreconfiguredDevice device) {
                persistenceService.saveOrUpdate(device);
                target.add(deviceList);
                addOrEditDeviceWindow.close(target);
            }
        };
    }

    private LoadableDetachableModel<List<PreconfiguredDevice>> getDevices(IsolationPointSourceType sourceType) {
        return new LoadableDetachableModel<List<PreconfiguredDevice>>() {
            @Override
            protected List<PreconfiguredDevice> load() {
                return procedureDefinitionService.getPreConfiguredDevices(sourceType);
            }
        };
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.manage_energy_type_fields",
                sourceType != null ? sourceType.getIdentifier() : new FIDLabelModel("label.shared_devices").getObject()));
    }

    @Override
    protected Component createBackToLink(String linkId, String linkLabelId) {
        return new BookmarkablePageLink<LotoSetupPage>(linkId, LotoSetupPage.class)
                .add(new Label(linkLabelId, new FIDLabelModel("label.back_to_setup")));
    }

    @Override
    protected Component createActionGroup(String actionGroupId) {
        return new ActionGroup(actionGroupId);
    }

    private PreconfiguredDevice createNewDevice() {
        PreconfiguredDevice device = new PreconfiguredDevice();
        device.setTenant(FieldIDSession.get().getTenant());
        device.setIsolationPointSourceType(sourceType);
        return device;
    }

    private class ActionGroup extends Fragment {

        public ActionGroup(String id) {
            super(id, "deviceActionGroup", ManageDevicePage.this);

            add(new AjaxLink<Void>("addLink") {

                @Override
                public void onClick(AjaxRequestTarget target) {
                    addOrEditDeviceWindow.setContent(createAddEditDevicePanel(Model.of(createNewDevice())));
                    addOrEditDeviceWindow.show(target);
                }
            });
        }
    }
}
