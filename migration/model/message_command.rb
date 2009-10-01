require 'composite_primary_keys'
class MessageCommand < ActiveRecord::Base
  set_table_name :messagecommands
  set_primary_keys :messagecommands_id, :mapkey
end