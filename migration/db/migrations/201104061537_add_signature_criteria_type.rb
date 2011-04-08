class AddSignatureCriteriaType < ActiveRecord::Migration

  def self.up
    create_table :signature_criteria, :primary_key => :id do |t|
    end

    create_table :signature_criteriaresults, :primary_key => :id do |t|
      t.boolean :signed, :null => false
    end

  end

  def self.down
    drop_table :signature_criteria
    drop_table :signature_criteriaresults
  end

end