CREATE TABLE `sudo_permissions` (
  `id`            BIGINT(21) NOT NULL AUTO_INCREMENT,
  `admin_user_id` BIGINT(21) NOT NULL,
  `user_id`       BIGINT(21) NOT NULL,
  `created`       DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `sudo_permissions_admin_user_to_user` (`admin_user_id`, `user_id`),
  FOREIGN KEY (`admin_user_id`) REFERENCES `admin_users` (`id`),
  FOREIGN KEY (`user_id`)       REFERENCES `users` (`id`)
);