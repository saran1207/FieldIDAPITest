require 'composite_primary_keys'
class OrganizationExtendedfeature < ActiveRecord::Base
  set_primary_keys :organization_id, :element
end