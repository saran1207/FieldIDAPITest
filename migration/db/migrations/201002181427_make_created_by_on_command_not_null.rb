require "message_command"
class MakeCreatedByOnCommandNotNull < ActiveRecord::Migration
  def self.up
    change_column(:messagecommands, :createdby, :integer, :null => false)
  end
  
  def self.down
    change_column(:messageommands, :createdby, :integer, :null => true)
  end
end