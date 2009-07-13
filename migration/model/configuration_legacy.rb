class ConfigurationLegacy < ActiveRecord::Base
  set_table_name :configuration
  set_primary_key :keystring
  
  def displayString
    "#{keystring} = #{keyvalue}"
  end
end