package com.n4systems.fieldid.wicket.pages.template;

import com.n4systems.fieldid.wicket.components.navigation.BreadCrumbBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.NavigationItem;
import com.n4systems.fieldid.wicket.pages.DashboardPage;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.Model;

/**
 * Created by rrana on 2016-01-27.
 */
public class FontelloFonts extends FieldIDTemplatePage {

    private WebMarkupContainer icon_th_large;
    private WebMarkupContainer icon_flow_cascade;
    private WebMarkupContainer icon_cancel_1;
    private WebMarkupContainer icon_plus_squared_small;
    private WebMarkupContainer icon_minus_squared_small;
    private WebMarkupContainer icon_info_1;
    private WebMarkupContainer icon_pin;
    private WebMarkupContainer icon_bookmark;
    private WebMarkupContainer icon_bookmark_empty;
    private WebMarkupContainer icon_download_1;
    private WebMarkupContainer icon_upload_1;
    private WebMarkupContainer icon_trash_1;
    private WebMarkupContainer icon_calendar;
    private WebMarkupContainer icon_calendar_empty;
    private WebMarkupContainer icon_zoom_in;
    private WebMarkupContainer icon_zoom_out;
    private WebMarkupContainer icon_down_dir_1;
    private WebMarkupContainer icon_up_dir_1;
    private WebMarkupContainer icon_left_dir_1;
    private WebMarkupContainer icon_right_dir_1;
    private WebMarkupContainer icon_angle_double_left;
    private WebMarkupContainer icon_angle_double_right;
    private WebMarkupContainer icon_angle_double_up;
    private WebMarkupContainer icon_angle_double_down;
    private WebMarkupContainer icon_cw_1;
    private WebMarkupContainer icon_ccw_1;
    private WebMarkupContainer icon_arrows_cw;
    private WebMarkupContainer icon_laptop;
    private WebMarkupContainer icon_tablet;
    private WebMarkupContainer icon_mobile;
    private WebMarkupContainer icon_table;
    private WebMarkupContainer icon_columns;
    private WebMarkupContainer icon_filter;
    private WebMarkupContainer icon_sort;
    private WebMarkupContainer icon_search;
    private WebMarkupContainer icon_mail;
    private WebMarkupContainer icon_user;
    private WebMarkupContainer icon_users;
    private WebMarkupContainer icon_user_add;
    private WebMarkupContainer icon_picture;
    private WebMarkupContainer icon_th_list;
    private WebMarkupContainer icon_layout;
    private WebMarkupContainer icon_menu;
    private WebMarkupContainer icon_check;
    private WebMarkupContainer icon_cancel;
    private WebMarkupContainer icon_plus;
    private WebMarkupContainer icon_plus_squared;
    private WebMarkupContainer icon_minus;
    private WebMarkupContainer icon_minus_squared;
    private WebMarkupContainer icon_help;
    private WebMarkupContainer icon_home;
    private WebMarkupContainer icon_link;
    private WebMarkupContainer icon_attach;
    private WebMarkupContainer icon_lock;
    private WebMarkupContainer icon_lock_open;
    private WebMarkupContainer icon_download;
    private WebMarkupContainer icon_upload;
    private WebMarkupContainer icon_export;
    private WebMarkupContainer icon_pencil;
    private WebMarkupContainer icon_print;
    private WebMarkupContainer icon_comment;
    private WebMarkupContainer icon_attention;
    private WebMarkupContainer icon_location;
    private WebMarkupContainer icon_map;
    private WebMarkupContainer icon_cog;
    private WebMarkupContainer icon_arrow_combo;
    private WebMarkupContainer icon_down_open;
    private WebMarkupContainer icon_left_open;
    private WebMarkupContainer icon_right_open;
    private WebMarkupContainer icon_up_open;
    private WebMarkupContainer icon_down_open_mini;
    private WebMarkupContainer icon_left_open_mini;
    private WebMarkupContainer icon_right_open_mini;
    private WebMarkupContainer icon_up_open_mini;
    private WebMarkupContainer icon_down_thin;
    private WebMarkupContainer icon_left_thin;
    private WebMarkupContainer icon_right_thin;
    private WebMarkupContainer icon_up_thin;
    private WebMarkupContainer icon_target;
    private WebMarkupContainer icon_camera;

