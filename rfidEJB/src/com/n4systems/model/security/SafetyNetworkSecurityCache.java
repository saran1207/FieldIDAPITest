package com.n4systems.model.security;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.safetynetwork.OrgConnection;
import com.n4systems.util.HashCode;

/**
 * A cache view of all safety network connections.  The main purpose for this class is
 * for the {@link SafetyNetworkSecurityCache#getConnectionSecurityLevel(InternalOrg, InternalOrg)} method
 * which determines the network distance from one InternalOrg to another. 
 */
public class SafetyNetworkSecurityCache {
	private static final SafetyNetworkSecurityCache self = new SafetyNetworkSecurityCache();
	
	public static SafetyNetworkSecurityCache getInstance() {
		return self;
	}
	
	/**
	 * Convenience method for {@link SafetyNetworkSecurityCache#getInstance()#getConnectionSecurityLevel(InternalOrg, InternalOrg)}
	 */
	public static SecurityLevel getSecurityLevel(InternalOrg from, InternalOrg to) {
		return self.getConnectionSecurityLevel(from, to);
	}
	
	private SafetyNetworkSecurityCache() {}
	
	/**
	 * This is a flat view of all org connections.  Both primary as well as secondary
	 * orgs will be in this list.
	 */
	private final Set<OrgNode> nodes = new CopyOnWriteArraySet<OrgNode>();
	
	/** Connects two InternalOrg together in a bi-directional connection. */
	public void connect(OrgConnection conn) {
		connect(conn.getVendor(), conn.getCustomer());
	}
	
	/** Connects two InternalOrg together in a bi-directional connection. */
	public void connect(InternalOrg org1, InternalOrg org2) {
		// connect these nodes directly together
		connect(findOrCreate(org1), findOrCreate(org2));
	}
	
	/**
	 * Returns the network distance (SecurityLevel) from one org to another.
	 * @param from	The org to search from
	 * @param to	The org to find the distance to
	 * @return		SecurityLevel representing the distance
	 */
	public SecurityLevel getConnectionSecurityLevel(InternalOrg from, InternalOrg to) {
		return getConnectionSecurityLevel(findOrCreate(from), findOrCreate(to));
	}
	
	/** Connects two nodes together, added each to each others connection list */
	private void connect(OrgNode node1, OrgNode node2) {
		// add the connection on both sides
		node1.addConnection(node2);
		node2.addConnection(node1);
	}
	
	/** Finds the SecurityLevel distance from one org to another */
	private SecurityLevel getConnectionSecurityLevel(OrgNode from, OrgNode to) {
		// since these links are bi-directional, it doesn't matter which one we get the SecurityLevel from
		return from.getConnectionSecurityLevel(to);
	}
	
	/** Finds a node in the node list for this orgs id */
	private OrgNode find(InternalOrg org) {
		for (OrgNode node: nodes) {
			if (node.getOrgId().equals(org.getId())) {
				return node;
			}
		}
		return null;
	}
	
	/** 
	 * Creates an org node and adds it to the node list.  If this is a secondary org,
	 * the primary will be found (or created) and this org will be added to the primaries
	 * secondary node list
	 */
	private OrgNode create(InternalOrg org) {
		OrgNode node = new OrgNode(org);
		nodes.add(node);
		
		// if the node is a secondary, we need to add it to the primary node
		if (org.isSecondary()) {
			OrgNode primaryNode = findOrCreate(org.getPrimaryOrg());
			primaryNode.addSecondary(node);
		}
		
		return node;
	}
	
	/** Attempts to find an org in the node list, creating it if not */
	private OrgNode findOrCreate(InternalOrg org) {
		OrgNode node = find(org);
		// if the node was not found, create and return
		return (node != null) ? node : create(org);
	}
	
	/** Removes all nodes from the node list */
	public void clear() {
		nodes.clear();
	}
	
	/** Represents an InternalOrg and all of its connections. */
	private class OrgNode {
		private final Long tenantId;
		private final Long orgId;
		private final Set<OrgNode> secondaries = new CopyOnWriteArraySet<OrgNode>();
		private final Set<OrgNode> connections = new CopyOnWriteArraySet<OrgNode>();
		
		public OrgNode(InternalOrg org) {
			this(org.getTenant().getId(), org.getId());
		}
		
		public OrgNode(Long tenantId, Long orgId) {
			this.tenantId = tenantId;
			this.orgId = orgId;
		}

		public Long getOrgId() {
			return orgId;
		}
		
		public boolean isConnected(OrgNode node) {
			for (OrgNode connectedNode: connections) {
				if (connectedNode.equals(node)) {
					return true;
				}
			}
			return false;
		}
		
		public void addSecondary(OrgNode node) {
			secondaries.add(node);
		}

		public void addConnection(OrgNode node) {
			connections.add(node);
		}
		
		public SecurityLevel getConnectionSecurityLevel(OrgNode node) {
			// if this is under the same tenant, it's LOCAL
			if (tenantId.equals(node.tenantId)) {
				return SecurityLevel.LOCAL;
			}
			
			// the node is directly connected
			if (isConnected(node)) {
				return SecurityLevel.DIRECT;
			}
			
			// if the node is direct to one of my secondary nodes (meaning I'm a primary), it's also direct for me.
			// note this list will simply be empty for secondary nodes
			for (OrgNode secondary: secondaries) {
				if (secondary.isConnected(node)) {
					return SecurityLevel.DIRECT;
				}
			}
			
			// if the node is directly connected to one of my connections, it's one away
			for (OrgNode connection: connections) {
				if (connection.isConnected(node)) {
					return SecurityLevel.ONE_AWAY;
				}
			}
			
			/*
			 * The node is more then 1 away.  In fact it may not be connected at all
			 * but it would be to inefficient to find exactly if and how far .. also we don't care 
			 */
			return SecurityLevel.MANY_AWAY;
		}
		
		@Override
		public boolean equals(Object other) {
			OrgNode otherNode = (OrgNode)other;
			return (tenantId.equals(otherNode.tenantId) && orgId.equals(otherNode.orgId));
		}

		@Override
		public int hashCode() {
			return HashCode.newHash().add(tenantId).add(orgId).toHash();
		}
	}
}
