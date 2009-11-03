require "primary_org"
require "user"

class MapNetsuiteToFieldid < ActiveRecord::Migration
  def self.up
    output = Array.new
    
    primary_org = PrimaryOrg.find(10802301)
    primary_org.externalid = 106
    primary_org.externalpassword = '783b43dc73018d97'
    
    admin_user = User.find(:first, :conditions => { :admin => true, :tenant_id => primary_org.baseOrg.tenant_id })  
    primary_org.externalusername=admin_user.emailaddress   
    primary_org.save
    output << "106, 10802301," + admin_user.emailaddress + ",783b43dc73018d97"
    
    primary_org = PrimaryOrg.find(15511538)
    primary_org.externalid = 2479
    primary_org.externalpassword = '8a88e5a7c3244df2'
    
    admin_user = User.find(:first, :conditions => { :admin => true, :tenant_id => primary_org.baseOrg.tenant_id })  
    primary_org.externalusername=admin_user.emailaddress   
    primary_org.save
    output << "2479, 15511538," + admin_user.emailaddress + ",8a88e5a7c3244df2"
    
    primary_org = PrimaryOrg.find(15511553)
    primary_org.externalid = 2742
    primary_org.externalpassword = 'f56d1553d8af5e71'
    
    admin_user = User.find(:first, :conditions => { :admin => true, :tenant_id => primary_org.baseOrg.tenant_id })
    primary_org.externalusername=admin_user.emailaddress
    primary_org.save
    output << "2742, 15511553," + admin_user.emailaddress + ",f56d1553d8af5e71"
    
    
    
    primary_org = PrimaryOrg.find(15511515)
    primary_org.externalid = 746
    primary_org.externalpassword = '3667d15010c9447f'
    
    admin_user = User.find(:first, :conditions => { :admin => true, :tenant_id => primary_org.baseOrg.tenant_id })
    primary_org.externalusername=admin_user.emailaddress
    primary_org.save
    output << "746, 15511515," + admin_user.emailaddress + ",3667d15010c9447f"
    
    primary_org = PrimaryOrg.find(15511501)
    primary_org.externalid = 475
    primary_org.externalpassword = '24095c600bc7caa6'
    
    admin_user = User.find(:first, :conditions => { :admin => true, :tenant_id => primary_org.baseOrg.tenant_id })
    primary_org.externalusername=admin_user.emailaddress
    primary_org.save
    output << "475, 15511501," + admin_user.emailaddress + ",24095c600bc7caa6"
    
    primary_org = PrimaryOrg.find(15511518)
    primary_org.externalid = 1707
    primary_org.externalpassword = '3e9bf6c1d7e04a2b'
    
    admin_user = User.find(:first, :conditions => { :admin => true, :tenant_id => primary_org.baseOrg.tenant_id })
    primary_org.externalusername=admin_user.emailaddress
    primary_org.save
    output << "1707, 15511518," + admin_user.emailaddress + ",3e9bf6c1d7e04a2b"
    
    primary_org = PrimaryOrg.find(10802350)
    primary_org.externalid = 73
    primary_org.externalpassword = '275e6d80103eb45a'
    
    admin_user = User.find(:first, :conditions => { :admin => true, :tenant_id => primary_org.baseOrg.tenant_id })
    primary_org.externalusername=admin_user.emailaddress
    primary_org.save
    output << "73, 10802350," + admin_user.emailaddress + ",275e6d80103eb45a"
    
    primary_org = PrimaryOrg.find(15511516)
    primary_org.externalid = 430
    primary_org.externalpassword = '8574b98e3feeb400'
    
    admin_user = User.find(:first, :conditions => { :admin => true, :tenant_id => primary_org.baseOrg.tenant_id })
    primary_org.externalusername=admin_user.emailaddress
    primary_org.save
    output << "430, 15511516," + admin_user.emailaddress + ",8574b98e3feeb400"
    
    primary_org = PrimaryOrg.find(132385)
    primary_org.externalid = 9
    primary_org.externalpassword = 'ce07aed37967a297'
    
    admin_user = User.find(:first, :conditions => { :admin => true, :tenant_id => primary_org.baseOrg.tenant_id })
    primary_org.externalusername=admin_user.emailaddress
    primary_org.save
    output << "9, 132385," + admin_user.emailaddress + ",ce07aed37967a297"
    
    primary_org = PrimaryOrg.find(15511480)
    primary_org.externalid = 133
    primary_org.externalpassword = '8e6a1089a32d10d1'
    
    admin_user = User.find(:first, :conditions => { :admin => true, :tenant_id => primary_org.baseOrg.tenant_id })
    primary_org.externalusername=admin_user.emailaddress
    primary_org.save
    output << "133, 15511480," + admin_user.emailaddress + ",8e6a1089a32d10d1"
    
    primary_org = PrimaryOrg.find(15511555)
    primary_org.externalid = 98
    primary_org.externalpassword = '6dac1887a8d1f434'
    
    admin_user = User.find(:first, :conditions => { :admin => true, :tenant_id => primary_org.baseOrg.tenant_id })
    primary_org.externalusername=admin_user.emailaddress
    primary_org.save
    output << "98, 15511555," + admin_user.emailaddress + ",6dac1887a8d1f434"
    
    primary_org = PrimaryOrg.find(15511537)
    primary_org.externalid = 1153
    primary_org.externalpassword = 'fdc1b07520666089'
    
    admin_user = User.find(:first, :conditions => { :admin => true, :tenant_id => primary_org.baseOrg.tenant_id })
    primary_org.externalusername=admin_user.emailaddress
    primary_org.save
    output << "1153, 15511537," + admin_user.emailaddress + ",fdc1b07520666089"
    
    primary_org = PrimaryOrg.find(15511504)
    primary_org.externalid = 960
    primary_org.externalpassword = '7b153b5f17b06183'
    
    admin_user = User.find(:first, :conditions => { :admin => true, :tenant_id => primary_org.baseOrg.tenant_id })
    primary_org.externalusername=admin_user.emailaddress
    primary_org.save
    output << "960, 15511504," + admin_user.emailaddress + ",7b153b5f17b06183"
    
    primary_org = PrimaryOrg.find(10802351)
    primary_org.externalid = 110
    primary_org.externalpassword = 'e85a1b2f3b898d4d'
    
    admin_user = User.find(:first, :conditions => { :admin => true, :tenant_id => primary_org.baseOrg.tenant_id })
    primary_org.externalusername=admin_user.emailaddress
    primary_org.save
    output << "110, 10802351," + admin_user.emailaddress + ",e85a1b2f3b898d4d"
    
    primary_org = PrimaryOrg.find(15511500)
    primary_org.externalid = 621
    primary_org.externalpassword = '6a1b41c7cf827fec'
    
    admin_user = User.find(:first, :conditions => { :admin => true, :tenant_id => primary_org.baseOrg.tenant_id })
    primary_org.externalusername=admin_user.emailaddress
    primary_org.save
    output << "621, 15511500," + admin_user.emailaddress + ",6a1b41c7cf827fec"
    
    primary_org = PrimaryOrg.find(15511532)
    primary_org.externalid = 2525
    primary_org.externalpassword = '060c82289d3a3976'
    
    admin_user = User.find(:first, :conditions => { :admin => true, :tenant_id => primary_org.baseOrg.tenant_id })
    primary_org.externalusername=admin_user.emailaddress
    primary_org.save
    output << "2525, 15511532," + admin_user.emailaddress + ",060c82289d3a3976"
    
    primary_org = PrimaryOrg.find(10802300)
    primary_org.externalid = 93
    primary_org.externalpassword = '037ae774ea9e4ec5'
    
    admin_user = User.find(:first, :conditions => { :admin => true, :tenant_id => primary_org.baseOrg.tenant_id })
    primary_org.externalusername=admin_user.emailaddress
    primary_org.save
    output << "93, 10802300," + admin_user.emailaddress + ",037ae774ea9e4ec5"
    
    primary_org = PrimaryOrg.find(15511558)
    primary_org.externalid = 3169
    primary_org.externalpassword = '76105ef61f071ccd'
    
    admin_user = User.find(:first, :conditions => { :admin => true, :tenant_id => primary_org.baseOrg.tenant_id })
    primary_org.externalusername=admin_user.emailaddress
    primary_org.save
    output << "3169, 15511558," + admin_user.emailaddress + ",76105ef61f071ccd"
    
    primary_org = PrimaryOrg.find(15511564)
    primary_org.externalid = 4911
    primary_org.externalpassword = '05d21d47d4272531'
    
    admin_user = User.find(:first, :conditions => { :admin => true, :tenant_id => primary_org.baseOrg.tenant_id })
    primary_org.externalusername=admin_user.emailaddress
    primary_org.save
    output << "4911, 15511564," + admin_user.emailaddress + ",05d21d47d4272531"
    
    primary_org = PrimaryOrg.find(216044)
    primary_org.externalid = 12
    primary_org.externalpassword = '2b96bff999a699ff'
    
    admin_user = User.find(:first, :conditions => { :admin => true, :tenant_id => primary_org.baseOrg.tenant_id })
    primary_org.externalusername=admin_user.emailaddress
    primary_org.save
    output << "12, 216044," + admin_user.emailaddress + ",2b96bff999a699ff"
    
    primary_org = PrimaryOrg.find(15511540)
    primary_org.externalid = 765
    primary_org.externalpassword = '1ed745b06e8e96a8'
    
    admin_user = User.find(:first, :conditions => { :admin => true, :tenant_id => primary_org.baseOrg.tenant_id })
    primary_org.externalusername=admin_user.emailaddress
    primary_org.save
    output << "765, 15511540," + admin_user.emailaddress + ",1ed745b06e8e96a8"
    
    primary_org = PrimaryOrg.find(15511505)
    primary_org.externalid = 836
    primary_org.externalpassword = 'fa935e4fb5ef5bfe'
    
    admin_user = User.find(:first, :conditions => { :admin => true, :tenant_id => primary_org.baseOrg.tenant_id })
    primary_org.externalusername=admin_user.emailaddress
    primary_org.save
    output << "836, 15511505," + admin_user.emailaddress + ",fa935e4fb5ef5bfe"
    
    primary_org = PrimaryOrg.find(15511519)
    primary_org.externalid = 785
    primary_org.externalpassword = '5f4118b352431695'
    
    admin_user = User.find(:first, :conditions => { :admin => true, :tenant_id => primary_org.baseOrg.tenant_id })
    primary_org.externalusername=admin_user.emailaddress
    primary_org.save
    output << "785, 15511519," + admin_user.emailaddress + ",5f4118b352431695"
    
    primary_org = PrimaryOrg.find(15511544)
    primary_org.externalid = 3321
    primary_org.externalpassword = '17e427e22efda632'
    
    admin_user = User.find(:first, :conditions => { :admin => true, :tenant_id => primary_org.baseOrg.tenant_id })
    primary_org.externalusername=admin_user.emailaddress
    primary_org.save
    output << "3321, 15511544," + admin_user.emailaddress + ",17e427e22efda632"
    
    primary_org = PrimaryOrg.find(15511523)
    primary_org.externalid = 1696
    primary_org.externalpassword = '532a908d97275f5b'
    
    admin_user = User.find(:first, :conditions => { :admin => true, :tenant_id => primary_org.baseOrg.tenant_id })
    primary_org.externalusername=admin_user.emailaddress
    primary_org.save
    output << "1696, 15511523," + admin_user.emailaddress + ",532a908d97275f5b"
    
    primary_org = PrimaryOrg.find(15511483)
    primary_org.externalid = 13
    primary_org.externalpassword = 'f1aa565afbfed531'
    
    admin_user = User.find(:first, :conditions => { :admin => true, :tenant_id => primary_org.baseOrg.tenant_id })
    primary_org.externalusername=admin_user.emailaddress
    primary_org.save
    output << "13, 15511483," + admin_user.emailaddress + ",f1aa565afbfed531"
    
    primary_org = PrimaryOrg.find(15511495)
    primary_org.externalid = 574
    primary_org.externalpassword = 'c4b89a9169a91dbf'
    
    admin_user = User.find(:first, :conditions => { :admin => true, :tenant_id => primary_org.baseOrg.tenant_id })
    primary_org.externalusername=admin_user.emailaddress
    primary_org.save
    output << "574, 15511495," + admin_user.emailaddress + ",c4b89a9169a91dbf"
    
    primary_org = PrimaryOrg.find(15511503)
    primary_org.externalid = 113
    primary_org.externalpassword = 'c8be6b9a360f30f9'
    
    admin_user = User.find(:first, :conditions => { :admin => true, :tenant_id => primary_org.baseOrg.tenant_id })
    primary_org.externalusername=admin_user.emailaddress
    primary_org.save
    output << "113, 15511503," + admin_user.emailaddress + ",c8be6b9a360f30f9"
    
    primary_org = PrimaryOrg.find(15511549)
    primary_org.externalid = 1190
    primary_org.externalpassword = '8ff2603ad3307cbb'
    
    admin_user = User.find(:first, :conditions => { :admin => true, :tenant_id => primary_org.baseOrg.tenant_id })
    primary_org.externalusername=admin_user.emailaddress
    primary_org.save
    output << "1190, 15511549," + admin_user.emailaddress + ",8ff2603ad3307cbb"
    
    primary_org = PrimaryOrg.find(15511545)
    primary_org.externalid = 777
    primary_org.externalpassword = 'c81e5441e28ea68e'
    
    admin_user = User.find(:first, :conditions => { :admin => true, :tenant_id => primary_org.baseOrg.tenant_id })
    primary_org.externalusername=admin_user.emailaddress
    primary_org.save
    output << "777, 15511545," + admin_user.emailaddress + ",c81e5441e28ea68e"
    
    primary_org = PrimaryOrg.find(15511490)
    primary_org.externalid = 424
    primary_org.externalpassword = 'b2f5eead291e39fd'
    
    admin_user = User.find(:first, :conditions => { :admin => true, :tenant_id => primary_org.baseOrg.tenant_id })
    primary_org.externalusername=admin_user.emailaddress
    primary_org.save
    output << "424, 15511490," + admin_user.emailaddress + ",b2f5eead291e39fd"
    
    primary_org = PrimaryOrg.find(15511486)
    primary_org.externalid = 623
    primary_org.externalpassword = '9ad38fa3099329fa'
    
    admin_user = User.find(:first, :conditions => { :admin => true, :tenant_id => primary_org.baseOrg.tenant_id })
    primary_org.externalusername=admin_user.emailaddress
    primary_org.save
    output << "623, 15511486," + admin_user.emailaddress + ",9ad38fa3099329fa"
    
    primary_org = PrimaryOrg.find(10802250)
    primary_org.externalid = 11
    primary_org.externalpassword = '7293c5d28252e927'
    
    admin_user = User.find(:first, :conditions => { :admin => true, :tenant_id => primary_org.baseOrg.tenant_id })
    primary_org.externalusername=admin_user.emailaddress
    primary_org.save
    output << "11, 10802250," + admin_user.emailaddress + ",7293c5d28252e927"
    
    primary_org = PrimaryOrg.find(15511533)
    primary_org.externalid = 3166
    primary_org.externalpassword = 'b8bd0c437047324e'
    
    admin_user = User.find(:first, :conditions => { :admin => true, :tenant_id => primary_org.baseOrg.tenant_id })
    primary_org.externalusername=admin_user.emailaddress
    primary_org.save
    output << "3166, 15511533," + admin_user.emailaddress + ",b8bd0c437047324e"
    
    primary_org = PrimaryOrg.find(15511485)
    primary_org.externalid = 273
    primary_org.externalpassword = 'f89b1b3bfad4865f'
    
    admin_user = User.find(:first, :conditions => { :admin => true, :tenant_id => primary_org.baseOrg.tenant_id })
    primary_org.externalusername=admin_user.emailaddress
    primary_org.save
    output << "273, 15511485," + admin_user.emailaddress + ",f89b1b3bfad4865f"
    
    primary_org = PrimaryOrg.find(15511520)
    primary_org.externalid = 1140
    primary_org.externalpassword = '1cd1cf7bf65e8f11'
    
    admin_user = User.find(:first, :conditions => { :admin => true, :tenant_id => primary_org.baseOrg.tenant_id })
    primary_org.externalusername=admin_user.emailaddress
    primary_org.save
    output << "1140, 15511520," + admin_user.emailaddress + ",1cd1cf7bf65e8f11"
    
    primary_org = PrimaryOrg.find(15511565)
    primary_org.externalid = 3300
    primary_org.externalpassword = '5728a2d636c9ac7b'
    
    admin_user = User.find(:first, :conditions => { :admin => true, :tenant_id => primary_org.baseOrg.tenant_id })
    primary_org.externalusername=admin_user.emailaddress
    primary_org.save
    output << "3300, 15511565," + admin_user.emailaddress + ",5728a2d636c9ac7b"
    
    primary_org = PrimaryOrg.find(15511513)
    primary_org.externalid = 5914
    primary_org.externalpassword = '550c6b928d6d39cc'
    
    admin_user = User.find(:first, :conditions => { :admin => true, :tenant_id => primary_org.baseOrg.tenant_id })
    primary_org.externalusername=admin_user.emailaddress
    primary_org.save
    output << "5914, 15511513," + admin_user.emailaddress + ",550c6b928d6d39cc"
    
    primary_org = PrimaryOrg.find(15511563)
    primary_org.externalid = 127
    primary_org.externalpassword = 'eeaef046c1750ca3'
    
    admin_user = User.find(:first, :conditions => { :admin => true, :tenant_id => primary_org.baseOrg.tenant_id })
    primary_org.externalusername=admin_user.emailaddress
    primary_org.save
    output << "127, 15511563," + admin_user.emailaddress + ",eeaef046c1750ca3"
    
    
    
    primary_org = PrimaryOrg.find(15511522)
    primary_org.externalid = 1693
    primary_org.externalpassword = '2a053885ff38b3ae'
    
    admin_user = User.find(:first, :conditions => { :admin => true, :tenant_id => primary_org.baseOrg.tenant_id })
    primary_org.externalusername=admin_user.emailaddress
    primary_org.save
    output << "1693, 15511522," + admin_user.emailaddress + ",2a053885ff38b3ae"
    
    primary_org = PrimaryOrg.find(15511541)
    primary_org.externalid = 1192
    primary_org.externalpassword = 'ed6e0fa168b3227e'
    
    admin_user = User.find(:first, :conditions => { :admin => true, :tenant_id => primary_org.baseOrg.tenant_id })
    primary_org.externalusername=admin_user.emailaddress
    primary_org.save
    output << "1192, 15511541," + admin_user.emailaddress + ",ed6e0fa168b3227e"
    
    primary_org = PrimaryOrg.find(10802400)
    primary_org.externalid = 74
    primary_org.externalpassword = '81d746ff0991d31b'
    
    admin_user = User.find(:first, :conditions => { :admin => true, :tenant_id => primary_org.baseOrg.tenant_id })
    primary_org.externalusername=admin_user.emailaddress
    primary_org.save
    output << "74, 10802400," + admin_user.emailaddress + ",81d746ff0991d31b"
    
    primary_org = PrimaryOrg.find(15511521)
    primary_org.externalid = 1142
    primary_org.externalpassword = '065d02cfe47ead41'
    
    admin_user = User.find(:first, :conditions => { :admin => true, :tenant_id => primary_org.baseOrg.tenant_id })
    primary_org.externalusername=admin_user.emailaddress
    primary_org.save
    output << "1142, 15511521," + admin_user.emailaddress + ",065d02cfe47ead41"
    
    primary_org = PrimaryOrg.find(15511497)
    primary_org.externalid = 987
    primary_org.externalpassword = 'b9df61a86f724d5b'
    
    admin_user = User.find(:first, :conditions => { :admin => true, :tenant_id => primary_org.baseOrg.tenant_id })
    primary_org.externalusername=admin_user.emailaddress
    primary_org.save
    output << "987, 15511497," + admin_user.emailaddress + ",b9df61a86f724d5b"
    
    primary_org = PrimaryOrg.find(15511568)
    primary_org.externalid = 5123
    primary_org.externalpassword = 'e6723c7145a8ac76'
    
    admin_user = User.find(:first, :conditions => { :admin => true, :tenant_id => primary_org.baseOrg.tenant_id })
    primary_org.externalusername=admin_user.emailaddress
    primary_org.save
    output << "5123, 15511568," + admin_user.emailaddress + ",e6723c7145a8ac76"
    
    primary_org = PrimaryOrg.find(15511492)
    primary_org.externalid = 121
    primary_org.externalpassword = 'c032587d97734dec'
    
    admin_user = User.find(:first, :conditions => { :admin => true, :tenant_id => primary_org.baseOrg.tenant_id })
    primary_org.externalusername=admin_user.emailaddress
    primary_org.save
    output << "121, 15511492," + admin_user.emailaddress + ",c032587d97734dec"
    
    primary_org = PrimaryOrg.find(15511453)
    primary_org.externalid = 118
    primary_org.externalpassword = '521a04ff13d241b4'
    
    admin_user = User.find(:first, :conditions => { :admin => true, :tenant_id => primary_org.baseOrg.tenant_id })
    primary_org.externalusername=admin_user.emailaddress
    primary_org.save
    output << "118, 15511453," + admin_user.emailaddress + ",521a04ff13d241b4"
    
    primary_org = PrimaryOrg.find(2)
    primary_org.externalid = 6
    primary_org.externalpassword = '887309d048beef83'
    
    admin_user = User.find(:first, :conditions => { :admin => true, :tenant_id => primary_org.baseOrg.tenant_id })
    primary_org.externalusername=admin_user.emailaddress
    primary_org.save
    output << "6, 2," + admin_user.emailaddress + ",887309d048beef83"
    
    primary_org = PrimaryOrg.find(15511488)
    primary_org.externalid = 25
    primary_org.externalpassword = 'e0d263e9e49adeb7'
    
    admin_user = User.find(:first, :conditions => { :admin => true, :tenant_id => primary_org.baseOrg.tenant_id })
    primary_org.externalusername=admin_user.emailaddress
    primary_org.save
    output << "25, 15511488," + admin_user.emailaddress + ",e0d263e9e49adeb7"
    
    primary_org = PrimaryOrg.find(15511524)
    primary_org.externalid = 1698
    primary_org.externalpassword = '125455fa57318377'
    
    admin_user = User.find(:first, :conditions => { :admin => true, :tenant_id => primary_org.baseOrg.tenant_id })
    primary_org.externalusername=admin_user.emailaddress
    primary_org.save
    output << "1698, 15511524," + admin_user.emailaddress + ",125455fa57318377"
    
    primary_org = PrimaryOrg.find(15511554)
    primary_org.externalid = 3802
    primary_org.externalpassword = 'b02c307fb357623f'
    
    admin_user = User.find(:first, :conditions => { :admin => true, :tenant_id => primary_org.baseOrg.tenant_id })
    primary_org.externalusername=admin_user.emailaddress
    primary_org.save
    output << "3802, 15511554," + admin_user.emailaddress + ",b02c307fb357623f"
    
    primary_org = PrimaryOrg.find(4)
    primary_org.externalid = 8
    primary_org.externalpassword = '64e095fe763fc624'
    
    admin_user = User.find(:first, :conditions => { :admin => true, :tenant_id => primary_org.baseOrg.tenant_id })
    primary_org.externalusername=admin_user.emailaddress
    primary_org.save
    output << "8, 4," + admin_user.emailaddress + ",64e095fe763fc624"
    
    primary_org = PrimaryOrg.find(15511498)
    primary_org.externalid = 748
    primary_org.externalpassword = 'ccc440ccb958fbf2'
    
    admin_user = User.find(:first, :conditions => { :admin => true, :tenant_id => primary_org.baseOrg.tenant_id })
    primary_org.externalusername=admin_user.emailaddress
    primary_org.save
    output << "748, 15511498," + admin_user.emailaddress + ",ccc440ccb958fbf2"
    
    
    output_file = File.open(File.dirname(__FILE__) + "/fieldid_to_netsuite_mapping/mapping.csv","w")
    output.each do |line|
      output_file.write(line + "\n")
    end
    output_file.close
    
  end
end