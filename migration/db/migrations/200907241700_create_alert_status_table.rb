class CreateAlertStatusTable < ActiveRecord::Migration
  
  def self.up
    create_table "alertstatus", :id => false do |t|
      t.integer :r_tenant, :null => false
      t.integer :diskspace, :null => false
    end

    execute("ALTER TABLE alertstatus ADD PRIMARY KEY (r_tenant)")
    foreign_key(:alertstatus, :r_tenant, :organization, :id)
    execute("CREATE UNIQUE INDEX idx_alertstatus_tenantid ON alertstatus (r_tenant);")
  end
  
  def self.down
    drop_table(:alertstatus)
  end
end