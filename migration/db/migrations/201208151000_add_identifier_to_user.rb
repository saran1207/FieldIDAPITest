class AddIdentifierToUser < ActiveRecord::Migration
  def self.up
    add_column(:users, :identifier, :string, :null=>true, :limit => 20)
  end

  def self.down
    remove_column(:users, :identifier)
  end
end