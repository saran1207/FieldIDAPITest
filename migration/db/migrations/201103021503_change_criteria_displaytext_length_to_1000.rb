require 'criteria'
require 'criteria_section'

class ChangeCriteriaDisplaytextLengthTo1000 < ActiveRecord::Migration

  def self.up
   change_column :criteria, :displaytext, :string, :limit => 1000
  end

  def self.down
     change_column :criteria, :displaytext, :string, :limit => 255
  end

end