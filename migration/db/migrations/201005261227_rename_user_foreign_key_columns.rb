class RenameUserForeignKeyColumns < ActiveRecord::Migration
  def self.up

    rename_column "eulaacceptances", "acceptor_uniqueid",  "acceptor_id"
    rename_column "inspectionsmaster", "inspector_uniqueid", "inspector_id"
    rename_column "products", "identifiedby_uniqueid", "identifiedby_id"
    rename_column "projects_users", "resources_uniqueid", "resources_id"
    rename_column "userrequest", "r_useraccount", "useraccount_id"
    
  end
  
  
  def self.down
    
    rename_column "eulaacceptances", "acceptor_id", "acceptor_uniqueid"
    rename_column "inspectionsmaster", "inspector_id", "inspector_uniqueid"
    rename_column "products", "identifiedby_id", "identifiedby_uniqueid"
    rename_column "projects_users", "resources_id", "resources_uniqueid"
    rename_column "userrequest", "useraccount_id", "r_useraccount"
  end
end  