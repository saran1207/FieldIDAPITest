require 'composite_primary_keys'
require 'primary_org'

class OrgExtendedfeature < ActiveRecord::Base
  set_table_name :org_extendedfeatures
  set_primary_keys :org_id, :feature
  
  belongs_to  :primaryOrg,  :foreign_key => 'org_id',  :class_name => 'PrimaryOrg'
end