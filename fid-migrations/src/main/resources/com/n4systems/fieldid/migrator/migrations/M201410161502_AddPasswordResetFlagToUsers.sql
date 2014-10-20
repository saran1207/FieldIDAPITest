alter table users  ADD (
  resetEmailSent tinyint(1)
);

UPDATE users SET resetEmailSent = false;