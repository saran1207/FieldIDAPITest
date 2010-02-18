require "message_command"
class AddCreatedByToCommands < ActiveRecord::Migration
  def self.up
    add_column(:messagecommands, :createdby, :integer)
    MessageCommand.reset_column_information
  end
  
  def self.down
    remove_column(:messagecommands, :createdby)
    MessageCommand.reset_column_information
  end
end