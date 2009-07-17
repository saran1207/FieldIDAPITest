
class AddEulaTables < ActiveRecord::Migration
  def self.up
    create_table :eulas do |t|
      create_abstract_entity_fields_on(t)
      t.text :legaltext, :null => false
      t.timestamp :effectivedate, :null => false
      t.string :version, :null => false
    end
    
    create_table :eulaacceptances do |t|
      create_entity_with_tenant_fields_on(t)
      t.integer :acceptor_uniqueid, :null => false
      t.integer :eula_id, :null => false
      t.timestamp :date, :null => false
    end
    
    create_foreign_keys_for_entity_with_tenant(:eulaacceptances)
    foreign_key(:eulaacceptances, :acceptor_uniqueid, :users, :uniqueid)
    foreign_key(:eulaacceptances, :eula_id, :eulas, :id)
    
  end
  
  def self.down
  end
end