package com.n4systems.model.admin;

import com.n4systems.model.BaseEntity;
import com.n4systems.model.api.UnsecuredEntity;
import com.n4systems.persistence.localization.Localized;

import javax.persistence.*;

@Entity
@Table(name = "admin_users")
@Localized(ignore = true)
public class AdminUser extends BaseEntity implements UnsecuredEntity {

	@Column(nullable = false)
	private boolean enabled;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AdminUserType type;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false, length = 128)
	private char[] password;

	@Column(nullable = false, length = 128)
	private char[] salt;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public AdminUserType getType() {
		return type;
	}

	public void setType(AdminUserType type) {
		this.type = type;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public char[] getPassword() {
		return password;
	}

	public void setPassword(char[] password) {
		this.password = password;
	}

	public char[] getSalt() {
		return salt;
	}

	public void setSalt(char[] salt) {
		this.salt = salt;
	}
}
