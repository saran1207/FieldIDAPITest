# Copyright (c) 2006 Alan Francis - alancfrancis.com
# Released under the MIT License.  See the LICENSE file for more details.

module ActiveRecord
  module ConnectionAdapters  
    module SchemaStatements
      # Defines a new foreign key constraint
      # This is actually implemented by the concrete 
      # ConnectionAdapter and at present only MySQL is
      # supported (as thats the only DB I know)
      #
      # The +table_name+ and +foreign_table_name+ parameters
      # should be the names of tables that already exist, either
      # in this migration, or an earlier one.
      #
      # The +options+ hash can include the following keys:
      # [<tt>:source_column</tt>]
      #   The column that will become constrained.  Defaults to rails 
      #   convention based on +foreign_table_name+.  e.g if 
      #   +foreign_table_name+ is +posts+, this will be <tt>:post_id</tt>
      # [<tt>:foreign_column</tt>]
      #   The column on the foreign table that will be keyed against.  
      #   Defaults to Rails convention ie <tt>:id</tt>
      # [<tt>:name</tt>]
      #   A name for the foreign_key.  Defaults to e.g <tt>:fk_comments_posts</tt>
      #
      # ===== Examples
      # ====== Create an FK to support a default rails has_many / belongs_to
      #
      #  create_table :posts do |t|
      #    t.column :title, :string
      #    t.column :body, :text
      #  end
      #
      #  create_table :comments do |t|
      #    t.column :contents, :text
      #    t.column :post_id, :integer
      #  end
      #
      #  add_foreign_key :comments, :posts
      #
      # (creates an FK constraint called <tt>fk_comments_posts</tt> from <tt>comments.post_id</tt> to 
      # <tt>posts.id</tt>)
      #
      # ====== Custom source column
      #
      #  create_table :posts do |t|
      #    t.column :title, :string
      #    t.column :body, :text
      #  end
      #
      #  create_table :comments do |t|
      #    t.column :contents, :text
      #    t.column :my_parent_post_id, :integer
      #  end
      #
      #  add_foreign_key :comments, :posts, :source_column => :my_parent_post_id
      #
      # (creates an FK constraint called <tt>fk_comments_posts</tt> from <tt>comments.my_parent_post_id</tt>
      # to <tt>posts.id</tt>)
      #
      # ====== Custom key name
      #
      #  create_table :programmers do |t|
      #    t.column :name, :string
      #  end
      #
      #  create_table :pairs do |t|
      #    t.column :first_id, :integer
      #    t.column :second_id, :integer
      #  end
      #
      #  add_foreign_key :pairs, :programmers,  
      #          :source_column => :first_id, 
      #          :name => :fk_first_programmer
      #  add_foreign_key :pairs, :programmers,  
      #          :source_column => :second_id, 
      #          :name => :fk_second_programmer
      #
      # (creates two fks, +fk_first_programmer+ and +fk_second_programmer+ 
      # from +pairs+ to <tt>programmers.id</tt> using the +first_id+ and +second_id+
      # fields) 
      def add_foreign_key( table_name, foreign_table_name, options={} )
        #default does nothing, overridden in ConnectionAdapter
      end

      # Remove a foreign key constraint
      # This is actually implemented by the concrete 
      # ConnectionAdapter and at present only MySQL is
      # supported (as thats the only DB I know)
      #
      # The +table_name+ and +foreign_table_name+ parameters
      # should be the names of tables that already exist, and have 
      # a foreign_key joining them as created by 
      # SchemaStatements#add_foreign_key in this migration, or an 
      # earlier one.
      #
      # The +options+ hash can include the following keys:
      # [<tt>:source_column</tt>]
      #   The column that was become constrained.  Defaults to rails 
      #   convention based on +foreign_table_name+.  e.g if 
      #   +foreign_table_name+ is +posts+, +:source_column+ will be +post_id+
      # [<tt>:foreign_column</tt>]
      #   The column on the foreign table that was be keyed against.  
      #   Defaults to Rails convention ie :id
      # [<tt>:name</tt>]
      #   The name of the foreign_key.  Defaults to fk_table_foreigntable
      def drop_foreign_key( table_name, foreign_table_name, options={} )
        #default does nothing, overridden in ConnectionAdapter
      end

      def fk_name( table_name, foreign_table_name, options={} )  #:nodoc:
        name = "fk_#{table_name.to_s}_#{foreign_table_name}"
        options[:name] || name.slice(0..63)
      end
      
      def fk_column( foreign_table_name, options={} )  #:nodoc:
        options[:source_column]
      end 
      
      def target_column( options )  #:nodoc:
        options[:foreign_column] || :id
      end


      def on_delete( options ) #:nodoc:
        options[:on_delete] || 'NO ACTION'
      end

      def on_update( options ) #:nodoc:
        options[:on_update] || 'NO ACTION'
      end

      def build_add_sql( table_name, foreign_table_name, options={} )  #:nodoc:
         %{
           ALTER TABLE    #{ table_name }
           ADD CONSTRAINT #{ fk_name( table_name, foreign_table_name, options ) }
           FOREIGN KEY    (#{ fk_column( foreign_table_name, options ) })
           REFERENCES     #{ foreign_table_name }(#{target_column( options ) })
           ON DELETE      #{ on_delete( options ) }
           ON UPDATE      #{ on_update( options ) }
         }
       end

       def build_drop_sql( table_name, foreign_table_name, options={})  #:nodoc:
         %{
           ALTER TABLE     #{ table_name }
           DROP CONSTRAINT #{fk_name( table_name, foreign_table_name, options ) }
         }
       end
    end

    class JdbcAdapter < AbstractAdapter
      # Defines a new foreign key constraint
      # This, the MySQL implementation, creates an index on the 
      # foreign_column if it doesn't already exist.
      #
      # See SchemaStatements#add_foreign_key for more detail
      def add_foreign_key_1(table_name, columns, foreign_table_name, foreign_columns, options={})
        add_foreign_key(table_name, foreign_table_name, options.merge({ :source_column => columns[0], :foreign_column => foreign_columns[0] }) )
      end
      
      def add_foreign_key( table_name, foreign_table_name, options={} )
        execute build_add_sql( table_name, foreign_table_name, options )
      end

      # Remove a foreign key constraint
      # This, the MySQL implementation, removes the automatically created
      # index on the foreign_column that was created by add_foreign_key
      #
      # See SchemaStatements#drop_foreign_key for more detail
      def drop_foreign_key_1(table_name, columns, foreign_table_name, foreign_columns, options={})
        drop_foreign_key(table_name, foreign_table_name, options.merge({ :source_column => columns[0], :foreign_column => foreign_columns[0] }) )
      end

      def drop_foreign_key( table_name, foreign_table_name, options={} )
        execute build_drop_sql( table_name, foreign_table_name, options )
      end

      def build_drop_sql( table_name, foreign_table_name, options={})  #:nodoc:
        %{
          ALTER TABLE     #{ table_name }
          DROP FOREIGN KEY #{fk_name( table_name, foreign_table_name, options ) }
        }
      end

      def table_has_index( table_name, index_name ) #:nodoc:
        indexes(table_name).any? {|i| i.name == index_name}
      end
      
      def show_foreign_keys(table_name, name = nil)
        sql = "SHOW CREATE TABLE #{quote_table_name(table_name)}"
        results = execute(sql)
  
        foreign_keys = []
  
        results.each do |row|
          row["Create Table"].each_line do |line|
            if line =~ /^  CONSTRAINT [`"](.+?)[`"] FOREIGN KEY \([`"](.+?)[`"]\) REFERENCES [`"](.+?)[`"] \((.+?)\)( ON DELETE (.+?))?( ON UPDATE (.+?))?,?$/
              name = $1
              column_names = $2
              references_table_name = $3
              references_column_names = $4
              on_update = $8
              on_delete = $6
              on_update = on_update.downcase.gsub(' ', '_').to_sym if on_update
              on_delete = on_delete.downcase.gsub(' ', '_').to_sym if on_delete
  
              foreign_keys << ForeignKeyDefinition.new(name,
                                             table_name, column_names.gsub('`', '').split(', '),
                                             references_table_name, references_column_names.gsub('`', '').split(', '),
                                             on_update, on_delete)
            end
          end 
        end
  
        foreign_keys
      end
    end
    
    
    
  end
end

class ForeignKeyDefinition < Struct.new(:name, :table_name, :column_names, :references_table_name, :references_column_names, :on_update, :on_delete, :deferrable)
    ACTIONS = { :cascade => "CASCADE", :restrict => "RESTRICT", :set_null => "SET NULL", :set_default => "SET DEFAULT", :no_action => "NO ACTION" }.freeze

  def to_dump
    dump = "add_foreign_key"
    dump << " #{table_name.inspect}, [#{Array(column_names).collect{ |name| name.inspect }.join(', ')}]"
    dump << ", #{references_table_name.inspect}, [#{Array(references_column_names).collect{ |name| name.inspect }.join(', ')}]"
    dump << ", :on_update => :#{on_update}" if on_update
    dump << ", :on_delete => :#{on_delete}" if on_delete
    dump << ", :deferrable => #{deferrable}" if deferrable
    dump << ", :name => #{name.inspect}" if name
    dump
  end
end


class Alex < ActiveRecord::Migration
  def self.go
    keys = []
    sql = "SHOW TABLES"
    select_all(sql).inject("") do |structure, table|
      table.delete('Table_type')
 
      show_foreign_keys(table.to_a.first.last).each do |t|
        if t.references_table_name == "users"
          keys << t.to_dump
        end
      end
    end
    keys.each { |key| puts key}
  end
  def self.hi
    execute("ALTER TABLE users modify id bigint(21) NOT NULL AUTO_INCREMENT")
  end
  
  def self.clean
    drop_table(:organization_extendedfeatures)
    drop_table(:tenantlink)
    drop_foreign_key(:organization , :organization, :source_column => :parent_id, :foreign_column => :id, :name => "fk_organization_parent")
    drop_foreign_key(:organization , :organization, :source_column => :r_tenant, :foreign_column => :id, :name => "fk_organization_tenant")
    drop_table(:organization)
  end
end