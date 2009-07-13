require 'composite_primary_keys'
require 'user'
require 'permission_action'

class Permissions < ActiveRecord::Base
  set_primary_keys :r_fieldiduser, :r_permissionaction
  set_table_name :permissions
  
  belongs_to  :user,              :foreign_key => 'r_fieldiduser',       :class_name => 'User'
  belongs_to  :permissionAction,  :foreign_key => 'r_permissionaction',  :class_name => 'PermissionAction'
end