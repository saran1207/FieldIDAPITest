require "base_org"
class OrgConnection < ActiveRecord::Base
  belongs_to :vendor, :foreign_key => :vendor_id, :class_name => 'BaseOrg'
  belongs_to :customer, :foreign_key => :customer_id, :class_name => 'BaseOrg'
end