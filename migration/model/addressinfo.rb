class Addressinfo < ActiveRecord::Base
  set_table_name :addressinfo
  
  def displayString
    "#{name} (#{id.to_s})"
  end
end