class CreateSetupDataLastModDatesTable < ActiveRecord::Migration
  
  def self.up
    create_table "setupdatalastmoddates", :id => false do |t|
      t.integer :r_tenant, :null => false
      t.timestamp :producttypes, :null => false
      t.timestamp :inspectiontypes, :null => false
      t.timestamp :autoattributes, :null => false
      t.timestamp :owners, :null => false
    end

    execute("ALTER TABLE setupdatalastmoddates ADD PRIMARY KEY (r_tenant)")
    foreign_key(:setupdatalastmoddates, :r_tenant, :organization, :id)

  end
  
  def self.down
    drop_table(:setupdatalastmoddates)
  end
end