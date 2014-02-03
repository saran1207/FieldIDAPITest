ALTER TABLE users ADD COLUMN last_login datetime DEFAULT NULL;
update users u join activesessions a on a.user_id = u.id set u.last_login = a.datecreated;