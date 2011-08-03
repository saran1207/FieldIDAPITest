require 'column_mapping'

class FixSerialOutputHandler < ActiveRecord::Migration

  def self.up
    ColumnMapping.update_all( { :output_handler => 'com.n4systems.fieldid.viewhelpers.handlers.EventIdentifierHandler' }, { :output_handler => 'com.n4systems.fieldid.viewhelpers.handlers.EventSerialNumberHandler' } )
  end

  def self.down
    ColumnMapping.update_all( { :output_handler => 'com.n4systems.fieldid.viewhelpers.handlers.EventSerialNumberHandler' }, { :output_handler => 'com.n4systems.fieldid.viewhelpers.handlers.EventIdentifierHandler' } )
  end

end