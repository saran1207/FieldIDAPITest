class RemoveConstraintOnJobsToAssets < ActiveRecord::Migration 
  def self.up
   add_index(:projects_products, [:projects_id, :products_id])
   execute "ALTER TABLE projects_products DROP CONSTRAINT projects_products_pkey"
  end
  
  def self.down
    
  end
end