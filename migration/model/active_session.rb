require 'user'

class ActiveSession < ActiveRecord::Base
  set_table_name :activesessions
  set_primary_key :user_id

  belongs_to  :user,              :foreign_key => 'user_id',       :class_name => 'User'
end