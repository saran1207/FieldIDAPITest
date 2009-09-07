class RemoveOldOwnerColumns < ActiveRecord::Migration
  
	def self.up
    
    # AddProductHistory
    remove_column(    :addproducthistory, :division_id)
    remove_column(    :addproducthistory, :customer_id)
    
    # User
    drop_foreign_key( :users, :org_base, :name => "fk_users_org_base")
    remove_column(    :users, :organization_id)
    remove_column(    :users, :division_id)
    remove_column(    :users, :customer_id)
    
    # Product
    drop_foreign_key( :products, :divisions, :name => "fk_products_divisions")
    remove_column(    :products, :division_id)
    remove_column(    :products, :customer_id)
    drop_foreign_key( :products, :org_base, :name => "fk_products_org_base")
    remove_column(    :products, :organization_id)
    
    # InspectionBook
    drop_foreign_key( :inspectionbooks, :customers, :name => "fk_inspectionbooks_customers")
    remove_column(    :inspectionbooks, :customer_id)
    
    # InspectionSchedule
    drop_foreign_key( :inspectionschedules, :divisions, :name => "fk_inspectionschedules_divisions")
    remove_column(    :inspectionschedules, :division_id)
    drop_foreign_key( :inspectionschedules, :customers, :name => "fk_inspectionschedules_customers")
    remove_column(    :inspectionschedules, :customer_id)
    
    # MasterInspection
    drop_foreign_key( :inspectionsmaster, :divisions, :name => "fk_inspectionsmaster_divisions")
    remove_column(    :inspectionsmaster, :division_id)
    drop_foreign_key( :inspectionsmaster, :customers, :name => "fk_inspectionsmaster_customers")
    remove_column(    :inspectionsmaster, :customer_id)
    drop_foreign_key( :inspectionsmaster, :org_base, :name => "fk_inspectionsmaster_org_base")
    remove_column(    :inspectionsmaster, :organization_id)
    
    # NotificationSettingsOwner
    drop_foreign_key( :notificationsettings_owner, :divisions, :name => "fk_notificationsettings_owner_divisions")
    remove_column(    :notificationsettings_owner, :division_id)
    drop_foreign_key( :notificationsettings_owner, :customers, :name => "fk_notificationsettings_owner_customers")
    remove_column(    :notificationsettings_owner, :customer_id)
    
    # Order
    drop_foreign_key( :orders, :divisions, :name => "fk_orders_divisions")
    remove_column(    :orders, :division_id)
    drop_foreign_key( :orders, :customers, :name => "fk_orders_customers")
    remove_column(    :orders, :customer_id)
    
    # ProductTypeSchedule
    drop_foreign_key( :producttypeschedules, :customers, :name => "fk_producttypeschedules_customers")
    remove_column(    :producttypeschedules, :customer_id)
    
    # Project
    drop_foreign_key( :projects, :divisions, :name => "fk_projects_divisions")
    remove_column(    :projects, :division_id)
    drop_foreign_key( :projects, :customers, :name => "fk_projects_customers")
    remove_column(    :projects, :customer_id)
    
    # JobSites
    drop_foreign_key(:jobsites, :divisions, :name => "fk_jobsites_divisions")
    drop_foreign_key(:jobsites, :customers, :name => "fk_jobsites_customers")
    drop_foreign_key(:jobsites, :tenants, :name => "fk_jobsites_tenants")
    drop_foreign_key(:jobsites, :users, :name => "fk_jobsites_users")
    
  end
	
	def self.down
  end
end