require "org_connection"
require "typed_org_connection"
class RemoveSecondaryOrgSafetyNetworkConnections < ActiveRecord::Migration
  def self.up
    OrgConnection.transaction do
      connections = OrgConnection.find(:all)
      connections_with_secondaries = connections.select do |connection|
        connection.vendor.secondary?  
      end
      connections_with_secondaries.each do |connection|
        remove_connection(connection)
      end
    end
  end
  
  
  def self.remove_connection(connection) 
    TypedOrgConnection.delete_all(:orgconnection_id => connection.id)
    connection.delete
  end
  
  
  def self.down
    
  end
end