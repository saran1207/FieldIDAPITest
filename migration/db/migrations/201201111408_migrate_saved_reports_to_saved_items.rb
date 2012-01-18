require 'saved_item'
require 'saved_report'

class MigrateSavedReportsToSavedItems < ActiveRecord::Migration

  def self.up

    SavedReport.find(:all).each do |saved_report|
      saved_item = SavedItem.new
      saved_item.type = 'R'
      saved_item.user_id = saved_report.user_id
      saved_item.report_id = saved_report.id
      saved_item.created = saved_report.created
      saved_item.createdby = saved_report.createdby
      saved_item.modified = saved_report.modified
      saved_item.modifiedby = saved_report.modifiedby
      saved_item.tenant_id = saved_report.tenant_id
      saved_item.name = saved_report.name
      saved_item.sharedbyname = saved_report.sharedbyname
      saved_item.save
    end

    # we now have to set the initial order of each users saved items to be the alphabetical order
    # which is the order they would have been used to on the saved reports tab
    saved_items_user_ids = SavedItem.find_by_sql("select distinct user_id from saved_items")
    user_ids = saved_items_user_ids.collect {|e|e.user_id}

    user_ids.each do |user_id|
      index = 0
      current_users_items = SavedItem.find(:all, :conditions => {:user_id => user_id})
      current_users_items = current_users_items.sort_by {|item|item.name.downcase}
      current_users_items.each do |item|
        item.orderIdx = index
        item.save
        index += 1
      end
    end

    execute "insert into users_saved_items(user_id, item_id, orderIdx) (select user_id,id,orderIdx from saved_items)"
    remove_column(:saved_items, :user_id)
    remove_column(:saved_items, :orderIdx)
  end

  def self.down
    execute "delete from users_saved_items"
    execute "delete from saved_items"
  end
  
end