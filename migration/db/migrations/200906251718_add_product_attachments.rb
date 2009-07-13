
class AddProductAttachments < ActiveRecord::Migration
  def self.up
    create_table :productattachments do |t|
      create_entity_with_tenant_fields_on(t)
      t.integer :product_id, :null => false
      t.text :comment
      t.string :filename
    end
    
    foreign_key(:productattachments, :product_id, :products, :id)
    
  end
  
  def self.down
    drop_table :productattachments
  end
end