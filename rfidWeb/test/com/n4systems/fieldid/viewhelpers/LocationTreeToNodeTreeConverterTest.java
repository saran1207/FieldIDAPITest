package com.n4systems.fieldid.viewhelpers;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.internal.matchers.TypeSafeMatcher;

import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.location.PredefinedLocationLevels;
import com.n4systems.model.location.PredefinedLocationTree;
import com.n4systems.model.location.PredefinedLocationTreeNode;
import com.n4systems.uitags.views.HeirarchicalNode;


public class LocationTreeToNodeTreeConverterTest {
	private class TreeStats {
		private int maxDepth = 0;
		private int nodeCount = 0;
		
		public void atDepth(int depth) {
			if (depth > maxDepth) {
				maxDepth = depth;
			}
		}
		
		public void incrementNodeCount() {
			nodeCount++;
		}
	}
	
	
	private final class IsEmptyCollection<T> extends TypeSafeMatcher<Collection<T>> {
		private IsEmptyCollection(Class<T> clazz) {
		}
		
		
		@Override
		public boolean matchesSafely(Collection<T> item) {
			
			return item.isEmpty();
		}

		public void describeTo(Description description) {
			description.appendText("emtpy collection");
			
		}
	}


	@Test(expected=RuntimeException.class)
	public void should_fail_if_the_tree_is_null() throws Exception {
		new LocationTreeToHeirarchicalNodesConverter().convert(null, new PredefinedLocationLevels());
	}
	
	
	@Test
	public void should_convert_an_empty_location_tree_into_an_empty_list_of_nodes() throws Exception {
		List<HeirarchicalNode> convertedTree = new LocationTreeToHeirarchicalNodesConverter().convert(anEmtpyPredefinedLocationTree(), new PredefinedLocationLevels());
		assertThat(convertedTree, anEmptyList(HeirarchicalNode.class));
	}
	
	
	@Test
	public void should_convert_an_location_tree_with_one_location_into_a_list_with_just_one_node() throws Exception {
		PredefinedLocationTree locationTree = anEmtpyPredefinedLocationTree();
		
		PredefinedLocation locationNode = new PredefinedLocation();
		locationNode.setName("name");
		locationNode.setId(1L);
		
		locationTree.addNode(new PredefinedLocationTreeNode(locationNode));
		
		List<HeirarchicalNode> convertedTree = new LocationTreeToHeirarchicalNodesConverter().convert(locationTree, new PredefinedLocationLevels());
		
		assertThat(new ArrayList<Object>(convertedTree), hasItem(node("name", 1L)));
	}
	
