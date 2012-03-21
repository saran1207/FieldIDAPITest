require "configuration"
class RemoveClickTaleConfigEntries < ActiveRecord::Migration

  def self.up
    Configuration.delete_all(:identifier => 'CLICKTALE_START')
    Configuration.delete_all(:identifier => 'CLICKTALE_END')
  end

  def self.down

  end

end