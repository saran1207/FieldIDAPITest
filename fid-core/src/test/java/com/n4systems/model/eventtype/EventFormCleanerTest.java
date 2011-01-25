package com.n4systems.model.eventtype;

import com.n4systems.model.CriteriaSection;
import com.n4systems.model.EventForm;
import com.n4systems.model.Tenant;
import com.n4systems.model.api.Cleaner;
import com.n4systems.model.builders.CriteriaSectionBuilder;
import com.n4systems.model.builders.TenantBuilder;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

public class EventFormCleanerTest {

    private static final List<CriteriaSection> sections= Arrays.asList(
            CriteriaSectionBuilder.aCriteriaSection().build(),
            CriteriaSectionBuilder.aCriteriaSection().withRetired(true).build(),
            CriteriaSectionBuilder.aCriteriaSection().build()
    );

    @Test
    public void test_clean_event_form() {
        EventForm eventForm = new EventForm();
        eventForm.setSections(sections);

        Tenant newTenant = TenantBuilder.aTenant().build();

        Cleaner<CriteriaSection> sectionCleaner = createMock(Cleaner.class);
        sectionCleaner.clean(sections.get(0));
        sectionCleaner.clean(sections.get(2));
        replay(sectionCleaner);

        EventFormCleaner cleaner = new EventFormCleaner(newTenant, sectionCleaner);
        cleaner.clean(eventForm);

        verify(sectionCleaner);
    }

}
