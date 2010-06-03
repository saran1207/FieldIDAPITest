class ActiveSession < ActiveRecord::Base
  set_table_name :activesessions
  set_primary_key :user_id
end