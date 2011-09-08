require 'event_form'

class AddScoreCalculationType < ActiveRecord::Migration

  def self.up
    remove_column(:eventforms, :na_comparator)
    remove_column(:eventforms, :na_value1)
    remove_column(:eventforms, :na_value2)

    remove_column(:score_criteria, :tenant_id)
    remove_column(:score_criteria, :created)
    remove_column(:score_criteria, :createdby)
    remove_column(:score_criteria, :modified)
    remove_column(:score_criteria, :modifiedby)

    remove_column(:score_criteriaresults, :tenant_id)
    remove_column(:score_criteriaresults, :created)
    remove_column(:score_criteriaresults, :createdby)
    remove_column(:score_criteriaresults, :modified)
    remove_column(:score_criteriaresults, :modifiedby)

    add_column(:eventforms, :score_calculation_type, :string, :null => false)
    add_column(:eventforms, :use_score_for_result, :boolean, :null => false)
    EventForm.update_all({ :use_score_for_result => false })
    EventForm.update_all({ :score_calculation_type => "SUM" })
  end

  def self.down
    add_column(:eventforms, :na_comparator, :string, :null => false)
    add_column(:eventforms, :na_value1, :decimal, :null => false, :precision=>15, :scale=>10)
    add_column(:eventforms, :na_value2, :decimal, :precision=>15, :scale=>10)

    add_column(:score_criteria, :tenant_id, :integer)
    add_column(:score_criteria, :created, :integer)
    add_column(:score_criteria, :createdby, :integer)
    add_column(:score_criteria, :modified, :integer)
    add_column(:score_criteria, :modifiedby, :integer)
    
    add_column(:score_criteriaresults, :tenant_id, :integer)
    add_column(:score_criteriaresults, :created, :integer)
    add_column(:score_criteriaresults, :createdby, :integer)
    add_column(:score_criteriaresults, :modified, :integer)
    add_column(:score_criteriaresults, :modifiedby, :integer)

    remove_column(:eventforms, :use_score_for_result)
    remove_column(:eventforms, :score_calculation_type)
  end

end