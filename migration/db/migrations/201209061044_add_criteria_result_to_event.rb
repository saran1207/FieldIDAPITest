class AddCriteriaResultToEvent < ActiveRecord::Migration

  def self.up
    execute("alter table masterevents add column source_criteria_result_id bigint")
    execute("alter table masterevents add foreign key source_criteria_result_fk (source_criteria_result_id) references criteriaresults (id)")
  end

  def self.down
    remove_column(:masterevents, :source_criteria_result_id)
  end

end