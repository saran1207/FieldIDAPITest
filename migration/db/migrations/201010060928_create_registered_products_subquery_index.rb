class CreateRegisteredProductsSubqueryIndex < ActiveRecord::Migration

  def self.up
    add_index "products", ["state", "tenant_id", "linked_id"], :name => "index_products_state_tenantid_linkedid"
  end


  def self.down
    remove_index "products", "index_products_state_tenantid_linkedid"
  end

end