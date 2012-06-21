
class ResetNextdateOutputHandler < ActiveRecord::Migration

  def self.up
    execute "update column_mappings set output_handler = null where path_expression = 'nextDate';"
  end

  def self.down
  end

end