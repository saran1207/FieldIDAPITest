class AddSelectCriteriaTables < ActiveRecord::Migration

  def self.up
  
    create_table :select_criteria, :primary_key => :id do |t|
    end
    
    create_table :select_criteria_options, :id => false, :force => true, :primary_key => [:select_criteria_id, :orderidx] do |t|
    	t.integer :select_criteria_id, :null => false
    	t.integer :orderidx, :null => false
    	t.string :selectoption, :null=>false
    end
    
    add_foreign_key(:select_criteria_options, :select_criteria, :source_column => :select_criteria_id, :foreign_column => :id)
    
    create_table :select_criteriaresults, :primary_key => :id do |t|
      t.string :value
    end
    
  end
  
  def self.down
    drop_table :select_criteria_options
    drop_table :select_criteria
    drop_table :select_criteriaresults
  end
end