
class FixInspectionBooks < ActiveRecord::Migration
  def self.up
    execute "update inspectionbooks set name=CONCAT(name, '_1') where name = 'Scozinc' limit 1";
    add_index "inspectionbooks", ["customer_id", "name", "r_tenant"], :name => "index_inspectionbooks_on_r_tenant_and_customer_uniqueid_and_nam", :unique => true

  end
  
  def self.down
  end
end