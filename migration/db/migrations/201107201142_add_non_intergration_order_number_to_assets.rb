
class AddNonIntergrationOrderNumberToAssets < ActiveRecord::Migration
  
 def self.up
    add_column(:assets, :nonIntergrationOrderNumber, :string)
  end                    

  def self.down
    remove_column(:assets, :nonIntergrationOrderNumber);
  end

end