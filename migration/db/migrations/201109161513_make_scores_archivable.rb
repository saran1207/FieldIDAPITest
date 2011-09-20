require 'score'
require 'score_group'

class MakeScoresArchivable < ActiveRecord::Migration

  def self.up
     add_column(:scores, :state, :string, :null=> false)
     Score.update_all("state = 'ACTIVE'")
     add_column(:score_groups, :state, :string, :null=> false)
     ScoreGroup.update_all("state = 'ACTIVE'")
  end

  def self.down
    remove_column(:scores, :satte)
    remove_column(:score_groups, :satte)
  end

end