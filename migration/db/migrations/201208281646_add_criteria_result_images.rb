class AddCriteriaResultImages < ActiveRecord::Migration

  def self.up
    create_table "criteriaresult_images", :primary_key => "id" do |t|
        t.column :criteriaresult_id, :bigint, :null => false
        t.string :file_name, :limit => 255, :null => false
        t.string :comments, :limit => 2000
    end

    execute("alter table criteriaresult_images add foreign key fk_criteriaresult_images_criteriaresults_id (criteriaresult_id) references criteriaresults (id) on update no action on delete no action")
  end

  def self.down
    drop_table :criteriaresult_images
  end

end