class AddAuthKeyToUsers < ActiveRecord::Migration

  def self.up
    add_column(:users, :authkey, :string, :length => 36, :null => false)
    execute "update users set authkey = uuid()"
    add_index(:users, :authkey, :unique => true)
  end

  def self.down
    remove_column(:users, :authkey)
  end

end