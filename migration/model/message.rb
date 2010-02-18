require "message_command"
class Message < ActiveRecord::Base
  
  belongs_to :command, :foreign_key => :command_id,      :class_name => 'MessageCommand'
  
  belongs_to :owner, :foreign_key => :owner_id,      :class_name => 'BaseOrg'
end