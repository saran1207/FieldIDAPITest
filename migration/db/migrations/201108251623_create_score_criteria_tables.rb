require 'event_form'

class CreateScoreCriteriaTables < ActiveRecord::Migration

  def self.up

    create_table :score_groups do |t|
      create_entity_with_tenant_fields_on(t)
      t.string :name, :limit => 1024
    end

    create_table :scores do |t|
      create_entity_with_tenant_fields_on(t)
      t.string :name, :limit => 1024
      t.decimal :value, :precision=>15, :scale=>10
      t.boolean :na
    end

    create_table :score_groups_scores do |t|
      t.integer :score_id, :null => false
      t.integer :score_group_id, :null => false
      t.integer :orderIdx, :null => false
    end

    create_table :score_criteria do |t|
      create_entity_with_tenant_fields_on(t)
      t.integer :group_id, :null => false
    end

    create_table :score_criteriaresults do |t|
      create_entity_with_tenant_fields_on(t)
      t.integer :score_id, :null => false
    end

    add_column(:eventforms, :pass_comparator, :string, :null => false)
    add_column(:eventforms, :pass_value1, :decimal, :null => false, :precision=>15, :scale=>10)
    add_column(:eventforms, :pass_value2, :decimal, :precision=>15, :scale=>10)

    add_column(:eventforms, :fail_comparator, :string, :null => false)
    add_column(:eventforms, :fail_value1, :decimal, :null => false, :precision=>15, :scale=>10)
    add_column(:eventforms, :fail_value2, :decimal, :precision=>15, :scale=>10)

    add_column(:eventforms, :na_comparator, :string, :null => false)
    add_column(:eventforms, :na_value1, :decimal, :null => false, :precision=>15, :scale=>10)
    add_column(:eventforms, :na_value2, :decimal, :precision=>15, :scale=>10)

    EventForm.update_all({ :pass_comparator => "BETWEEN" })
    EventForm.update_all({ :fail_comparator => "BETWEEN" })
    EventForm.update_all({ :na_comparator => "BETWEEN" })

    add_foreign_key(:score_groups_scores, :scores, :source_column => :score_id, :foreign_column => :id, :name => "fk_groups_scores")
    add_foreign_key(:score_groups_scores, :score_groups, :source_column => :score_group_id, :foreign_column => :id, :name => "fk_groups_groups")
    add_foreign_key(:score_criteriaresults, :scores, :source_column => :score_id, :foreign_column => :id, :name => "fk_score_results_to_score")
    add_foreign_key(:score_criteria, :score_groups, :source_column => :group_id, :foreign_column => :id, :name => "fk_criteria_to_groups")
  end

  def self.down
    drop_table :score_criteriaresults
    drop_table :score_criteria
    drop_table :score_groups_scores
    drop_table :scores
    drop_table :score_groups

    remove_column(:eventforms, :pass_comparator)
    remove_column(:eventforms, :pass_value1)
    remove_column(:eventforms, :pass_value2)

    remove_column(:eventforms, :fail_comparator)
    remove_column(:eventforms, :fail_value1)
    remove_column(:eventforms, :fail_value2)

    remove_column(:eventforms, :na_comparator)
    remove_column(:eventforms, :na_value1)
    remove_column(:eventforms, :na_value2)
  end

end