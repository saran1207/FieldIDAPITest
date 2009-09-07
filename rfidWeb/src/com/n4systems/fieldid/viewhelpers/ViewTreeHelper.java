package com.n4systems.fieldid.viewhelpers;

import java.util.Collections;
import java.util.List;

import rfid.ejb.entity.UserBean;
import rfid.ejb.session.User;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.utils.UserComparator;

public class ViewTreeHelper {
	private final PersistenceManager persistenceManager;
	private final User userManager;
	
	public ViewTreeHelper(final PersistenceManager persistenceManager, final User userManager) {
		this.persistenceManager = persistenceManager;
		this.userManager = userManager;
	}
	
	/**
	 * Creates a view tree for a set of user information. 
	 * @see User#getOuterUserList(Long, Long, Long, Long, SecurityFilter)
	 * @see #addUserToTree(UserBean, ViewTree, SecurityFilter)
	 * @return				A constructed ViewTree of User id's
	 */
	public ViewTree<Long> getUserViewTree(Tenant tenant, Long ownerId, Long userId, SecurityFilter filter) {
		List<UserBean> users = userManager.getOuterUserList(tenant.getId(), ownerId, userId, filter);
		
		// sort the users by customer and division, so the tree nodes are added in order
		Collections.sort(users, new UserComparator());
		
		ViewTree<Long> treeRoot = new ViewTree<Long>(tenant.getId(), tenant.getDisplayName());

		// populate the tree
		for (UserBean user: users) {
			addUserToTree(user, treeRoot, filter);
		}
		
		return treeRoot;
	}
	
	/**
	 * Adds a user to the tree in the correct tree position.  Also resolves customer/division names when creating tree nodes.
	 * @param user		The user to add
	 * @param treeRoot	The root of the tree
	 * @param filter	a security filter used in customer/division name resolution
	 */
	private void addUserToTree(UserBean user, ViewTree<Long> treeRoot, SecurityFilter filter) {
		ViewTree<Long> node;
		
		// TODO: CUSTOMER_REFACTOR: This should be changed to whatever we use for customer selection
		// first we need to locate the tree node to add this user on
		if (user.getCustomerId() != null) {
			// find the customer node
			node = findOrCreateTreeNode(user.getCustomerId(), treeRoot);
			
			// if the node name is null, it's new, we need to find and set the customer name
			if (node.getNodeName() == null) {
				node.setNodeName(persistenceManager.findName(CustomerOrg.class, user.getCustomerId(), filter));
			}
			
			if (user.getDivisionId() != null) {
				// has customer and division, find the division node off the customer node
				node = findOrCreateTreeNode(user.getDivisionId(), node);
				
				// if the node name is null, it's new, we need to find and set the division name
				if (node.getNodeName() == null) {
					node.setNodeName(persistenceManager.findName(DivisionOrg.class, user.getDivisionId(), filter));
				}
			}
		} else {
			node = treeRoot;
		}
		
		node.getElements().add(user);
	}
	
	/**
	 * Finds or creates a ViewTree child node, by id.  If a child node gets created, it will be added to the treeNode
	 * @param nodeId	The id of a node
	 * @param treeNode	The node to look for children on
	 * @return			The found or created child node
	 */
	private <T> ViewTree<T> findOrCreateTreeNode(T nodeId, ViewTree<T> treeNode) {
		ViewTree<T> childNode = null;
		
		// try and find the node by id
		for (ViewTree<T> child: treeNode.getChildNodes()) {
			if (child.getNodeId().equals(nodeId)) {
				childNode = child;
				break;
			}
		}
		
		// if the node is still null, create a new one and add it as a child
		if (childNode == null) {
			childNode = new ViewTree<T>();
			childNode.setNodeId(nodeId);
			treeNode.getChildNodes().add(childNode);
		}
		
		return childNode;
	}
	

}
