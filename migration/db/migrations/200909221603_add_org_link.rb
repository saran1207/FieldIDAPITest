class AddOrgLink < ActiveRecord::Migration
  def self.up
    add_column(:org_customer, :linked_id, :integer)
    add_foreign_key(:org_customer, :org_base,  :source_column => :linked_id, :foreign_column => :id, :name => "fk_customer_linked_org")
    
    add_column(:org_division, :linked_id, :integer)
    add_foreign_key(:org_division, :org_base,  :source_column => :linked_id, :foreign_column => :id, :name => "fk_division_linked_org")
  end
  
  def self.down
    drop_foreign_key(:org_customer, :org_base, :name => "fk_customer_linked_org")
    remove_column(:org_customer, :linked_id)
    
    drop_foreign_key(:org_division, :org_base, :name => "fk_division_linked_org")
    remove_column(:org_division, :linked_id)
  end
end