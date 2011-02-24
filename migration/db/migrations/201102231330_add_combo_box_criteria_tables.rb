class AddComboBoxCriteriaTables < ActiveRecord::Migration

  def self.up
  
    create_table :combobox_criteria, :primary_key => :id do |t|
    end
    
    create_table :combobox_criteria_options, :id => false, :force => true, :primary_key => [:combobox_criteria_id, :orderidx] do |t|
    	t.integer :combobox_criteria_id, :null => false
    	t.integer :orderidx, :null => false
    	t.string :selectoption, :null=>false
    end
    
    add_foreign_key(:combobox_criteria_options, :combobox_criteria, :source_column => :combobox_criteria_id, :foreign_column => :id)
    
    create_table :combobox_criteriaresults, :primary_key => :id do |t|
      t.string :value
    end
    
  end
  
  def self.down
    drop_table :combobox_criteria_options
    drop_table :combobox_criteria
    drop_table :combobox_criteriaresults
  end
end