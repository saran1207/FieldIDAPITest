class AddSetupDataLastModDatesIndex < ActiveRecord::Migration
  
  def self.up

    execute("CREATE UNIQUE INDEX idx_setupdatalastmoddates_tenantid ON setupdatalastmoddates (r_tenant);")

  end
  
  def self.down
    
    execute("DROP INDEX idx_setupdatalastmoddates_tenantid;")
    
  end
end