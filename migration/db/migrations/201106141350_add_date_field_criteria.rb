class AddDateFieldCriteria < ActiveRecord::Migration

  def self.up
  
    create_table :datefield_criteria, :primary_key => :id do |t|
      t.boolean   :datetime,                 :null => false
    end

    create_table :datefield_criteriaresults, :primary_key => :id do |t|
      t.string :value, :limit => 500
    end
    
  end
  
  def self.down    
  	drop_table :datefield_criteria
    drop_table :datefield_criteriaresults
  end
  
end