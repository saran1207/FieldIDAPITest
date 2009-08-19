class Tenant < ActiveRecord::Base
  set_table_name :tenants
  
  def displayString
    "#{name} (#{id.to_s})"
  end
end