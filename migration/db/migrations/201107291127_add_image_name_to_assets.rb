
class AddImageNameToAssets < ActiveRecord::Migration
  
 def self.up
    add_column(:assets, :imageName, :string)
  end                    

  def self.down
    remove_column(:assets, :imageName);
  end

end