require "message"
class AddCommandToMessage < ActiveRecord::Migration
  def self.up
    Message.delete_all
    add_column(:messages, :command_id, :integer, :null => false)
    add_foreign_key(:messages, :messagecommands, :source_column => :command_id, :foreign_column => :id)
  end
  
  def self.down
    remove_foreign_key(:messages, :messagecommands, :source_column => :command_id, :foreign_column => :id)
    remove_column(:messages, :command_id)
  end
end