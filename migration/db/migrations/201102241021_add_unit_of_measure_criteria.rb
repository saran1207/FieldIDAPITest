class AddUnitOfMeasureCriteria < ActiveRecord::Migration

  def self.up
    create_table :unitofmeasure_criteria, :primary_key => :id do |t|
      t.integer :primary_unit_id
      t.integer :secondary_unit_id
    end

    add_foreign_key(:unitofmeasure_criteria, :unitofmeasures, :source_column => :primary_unit_id, :foreign_column => :id, :name => "fk_criteria_primary_measure")
    add_foreign_key(:unitofmeasure_criteria, :unitofmeasures, :source_column => :secondary_unit_id, :foreign_column => :id, :name => "fk_criteria_secondary_measure")

    create_table :unitofmeasure_criteriaresults, :primary_key => :id do |t|
      t.string :primary_value, :limit => 255
      t.string :secondary_value, :limit => 255
    end
    
  end

  def self.down
    drop_table :unitofmeasure_criteria
    drop_table :combobox_criteriaresults
  end

end