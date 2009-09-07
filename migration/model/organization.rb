class Organization < ActiveRecord::Base
  set_table_name :organization
  set_inheritance_column :none # this way the active record modle doesn't try to use the type column as multi inheritance.
  
  belongs_to  :tenant,      :foreign_key => 'tenant_id',       :class_name => 'Tenant'
  
  def displayString
    "#{name} (#{id.to_s})"
  end
end
