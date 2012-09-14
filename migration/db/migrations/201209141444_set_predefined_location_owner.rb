

class SetPredefinedLocationOwner < ActiveRecord::Migration

  def self.up
    execute("update predefinedlocations loc set owner_id = (select id from org_base org where loc.tenant_id = org.tenant_id and secondary_id is null and customer_id is null and division_id is null);");
  end

  def self.down
  end

end