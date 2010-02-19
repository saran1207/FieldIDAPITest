class RemoveUnusedTables < ActiveRecord::Migration
  def self.up
    drop_table(:legacybuttonstatemappings)
    drop_table(:divisions)
    drop_table(:customers)
    drop_table(:organization_extendedfeatures)
    drop_table(:tenantlink)
    drop_foreign_key(:organization , :organization, :source_column => :parent_id, :foreign_column => :id, :name => "fk_organization_parent")
    drop_foreign_key(:organization , :organization, :source_column => :r_tenant, :foreign_column => :id, :name => "fk_organization_tenant")
    drop_table(:organization)
  end
end