	@Test
	public void should_convert_an_location_tree_with_multiple_levels_of_nodes_into_a_tree_of_nodes_matching() throws Exception {
		
		
		PredefinedLocationTreeNode locationNode = aPredefinedLocationTreeNode();
		PredefinedLocationTreeNode locationNode2 = aPredefinedLocationTreeNode();
		locationNode.addChild(locationNode2);
		PredefinedLocationTreeNode locationNode3 = aPredefinedLocationTreeNode();
		locationNode2.addChild(locationNode3);
		
		
		
		PredefinedLocationTree locationTree = aPredefinedLocationTreeWithTopLevelLocations(locationNode);
		
		
		List<HeirarchicalNode> convertedTree = new LocationTreeToHeirarchicalNodesConverter().convert(locationTree, new PredefinedLocationLevels());
		
		assertThat(convertedTree, hasNodeTree(3,3));
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Test
	public void should_order_each_level_of_nodes_by_name_alphabetically_ignoring_case() throws Exception {
		
		PredefinedLocationTreeNode locationNode_Bob = aPredefinedLocationTreeNodeNamed("Bob");
		PredefinedLocationTreeNode locationNode_alex = aPredefinedLocationTreeNodeNamed("alex");
		PredefinedLocationTreeNode locationNode_Matt = aPredefinedLocationTreeNodeNamed("Matt");
		PredefinedLocationTreeNode locationNode_47_fraiser = aPredefinedLocationTreeNodeNamed("47 fraiser st");
		PredefinedLocationTreeNode locationNode_mark = aPredefinedLocationTreeNodeNamed("mark");
		
		PredefinedLocationTree locationTree = aPredefinedLocationTreeWithTopLevelLocations(locationNode_Bob, locationNode_mark, locationNode_alex, locationNode_Matt, locationNode_47_fraiser);
		
		List<HeirarchicalNode> convertedTree = new LocationTreeToHeirarchicalNodesConverter().convert(locationTree, new PredefinedLocationLevels());
		
		assertThat((Iterable<?>)convertedTree, contains(hasProperty("name", equalTo("47 fraiser st")),
										hasProperty("name", equalTo("alex")),
										hasProperty("name", equalTo("Bob")),
										hasProperty("name", equalTo("mark")),
										hasProperty("name", equalTo("Matt"))
				));
	}


	private  Matcher<Iterable<?>> contains(final Matcher<? super Object> ... elementMatchers) {
		
		return new TypeSafeMatcher<Iterable<?>>() {

			@Override
			public boolean matchesSafely(Iterable<?> iterable) {
				Iterator<?> iterator = iterable.iterator();
				for (Matcher<? super Object> matcher : elementMatchers) {
					if (!iterator.hasNext()) {
						return false;
					}
					
					if (!matcher.matches(iterator.next())) {
						return false;
					}
				}
				return (!iterator.hasNext());
			}

			public void describeTo(Description description) {
				for (Matcher<? super Object> matcher : elementMatchers) {
					description.appendDescriptionOf(matcher).appendText(" ");
					
				}
			}
		};
	}


	private PredefinedLocationTree aPredefinedLocationTreeWithTopLevelLocations(PredefinedLocationTreeNode ... locations) {
		PredefinedLocationTree locationTree = anEmtpyPredefinedLocationTree();
		for (PredefinedLocationTreeNode location : locations) {
			locationTree.addNode(location);
		}
		return locationTree;
	}


	private PredefinedLocationTreeNode aPredefinedLocationTreeNodeNamed(String name) {
		PredefinedLocation location = new PredefinedLocation();
		location.setName(name);
		
		return new PredefinedLocationTreeNode(location);
	}


	private PredefinedLocationTreeNode aPredefinedLocationTreeNode() {
		return new PredefinedLocationTreeNode(new PredefinedLocation());
	}
	
	

	private Matcher<List<HeirarchicalNode>> hasNodeTree(final int totalNodes, final int maxDepth) {
		
		return new TypeSafeMatcher<List<HeirarchicalNode>>() {

			@Override
			public boolean matchesSafely(List<HeirarchicalNode> item) {
				TreeStats stats;
				stats = new TreeStats();
				
				for (HeirarchicalNode basicNode : item) {
					stats.incrementNodeCount();
					getStatsForNode(stats, basicNode, 1);
				}
				
				
				return (stats.maxDepth == maxDepth && stats.nodeCount == maxDepth);
			}

			private void getStatsForNode(TreeStats stats, HeirarchicalNode basicNode, int level) {
				stats.atDepth(level);
				for (HeirarchicalNode child : basicNode.getChildren()) {
					stats.incrementNodeCount();
					getStatsForNode(stats, child, level + 1);
				}
			}

			public void describeTo(Description description) {
				description.appendText("a tree with max depth ").appendValue(maxDepth).appendText(" and node count of ").appendValue(totalNodes);
				
			}
		};
	}


	@SuppressWarnings("unchecked")
	private Matcher<Object> node(String name, Long id) {
		return allOf(hasProperty("name", equalTo(name)), hasProperty("id", equalTo(id)));
	}




	private <T> Matcher<Collection<T>> anEmptyList(Class<T> clazz) {
		return new IsEmptyCollection<T>(clazz);
	}


	private PredefinedLocationTree anEmtpyPredefinedLocationTree() {
		return new PredefinedLocationTree();
	}
}
