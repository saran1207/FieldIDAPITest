class RemoveConstraintsOnObservations < ActiveRecord::Migration 
  def self.up
   add_index(:criteriaresults_deficiencies, [:criteriaresults_id, :deficiencies_id])
   execute "ALTER TABLE criteriaresults_deficiencies DROP CONSTRAINT criteriaresults_deficiencies_pkey"
   execute "ALTER TABLE criteriaresults_deficiencies DROP CONSTRAINT criteriaresults_deficiencies_deficiencies_id_key"
   
   add_index(:criteriaresults_recommendations, [:criteriaresults_id, :recommendations_id])
   execute "ALTER TABLE criteriaresults_recommendations DROP CONSTRAINT criteriaresults_recommendations_pkey"
   execute "ALTER TABLE criteriaresults_recommendations DROP CONSTRAINT criteriaresults_recommendations_recommendations_id_key"
  end
  
  def self.down
   remove_index(:criteriaresults_deficiencies, [:criteriaresults_id, :deficiencies_id])
   remove_index(:criteriaresults_recommendations, [:criteriaresults_id, :recommendations_id])
   
  end
end