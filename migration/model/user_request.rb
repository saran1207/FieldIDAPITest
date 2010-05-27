require "user"
class UserRequest < ActiveRecord::Base
  set_table_name :userrequest
  
  belongs_to :user, :foreign_key => :useraccount_id, :class_name => User
end