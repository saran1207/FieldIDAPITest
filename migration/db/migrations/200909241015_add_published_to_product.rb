class AddPublishedToProduct < ActiveRecord::Migration
  
  def self.up
   # add_column(:products, :published, :boolean)
    
    execute "UPDATE products SET published = false"
    
    execute "ALTER TABLE products MODIFY published BIT NOT NULL"
    
    add_index(:products, :published)
    
  end

  def self.down
    remove_column(:products, :published)
  end

end