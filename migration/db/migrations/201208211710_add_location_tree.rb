class AddLocationTree < ActiveRecord::Migration

  def self.up
    execute("ALTER TABLE predefinedlocations add owner_id bigint(20) NULL;")
    execute("ALTER TABLE predefinedlocations ADD CONSTRAINT fk_predefined_location_org FOREIGN KEY fk_predefined_location_org(owner_id) REFERENCES org_base(id) on update no action on delete no action")

    # by default, set all predefined locations to primary org as parent.
    execute("update predefinedlocations loc set owner_id = (select id from org_base org where loc.tenant_id = org.id);")
  end

  def self.down
    execute("alter table predefinedlocations drop  foreign key fk_predefined_location_org");
    remove_column(:predefinedlocations, :owner_id);
  end
end