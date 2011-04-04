require 'download'

class AddDownloadidColumnToDownloadlink < ActiveRecord::Migration
  def self.up
    add_column(:downloads, :downloadid, :string, :null => false)

    Download.find(:all).each do |download|
      download.update_attribute :downloadid, ('a'..'z').sort_by {rand}[0,10].join.to_s
    end

    add_index(:downloads, :downloadid, :unique => true)
  end

  def self.down
    remove_index(:downloads, :downloadid)
    remove_column(:downloads, :downloadid)
  end
end