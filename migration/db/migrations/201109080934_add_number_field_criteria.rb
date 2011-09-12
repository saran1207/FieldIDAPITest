class AddNumberFieldCriteria < ActiveRecord::Migration

  def self.up
  
    create_table :numberfield_criteria, :primary_key => :id do |t|
      t.integer  :decimalplaces, :null => false, :default => 0
    end

    create_table :numberfield_criteriaresults, :primary_key => :id do |t|
      t.decimal :value, :precision=>65, :scale=>10
    end
    
  end
  
  def self.down    
  	drop_table :numberfield_criteria
    drop_table :numberfield_criteriaresults
  end
  
end