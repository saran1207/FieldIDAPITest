class AddNotesToEvents < ActiveRecord::Migration

  def self.up
    add_column(:masterevents, :notes, :string, :limit => 500)
    execute("alter table masterevents add column priority_id bigint")
    execute("alter table criteriaresults_actions add foreign key event_fk (event_id) references masterevents (event_id)")
  end

  def self.down
    remove_column(:masterevents, :notes)
    remove_column(:masterevents, :priority_id)
  end

end