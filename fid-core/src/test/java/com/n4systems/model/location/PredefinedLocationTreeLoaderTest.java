package com.n4systems.model.location;

import com.google.common.collect.ImmutableList;
import com.n4systems.model.builders.OrgBuilder;
import com.n4systems.persistence.Transaction;
import com.n4systems.testutils.DummyTransaction;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.internal.matchers.TypeSafeMatcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import static com.n4systems.model.builders.TenantBuilder.aTenant;
import static com.n4systems.model.location.EmptyPredefinedLocationTreeMatcher.anEmptyLocationTree;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertThat;


public class PredefinedLocationTreeLoaderTest {

	private static final ArrayList<PredefinedLocation> EMPTY_LIST = new ArrayList<PredefinedLocation>();

	
	@Test
	public void should_create_just_a_root_node_when_there_are_no_predefined_locations() throws Exception {
        Transaction transaction = new DummyTransaction();

        PredefinedLocationListLoader loader = createMock(PredefinedLocationListLoader.class);
		expect(loader.load(transaction.getEntityManager())).andReturn(EMPTY_LIST);
		replay(loader);
		
		PredefinedLocationTreeLoader sut = new PredefinedLocationTreeLoader(loader);
		
		PredefinedLocationTree root = sut.load(transaction);
		
		assertThat(root, anEmptyLocationTree());
	}
	
	
	@Test
	public void should_create_just_a_root_node_with_a_set_of_nodes_when_all_locations_are_in_the_first_level() throws Exception {
        Transaction transaction = new DummyTransaction();

        PredefinedLocationListLoader loader = createMock(PredefinedLocationListLoader.class);
		expect(loader.load(transaction.getEntityManager())).andReturn(ImmutableList.of(aLevelOneLocation(), aLevelOneLocation()));
		replay(loader);
		
		PredefinedLocationTreeLoader sut = new PredefinedLocationTreeLoader(loader);
		
		PredefinedLocationTree root = sut.load(transaction);
		
		assertThat(root.getNodes(), hasNodesAllMatch(leafNode()));
	}
	
	
	@Test
	public void should_create_a_tree_with_all_multiple_levels_of_nodes() throws Exception {
        Transaction transaction = new DummyTransaction();


        PredefinedLocation aLevelOneLocation = aLevelOneLocation();
		PredefinedLocation aLevelTwoLocation = aLocationWithParent(aLevelOneLocation);
		PredefinedLocation aLevelThreeLocation = aLocationWithParent(aLevelTwoLocation);
		
		
		PredefinedLocationListLoader loader = createMock(PredefinedLocationListLoader.class);
		expect(loader.load(transaction.getEntityManager())).andReturn(ImmutableList.of(aLevelOneLocation, aLevelTwoLocation, aLevelThreeLocation));
		replay(loader);
		
		PredefinedLocationTreeLoader sut = new PredefinedLocationTreeLoader(loader);
		
		PredefinedLocationTree root = sut.load(transaction);
		
		assertThat(root.getNodes(), aTree(3,1));
	}
	
	
	@Test
	public void should_create_a_tree_with_all_multiple_nodes_per_level() throws Exception {
        Transaction transaction = new DummyTransaction();

        PredefinedLocation aLevelOneLocation = aLevelOneLocation();
		PredefinedLocation aSecondeLevelOneLocation = aLevelOneLocation();
		PredefinedLocation aLevelTwoLocation = aLocationWithParent(aLevelOneLocation);
		PredefinedLocation aSecondLevelTwoLocation = aLocationWithParent(aLevelOneLocation);
		PredefinedLocation aThirdLevelTwoLocation = aLocationWithParent(aSecondeLevelOneLocation);
		PredefinedLocation aForthLevelTwoLocation = aLocationWithParent(aSecondeLevelOneLocation);
		
		PredefinedLocationListLoader loader = createMock(PredefinedLocationListLoader.class);
		expect(loader.load(transaction.getEntityManager())).andReturn(ImmutableList.of(aLevelOneLocation, aSecondeLevelOneLocation,  aLevelTwoLocation, aSecondLevelTwoLocation, aThirdLevelTwoLocation, aForthLevelTwoLocation));
		replay(loader);
		
		PredefinedLocationTreeLoader sut = new PredefinedLocationTreeLoader(loader);
		
		PredefinedLocationTree root = sut.load(transaction);
		
		assertThat(root.getNodes(), aTree(2,2));
	}


	@Test
	public void should_use_loader_to_get_list_of_predefined_locations_in_parent_first_order() throws Exception {
        Transaction transaction = new DummyTransaction();
        PredefinedLocationListLoader loader = createMock(PredefinedLocationListLoader.class);
		expect(loader.load(transaction.getEntityManager())).andReturn(EMPTY_LIST);
		replay(loader);
		
		PredefinedLocationTreeLoader sut = new PredefinedLocationTreeLoader(loader);
		
		sut.load(transaction);
		
		verify(loader);
	}
	
	
	private Matcher<Set<PredefinedLocationTreeNode>> aTree(final int levels, final int childrenPerNode) {
		return new TypeSafeMatcher<Set<PredefinedLocationTreeNode>>() {

			@Override
			public boolean matchesSafely(Set<PredefinedLocationTreeNode> items) {
				
				return childrenMatch(items, levels, childrenPerNode);
			}

			private boolean childrenMatch(Set<PredefinedLocationTreeNode> nodes, int levels, int childrenPerNode) {
				if (levels >= 1 && nodes.size() != childrenPerNode) {
					return false;
				}
				
				if (levels == 0 && !nodes.isEmpty()) {
					return false;
				}
				
				for (PredefinedLocationTreeNode predefinedLocationTreeNode : nodes) {
					if (!childrenMatch(predefinedLocationTreeNode.getChildren(), levels - 1, childrenPerNode)) {
						return false;
					}
				}
				return true;
				
			}

			@Override
			public void describeTo(Description arg0) {
				arg0.appendText("a tree with ").appendValue(levels).appendText(" levels and ").appendValue(childrenPerNode).appendText(" children per node");
				
			}
			
		};
	}


	private PredefinedLocation aLocationWithParent(PredefinedLocation parent) {
		PredefinedLocation predefinedLocation = new PredefinedLocation(aTenant().build(), parent, OrgBuilder.aPrimaryOrg().build());
		return predefinedLocation;
	}


	private Matcher<PredefinedLocationTreeNode> leafNode() {
		return new TypeSafeMatcher<PredefinedLocationTreeNode>() {

			@Override
			public boolean matchesSafely(PredefinedLocationTreeNode item) {
				return item.getChildren().isEmpty();
			}

			@Override
			public void describeTo(Description description) {
				description.appendText("node to be a leaf node and have no children");
				
			}
		};
		
	}


	private Matcher<Collection<PredefinedLocationTreeNode>> hasNodesAllMatch(final Matcher<PredefinedLocationTreeNode> matcher) {
		
		return new TypeSafeMatcher<Collection<PredefinedLocationTreeNode>>() {

			@Override
			public boolean matchesSafely(Collection<PredefinedLocationTreeNode> item) {
				for (PredefinedLocationTreeNode predefinedLocationTreeNode : item) {
					if (!matcher.matches(predefinedLocationTreeNode)) {
						return false;
					}
				}
				return true;
			}

			@Override
			public void describeTo(Description description) {
				description.appendText("items all to be");
				matcher.describeTo(description);
			}
		};
	}


	private PredefinedLocation aLevelOneLocation() {
		return new PredefinedLocation();
	}

}
