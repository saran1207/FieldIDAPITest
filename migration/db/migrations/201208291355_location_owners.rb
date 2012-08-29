
class LocationOwners< ActiveRecord::Migration

  def self.up
    execute("update predefinedlocations loc set owner_id = (select id from org_base org where loc.tenant_id = org.id);")
  end

  def self.down

  end

end