    public FontelloFonts() {

        add(icon_th_large = new WebMarkupContainer("icon-th-large"));
        icon_th_large.setOutputMarkupId(true);
        icon_th_large.add(new AttributeAppender("class", new Model<String>("icon-th-large"), " "));

        add(icon_flow_cascade = new WebMarkupContainer("icon-flow-cascade"));
        icon_flow_cascade.setOutputMarkupId(true);
        icon_flow_cascade.add(new AttributeAppender("class", new Model<String>("icon-flow-cascade"), " "));

        add(icon_cancel_1 = new WebMarkupContainer("icon-cancel-1"));
        icon_cancel_1.setOutputMarkupId(true);
        icon_cancel_1.add(new AttributeAppender("class", new Model<String>("icon-cancel-1"), " "));

        add(icon_plus_squared_small = new WebMarkupContainer("icon-plus-squared-small"));
        icon_plus_squared_small.setOutputMarkupId(true);
        icon_plus_squared_small.add(new AttributeAppender("class", new Model<String>("icon-plus-squared-small"), " "));

        add(icon_minus_squared_small = new WebMarkupContainer("icon-minus-squared-small"));
        icon_minus_squared_small.setOutputMarkupId(true);
        icon_minus_squared_small.add(new AttributeAppender("class", new Model<String>("icon-minus-squared-small"), " "));

        add(icon_info_1 = new WebMarkupContainer("icon-info-1"));
        icon_info_1.setOutputMarkupId(true);
        icon_info_1.add(new AttributeAppender("class", new Model<String>("icon-info-1"), " "));

        add(icon_pin = new WebMarkupContainer("icon-pin"));
        icon_pin.setOutputMarkupId(true);
        icon_pin.add(new AttributeAppender("class", new Model<String>("icon-pin"), " "));

        add(icon_bookmark = new WebMarkupContainer("icon-bookmark"));
        icon_bookmark.setOutputMarkupId(true);
        icon_bookmark.add(new AttributeAppender("class", new Model<String>("icon-bookmark"), " "));

        add(icon_bookmark_empty = new WebMarkupContainer("icon-bookmark-empty"));
        icon_bookmark_empty.setOutputMarkupId(true);
        icon_bookmark_empty.add(new AttributeAppender("class", new Model<String>("icon-bookmark-empty"), " "));

        add(icon_download_1 = new WebMarkupContainer("icon-download-1"));
        icon_download_1.setOutputMarkupId(true);
        icon_download_1.add(new AttributeAppender("class", new Model<String>("icon-download-1"), " "));

        add(icon_upload_1 = new WebMarkupContainer("icon-upload-1"));
        icon_upload_1.setOutputMarkupId(true);
        icon_upload_1.add(new AttributeAppender("class", new Model<String>("icon-upload-1"), " "));

        add(icon_trash_1 = new WebMarkupContainer("icon-trash-1"));
        icon_trash_1.setOutputMarkupId(true);
        icon_trash_1.add(new AttributeAppender("class", new Model<String>("icon-trash-1"), " "));

        add(icon_calendar = new WebMarkupContainer("icon-calendar"));
        icon_calendar.setOutputMarkupId(true);
        icon_calendar.add(new AttributeAppender("class", new Model<String>("icon-calendar"), " "));

        add(icon_calendar_empty = new WebMarkupContainer("icon-calendar-empty"));
        icon_calendar_empty.setOutputMarkupId(true);
        icon_calendar_empty.add(new AttributeAppender("class", new Model<String>("icon-calendar-empty"), " "));

        add(icon_zoom_in = new WebMarkupContainer("icon-zoom-in"));
        icon_zoom_in.setOutputMarkupId(true);
        icon_zoom_in.add(new AttributeAppender("class", new Model<String>("icon-zoom-in"), " "));

        add(icon_zoom_out = new WebMarkupContainer("icon-zoom-out"));
        icon_zoom_out.setOutputMarkupId(true);
        icon_zoom_out.add(new AttributeAppender("class", new Model<String>("icon-zoom-out"), " "));

        add(icon_down_dir_1 = new WebMarkupContainer("icon-down-dir-1"));
        icon_down_dir_1.setOutputMarkupId(true);
        icon_down_dir_1.add(new AttributeAppender("class", new Model<String>("icon-down-dir-1"), " "));

        add(icon_up_dir_1 = new WebMarkupContainer("icon-up-dir-1"));
        icon_up_dir_1.setOutputMarkupId(true);
        icon_up_dir_1.add(new AttributeAppender("class", new Model<String>("icon-up-dir-1"), " "));

        add(icon_left_dir_1 = new WebMarkupContainer("icon-left-dir-1"));
        icon_left_dir_1.setOutputMarkupId(true);
        icon_left_dir_1.add(new AttributeAppender("class", new Model<String>("icon-left-dir-1"), " "));

        add(icon_right_dir_1 = new WebMarkupContainer("icon-right-dir-1"));
        icon_right_dir_1.setOutputMarkupId(true);
        icon_right_dir_1.add(new AttributeAppender("class", new Model<String>("icon-right-dir-1"), " "));

        add(icon_angle_double_left = new WebMarkupContainer("icon-angle-double-left"));
        icon_angle_double_left.setOutputMarkupId(true);
        icon_angle_double_left.add(new AttributeAppender("class", new Model<String>("icon-angle-double-left"), " "));

        add(icon_angle_double_right = new WebMarkupContainer("icon-angle-double-right"));
        icon_angle_double_right.setOutputMarkupId(true);
        icon_angle_double_right.add(new AttributeAppender("class", new Model<String>("icon-angle-double-right"), " "));

        add(icon_angle_double_up = new WebMarkupContainer("icon-angle-double-up"));
        icon_angle_double_up.setOutputMarkupId(true);
        icon_angle_double_up.add(new AttributeAppender("class", new Model<String>("icon-angle-double-up"), " "));

        add(icon_angle_double_down = new WebMarkupContainer("icon-angle-double-down"));
        icon_angle_double_down.setOutputMarkupId(true);
        icon_angle_double_down.add(new AttributeAppender("class", new Model<String>("icon-angle-double-down"), " "));

        add(icon_cw_1 = new WebMarkupContainer("icon-cw-1"));
        icon_cw_1.setOutputMarkupId(true);
        icon_cw_1.add(new AttributeAppender("class", new Model<String>("icon-cw-1"), " "));

        add(icon_ccw_1 = new WebMarkupContainer("icon-ccw-1"));
        icon_ccw_1.setOutputMarkupId(true);
        icon_ccw_1.add(new AttributeAppender("class", new Model<String>("icon-ccw-1"), " "));

        add(icon_arrows_cw = new WebMarkupContainer("icon-arrows-cw"));
        icon_arrows_cw.setOutputMarkupId(true);
        icon_arrows_cw.add(new AttributeAppender("class", new Model<String>("icon-arrows-cw"), " "));

        add(icon_laptop = new WebMarkupContainer("icon-laptop"));
        icon_laptop.setOutputMarkupId(true);
        icon_laptop.add(new AttributeAppender("class", new Model<String>("icon-laptop"), " "));

        add(icon_tablet = new WebMarkupContainer("icon-tablet"));
        icon_tablet.setOutputMarkupId(true);
        icon_tablet.add(new AttributeAppender("class", new Model<String>("icon-tablet"), " "));

        add(icon_mobile = new WebMarkupContainer("icon-mobile"));
        icon_mobile.setOutputMarkupId(true);
        icon_mobile.add(new AttributeAppender("class", new Model<String>("icon-mobile"), " "));

        add(icon_table = new WebMarkupContainer("icon-table"));
        icon_table.setOutputMarkupId(true);
        icon_table.add(new AttributeAppender("class", new Model<String>("icon-table"), " "));

        add(icon_columns = new WebMarkupContainer("icon-columns"));
        icon_columns.setOutputMarkupId(true);
        icon_columns.add(new AttributeAppender("class", new Model<String>("icon-columns"), " "));

        add(icon_filter = new WebMarkupContainer("icon-filter"));
        icon_filter.setOutputMarkupId(true);
        icon_filter.add(new AttributeAppender("class", new Model<String>("icon-filter"), " "));

        add(icon_sort = new WebMarkupContainer("icon-sort"));
        icon_sort.setOutputMarkupId(true);
        icon_sort.add(new AttributeAppender("class", new Model<String>("icon-sort"), " "));

        add(icon_search = new WebMarkupContainer("icon-search"));
        icon_search.setOutputMarkupId(true);
        icon_search.add(new AttributeAppender("class", new Model<String>("icon-search"), " "));

        add(icon_mail = new WebMarkupContainer("icon-mail"));
        icon_mail.setOutputMarkupId(true);
        icon_mail.add(new AttributeAppender("class", new Model<String>("icon-mail"), " "));

        add(icon_user = new WebMarkupContainer("icon-user"));
        icon_user.setOutputMarkupId(true);
        icon_user.add(new AttributeAppender("class", new Model<String>("icon-user"), " "));

        add(icon_users = new WebMarkupContainer("icon-users"));
        icon_users.setOutputMarkupId(true);
        icon_users.add(new AttributeAppender("class", new Model<String>("icon-users"), " "));

        add(icon_user_add = new WebMarkupContainer("icon-user-add"));
        icon_user_add.setOutputMarkupId(true);
        icon_user_add.add(new AttributeAppender("class", new Model<String>("icon-user-add"), " "));

        add(icon_picture = new WebMarkupContainer("icon-picture"));
        icon_picture.setOutputMarkupId(true);
        icon_picture.add(new AttributeAppender("class", new Model<String>("icon-picture"), " "));

        add(icon_th_list = new WebMarkupContainer("icon-th-list"));
        icon_th_list.setOutputMarkupId(true);
        icon_th_list.add(new AttributeAppender("class", new Model<String>("icon-th-list"), " "));

        add(icon_layout = new WebMarkupContainer("icon-layout"));
        icon_layout.setOutputMarkupId(true);
        icon_layout.add(new AttributeAppender("class", new Model<String>("icon-layout"), " "));

        add(icon_menu = new WebMarkupContainer("icon-menu"));
        icon_menu.setOutputMarkupId(true);
        icon_menu.add(new AttributeAppender("class", new Model<String>("icon-menu"), " "));

        add(icon_check = new WebMarkupContainer("icon-check"));
        icon_check.setOutputMarkupId(true);
        icon_check.add(new AttributeAppender("class", new Model<String>("icon-check"), " "));

        add(icon_cancel = new WebMarkupContainer("icon-cancel"));
        icon_cancel.setOutputMarkupId(true);
        icon_cancel.add(new AttributeAppender("class", new Model<String>("icon-cancel"), " "));

        add(icon_plus = new WebMarkupContainer("icon-plus"));
        icon_plus.setOutputMarkupId(true);
        icon_plus.add(new AttributeAppender("class", new Model<String>("icon-plus"), " "));

        add(icon_plus_squared = new WebMarkupContainer("icon-plus-squared"));
        icon_plus_squared.setOutputMarkupId(true);
        icon_plus_squared.add(new AttributeAppender("class", new Model<String>("icon-plus-squared"), " "));

        add(icon_minus = new WebMarkupContainer("icon-minus"));
        icon_minus.setOutputMarkupId(true);
        icon_minus.add(new AttributeAppender("class", new Model<String>("icon-minus"), " "));

        add(icon_minus_squared = new WebMarkupContainer("icon-minus-squared"));
        icon_minus_squared.setOutputMarkupId(true);
        icon_minus_squared.add(new AttributeAppender("class", new Model<String>("icon-minus-squared"), " "));

        add(icon_help = new WebMarkupContainer("icon-help"));
        icon_help.setOutputMarkupId(true);
        icon_help.add(new AttributeAppender("class", new Model<String>("icon-help"), " "));

        add(icon_home = new WebMarkupContainer("icon-home"));
        icon_home.setOutputMarkupId(true);
        icon_home.add(new AttributeAppender("class", new Model<String>("icon-home"), " "));

        add(icon_link = new WebMarkupContainer("icon-link"));
        icon_link.setOutputMarkupId(true);
        icon_link.add(new AttributeAppender("class", new Model<String>("icon-link"), " "));

        add(icon_attach = new WebMarkupContainer("icon-attach"));
        icon_attach.setOutputMarkupId(true);
        icon_attach.add(new AttributeAppender("class", new Model<String>("icon-attach"), " "));

        add(icon_lock = new WebMarkupContainer("icon-lock"));
        icon_lock.setOutputMarkupId(true);
        icon_lock.add(new AttributeAppender("class", new Model<String>("icon-lock"), " "));

        add(icon_lock_open = new WebMarkupContainer("icon-lock-open"));
        icon_lock_open.setOutputMarkupId(true);
        icon_lock_open.add(new AttributeAppender("class", new Model<String>("icon-lock-open"), " "));

        add(icon_download = new WebMarkupContainer("icon-download"));
        icon_download.setOutputMarkupId(true);
        icon_download.add(new AttributeAppender("class", new Model<String>("icon-download"), " "));

        add(icon_upload = new WebMarkupContainer("icon-upload"));
        icon_upload.setOutputMarkupId(true);
        icon_upload.add(new AttributeAppender("class", new Model<String>("icon-upload"), " "));

        add(icon_export = new WebMarkupContainer("icon-export"));
        icon_export.setOutputMarkupId(true);
        icon_export.add(new AttributeAppender("class", new Model<String>("icon-export"), " "));

        add(icon_pencil = new WebMarkupContainer("icon-pencil"));
        icon_pencil.setOutputMarkupId(true);
        icon_pencil.add(new AttributeAppender("class", new Model<String>("icon-pencil"), " "));

        add(icon_print = new WebMarkupContainer("icon-print"));
        icon_print.setOutputMarkupId(true);
        icon_print.add(new AttributeAppender("class", new Model<String>("icon-print"), " "));

        add(icon_comment = new WebMarkupContainer("icon-comment"));
        icon_comment.setOutputMarkupId(true);
        icon_comment.add(new AttributeAppender("class", new Model<String>("icon-comment"), " "));

        add(icon_attention = new WebMarkupContainer("icon-attention"));
        icon_attention.setOutputMarkupId(true);
        icon_attention.add(new AttributeAppender("class", new Model<String>("icon-attention"), " "));

        add(icon_location = new WebMarkupContainer("icon-location"));
        icon_location.setOutputMarkupId(true);
        icon_location.add(new AttributeAppender("class", new Model<String>("icon-location"), " "));

        add(icon_map = new WebMarkupContainer("icon-map"));
        icon_map.setOutputMarkupId(true);
        icon_map.add(new AttributeAppender("class", new Model<String>("icon-map"), " "));

        add(icon_cog = new WebMarkupContainer("icon-cog"));
        icon_cog.setOutputMarkupId(true);
        icon_cog.add(new AttributeAppender("class", new Model<String>("icon-cog"), " "));

        add(icon_arrow_combo = new WebMarkupContainer("icon-arrow-combo"));
        icon_arrow_combo.setOutputMarkupId(true);
        icon_arrow_combo.add(new AttributeAppender("class", new Model<String>("icon-arrow-combo"), " "));

        add(icon_down_open = new WebMarkupContainer("icon-down-open"));
        icon_down_open.setOutputMarkupId(true);
        icon_down_open.add(new AttributeAppender("class", new Model<String>("icon-down-open"), " "));

        add(icon_left_open = new WebMarkupContainer("icon-left-open"));
        icon_left_open.setOutputMarkupId(true);
        icon_left_open.add(new AttributeAppender("class", new Model<String>("icon-left-open"), " "));

        add(icon_right_open = new WebMarkupContainer("icon-right-open"));
        icon_right_open.setOutputMarkupId(true);
        icon_right_open.add(new AttributeAppender("class", new Model<String>("icon-right-open"), " "));

        add(icon_up_open = new WebMarkupContainer("icon-up-open"));
        icon_up_open.setOutputMarkupId(true);
        icon_up_open.add(new AttributeAppender("class", new Model<String>("icon-up-open"), " "));

        add(icon_down_open_mini = new WebMarkupContainer("icon-down-open-mini"));
        icon_down_open_mini.setOutputMarkupId(true);
        icon_down_open_mini.add(new AttributeAppender("class", new Model<String>("icon-down-open-mini"), " "));

        add(icon_left_open_mini = new WebMarkupContainer("icon-left-open-mini"));
        icon_left_open_mini.setOutputMarkupId(true);
        icon_left_open_mini.add(new AttributeAppender("class", new Model<String>("icon-left-open-mini"), " "));

        add(icon_right_open_mini = new WebMarkupContainer("icon-right-open-mini"));
        icon_right_open_mini.setOutputMarkupId(true);
        icon_right_open_mini.add(new AttributeAppender("class", new Model<String>("icon-right-open-mini"), " "));

        add(icon_up_open_mini = new WebMarkupContainer("icon-up-open-mini"));
        icon_up_open_mini.setOutputMarkupId(true);
        icon_up_open_mini.add(new AttributeAppender("class", new Model<String>("icon-up-open-mini"), " "));

        add(icon_down_thin = new WebMarkupContainer("icon-down-thin"));
        icon_down_thin.setOutputMarkupId(true);
        icon_down_thin.add(new AttributeAppender("class", new Model<String>("icon-down-thin"), " "));

        add(icon_left_thin = new WebMarkupContainer("icon-left-thin"));
        icon_left_thin.setOutputMarkupId(true);
        icon_left_thin.add(new AttributeAppender("class", new Model<String>("icon-left-thin"), " "));

        add(icon_right_thin = new WebMarkupContainer("icon-right-thin"));
        icon_right_thin.setOutputMarkupId(true);
        icon_right_thin.add(new AttributeAppender("class", new Model<String>("icon-right-thin"), " "));

        add(icon_up_thin = new WebMarkupContainer("icon-up-thin"));
        icon_up_thin.setOutputMarkupId(true);
        icon_up_thin.add(new AttributeAppender("class", new Model<String>("icon-up-thin"), " "));

        add(icon_target = new WebMarkupContainer("icon-target"));
        icon_target.setOutputMarkupId(true);
        icon_target.add(new AttributeAppender("class", new Model<String>("icon-target"), " "));

        add(icon_camera = new WebMarkupContainer("icon-camera"));
        icon_camera.setOutputMarkupId(true);
        icon_camera.add(new AttributeAppender("class", new Model<String>("icon-camera"), " "));

    }

    @Override
    protected void addBreadCrumbBar(String breadCrumbBarId) {
        add(new BreadCrumbBar(breadCrumbBarId,
                new NavigationItem(new FIDLabelModel("label.dashboard"), DashboardPage.class),
                new NavigationItem(Model.of("Next page in hierarchy"), TemplatePage.class),
                new NavigationItem(new FIDLabelModel("label.current_page"), FontelloFonts.class)
        ));
    }
}
