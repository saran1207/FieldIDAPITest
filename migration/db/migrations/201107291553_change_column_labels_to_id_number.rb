require 'column_mapping'

class ChangeColumnLabelsToIdNumber < ActiveRecord::Migration

  def self.up
    ColumnMapping.update_all( { :label => 'label.id_number' }, { :label => 'label.serialnumber' } )
  end

  def self.down
    ColumnMapping.update_all( { :label => 'label.serialnumber' }, { :label => 'label.id_number' } )
  end

end