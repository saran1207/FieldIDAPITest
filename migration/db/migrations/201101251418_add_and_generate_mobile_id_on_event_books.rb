class AddAndGenerateMobileIdOnEventBooks < ActiveRecord::Migration
  def self.up
    remove_column(:eventbooks, :legacyid)
    
    add_column(:eventbooks, :mobileid, :string, :null => true, :limit => 36)
    add_index(:eventbooks, :mobileid, :unique => true)
    execute("update eventbooks set mobileid = UUID()")
    change_column(:eventbooks, :mobileid, :string, :null => false, :limit => 36)
  end
  
  def self.down
  end
end