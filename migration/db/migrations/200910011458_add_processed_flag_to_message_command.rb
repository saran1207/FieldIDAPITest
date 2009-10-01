require "message_command"
class AddProcessedFlagToMessageCommand < ActiveRecord::Migration
  def self.up
    add_column(:messagecommands, :processed, :boolean)
    MessageCommand.reset_column_information
    MessageCommand.update_all(:processed => false)
    
    change_column(:messagecommands, :processed, :boolean, :null => false)
    MessageCommand.reset_column_information
  end
  
  def self.down
    drop_column(:messagecommands, :processed)
  end
end