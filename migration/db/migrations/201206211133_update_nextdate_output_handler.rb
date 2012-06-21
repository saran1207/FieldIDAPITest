
class UpdateNextdateOutputHandler < ActiveRecord::Migration

  def self.up
    execute "update column_mappings set output_handler = 'com.n4systems.fieldid.viewhelpers.handlers.DateTimeHandler' where path_expression = 'nextDate';"
  end

  def self.down
    execute "update column_mappings set output_handler = null where path_expression = 'nextDate';"
  end

end