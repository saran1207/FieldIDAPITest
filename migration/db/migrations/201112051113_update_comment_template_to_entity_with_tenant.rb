class UpdateCommentTemplateToEntityWithTenant < ActiveRecord::Migration

  def self.up
    create_table :comment_templates do |t|
      create_entity_with_tenant_fields_on(t)
      t.string :name, :null => false
      t.string :comment, :limit => 2500, :null => false
    end

    add_foreign_key(:comment_templates, :users,   :source_column => :createdby, :foreign_column => :id, :name => 'fk_comment_templates_users_on_createdby')    
    add_foreign_key(:comment_templates, :users,   :source_column => :modifiedby, :foreign_column => :id, :name => 'fk_comment_templates_users_on_modifiedby')
    add_foreign_key(:comment_templates, :tenants, :source_column => :tenant_id,  :foreign_column => :id)
    
    execute("INSERT INTO comment_templates (id, created, modified, tenant_id, name, comment) SELECT uniqueid, datecreated, datemodified, tenant_id, templateid, contents FROM commenttemplate")
    
    drop_table(:commenttemplate)
  end

  def self.down
  end

end