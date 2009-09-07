require 'user'
require 'base_org'

class AddProductHistory < ActiveRecord::Base
  
  set_table_name :addproducthistory
  set_primary_key :uniqueid
  
  belongs_to  :user,      :foreign_key => 'r_fieldiduser',       :class_name => 'User'
  belongs_to  :owner,     :foreign_key => 'owner_id',            :class_name => 'BaseOrg'
  
  def tenant_id
    user.tenant_id
  end
  
end
