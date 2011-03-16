class FixDefaultReportingLayout < ActiveRecord::Migration

  def self.up
    execute("delete from active_column_mappings where column_layout_id = 1 and ordervalue = 4")
    execute("update active_column_mappings set ordervalue = ordervalue - 1 where ordervalue > 4 and column_layout_id = 1")
  end

  def self.down
    # no point in putting a duplicate row back
  end

end