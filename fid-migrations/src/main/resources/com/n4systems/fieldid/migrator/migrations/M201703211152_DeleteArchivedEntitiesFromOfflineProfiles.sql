DELETE o FROM offline_profiles_assets o JOIN assets a ON o.assets = a.mobileguid WHERE a.state ='ARCHIVED';

DELETE o FROM offline_profiles_orgs o JOIN org_base b ON o.organizations = b.id WHERE b.state='ARCHIVED';