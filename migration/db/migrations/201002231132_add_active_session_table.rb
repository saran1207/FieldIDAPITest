class AddActiveSessionTable < ActiveRecord::Migration
  def self.up
    create_table :activesessions, :primary_key => :user_id do |t|
      t.string :sessionid, :null => false, :length => 64
    end
    
    add_foreign_key(:activesessions, :users, :source_column => :user_id, :foreign_column => :uniqueid)
    add_index(:activesessions, :user_id, :unique => true)
  end
  
  def self.down
    drop_table :activesessions
  end
end