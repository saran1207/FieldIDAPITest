class AddEventFormsTable < ActiveRecord::Migration

  def self.up
    create_table "eventforms", :primary_key => :id do |t|
      create_entity_with_tenant_fields_on(t)
      t.string "state", :null => false
    end

    add_column(:eventtypes, :eventform_id, :integer)

    add_foreign_key(:eventforms, :tenants, :source_column => :tenant_id, :foreign_column => :id)
    add_foreign_key(:eventtypes, :eventforms, :source_column => :eventform_id, :foreign_column => :id)

    execute("insert into eventforms(id,state,created,modified,tenant_id) (select id,state,created,modified,tenant_id from eventtypes)")
    execute("update eventtypes set eventform_id = id")

    change_column(:eventtypes, :eventform_id, :integer, :null => false)

    execute("alter table eventtypes_criteriasections drop foreign key eventtypes_criteriasections_ibfk_1")
    rename_column(:eventtypes_criteriasections, :eventtypes_id, :eventform_id)
    rename_table(:eventtypes_criteriasections, :eventforms_criteriasections)
    add_foreign_key(:eventforms_criteriasections, :eventforms, :source_column => :eventform_id, :foreign_column => :id, :name => :fk_eventform)
  end

  def self.down
    drop_foreign_key(:eventtypes, :eventforms, :source_column => :eventform_id, :foreign_column => :id, :name => "fk_eventforms_eventtypes")
    remove_column(:eventtypes, :eventform_id)

    execute("alter table eventforms_criteriasections drop foreign key fk_eventform")
    rename_column(:eventtypes_criteriasections, :eventform_id, :eventtypes_id)
    rename_table(:eventforms_criteriasections, :eventtypes_criteriasections)
    add_foreign_key(:eventtypes_criteriasections, :eventtypes, :source_column => :eventtype_id, :foreign_column => :id, :name => :eventtypes_criteriasections_ibfk_1)

    drop_table(:eventforms)
  end

end