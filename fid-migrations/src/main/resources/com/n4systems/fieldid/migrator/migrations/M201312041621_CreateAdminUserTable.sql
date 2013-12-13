CREATE TABLE `admin_users` (
  `id`        BIGINT(21) NOT NULL AUTO_INCREMENT,
  `enabled`   BIT(1) NOT NULL DEFAULT 1,
  `type`      VARCHAR(48) NOT NULL,
  `email`     VARCHAR(255) NOT NULL,
  `password`  CHAR(128) NOT NULL,
  `salt`      CHAR(128) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY `admin_users_email` (`email`)
);

insert into admin_users (enabled, type, email, password, salt) values (true, 'SUPER', 'dev@fieldid.com', 'aefe443ae67560fb597d41a920f293469aa634abd7085a8d1edac07a35dc8ea7101f9856cea543728cd6d579623906e9a09d54bec0f2bb78017fbb92f690a670', 'ba37f6794e3e59cd6d3ab41021599847b340f3924d961baf2c1627e6ac2c2058da233cbb0af752626b897132131cb2237b2688e99ce803c051355f5c80fe5051');
insert into admin_users (enabled, type, email, password, salt) values (true, 'SUPER', 'fieldid@fieldid.com', '43ec98159dec460179b2979753739a8b3eb49ed1501f5b9e66b9ca83f9c254f0845bb7f3b186efc2ea4527b77308a9bbc85acd1652658d77aa957f9d50344d84', '3c7cdd6d5e3adca6f2df9a4588810c6906b56f1fdec50747575d6d3fdaf1d89a50a6c491c62bdc18aac789645931146b756852c6dc54fef62d6a90d5ed955c1f');
