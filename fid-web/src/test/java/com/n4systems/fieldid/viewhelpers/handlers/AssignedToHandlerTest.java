package com.n4systems.fieldid.viewhelpers.handlers;

import static com.n4systems.model.builders.AssetBuilder.*;
import static com.n4systems.model.builders.UserBuilder.*;
import static org.easymock.EasyMock.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import com.n4systems.model.Asset;
import org.junit.Test;

import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.util.views.ExcelOutputHandler;


public class AssignedToHandlerTest {
	private static final String UNASSIGNED_LABEL = "label.unassigned";
	private final String UNASSIGNED_USER_FROM_QUERY = "";
	
	private final class ReturningLabelAbstractAction extends AbstractAction {
		private ReturningLabelAbstractAction() {
			super(null);
		}

		@Override
		public String getText(String key) {
			return key;
		}
	}



	private static final AbstractAction UNUSED_ABSTRACT_ACTION = null;

	@Test
	public void should_render_name_of_user_when_a_user_is_assigned_for_web() throws Exception {
		Asset asset = anAsset().assignedTo(anEmployee().build()).build();
		
		WebOutputHandler sut = new AssignedToHandler(UNUSED_ABSTRACT_ACTION);
		
		assertThat(sut.handleWeb(asset.getId(), asset.getAssignedUser()), containsString(asset.getAssignedUser().getUserLabel()));
	}
	
	@Test
	public void should_render_unassigned_when_a_null_is_given_for_user_for_web() throws Exception {
		Asset asset = anAsset().assignedTo(anEmployee().build()).build();
		
		WebOutputHandler sut = new AssignedToHandler(new ReturningLabelAbstractAction());
		
		assertThat(sut.handleWeb(asset.getId(), null), containsString(UNASSIGNED_LABEL));
	}
	
	
	
	@Test
	public void should_render_the_unassigned_label_for_an_asset_that_is_unassigned_for_web() throws Exception {
		Asset asset = anAsset().unassigned().build();
		
		WebOutputHandler sut = new AssignedToHandler(new ReturningLabelAbstractAction());
		
		assertThat(sut.handleWeb(asset.getId(), UNASSIGNED_USER_FROM_QUERY), containsString(UNASSIGNED_LABEL));
	}
	
	@Test
	public void should_resolve_the_unassigned_label_for_unassigned_throught_the_text_provider_for_web() throws Exception {
		Asset asset = anAsset().unassigned().build();
		
		AbstractAction textProvider = createMock(AbstractAction.class);
		expect(textProvider.getText(UNASSIGNED_LABEL)).andReturn("rendered label");
		replay(textProvider);
		
		WebOutputHandler sut = new AssignedToHandler(textProvider);
		
		sut.handleWeb(asset.getId(), UNASSIGNED_USER_FROM_QUERY);
		
		verify(textProvider);
	}
	
	
	@Test
	public void should_render_name_of_user_when_a_user_is_assigned_for_excel() throws Exception {
		Asset asset = anAsset().assignedTo(anEmployee().build()).build();
		
		ExcelOutputHandler sut = new AssignedToHandler(UNUSED_ABSTRACT_ACTION);
		
		assertThat(sut.handleExcel(asset.getId(), asset.getAssignedUser()).toString(), containsString(asset.getAssignedUser().getUserLabel()));
	}
	
	
	
	@Test
	public void should_render_the_unassigned_label_for_an_asset_that_is_unassigned_for_excel() throws Exception {
		Asset asset = anAsset().unassigned().build();
		
		ExcelOutputHandler sut = new AssignedToHandler(new ReturningLabelAbstractAction());
		
		assertThat(sut.handleExcel(asset.getId(), UNASSIGNED_USER_FROM_QUERY).toString(), containsString(UNASSIGNED_LABEL));
	}
	
	@Test
	public void should_resolve_the_unassigned_label_for_unassigned_throught_the_text_provider_for_excel() throws Exception {
		Asset asset = anAsset().unassigned().build();
		
		AbstractAction textProvider = createMock(AbstractAction.class);
		expect(textProvider.getText(UNASSIGNED_LABEL)).andReturn("rendered label");
		replay(textProvider);
		
		ExcelOutputHandler sut = new AssignedToHandler(textProvider);
		
		sut.handleExcel(asset.getId(), UNASSIGNED_USER_FROM_QUERY);
		
		verify(textProvider);
	}
	
}
