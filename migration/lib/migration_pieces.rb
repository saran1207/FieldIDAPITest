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
        options[:source_column] || "#{Inflector.singularize(foreign_table_name)}_id"
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

    class PostgreSQLAdapter < AbstractAdapter
      # Defines a new foreign key constraint
      # This is the PostgreSQL version
      #
      # See SchemaStatements#add_foreign_key for more detail
      def add_foreign_key( table_name, foreign_table_name, options={} )
        execute build_add_sql( table_name, foreign_table_name, options )
      end

      # Remove a foreign key constraint
      # This is the PostgresSQL version
      #
      # See SchemaStatements#drop_foreign_key for more detail
      def drop_foreign_key( table_name, foreign_table_name, options={} )
        execute build_drop_sql( table_name, foreign_table_name, options )
      end

    end

    class MysqlAdapter < AbstractAdapter

      # Defines a new foreign key constraint
      # This, the MySQL implementation, creates an index on the 
      # foreign_column if it doesn't already exist.
      #
      # See SchemaStatements#add_foreign_key for more detail
      def add_foreign_key( table_name, foreign_table_name, options={} )
        execute build_add_sql( table_name, foreign_table_name, options )
      end

      # Remove a foreign key constraint
      # This, the MySQL implementation, removes the automatically created
      # index on the foreign_column that was created by add_foreign_key
      #
      # See SchemaStatements#drop_foreign_key for more detail
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
    end
  end
end
