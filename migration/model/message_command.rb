require "message_command_parameter"
class MessageCommand < ActiveRecord::Base
  set_table_name :messagecommands
  
  has_many :parameters, :foreign_key => :messagecommands_id, :class_name => "MessageCommandParameter"
end