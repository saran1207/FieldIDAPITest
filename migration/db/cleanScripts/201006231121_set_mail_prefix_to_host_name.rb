require "configuration"
require "socket"

class SetMailPrefixToHostName < ActiveRecord::Migration
  def self.up
    Configuration.delete_all(:identifier => 'MAIL_SUBJECT_PREFIX')
    Configuration.create(:identifier => 'MAIL_SUBJECT_PREFIX', :value => '[testing-' + Socket.gethostname + ' ]', :created => Time.now, :modified => Time.now)
  end
end