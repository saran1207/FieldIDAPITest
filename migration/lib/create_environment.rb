$:.unshift "./model/"
$:.unshift "./lib/"

gem 'activerecord', '= 2.3.2'
gem 'composite_primary_keys', '= 2.2.2'
gem 'tzinfo'

require 'yaml'
require 'active_record'
require 'migration_helper'
puts RUBY_PLATFORM
if RUBY_PLATFORM =~ /java/
  gem 'activerecord-jdbcpostgresql-adapter', '= 0.9'
  require 'active_record/connection_adapters/jdbcpostgresql_adapter'
end



class ActiveRecord::Migration 
  extend MigrationHelpers
end

# monkey patch the postgres defaut types.
  
class PostgreSQLColumn #:nodoc:
  def native_database_types #:nodoc:
      {
        :primary_key => "bigserial primary key",
        :string      => { :name => "character varying", :limit => 255 },
        :text        => { :name => "text" },
        :integer     => { :name => "integer", :limit => 8 },
        :float       => { :name => "float" },
        :decimal     => { :name => "decimal" },
        :datetime    => { :name => "timestamp" },
        :timestamp   => { :name => "timestamp" },
        :time        => { :name => "time" },
        :date        => { :name => "date" },
        :binary      => { :name => "bytea" },
        :boolean     => { :name => "boolean" }
      }
  end
end

# auto load the model directory.


