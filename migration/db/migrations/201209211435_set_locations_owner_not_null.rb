class SetLocationsOwnerNotNull< ActiveRecord::Migration

  def self.up
    execute("ALTER table predefinedlocations modify owner_id bigint(20)  not null;")
  end

  def self.down
    execute("ALTER table predefinedlocations modify owner_id bigint(20)  null;")
  end

end

