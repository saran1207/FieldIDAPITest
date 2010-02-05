require "user_request"
class AddCityToUserRequest < ActiveRecord::Migration
  def self.up
    add_column(:userrequest, :city, :string)
    UserRequest.reset_column_information
    UserRequest.update_all("city = ''");
  end
end