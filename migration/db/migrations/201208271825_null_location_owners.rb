
class NullLocationOwners< ActiveRecord::Migration

  def self.up
    execute("update predefinedlocations set owner_id = null where parent_id is not null or state='ARCHIVED';")
  end

  def self.down

  end

end
