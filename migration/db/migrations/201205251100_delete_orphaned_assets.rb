require 'column_mapping'

class DeleteOrphanedAssets < ActiveRecord::Migration

  def self.up
    execute("update assets a, assettypes at set a.state = 'ARCHIVED' where a.type_id = at.id and at.state='ARCHIVED' and a.state='ACTIVE'");
  end

  def self.down

  end

end