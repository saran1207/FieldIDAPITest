class AddCountsTowardsLimitToProduct < ActiveRecord::Migration
  
  def self.up
    add_column(:products, :countstowardslimit, :boolean)
    
    execute "UPDATE products SET countstowardslimit = true"
    
    execute "ALTER TABLE products MODIFY countstowardslimit BIT NOT NULL"
    
    add_index(:products, :countstowardslimit)
    
  end

  def self.down
    remove_column(:products, :countstowardslimit)
  end

end