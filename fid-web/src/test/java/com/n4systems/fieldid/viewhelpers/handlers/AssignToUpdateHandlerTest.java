package com.n4systems.fieldid.viewhelpers.handlers;

import static com.n4systems.model.builders.UserBuilder.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.model.inspection.AssignedToUpdate;


public class AssignToUpdateHandlerTest {

	private final class StaticHandler extends WebOutputHandler {
		public static final String handlerString = "Used Handler";

		private StaticHandler(AbstractAction action) {
			super(action);
		}

		public Object handleExcel(Long entityId, Object value) {
			return null;
		}

		@Override
		public String handleWeb(Long entityId, Object value) {
			return handlerString;
		}
	}


	private static final long EVENT_ID = 1L;
	private static final String BLANK = "";
	private static final Object NO_ASSIGNED_TO_UPDATE = BLANK;
	private static final AbstractAction UNUSED_ABSTRACT_ACTION = null;

	@Test
	public void should_render_blank_when_assign_to_update_was_not_done() throws Exception {
		WebOutputHandler sut = new AssignedToUpdateHandler(UNUSED_ABSTRACT_ACTION);
		assertThat(sut .handleWeb(EVENT_ID, NO_ASSIGNED_TO_UPDATE), equalTo(BLANK));
	}
	
	
	@Test
	public void should_have_assign_to_update_render_the_assigned_user_when_assign_to_update_exists_and_for_unassigned() throws Exception {
		AssignedToUpdateHandler sut = new AssignedToUpdateHandler(UNUSED_ABSTRACT_ACTION);
		sut.setAssignToHandler(new StaticHandler(UNUSED_ABSTRACT_ACTION));
		assertThat(sut.handleWeb(EVENT_ID, AssignedToUpdate.unassignAsset()), equalTo(StaticHandler.handlerString));
	}
	
	@Test
	public void should_have_assign_to_update_render_the_assigned_user_when_assign_to_update_exists_and_for_an_user() throws Exception {
		AssignedToUpdateHandler sut = new AssignedToUpdateHandler(UNUSED_ABSTRACT_ACTION);
		sut.setAssignToHandler(new StaticHandler(UNUSED_ABSTRACT_ACTION));
		
		String renderedString = sut.handleWeb(EVENT_ID, AssignedToUpdate.assignAssetToUser(anEmployee().build()));
		
		assertThat(renderedString, equalTo(StaticHandler.handlerString));
	}
}
