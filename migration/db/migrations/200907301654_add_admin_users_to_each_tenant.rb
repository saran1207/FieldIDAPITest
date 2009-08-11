require "organization"
require "user"

class AddAdminUsersToEachTenant < ActiveRecord::Migration
  def self.up
    users = { 'cm' => 'n4systems',
            'lcrane' => 'lance',
            'vm' => 'mmclees',
            'aoc' => 'richard',
            'iwi' => 'jiacco',
            'interstate' => 'eric',
            'ces' => 'hmiller',
            'hysafe' => 'ianzaldi',
            'allway' => 'allwaysean',
            'tristate' => 'nick',
            'nischain' => 'mbrett',
            'standard' => 'rtorres',
            'halo' => 'tsmith',
            'hesco' => 'ed',
            'capco' => 'mhedquist',
            'flaherty' => 'mflaherty',
            'elko' => 'beau',
            'johnsakach' => 'tdriscoll',
            'brs' => 'chrisf',
            'hercules' => 'mgiannou',
            'msa' =>         'jeremy',
            'peakworks' =>   'gabe',
            'wcwr' =>        'johna',
            'cicb' =>        'craig',
            'stellar' =>     'jgiannou',
            'key' =>         'rick',
            'certex' =>      'farchila',
            'oceaneering' => 'sstites',
            'marine' =>      'royiv',
            'unilift' =>     'sarah',
            'uts' =>         'jim',
            'swwr' =>        'dsmith',
            'jergens' =>     'tchurchia',
            'swos' =>        'whitney',
            'wiscolift' =>   'bkinney',
            'n4' =>          'sricci',
            'seafit' =>      'tom',
            'cglift' =>      'bross',
            'sievert' =>     'sthompson',
            'illinois' =>    'kcarver',
            'piedmont' =>    'mmelquist',
            'pawspumps' =>   'pawspumps',
            'domson' =>      'dennis',
            'unirope' =>     'ceskra' }
    users.each do |tenant_name, username|
      puts tenant_name + "  " + username
      tenant = Organization.find(:first, :conditions => { :name => tenant_name })
      user = User.find(:first, :conditions => { :userid => username, :r_tenant => tenant.id})
      if (!user.nil?)
        user.admin = true;
        user.datemodified = Time.new;
        user.save
      end
    end 

  end
  
  def self.down 
    
  end
end