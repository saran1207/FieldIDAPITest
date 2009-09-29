require "customer"
require "division"
require "tenant"
require "base_org"
require "primary_org"
require "secondary_org"
require "customer_org"
require "division_org"
require "add_product_history"
require "inspection_schedule"
require "inspectionmaster"
require "notification_settings_owner"
require "product"
require "project"
require "job_site"
require "user"

class MigrateJobsites < ActiveRecord::Migration
  
	def self.up
    
    JobSite.find_each(:all) do |site|
      migrate_job_site(site)
    end
    
    
    migrate_job_sites(AddProductHistory.find(:all, :conditions => "jobsite_id is not null"))
    migrate_job_sites(InspectionSchedule.find(:all, :conditions => "jobsite_id is not null"))
    migrate_job_sites(Inspection.find(:all, :conditions => "jobsite_id is not null"))
    migrate_job_sites(NotificationSettingsOwner.find(:all, :conditions => "jobsite_id is not null"))
    migrate_job_sites(Product.find(:all, :conditions => "jobsite_id is not null"))
    migrate_job_sites(Project.find(:all, :conditions => "jobsite_id is not null"))

    remove_column(:addproducthistory, :jobsite_id)
    
    drop_foreign_key(:inspectionschedules, :jobsites, :name => "fk_inspectionschedules_jobsites")
    remove_column(:inspectionschedules, :jobsite_id)
    
    drop_foreign_key(:inspectionsmaster, :jobsites, :name => "fk_inspectionsmaster_jobsites")
    remove_column(:inspectionsmaster, :jobsite_id)
    
    drop_foreign_key(:notificationsettings_owner, :jobsites, :name => "fk_notificationsettings_owner_jobsites")
    remove_column(:notificationsettings_owner, :jobsite_id)
    
    drop_foreign_key(:products, :jobsites, :name => "fk_products_jobsites")
    remove_column(:products, :jobsite_id)
    
    drop_foreign_key(:projects, :jobsites, :name => "fk_projects_jobsites")
    remove_column(:projects, :jobsite_id)
    
  end
	
	def self.down
  end

  def self.migrate_job_sites(models)
    models.each do |model|
      update_owner(model)
    end
  end

  def self.update_owner(model)
    model.owner_id = @site_to_customer[model.job_site_id]
    model.save
  end

  def self.migrate_job_site(site)
    cust_org = create_customer(site.name, site.customerId, site.modified_by, site.tenant)
    @site_to_customer_map[site.id] = cust_org.id    
  end

  def self.create_customer(name, code, modified_by, tenant)
    now = Time.now
    
    customerBase = BaseOrg.new
    customerBase.created = now
    customerBase.modified = now
    customerBase.modifiedBy = User.find(modified_by)
    customerBase.tenant = tenant
    customerBase.name = name
    customerBase.save
    customerBase.customer_id = customerBase.id
    customerBase.save

    customerOrg = CustomerOrg.new
    customerOrg.baseOrg = customerBase
    customerOrg.parent = tenant.primaryOrg
    customerOrg.code = code
    customerOrg.save
    
    return customerBase
  end
end