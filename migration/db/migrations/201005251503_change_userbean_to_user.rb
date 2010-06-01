require "base_org"
class ChangeUserbeanToUser < ActiveRecord::Migration
  def self.up
    run_with_out_foreign_keys do

      drop_foreign_key_1 "activesessions", ["user_id"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_activesessions_users"
      drop_foreign_key_1 "addproducthistory", ["assigneduser_id"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_addproducthistory_users"
      drop_foreign_key_1 "addressinfo", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_addressinfo_users"
      drop_foreign_key_1 "associatedinspectiontypes", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_associatedinspectiontypes_users"
      drop_foreign_key_1 "autoattributecriteria", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_autoattributecriteria_users"
      drop_foreign_key_1 "autoattributedefinition", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_autoattributedefinition_users"
      drop_foreign_key_1 "configurations", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_configurations_users"
      drop_foreign_key_1 "criteria", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_criteria_users"
      drop_foreign_key_1 "criteriaresults", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_criteriaresults_users"
      drop_foreign_key_1 "criteriasections", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_criteriasections_users"
      drop_foreign_key_1 "downloads", ["user_id"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_downloads_owner"
      drop_foreign_key_1 "downloads", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_downloads_users"
      drop_foreign_key_1 "eulaacceptances", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_eulaacceptances_modfied_user"
      drop_foreign_key_1 "eulaacceptances", ["acceptor_uniqueid"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_eulaacceptances_users"
      drop_foreign_key_1 "fileattachments", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_fileattachments_users"
      drop_foreign_key_1 "inspectionbooks", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_inspectionbooks_users"
      drop_foreign_key_1 "inspectiongroups", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_inspectiongroups_users"
      drop_foreign_key_1 "inspections", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_inspections_users"
      drop_foreign_key_1 "inspectionschedules", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_inspectionschedules_users"
      drop_foreign_key_1 "inspectionsmaster", ["inspector_uniqueid"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_inspectionsmaster_users"
      drop_foreign_key_1 "inspectiontypegroups", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_inspectiontypegroups_users"
      drop_foreign_key_1 "inspectiontypes", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_inspectiontypes_users"
      drop_foreign_key_1 "lineitems", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_lineitems_users"
      drop_foreign_key_1 "messagecommands", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_messagecommands_users"
      drop_foreign_key_1 "messages", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_messages_users"
      drop_foreign_key_1 "notificationsettings", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_notificationsettings_users"
      drop_foreign_key_1 "notificationsettings", ["user_id"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_notificationsettings_users_owner"
      drop_foreign_key_1 "observations", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_observations_users"
      drop_foreign_key_1 "orders", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_orders_users"
      drop_foreign_key_1 "org_base", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_org_base_users"
      drop_foreign_key_1 "org_connections", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_org_connections_users"
      drop_foreign_key_1 "printouts", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_printouts_users"
      drop_foreign_key_1 "products", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_products_modified_by_user"
      drop_foreign_key_1 "products", ["assigneduser_id"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_products_users"
      drop_foreign_key_1 "products", ["identifiedby_uniqueid"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_proudcts_identifiedby_user"
      drop_foreign_key_1 "producttypes", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_producttypes_users"
      drop_foreign_key_1 "producttypeschedules", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_producttypeschedules_users"
      drop_foreign_key_1 "projects_users", ["resources_uniqueid"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_projects_users_users"
      drop_foreign_key_1 "requesttransactions", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_requesttransactions_users"
      drop_foreign_key_1 "savedreports", ["user_id"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_savedreports_users"
      drop_foreign_key_1 "seenitstorageitem", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_seenitstorageitem_users"
      drop_foreign_key_1 "signupreferrals", ["referral_user_id"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "signupreferrals_referral_user"
      drop_foreign_key_1 "states", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_states_users"
      drop_foreign_key_1 "statesets", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_statesets_users"
      drop_foreign_key_1 "tagoptions", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_tagoptions_users"
      drop_foreign_key_1 "typedorgconnections", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_typedorgconnections_users"
      drop_foreign_key_1 "userrequest", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_userrequest_modifiedusers"
      drop_foreign_key_1 "userrequest", ["r_useraccount"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_userrequest_users"
      rename_column(:users, :uniqueid, :id)
      execute("ALTER TABLE users modify id bigint(21) NOT NULL AUTO_INCREMENT")
      
      rename_column(:users, :datecreated, :created)    
      rename_column(:users, :datemodified, :modified)
      remove_column(:users, :modifiedby)
      add_column(:users, :modifiedby, :integer)
    end
  end
  
  def self.down
    run_with_out_foreign_keys do
      remove_column(:users, :modifiedby)
      add_column(:users, :modifiedby, :string)
      rename_column(:users, :id, :uniqueid)
      execute("ALTER TABLE users modify uniqueid bigint(21) NOT NULL AUTO_INCREMENT")
      rename_column(:users, :created, :datecreated)    
      rename_column(:users, :modified, :datemodified)
      
      add_foreign_key "activesessions", ["user_id"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_activesessions_users"
      add_foreign_key "addproducthistory", ["assigneduser_id"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_addproducthistory_users"
      add_foreign_key "addressinfo", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_addressinfo_users"
      add_foreign_key "associatedinspectiontypes", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_associatedinspectiontypes_users"
      add_foreign_key "autoattributecriteria", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_autoattributecriteria_users"
      add_foreign_key "autoattributedefinition", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_autoattributedefinition_users"
      add_foreign_key "configurations", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_configurations_users"
      add_foreign_key "criteria", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_criteria_users"
      add_foreign_key "criteriaresults", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_criteriaresults_users"
      add_foreign_key "criteriasections", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_criteriasections_users"
      add_foreign_key "downloads", ["user_id"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_downloads_owner"
      add_foreign_key "downloads", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_downloads_users"
      add_foreign_key "eulaacceptances", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_eulaacceptances_modfied_user"
      add_foreign_key "eulaacceptances", ["acceptor_uniqueid"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_eulaacceptances_users"
      add_foreign_key "fileattachments", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_fileattachments_users"
      add_foreign_key "inspectionbooks", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_inspectionbooks_users"
      add_foreign_key "inspectiongroups", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_inspectiongroups_users"
      add_foreign_key "inspections", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_inspections_users"
      add_foreign_key "inspectionschedules", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_inspectionschedules_users"
      add_foreign_key "inspectionsmaster", ["inspector_uniqueid"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_inspectionsmaster_users"
      add_foreign_key "inspectiontypegroups", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_inspectiontypegroups_users"
      add_foreign_key "inspectiontypes", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_inspectiontypes_users"
      add_foreign_key "lineitems", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_lineitems_users"
      add_foreign_key "messagecommands", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_messagecommands_users"
      add_foreign_key "messages", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_messages_users"
      add_foreign_key "notificationsettings", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_notificationsettings_users"
      add_foreign_key "notificationsettings", ["user_id"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_notificationsettings_users_owner"
      add_foreign_key "observations", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_observations_users"
      add_foreign_key "orders", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_orders_users"
      add_foreign_key "org_base", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_org_base_users"
      add_foreign_key "org_connections", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_org_connections_users"
      add_foreign_key "printouts", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_printouts_users"
      add_foreign_key "products", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_products_modified_by_user"
      add_foreign_key "products", ["assigneduser_id"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_products_users"
      add_foreign_key "products", ["identifiedby_uniqueid"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_proudcts_identifiedby_user"
      add_foreign_key "producttypes", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_producttypes_users"
      add_foreign_key "producttypeschedules", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_producttypeschedules_users"
      add_foreign_key "projects_users", ["resources_uniqueid"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_projects_users_users"
      add_foreign_key "requesttransactions", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_requesttransactions_users"
      add_foreign_key "savedreports", ["user_id"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_savedreports_users"
      add_foreign_key "seenitstorageitem", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_seenitstorageitem_users"
      add_foreign_key "signupreferrals", ["referral_user_id"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "signupreferrals_referral_user"
      add_foreign_key "states", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_states_users"
      add_foreign_key "statesets", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_statesets_users"
      add_foreign_key "tagoptions", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_tagoptions_users"
      add_foreign_key "typedorgconnections", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_typedorgconnections_users"
      add_foreign_key "userrequest", ["modifiedby"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_userrequest_modifiedusers"
      add_foreign_key "userrequest", ["r_useraccount"], "users", ["uniqueid"], :on_update => "NO ACTION", :on_delete => "NO ACTION", :name => "fk_userrequest_users"
    end
  end
end