class AddLinkedProductToProduct < ActiveRecord::Migration
  
  def self.up
    remove_column(:products, :uuid)
    remove_column(:products, :linkeduuid)
    add_column(:products, :linked_id, :integer)
    add_foreign_key(:products, :products, :source_column => :linked_id, :foreign_column => :id, :name => "fk_linked_product_id")
    
  end

  def self.down
    remove_column(:products, :linked_id)
    add_column(:products, :uuid, :string)
    add_column(:products, :linkeduuid, :string)
  end

end