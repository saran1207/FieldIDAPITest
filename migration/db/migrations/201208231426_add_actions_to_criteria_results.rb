class AddActionsToCriteriaResults < ActiveRecord::Migration

  def self.up
    execute "create table criteriaresults_actions (id bigint(20) not null auto_increment, primary key (id), criteriaresult_id bigint(20), event_id bigint(20), orderidx bigint(20))"

    execute("alter table criteriaresults_actions add foreign key event_fk (event_id) references masterevents (event_id)")
    execute("alter table criteriaresults_actions add foreign key criteriaresult_fk (criteriaresult_id) references criteriaresults (id)")
  end

  def self.down
    execute "drop table criteriaresults_actions"
  end

end
