require 'composite_primary_keys'
class MessageCommandParameter < ActiveRecord::Base
  set_table_name  :messagecommands_paramaters
  set_primary_keys :messagecommands_id, :mapkey
end