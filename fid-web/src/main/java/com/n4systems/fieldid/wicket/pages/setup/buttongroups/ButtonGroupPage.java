package com.n4systems.fieldid.wicket.pages.setup.buttongroups;

import com.n4systems.fieldid.service.event.ButtonGroupService;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * This is the Setup page for Button Groups.  It lists all available button groups and allows the users to edit them
 * and create new button groups.  Once you've made a button group, however, it's there forever.  You should be
 * punished for even thinking about deleting them.  Shame on you.
 *
 * Created by Jordan Heath on 2016-03-11.
 */
public class ButtonGroupPage extends FieldIDTemplatePage {

    @SpringBean
    private ButtonGroupService buttonGroupService;
}
