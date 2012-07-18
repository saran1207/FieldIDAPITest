class ChangeIncompleteToOpenInEventSearch < ActiveRecord::Migration

  def self.up
    execute("update saved_reports set eventState = 'OPEN' where eventState = 'INCOMPLETE'")
  end

  def self.down
    execute("update saved_reports set eventState = 'INCOMPLETE' where eventState = 'OPEN'")
  end

end