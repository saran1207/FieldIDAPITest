class LowercaseTaskColumn < ActiveRecord::Migration
  def self.up

    rename_column(:tasks, :taskEntityId, :taskentityid_)
    rename_column(:tasks, :taskentityid_, :taskentityid)
    
  end
  
  def self.down
  end
end