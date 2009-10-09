require "message"
class RenameReadColumnOnMessage < ActiveRecord::Migration
  def self.up
    add_column(:messages, :unread, :boolean, :null => false)
    remove_column(:messages, :read)
    Message.reset_column_information  
  end
  
  def self.down
    add_column(:messages, :read, :boolean, :null => false)
    remove_column(:messages, :unread)
  end
end