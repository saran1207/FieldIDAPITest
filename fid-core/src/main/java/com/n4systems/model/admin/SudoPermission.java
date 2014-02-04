package com.n4systems.model.admin;

import com.n4systems.model.BaseEntity;
import com.n4systems.model.api.UnsecuredEntity;
import com.n4systems.model.user.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "sudo_permissions")
public class SudoPermission extends BaseEntity implements UnsecuredEntity {

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "admin_user_id", nullable = false)
	private AdminUser adminUser;

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created", nullable = false)
	private Date created;

	@Override
	protected void onCreate() {
		super.onCreate();
		created = new Date();
	}

	public AdminUser getAdminUser() {
		return adminUser;
	}

	public void setAdminUser(AdminUser adminUser) {
		this.adminUser = adminUser;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
}
