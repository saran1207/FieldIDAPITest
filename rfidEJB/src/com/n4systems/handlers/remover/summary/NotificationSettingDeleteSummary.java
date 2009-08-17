package com.n4systems.handlers.remover.summary;

public class NotificationSettingDeleteSummary extends RemovalSummary {

	private Long notificationsToDelete;
	
	public NotificationSettingDeleteSummary() {
		this(0L);
	}
	
	public NotificationSettingDeleteSummary(Long notifationsToDelete) {
		super();
		this.notificationsToDelete = notifationsToDelete;
	}

	@Override
	public boolean canBeRemoved() {
		return true;
	}

	public Long getNotificationsToDelete() {
		return notificationsToDelete;
	}

	public void setNotificationsToDelete(Long notificationsToDelete) {
		this.notificationsToDelete = notificationsToDelete;
	}
	
	public void setNotificationsToDelete(int notificationsToDelete) {
		setNotificationsToDelete(new Long(notificationsToDelete));
	}

	
}
