class RenameInspectorToPerformedBy < ActiveRecord::Migration
  def self.up
   run_with_out_foreign_keys do
      drop_foreign_key_1 "inspectionsmaster", ["inspector_id"], "users", ["id"], :name => "fk_inspectionsmaster_users"
      
      rename_column :inspectionsmaster, :inspector_id, :performedby_id  
      
      add_foreign_key_1 "inspectionsmaster", ["performedby_id"], "users", ["id"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_inspectionsmaster_performed_bys"
    end
  end
  
  def self.down
    run_with_out_foreign_keys do
      
      drop_foreign_key_1 "inspectionsmaster", ["performedby_id"], "users", ["id"], :name => "fk_inspectionsmaster_performed_bys"
      
      rename_column :inspectionsmaster, :performedby_id, :inspector_id
      
      add_foreign_key_1 "inspectionsmaster", ["inspector_id"], "users", ["id"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_inspectionsmaster_users"
    
    end
  end
end