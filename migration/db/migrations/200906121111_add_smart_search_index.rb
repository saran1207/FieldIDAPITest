class AddSmartSearchIndex < ActiveRecord::Migration
  def self.up
    execute 'CREATE  INDEX "smart_search_rfid" ON "products" (r_tenant, upper(rfidnumber))'
    execute 'CREATE  INDEX "smart_search_customer_ref_number" ON "products" (r_tenant, upper(customerrefnumber))'
    execute 'CREATE  INDEX "smart_search_serial_number" ON "products" (r_tenant, upper(serialnumber))'
  end
  
  def self.down
    execute 'DROP INDEX "smart_search_rfid"'
    execute 'DROP INDEX "smart_search_serial_number"'
    execute 'DROP INDEX "smart_search_customer_ref_number"'
  end
end