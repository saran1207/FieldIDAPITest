require "customer"
require "division"
require "tenant"
require "base_org"
require "primary_org"
require "secondary_org"
require "customer_org"
require "division_org"

class PrepareTablesForOwnerMigration < ActiveRecord::Migration
	def self.up

    rename_columns(:addproducthistory,           :r_owner,     :r_division, :r_jobsite, true)
    rename_columns(:users,                       :r_enduser,   :r_division, nil, true)
    rename_columns(:products,                    :owner_id,    nil,         nil, true)
    
    add_owner_column(:addproducthistory)
    add_owner_column(:users)
    add_owner_column(:products)
    add_owner_column(:inspectionbooks)
    add_owner_column(:inspectionschedules)
    add_owner_column(:inspectionsmaster)
    add_owner_column(:notificationsettings_owner)
    add_owner_column(:orders)
    add_owner_column(:producttypeschedules)
    add_owner_column(:projects)
    
  end
	
	def self.down
  end

  def self.rename_columns(table, customer_column, division_column, jobsite_column, dropfk)
    if (!customer_column.nil?)
      rename_customer_column(table, customer_column, dropfk)
    end
    
    if (!division_column.nil?)
      rename_division_column(table, division_column, dropfk)  
    end
    
    if (!jobsite_column.nil?)
      rename_jobsite_column(table, jobsite_column, dropfk)  
    end
    
  end

  def self.rename_customer_column(table, column, dropfk)
    if (dropfk)
      drop_foreign_key(table, :customers, :source_column => column, :foreign_column => :id)
    end
    
    rename_column(table, column, :customer_id)
  end
  
  def self.rename_division_column(table, column, dropfk)
    if (dropfk)
      drop_foreign_key(table, :divisions, :source_column => column, :foreign_column => :id)
    end
    
    rename_column(table, column, :division_id)
  end
  
  def self.rename_jobsite_column(table, column, dropfk)
    if (dropfk)
      drop_foreign_key(table, :jobsites, :source_column => column, :foreign_column => :id)
    end
    
    rename_column(table, column, :jobsite_id)
  end
  
  def self.add_owner_column(table)
    add_column(table, :owner_id, :integer)
    add_foreign_key(table, :org_base,  :source_column => :owner_id, :foreign_column => :id, :name => "fk_#{table}_owner")
  end
  
end