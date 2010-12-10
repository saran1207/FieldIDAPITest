class AddCriteriaSubtypesTables < ActiveRecord::Migration

  def self.up
    create_table :oneclick_criteria, :primary_key => :id do |t|
      t.integer :states_id, :null => false, :limit => 20
    end

    create_table :oneclick_criteriaresults, :primary_key => :id do |t|
      t.integer :state_id, :null => false, :limit => 20
    end

    create_table :textfield_criteria, :primary_key => :id do |t|
    end

    create_table :textfield_criteriaresults, :primary_key => :id do |t|
      t.string :value, :limit => 500
    end

    add_foreign_key(:oneclick_criteria, :statesets, :source_column => :states_id, :foreign_column => :id)
    add_foreign_key(:oneclick_criteriaresults, :states, :source_column => :state_id, :foreign_column => :id)

    execute "insert into oneclick_criteria (select id,states_id from criteria)"
    execute "insert into oneclick_criteriaresults (select id,state_id from criteriaresults)"

    execute "alter table criteria drop foreign key fk_criteria_statesets"
    execute "alter table criteria drop key index_criteria_on_states_id"
    remove_column(:criteria, :states_id)

    execute "alter table criteriaresults drop foreign key fk_criteriaresults_states"
    execute "alter table criteriaresults drop key index_criteriaresults_on_state_id"
    remove_column(:criteriaresults, :state_id)
  end

  def self.down
    add_column(:criteria, :states_id, :integer, :limit => 20)
    add_column(:criteriaresults, :state_id, :integer, :limit => 20)
    
    execute "update criteria,oneclick_criteria set criteria.states_id = oneclick_criteria.states_id where criteria.id=oneclick_criteria.id"
    execute "update criteriaresults,oneclick_criteriaresults set criteriaresults.state_id = oneclick_criteriaresults.state_id where criteriaresults.id=oneclick_criteriaresults.id"

    change_column(:criteria, :states_id, :integer, :limit => 20, :null => false)
    change_column(:criteriaresults, :state_id, :integer, :limit => 20, :null => false)

    add_foreign_key(:oneclick_criteria, :statesets, :source_column => :states_id, :foreign_column => :id)
    add_foreign_key(:oneclick_criteriaresults, :states, :source_column => :state_id, :foreign_column => :id)

    drop_table :oneclick_criteria
    drop_table :oneclick_criteriaresults

    drop_table :textfield_criteria
    drop_table :textfield_criteriaresults
  end

end