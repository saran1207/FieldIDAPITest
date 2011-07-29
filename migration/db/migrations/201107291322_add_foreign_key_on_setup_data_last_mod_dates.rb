class AddForeignKeyOnSetupDataLastModDates < ActiveRecord::Migration

  def self.up
  	add_foreign_key(:setupdatalastmoddates, :tenants, :source_column => :tenant_id, :foreign_column => :id)  	
  end

  def self.down
  end

end
